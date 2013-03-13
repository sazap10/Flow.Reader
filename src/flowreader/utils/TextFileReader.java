/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class TextFileReader extends FileReader{

    public TextFileReader() {
        super();
    }
    
    public TextFileReader(File file) {
        super(file);
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

}