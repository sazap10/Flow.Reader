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
import java.util.List;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import java.awt.image.BufferedImage;

/**
 *
 * @author D-Day
 */
public class TextFileReader{

    private File file;
    
    private HashMap<String, Integer> commonWords;
    private HashMap<String, Integer> documentOccurrences; //dict for whole document
    private ArrayList<Integer> pageWordCounts; //array storing the word count for each page in the book
    private int wordCount = 0;
    public TextFileReader() {
        this.commonWords = new HashMap<>();
        this.documentOccurrences = new HashMap<>();
        this.pageWordCounts = new ArrayList<>();
       // this.getCommonWords();
    }

    public void startFileChooser(Stage primaryStage) {
        //start file chooser
        File f = new File(System.getProperty("user.dir"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please choose a text file to read");
        fileChooser.setInitialDirectory(f);

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        file = fileChooser.showOpenDialog(primaryStage);

    }

    /**
     * @param bounds the boundaries of the page used to know how much text contains a page
     * @return a list of pages that contains the text of each page and the words occurrences
     * @throws IOException 
     */
    
    
    //reads in the images from the pdf file
    public Document readFile(Rectangle bounds) throws IOException{
    ArrayList myList = new ArrayList<BufferedImage>();
    ArrayList<String> pageText;
    ArrayList<ArrayList<WordCloud>> clouds = new ArrayList<ArrayList<WordCloud>>();
        try {
            if (file != null) {
 
                PDDocument document = PDDocument.load(file);
                List<PDPage> pages = document.getDocumentCatalog().getAllPages();
                for (int i = 0; i < pages.size(); i++) {
                    PDPage pDPage = pages.get(i);
                    myList.add(pDPage.convertToImage(BufferedImage.TYPE_3BYTE_BGR, 60 ));
                }
                pageText = getText(document, myList.size());
                 
                //get the page word counts for each page
                for(int i = 0; i < pageText.size(); i++){                   
                   this.pageWordCounts.add(pageText.get(i).split("").length);
                }
               
                
                clouds = makeWordClouds(pageText);
                document.close();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        Document myDocument = new Document(myList,this.pageWordCounts, clouds, this.documentOccurrences, this.wordCount);
        System.out.println("ok here");
        return myDocument;

    }
    
    
    //returns an indexed array of strings for each page of the input document
    public ArrayList<String> getText(PDDocument document, int numOfPages) throws IOException{
        ArrayList<String> pages = new ArrayList<>();
        ByteArrayOutputStream bout;
        OutputStreamWriter writer;
        try{
            //set the buffer
    bout = new ByteArrayOutputStream();
    writer = new OutputStreamWriter(bout);
 
    //strip the document to the buffer 
    
   
         PDFTextStripper stripper = new PDFTextStripper();
            for(int i = 1; i <= numOfPages; i++){
            stripper.setStartPage( i);
            stripper.setEndPage( i+1 );
            stripper.writeText(document, writer);
            bout.flush();
            writer.flush();
            String page = bout.toString();
            System.out.println("page " + i + ": " + page);
            pages.add(page);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return pages;
    }
    
    //creates wordClouds from page text
    public ArrayList<ArrayList<WordCloud>> makeWordClouds(ArrayList<String> pages){
        ArrayList<WordCloud> firstPages = new ArrayList<>();
        ArrayList<ArrayList<WordCloud>> wordCloudLevels = new ArrayList<>();
        HashMap<String, Integer> wordsOccurrences;
        
        //create the first level
        for(String page: pages){
            wordsOccurrences = getWordsOccurrences(page);
            WordCloud tmpCloud = new WordCloud(wordsOccurrences);
            firstPages.add(tmpCloud);
        }
        
        //add the first level to the list
        wordCloudLevels.add(firstPages);
        
        //add each level of merged clouds
        for (ArrayList<WordCloud> tmpList : makeCloudLevels(firstPages)){
            wordCloudLevels.add(tmpList);
        }
        
        return wordCloudLevels;
        
    }
    
    //returns a hashmap of strings in the provided text with their frequency count
    public HashMap<String, Integer> getWordsOccurrences(String text){
        String[] words = text.split(" ");
        HashMap<String,Integer> wordsOccurrences = new HashMap();
        for(int i = 0; i < words.length; i++){
            this.wordCount++;
            String word = trimPunctuation(words[i]);
            if (!this.commonWords.containsKey(word)) {
                            if(wordsOccurrences.get(word)!=null){
                                this.addDocumentOccurrence(word);
                                wordsOccurrences.put(word, wordsOccurrences.get(word)+1);
                            }
                            else{
                                
                                this.addDocumentOccurrence(word);
                                wordsOccurrences.put(word, 1);
                                
                            }
                        }
        }
        return wordsOccurrences;
        
    }
        
    
    /*public Document readFile(Rectangle bounds) throws IOException {
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
                            WordCloud wordCloud = new WordCloud(wordsOccurrences);
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
                        
                        //add the occurence to the document map
                        

                        
                        if (!this.commonWords.containsKey(word)) {
                            if(wordsOccurrences.get(word)!=null){
                                this.addDocumentOccurrence(word);
                                wordsOccurrences.put(word, wordsOccurrences.get(word)+1);
                            }
                            else{
                                
                                this.addDocumentOccurrence(word);
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
            WordCloud wordCloud = new WordCloud(wordsOccurrences);
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
        Document document = new Document(pages, wordCloudLevels, this.documentOccurrences, this.wordCount);
        return document;
    }
    
  */
    
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
   
    //adds a word's occurrence to the document Occurrences Map
    private void addDocumentOccurrence(String word){
        if (this.documentOccurrences.containsKey(word)){
            int count = this.documentOccurrences.get(word);
            count++;
            this.documentOccurrences.put(word, count);
        }
        else{
            this.documentOccurrences.put(word, 1);
        }
        
        //finally, increment the overall word count
        this.wordCount++;
        
    }

    
    //removes punctuation from any words found
    private String trimPunctuation(String word) {
        return word.toLowerCase().replaceAll("\\W", "");
    }
   
}