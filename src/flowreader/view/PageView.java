/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.utils.Parameters;
import javafx.scene.shape.Rectangle;

/**
 * Visual representation of a page
 * @author D-Day
 */
public abstract class PageView extends RibbonElement {
    protected Rectangle pageBoundary;

    public PageView(double x, double y) {
        pageBoundary = new Rectangle(x, y, Parameters.pageWidth, Parameters.pageHeight);
        pageBoundary.setId("page_background");
        this.getChildren().add(pageBoundary);
    }

    /**
     * @return the width of a page
     */
    public double getPageWidth() {
        return pageBoundary.getWidth();
    }
    
    /**
     * @return the x coordinate of a page
     */
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
