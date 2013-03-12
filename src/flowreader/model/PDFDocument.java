/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import javafx.scene.Group;

/**
 *
 * @author Jim
 */

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
/**
 *
 * @author jim
 */
public class PDFDocument implements Document {
    ArrayList<WritableImage> inputImages;
    ArrayList<ImageView> pageViews;
    ArrayList<Page> pages;
    ArrayList<Integer> pageWordCounts;
    HashMap<String, Integer> documentOccurrences; //wordOccurrences in the document
    ArrayList<ArrayList<WordCloud>> wordClouds;
    int numOfPages;
    int numOfCloudLevels;
    int wordCount;
    int pageWidth;
    int pageHeight;
    
  /*   public Document(ArrayList<WritableImage> pages, ArrayList<Integer> pageWordCounts,  ArrayList<ArrayList<WordCloud>> wordClouds, HashMap<String, Integer> documentOccurrences, int wordCount){
        this.pages = new ArrayList();
        this.pageWordCounts = pageWordCounts;
        this.wordClouds = wordClouds;
        this.numOfPages = pages.size();
        this.numOfCloudLevels = wordClouds.size();
        this.documentOccurrences = documentOccurrences;
        this.wordCount = wordCount;
        setWordCloudValues();
        System.out.println("no exception here");
        resizePages(700*2, 500*2);
    }
    
    public Document(ArrayList<WritableImage> pages){
        
        this.pages = new ArrayList();
        this.inputImages = pages;
        this.numOfPages = this.inputImages.size();
    }
    */
    
    public PDFDocument(int pageHeight, int pageWidth){

        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        pageViews = new ArrayList<>();
    }
    public void addImage(WritableImage image){
        System.out.println("in addimage function");
           ImageView newPage = new ImageView(image);
           newPage.setFitWidth(this.pageWidth);
           newPage.setFitHeight(this.pageHeight);
           System.out.append("created image with correct bounds");
           pageViews.add(newPage);
           System.out.append("added to pageViews");
        
    }
    
    public void addPageCounts(ArrayList<Integer> pageWordCounts){
        this.pageWordCounts = pageWordCounts;
        
    }
    
    public void addWordClouds(ArrayList<ArrayList<WordCloud>> clouds, HashMap<String, Integer> documentOccurrences, int wordCount){
        this.wordClouds = clouds;
        this.numOfPages = pages.size();
        this.numOfCloudLevels = wordClouds.size();
        this.documentOccurrences = documentOccurrences;
        this.wordCount = wordCount;
        
    }
    
    public ArrayList<Page> getPages(){
        return this.pages;
    }
    
    public void setPages(ArrayList<Page> pages){
        this.pages = pages;
    }
    
    
    //need to create group for each imageView and set it's location
    public ArrayList<Group> getPageViews(){
        ArrayList<Group> pageViews = new ArrayList<>();
        for (ImageView page: this.pageViews){
            //create group for each page
            Group pageGroup = new Group();
            //add the image to the group
            pageGroup.getChildren().add(page);
            pageViews.add(pageGroup);
            
        }
        return pageViews;
        
    }
        
    
    public void resizePages(int pageHeight, int pageWidth){
        for(WritableImage image: inputImages){
           ImageView newPage = new ImageView(image);
           newPage.setFitWidth(pageWidth);
           newPage.setFitHeight(pageHeight);
           pageViews.add(newPage);
            
        }
        
    }
 
    
    public ArrayList<ArrayList<WordCloud>> getWordClouds(){
        return this.wordClouds;
    }
   
    public WordCloud getCloud (int level, int cloudNum){
        System.out.println("returning cloud " + cloudNum + " from level " + level);
        return wordClouds.get(level).get(cloudNum);
    }
    
    public ArrayList<WordCloud> getCloudLevel(int level){
        return wordClouds.get(level -1 );
    }

    public int getNumOfPages(){
        return this.numOfPages;
    }

    public int getNumOfCloudLevels(){
        return this.numOfCloudLevels;
    }
    
    //gets the word count for each page and adds it to the list
   
  /*  public void setWordCloudValues(){
        int level = 1;
        int currPage;
        int wordCount;
        for(ArrayList<WordCloud> cloudList : this.wordClouds){
            currPage = 1;
            for(WordCloud cloud : cloudList){
                //need to sum the wordCounts from the next (level) pages
                wordCount = 0;
                for(int i = 1; i <= level; i++){
                    wordCount += this.pageWordCounts.get(currPage - 1);                 
                }
                cloud.setWordValues(documentOccurrences, wordCount, this.wordCount);
            }
            level++;
            
        }
        * */
    }
