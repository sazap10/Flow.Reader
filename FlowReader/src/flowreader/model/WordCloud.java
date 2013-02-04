package flowreader.model;

import flowreader.utils.ValueComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashMap;
import java.math.BigDecimal;
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
    private TreeMap<String, BigDecimal> sortedMap;
    private int maxFontSize = 30;
    private int minFontSize = 14;
    private ArrayList<Text> words;
    private Integer numOfWordsInCloud = 12;
    BigDecimal minCount; //count of smallest word in occurences
    BigDecimal maxCount; //cound of biggest word in occurrences;

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
       BigDecimal pageFreq, docFreq;
       //float newVal;
       BigDecimal smallest = new BigDecimal(0);
       BigDecimal largest = new BigDecimal (0);
       HashMap<String, BigDecimal> occurrences = new HashMap<>();
       //loop through adding double value occurrences to a map
       for (String word: this.wordsOccurrences.keySet()){
           pageCount = this.wordsOccurrences.get(word);
           docCount = documentOccurrences.get(word);
           
            pageFreq =  new BigDecimal(pageCount / cloudWordCount);
           docFreq = new BigDecimal(docCount / docWordCount);
         
           BigDecimal result = pageFreq.divide(docFreq, 30, BigDecimal.ROUND_UP);
          
           occurrences.put(word, result);
           if (result.compareTo(smallest) == -1){
               smallest = result;
           }
           if (result.compareTo(largest) == 1){
               largest = result;
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
       this.sortedMap = sortWordsOccurrences(occurrences);
       minCount = sortedMap.lastEntry().getValue();
       maxCount = sortedMap.firstEntry().getValue();
       
   }
    

   public TreeMap<String, BigDecimal> getSortedMap(){
       return this.sortedMap;
   }

   
 
  
    
   public void setWordOccurrences(HashMap<String,Integer> wordsOccurrences){
        this.wordsOccurrences = wordsOccurrences;
       
              
    }
    
   public   HashMap<String,Integer> getWordOccurrences( ){
        return this.wordsOccurrences;
              
    }
      
       private TreeMap<String, BigDecimal> sortWordsOccurrences(HashMap<String, BigDecimal> wordsOccurrences){
        ValueComparator bvc =  new ValueComparator(wordsOccurrences);
        TreeMap<String,BigDecimal> sortedWordsOccurrences = new TreeMap<>(bvc);
        TreeMap<String, BigDecimal> trimmedOccurrences = new TreeMap<>();
        sortedWordsOccurrences.putAll(wordsOccurrences);
        Set<Map.Entry<String, BigDecimal>> w = sortedWordsOccurrences.entrySet();
        Iterator i = w.iterator();
        int j = 0;
        while(j<this.numOfWordsInCloud && i.hasNext()) {
            Map.Entry<String, BigDecimal> e = (Map.Entry<String, BigDecimal>)i.next();
            trimmedOccurrences.put(e.getKey(), e.getValue());
            j++;
	}
       
        return trimmedOccurrences;
    }
       
      private void setMaxCount(BigDecimal count){
          if (this.maxCount.compareTo(count) == -1){
              this.maxCount = count;
          }
      }
      
      public BigDecimal getMaxCount(){
          return this.maxCount;
      }
      
      public BigDecimal getMinCount(){
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