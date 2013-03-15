/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.WordCloud;
import flowreader.view.WordCloudView;
import java.util.ArrayList;

/**
 * Represents a ribbon of word clouds
 * @author D-Day
 */
public class DiveWcRibbonPane extends DiveRibbonPane{

    public DiveWcRibbonPane(int index, ArrayList<WordCloud> wordClouds, double x, double y) {
        super(index, x, y, WordCloudView.width, WordCloudView.heigth);
        
        // Creation of the word clouds
        for(WordCloud wc : wordClouds){
            this.ribbonElts.add(new WordCloudView(wc, x, y, this.elementWidth, this.elementHeight));
            x = x + this.elementWidth + this.elementInterval;
        }
    }
    
    @Override
    public void createRibbon(ArrayList<Integer> indexes) {
        this.getChildren().clear();
        this.ribbon.getChildren().clear();
        for (Integer i : indexes) {
            this.ribbon.getChildren().add(this.ribbonElts.get(i));
        }
        this.selected = indexes;
        this.getChildren().add(this.ribbon);
    }
    
    @Override
    public double getFocusPoint() {
        double focusSquareWidth = (this.elementWidth * this.ribbon.getChildren().size()) + (this.elementInterval * (this.ribbon.getChildren().size()));
        double focusPointInSquare = focusSquareWidth / 2.0;
        return focusPointInSquare;
    }
    
}
