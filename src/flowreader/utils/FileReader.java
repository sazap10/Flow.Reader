/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.model.WordCloud;
import flowreader.view.TextPageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Jim
 */
public abstract class FileReader extends Task {

    protected File file;
    protected HashMap<String, Integer> commonWords;
    protected Document document;

    public FileReader() {
        this.commonWords = new HashMap<String, Integer>();
        this.getCommonWords();
    }

    public abstract Document readFile(double width, double height) throws IOException;

    @Override
    public Document call() throws IOException {
        Document docu = readFile(TextPageView.textBoundWidth, TextPageView.textBoundHeight);
        return docu;
    }

    public void setFile(File file) {
        this.file = file;
    }

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

    public String trimPunctuation(String word) {
        String w = word.toLowerCase().replaceAll("\\W", "");
        return w;
    }
}
