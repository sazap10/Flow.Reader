/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.PDFPage;
import flowreader.utils.Parameters;
import javafx.scene.image.ImageView;

/**
 *
 * @author D-Day
 */
public class PDFPageView extends PageView {
    private ImageView image;
    
     public PDFPageView(PDFPage page){
        super(0,0);
        this.image = new ImageView(page.getImage());
        this.image.setFitWidth(Parameters.pageWidth);
        this.image.setFitHeight(Parameters.pageHeight);
        this.getChildren().add(this.image);
    }
     
     @Override
    public void setHighlight(boolean on) {
        if(on){
            this.pageBoundary.setOpacity(1);
            this.image.setOpacity(1);
        }
        else{
            this.pageBoundary.setOpacity(0.5);
            this.image.setOpacity(0.5);
        }
    }
    
    
}
