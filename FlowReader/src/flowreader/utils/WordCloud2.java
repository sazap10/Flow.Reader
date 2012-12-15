/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author D-Day
 */
public class WordCloud2 {
    
    private HashMap<String, Integer> wordsOccurrences;
    
    WordCloud2(HashMap<String, Integer> wordsOccurrences){
        this.wordsOccurrences = wordsOccurrences;
    }
    
    void sortWordsOccurrences(){
        // Ajout des entrées de la map à une liste
        final List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(this.wordsOccurrences.entrySet());

        // Tri de la liste sur la valeur de l'entrée
        Comparator compareValue = new Comparator<Entry<String, Integer>>() {
          public int compare(final Entry<String, Integer> e1, final Entry<String, Integer> e2) {
            return e1.getValue().compareTo(e2.getValue());
          }
        };
        Collections.sort(entries, compareValue);

        // Affichage du résultat
        for (final Entry<String, Integer> entry : entries) {
          System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
    
}
