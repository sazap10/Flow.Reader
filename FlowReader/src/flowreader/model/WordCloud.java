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
        trimOccurrences();
        
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
        trimOccurrences();
        
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
       
       
       //creates Word objects with the top n most common words

    private void trimOccurrences() {	
        String maxKey = "";
        int tmpCount;	
        boolean maxCount = false;
        int minCount;
        HashMap<String, Integer> words = new HashMap<String, Integer>();
        
        for (int i = 0; i <= this.numOfWordsInCloud; i++) {
            
            Integer maxInMap = Collections.max(this.wordsOccurrences.values());	
            Iterator it = this.wordsOccurrences.entrySet().iterator();
            
            while (it.hasNext()) {	
                
                Map.Entry pairs = (Map.Entry) it.next();			
                tmpCount = Integer.parseInt(pairs.getValue().toString());
                
                //if it's the highest value, add it to our current array
                 if (tmpCount == maxInMap) {
                    maxKey = pairs.getKey().toString();	
                    words.put(maxKey, tmpCount);	
                      if (!(maxCount)) {
                        maxInMap = tmpCount;	
                        maxCount = true;
	
                    }
	
               }
	
           }

          this.wordsOccurrences.remove(maxKey);
          minCount = maxInMap;
	
        }
         this.wordsOccurrences = words;	
    }

    
 
}