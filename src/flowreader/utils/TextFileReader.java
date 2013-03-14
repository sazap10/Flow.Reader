/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.view.TxtPageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.concurrent.Task;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author D-Day
 */
public class TextFileReader extends Reader implements FileReader {

  

    public TextFileReader() {
        this.commonWords = new HashMap<String, Integer>();
        this.getCommonWords();
    }
    


    /**
     * Create and start the file chooser
     * @param primaryStage 
     */
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
     * @param bounds the boundaries of the page used to know how much text
     * contains a page
     * @return a document based on the file File
     * @throws IOException
     */
    public Document readFile(double width, double height) throws IOException {
        ArrayList<Page> pages = new ArrayList<Page>(); // The list of all the pages
        ArrayList<WordCloud> wordClouds = new ArrayList();
        ArrayList<ArrayList<WordCloud>> wordCloudLevels = new ArrayList<ArrayList<WordCloud>>();
        // Text Wrapper
        double boundWidth = width; // Width of the page
        double boundHeight = height; // Height of the page
        double spaceWidth = new Text(" ").getBoundsInLocal().getWidth(); // Width of a space
        double lineHeight = new Text("").getBoundsInLocal().getHeight(); // Height of a line
        double spaceLeft = width; // Width of the page

        Text tempPage = new Text("");
        String pageText = "";

        HashMap<String, Integer> wordsOccurrences = new HashMap<String, Integer>();
        LineNumberReader lnr = new LineNumberReader(new java.io.FileReader(file));
        lnr.skip(Long.MAX_VALUE);
        int numberOfLines = lnr.getLineNumber();
        LineNumberReader r = new LineNumberReader(new java.io.FileReader(file));
        try {
            String paragraph, word;
            int pageCount = 0;
            while ((paragraph = r.readLine()) != null) {
                pageCount++;
                this.updateProgress(pageCount, numberOfLines + 1);
                Scanner sc = new Scanner(paragraph);
                try {
                    while (sc.hasNext()) { // while there is words in the line
                        word = sc.next();
                        double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                        double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                        if (textWithNewLine > boundHeight) {
                            Page page = new Page(pageText);
                            WordCloud wordCloud = new WordCloud(wordsOccurrences);
                            System.out.println("adding page"+page.getText());
                            pages.add(page);
                            wordClouds.add(wordCloud);
                            pageText = "";
                            wordsOccurrences = new HashMap<String, Integer>();
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
                            if (wordsOccurrences.get(word) != null) {
                                wordsOccurrences.put(word, wordsOccurrences.get(word) + 1);
                            } else {
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
                } finally {
                    if (sc != null) {
                        sc.close();
                    }
                }

            }
            Page page = new Page(pageText);
            WordCloud wordCloud = new WordCloud(wordsOccurrences);
            System.out.println("adding page"+page.getText());
            pages.add(page);
            wordClouds.add(wordCloud);

        } finally {
            if (r != null) {
                r.close();
            }
        }

        ArrayList<ArrayList<WordCloud>> temp_list = makeCloudLevels(wordClouds);

        for (ArrayList<WordCloud> temp_element : temp_list) {
            wordCloudLevels.add(temp_element);
        }

        System.out.println("nbpages "+pages.size());
        Document document = new Document(pages, wordCloudLevels);
        return document;
    }

    /**
     * @param clouds
     * @return all the levels of word clouds based on the first level call clouds
     */
   

    /**
     * @return an hashmap containing all the common words 
     */
   

    /**
     * @param word
     * @return word without any punctuation
     */

    
        @Override
    public Document call() throws IOException {
        Document docu = readFile(TxtPageView.textBoundWidth, TxtPageView.textBoundHeight);
        return docu;
    }
    

    
}