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
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * 
 * @author D-Day
 */
public class RibbonView extends Group {

	private ArrayList<Page> pages;
	WordCloudView wordCloud;
	int pageWidth = 500;
	int pageHeight = 700;
	int pageInterval = 5;
	int pagesNumber = 30;
	int maxScale = 15;
	int minScale = -20;
	int curScale = 0;

	public RibbonView(WordCloudView wordCloud) {
		this.pages = new ArrayList<Page>();
		this.wordCloud = wordCloud;
	}

	public void buildRibbon(int pagesNumber) {
		this.pagesNumber = pagesNumber;

		int i = 0;
		int x = 0;
		int y = 0;
		while (i < pagesNumber) {
			Page page = new Page(new Rectangle(x, y, pageWidth, pageHeight));
			this.pages.add(page);
			this.getChildren().add(page.getPage());
			x += pageWidth + pageInterval;
			i++;
		}
		this.setRibbonEvents();
	}

	public void zoom(double deltaY, double x,double y,StackPane stackPane) {
		double zoomFactor = 1.05;
		if (deltaY <= 0) {
			if (curScale < minScale)
				zoomFactor = 1;
			else {
				zoomFactor = 2.0 - zoomFactor;
				curScale--;
				setOpacity();
			}
		} else {
			if (curScale > maxScale)
				zoomFactor = 1;
			else {
				curScale++;
				setOpacity();
			}
		}
		// System.out.println(zoomFactor);
		double scaleX = pages.get(0).getPage().getScaleX() * zoomFactor;
		double scaleY = pages.get(0).getPage().getScaleY() * zoomFactor;
		// System.out.println("scaleX: " + scaleX + " scaleY: " + scaleY);
			Scale scale = new Scale(scaleX, scaleY,stackPane.getLayoutBounds().getWidth()/2,stackPane.getLayoutBounds().getHeight()/2);
			stackPane.getTransforms().add(scale);
		

	}

	public void setOpacity() {
		double opacity;
		double range = maxScale - minScale;
		if (minScale < 0) {
			opacity = (curScale + (Math.abs(minScale))) / range;
		} else {
			opacity = (curScale - (minScale)) / range;
		}
		for (int i = 0; i < pages.size(); i++) {
			pages.get(i).getPage().setOpacity(opacity);
		}
	}

	public double getPageWidth() {
		return pageWidth;
	}

	public double getPageHeight() {
		return pageHeight;
	}

	public void setTexttoPages(ArrayList<String> text) {
		for (int i = 0; i < pages.size(); i++) {
			pages.get(i).setText(text.get(i));
		}
	}

	private void setRibbonEvents() {
		EventHandler<MouseEvent> swipeHandler = new EventHandler<MouseEvent>() {
			MouseEvent previousEvent, firstEvent;

			@Override
			public void handle(MouseEvent event) {
				if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
					previousEvent = event;
					firstEvent = event;
					System.out.println("PRESSED");
				} else if (event.getEventType()
						.equals(MouseEvent.MOUSE_DRAGGED)) {

					System.out.println("DRAGGED");
					double d = event.getSceneX() - previousEvent.getSceneX();
					RibbonView.this
							.setLayoutX(RibbonView.this.getLayoutX() + d);

					TranslateTransition tt = new TranslateTransition(
							Duration.millis(400), RibbonView.this);

					tt.setByX(d);

					tt.setCycleCount(0);
					tt.setAutoReverse(true);
					tt.play();
				}
				previousEvent = event;
				event.consume();
			}
		};

		RibbonView.this.setOnMouseDragged(swipeHandler);
		RibbonView.this.setOnMousePressed(swipeHandler);
		RibbonView.this.setOnMouseReleased(swipeHandler);
	}
}
