/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public class DiveWcRibbonPane extends DiveRibbonPane{

    public DiveWcRibbonPane(ArrayList<WordCloud> wordClouds, double x, double y) {
        super(x, y, DiveWordCloud.width, DiveWordCloud.heigth);
        
        // Creation of the word clouds
        for(WordCloud wc : wordClouds){
            this.ribbonElts.add(new DiveWordCloud(wc, x, y, this.elementWidth, this.elementHeight));
            x = x + this.elementWidth + this.elementInterval;
        }
        //this.ribbon.getChildren().addAll(this.ribbonElts);
        this.getChildren().add(this.ribbon);
    }
    
}
