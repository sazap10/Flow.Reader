/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.newflowview;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.util.Duration;
import flowreader.view.diveview.*;

/**
 *
 * @author D-Day
 */
public class NewFlowView extends Group {

    private ArrayList<PageView> pages;
    private ArrayList<PageView> culledPages;
    private ArrayList<Group> wordClouds;
    private HashMap<Integer, Integer> zoomTable;
    int pageWidth = 500;
    int pageHeight = 700;
    int pageInterval = 5;
    int pagesNumber = 30;
    int maxScale = 100;
    int minScale = 0;
    int curScale = 80;
    int opaqueScale = 100;
    int currentZoomLevel;
    int minZoomLevel = 1;
    int maxZoomLevel;
    DoubleProperty x_coord = new SimpleDoubleProperty(0.0);
    DoubleProperty y_coord = new SimpleDoubleProperty(0.0);
    Translate t = new Translate(0, 0);
    VBox VBox;
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
    double previous_x = 0;
    double previous_y = 0;
    Scale previousScale = new Scale(1, 1);
    Scale scale = new Scale(1, 1);
    Point2D previous_p = new Point2D(0, 0);
    double leftx = 0;

    public NewFlowView(StackPane stackPane) {
        this.pages = new ArrayList<>();
        this.wordClouds = new ArrayList<>();
        this.stackPane = stackPane;
        pagesPane = new StackPane();
        wordCloudPane = new StackPane();
        this.zoomTable = new HashMap<>();
        this.currentZoomLevel = 1;
        this.VBox = new VBox();
    }

    public ArrayList<PageView> getPages() {
        return this.pages;
    }

    // sets the scale needed for the correct level of precision and other stuff
    public void createZoomTable(int zoomLevels) {
        // first, find the final zoom level

        int zoomTable_scale;
        float percent;
        for (int i = 1; i <= zoomLevels; i++) {
            percent = curScale / (i * 100.0f - 60);
            zoomTable_scale = (int) (percent * maxScale);
            zoomTable.put(i, zoomTable_scale);
            //System.out.println("put " + zoomTable_scale + "at level " + i);
        }
        maxZoomLevel = zoomLevels;

    }

    public void checkCloudLevel() {
        int nextDown, nextUp;
        //System.out.println("this is being called with curscale " + curScale);
        //System.out.println("current zoom level: " + currentZoomLevel);
        int zoomTable_minScale = zoomTable.get(minZoomLevel);
        int zoomTable_maxScale = zoomTable.get(maxZoomLevel);

        if ((curScale < zoomTable_minScale + 1) && (curScale > zoomTable_maxScale - 1)) {
            //System.out.println("curScale is in the confines it should be");             
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


            //System.out.println("nextdown = " + zoomTable.get(nextDown));
            //System.out.println("nextup = " + zoomTable.get(nextUp));
            //now nextUp and nextDown have been set, check if we need to scale up or down
            // based on the current scale
            if ((curScale > zoomTable.get(nextDown)) || (curScale > zoomTable.get(currentZoomLevel))) {
                scaleCloud(nextDown, -1);
                currentZoomLevel--;

                //System.out.println("scaling cloud down");
            } else if (curScale < zoomTable.get(nextUp)) {
                scaleCloud(nextUp, 1);
                currentZoomLevel++;
                //System.out.println("scaling cloud up");
            }

        }

    }

    //function to replace all clouds currently displayed with half the amount (larger ones from next level up)
    public void scaleCloud(int level, int upOrDown) {
        //index of list is one less than the level of the cloud, so no need to increase zoom level:
        Group newLevel = wordClouds.get(level);
        //need to switch out the current group from the stackpane
        previous_x = this.getLayoutX();
        previous_y = this.getLayoutY();

        wordCloudPane.getChildren().clear();
        wordCloudPane.getChildren().add(newLevel);

        // translatePages(level);
    }

    //translates the pages appropriately to the wordcloud level they are on
    public void translatePages(int level) {
        //level is the level we are GOING TO
        //so if going to level 2, we need to translate by 1 cloudheight = 2^0 = 2^ level-2
        //if going to level 3, need to translate by 
        double numOfHeights;
        pagesGroup.getTransforms().clear();

        double magnitude;
        if (level > 1) {
            numOfHeights = Math.pow(2, level - 1) - 1;
        } else {
            numOfHeights = 0;

        }
        magnitude = numOfHeights * (pageHeight / 3);
        //System.out.println("translating down by " + (magnitude/(pageHeight/3)) +" cloud heights");

        Translate translate = new Translate(0, magnitude);
        pagesGroup.getTransforms().add(translate);
    }

    //function to replace all clouds currently displayed with double the amount (smaller ones from lower level)
    public void scaleCloudDown() {
        //index of list is one less than the level of the cloud, so no need decrease current level by 2:
        Group newLevel = wordClouds.get(currentZoomLevel - 1);
        //need to switch out the current group from the stackpane
        this.wordCloudPane.getChildren().clear();
        this.wordCloudPane.getChildren().add(newLevel);
        this.getChildren().clear();
        this.getChildren().add(pagesGroup);
        this.getChildren().add(newLevel);

    }

