/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author D-Day
 */
public class PageTest {
    
    public PageTest() {
    }

    /**
     * Test of getText method, of class Page.
     */
    @Test
    public void testGetText() {
        Page instance = new Page("test text");
        String expResult = "test text";
        String result = instance.getText();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getText method, of class Page.
     */
    @Test
    public void testToString() {
        Page instance = new Page("test text");
        String expResult = "PAGE \n test text\n\n";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}