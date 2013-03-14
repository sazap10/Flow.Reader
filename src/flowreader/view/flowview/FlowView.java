/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.flowview;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.utils.PageViewFactory;
import flowreader.view.PageView;
import flowreader.view.TxtPageView;
import flowreader.view.diveview.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class FlowView extends Group {

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
    int curScale = 0;
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
    private boolean otherTransitionsFinished = true;
    private boolean zoomLock = false;
    private boolean verticalLock = false;
    private boolean split_version = false;
    private boolean zoomAtMouse = false;
private boolean text_visible = true;
    public FlowView(StackPane stackPane, boolean split_version) {
        this.pages = new ArrayList<PageView>();
        this.wordClouds = new ArrayList<Group>();
        this.stackPane = stackPane;
        pagesPane = new StackPane();
        wordCloudPane = new StackPane();
        this.zoomTable = new HashMap<Integer, Integer>();
        this.currentZoomLevel = maxZoomLevel;
        this.VBox = new VBox();
        this.split_version = split_version;

    }
    public boolean toggleText() {
        if (text_visible) {
            text_visible = false;
            for (int i = 0; i < pagesGroup.getChildren().size(); i++) {

                TxtPageView p = (TxtPageView) pagesGroup.getChildren().get(i);
                p.toggleTextVisible(text_visible);
            }
        } else {
            text_visible = true;
            for (int i = 0; i < pagesGroup.getChildren().size(); i++) {

                TxtPageView p = (TxtPageView) pagesGroup.getChildren().get(i);
                p.toggleTextVisible(text_visible);
            }
        }
        return text_visible;
    }

    public boolean toggleZoomCenter() {
        if (zoomAtMouse) {
            zoomAtMouse = false;
        } else {
            zoomAtMouse = true;
        }
        return zoomAtMouse;
    }

    public ArrayList<PageView> getPages() {
        return this.pages;
    }

    public boolean getOtherTransitionsFinished() {
        return otherTransitionsFinished;
    }

    public int getMaxZoomLevel() {
        return maxZoomLevel;
    }

    public void setXCoord(int diff) {
        TranslateTransition tt = new TranslateTransition(
                Duration.millis(100), FlowView.this.VBox);
        tt.setByX(diff);
        tt.setCycleCount(0);
        tt.setAutoReverse(true);
        tt.play();
        x_coord.set(x_coord.doubleValue() + diff);
    }

    public void goToReadingMode() {
        curScale = 80;
        x_coord.set(0);
        y_coord.set(0);
        this.currentZoomLevel = 1;
        zoom(-1, screenBounds.getWidth() / 2, ((screenBounds.getHeight() / 2) - (screenBounds.getHeight() * 0.35)));
        wordCloudPane.getChildren().clear();
        wordCloudPane.getChildren().add(wordClouds.get(0));
    }

    public boolean getZoomLock() {
        return zoomLock;
    }

    public void setZoomLock(boolean lock) {
        zoomLock = lock;
    }

    public boolean getVerticalLock() {
        return verticalLock;
    }

    public void setVerticalLock(boolean lock) {
        verticalLock = lock;
    }
    // sets the scale needed for the correct level of precision and other stuff
  public void createZoomTable(int zoomLevels) {
        // first, find the final zoom level
/*
        int zoomTable_scale;
        float percent;
        for (int i = 1; i <= zoomLevels; i++) {
            percent = 80 / (i * 100.0f - 60);
            zoomTable_scale = (int) (percent * maxScale);
            //           zoomTable_scale = (int) (10*i);

            System.out.println("put " + i + " " + zoomTable_scale);
            zoomTable.put(i, zoomTable_scale);
        }

        
        */
         //set the percentage linear increment
         int inc = (int) (maxScale*0.85 / (zoomLevels-1));
         //walk the increments to 100 building the table
         int tmpScale = 1;
         int currLevel = zoomLevels;
         
                
         for (int i = zoomLevels; i >= 1; i--){
         zoomTable.put(currLevel, tmpScale);
         System.out.println("put "+currLevel+" "+tmpScale);
         tmpScale = tmpScale + inc;
         currLevel--;
            
         }
         

        maxZoomLevel = zoomLevels;
        currentZoomLevel = maxZoomLevel;
    }
    public void checkCloudLevel() {
        int nextDown, nextUp;
        int zoomTable_minScale = zoomTable.get(minZoomLevel);
        int zoomTable_maxScale = zoomTable.get(maxZoomLevel);

        if ((curScale < zoomTable_minScale + 1) && (curScale > zoomTable_maxScale - 1)) {
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

            //now nextUp and nextDown have been set, check if we need to scale up or down
            // based on the current scale
            if ((curScale > zoomTable.get(nextDown)) || (curScale > zoomTable.get(currentZoomLevel))) {
                scaleCloud(nextDown, -1);
                currentZoomLevel--;
            } else if (curScale < zoomTable.get(nextUp)) {
                scaleCloud(nextUp, 1);
                currentZoomLevel++;
            }

        }

    }

    //function to replace all clouds currently displayed with half the amount (larger ones from next level up)
    public void scaleCloud(int level, int upOrDown) {
        if (zoomLock) {
            otherTransitionsFinished = false;
        }

        //index of list is one less than the level of the cloud, so no need to increase zoom level:
        final Node newLevel = wordClouds.get(level);

        //need to switch out the current group from the stackpane
        /*previous_x = this.getLayoutX();
         previous_y = this.getLayoutY();
         */

        //final Node previous = wordCloudPane.getChildren().get(0);
        /*if (!wordCloudPane.getChildren().contains(newLevel)) {
         wordCloudPane.getChildren().add(newLevel);
         }*/

        if (upOrDown == -1) {
            // Run the transition effects
            wordCloudPane.getChildren().clear();
            ParallelTransition at = appearTransition(newLevel);
            wordCloudPane.getChildren().add(newLevel);
            //ParallelTransition dt = disappearTransition(previous);
            at.play();
            at.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    otherTransitionsFinished = true; // Transition is finished

                }
            });
            //dt.play();


            // When the transition is finished we remove the previous level
            /*dt.setOnFinished(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent event) {
             wordCloudPane.getChildren().clear();
             wordCloudPane.getChildren().add(newLevel);
             otherTransitionsFinished = true; // Transition is finished

             }
             });*/

        } else {
            // Run the transition effects
            wordCloudPane.getChildren().clear();
            ParallelTransition at = appearTransition(newLevel);
            wordCloudPane.getChildren().add(newLevel);
            //ParallelTransition dt = disappearTransition(previous);
            at.play();
            at.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    otherTransitionsFinished = true; // Transition is finished

                }
            });
            //dt.play();

            // When the transition is finished we remove the previous level
           /* dt.setOnFinished(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent event) {
             wordCloudPane.getChildren().clear();
             wordCloudPane.getChildren().add(newLevel);
             otherTransitionsFinished = true; // Transition is finished

             }
             });*/

        }

        //translatePages(level);
    }

    public ParallelTransition appearTransition(Node victim) {
        int duration = 550;

        FadeTransition ft = new FadeTransition(Duration.millis(duration), victim);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);


        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft);
        pt.setCycleCount(1);
        return pt;
    }

    public ParallelTransition disappearTransition(Node victim) {
        int duration = 550;

        FadeTransition ft = new FadeTransition(Duration.millis(duration), victim);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);



        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft);
        pt.setCycleCount(1);
        return pt;
    }

    //translates the pages appropriately to the wordcloud level they are on
    public void translatePages(int level) {
        //level is the level we are GOING TO
        //so if going to level 2, we need to translate by 1 cloudheight = 2^0 = 2^ level-2
        //if going to level 3, need to translate by 
        double numOfHeights;
        wordCloudPane.getTransforms().clear();

        double magnitude;
        if (level > 1) {
            numOfHeights = Math.pow(2, level - 1) - 1;
        } else {
            numOfHeights = 0;

        }
        magnitude = (pageHeight + 20);
        //System.out.println("translating down by " + (magnitude/(pageHeight/3)) +" cloud heights");

        Translate translate = new Translate(0, magnitude);
        System.out.println("magnitude! : " + magnitude);
        wordCloudPane.getTransforms().add(translate);
        System.out.println("transformation : " + wordCloudPane.getTransforms().toString());

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
        VBox.setSpacing(1);

        //                VBox.getChildren().get(0).setLayoutY(0);
        //VBox.getChildren().get(1).setLayoutY(VBox.getChildren().get(0).getBoundsInLocal().getHeight());
        //StackPane.setAlignment(wordCloudPane,Pos.TOP_CENTER);
        //StackPane.setAlignment(pagesPane, Pos.CENTER);


        // set up zoom levels
        createZoomTable(document.getWordClouds().size());
        for (int j = 0; j <= maxScale; j++) {
            array[j] = Math.pow(1.05, j - 81);
        }
        ArrayList<WordCloud> clouds = document.getWordClouds().get(0);
        //ArrayList<WordCloudView> cloudViews = new ArrayList<WordCloudView>();


        pagesGroup = new Group();
        wordCloudGroup = new Group();

        //wordCloudPane.setAlignment(Pos.CENTER_LEFT);
        wordCloudPane.setAlignment(Pos.CENTER);

        while (i < document.getPages().size()) {
            DiveWordCloud wordCloud = new DiveWordCloud(clouds.get(i), x, pageHeight,
                    pageWidth, pageHeight, 1, this);
            wordCloudGroup.setOpacity(1);
            this.wordCloudGroup.getChildren().add(wordCloud);

            Group page = PageViewFactory.getView(document.getPages().get(i));
            this.pagesGroup.getChildren().add(page);
            page.relocate(x,y + 50 + (pageHeight / 3));

            x += pageWidth + pageInterval;
            i++;
        }


        //add the first level of clouds
        this.wordClouds.add(wordCloudGroup);

        //create the rest of the clouds
        createCloudLevelGroups(document);


        this.pagesPane.getChildren().add(pagesGroup);
        pagesPane.setId("pagespane");
        this.wordCloudPane.getChildren().add(wordClouds.get(document.getWordClouds().size() - 1));
        stackPane.getChildren().add(VBox);

        VBox.getTransforms().add(t);
        t.xProperty().bind(x_coord);
        t.yProperty().bind(y_coord);
        this.defineRibbonEvents();
        this.setRibbonEvents(true);

     double zoom_x = screenBounds.getWidth() / 2;
                    double zoom_y = (screenBounds.getHeight() / 2) - (screenBounds.getHeight() * 0.35);

                    if (FlowReader.split_toggle) {
                            zoom(-1, zoom_x / 2, zoom_y);      
                    } else {
                            zoom(-1, zoom_x, zoom_y);
                    }
                
                    x_coord.set(-(VBox.getBoundsInLocal().getWidth() / 2));


    }

    //creates all but the first level of wordCloud groups and adds them to the list of groups
    public void createCloudLevelGroups(Document document) {
        ArrayList<WordCloud> currentLevelClouds;
        Group currentLevelViews;
        double cloudWidth = pageWidth;
        double cloudHeight = pageHeight;
        double cloudInterval = pageInterval;
        int x, y;

        for (int i = 1; i < document.getWordClouds().size(); i++) {
            currentLevelClouds = document.getWordClouds().get(i);
            x = 0;
            y = 0;
            currentLevelViews = new Group();

            //render each cloud on this level and add it to the group
            DiveWordCloud currentView = null;
            for (WordCloud wordCloud : currentLevelClouds) {
                currentView = new DiveWordCloud(wordCloud, x,cloudHeight, cloudWidth, cloudHeight, i, this);
                currentLevelViews.getChildren().add(currentView);
                x += cloudWidth + cloudInterval;
            }

            //add the group, and double dimensions
            wordClouds.add(currentLevelViews);
            double ratio = 1;

            //ratio = (Math.pow((double)2, currentView.calculateFontSizeFromLevel(i)))/(Math.pow((double)2, currentView.calculateFontSizeFromLevel(i-1)));
            ratio = 2;
            cloudWidth *= ratio;
            cloudHeight *= ratio;
            cloudInterval *= ratio;
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
                curScale = curScale - 1;
                //setEffect();
            }
        } else {
            if (curScale > maxScale - 1) {
            } else {
                curScale = curScale + 1;
                //setEffect();
            }
        }


        previousScale = scale;
        scale = new Scale(array[curScale], array[curScale], x, y);
        //stackPane.getTransforms().remove(previousScale);

        double ori_pos_x = stackPane.getLayoutX();
        double ori_pos_y = stackPane.getLayoutY();
        //Translate t1 = new Translate(-x,-y);
