/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Page;
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

/**
 *
 * @author D-Day
 */
public class RibbonView extends Group {

    private ArrayList<PageView> pages;
    private ArrayList<WordCloudView> wordClouds;
    int pageWidth = 500;
    int pageHeight = 700;
    int pageInterval = 5;
    int pagesNumber = 30;
    int maxScale = 100;
    int minScale = 0;
    int curScale = 81;
    int opaqueScale = 81;
    double wordCloudScale =1;
    StackPane stackPane;
    StackPane ribbonPane;
    Group pagesGroup;
    Group wordCloudGroup;
    double[] array = new double[maxScale+1];
    private EventHandler<MouseEvent> swipeHandler;
    private EventHandler<ScrollEvent> scrollHandler;
    private EventHandler<ZoomEvent> zoomHandler;
    private Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    public RibbonView(StackPane stackPane,StackPane ribbonPane) {
        this.pages = new ArrayList<>();
        this.wordClouds = new ArrayList<>();
        this.stackPane = stackPane;
        this.ribbonPane=ribbonPane;
    }

    public ArrayList<PageView> getPages() {
        return this.pages;
    }

    public void buildRibbon(ArrayList<Page> pagesContent) {
        int i = 0;
        int x = 0;
        int y = 0;
        pagesGroup = new Group();
        wordCloudGroup = new Group();
        while (i < pagesContent.size()) {
            PageView page = new PageView(new Rectangle(x, y, pageWidth, pageHeight));
            page.setText(pagesContent.get(i).getText());
            this.pages.add(page);
            this.pagesGroup.getChildren().add(page);

            WordCloudView wordCloud = new WordCloudView(new Rectangle(x, y, pageWidth, pageHeight/2));
            wordCloud.setWordOccurrences(pagesContent.get(i).getWordsOccurrences());
            this.wordClouds.add(wordCloud);
            //wordCloudGroup.setOpacity(0);
            this.wordCloudGroup.getChildren().add(wordCloud);
            x += pageWidth + pageInterval;
            i++;
        }
            this.getChildren().add(pagesGroup);
            ribbonPane.getChildren().add(wordCloudGroup);
//stackPane.autosize();
        //set up zoom levels
        for (int j = 0; j < maxScale+1; j++) {
            array[j] = Math.pow(1.05, j - 81);
            System.out.println("array["+j+"]: "+array[j]);
        }
        System.out.println("screen properties:"+
                "\nmax X: "+screenBounds.getMaxX()+
                "\nmax Y: "+screenBounds.getMaxY());
        
        this.defineRibbonEvents();
        this.setRibbonEvents(true);
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
                setTheOpacity(deltaY);
            }
        } else {
            if (curScale > maxScale - 1) {
            } else {
                curScale++;
                setTheOpacity(deltaY);
            }
        }


        Scale scale = new Scale(array[curScale], array[curScale], 800,400);
        
        //remove previously applied transformation(s)
        stackPane.getTransforms().clear();
        //add the transformation to the groups
        stackPane.getTransforms().add(scale);
       
        FlowReader.zoomLabel.setText("zoom: " + ((float) curScale / (float) maxScale) * 100
                + "%\ncurScale: " + curScale
                + "\nmin Scale: " + minScale
                + "\nmax Scale: " + maxScale);
    }

    public void setTheOpacity(double deltaY) {
        double opacity;
        opacity = curScale / (double) opaqueScale;

        //stackPane.setOpacity(opacity);
          
           System.out.println("opacity: "+opacity);
            pagesGroup.setOpacity(opacity);
            wordCloudGroup.setOpacity(1-opacity);
            
            //make word cloud bigger at a certain opacity
            if(opacity<1 && deltaY<0){
                wordCloudScale = wordCloudScale*1.05;
                                                         //   Scale scale = new Scale(wordCloudScale, wordCloudScale, 800,405.5);

                for(int i=0;i<wordClouds.size();i++){
                //    wordClouds.get(i).incrementFont();
                                    //remove previously applied transformation(s)
                   //  wordClouds.get(i).getTransforms().clear();
                     //add the transformation to the groups
                     //wordClouds.get(i).getTransforms().add(scale);
                             wordClouds.get(i).setScaleX(wordCloudScale);
                              wordClouds.get(i).setScaleY(wordCloudScale);
                }
//stackPane.autosize();

                }else{
                if(wordCloudScale>1){
                                    wordCloudScale = wordCloudScale/1.05;
                                           // Scale scale = new Scale(wordCloudScale, wordCloudScale, 800,405.5);

                                     for(int i=0;i<wordClouds.size();i++){
                                                             //wordClouds.get(i).decrementFont();

        
                     //remove previously applied transformation(s)
                     //wordClouds.get(i).getTransforms().clear();
                     //add the transformation to the groups
                    // wordClouds.get(i).getTransforms().add(scale);
                     
                              wordClouds.get(i).setScaleX(wordCloudScale);
                              wordClouds.get(i).setScaleY(wordCloudScale);
                                     }
                                   //  stackPane.autosize();

                }else{
                    wordCloudScale=1;
                       //     Scale scale = new Scale(wordCloudScale, wordCloudScale, 800,405.5);
                     for(int i=0;i<wordClouds.size();i++){
                                         //remove previously applied transformation(s)
                     //wordClouds.get(i).getTransforms().clear();
                     //add the transformation to the groups
                    // wordClouds.get(i).getTransforms().add(scale);
                    wordClouds.get(i).setScaleX(wordCloudScale);
                      wordClouds.get(i).setScaleY(wordCloudScale);
                                     }
                     //stackPane.autosize();

                }
            }
            
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
                    RibbonView.this.setLayoutX(RibbonView.this.getLayoutX()
                            + dx);
                    RibbonView.this.setLayoutY(RibbonView.this.getLayoutY()
                            + dy);

                    TranslateTransition tt = new TranslateTransition(
                            Duration.millis(100), stackPane);
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
                    System.out.println("---------------"+
                            "\nScreen X: "+event.getScreenX()+"Screen Y: "+event.getScreenY()+
                            "\nget X: "+event.getX()+"get Y: "+event.getY());
                                    double height = flowreader.FlowReader.borderPane.getCenter().getLayoutBounds().getHeight();
                                     double width = flowreader.FlowReader.borderPane.getCenter().getLayoutBounds().getWidth();
                       
                                        RibbonView.this.zoom(event.getDeltaY(), event.getScreenX(), event.getScreenY());

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
           stackPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
           stackPane.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            stackPane.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
           stackPane.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
            stackPane.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
             
        } else {
            flowreader.FlowReader.the_Group.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            flowreader.FlowReader.the_Group.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
           flowreader.FlowReader.the_Group.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
            flowreader.FlowReader.the_Group.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
            flowreader.FlowReader.the_Group.removeEventHandler(ZoomEvent.ZOOM, zoomHandler);
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
                //System.out.println(page.getOnDragDetected().toString());
            } else {
                pages.get(pageNum).setOnDragDetected(null);
            }
            pageNum++;
        }

    }
}
