package flowreader.view;

import flowreader.model.WordCloud;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
    private WordCloud wordCloud;
    private ArrayList<Text> words;
    private FlowPane cloud;
    private Integer maxFontSize = 100;
    private Integer minFontSize = 14;
    private Integer numOfWordsInCloud = 30;

    public WordCloudView(WordCloud wordCloud, Rectangle boundary) {
        wordCloudBoundary = boundary;
        wordCloudBoundary.setFill(Color.TRANSPARENT);
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

    private void renderWordCloud(){
        Set<Map.Entry<String, Integer>> w = this.wordCloud.getSortedWordOccurrences().entrySet();
        Integer[] values;
        Color col1 = Color.BLUE;
        Color col2 = Color.RED;
        if(w.size()>this.numOfWordsInCloud){
            values = new Integer[this.numOfWordsInCloud];
        }
        else{
            values = new Integer[w.size()];
        }
        
        Iterator i = w.iterator();
        int j = 0;
        while(j<this.numOfWordsInCloud && i.hasNext()) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)i.next();
            values[j] = e.getValue();
            Text word = new Text(e.getKey());
            //col = col.brighter();
            //System.out.println(""+col.getBlue());
            //col = new Color(col.getRed(), col.getGreen()-(1.0/(this.numOfWordsInCloud.doubleValue()+1)), col.getBlue()- (1.0/(this.numOfWordsInCloud.doubleValue()+1)), 1);
                   
            word.setFill(gradedValue(col2, col1, ((double)j/this.numOfWordsInCloud.doubleValue())));
            this.words.add(word);
            j++;
	}       
        
        //System.out.println("New Cloud");
        for(int k=0; k<values.length; k++){
            this.setWordSize(this.words.get(k), values[k], values);
            this.words.get(k).setWrappingWidth(this.words.get(k).getLayoutBounds().getWidth()+10);
            //System.out.println(this.words.get(k).getText()+" "+values[k]);
        }
        
        Collections.shuffle(this.words);
        this.cloud.getChildren().addAll(this.words);
    }
    
    private Color gradedValue(Color beginColor, Color endColor, double percent) {
        double red = beginColor.getRed() + (double)(percent * (endColor.getRed() - beginColor.getRed()));
        double blue = beginColor.getBlue() + (double)(percent * (endColor.getBlue() - beginColor.getBlue()));
        double green = beginColor.getGreen() + (double)(percent * (endColor.getGreen() - beginColor.getGreen()));
        return new Color(red, green, blue, 1);
    }
    
    //assigns them a font size to a word object
   private void setWordSize(Text word, int wordFrequency, Integer[] values) {
        int current = wordFrequency - values[values.length-1];

        int max = values[0] - values[values.length-1];
        int percent;
        if(max != 0){
            percent = (current*100)/max;
        }
        else{
            percent = 50;
        }
        
        int adjustMaxFontSize = this.maxFontSize - this.minFontSize;
        int adjustCurrentFontSize = (percent*adjustMaxFontSize)/100;
        int currentFontSize = adjustCurrentFontSize + this.minFontSize;

        word.setFont(new Font(currentFontSize));
    }
}