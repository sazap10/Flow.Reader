/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.flowview;

import flowreader.model.Document;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public class FlowViewScene extends StackPane{
    private RibbonView rb;
    private Document document;
    
    public FlowViewScene(Document document){
        rb = new RibbonView(this);
        this.document = document;
    }
    
    public void build(){
       rb.buildRibbon(document);
       this.getChildren().add(rb);
    }
    
}

    