/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Page;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author D-Day
 */
public class TextFileReader{

    private File file;
    private HashMap<String, Integer> commonWords;

    public TextFileReader() {
        this.commonWords = new HashMap<>();
        this.getCommonWords();
    }

    public void startFileChooser(Stage primaryStage) {
        //start file chooser
        File f = new File(System.getProperty("user.dir"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please choose a text file to read");
        fileChooser.setInitialDirectory(f);

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        file = fileChooser.showOpenDialog(primaryStage);

    }

    /**
     * @param bounds the boundaries of the page used to know how much text contains a page
     * @return a list of pages that contains the text of each page and the words occurrences
     * @throws IOException 
     */
    public ArrayList<Page> readFile(Rectangle bounds) throws IOException {
        ArrayList<Page> pages = new ArrayList<>(); // The list of all the pages
        
        // Text Wrapper
        double boundWidth = bounds.getWidth(); // Width of the page
        double boundHeight = bounds.getHeight(); // Height of the page
        double spaceWidth = new Text(" ").getBoundsInLocal().getWidth(); // Width of a space
        double lineHeight = new Text("").getBoundsInLocal().getHeight(); // Height of a line
        double spaceLeft = bounds.getWidth(); // Width of the page

        Text tempPage = new Text("");
        String pageText = "";
        HashMap<String, Integer> wordsOccurrences = new HashMap<>();
        try (LineNumberReader r = new LineNumberReader(new java.io.FileReader(file))) {
            String paragraph, word;
            while ((paragraph = r.readLine()) != null) {
                try (Scanner sc = new Scanner(paragraph)) {
                    while (sc.hasNext()) { // while there is words in the line
                        word = sc.next(); 
                        double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                        double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                        if (textWithNewLine > boundHeight) {
                            TreeMap<String, Integer> wordsOccurrencesSorted = this.sortWordsOccurrences(wordsOccurrences);
                            Page page = new Page(pageText, wordsOccurrencesSorted);
                            //System.out.println(""+page.toString());
                            pages.add(page);
                            pageText = "";
                            wordsOccurrences = new HashMap<>();
                        }
                        if (wordWidth + spaceWidth > spaceLeft) {
                            if (!(textWithNewLine > boundHeight)) {
                                pageText += "\n";
                            }
                            spaceLeft = boundWidth - wordWidth;
                        } else {
                            spaceLeft = spaceLeft - (wordWidth + spaceWidth);
                        }
                        pageText += word + " ";
                        word = this.trimPunctuation(word);
                        if (!this.commonWords.containsKey(word)) {
                            if(wordsOccurrences.get(word)!=null){
                                wordsOccurrences.put(word, wordsOccurrences.get(word)+1);
                            }
                            else{
                                wordsOccurrences.put(word, 1);
                            }
                        }
                        tempPage.setText(pageText);
                    }
                    if (!((tempPage.getBoundsInLocal().getHeight() + lineHeight) > boundHeight)) {
                        pageText += "\n";
                        tempPage.setText(pageText);
                        spaceLeft = boundWidth;
                    }
                }
            }
            TreeMap<String, Integer> wordsOccurrencesSorted = this.sortWordsOccurrences(wordsOccurrences);
            Page page = new Page(pageText, wordsOccurrencesSorted);
            //System.out.println(""+page.toString());
            pages.add(page);
        }
        return pages;
    }

    private void getCommonWords() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(System.getProperty("user.dir")+System.getProperty("file.separator")+"CommonEnglishWords.txt"));
            
            String temp_text;
            while ((temp_text = bufferedReader.readLine()) != null) {
                //System.out.println(temp_text);
                this.commonWords.put(temp_text, 1);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't find the file!");
        } catch (IOException ex) {
            System.out.println("no idea!.. some IOException");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                System.out.println("couldn't close the file!");
            }
        }
    }
    
    private TreeMap<String, Integer> sortWordsOccurrences(HashMap<String, Integer> wordsOccurrences){
        ValueComparator bvc =  new ValueComparator(wordsOccurrences);
        TreeMap<String,Integer> sortedWordsOccurrences = new TreeMap<>(bvc);
        sortedWordsOccurrences.putAll(wordsOccurrences);
        
        //System.out.println("unsorted map: "+wordsOccurrences);
        //System.out.println("results: "+sortedWordsOccurrences);
       
        return sortedWordsOccurrences;
    }
    
    //removes punctuation from any words found
    private String trimPunctuation(String word) {
        return word.toLowerCase().replaceAll("\\W", "");
    }
}
