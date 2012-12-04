/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project_1st_attempt;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author jim
 */
public class WordCloud {
    private Group words;
    
    
    public WordCloud(){
        this.words = new Group();
    }
    
   public void renderCloud(ArrayList<Word> wordObjects ){
     
    //create the basic rectangle (usually in terms of class attributes)
    Group cloud = new Group();
    Rectangle r = new Rectangle();
    r.setX(50);
    r.setY(50);
    r.setWidth(200);
    r.setHeight(100);
    r.setArcWidth(20);
    r.setArcHeight(20);
    
    //these would normally be inputs to the function
    double originX = 50;
    double originY = 50;
    double currX = originX;
    double currY = originY;
    double maxWidth = 200;
    double maxHeight = 100;
    
    for (Word word : wordObjects ){
     Text currText = new Text();
     currText.setFont(Font.font("Veranda", word.getFontSize()));
     currText.setText(word.getText());
     double wordWidth = currText.getBoundsInLocal().getWidth();
     double wordHeight = currText.getBoundsInLocal().getHeight();
     if ((wordWidth + currX) > maxWidth){
         if ((wordHeight + currY) > maxHeight){
             break; // word won't fit on rectangle
         }
         else{
             //start new line
             currY = wordHeight + currY;
             currX = originX;
             currText.setX(currX);
             currText.setY(currY);
             this.words.getChildren().add(currText);
             //need some kind of 'renderWord(x,y)' here
         }
     }
     else{
         currY = wordHeight + currY;
         currX = wordWidth + currX;
          currText.setX(currX);
          currText.setY(currY);
          this.words.getChildren().add(currText);
         //need some kind of 'renderWord(x,y)' here
     }
    }
    this.words.getChildren().add(r);
   
 }
 
 
 

} 