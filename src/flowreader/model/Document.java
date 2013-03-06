/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;
import java.util.ArrayList;

/**
 *
 * @author jim
 */
public class Document {
    private ArrayList<Page> pages;
    private ArrayList<ArrayList<WordCloud>> wordClouds;
    
    public Document(ArrayList<Page> pages, ArrayList<ArrayList<WordCloud>> wordClouds){
        this.pages = pages;
        this.wordClouds = wordClouds;
        
        for(ArrayList<WordCloud> alwc : wordClouds){
            for(WordCloud wc : alwc){
                wc.getWordsFrequenciesOnTotalDocument(wordClouds.get(wordClouds.size()-1).get(0).getWordFrequencies());
            }
        }
    }
    
    /**
     * @return the pages of the document
     */
    public ArrayList<Page> getPages(){
        return this.pages;
    }
    
    /**
     * @return all the levels of word clouds of the documents
     */
    public ArrayList<ArrayList<WordCloud>> getWordClouds(){
        return this.wordClouds;
    }
}