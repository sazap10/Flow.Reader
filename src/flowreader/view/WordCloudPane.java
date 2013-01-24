/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public class WordCloudPane extends StackPane{
    private Group wordCloudGroup;
    private ArrayList<WordCloudView> wordCloudsView;
    
    private int wordCloudWidth = 500;
    private int wordCloudHeight = 500;
    private int wordCloudInterval = 5;
    
    public WordCloudPane(ArrayList<WordCloud> wordClouds, double gx, double gy){
        this.wordCloudGroup = new Group();
        this.wordCloudGroup.setLayoutX(gx);
        this.wordCloudGroup.setLayoutY(gy);
        this.wordCloudsView = new ArrayList<>();
        
        // Creation of the word clouds
        double x = 0;
        double y = 0;
        for(WordCloud wc : wordClouds){
            wordCloudsView.add(new WordCloudView(wc, new Rectangle(x, y, this.wordCloudWidth, this.wordCloudHeight)));
            x = x + this.wordCloudWidth + this.wordCloudInterval;
        }
        this.wordCloudGroup.getChildren().addAll(this.wordCloudsView);
        this.getChildren().add(this.wordCloudGroup);
    }
    
        public WordCloudPane(ArrayList<WordCloud> wordClouds, int x, int y){
        this.wordCloudGroup = new Group();
        this.wordCloudsView = new ArrayList<>();
        
        // Creation of the word clouds
        for(WordCloud wc : wordClouds){
            wordCloudsView.add(new WordCloudView(wc, new Rectangle(x, y, this.wordCloudWidth, this.wordCloudHeight)));
            x = x + this.wordCloudWidth + this.wordCloudInterval;
        }
        this.wordCloudGroup.getChildren().addAll(this.wordCloudsView);
        this.getChildren().add(this.wordCloudGroup);
    }

    void setNewPosition(double posX, double posY) {
        this.wordCloudGroup.setLayoutX(posX);
        this.wordCloudGroup.setLayoutY(posY);
    }
    
    void move(double dX, double dY) {
        this.wordCloudGroup.setLayoutX(this.wordCloudGroup.getLayoutX()+dX);
        this.wordCloudGroup.setLayoutY(this.wordCloudGroup.getLayoutY()+dY);
    }
    
    double getGroupWidth(){
        return this.wordCloudGroup.getBoundsInLocal().getWidth();
    }
    
}
