/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.model.PDFPage;
import flowreader.model.Page;
import flowreader.model.WordCloud;
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
 * Read a PDF file and transform it in a Document object
 * @author D-Day
 */
public class PDFFileReader extends FileReader {

    public PDFFileReader(File file) {
        super(file);
    }

    @Override
    public Document readFile(double a, double b) throws IOException {
        ArrayList<String> pageText;
        ArrayList<ArrayList<WordCloud>> clouds = new ArrayList<ArrayList<WordCloud>>();
        ArrayList<Page> pages = new ArrayList<Page>();
        try {
            if (file != null) {

                PDDocument pdfDocument = PDDocument.load(file);
                pageText = getText(pdfDocument, pdfDocument.getNumberOfPages());
                for (int i = 0; i < pdfDocument.getNumberOfPages(); i++) {
                    PDPage pDPage = (PDPage) pdfDocument.getDocumentCatalog().getAllPages().get(i);
                    WritableImage image = this.BufferedToWritable(pDPage.convertToImage(BufferedImage.TYPE_USHORT_555_RGB, 60));
                    pages.add(new PDFPage(pageText.get(i), image));
                    this.updateProgress(i + 1, pdfDocument.getNumberOfPages());
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

        return document;

    }

    /**
     * @param bf
     * @return a writable image based on bf
     */
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

    /**
     * @param document
     * @param numOfPages
     * @return an indexed array of strings for each page of the input document
     * @throws IOException 
     */
    public ArrayList<String> getText(PDDocument document, int numOfPages) throws IOException {
        ArrayList<String> pages = new ArrayList<String>();
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
                pages.add(page);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    /**
     * @param pages
     * @return wordClouds from page text
     */
    public ArrayList<ArrayList<WordCloud>> makeWordClouds(ArrayList<String> pages) {
        ArrayList<WordCloud> firstPages = new ArrayList<WordCloud>();
        ArrayList<ArrayList<WordCloud>> wordCloudLevels = new ArrayList<ArrayList<WordCloud>>();
        HashMap<String, Integer> wordsOccurrences;

        //create the first level
        for (String page : pages) {
            wordsOccurrences = getWordsOccurrences(page);
            WordCloud tmpCloud = new WordCloud(wordsOccurrences);
            firstPages.add(tmpCloud);
        }

        //add each level of merged clouds
        for (ArrayList<WordCloud> tmpList : makeCloudLevels(firstPages)) {
            wordCloudLevels.add(tmpList);
        }

        return wordCloudLevels;

    }
 
    /**
     * @param text
     * @return a hashmap of strings in the provided text with their frequency count
     */
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
