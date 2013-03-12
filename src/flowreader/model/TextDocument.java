/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;
import flowreader.view.flowview.PageView;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author jim
 */
public class TextDocument  implements Document{
    private ArrayList<Page> pages;
    private ArrayList<Group> pageViews;
    private ArrayList<ArrayList<WordCloud>> wordClouds;
    
    public TextDocument(ArrayList<Page> pages, ArrayList<ArrayList<WordCloud>> wordClouds){
        this.pages = pages;
        this.wordClouds = wordClouds;
        this.pageViews = new ArrayList<>();
        for(ArrayList<WordCloud> alwc : wordClouds){
            for(WordCloud wc : alwc){
                wc.getWordsFrequenciesOnTotalDocument(wordClouds.get(wordClouds.size()-1).get(0).getWordFrequencies());
            }
        }
        buildPageViews();
    }
    
    //builds pageview objects for each page
    private void buildPageViews(){

        for(Page page : pages){
             PageView pageView = new PageView(new Rectangle(0, 0, 500, 700));
            pageView.setText(page.getText());
            pageViews.add(pageView);

        }
        
        
        
    }
    
    /**
     * @return the pages of the document
     */
   // public ArrayList<Page> getPages(){
   //     return this.pages;
  //  }
    
    @Override
    public ArrayList<Group> getPageViews(){
        return pageViews;
        
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