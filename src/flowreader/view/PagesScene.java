/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Page;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class PagesScene extends StackPane{
     private Group pageGroup;
     private ArrayList<PageView> pagesView;
     private int pageInterval = 5;

     private int maxPageZoom = 100;
     private int curPageZoom = 50;
     private int minPageZoom = 40;
     private double[] scaleFactors = new double[maxPageZoom + 1];
     
     private EventHandler<MouseEvent> swipeHandler;
     private EventHandler<ScrollEvent> scrollHandler;
     private EventHandler<ZoomEvent> zoomHandler;
     
     public PagesScene(ArrayList<Page> pages){
         this.pageGroup = new Group();
         this.pagesView = new ArrayList<>();
         
         // Creation of the pages
         double x=0;
         double y=0;
         for(Page p : pages){
            this.pagesView.add(new PageView(x, y, p.getText()));
            x = x + PageView.width + this.pageInterval;
         }
         
         this.pageGroup.getChildren().addAll(this.pagesView);
         this.getChildren().add(this.pageGroup);
         
         // Creates the zoom factors
        for (int j = 0; j <= maxPageZoom; j++) {
            scaleFactors[j] = Math.pow(1.05, j - 81);
        }
        
        this.defineEvents();
        this.setEvents(true);
        zoom(1,Screen.getPrimary().getBounds().getWidth()/2, Screen.getPrimary().getBounds().getHeight()/2);
     }
     
    public void zoom(double deltaY, double x, double y) {   
         // Set the zoom level
            if (deltaY <= 0) { // If scroll down
                if (curPageZoom > minPageZoom) { // if we are above the minimum zoom
                    curPageZoom--;
                }
            } 
            else{ // if we scroll up
                if (curPageZoom < maxPageZoom){ // if we are below the maximum zoom
                    curPageZoom++;
                }
            }
            
            Scale scale = new Scale(scaleFactors[curPageZoom], scaleFactors[curPageZoom], x, y);

            this.getTransforms().clear();
            this.getTransforms().add(scale);
     }
     
    private void move(double dx, double dy) {
        this.pageGroup.setLayoutX(this.pageGroup.getLayoutX()+dx);
        this.pageGroup.setLayoutY(this.pageGroup.getLayoutY()+dy);
    }
     
    private void defineEvents() {
            swipeHandler = new EventHandler<MouseEvent>() {
                MouseEvent previousEvent;

                    @Override
                    public void handle(MouseEvent event) {
                            if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                                    previousEvent = event;
                                    // System.out.println("PRESSED");
                            } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

                                    // System.out.println("DRAGGED");
                                    double dx = event.getX() - previousEvent.getX();
                                    double dy = event.getY() - previousEvent.getY();
                                    move(dx, dy);
                                    
                                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), PagesScene.this.pageGroup);
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
                                double height = PagesScene.this.getLayoutBounds().getHeight();
                                double width = PagesScene.this.getLayoutBounds().getWidth();
                                PagesScene.this.zoom(event.getDeltaY(), Screen.getPrimary().getBounds().getWidth()/2, Screen.getPrimary().getBounds().getHeight()/2);
                        }
                        event.consume();
                    }
            };

            zoomHandler = new EventHandler<ZoomEvent>() {
                    @Override
                    public void handle(ZoomEvent event) {
                        double delta = event.getZoomFactor() - 1;
                       PagesScene.this.zoom(delta, event.getX(), event.getY());
                        event.consume();
                    }
            };
            
        }
    
        public void setEvents(boolean on) {
            if(on){
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
            this.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
            this.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
        }else{
                this.removeEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            this.removeEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            this.removeEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);

}
}
}
