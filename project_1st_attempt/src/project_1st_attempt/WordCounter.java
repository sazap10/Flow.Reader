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
/**
 *
 * @author jim
 */
public class WordCounter {
 private HashMap<String, Integer> words;
 
 public WordCounter(){
     this.words = new HashMap<String, Integer>();
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
     for (String i: words.keySet()){
        if (this.words.get(i) > currentMax){
           output = ("the word:" + i + ":" + "        " + this.words.get(i) + "\n");
           currentMax = this.words.get(i);
        }
       
     }
    
     return output;
     
 }
 
 public String trimPunctuation(String word){
     return word.replaceAll("\\W", "");    
     
 }
 

}