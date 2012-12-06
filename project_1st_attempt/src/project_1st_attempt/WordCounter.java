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
 private Integer maxFontSize = 100;
 private Integer maxCount;
 private Integer minCount;
 private Integer normalizationConstant = 3;
 public ArrayList<Word> wordObjects;  //only public for basic testing
 public WordCounter(){
     this.words = new HashMap<String, Integer>();
     this.wordObjects = new ArrayList<Word>();
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

 //returns the n most common words in a document as a string
 //also converts these strings into word objects;
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
              this.wordObjects.add(new Word(maxKey, tmpCount));
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
 
 public void setWordSizes(){
     for (Word word : wordObjects){
         int countDiff = word.getCount() - this.minCount;
         int totalCountDiff = this.maxCount - this.minCount;
         word.setFontSize( ((this.maxFontSize * countDiff) / (this.normalizationConstant * totalCountDiff)));
         
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
