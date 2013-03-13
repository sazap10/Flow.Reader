/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.PDFPage;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 *
 * @author D-Day
 */
public class PDFPageView extends PageView {
    private ImageView image;
    
    public PDFPageView(WritableImage image){
        super(0,0);
        this.image = new ImageView(image);
    }
    
     public PDFPageView(PDFPage page){
        super(0,0);
        this.image = new ImageView(page.getImage());
    }
    
    
}
