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
import java.util.HashMap;

/**
 *
 * @author D-Day
 */
public class RibbonView extends Group {

    private ArrayList<PageView> pages;
    private ArrayList<WordCloudView> wordClouds;
    private HashMap<Integer,ArrayList<WordCloudView>> cloudLevels;
    int pageWidth = 500;
    int pageHeight = 700;
    int pageInterval = 5;
    int pagesNumber = 30;
    int maxScale = 100;
    int minScale = 0;
    int curScale = 81;
    int opaqueScale = 15;
    StackPane stackPane;
    StackPane pagesPane;
    StackPane wordCloudPane;
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
    
    //creates the wordClouds needed for each zoom level
    public void setCloudZoomLevels(){
        int pageCount = pages.size();
        int levelCount = 0;
        cloudLevels.put(0, wordClouds);
        while (pageCount >= 1){
            pageCount /= 2;  //half pages
            ArrayList<WordCloudView> currClouds = mergeClouds(cloudLevels.get(levelCount));
            levelCount++;
            cloudLevels.put(levelCount, currClouds);
           
        }
    }
    
    
    //merges all clouds from one zoom level and outputs half the amount of clouds
    //IMPORTANT: only works with an even number of clouds, final page is missed out at this point,
    //need to improve later
    public ArrayList<WordCloudView> mergeClouds(ArrayList<WordCloudView> input){
        WordCloudView b = null;
        Boolean haveb = false;
        ArrayList<WordCloudView> clouds = new ArrayList<WordCloudView>();
        for (WordCloudView i: input){
            if (haveb && (b!= null)){
                WordCloudView newCloud = new WordCloudView(i,b);
                clouds.add(newCloud);
                haveb = false;
            }
            else{
                b = i;
                haveb = true;
            }
            
        }
        return clouds;
        
    }
    

    public void buildRibbon(ArrayList<Page> pagesContent) {
        int i = 0;
        int x = 0;
        int y = 0;
        pagesPane = new StackPane();
        wordCloudPane = new StackPane();
        pagesGroup = new Group();
        wordCloudGroup = new Group();
        while (i < pagesContent.size()) {

            WordCloudView wordCloud = new WordCloudView(new Rectangle(x, y, pageWidth, pageHeight/3));
            wordCloud.setWordOccurrences(pagesContent.get(i).getWordsOccurrences());
            this.wordClouds.add(wordCloud);
            wordCloudGroup.setOpacity(1);
            this.wordCloudGroup.getChildren().add(wordCloud);
            
                        PageView page = new PageView(new Rectangle(x, y+50+(pageHeight/3), pageWidth, pageHeight));
            page.setText(pagesContent.get(i).getText());
            this.pages.add(page);
            this.pagesGroup.getChildren().add(page);
            
            x += pageWidth + pageInterval;
            i++;
        }
        this.pagesPane.getChildren().add(pagesGroup);
        this.wordCloudPane.getChildren().add(wordCloudGroup);
            this.getChildren().add(pagesGroup);
            this.getChildren().add(wordCloudGroup);

        //set up zoom levels
        for (int j = 0; j <= maxScale; j++) {
            array[j] = Math.pow(1.05, j - 81);
            System.out.println("array["+j+"]: "+array[j]);
        }
        System.out.println("screen properties:"+
                "\nmax X: "+screenBounds.getMaxX()+
                "\nmax Y: "+screenBounds.getMaxY());
        
        this.defineRibbonEvents();
        this.setRibbonEvents(true);
        
        //finally, set the zoom levels for clouds
        setCloudZoomLevels();
        
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


        Scale scale = new Scale(array[curScale], array[curScale],x,y);
        Scale scale2 = new Scale(array[maxScale-10]-array[curScale], array[maxScale-10]-array[curScale]);
        
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
                if (stackPane.getTransforms().size() > 0) {
            for (int j = 0; j < stackPane.getTransforms().size(); j++) {
                stackPane.getTransforms().remove(j);
            }
        }
        //add the transformation to the groups
        //pagesGroup.getTransforms().add(scale);
        //wordCloudGroup.getTransforms().add(scale2);
                stackPane.getTransforms().add(scale);
        System.out.println("x: "+x+"y: "+y+"\nstackPane parent bound x: "+stackPane.getBoundsInParent().getWidth()+"stackPane parent bound y: "+stackPane.getBoundsInParent().getHeight());
        FlowReader.zoomLabel.setText("zoom: " + ((float) curScale / (float) maxScale) * 100
                + "%\ncurScale: " + curScale
                + "\nmin Scale: " + minScale
                + "\nmax Scale: " + maxScale);
    }

    public void setOpacity() {
        double opacity;
        opacity = curScale / (double) opaqueScale;
        //stackPane.setOpacity(opacity);
          
           
            //pagesGroup.setOpacity(opacity);
            //wordCloudGroup.setOpacity(1-opacity);
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
                       //     "\nScreen X: "+event.getScreenX()+"Screen Y: "+event.getScreenY()+
                       //     "\nget X: "+event.getX()+"get Y: "+event.getY());
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
                //System.out.println(page.getOnDragDetected().toString());
            } else {
                pages.get(pageNum).setOnDragDetected(null);
            }
            pageNum++;
        }

    }
}
