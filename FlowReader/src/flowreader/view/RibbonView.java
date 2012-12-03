/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;
 
import java.util.ArrayList;

import flowreader.core.Page;
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
    
    private ArrayList<Page> pages;
    Group root;
    int pageWidth = 200;
    int pageHeight = 200;
    int pageInterval = 5;
    int pagesNumber = 30;
    int maxPageWidth = 700;
    int maxPageHeight = 700;
    int minPageWidth = 200;
    int minPageHeight = 200;
    
    public RibbonView(){
    }
    
    public void buildRibbon(){
        this.root = new Group();
        
        this.pages = new ArrayList<Page>();
        
        int i = 0;
        int x = 0;
        int y = 0;
        while(i<pagesNumber){
            x = x + pageWidth + pageInterval;
            Page page = new Page(new Rectangle(x,y,pageWidth,pageHeight));
            System.out.println(page.getX());
            this.pages.add(page);
            root.getChildren().add(page.getPage());
            i++;
        }
    } 
    
    public void zoomIn(){
        if(pageWidth<this.maxPageWidth && pageHeight<this.maxPageHeight){
            pageWidth = pageWidth + 10;
            pageHeight = pageHeight + 10;
            pageInterval = pageWidth + 5;
            int x = 0;
            for (int i=0; i<pages.size(); i++){
                pages.get(i).setPageHeight(pageHeight);
                pages.get(i).setPageWidth(pageWidth);
                x = x + pageInterval;
                pages.get(i).setX(x);
            }
        }
    }
    
    public void zoomOut(){
        if(pageWidth>this.minPageWidth && pageHeight>this.minPageHeight){
            pageWidth = pageWidth - 10;
            pageHeight = pageHeight - 10;
            pageInterval = pageWidth + 5;
            int x = 0;
            for (int i=0; i<pages.size(); i++){
                pages.get(i).setPageHeight(pageHeight);
                pages.get(i).setPageWidth(pageWidth);
                x = x + pageInterval;
                pages.get(i).setX(x);
            }
        }
    }
    
    public Group getRoot(){
        return this.root;
    }
    
   
}
