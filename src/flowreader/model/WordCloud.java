package flowreader.model;

import flowreader.utils.ValueComparator;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Jim
 */
public class WordCloud{
    private HashMap<String, Integer> wordsOccurrences;
    private TreeMap<String, Integer> sortedWordsOccurrences;

    public WordCloud(HashMap<String, Integer> wordsOccurrences) {
        this.wordsOccurrences = wordsOccurrences;       
        this.sortedWordsOccurrences = sortWordsOccurrences(wordsOccurrences);
    }
    
    public WordCloud(WordCloud a, WordCloud b){                   
        HashMap<String,Integer> bMap = b.getWordOccurrences();            
        this.wordsOccurrences = a.getWordOccurrences();      
        
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
        this.sortedWordsOccurrences = sortWordsOccurrences(wordsOccurrences);
    }
    
    public HashMap<String,Integer> getWordOccurrences( ){
         return this.wordsOccurrences;        
     }
    
    public TreeMap<String, Integer> getSortedWordOccurrences( ){
         return this.sortedWordsOccurrences;        
     }
      
    private TreeMap<String, Integer> sortWordsOccurrences(HashMap<String, Integer> wordsOccurrences){
        ValueComparator bvc =  new ValueComparator(wordsOccurrences);
        TreeMap<String,Integer> sortedMap = new TreeMap<>(bvc);
        sortedMap.putAll(wordsOccurrences);
        return sortedMap;
    }
}