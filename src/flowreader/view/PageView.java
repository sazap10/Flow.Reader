/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public abstract class PageView extends Group {

    protected Rectangle pageBoundary;
    public static double width;
    public static double height;

    public PageView(double x, double y) {
        pageBoundary = new Rectangle(x, y, width, height);
        pageBoundary.setFill(Color.FLORALWHITE);
        this.getChildren().add(pageBoundary);
    }

    public void setPageWidth(double width) {
        pageBoundary.setWidth(width);
    }

    public void setPageHeight(double height) {
        pageBoundary.setHeight(height);
    }

    public static void setUpPageSize(double width, double height) {
        TxtPageView.width = width;
        TxtPageView.height = height;
    }

    public double getPageWidth() {
        return pageBoundary.getWidth();
    }

    public double getPageHeight() {
        return pageBoundary.getHeight();
    }
    
    public void setX(double x) {
        pageBoundary.setX(x);
    }

    public double getX() {
        return pageBoundary.getX();
    }
}
