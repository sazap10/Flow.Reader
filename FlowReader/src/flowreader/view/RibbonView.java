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
    StackPane stackPane;
    Group pagesGroup;
    Group wordCloudGroup;
    double[] array = new double[maxScale+1];
    private EventHandler<MouseEvent> swipeHandler;
    private EventHandler<ScrollEvent> scrollHandler;
    private EventHandler<ZoomEvent> zoomHandler;
    private Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    public RibbonView(StackPane stackPane) {
        this.pages = new ArrayList<>();
        this.wordClouds = new ArrayList<>();
        this.stackPane = stackPane;
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
            this.getChildren().add(wordCloudGroup);

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
                setOpacity();
            }
        } else {
            if (curScale > maxScale - 1) {
            } else {
                curScale++;
                setOpacity();
            }
        }


        Scale scale = new Scale(array[curScale], array[curScale], pagesGroup.getBoundsInLocal().getWidth()/2,pagesGroup.getBoundsInLocal().getHeight()/2);
        Scale scale2 = new Scale(array[maxScale-10]-array[curScale], array[maxScale-10]-array[curScale], wordCloudGroup.getBoundsInLocal().getWidth()/2,wordCloudGroup.getBoundsInLocal().getHeight()/2);
        
        //remove previously applied transformation(s)
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
        
        //add the transformation to the groups
        pagesGroup.getTransforms().add(scale);
        wordCloudGroup.getTransforms().add(scale2);
        
        FlowReader.zoomLabel.setText("zoom: " + ((float) curScale / (float) maxScale) * 100
                + "%\ncurScale: " + curScale
                + "\nmin Scale: " + minScale
                + "\nmax Scale: " + maxScale);
    }

    public void setOpacity() {
        double opacity;
        opacity = curScale / (double) opaqueScale;
        //stackPane.setOpacity(opacity);
          
           
            pagesGroup.setOpacity(opacity);
            wordCloudGroup.setOpacity(1-opacity);
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
                    System.out.println("---------------"+
                            "\nScreen X: "+event.getScreenX()+"Screen Y: "+event.getScreenY()+
                            "\nget X: "+event.getX()+"get Y: "+event.getY());
                                    double height = flowreader.FlowReader.borderPane.getCenter().getLayoutBounds().getHeight();
                                     double width = flowreader.FlowReader.borderPane.getCenter().getLayoutBounds().getWidth();
                                     
					RibbonView.this.zoom(event.getDeltaY(),0,0);
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
            this.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
            this.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
        } else {
            stackPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            stackPane.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
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
                //System.out.println(page.getOnDragDetected().toString());
            } else {
                pages.get(pageNum).setOnDragDetected(null);
            }
            pageNum++;
        }

    }
}
