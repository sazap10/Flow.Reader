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
}