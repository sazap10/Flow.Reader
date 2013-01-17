package flowreader.model;

import flowreader.utils.ValueComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Jim
 */
public class WordCloud extends Group{


    private HashMap<String, Integer> wordsOccurrences;
    private TreeMap<String, Integer> sortedMap;
    private ArrayList<Text> words;
    private Integer numOfWordsInCloud = 10;


    public WordCloud(HashMap<String, Integer> wordsOccurrences) {
        this.wordsOccurrences = wordsOccurrences;       
        this.sortedMap = sortWordsOccurrences(wordsOccurrences);
 
        
    }
    
     public WordCloud (WordCloud a, WordCloud b){                   
        HashMap<String,Integer> bMap = b.getWordOccurrences();            
        this.setWordOccurrences(a.getWordOccurrences());      
        
        //walk through cloud b and add it's words
        for (String word: bMap.keySet()){   
    
            if (wordsOccurrences.containsKey(word)){
                int count = wordsOccurrences.get(word) + bMap.get(word);
                wordsOccurrences.put(word, count);
            }
            else{
                wordsOccurrences.put(word, bMap.get(word));
            }                 
            
        }
        
    }
    

   
    
   public void setWordOccurrences(HashMap<String,Integer> wordsOccurrences){
        this.wordsOccurrences = wordsOccurrences;
       
              
    }
    
   public   HashMap<String,Integer> getWordOccurrences( ){
        return this.wordsOccurrences;
              
    }
      
       private TreeMap<String, Integer> sortWordsOccurrences(HashMap<String, Integer> wordsOccurrences){
        ValueComparator bvc =  new ValueComparator(wordsOccurrences);
        TreeMap<String,Integer> sortedWordsOccurrences = new TreeMap<>(bvc);
        sortedWordsOccurrences.putAll(wordsOccurrences);
        
        //System.out.println("unsorted map: "+wordsOccurrences);
        //System.out.println("results: "+sortedWordsOccurrences);
       
        return sortedWordsOccurrences;
    }

    
 
}