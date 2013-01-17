package flowreader.view;

import flowreader.utils.ValueComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import flowreader.model.WordCloud;

/**
 *
 * @author D-Day
 */
public class WordCloudView extends Group{

    private Rectangle wordCloudBoundary;
    private WordCloud wordCloud;
    private ArrayList<Text> words;
    private FlowPane cloud;
    private Integer maxFontSize = 500;
    private Integer minFontSize = 14;
    private Integer numOfWordsInCloud = 10;
    private Integer normalizationConstant = 3;

    public WordCloudView( WordCloud wordCloud, Rectangle boundary) {
        wordCloudBoundary = boundary;
        wordCloudBoundary.setFill(Color.ALICEBLUE);
        this.wordCloud = wordCloud;
        this.words = new ArrayList<>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());
        
        this.getChildren().addAll(wordCloudBoundary, cloud);
        renderWordCloud();
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

   


    
    void renderWordCloud(){
        TreeMap<String, Integer> sortedWordOccurrences = sortWordsOccurrences(wordCloud.getWordOccurrences());
        Set<Map.Entry<String, Integer>> w = sortedWordOccurrences.entrySet();
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
   /* private void setWordSizes(Text word, int numberOfOccurrences) {
        int countDiff = numberOfOccurrences - this.wordsOccurrences.firstEntry().getValue();
        int totalCountDiff = this.wordsOccurrences.lastEntry().getValue() - this.wordsOccurrences.firstEntry().getValue();
        int fontSize = ((this.maxFontSize * countDiff) / (this.normalizationConstant * totalCountDiff));
        if (fontSize < this.minFontSize) {
            fontSize = this.minFontSize;
        }
        word.setFont(new Font(fontSize));
    }
    * */
    
     private TreeMap<String, Integer> sortWordsOccurrences(HashMap<String, Integer> wordsOccurrences){
        ValueComparator bvc =  new ValueComparator(wordsOccurrences);
        TreeMap<String,Integer> sortedWordsOccurrences = new TreeMap<>(bvc);
        sortedWordsOccurrences.putAll(wordsOccurrences);
        
        //System.out.println("unsorted map: "+wordsOccurrences);
        //System.out.println("results: "+sortedWordsOccurrences);
       
        return sortedWordsOccurrences;
    }
}