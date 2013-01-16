package flowreader.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class WordCloudView extends Group{

    private Rectangle wordCloudBoundary;
    private TreeMap<String, Integer> wordsOccurrences;
    private ArrayList<Text> words;
    private FlowPane cloud;
    private Integer maxFontSize = 500;
    private Integer minFontSize = 14;
    private Integer numOfWordsInCloud = 10;
    private Integer normalizationConstant = 3;

    public WordCloudView(Rectangle boundary) {
        wordCloudBoundary = boundary;
        wordCloudBoundary.setFill(Color.ALICEBLUE);
        this.words = new ArrayList<>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());
        
        this.getChildren().addAll(wordCloudBoundary, cloud);
        
    }
    
     public WordCloudView (WordCloudView a, WordCloudView b, Rectangle boundary){    
        this(boundary);         
        TreeMap<String,Integer> bMap = b.getWordOccurrences();       
        Set<Map.Entry<String, Integer>> w = bMap.entrySet();     
        Iterator i = w.iterator();
        this.setWordOccurrences(a.getWordOccurrences());      
        
        //walk through cloud b and add it's words
        while (i.hasNext()){   
    
            Set<Map.Entry<String, Integer>> occurrences = wordsOccurrences.entrySet(); 
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)i.next();      
            String key = e.getKey();
            int value = e.getValue();
            
            //if word exists, use sum of counts
            if (wordsOccurrences.containsKey(key)){
                int count = wordsOccurrences.get(key) + value;
                wordsOccurrences.put(key, count);
            }
            else{             
                wordsOccurrences.put(key, value);
                
                
            }
         
          
            
        }
        
    }
    
    public double getPageWidth() {
        return wordCloudBoundary.getWidth();
    }

    public double getPageHeight() {
        return wordCloudBoundary.getHeight();
    }
    
    public void setPageWidth(double width) {
        wordCloudBoundary.setWidth(width);
    }

    public void setPageHeight(double height) {
        wordCloudBoundary.setHeight(height);
    }

    public void setX(double x) {
        wordCloudBoundary.setX(x);
        System.out.println("x: " + x + "pageBoundary Width: " + wordCloudBoundary.getWidth());

    }

    public double getX() {
        return wordCloudBoundary.getX();
    }

    void setWordOccurrences(TreeMap<String, Integer> wordsOccurrences) {
        this.wordsOccurrences = wordsOccurrences;
        this.renderWordCloud();
    }
    
    TreeMap<String, Integer> getWordOccurrences(){
        return this.wordsOccurrences;
    }
    
    void renderWordCloud(){
        Set<Map.Entry<String, Integer>> w = this.wordsOccurrences.entrySet();
        Iterator i = w.iterator();
        int j = 0;
        while(j<this.numOfWordsInCloud && i.hasNext()) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)i.next();
            Text word = new Text(e.getKey());
            //this.setWordSizes(word, e.getValue());
            word.setFont(new Font(10*e.getValue()));
            word.setWrappingWidth(word.getLayoutBounds().getWidth()+10);
            this.words.add(word);
            j++;
	}
        Collections.shuffle(this.words);
        this.cloud.getChildren().addAll(this.words);
    }
    
    //iterates through the word objects and assigns them a font size
    private void setWordSizes(Text word, int numberOfOccurrences) {
        int countDiff = numberOfOccurrences - this.wordsOccurrences.firstEntry().getValue();
        int totalCountDiff = this.wordsOccurrences.lastEntry().getValue() - this.wordsOccurrences.firstEntry().getValue();
        int fontSize = ((this.maxFontSize * countDiff) / (this.normalizationConstant * totalCountDiff));
        if (fontSize < this.minFontSize) {
            fontSize = this.minFontSize;
        }
        word.setFont(new Font(fontSize));
    }
}