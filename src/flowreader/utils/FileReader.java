/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import java.io.IOException;

/**
 *
 * @author Jim
 */
public interface FileReader {
    
    public Document readFile(double width, double height) throws IOException;
 //   public Document get() throws IOException;
    //public Document call() throws IOException;
    
}