    public void buildRibbon(Document document) {


        int i = 0;
        int x = 0;
        int y = 0;
        VBox.setMinHeight(1);
        VBox.getChildren().add(pagesPane);
        VBox.getChildren().add(wordCloudPane);
        VBox.setSpacing(20.0);

        //                VBox.getChildren().get(0).setLayoutY(0);
        //VBox.getChildren().get(1).setLayoutY(VBox.getChildren().get(0).getBoundsInLocal().getHeight());
        //StackPane.setAlignment(wordCloudPane,Pos.TOP_CENTER);
        //StackPane.setAlignment(pagesPane, Pos.CENTER);

        pagesGroup = new Group();
        wordCloudGroup = new Group();
        ArrayList<WordCloud> clouds = document.getCloudLevel(0);
        ArrayList<WordCloudView> cloudViews = new ArrayList<>();
        while (i < document.getNumOfPages()) {

            DiveWordCloud wordCloud = new DiveWordCloud(clouds.get(i), x, y + 50 + pageHeight,
                    pageWidth, pageHeight / 3);
            wordCloudGroup.setOpacity(1);
            this.wordCloudGroup.getChildren().add(wordCloud);

            PageView page = new PageView(new Rectangle(x, y, pageWidth, pageHeight));
            page.setText(document.getPage(i).getText());
            this.pages.add(page);
            this.pagesGroup.getChildren().add(page);

            x += pageWidth + pageInterval;
            i++;
        }
        //add the first level of clouds
        this.wordClouds.add(wordCloudGroup);

        //
        //create the rest of the clouds
        createCloudLevelGroups(document);

        this.pagesPane.getChildren().add(pagesGroup);
        this.wordCloudPane.getChildren().add(wordCloudGroup);
        stackPane.getChildren().add(VBox);
        // set up zoom levels
        createZoomTable(document.getNumOfCloudLevels());
        for (int j = 0; j <= maxScale; j++) {
            array[j] = Math.pow(1.05, j - 81);
            //System.out.println("array[" + j + "]: " + array[j]);
        }
        /*System.out
         .println("screen properties:" + "\nmax X: "
         + screenBounds.getMaxX() + "\nmax Y: "
         + screenBounds.getMaxY());*/
        VBox.getTransforms().add(t);
        t.xProperty().bind(x_coord);
        t.yProperty().bind(y_coord);
        this.defineRibbonEvents();
        this.setRibbonEvents(true);
    }

    //creates all but the first level of wordCloud groups and adds them to the list of groups
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

            //render each cloud on this level and add it to the group

            for (WordCloud wordCloud : currentLevelClouds) {
                DiveWordCloud currentView = new DiveWordCloud(wordCloud, x, y, cloudWidth, cloudHeight, i);
                currentLevelViews.getChildren().add(currentView);
                x += cloudWidth;
            }

            //add the group, and double dimensions
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
        for (int i = 0; i < this.pages.size(); i++) {
            this.getChildren().add(this.pages.get(i));
        }
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

        previousScale = scale;
        scale = new Scale(array[curScale], array[curScale], x, y);

//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!"+stackPane.getTransforms().toString());

        stackPane.getTransforms().remove(previousScale);
        checkCloudLevel();
        stackPane.getTransforms().add(scale);


        /*FlowReader.zoomLabel.setText("zoom: "
         + ((float) curScale / (float) maxScale) * 100 + "%\ncurScale: "
         + curScale + "\nmin Scale: " + minScale + "\nmax Scale: "
         + maxScale);*/
    }

    public void setOpacity() {
        double opacity;
        opacity = curScale / (double) opaqueScale;
        //pagesPane.setOpacity(opacity);
        //wordCloudPane.setOpacity(1-opacity);
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
                    double dx = event.getX() - previousEvent.getX();
                    double dy = event.getY() - previousEvent.getY();
                    TranslateTransition tt = new TranslateTransition(
                            Duration.millis(100), NewFlowView.this.VBox);
                    tt.setByX(dx);
                    tt.setByY(dy);
                    tt.setCycleCount(0);
                    tt.setAutoReverse(true);
                    tt.play();
                    x_coord.set(x_coord.doubleValue() + dx);
                    y_coord.set(y_coord.doubleValue() + dy);
                    leftx+=dx;
                    System.out.println("left x: "+ x_coord);
                }
                previousEvent = event;
                event.consume();
            }
        };

        scrollHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (!event.isDirect()) {
                    double x = screenBounds.getWidth() / 2;
                    double y = screenBounds.getHeight() / 2;
                    zoom(event.getDeltaY(), x, y);

                }
                event.consume();

            }
        };

        zoomHandler = new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                double delta = event.getZoomFactor() - 1;
                NewFlowView.this.zoom(delta, event.getX(), event.getY());
                event.consume();
            }
        };
    }

    public void setRibbonEvents(boolean setFlag) {
        if (setFlag) {
            stackPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            stackPane.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            stackPane.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
            stackPane.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
            stackPane.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
        } else {
            stackPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
            stackPane.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
            stackPane.removeEventHandler(ZoomEvent.ZOOM, zoomHandler);
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

    public ArrayList<PageView> culling(double sceneWidth) {
        ArrayList<PageView> clippedPages = new ArrayList<>();
        double culling_pageWidth = clippedPages.get(0).getPageWidth();
        int noOfPages = (int) Math.ceil(sceneWidth / culling_pageWidth);
        if (noOfPages < pages.size()) {
            noOfPages = pages.size();
        }
        boolean found = false;
        for (int i = 0; i < this.pages.size(); i++) {
            if (noOfPages == 0) {
                break;
            }
            if (this.pages.get(i).getX() > -(culling_pageWidth)
                    && this.pages.get(i).getX() < (culling_pageWidth)) {
                found = true;
            }
            if (found && noOfPages > 0) {
                clippedPages.add(this.pages.get(i));
                noOfPages--;
                continue;
            }
        }
        return clippedPages;
    }
}
