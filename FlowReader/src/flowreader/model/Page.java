package flowreader.model;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author D-Day
 */
public class Page {

    private String text;
    private int wordCount;

    public Page(String text) {
        this.text = text;
        wordCount = text.split(" ").length;

    }

    public String getText() {
        return this.text;
    }
    
    public int getWordCount(){
        return this.wordCount;
    }


    @Override
    public String toString() {
        String s = "PAGE \n " + text + "\n";

    
        return s + "\n";
    }
}
