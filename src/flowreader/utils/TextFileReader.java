/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.view.MainView;
import flowreader.view.PageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author D-Day
 */
public class TextFileReader extends Task {

    private File file;
    private HashMap<String, Integer> commonWords;
    private Document doc;
    private MainView mv;
    private ProgressIndicator pi;

    public TextFileReader(MainView mv, ProgressIndicator pi) {
        this.commonWords = new HashMap<String, Integer>();
        this.getCommonWords();
        this.mv = mv;
        this.pi = pi;
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
     * @param bounds the boundaries of the page used to know how much text
     * contains a page
     * @return a list of pages that contains the text of each page and the words
     * occurrences
     * @throws IOException
     */
    public Document readFile(double width, double height) throws IOException {
        System.out.println("Debut readfile");
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
        //System.out.println(numberOfLines);
        LineNumberReader r = new LineNumberReader(new java.io.FileReader(file));
        try{
            String paragraph, word;
            int nbligne = 0;
            while ((paragraph = r.readLine()) != null) {
                nbligne++;
                this.updateProgress(nbligne, numberOfLines+1);
                //System.out.println(nbligne);
                Scanner sc = new Scanner(paragraph);
                try{
                    while (sc.hasNext()) { // while there is words in the line
                        word = sc.next();
                        /*if(this.trimPunctuation(word).equals("apple")){
                         System.out.println(word+" "+this.trimPunctuation(word));
                         }*/
                        double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                        double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                        if (textWithNewLine > boundHeight) {
                            Page page = new Page(pageText);
                            WordCloud wordCloud = new WordCloud(wordsOccurrences);
                            //System.out.println(""+page.toString());
                            pages.add(page);
                            wordClouds.add(wordCloud);
                            pageText = "";
                            //System.out.println("Apple: "+wordsOccurrences.get("apple"));
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
                }
                finally{
                    if(sc!=null){
                        sc.close();
                    }
                }
                            
            }
            //System.out.println("Apple: "+wordsOccurrences.get("apple"));
            Page page = new Page(pageText);
            WordCloud wordCloud = new WordCloud(wordsOccurrences);
            //System.out.println(""+page.toString());
            pages.add(page);
            wordClouds.add(wordCloud);

        }
        finally{
            if(r!=null){
                r.close();
            }
        }
        //wordCloudLevels.add(wordClouds);// add the first level

        ArrayList<ArrayList<WordCloud>> temp_list = makeCloudLevels(wordClouds);

        for (ArrayList<WordCloud> temp_element : temp_list) {
            wordCloudLevels.add(temp_element);
        }

        //System.out.println("number of levels:" + wordCloudLevels.size());
        
        Document document = new Document(pages, wordCloudLevels);
        System.out.println("fin readfile");
        return document;
    }

    private ArrayList<ArrayList<WordCloud>> makeCloudLevels(ArrayList<WordCloud> clouds) {
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

    private void getCommonWords() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream f;
            f = TextFileReader.class.getResourceAsStream("CommonEnglishWords.txt");
//File f = new File("CommonEnglishWords.txt");
//System.out.println("xx"+f.getAbsolutePath());

            bufferedReader = new BufferedReader(new InputStreamReader(f));

            String temp_text;
            while ((temp_text = bufferedReader.readLine()) != null) {
                //System.out.println(temp_text);
                this.commonWords.put(temp_text, 1);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't find the file!");
            
           
        } catch (IOException ex) {
            System.out.println("no idea!.. some IOException");
        }finally {
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
        String w = word.toLowerCase().replaceAll("\\W", "");
        return w;
    }

    @Override
    protected Document call() throws Exception {
        Document docu = readFile(PageView.textBoundWidth, PageView.textBoundHeight);
        return docu;
    }
}