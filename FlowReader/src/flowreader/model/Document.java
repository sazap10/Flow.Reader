/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;
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
public class Document {
    ArrayList<WritableImage> inputImages;
    ArrayList<ImageView> pages;
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
    
    public Document(int pageHeight, int pageWidth){
        this.pages = new ArrayList();
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
    }
    public void addImage(WritableImage image){
           ImageView newPage = new ImageView(image);
           newPage.setFitWidth(this.pageWidth);
           newPage.setFitHeight(this.pageHeight);
           pages.add(newPage);
        
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
        setWordCloudValues();
    }
    
    public ImageView getPage(int index){
        return this.pages.get(index);
    }
        
    
    public void resizePages(int pageHeight, int pageWidth){
        for(WritableImage image: inputImages){
           ImageView newPage = new ImageView(image);
           newPage.setFitWidth(pageWidth);
           newPage.setFitHeight(pageHeight);
           pages.add(newPage);
            
        }
        
    }
 /*   public static Image createImage(java.awt.Image image, int height, int width) throws IOException {
    if (!(image instanceof RenderedImage)) {
      BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
              image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      Graphics g = bufferedImage.createGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose();
      image = bufferedImage;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write((RenderedImage) image, "png", out);
    out.flush();
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    return new javafx.scene.image.Image(in, width, height, false, false );
  }
       
 */   
    
    
/*
   
    
    public ArrayList<Page> getPages(){
        return this.pages;
    }
    
    public Page getPage(int page){
        return pages.get(page);
    }
*/    
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
                    wordCount += this.pageWordCounts.get(currPage - 1);                 
                }
                cloud.setWordValues(documentOccurrences, wordCount, this.wordCount);
            }
            level++;
            
        }
    }

}