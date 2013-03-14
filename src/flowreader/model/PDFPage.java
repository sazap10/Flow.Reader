/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import javafx.scene.image.WritableImage;

/**
 * A PDFpage is the representation of a page in a pdf document it contains the text of the page and also the image of it.
 * @author D-Day
 */
public class PDFPage extends Page{
    private WritableImage image;
    
    public PDFPage(String text, WritableImage image){
        super(text);
        this.image = image;
    }
    
    /**
     * @return the image of the page
     */
    public WritableImage getImage(){
        return image;
    }
    
}
