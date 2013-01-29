/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Page;
import flowreader.model.Document;
import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.util.HashMap;

/**
 * 
 * @author D-Day
 */
public class RibbonView extends Group {

	private ArrayList<PageView> pages, culledPages;
	private ArrayList<Group> wordClouds;
	private HashMap<Integer, Integer> zoomTable;
	int pageWidth = 500;
	int pageHeight = 700;
	int pageInterval = 5;
	int pagesNumber = 30;
	int maxScale = 100;
	int minScale = 0;
	int curScale = 80;
	int opaqueScale = 15;
	int currentZoomLevel;
	int minZoomLevel = 1;
	int maxZoomLevel;

	StackPane stackPane;
	StackPane pagesPane;
	StackPane wordCloudPane;
	Group pagesGroup;
	Group wordCloudGroup;
	double[] array = new double[maxScale + 1];
	private EventHandler<MouseEvent> swipeHandler;
	private EventHandler<ScrollEvent> scrollHandler;
	private EventHandler<ZoomEvent> zoomHandler;
	private Rectangle2D screenBounds = Screen.getPrimary().getBounds();

	public RibbonView(StackPane stackPane) {
		this.pages = new ArrayList<>();
		this.wordClouds = new ArrayList<Group>();
		this.stackPane = stackPane;
		this.zoomTable = new HashMap<Integer, Integer>();
		this.currentZoomLevel = 1;
	}

	public ArrayList<PageView> getPages() {
		return this.pages;
	}

	// sets the scale needed for the correct level of precision and other stuff
	public void createZoomTable(int zoomLevels) {
		// first, find the final zoom level

		int scale;
		float percent;
		for (int i = 1; i <= zoomLevels; i++) {
			percent = curScale / (i * 100.0f);
			scale = (int) (percent * maxScale);
			zoomTable.put(i, scale);
		}
		maxZoomLevel = zoomLevels;

	}

	public void checkCloudLevel() {
		int nextDown, nextUp;
		System.out.println("this is being called with curscale " + curScale);
		int minScale = zoomTable.get(minZoomLevel);
		int maxScale = zoomTable.get(maxZoomLevel);
		if ((curScale < minScale + 1) && (curScale > maxScale - 1)) {
			System.out.println("curScale is in the confines it should be");
			if (currentZoomLevel != minZoomLevel) {
				nextDown = currentZoomLevel - 1;
			} else {
				nextDown = currentZoomLevel;
			}
			if (currentZoomLevel != maxZoomLevel) {
				nextUp = currentZoomLevel + 1;
			} else {
				nextUp = currentZoomLevel;
			}

			// now nextUp and nextDown have been set, check if we need to scale
			// up or down
			// based on the current scale
			if (curScale == zoomTable.get(nextDown)) {
				scaleCloudDown();
				currentZoomLevel--;

			} else if (curScale == zoomTable.get(nextUp)) {
				scaleCloudUp();
				currentZoomLevel++;
			}

		}

	}

	// function to replace all clouds currently displayed with half the amount
	// (larger ones from next level up)
	public void scaleCloudUp() {
		// index of list is one less than the level of the cloud, so no need to
		// increase zoom level:
		Group newLevel = wordClouds.get(currentZoomLevel);
		// need to switch out the current group from the stackpane
		System.out.println("clearing current clouds");
		this.wordCloudPane.getChildren().clear();
		this.wordCloudPane.getChildren().add(newLevel);
		this.getChildren().clear();
		this.getChildren().add(pagesGroup);
		this.getChildren().add(newLevel);
	}

	// function to replace all clouds currently displayed with double the amount
	// (smaller ones from lower level)
	public void scaleCloudDown() {
		// index of list is one less than the level of the cloud, so no need
		// decrease current level by 2:
		Group newLevel = wordClouds.get(currentZoomLevel - 2);
		// need to switch out the current group from the stackpane
		this.wordCloudPane.getChildren().clear();
		this.wordCloudPane.getChildren().add(newLevel);
		this.getChildren().clear();
		this.getChildren().add(pagesGroup);
		this.getChildren().add(newLevel);

	}

