package flowreader.model;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author D-Day
 */
public class Page {

    private String text;
    private TreeMap<String, Integer> wordsOccurrences;

    public Page(String text, TreeMap<String, Integer> wordsOccurrences) {
        this.text = text;
        this.wordsOccurrences = wordsOccurrences;
    }

    public String getText() {
        return this.text;
    }

    public TreeMap<String, Integer> getWordsOccurrences() {
        return this.wordsOccurrences;
    }

    @Override
    public String toString() {
        String s = "PAGE \n " + text + "\n";

        for (Map.Entry<String, Integer> entree : this.wordsOccurrences.entrySet()) {
            System.out.println("Clé : " + entree.getKey() + " Valeur : " + entree.getValue());
        }
        return s + "\n";
    }
}
