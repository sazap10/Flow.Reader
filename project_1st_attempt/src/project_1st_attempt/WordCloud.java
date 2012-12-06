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
import javafx.scene.paint.Color;

/**
 *
 * @author jim
 */
public class WordCloud {
    public Group words;
    
    
    public WordCloud(){
        this.words = new Group();
    }
    
   public void renderCloud(ArrayList<Word> wordObjects ){
     
    //create the basic rectangle (usually in terms of class attributes)
    Group cloud = new Group();
    Rectangle r = new Rectangle();
    r.setX(50);
    r.setY(50);
    r.setWidth(1500);
    r.setHeight(500);
    r.setArcWidth(100);
    r.setArcHeight(100);
    r.setFill(Color.WHITE);
    
    //these would normally be inputs to the function
    double originX = 50;
    double originY = 50;
    double currX = originX;
    double currY = originY;
    double maxWidth = 200;
    double maxHeight = 100;
    
    for (Word word : wordObjects ){
     Text currText = new Text();   
     currText.setText(word.getText());
     currText.setFont(new Font(word.getFontSize()));
     double wordWidth = currText.getBoundsInLocal().getWidth();
     double wordHeight = currText.getBoundsInLocal().getHeight();
     if ((wordWidth + currX) <= maxWidth){
      //if it doesn't fit on the current line
          System.out.println("goes on the same line!");
          System.out.println("word text:" + word.getText());
          System.out.println("font size:" + word.getFontSize().toString());
          System.out.println("width:" + wordWidth);
          currY = currY;
          currX = currX;
          currText.setX(currX);
          currText.setY(currY);
          currX = wordWidth + currX;
          this.words.getChildren().add(currText);       
         
     }else{
         if((wordHeight + currY) <= maxHeight){
             
             //start new line
             System.out.println("goes on a new line!");
             currY = wordHeight + currY;
             currX = originX;
             currText.setX(currX);
             currText.setY(currY);
             this.words.getChildren().add(currText);
             currX += wordWidth;
             //need some kind of 'renderWord(x,y)' here
         }
         else{
             System.out.println("finished, because it doesn't fit at all!");
             break; //doesn't fit
         }
     }
    
    }
    //this.words.getChildren().add(r);
   
 }
 
 
 

} 