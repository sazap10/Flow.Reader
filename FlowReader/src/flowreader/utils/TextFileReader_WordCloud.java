/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;
import flowreader.model.Page;
import flowreader.view.PageView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Pat
 */
public class TextFileReader_WordCloud {

    private HashMap<String, Integer> words;
    private Integer numOfWordsInCloud = 30;
    private Integer maxFontSize = 500;
    public Integer minFontSize = 14;
    private Integer maxCount;
    private Integer minCount;
    private Integer normalizationConstant = 3;
    public ArrayList<Word> wordObjects;  //only public for basic testing
    private HashMap<String, Integer> commonWords;

    public TextFileReader_WordCloud(PageView page) {
        this.words = new HashMap<>();
        this.wordObjects = new ArrayList<>();
        this.commonWords = new HashMap<>();
        //readTextFromPage(page);
    }
    
    
    public TextFileReader_WordCloud(TextFileReader_WordCloud a, TextFileReader_WordCloud b){
        this.words = a.getWords();
         Iterator it = b.getWords().entrySet().iterator();
        while (it.hasNext()){
             Map.Entry pairs = (Map.Entry) it.next();
             if (this.words.containsKey(pairs.getKey().toString())){
                 int count = this.words.get(pairs.getKey().toString());
                 count += b.getWords().get(pairs.getKey().toString());
             }
             else{
                 this.words.put(pairs.getKey().toString(),Integer.parseInt(pairs.getValue().toString()));
             }         
            
        }
        
    }
    
    public HashMap<String, Integer> getWords(){
        return this.words;
    }
    
    public String getWordCount() {
        String output = "";
        Integer currentMax = 0;
        return this.getNodes();
        /*for (String i: words.keySet()){
         if (this.words.get(i) > currentMax){
         output = ("the word:" + i + ":" + "        " + this.words.get(i) + "\n");
         currentMax = this.words.get(i);
         }
       
         }
    
         return output;
         */
    }



    public String getNodes() {
        String output = "";
        String maxKey = "";
        int tmpCount;
        boolean maxCount = false;
        boolean minCount = false;
        for (int i = 0; i <= this.numOfWordsInCloud; i++) {
            Integer maxInMap = Collections.max(this.words.values());
            Iterator it = this.words.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pairs = (Map.Entry) it.next();
                //System.out.println(pairs.getKey() + " = " + pairs.getValue());

                tmpCount = Integer.parseInt(pairs.getValue().toString());
                if (tmpCount == maxInMap) {
                    output += (pairs.getKey().toString() + ":" + "        " + tmpCount + "\n");
                    maxKey = pairs.getKey().toString();
                    this.wordObjects.add(new Word(maxKey, tmpCount));
                    if (!(maxCount)) {
                        this.maxCount = tmpCount;
                        maxCount = true;
                    }
                }

            }
            this.words.remove(maxKey);
            this.minCount = maxInMap;
        }

        return output;
    }

    public void setWordSizes() {
        for (Word word : wordObjects) {
            int countDiff = word.getCount() - this.minCount;
            int totalCountDiff = this.maxCount - this.minCount;
            int fontSize = ((this.maxFontSize * countDiff) / (this.normalizationConstant * totalCountDiff));
            if (fontSize < this.minFontSize) {
                fontSize = this.minFontSize;
            }
            word.setFontSize(fontSize);

        }
    }
}
