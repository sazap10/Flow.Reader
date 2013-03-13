/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import javafx.scene.image.WritableImage;

/**
 *
 * @author D-Day
 */
public class PDFPage extends Page{
    private WritableImage image;
    
    public PDFPage(String text, WritableImage image){
        super(text);
        this.image = image;
    }
    
    public WritableImage getImage(){
        return image;
    }
    
}