	public void buildRibbon(Document document) {
		this.setLayoutX(0);
		int i = 0;
		int x = 0;
		int y = 0;
		pagesPane = new StackPane();
		wordCloudPane = new StackPane();
		pagesGroup = new Group();
		wordCloudGroup = new Group();
		ArrayList<WordCloud> clouds = document.getCloudLevel(1);
		ArrayList<WordCloudView> cloudViews = new ArrayList<WordCloudView>();
		while (i < document.getNumOfPages()) {

			WordCloudView wordCloud = new WordCloudView(clouds.get(i),
					new Rectangle(x, y, pageWidth, pageHeight / 3));
			wordCloudGroup.setOpacity(1);
			this.wordCloudGroup.getChildren().add(wordCloud);

			PageView page = new PageView(new Rectangle(x, y + 50
					+ (pageHeight / 3), pageWidth, pageHeight));
			page.setText(document.getPage(i).getText());
			page.setVisible(false);
			this.pages.add(page);
			// this.pagesGroup.getChildren().add(page);

			x += pageWidth + pageInterval;
			i++;
		}
		culling();
		// add the first level of clouds
		this.wordClouds.add(wordCloudGroup);
		this.pagesGroup.getChildren().addAll(pages);
		//
		// create the rest of the clouds
		// createCloudLevelGroups(document);

		this.pagesPane.getChildren().add(pagesGroup);
		// this.wordCloudPane.getChildren().add(wordCloudGroup);
		this.getChildren().add(pagesGroup);
		// this.getChildren().add(wordCloudGroup);

		// set up zoom levels
		// createZoomTable(document.getNumOfCloudLevels());
		for (int j = 0; j <= maxScale; j++) {
			array[j] = Math.pow(1.05, j - 81);
			System.out.println("array[" + j + "]: " + array[j]);
		}
		System.out
				.println("screen properties:" + "\nmax X: "
						+ screenBounds.getMaxX() + "\nmax Y: "
						+ screenBounds.getMaxY());

		this.defineRibbonEvents();
		this.setRibbonEvents(true);
	}

	// creates all but the first level of wordCloud groups and adds them to the
	// list of groups
	public void createCloudLevelGroups(Document document) {
		ArrayList<WordCloud> currentLevelClouds;
		Group currentLevelViews;
		int cloudWidth = pageWidth;
		int cloudHeight = pageHeight / 3;
		int cloudInterval = pageInterval;
		int x, y;

		for (int i = 1; i < document.getNumOfCloudLevels(); i++) {
			currentLevelClouds = document.getCloudLevel(i);
			x = 0;
			y = 0;
			currentLevelViews = new Group();

			// render each cloud on this level and add it to the group

			for (WordCloud wordCloud : currentLevelClouds) {
				WordCloudView currentView = new WordCloudView(wordCloud,
						new Rectangle(x, y, cloudWidth, cloudHeight));
				currentLevelViews.getChildren().add(currentView);
				x += cloudWidth;
			}

			// add the group, and double dimensions
			wordClouds.add(currentLevelViews);
			cloudWidth *= 2;
			cloudHeight *= 2;
			cloudInterval *= 2;
		}

	}

	public void switchToWordCloud() {
		this.getChildren().clear();
		for (int i = 0; i < this.wordClouds.size(); i++) {
			this.getChildren().add(this.wordClouds.get(i));
		}
	}

	public void switchToPages() {
		this.getChildren().clear();
		culling();
		/*
		 * for (int i = 0; i < this.pages.size(); i++) {
		 * this.getChildren().add(this.pages.get(i)); }
		 */
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
		// before any actual scaling takes place, need to check if cloud level
		// needs to be changed
		// checkCloudLevel();
		Scale scale = new Scale(array[curScale], array[curScale], x, y);
		Scale scale2 = new Scale(array[maxScale - 10] - array[curScale],
				array[maxScale - 10] - array[curScale]);

		// remove previously applied transformation(s)
		if (pagesGroup.getTransforms().size() > 0) {
			for (int i = 0; i < pagesGroup.getTransforms().size(); i++) {
				pagesGroup.getTransforms().remove(i);
			}
		}
		if (wordCloudGroup.getTransforms().size() > 0) {
			for (int j = 0; j < wordCloudGroup.getTransforms().size(); j++) {
				wordCloudGroup.getTransforms().remove(j);
			}
		}
		
		if (stackPane.getTransforms().size() > 0) {
			for (int j = 0; j < stackPane.getTransforms().size(); j++) {
				stackPane.getTransforms().remove(j);
			}
		}
		// add the transformation to the groups
		// pagesGroup.getTransforms().add(scale);
		// wordCloudGroup.getTransforms().add(scale2);
		stackPane.getTransforms().add(scale);
		culling();
		System.out.println("x: " + x + "y: " + y
				+ "\nstackPane parent bound x: "
				+ stackPane.getBoundsInParent().getWidth()
				+ "stackPane parent bound y: "
				+ stackPane.getBoundsInParent().getHeight());
		FlowReader.zoomLabel.setText("zoom: "
				+ ((float) curScale / (float) maxScale) * 100 + "%\ncurScale: "
				+ curScale + "\nmin Scale: " + minScale + "\nmax Scale: "
				+ maxScale);
	}

