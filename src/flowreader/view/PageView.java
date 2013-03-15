/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.utils.Parameters;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public abstract class PageView extends RibbonElement {

    protected Rectangle pageBoundary;
    //public static double width;
    //public static double height;

    public PageView(double x, double y) {
        pageBoundary = new Rectangle(x, y, Parameters.pageWidth, Parameters.pageHeight);
        //pageBoundary.setFill(Color.FLORALWHITE);
        pageBoundary.setId("page_background");
        this.getChildren().add(pageBoundary);
    }

    public void setPageWidth(double width) {
        pageBoundary.setWidth(width);
    }

    public void setPageHeight(double height) {
        pageBoundary.setHeight(height);
    }

    /*public static void setUpPageSize(double width, double height) {
        PageView.width = width;
        PageView.height = height;
    }*/

    public double getPageWidth() {
        return pageBoundary.getWidth();
    }

    public double getPageHeight() {
        return pageBoundary.getHeight();
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
}
