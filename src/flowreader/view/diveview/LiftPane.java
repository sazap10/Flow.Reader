/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;


import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represent the pane with the lift on the right side
 * @author D-Day
 */
public class LiftPane extends VBox{
    
    private ArrayList<StackPane> levels;
    
    public LiftPane(int level){
        this.setSpacing(5);
        this.getChildren().add(new Rectangle(50, 50, Color.TRANSPARENT));
        this.levels = new ArrayList<StackPane>();
        for(int i=0; i<level; i++){
            StackPane sp = new StackPane();
            Text t = new Text(25, 25, ""+(level-(i+1)));
            t.setFill(Color.WHITE);
            t.setFont(new Font(30));
            Rectangle r = new Rectangle(50, 50);
            r.setFill(Color.BROWN);
            sp.getChildren().add(r);
            sp.getChildren().add(t);
            this.levels.add(sp);
            this.getChildren().add(sp);
        }
    }
    
    /**
     * Highlight the index i and lowlight others
     * @param i 
     */
    public void setHighLight(int i){
        int k = this.levels.size()-(i+1);
        for(int j=0; j<this.levels.size(); j++){
            if(j==k){
                this.levels.get(j).setOpacity(1);
            }
            else{
                this.levels.get(j).setOpacity(0.2);
            }
        }
    }
    
}
