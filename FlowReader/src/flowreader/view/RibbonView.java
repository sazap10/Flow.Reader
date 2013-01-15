/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import flowreader.core.WordCloud;
import flowreader.core.Page;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * 
 * @author D-Day
 */
public class RibbonView extends Group {

	private ArrayList<Page> pages;
        private ArrayList<WordCloud> wordClouds;// plan to have one of these for each zoom level
	int pageWidth = 500;
	int pageHeight = 700;
	int pageInterval = 5;
	int pagesNumber = 30;
	int maxScale = 35;
	int minScale = 0;
	int curScale = 15;
	int opaqueScale = 15;
	StackPane stackPane;
	EventHandler<MouseEvent> swipeHandler;

	public RibbonView(StackPane stackPane) {
		this.pages = new ArrayList<Page>();
                this.wordClouds = new ArrayList<WordCloud>();
		this.stackPane = stackPane;
	}
        
        public void setWordCloudsForPages(){
            WordCloud cloud;
            //create a page with the text of each page
            Group cloudObjects;
            int i = 0;
            for (Page page : pages){
                cloud = new WordCloud(page);
                cloud.setHeight(this.pageHeight / 3);
                cloud.setWidth(this.pageWidth);
                System.out.println("page width: " + this.pageWidth);
                System.out.println("page height: " + this.pageHeight);
                double pageX = ((this.pageWidth + pageInterval) * i);
                System.out.println("page startX:" + pageX);
                double pageY =  0; //arbitrary gap between cloud and page;
                 //System.out.println("cloud start X:" + pageX);
                 // System.out.println("cloud startY:" + pageY);
                System.out.println("calling rendercloud with input pagex: " + pageX + " pageY: " + pageY);
                cloudObjects = cloud.renderCloud(pageX, pageY);
                this.getChildren().add(cloudObjects);
                i++;
            }
            
            
        }
        
        public ArrayList<Page> getPages(){
           return this.pages;
           
        }

	public void buildRibbon(int pagesNumber) {
		this.pagesNumber = pagesNumber;

		int i = 0;
		int x = 0;
		int y = (this.pageHeight / 3) + 30;
		while (i < pagesNumber) {
			Page page = new Page(new Rectangle(x, y, pageWidth, pageHeight));
			this.pages.add(page);
                        //this.wordClouds.add(new WordCloud(page));
			this.getChildren().add(page.getPage());
			x += pageWidth + pageInterval;
			i++;
		}
		this.defineRibbonEvents();
		this.setRibbonEvents(true);
	}

	public void zoom(double deltaY, double x, double y) {
		if (deltaY <= 0) {
			if (curScale < minScale + 1) {
			} else {
				curScale--;
				setOpacity();
			}
		} else {
			if (curScale > maxScale - 1) {
			} else {
				curScale++;
				setOpacity();
			}
		}

		double[] array = new double[36];
		for (int i = 0; i < 36; i++) {
			array[i] = Math.pow(1.05, i - 15);
		}
                
                System.out.println("current scale:" + curScale);

		Scale scale = new Scale(array[curScale], array[curScale], x, y);
		if (stackPane.getTransforms().size() > 0) {
			for (int i = 0; i < stackPane.getTransforms().size(); i++) {
				stackPane.getTransforms().remove(i);
			}
		}
		stackPane.getTransforms().add(scale);

	}

	public void setOpacity() {
		double opacity;
		opacity = curScale / (double) opaqueScale;
		stackPane.setOpacity(opacity);

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

	private void defineRibbonEvents() {
		swipeHandler = new EventHandler<MouseEvent>() {
			MouseEvent previousEvent;

			@Override
			public void handle(MouseEvent event) {
				if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
					previousEvent = event;
					// System.out.println("PRESSED");
				} else if (event.getEventType()
						.equals(MouseEvent.MOUSE_DRAGGED)) {

					// System.out.println("DRAGGED");
					double dx = event.getSceneX() - previousEvent.getSceneX();
					double dy = event.getSceneY() - previousEvent.getSceneY();
					RibbonView.this.setLayoutX(RibbonView.this.getLayoutX()
							+ dx);
					RibbonView.this.setLayoutY(RibbonView.this.getLayoutY()
							+ dy);

					TranslateTransition tt = new TranslateTransition(
							Duration.millis(100), RibbonView.this);
					tt.setByX(dx);
					tt.setByY(dy);
					tt.setCycleCount(0);
					tt.setAutoReverse(true);
					tt.play();
				}
				previousEvent = event;
				event.consume();
			}
		};
	}

	public void setRibbonEvents(boolean setFlag) {
		if (setFlag) {
			this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
			this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
			this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
		} else {
			this.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
			this.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
			this.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
		}

	}

	public void setPageDragEvent(boolean setFlag) {
		EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard drag = ((Node) event.getSource())
						.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(((Page) event.getSource()).getText());
				drag.setContent(content);

				event.consume();
			}

		};
		int pageNum = 0;
		while(pageNum<pages.size()){
			if (setFlag) {
				pages.get(pageNum).setOnDragDetected(dragHandler);
				//System.out.println(page.getOnDragDetected().toString());
			} else {
				pages.get(pageNum).setOnDragDetected(null);
			}
			pageNum++;
		}

	}
}
