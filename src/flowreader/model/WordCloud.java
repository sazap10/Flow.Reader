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
public class WordCloud {
    // Occurences of each word
    private HashMap<String, Integer> wordsOccurrences;
    private TreeMap<String, Integer> sortedWordsOccurrences;
    // Occurences of each word / Total number of words in this part of the document
    private HashMap<String, Double> wordsFrequencies;
    private TreeMap<String, Double> sortedWordsFrequencies;
    // frequency of each word / frequency of this word in the document
    private HashMap<String, Double> wordsFrequenciesOnTotalDocument;
    private TreeMap<String, Double> sortedWordsFrequenciesOnTotalDocument;

    public WordCloud(HashMap<String, Integer> wordsOccurrences) {
        this.wordsOccurrences = wordsOccurrences;
        this.calculeWordsFrequencies();
        this.sortedWordsOccurrences = this.sortOccurrences(wordsOccurrences);
    }

    /**
     * Merge the word cloud a with the word cloud b
     *
     * @param a
     * @param b
     */
    public WordCloud(WordCloud a, WordCloud b) {
        HashMap<String, Integer> bMap = b.getWordOccurrences();
        this.wordsOccurrences = a.getWordOccurrences();

        //walk through cloud b and add it's words
        for (String word : bMap.keySet()) {

            if (wordsOccurrences.containsKey(word)) {
                int count = wordsOccurrences.get(word) + bMap.get(word);
                wordsOccurrences.put(word, count);
            } else {
                wordsOccurrences.put(word, bMap.get(word));
            }
        }
        this.calculeWordsFrequencies();
        this.sortedWordsOccurrences = this.sortOccurrences(wordsOccurrences);
    }

    /**
     * @return the words occurrences
     */
    public HashMap<String, Integer> getWordOccurrences() {
        return this.wordsOccurrences;
    }

    /**
     * @return the word occurrences sorted
     */
    public TreeMap<String, Integer> getSortedWordOccurrences() {
        return this.sortedWordsOccurrences;
    }

    /**
     * @return the words frequencies
     */
    public HashMap<String, Double> getWordFrequencies() {
        return this.wordsFrequencies;
    }

    /**
     * @return the words frequencies sorted
     */
    public TreeMap<String, Double> getSortedWordFrequencies() {
        return this.sortedWordsFrequencies;
    }

    /**
     * @param totalDocumentWordFrequencies
     * @return the frequency of each word divided by the frequency of this word in the document
     */
    public HashMap<String, Double> getWordsFrequenciesOnTotalDocument(HashMap<String, Double> totalDocumentWordFrequencies) {
        if (this.wordsFrequenciesOnTotalDocument == null) {
            this.wordsFrequenciesOnTotalDocument = new HashMap<String, Double>();

            for (Entry e : this.wordsFrequencies.entrySet()) {
                String key = e.getKey().toString();
                Double value = (Double) (((Double) e.getValue()) / (Double) totalDocumentWordFrequencies.get(key));
                this.wordsFrequenciesOnTotalDocument.put(key, value);
            }
        }
        return this.wordsFrequenciesOnTotalDocument;
    }

    /**
     * @param totalDocumentWordFrequencies
     * @return the frequency of each word divided by the frequency of this word in the document
     */
    public TreeMap<String, Double> getSortedWordsFrequenciesOnTotalDocument(HashMap<String, Double> totalDocumentWordFrequencies) {
        if (this.sortedWordsFrequenciesOnTotalDocument == null) {
            this.sortedWordsFrequenciesOnTotalDocument = this.sortFrequencies(this.getWordsFrequenciesOnTotalDocument(totalDocumentWordFrequencies));
        }
        return this.sortedWordsFrequenciesOnTotalDocument;
    }
    
    /**
     * Calcule the word frequencies and sort them
     */
    private void calculeWordsFrequencies() {
        Integer totalNbOfWords = 0;
        for (Entry e : this.wordsOccurrences.entrySet()) {
            totalNbOfWords = totalNbOfWords + (Integer) e.getValue();
        }

        this.wordsFrequencies = new HashMap<String, Double>();

        for (Entry e : this.wordsOccurrences.entrySet()) {
            String key = e.getKey().toString();
            Double value = (Double) (((Integer) e.getValue()).doubleValue() / totalNbOfWords.doubleValue());
            this.wordsFrequencies.put(key, value);
        }

        this.sortedWordsFrequencies = this.sortFrequencies(wordsFrequencies);
    }

    /**
     * @param wordsFrequencies
     * @return the word frequencies sorted in a TreeMap
     */
    private TreeMap<String, Double> sortFrequencies(HashMap<String, Double> wordsFrequencies) {
        ValueComparatorDouble bvc = new ValueComparatorDouble(wordsFrequencies);
        TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(bvc);
        sortedMap.putAll(wordsFrequencies);
        Set<Map.Entry<String, Double>> w = sortedMap.entrySet();
        Iterator j = w.iterator();
        return sortedMap;
    }

    /**
     *
     * @param wordsOccurrences
     * @return the word occurrences sorted in a TreeMap
     */
    private TreeMap<String, Integer> sortOccurrences(HashMap<String, Integer> wordsOccurrences) {
        ValueComparatorInteger bvc = new ValueComparatorInteger(wordsOccurrences);
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(wordsOccurrences);
        Set<Map.Entry<String, Integer>> w = sortedMap.entrySet();
        Iterator j = w.iterator();
        return sortedMap;
    }
}