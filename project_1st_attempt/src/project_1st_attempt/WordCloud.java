/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project_1st_attempt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

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
    //ArrayList<Integer> indexes = new ArrayList<Integer>();
   // ArrayList<Word> wordObjects = new ArrayList<Word>();
   //  for (int i = 0; i <= wordObjects.size() -1; i ++){
  ////       indexes.add(i, i);
   //  }
     Collections.shuffle(wordObjects);
   //  System.out.println("PRINTING INDEXES!");
  //   System.out.println(indexes);
    //create the basic rectangle (usually in terms of class attributes)
    Group cloud = new Group();
    Rectangle r = new Rectangle();
    r.setX(0);
    r.setY(0);
    r.setWidth(1500);
    r.setHeight(500);
    r.setArcWidth(100);
    r.setArcHeight(100);
    r.setFill(Color.WHITE);
    
    //these would normally be inputs to the function
    
    
    double maxWidth = 1000;
    double maxHeight = 800;
    double spacing = 20;
    double originX = (Screen.getPrimary().getVisualBounds().getWidth() / 2) - (maxWidth / 2);
    double originY = (Screen.getPrimary().getVisualBounds().getHeight() / 2) - (maxHeight / 2);
    double currX = originX;
    double currY = originY;
    double currHighest = 0;
    ArrayList<ArrayList<Word>> lines = new ArrayList<ArrayList<Word>>();
    ArrayList<Double> highest = new ArrayList<Double>();
    int currentLine = 0;
    lines.add(new ArrayList<Word>());
    
    for (Word word : wordObjects ){
     Text currText = new Text();   
     currText.setText(word.getText());
     currText.setFont(new Font(word.getFontSize()));
     double wordWidth = currText.getBoundsInLocal().getWidth();
     double wordHeight = currText.getBoundsInLocal().getHeight();
     
     if ((wordWidth + currX) <= maxWidth){
      //fits on the same line
         if (wordHeight > currHighest){
             currHighest = wordHeight;
         
         }
          currText.setX(currX);
          currText.setY(currY);
          currX = wordWidth + currX + spacing;
          //this.words.getChildren().add(currText);       
         ArrayList<Word> tmpLine = lines.get(currentLine);
         tmpLine.add(word);
         lines.add(currentLine, tmpLine);
         
     }else{
         if((wordHeight + currY) <= maxHeight){
             
             //start new line
         
             currY = wordHeight + currY;
             currX = originX;
             currText.setX(currX);
             currText.setY(currY);
             //this.words.getChildren().add(currText);
             currX += wordWidth + spacing;
              //add current highest to previous
           
            
             currentLine++;
             highest.add(currHighest);
             ArrayList<Word> tmpLine = new ArrayList<Word>();
             tmpLine.add(word);
             lines.add(currentLine, tmpLine);
             currHighest = 0;
           
             
             //need some kind of 'renderWord(x,y)' here
         }
         else{
             System.out.println("finished, because it doesn't fit at all!");
             break; //doesn't fit
         }
     }
    
    }
    
    int renderX = 0;
    int renderY = 0;
    for (int i = 0; i <= currentLine-1; i++){
       
           renderY += highest.get(i);  
        
       
        renderX = 0;
        for (Word word : lines.get(i)){
             Text currText = new Text();   
             currText.setText(word.getText());
             currText.setFont(new Font(word.getFontSize()));
             double wordWidth = currText.getBoundsInLocal().getWidth();
             currText.setX(renderX);
             currText.setY(renderY);
             this.words.getChildren().add(currText);
             renderX += (wordWidth + spacing) ;
        }
    }
    //this.words.getChildren().add(r);
   
 }
 
 
 

} 