//stackPane.getTransforms().add(t1);
        checkCloudLevel();
        //stackPane.getTransforms().add(scale);
        //                        Translate t2 = new Translate(x,y);

        // stackPane.getTransforms().add(t2);

if(stackPane.getTransforms().size()>0){
        stackPane.getTransforms().set(0, scale);}
        else{
            stackPane.getTransforms().add(scale);
        }
    }

    public void setEffect() {
        double opacity;
        opacity = curScale / (double) opaqueScale;
        //pagesPane.setOpacity(opacity);
        //wordCloudPane.setOpacity(1-opacity);
        double x = (maxScale - curScale) / 100f * 200f;
        System.out.println(Math.round(x));
        System.out.println("- " + x);

        VBox.setEffect(null);
        VBox.setEffect(new BoxBlur(x, x, 1));

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
                if (otherTransitionsFinished) {
                    if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                        previousEvent = event;
                        // System.out.println("PRESSED");
                    } else if (event.getEventType()
                            .equals(MouseEvent.MOUSE_DRAGGED)) {

                        // System.out.println("DRAGGED");
                        double dx = event.getX() - previousEvent.getX();
                        double dy = event.getY() - previousEvent.getY();
                        if (verticalLock) {
                            dy = 0;
                        }/*
                        TranslateTransition tt = new TranslateTransition(
                                Duration.millis(100), FlowView.this.VBox);
                        tt.setByX(dx);
                        tt.setByY(dy);
                        tt.setCycleCount(0);
                        tt.setAutoReverse(true);
                        tt.play();*/
                        x_coord.set(x_coord.doubleValue() + dx);
                        y_coord.set(y_coord.doubleValue() + dy);

                    }
                    previousEvent = event;
                    event.consume();
                }
            }
        };

        scrollHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (!event.isDirect() && otherTransitionsFinished) {
                    double x = screenBounds.getWidth() / 2;
                    double y = (screenBounds.getHeight() / 2) - (screenBounds.getHeight() * 0.35);



                    if (FlowReader.split_toggle) {
                        if (zoomAtMouse) {

                            zoom(event.getDeltaY(), event.getX(), event.getY());
                        } else {
                            zoom(event.getDeltaY(), x / 2, y);

                        }
                    } else {

                        if (zoomAtMouse) {
                            zoom(event.getDeltaY(), event.getX(), event.getY());
                        } else {
                            zoom(event.getDeltaY(), x, y);

                        }
                    }
                }
                event.consume();

            }
        };

        zoomHandler = new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                if (otherTransitionsFinished) {
                    double delta = event.getZoomFactor() - 1;
                    FlowView.this.zoom(delta, event.getX(), event.getY());
                    event.consume();
                }
            }
        };
    }

    public void setRibbonEvents(boolean setFlag) {
        if (setFlag) {
            stackPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            stackPane.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            stackPane.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);

            if (!split_version) {
                FlowReader.rootPane.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
                FlowReader.rootPane.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
            } else {
                FlowReader.rootPane2.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
                FlowReader.rootPane2.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
            }
        } else {
            stackPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);

            if (!split_version) {
                FlowReader.rootPane.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
                FlowReader.rootPane.removeEventHandler(ZoomEvent.ZOOM, zoomHandler);
            } else {
                FlowReader.rootPane2.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
                FlowReader.rootPane2.removeEventHandler(ZoomEvent.ZOOM, zoomHandler);
            }
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
        ArrayList<PageView> clippedPages = new ArrayList<PageView>();
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
