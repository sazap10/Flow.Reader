/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import flowreader.utils.ValueComparatorDouble;
import flowreader.utils.ValueComparatorInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author D-Day
 */
public class WordCloudTest {
    
    private WordCloud wc;
    private WordCloud mergedWc;
    
    public WordCloudTest() {
    }
    
    @Before
    public void setUp() {
        // Normal word cloud creation
        HashMap<String, Integer> wordsOccurrences = new HashMap<String, Integer>();
        wordsOccurrences.put("word1", 1);
        wordsOccurrences.put("word2", 2);
        this.wc = new WordCloud(wordsOccurrences);
        
        // Merged word clouds creation
        HashMap<String, Integer> wordsOccurrences1 = new HashMap<String, Integer>();
        wordsOccurrences1.put("word2", 1);
        WordCloud wc1 = new WordCloud(wordsOccurrences1);
        HashMap<String, Integer> wordsOccurrences2 = new HashMap<String, Integer>();
        wordsOccurrences2.put("word2", 1);
        wordsOccurrences2.put("word1", 1);
        WordCloud wc2 = new WordCloud(wordsOccurrences2);
        this.mergedWc = new WordCloud(wc1, wc2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getWordOccurrences method, of class WordCloud.
     */
    @Test
    public void testGetWordOccurrences() {
        System.out.println("getWordOccurrences");
        HashMap<String, Integer> expResult = new HashMap<String, Integer>();
        expResult.put("word1", 1);
        expResult.put("word2", 2);
        HashMap result = this.wc.getWordOccurrences();
        Iterator iresult = result.entrySet().iterator();
        Iterator iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.mergedWc.getWordOccurrences();
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
    }

    /**
     * Test of getSortedWordOccurrences method, of class WordCloud.
     */
    @Test
    public void testGetSortedWordOccurrences() {
        System.out.println("getSortedWordOccurrences");
        HashMap<String, Integer> wordOccurrences = new HashMap<String, Integer>();
        wordOccurrences.put("word1", 1);
        wordOccurrences.put("word2", 2);
        ValueComparatorInteger bvc = new ValueComparatorInteger(wordOccurrences);
        TreeMap<String, Integer> expResult = new TreeMap<String, Integer>(bvc);
        expResult.putAll(wordOccurrences);
        TreeMap<String, Integer> result = this.wc.getSortedWordOccurrences();
        Iterator iresult = result.entrySet().iterator();
        Iterator iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.mergedWc.getSortedWordOccurrences();
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
    }

    /**
     * Test of getWordFrequencies method, of class WordCloud.
     */
    @Test
    public void testGetWordFrequencies() {
        System.out.println("getWordFrequencies");
        HashMap<String, Double> expResult = new HashMap<String, Double>();
        expResult.put("word1", 1.0/3.0);
        expResult.put("word2", 2.0/3.0);
        HashMap<String, Double>  result = this.wc.getWordFrequencies();
        Iterator iresult = result.entrySet().iterator();
        Iterator iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.mergedWc.getWordFrequencies();
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
    }

    /**
     * Test of getSortedWordFrequencies method, of class WordCloud.
     */
    @Test
    public void testGetSortedWordFrequencies() {
        System.out.println("getSortedWordFrequencies");
        HashMap<String, Double> wordFrequencies = new HashMap<String, Double>();
        wordFrequencies.put("word1", 1.0/3.0);
        wordFrequencies.put("word2", 2.0/3.0);
        ValueComparatorDouble bvc = new ValueComparatorDouble(wordFrequencies);
        TreeMap<String, Double> expResult = new TreeMap<String, Double>(bvc);
        expResult.putAll(wordFrequencies);
        TreeMap<String, Double> result = this.wc.getSortedWordFrequencies();
        Iterator iresult = result.entrySet().iterator();
        Iterator iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.mergedWc.getSortedWordFrequencies();
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
    }

    /**
     * Test of getWordsFrequenciesOnTotalDocument method, of class WordCloud.
     */
    @Test
    public void testGetWordsFrequenciesOnTotalDocument() {
        System.out.println("getWordsFrequenciesOnTotalDocument");
        HashMap<String, Double> wordFrequencies = new HashMap<String, Double>();
        wordFrequencies.put("word1", 4.0/100.0);
        wordFrequencies.put("word2", 15.0/100.0);
        HashMap<String, Double> expResult = new HashMap<String, Double>();
        expResult.put("word1", (1.0/3.0)/wordFrequencies.get("word1"));
        expResult.put("word2", (2.0/3.0)/wordFrequencies.get("word2"));
        HashMap<String, Double>  result = this.wc.getWordsFrequenciesOnTotalDocument(wordFrequencies);
        Iterator iresult = result.entrySet().iterator();
        Iterator iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.mergedWc.getWordsFrequenciesOnTotalDocument(wordFrequencies);
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.wc.getWordsFrequenciesOnTotalDocument(wordFrequencies);
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
    }

    /**
     * Test of getSortedWordsFrequenciesOnTotalDocument method, of class WordCloud.
     */
    @Test
    public void testGetSortedWordsFrequenciesOnTotalDocument() {
        System.out.println("getSortedWordsFrequenciesOnTotalDocument");
        HashMap<String, Double> wordFrequencies = new HashMap<String, Double>();
        wordFrequencies.put("word1", 4.0/100.0);
        wordFrequencies.put("word2", 15.0/100.0);
        HashMap<String, Double> wordFrequenciesOnTotalDocument = new HashMap<String, Double>();
        wordFrequenciesOnTotalDocument.put("word1", (1.0/3.0)/wordFrequencies.get("word1"));
        wordFrequenciesOnTotalDocument.put("word2", (2.0/3.0)/wordFrequencies.get("word2"));
        ValueComparatorDouble bvc = new ValueComparatorDouble(wordFrequenciesOnTotalDocument);
        TreeMap<String, Double> expResult = new TreeMap<String, Double>(bvc);
        expResult.putAll(wordFrequenciesOnTotalDocument);
        TreeMap<String, Double>  result = this.wc.getSortedWordsFrequenciesOnTotalDocument(wordFrequencies);
        Iterator iresult = result.entrySet().iterator();
        Iterator iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.mergedWc.getSortedWordsFrequenciesOnTotalDocument(wordFrequencies);
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
        result = this.wc.getSortedWordsFrequenciesOnTotalDocument(wordFrequencies);
        iresult = result.entrySet().iterator();
        iexpresult = expResult.entrySet().iterator();
        while(iresult.hasNext()){
            Entry r = (Entry) iresult.next();
            Entry er = (Entry) iexpresult.next();
            assertEquals(r.getKey(), er.getKey());
            assertEquals(r.getValue(), er.getValue());
        }
    }
}
