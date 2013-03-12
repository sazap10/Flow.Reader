/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Pat
 */
public interface Document {
    
    public ArrayList<ArrayList<WordCloud>> getWordClouds();
    public ArrayList<Group> getPageViews();
    public ArrayList<Page> getPages();
}
