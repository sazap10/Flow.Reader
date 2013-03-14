/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.model.PDFPage;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.view.MainView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author D-Day
 */
public class PDFFileReader extends FileReader {

    public PDFFileReader(File file) {
        super(file);
    }

    /**
     * @param bounds the boundaries of the page used to know how much text
     * contains a page
     * @return a list of pages that contains the text of each page and the words
     * occurrences
     * @throws IOException
     */
    public Document readFile(double a, double b) throws IOException {
        ArrayList<String> pageText;
        ArrayList<ArrayList<WordCloud>> clouds = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<Page> pages = new ArrayList<>();
        try {
            if (file != null) {

                PDDocument pdfDocument = PDDocument.load(file);
                pageText = getText(pdfDocument, pdfDocument.getNumberOfPages());
                System.out.println(pdfDocument.getNumberOfPages());
                for (int i = 0; i < pdfDocument.getNumberOfPages(); i++) {
                    PDPage pDPage = (PDPage) pdfDocument.getDocumentCatalog().getAllPages().get(i);
                    WritableImage image = this.BufferedToWritable(pDPage.convertToImage(BufferedImage.TYPE_USHORT_555_RGB, 60));
                    pages.add(new PDFPage(pageText.get(i), image));
                    System.out.println("got an image for " + i);
                    this.updateProgress(i, pdfDocument.getNumberOfPages());
                }
                clouds = makeWordClouds(pageText);

                pdfDocument.close();
                return new Document(pages, clouds);
            } else {
                System.out.println("file is null");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("about to call document constructor");

        System.out.println("ok here");
        return document;

    }

    //converts a buffered image to a writeable image
    public WritableImage BufferedToWritable(BufferedImage bf) {
        WritableImage wr = null;
        if (bf != null) {
            wr = new WritableImage(bf.getWidth(), bf.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bf.getWidth(); x++) {
                for (int y = 0; y < bf.getHeight(); y++) {
                    pw.setArgb(x, y, bf.getRGB(x, y));
                }
            }
        }

        return wr;
    }

    //returns an indexed array of strings for each page of the input document
    public ArrayList<String> getText(PDDocument document, int numOfPages) throws IOException {
        ArrayList<String> pages = new ArrayList<>();
        ByteArrayOutputStream bout;
        OutputStreamWriter writer;
        try {
            //set the buffer
            bout = new ByteArrayOutputStream();
            writer = new OutputStreamWriter(bout);

            //strip the document to the buffer 


            PDFTextStripper stripper = new PDFTextStripper();
            for (int i = 1; i <= numOfPages; i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i + 1);
                stripper.writeText(document, writer);
                bout.flush();
                writer.flush();
                String page = bout.toString();
                System.out.println("page " + i + "finished");
                pages.add(page);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    //creates wordClouds from page text
    public ArrayList<ArrayList<WordCloud>> makeWordClouds(ArrayList<String> pages) {
        ArrayList<WordCloud> firstPages = new ArrayList<>();
        ArrayList<ArrayList<WordCloud>> wordCloudLevels = new ArrayList<>();
        HashMap<String, Integer> wordsOccurrences;

        //create the first level
        for (String page : pages) {
            wordsOccurrences = getWordsOccurrences(page);
            WordCloud tmpCloud = new WordCloud(wordsOccurrences);
            firstPages.add(tmpCloud);
        }

        //add the first level to the list
        // wordCloudLevels.add(firstPages);

        //add each level of merged clouds
        for (ArrayList<WordCloud> tmpList : makeCloudLevels(firstPages)) {
            wordCloudLevels.add(tmpList);
        }

        return wordCloudLevels;

    }

    //returns a hashmap of strings in the provided text with their frequency count
    public HashMap<String, Integer> getWordsOccurrences(String text) {
        String[] words = text.split(" ");
        HashMap<String, Integer> wordsOccurrences = new HashMap();
        for (int i = 0; i < words.length; i++) {

            String word = trimPunctuation(words[i]);
            if (!this.commonWords.containsKey(word)) {
                if (wordsOccurrences.get(word) != null) {

                    wordsOccurrences.put(word, wordsOccurrences.get(word) + 1);
                } else {

                    wordsOccurrences.put(word, 1);

                }
            }
        }
        return wordsOccurrences;

    }
}