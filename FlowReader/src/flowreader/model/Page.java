/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author D-Day
 */
public class Page{

    private String text;
    private HashMap<String, Integer> wordsOccurrences;

    public Page(String text, HashMap<String, Integer> wordsOccurrences) {
        this.text = text;
        this.wordsOccurrences = wordsOccurrences;
        Iterator i = wordsOccurrences.keySet().iterator();
        while(i.hasNext()){
            String word = (String)i.next();
            System.out.println(""+word+": "+wordsOccurrences.get(word));
        }
    }
    
    public String getText(){
        return this.text;
    }
    
    public HashMap<String, Integer> getWordsOccurrences(){
        return this.wordsOccurrences;
    }
}
