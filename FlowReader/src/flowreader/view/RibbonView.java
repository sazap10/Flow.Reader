/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import flowreader.core.Page;

/**
 *
 * @author D-Day
 */
public class RibbonView {

    private ArrayList<Page> pages;
    Group root;
    int pageWidth = 200;
    int pageHeight = 200;
    int pageInterval = 5;
    int pagesNumber = 30;
    int maxPageWidth = 700;
    int maxPageHeight = 700;
    int minPageWidth = 200;
    int minPageHeight = 200;

    public RibbonView() {
        this.pages = new ArrayList<Page>();
        this.root = new Group();
    }

    public void buildRibbon(int pagesNumber) {
        this.pagesNumber = pagesNumber;

        int i = 0;
        int x = 0;
        int y = 0;
        while (i < pagesNumber) {
            x = x + pageWidth + pageInterval;
            Page page = new Page(new Rectangle(x, y, pageWidth, pageHeight));
            this.pages.add(page);
            root.getChildren().add(page.getPage());
            i++;
        }
    }

    public void zoomIn() {
        if (pageWidth < this.maxPageWidth && pageHeight < this.maxPageHeight) {
            pageWidth = pageWidth + 10;
            pageHeight = pageHeight + 10;
            pageInterval = pageWidth + 5;
            int x = 0;
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).setPageHeight(pageHeight);
                pages.get(i).setPageWidth(pageWidth);
                x = x + pageInterval;
                pages.get(i).setTextScale(true);
                pages.get(i).setX(x);
            }
        }
    }

    public void zoomOut() {
        if (pageWidth > this.minPageWidth && pageHeight > this.minPageHeight) {
            pageWidth = pageWidth - 10;
            pageHeight = pageHeight - 10;
            pageInterval = pageWidth + 5;
            int x = 0;
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).setPageHeight(pageHeight);
                pages.get(i).setPageWidth(pageWidth);
                x = x + pageInterval;
                pages.get(i).setTextScale(false);
                pages.get(i).setX(x);
            }
        }
    }

    public double getPageWidth() {
        return pageWidth;
    }

    public double getPageHeight() {
        return pageHeight;
    }

    public Group getRoot() {
        return this.root;
    }

    public void setTexttoPages(ArrayList<String> text) {
        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setText(text.get(i));
        }
    }
}
