/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.model.Document;
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
    private HashMap<String, Integer> documentOccurrences; //dict for whole document
    public TextFileReader() {
        this.commonWords = new HashMap<>();
        this.documentOccurrences = new HashMap<>();
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
    public Document readFile(Rectangle bounds) throws IOException {
        ArrayList<Page> pages = new ArrayList<>(); // The list of all the pages
        ArrayList<WordCloud> wordClouds = new ArrayList();
        ArrayList<ArrayList<WordCloud>> wordCloudLevels = new ArrayList<ArrayList<WordCloud>>();
       
        // Text Wrapper
        double boundWidth = bounds.getWidth(); // Width of the page
        double boundHeight = bounds.getHeight(); // Height of the page
        double spaceWidth = new Text(" ").getBoundsInLocal().getWidth(); // Width of a space
        double lineHeight = new Text("").getBoundsInLocal().getHeight(); // Height of a line
        double spaceLeft = bounds.getWidth(); // Width of the page

        Text tempPage = new Text("");
        String pageText = "";
        
        HashMap<String, Integer> wordsOccurrences = new HashMap<>(); //dict for a single page
        try (LineNumberReader r = new LineNumberReader(new java.io.FileReader(file))) {
            String paragraph, word;
            while ((paragraph = r.readLine()) != null) {
                try (Scanner sc = new Scanner(paragraph)) {
                    while (sc.hasNext()) { // while there is words in the line
                        word = sc.next(); 
                        double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                        double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                        if (textWithNewLine > boundHeight) {
                            Page page = new Page(pageText);
                            WordCloud wordCloud = new WordCloud(wordsOccurrences, this.documentOccurrences);
                            //System.out.println(""+page.toString());
                            pages.add(page);
                            wordClouds.add(wordCloud);
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
   
            
            Page page = new Page(pageText);
            WordCloud wordCloud = new WordCloud(wordsOccurrences, this.documentOccurrences);
            //System.out.println(""+page.toString());
            pages.add(page);
            wordClouds.add(wordCloud);           
        }
        wordCloudLevels.add(wordClouds);// add the first level
        
        //add each level of merged clouds
        for (ArrayList<WordCloud> tmpList : makeCloudLevels(wordClouds)){
            wordCloudLevels.add(tmpList);
        }
   
        System.out.println("number of levels:" + wordCloudLevels.size());
        Document document = new Document(pages, wordCloudLevels);
        return document;
    }
    
    private void addToDocumentMap(HashMap<String, Integer> cloud){
        for (String word : cloud.keySet()){
            if (this.documentOccurrences.containsKey(word)){
                int count = this.documentOccurrences.get(word) + cloud.get(word);
                this.documentOccurrences.put(word, count);
            }
            else{
                this.documentOccurrences.put(word, cloud.get(word));
            }
        }
        
    }

    
    private ArrayList<ArrayList<WordCloud>> makeCloudLevels(ArrayList<WordCloud> clouds){
        ArrayList<ArrayList<WordCloud>> localLevels = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<ArrayList<WordCloud>> otherLevels = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<WordCloud> currentLevel = new ArrayList<WordCloud>();
        WordCloud cloudB = null;
        boolean haveB = false;
        int listCount = 0;
        int lastIndex = clouds.size() -1;
        if (lastIndex == 0){  // only one cloud in array
           // return clouds;
            localLevels.add(clouds);
            return localLevels;
            
        }
      
        for (WordCloud cloud : clouds){
            
             if (haveB && (cloudB!= null)){      
                 WordCloud newCloud = new WordCloud(cloud, cloudB);
                 if (clouds.indexOf(cloud) + 1 == lastIndex){
                    WordCloud triCloud = new WordCloud(newCloud, clouds.get(lastIndex));
                    currentLevel.add(triCloud);
                 }
                 else{
                currentLevel.add(newCloud);   
                 }
                haveB = false;
            }
             else{
                 cloudB = cloud;
                 haveB = true;
             }
            
        }
        localLevels.add(currentLevel);
        otherLevels = makeCloudLevels(currentLevel);
        for (ArrayList<WordCloud> cloudList : otherLevels){ // add otherLevels to localLevels
            localLevels.add(cloudList);
        }
        return localLevels;
        
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

    
    //removes punctuation from any words found
    private String trimPunctuation(String word) {
        return word.toLowerCase().replaceAll("\\W", "");
    }
}