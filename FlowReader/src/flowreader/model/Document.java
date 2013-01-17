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
    ArrayList<Page> pages;
    ArrayList<ArrayList<WordCloud>> wordClouds;
    int numOfPages;
    int numOfCloudLevels;
    
    
    public Document(ArrayList<Page> pages, ArrayList<ArrayList<WordCloud>> wordClouds){
        this.pages = pages;
        this.wordClouds = wordClouds;
        this.numOfPages = pages.size();
        this.numOfCloudLevels = wordClouds.size();
        
    }
    
    public ArrayList<Page> getPages(){
        return this.pages;
    }
    
    public Page getPage(int page){
        return pages.get(page);
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
    
}