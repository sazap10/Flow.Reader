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
    }
    
    public ArrayList<Page> getPages(){
        return this.pages;
    }
    
    public ArrayList<WordCloud> getCloudLevel(int level){
        return wordClouds.get(level);
    }
    
    public Page getPage(int page){
        return pages.get(page);
    }
    
    public WordCloud getCloud(int level, int cloudNum){
        System.out.println("returning cloud " + cloudNum + " from level " + level);
        return wordClouds.get(level).get(cloudNum);
    }

    public int getNumOfPages(){
        return this.pages.size();
    }
    
    public int getNumOfCloudLevels(){
        return this.wordClouds.size();
    }
    
    public ArrayList<ArrayList<WordCloud>> getWordClouds(){
        return this.wordClouds;
    }
    
}