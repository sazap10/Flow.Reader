/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import java.util.ArrayList;

/**
 * A document contains a list of pages and the word clouds that corresponds to these pages
 * @author Pat
 */
public class Document {
    
    private ArrayList<ArrayList<WordCloud>> wordClouds;
    private ArrayList<Page> pages;
    private boolean empty = false;
    private String errorMsg = "";
    public Document(ArrayList<Page> pages, ArrayList<ArrayList<WordCloud>> wordClouds){
        this.pages = pages;
        this.wordClouds = wordClouds;

    }
    
    //document constructor for aborted readfile, e.g. for a pdf which is too large
    public Document(String errorMsg){
        this.pages = new ArrayList<>();
        this.wordClouds = new ArrayList<>();
        this.empty = true;
        this.errorMsg = errorMsg;
    }
    
    public boolean isEmpty(){
        return this.empty;
    }
    
    public String getErrorMsg(){
        return this.errorMsg;
    }
    
    /**
     * @return all the pages 
     */
    public ArrayList<Page> getPages(){
        return pages;
    }
    
    /**
     * @return all the levels of word clouds of the documents
     */
    public ArrayList<ArrayList<WordCloud>> getWordClouds(){
        return this.wordClouds;
    }
}
