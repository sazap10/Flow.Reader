/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.view.PageView;
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
public class TextFileReader extends Task {

    private File file;
    private HashMap<String, Integer> commonWords;

    public TextFileReader() {
        this.commonWords = new HashMap<String, Integer>();
        this.getCommonWords();
    }
    
    public TextFileReader(File file) {
        this.commonWords = new HashMap<String, Integer>();
        this.getCommonWords();
        this.file = file;
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
            int nbligne = 0;
            while ((paragraph = r.readLine()) != null) {
                nbligne++;
                this.updateProgress(nbligne, numberOfLines + 1);
                Scanner sc = new Scanner(paragraph);
                try {
                    while (sc.hasNext()) { // while there is words in the line
                        word = sc.next();
                        double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                        double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                        if (textWithNewLine > boundHeight) {
                            Page page = new Page(pageText);
                            WordCloud wordCloud = new WordCloud(wordsOccurrences);
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

        Document document = new Document(pages, wordCloudLevels);
        return document;
    }

    /**
     * @param clouds
     * @return all the levels of word clouds based on the first level call clouds
     */
    public ArrayList<ArrayList<WordCloud>> makeCloudLevels(ArrayList<WordCloud> clouds) {
        ArrayList<ArrayList<WordCloud>> localLevels = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<ArrayList<WordCloud>> otherLevels = new ArrayList<ArrayList<WordCloud>>();
        //updateProgress(1, 10);
        localLevels.add(clouds);

        ArrayList<WordCloud> currentLevel = new ArrayList<WordCloud>();
        WordCloud cloudB = null;
        boolean haveB = false;
        int listCount = 0;
        int lastIndex = clouds.size() - 1;

        // there is only one word cloud on the previous level
        if (lastIndex == 0) {
            //localLevels.add(clouds);
            return localLevels;
        }

        // Get through the previous level of cloud to create the current level
        for (WordCloud cloud : clouds) {
            if (haveB && (cloudB != null)) {
                WordCloud newCloud = new WordCloud(cloud, cloudB);
                // if odd number of cloud merge of three clouds
                if (clouds.indexOf(cloud) + 1 == lastIndex) {
                    WordCloud triCloud = new WordCloud(newCloud, clouds.get(lastIndex));
                    currentLevel.add(triCloud);
                } else { // else add the new cloud to the level
                    currentLevel.add(newCloud);
                }
                haveB = false;
            } else {
                cloudB = cloud;
                haveB = true;
            }

        }
        otherLevels = makeCloudLevels(currentLevel);
        for (ArrayList<WordCloud> cloudList : otherLevels) { // add otherLevels to localLevels
            localLevels.add(cloudList);
        }
        return localLevels;
    }

    /**
     * @return an hashmap containing all the common words 
     */
    public final HashMap<String, Integer> getCommonWords() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream f;
            f = TextFileReader.class.getResourceAsStream("CommonEnglishWords.txt");

            bufferedReader = new BufferedReader(new InputStreamReader(f));

            String temp_text;
            while ((temp_text = bufferedReader.readLine()) != null) {
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
        return this.commonWords;
    }

    /**
     * @param word
     * @return word without any punctuation
     */
    public String trimPunctuation(String word) {
        String w = word.toLowerCase().replaceAll("\\W", "");
        return w;
    }

    @Override
    protected Document call() throws Exception {
        Document docu = readFile(PageView.textBoundWidth, PageView.textBoundHeight);
        return docu;
    }
}