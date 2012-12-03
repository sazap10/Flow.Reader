/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.core;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class Page{
    private Rectangle pageBoundary,textBound;
    private Text pageText;
    private Group page;
    
    public Page(Rectangle boundary){
    	pageBoundary = boundary;
    	pageBoundary.setFill(Color.FLORALWHITE);
    	textBound = new Rectangle(boundary.getWidth()*0.8,boundary.getHeight()*0.8);
    	pageText = new Text();
    	pageText.setX(pageBoundary.getX()+((pageBoundary.getWidth()-textBound.getWidth())*0.5));
    	pageText.setY(pageBoundary.getY()+((pageBoundary.getHeight()-textBound.getHeight())*0.5));
    	page = new Group();
    	page.getChildren().addAll(pageBoundary,pageText);
    }
    
    public void setText(String text){
    	pageText.setText(text);
    }
    
    public double getPageWidth(){
    	return pageBoundary.getWidth();
    }
    
    public double getPageHeight(){
    	return pageBoundary.getHeight();
    }
    
    public void setPageWidth(double width){
    	  pageBoundary.setWidth(width);
    }
    
    public void setPageHeight(double height){
    	pageBoundary.setHeight(height);
    }
    
    public Group getPage(){
    	return page;
    }
    
    public void setX(double x){
    	pageBoundary.setX(x);
    }
    
    public double getX(){
    	return pageBoundary.getX();
    }
}
