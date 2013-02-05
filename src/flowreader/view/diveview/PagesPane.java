/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Page;
import flowreader.view.PageView;
import flowreader.view.RibbonPane;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public class PagesPane extends RibbonPane{
    private Group pageGroup;
    private ArrayList<PageView> pagesView;
    
    private int pageInterval = 5;
    
    public PagesPane(ArrayList<Page> pages, double x, double y){
        this.pageGroup = new Group();
         this.pagesView = new ArrayList<>();
         
         // Creation of the pages
         for(Page p : pages){
            this.pagesView.add(new PageView(x, y, p.getText()));
            x = x + PageView.width + this.pageInterval;
         }
         
         this.pageGroup.getChildren().addAll(this.pagesView);
         this.getChildren().add(this.pageGroup);
    }

    @Override
    public void setNewPosition(double posX, double posY) {
        this.pageGroup.setLayoutX(posX);
        this.pageGroup.setLayoutY(posY);
    }

    @Override
    public void move(double dX, double dY) {
        this.pageGroup.setLayoutX(this.pageGroup.getLayoutX()+dX);
        this.pageGroup.setLayoutY(this.pageGroup.getLayoutY()+dY);
    }
    
}
