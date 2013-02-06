/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Page;
import flowreader.view.PageView;
import java.util.ArrayList;

/**
 *
 * @author D-Day
 */
public class DivePagesRibbonPane extends DiveRibbonPane{
    
    public DivePagesRibbonPane(ArrayList<Page> pages, double x, double y){
         super(x, y, PageView.width, PageView.height);
         // Creation of the pages
         for(Page p : pages){
            this.ribbonElts.add(new DivePage(p.getText(), x, y, this.elementWidth, this.elementHeight));
            x = x + this.elementWidth + this.elementInterval;
         }
         
         this.ribbon.getChildren().addAll(this.ribbonElts);
         this.getChildren().add(this.ribbon);
    }
}
