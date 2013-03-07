/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author jim
 */
public interface FileReader {
    
    public Document readFile(Rectangle bounds);
    
}
