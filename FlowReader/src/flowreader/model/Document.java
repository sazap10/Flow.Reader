/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
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
    ArrayList<BufferedImage> inputImages;
    ArrayList<Image> pages;
    ArrayList<Integer> pageWordCounts;
    HashMap<String, Integer> documentOccurrences; //wordOccurrences in the document
    ArrayList<ArrayList<WordCloud>> wordClouds;
    int numOfPages;
    int numOfCloudLevels;
    int wordCount;
    
     public Document(ArrayList<BufferedImage> pages, ArrayList<Integer> pageWordCounts,  ArrayList<ArrayList<WordCloud>> wordClouds, HashMap<String, Integer> documentOccurrences, int wordCount){
        this.pages = new ArrayList();
        this.inputImages = pages;
        this.pageWordCounts = pageWordCounts;
        this.wordClouds = wordClouds;
        this.numOfPages = pages.size();
        this.numOfCloudLevels = wordClouds.size();
        this.documentOccurrences = documentOccurrences;
        this.wordCount = wordCount;
        setWordCloudValues();
    }
    public Document(ArrayList<BufferedImage> pages){
        
        this.pages = new ArrayList();
        this.inputImages = pages;
        this.numOfPages = this.inputImages.size();
    }
    
    public Image getPage(int index){
        return this.pages.get(index);
    }
        
    
    public void resizePages(int pageHeight, int pageWidth) throws IOException{
        for(BufferedImage image: inputImages){
            pages.add(createImage(image, pageHeight, pageWidth));
        }
        
    }
    public static Image createImage(java.awt.Image image, int height, int width) throws IOException {
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