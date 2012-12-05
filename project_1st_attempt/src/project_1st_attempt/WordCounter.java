/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package project_1st_attempt;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import javafx.scene.text.Text;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.Group;
/**
 *
 * @author jim
 */
public class WordCounter {
 private HashMap<String, Integer> words;
 private Integer numOfWordsInCloud = 30;
 private Integer maxFontSize = 40;
 private Integer maxCount;
 private Integer minCount;
 private ArrayList<Word> wordObjects;
 public WordCounter(){
     this.words = new HashMap<String, Integer>();
     this.setWordObjects(new ArrayList<Word>());
 }
 
 //counts words in a line of texts and adds them to the 'words' hashMap
 public void readLine(String text){
     ArrayList<String> characters;
     for (String i : text.split(" ")){
         i = this.trimPunctuation(i);
         if (!(i.equals(""))){
            if (this.words.get(i) == null){
              words.put(i, 1);    
            }
            else{
                Integer count = this.words.get(i);
                count++;
                this.words.put(i, count);
            }
        }
     }
 }
     
 //returns a string of the 'words' hashmap (used for debugging)   
 //(currently printing the most commonly found word
 public String getWordCount(){
     String output = "";
     Integer currentMax = 0;
     return this.getNodes();
     /*for (String i: words.keySet()){
        if (this.words.get(i) > currentMax){
           output = ("the word:" + i + ":" + "        " + this.words.get(i) + "\n");
           currentMax = this.words.get(i);
        }
       
     }
    
     return output;
 */    
 }
 
 public String trimPunctuation(String word){
     return word.replaceAll("\\W", "");    
     
 }
 
 public String getNodes(){
     String output = "";
     String maxKey = "";
     int tmpCount;
     boolean maxCount= false;
     boolean minCount = false;
    for (int i = 0; i <= this.numOfWordsInCloud; i++){
       Integer maxInMap = Collections.max(this.words.values());
       Iterator it = this.words.entrySet().iterator();
        while (it.hasNext()) {
          
           Map.Entry pairs = (Map.Entry)it.next();
           //System.out.println(pairs.getKey() + " = " + pairs.getValue());
          
           tmpCount = Integer.parseInt(pairs.getValue().toString());
           if (tmpCount == maxInMap){
              output += (pairs.getKey().toString()+ ":" + "        " + tmpCount + "\n");
              maxKey = pairs.getKey().toString();
              this.getWordObjects().add(new Word(maxKey, tmpCount));
              if (!(maxCount)){
                  this.maxCount = tmpCount;
                  maxCount = true;
                  }
              }
                
        }
        this.words.remove(maxKey);
    this.minCount = maxInMap; 
    }
    
    return output;
 }
 
 public void setWordFontsizes(){
     for (Word word : getWordObjects()){
         int countDiff = word.getCount() - this.maxCount;
         int totalCountDiff = this.maxCount - this.minCount;
         word.setFontSize( ((this.maxFontSize* countDiff) / totalCountDiff));
         
     }
 }
 
 

/**
 * @return the wordObjects
 */
public ArrayList<Word> getWordObjects() {
	return wordObjects;
}

/**
 * @param wordObjects the wordObjects to set
 */
public void setWordObjects(ArrayList<Word> wordObjects) {
	this.wordObjects = wordObjects;
}



class WordCloud {
    private Group words;
    
    
    public WordCloud(){
        this.words = new Group();
    }
    
   public void renderCloud(ArrayList<Word> wordObjects ){
     
    //create the basic rectangle (usually in terms of function inputs)
    Group cloud = new Group();
    Rectangle r = new Rectangle();
    r.setX(50);
    r.setY(50);
    r.setWidth(200);
    r.setHeight(100);
    r.setArcWidth(20);
    r.setArcHeight(20);
    
    //these would normally be inputs to the function
    double originX = 50;
    double originY = 50;
    double currX = originX;
    double currY = originY;
    double maxWidth = 200;
    double maxHeight = 100;
    
    for (Word word : wordObjects ){
     Text currText = new Text();
     currText.setFont(Font.font("Veranda", word.getFontSize()));
     currText.setText(word.getText());
     double wordWidth = currText.getBoundsInLocal().getWidth();
     double wordHeight = currText.getBoundsInLocal().getHeight();
     if ((wordWidth + currX) > maxWidth){
         if ((wordHeight + currY) > maxHeight){
             break; // word won't fit on rectangle
         }
         else{
             //start new line
             currY = wordHeight + currY;
             currX = originX;
             currText.setX(currX);
             currText.setY(currY);
             this.words.getChildren().add(currText);
             //need some kind of 'renderWord(x,y)' here
         }
     }
     else{
         currY = wordHeight + currY;
         currX = wordWidth + currX;
          currText.setX(currX);
          currText.setY(currY);
          this.words.getChildren().add(currText);
         //need some kind of 'renderWord(x,y)' here
     }
    }
    this.words.getChildren().add(r);
   
 }
 
 
 

} 
    
    
    
}
class Word {
    
    private String letters;
    private Integer count;
    private Integer fontSize;
    public Word(String letters, Integer count){
        this.letters = letters;
        this.count = count;
    }
    
    public void setFontSize(Integer size){
        this.fontSize = size;
    }
    public Integer getFontSize(){
        return this.fontSize;
    }
    public Integer getCount(){
        return this.count;
    }
    public String getText(){
        return this.letters;
    }
     
 }