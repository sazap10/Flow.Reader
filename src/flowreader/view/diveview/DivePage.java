/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.view.DiveRibbonElement;
import flowreader.view.PageView;
import flowreader.view.TxtPageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class DivePage extends DiveRibbonElement{
    private Rectangle pageBoundary, textBound;
    private Text pageText;
    
    
    DivePage(String text, double x, double y, double elementWidth, double elementHeigth) {
        pageBoundary = new Rectangle(x, y, elementWidth, elementHeigth);
        pageBoundary.setFill(Color.FLORALWHITE);
        textBound = new Rectangle(TxtPageView.textBoundWidth, TxtPageView.textBoundHeight);
        textBound.setFill(Color.TRANSPARENT);
        pageText = new Text();
        pageText.setX(pageBoundary.getX() + ((pageBoundary.getWidth() - textBound.getWidth()) * 0.5));
        pageText.setY(pageBoundary.getY() + ((pageBoundary.getHeight() - textBound.getHeight()) * 0.5));
        //page = new Group();
        this.getChildren().addAll(pageBoundary, pageText);
        pageText.setText(text);
    }
    
    public static void setUpPageSize(double width, double height){
        PageView.width = width;
        PageView.height = height;
        TxtPageView.textBoundWidth = width * 0.8;
        TxtPageView.textBoundHeight = height * 0.8;
    }

    public void setX(double x) {
        pageBoundary.setX(x);
        textBound.setX(x + (pageBoundary.getWidth() * 0.1));
        textBound.setY(pageBoundary.getY() + pageBoundary.getHeight() * 0.1);
        System.out.println("x: " + x + "pageBoundary Width: " + pageBoundary.getWidth());

    }

    public double getX() {
        return pageBoundary.getX();
    }

    @Override
    public void setHighlight(boolean on) {
        if(on){
            this.pageBoundary.setOpacity(1);
        }
        else{
            this.pageBoundary.setOpacity(0.5);
        }
    }
    
    public Rectangle pagebound(){
        return this.pageBoundary;
    }
    
}
