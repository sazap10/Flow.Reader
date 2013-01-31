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
    private int maxFontSize = 30;
    private int minFontSize = 14;
    private ArrayList<Text> words;
    private Integer numOfWordsInCloud = 12;
    int minCount; //count of smallest word in occurences
    int maxCount; //cound of biggest word in occurrences;

    public WordCloud(HashMap<String, Integer> wordsOccurrences) {
        
        this.wordsOccurrences = wordsOccurrences; 
        
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
     
     
   //sets a value for each word in the wordsOccurrences based on their
   //frequency in the page relative to the frequency in the whole document
   public void setWordValues(HashMap<String, Integer> documentOccurrences, int cloudWordCount, int docWordCount){
       int finalVal, newVal;
       float pageCount, docCount;
       //float newVal;
       float smallest = 0;
       float largest = 0;
       HashMap<String, Float> occurrences = new HashMap<>();
       //loop through adding double value occurrences to a map
       for (String word: this.wordsOccurrences.keySet()){
           pageCount = this.wordsOccurrences.get(word);
           docCount = documentOccurrences.get(word);
        
           float pageFreq = pageCount / cloudWordCount;
           float docFreq = docCount / docWordCount;         
           newVal = (int)  (pageFreq / docFreq);
           this.wordsOccurrences.put(word, newVal);
           if (newVal < smallest){
               smallest = newVal;
           }
           if (newVal > largest){
               largest = newVal;
           }
       }
       //now need to loop through to ensure each value is greater than zero
    /*   for (String word: occurrences.keySet()){
            System.out.println("new val: " + occurrences.get(word));
            finalVal = (int) Math.ceil((Math.pow(occurrences.get(word), 2) * coefficient) / 100.0);
            System.out.println("final val: " + finalVal);
            this.wordsOccurrences.put(word, finalVal);
          
       }
       * */
       this.sortedMap = sortWordsOccurrences(this.wordsOccurrences);
       minCount = sortedMap.lastEntry().getValue();
       maxCount = sortedMap.firstEntry().getValue();
       
   }
    

   public TreeMap<String, Integer> getSortedMap(){
       return this.sortedMap;
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
       
      private void setMaxCount(int count){
          if (this.maxCount < count){
              this.maxCount = count;
          }
      }
      
      public int getMaxCount(){
          return this.maxCount;
      }
      
      public int getMinCount(){
          return this.minCount;
      }
       
       
       //creates Word objects with the top (numOfWordsInCloud) most common words

 /*   private void trimOccurrences() {	
        String maxKey = "";
        int tmpCount;	
        boolean maxCount = false;
        HashMap<String, Integer> words = new HashMap<String, Integer>();
        
        for (int i = 0; i <= this.numOfWordsInCloud; i++) {
            
            Integer maxInMap = Collections.max(this.wordsOccurrences.values());	
            this.setMaxCount(maxInMap);
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
*/
    
 
}