	public void setOpacity() {
		double opacity;
		opacity = curScale / (double) opaqueScale;
		// stackPane.setOpacity(opacity);

		// pagesGroup.setOpacity(opacity);
		// wordCloudGroup.setOpacity(1-opacity);
	}

	public double getPageWidth() {
		return pageWidth;
	}

	public double getPageHeight() {
		return pageHeight;
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
					culling();
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

		scrollHandler = new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (!event.isDirect()) {
					// System.out.println("---------------"+
					// "\nScreen X: "+event.getScreenX()+"Screen Y: "+event.getScreenY()+
					// "\nget X: "+event.getX()+"get Y: "+event.getY());
					RibbonView.this.zoom(event.getDeltaY(), event.getScreenX(),
							event.getScreenY());

				}
				event.consume();

			}
		};

		zoomHandler = new EventHandler<ZoomEvent>() {
			@Override
			public void handle(ZoomEvent event) {
				double delta = event.getZoomFactor() - 1;
				RibbonView.this.zoom(delta, event.getX(), event.getY());
				event.consume();
			}
		};
	}

	public void setRibbonEvents(boolean setFlag) {
		if (setFlag) {
			this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
			this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
			this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
			this.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
			this.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
		} else {
			this.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
			this.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
			this.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
			this.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
			this.removeEventHandler(ZoomEvent.ZOOM, zoomHandler);
		}

	}

	public void setPageDragEvent(boolean setFlag) {
		EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
			@Override
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
		while (pageNum < pages.size()) {
			if (setFlag) {
				pages.get(pageNum).setOnDragDetected(dragHandler);
				// System.out.println(page.getOnDragDetected().toString());
			} else {
				pages.get(pageNum).setOnDragDetected(null);
			}
			pageNum++;
		}

	}

	// culling method sets the pages to be displayed as visible
	private void culling() {
		
		//set all the pages as invisible to clear previous culling call
		/*for(PageView page:pages)
			page.setVisible(false);
		*/
		/// get the width of the page
		double pageWidth = this.pages.get(0).getPageWidth();
		// calculate the number of pages to display
		int noOfPages = (int) Math.ceil(screenBounds.getWidth() / pageWidth);
		//System.out.println("page Width = "+ pageWidth);
		// if the number of pages calculated above is greater than that of the
		// amount of pages then use the amount of pages as noOfPages
		if (noOfPages > this.pages.size())
			noOfPages = this.pages.size();
		boolean found = false;
		System.out.println("left x: "+RibbonView.this.getLayoutX()+ " bounds: "+this.getBoundsInLocal().getMinX());
		for (int i = 0; i < this.pages.size(); i++) {
			//System.out.println("page no: "+i+" x: "+pages.get(i).getX());
			//no more pages to set as visible
			if (noOfPages == 0){
				found = false;
				if(i< pages.size()-1){
					pages.get(i+1).setVisible(true);
					continue;
				}
			}
				
			//check which pages is the most left on the screen.
			if ( (-this.getLayoutX()>this.pages.get(i).getX()-pageWidth)
					&& (-this.getLayoutX()<this.pages.get(i).getX()+pageWidth) && !found) {
				if(i != 0)
					pages.get(i-1).setVisible(true);
				found = true;
				 System.out.println("page no: "+i);
			}
			//set the pages as visible
			if (found && noOfPages > 0) {
				pages.get(i).setVisible(true);
				noOfPages--;
				continue;
			}
			//pages.get(i).setVisible(false);
		}
	}

}
