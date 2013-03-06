package flowreader.model;

import flowreader.utils.ValueComparatorDouble;
import flowreader.utils.ValueComparatorInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Jim
 */
public class WordCloud{
    private Integer totalNbOfWords = 0;
    private HashMap<String, Integer> wordsOccurrences;
    private HashMap<String, Double> wordsFrequencies; // wf
    private HashMap<String, Double> wfOnTotalDocument;
    private TreeMap<String, Double> sortedWfOnTotalDocument;
    private TreeMap<String, Double> sortedWordsFrequencies;
    private TreeMap<String, Integer> sortedWordsOccurrences;

    public WordCloud(HashMap<String, Integer> wordsOccurrences) {
        this.wordsOccurrences = wordsOccurrences;       
        this.calculeWordsFrequencies();
        this.sortedWordsOccurrences = this.sortOccHashMap(wordsOccurrences);
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
        this.calculeWordsFrequencies();
        this.sortedWordsOccurrences = this.sortOccHashMap(wordsOccurrences);
    }
    
    private void calculeWordsFrequencies(){
        for(Entry e : this.wordsOccurrences.entrySet()){
            this.totalNbOfWords = this.totalNbOfWords + (Integer)e.getValue();
        }
        
        this.wordsFrequencies = new HashMap<String, Double>();
        
        for(Entry e: this.wordsOccurrences.entrySet()){
            String key = e.getKey().toString();
            Double value = (Double)(((Integer)e.getValue()).doubleValue()/this.totalNbOfWords.doubleValue());
            this.wordsFrequencies.put(key, value);
            /*if(e.getKey().equals("the")){
               System.out.println(key+" : "+this.wordsOccurrences.get(key)+" "+((Integer)e.getValue())+" "+this.totalNbOfWords.doubleValue()+" "+value); 
            }*/
        }
        
        this.sortedWordsFrequencies = this.sortFreqHashMap(wordsFrequencies);
    }
    
    public void calculeWordsFrequenciesOnTotalDocument(WordCloud wcDocument){
        this.wfOnTotalDocument = new HashMap<String, Double>();
        HashMap<String, Double> wfDoc = wcDocument.getWordFrequencies();
        
        for(Entry e: this.wordsFrequencies.entrySet()){
            String key = e.getKey().toString();
            Double value = (Double)(((Double)e.getValue())/(Double)wfDoc.get(key));
            this.wfOnTotalDocument.put(key, value);
            /*if(key.equals("apple")){
               System.out.println(key);
               System.out.println("Occurrences in page: "+this.wordsOccurrences.get(key));
               System.out.println("Words number in page: "+this.totalNbOfWords);
               System.out.println("Frequency in page: "+this.wordsFrequencies.get(key));
               System.out.println("Frequency in document: "+(Double)wfDoc.get(key));
               System.out.println("fp on fd: "+value);
            }*/
        }
        
        this.sortedWfOnTotalDocument = this.sortFreqHashMap(this.wfOnTotalDocument);
    }
    
    public HashMap<String,Integer> getWordOccurrences( ){
         return this.wordsOccurrences;        
     }
    
     public TreeMap<String, Integer> getSortedWordOccurrences( ){
         return this.sortedWordsOccurrences;        
     }
    
    public TreeMap<String, Double> getSortedWordFrequencies( ){
         return this.sortedWordsFrequencies;        
     }
      
    private TreeMap<String, Double> sortFreqHashMap(HashMap<String, Double> wordsOccurrences){
        ValueComparatorDouble bvc =  new ValueComparatorDouble(wordsOccurrences);
        TreeMap<String,Double> sortedMap = new TreeMap<String, Double>(bvc);
        sortedMap.putAll(wordsOccurrences);
        Set<Map.Entry<String, Double>> w = sortedMap.entrySet();
        Iterator j = w.iterator();
        /*System.out.println("Word Cloud");
        for (int i=0; i<10; i++){
            Entry e = (Entry)j.next();
            System.out.println(e.getKey()+" : "+e.getValue()+" "+this.wordsFrequencies.get((String)e.getKey())+" "+this.wordsOccurrences.get((String)e.getKey()));
        }*/
        return sortedMap;
    }
    
    private TreeMap<String, Integer> sortOccHashMap(HashMap<String, Integer> wordsOccurrences){
        ValueComparatorInteger bvc =  new ValueComparatorInteger(wordsOccurrences);
        TreeMap<String,Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(wordsOccurrences);
        Set<Map.Entry<String, Integer>> w = sortedMap.entrySet();
        Iterator j = w.iterator();
        /*System.out.println("Word Cloud");
        for (int i=0; i<10; i++){
            Entry e = (Entry)j.next();
            System.out.println(e.getKey()+" : "+e.getValue()+" "+this.wordsFrequencies.get((String)e.getKey())+" "+this.wordsOccurrences.get((String)e.getKey()));
        }*/
        return sortedMap;
    }

    private HashMap<String, Double> getWordFrequencies() {
        return this.wordsFrequencies;
    }

}