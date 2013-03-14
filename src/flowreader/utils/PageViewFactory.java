/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.PDFPage;
import flowreader.model.Page;
import flowreader.view.PDFPageView;
import flowreader.view.PageView;
import flowreader.view.TextPageView;

/**
 *
 * @author D-Day
 */
public  class PageViewFactory {
    
    /**
     * 
     * @param page
     * @return the corresponding pageView
     */
    public static PageView getView(Page page){
        if(page instanceof PDFPage){
            return new PDFPageView((PDFPage)page);
        }
        else{
            return new TextPageView(page);
        }
        
    }
    
}
