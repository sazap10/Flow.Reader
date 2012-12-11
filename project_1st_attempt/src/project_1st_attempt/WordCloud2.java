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
import javafx.stage.Screen;

/**
 *
 * @author Jeegar
 */
public class WordCloud2 {

    public Group words;

    public WordCloud2() {
        this.words = new Group();
    }

    public void renderCloud(ArrayList<Word> wordObjects) {

        //create the basic rectangle (usually in terms of class attributes)
        //Group cloud = new Group();
        Rectangle r = new Rectangle();
        r.setX(50);
        r.setY(50);
        r.setWidth(1500);
        r.setHeight(500);
        r.setArcWidth(100);
        r.setArcHeight(100);
        r.setFill(Color.WHITE);

        //these would normally be inputs to the function


        double maxWidth = 800;
        double maxHeight = 400;
        double originX = (Screen.getPrimary().getVisualBounds().getWidth() / 2) - (maxWidth / 2);
        double originY = (Screen.getPrimary().getVisualBounds().getHeight() / 2) - (maxHeight / 2);
        double currX = 0;
        double currY = 0;

        for (Word word : wordObjects) {
            Text currText = new Text();
            currText.setText(word.getText());
            currText.setFont(new Font(word.getFontSize()));
            double wordWidth = currText.getBoundsInLocal().getWidth();
            double wordHeight = currText.getBoundsInLocal().getHeight();
            if ((wordWidth + currX) <= maxWidth) {
                System.out.println("goes on the same line!");
                System.out.println("word text:" + word.getText());
                System.out.println("font size:" + word.getFontSize().toString());
                System.out.println("width:" + wordWidth);
                currText.setX(originX + currX);
                currText.setY(originY + currY);
                currX = wordWidth + currX + 20;
                this.words.getChildren().add(currText);
            } else {
                if ((wordHeight + currY) <= maxHeight) {

                    //start new line
                    System.out.println("goes on a new line!");
                    currY = wordHeight + currY;
                    currX = 0;
                    currText.setX(originX+currX);
                    currText.setY(originY+currY);
                    this.words.getChildren().add(currText);
                    currX += wordWidth + 20;
                    //need some kind of 'renderWord(x,y)' here
                } else {
                    System.out.println("finished, because it doesn't fit at all!");
                    break; //doesn't fit
                }
            }

        }
    }
}
    //this.words.getChildren().add(r);