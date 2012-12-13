/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.data;
import flowreader.core.Page;
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

    public TextFileReader_WordCloud(Page page) {
        this.words = new HashMap<>();
        this.wordObjects = new ArrayList<>();
        this.commonWords = new HashMap<>();
        readTextFromPage(page);
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

    private void countWords(String text) {

        if (text != null) {
            ArrayList<String> characters;
            for (String i : text.split(" ")) {
                i = this.trimPunctuation(i);
                if (!(i.equals(""))) {
                    if (this.words.get(i) == null) {
                        words.put(i, 1);
                    } else {
                        if (!this.commonWords.containsKey(i)) {
                            Integer count = this.words.get(i);
                            count++;
                            this.words.put(i, count);
                        }
                    }
                }
            }
        }
    }

    
    public void readTextFromPage(Page page){
        String text = page.getText();
        countWords(text);
        
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

    public void getCommonWords() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(System.getProperty("user.dir")+System.getProperty("file.separator")+"CommonEnglishWords.txt"));
            
            String temp_text;
            while ((temp_text = bufferedReader.readLine()) != null) {
                System.out.println(temp_text);
                this.commonWords.put(temp_text, 1);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't find the file!");
        } catch (IOException ex) {
            System.out.println("no idea!.. some IOException");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                System.out.println("couldn't close the file!");
            }
        }


    }

    public String trimPunctuation(String word) {
        return word.replaceAll("\\W", "");

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
