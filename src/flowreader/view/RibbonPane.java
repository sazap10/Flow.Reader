/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public abstract class RibbonPane extends StackPane {
    
    public abstract void setNewPosition(double posX, double posY);
    
    public abstract void move(double dX, double dY);
    
    
}
