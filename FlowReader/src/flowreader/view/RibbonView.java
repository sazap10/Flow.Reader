/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;
 
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 *
 * @author D-Day
 */
public class RibbonView {
    
    private ArrayList<Rectangle> pages;
    Group root;
    int pageWidth = 100;
    int pageHeight = 100;
    int pageInterval = 5;
    int pagesNumber = 30;
    
    public RibbonView(){
        this.buildRibbon();
    }
    
    public void buildRibbon(){
        this.root = new Group();
        
        this.pages = new ArrayList<>();
        
        int i = 0;
        int x = 0;
        int y = 0;
        while(i<pagesNumber){
            x = x + pageWidth + pageInterval;
            Rectangle page = RectangleBuilder.create()
             .x(x)
             .y(y)
             .width(pageWidth)
             .height(pageHeight)
             .fill(Color.FLORALWHITE)
             .build();
            this.pages.add(page);
            root.getChildren().add(page);
            i++;
        }
    } 
    
    public void zoomIn(){
        pageWidth = pageWidth + 10;
        pageHeight = pageHeight + 10;
        pageInterval = pageWidth + 5;
        int x = 0;
        for (int i=0; i<pages.size(); i++){
            pages.get(i).setHeight(pageHeight);
            pages.get(i).setWidth(pageWidth);
            x = x + pageInterval;
            pages.get(i).setX(x);
        }
    }
    
    public void zoomOut(){
        pageWidth = pageWidth - 10;
        pageHeight = pageHeight - 10;
        pageInterval = pageWidth + 5;
        int x = 0;
        for (int i=0; i<pages.size(); i++){
            pages.get(i).setHeight(pageHeight);
            pages.get(i).setWidth(pageWidth);
            x = x + pageInterval;
            pages.get(i).setX(x);
        }
    }
    
    public Group getRoot(){
        return this.root;
    }
    
   
}
