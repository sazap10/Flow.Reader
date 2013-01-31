/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jim
 */
public class Document {
    ArrayList<Page> pages;
    HashMap<String, Integer> documentOccurrences; //wordOccurrences in the document
    ArrayList<ArrayList<WordCloud>> wordClouds;
    int numOfPages;
    int numOfCloudLevels;
    int wordCount;
    
    public Document(ArrayList<Page> pages, ArrayList<ArrayList<WordCloud>> wordClouds, HashMap<String, Integer> documentOccurrences, int wordCount){
        this.pages = pages;
        this.wordClouds = wordClouds;
        this.numOfPages = pages.size();
        this.numOfCloudLevels = wordClouds.size();
        this.documentOccurrences = documentOccurrences;
        this.wordCount = wordCount;
        setWordCloudValues();
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
    
    //gets the word count for each page and adds it to the list
   
    public void setWordCloudValues(){
        int level = 1;
        int currPage;
        int wordCount;
        for(ArrayList<WordCloud> cloudList : this.wordClouds){
            currPage = 1;
            for(WordCloud cloud : cloudList){
                //need to sum the wordCounts from the next (level) pages
                wordCount = 0;
                for(int i = 1; i <= level; i++){
                    wordCount += this.pages.get(currPage - 1).getWordCount();                 
                }
                cloud.setWordValues(documentOccurrences, wordCount, this.wordCount);
            }
            level++;
            
        }
    }
    
}