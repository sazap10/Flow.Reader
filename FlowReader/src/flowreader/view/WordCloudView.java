package flowreader.view;

import java.util.ArrayList;
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

    public WordCloudView(Rectangle boundary) {
        wordCloudBoundary = boundary;
        wordCloudBoundary.setFill(Color.TRANSPARENT);
        this.words = new ArrayList<>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());
        
        this.getChildren().addAll(wordCloudBoundary, cloud);
        
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
    
    void renderWordCloud(){
        Set<Map.Entry<String, Integer>> w = this.wordsOccurrences.entrySet();
        Iterator i = w.iterator();
        int j = 0;
        while(j<10 && i.hasNext()) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)i.next();
            Text word = new Text(e.getKey());
            word.setFont(new Font(10*e.getValue()));
            word.setWrappingWidth(word.getLayoutBounds().getWidth()+10);
            this.words.add(word);
            this.cloud.getChildren().add(word);
            j++;
	}
    }
}