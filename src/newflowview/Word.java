/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newflowview;

/**
 *
 * @author jim
 */
public class Word {
    
    int fontSize;
    String text;
    
    public Word(int fontSize, String text){
        this.fontSize = fontSize;
        this.text = text;
    }
    
    public String getText(){
        return this.text;
    }
    
    public int getFontSize(){
        return this.fontSize;
    }
    
}
