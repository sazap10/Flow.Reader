/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.TextDocument;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.view.TxtPageView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author D-Day
 */
public class TextFileReaderTest {
    private TextFileReader tfr;
    
    public TextFileReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        File f = new File("C:\\Users\\D-Day\\Workspace\\Flow.Reader\\books\\test.txt");
        tfr = new TextFileReader(f);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readFile method, of class TextFileReader.
     */
    @Test
    public void testReadFile() throws Exception {
        System.out.println("readFile");
        
        Page p = new Page("This is a test.");
        ArrayList<Page> pages = new ArrayList<Page>();
        pages.add(p);

        HashMap<String, Integer> wordsOccurrences = new HashMap<String, Integer>();
        wordsOccurrences.put("test", 1);
        WordCloud wc = new WordCloud(wordsOccurrences);
        ArrayList<WordCloud> lwc = new ArrayList<WordCloud>();
        lwc.add(wc);
        ArrayList<ArrayList<WordCloud>> llwc = new ArrayList<ArrayList<WordCloud>>();
        llwc.add(lwc);

        TextDocument expResult = new TextDocument(pages, llwc);

        TextDocument result = this.tfr.readFile(TxtPageView.textBoundWidth, TxtPageView.textBoundHeight);
        assertEquals(expResult.getPages().get(0).getText(), result.getPages().get(0).getText());
        
        ArrayList<ArrayList<WordCloud>> expclouds = expResult.getWordClouds();
        ArrayList<ArrayList<WordCloud>> resultclouds = result.getWordClouds();
        for (int i = 0; i < resultclouds.size(); i++) {
            for (int j = 0; j < resultclouds.get(i).size(); j++) {
                HashMap occresult = resultclouds.get(i).get(j).getWordOccurrences();
                HashMap occexpresult = expclouds.get(i).get(j).getWordOccurrences();
                Iterator iresult = occresult.entrySet().iterator();
                Iterator iexpresult = occexpresult.entrySet().iterator();
                while (iresult.hasNext()) {
                    Map.Entry r = (Map.Entry) iresult.next();
                    Map.Entry er = (Map.Entry) iexpresult.next();
                    assertEquals(r.getKey(), er.getKey());
                    assertEquals(r.getValue(), er.getValue());
                }
            }
        }
    }

    /**
     * Test of makeCloudLevels method, of class TextFileReader.
     */
    @Test
    public void testMakeCloudLevels() {
        System.out.println("makeCloudLevels");
        ArrayList<WordCloud> clouds = null;
        TextFileReader instance = new TextFileReader();
        ArrayList expResult = null;
        ArrayList result = instance.makeCloudLevels(clouds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCommonWords method, of class TextFileReader.
     */
    @Test
    public void testGetCommonWords() {
        System.out.println("getCommonWords");
        TextFileReader instance = new TextFileReader();
        HashMap expResult = null;
        HashMap result = instance.getCommonWords();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trimPunctuation method, of class TextFileReader.
     */
    @Test
    public void testTrimPunctuation() {
        System.out.println("trimPunctuation");
        String word = "";
        TextFileReader instance = new TextFileReader();
        String expResult = "";
        String result = instance.trimPunctuation(word);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of call method, of class TextFileReader.
     */
    @Test
    public void testCall() throws Exception {
        System.out.println("call");
        TextFileReader instance = new TextFileReader();
        TextDocument expResult = null;
        TextDocument result = instance.call();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
