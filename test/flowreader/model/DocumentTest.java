/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author D-Day
 */
public class DocumentTest {

    private Document doc;

    public DocumentTest() {
    }

    @Before
    public void setUp() {
        Page p = new Page("word1 word2 word2");
        ArrayList<Page> pages = new ArrayList<Page>();
        pages.add(p);

        HashMap<String, Integer> wordsOccurrences = new HashMap<String, Integer>();
        wordsOccurrences.put("word1", 1);
        wordsOccurrences.put("word2", 2);
        WordCloud wc = new WordCloud(wordsOccurrences);
        ArrayList<WordCloud> lwc = new ArrayList<WordCloud>();
        lwc.add(wc);
        ArrayList<ArrayList<WordCloud>> llwc = new ArrayList<ArrayList<WordCloud>>();
        llwc.add(lwc);

        this.doc = new Document(pages, llwc);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPages method, of class Document.
     */
    @Test
    public void testGetPages() {
        System.out.println("getPages");
        Page p = new Page("word1 word2 word2");
        ArrayList<Page> expResult = new ArrayList<Page>();
        expResult.add(p);
        ArrayList<Page> result = this.doc.getPages();
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expResult.get(i).getText(), result.get(i).getText());
        }
    }

    /**
     * Test of getWordClouds method, of class Document.
     */
    @Test
    public void testGetWordClouds() {
        System.out.println("getWordClouds");
        HashMap<String, Integer> wordsOccurrences = new HashMap<String, Integer>();
        wordsOccurrences.put("word1", 1);
        wordsOccurrences.put("word2", 2);
        WordCloud wc = new WordCloud(wordsOccurrences);
        ArrayList<WordCloud> lwc = new ArrayList<WordCloud>();
        lwc.add(wc);
        ArrayList<ArrayList<WordCloud>> expResult = new ArrayList<ArrayList<WordCloud>>();
        expResult.add(lwc);

        ArrayList<ArrayList<WordCloud>> result = this.doc.getWordClouds();
        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result.get(i).size(); j++) {
                HashMap occresult = result.get(i).get(j).getWordOccurrences();
                HashMap occexpresult = expResult.get(i).get(j).getWordOccurrences();
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
}
