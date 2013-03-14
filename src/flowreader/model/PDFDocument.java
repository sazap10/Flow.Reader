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
public class PDFDocument {
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

          // newPage.setFitWidth(this.pageWidth);
          // newPage.setFitHeight(this.pageHeight);
           //pageViews.add(newPage);

        
    }
    

    
    public void addWordClouds(ArrayList<ArrayList<WordCloud>> clouds){
        this.wordClouds = clouds;
        
    }
    
    public ArrayList<Page> getPages(){
        return this.pages;
    }
    
    public void setPages(ArrayList<Page> pages){
        this.pages = pages;
    }
    
    

        

 
    
    public ArrayList<ArrayList<WordCloud>> getWordClouds(){
        return this.wordClouds;
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
   
 
    }
