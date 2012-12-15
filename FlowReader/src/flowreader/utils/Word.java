/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

/**
 *
 * @author Pat
 */
public class Word {

    private String letters;
    private Integer count;
    private Integer fontSize;

    public Word(String letters, Integer count) {
        this.letters = letters;
        this.count = count;
    }

    public void setFontSize(Integer size) {
        this.fontSize = size;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public Integer getCount() {
        return this.count;
    }

    public String getText() {
        return this.letters;
    }
}