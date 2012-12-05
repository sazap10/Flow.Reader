/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import flowreader.core.Page;

/**
 * 
 * @author D-Day
 */
public class RibbonView {

	private ArrayList<Page> pages;
	Group root;
	int pageWidth = 700;
	int pageHeight = 700;
	int pageInterval = 5;
	int pagesNumber = 30;
	int maxPageWidth = 700;
	int maxPageHeight = 700;
	int minPageWidth = 200;
	int minPageHeight = 200;
	int maxScale = 15;
	int minScale = -20;
	int curScale = 0;

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

	public void zoom(double deltaY) {
		double zoomFactor = 1.05;
		if (deltaY < 0) {
			if (curScale < minScale)
				zoomFactor = 1;
			else {
				zoomFactor = 2.0 - zoomFactor;
				curScale--;
			}
		} else{
			if(curScale>maxScale)
				zoomFactor=1;
			else
				curScale++;
		}
		//System.out.println(zoomFactor);
		double scaleX = pages.get(0).getPage().getScaleX() * zoomFactor;
		double scaleY = pages.get(0).getPage().getScaleY() * zoomFactor;
		//System.out.println("scaleX: " + scaleX + " scaleY: " + scaleY);
		for (int i = 0; i < pages.size(); i++) {
			Scale scale = new Scale(scaleX, scaleY, 0, 0);
			pages.get(i).getPage().getTransforms().add(scale);
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
