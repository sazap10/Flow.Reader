/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Pat
 */
public class Document {
    
    private ArrayList<ArrayList<WordCloud>> wordClouds;
    private ArrayList<Page> pages;
    
    public Document(ArrayList<Page> pages, ArrayList<ArrayList<WordCloud>> wordClouds){
        this.pages = pages;
        this.wordClouds = wordClouds;
    }
    
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
