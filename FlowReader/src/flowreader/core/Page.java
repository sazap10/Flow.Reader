/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.core;

import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
    }
    
    public String getText(){
        return this.text;
    }
    
    public HashMap<String, Integer> getWordsOccurrences(){
        return this.wordsOccurrences;
    }
}
