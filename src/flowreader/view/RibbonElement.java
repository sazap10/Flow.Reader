/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import javafx.scene.Group;

/**
 * Represent any element (i.e page or word cloud) that could be put in the ribbon view
 * @author D-Day
 */
public abstract class RibbonElement extends Group{

    /**
     * if on then highlight the element, else not highlight it
     * @param on 
     */
    public abstract void setHighlight(boolean on);
    
    
}
