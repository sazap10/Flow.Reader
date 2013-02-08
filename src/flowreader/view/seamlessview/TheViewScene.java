/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.seamlessview;

import flowreader.view.PagesScene;
import flowreader.view.WordCloudsScene;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author Pat
 */
public class TheViewScene extends StackPane {

    int maxScale = 100;
    int minScale = 0;
    int curScale = 80;
    	double[] array = new double[maxScale + 1];
	private Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    PagesScene pagesPane;
    WordCloudsScene wordCloudsPane;
    private EventHandler<MouseEvent> swipeHandler;
    private EventHandler<ScrollEvent> scrollHandler;
    private EventHandler<ZoomEvent> zoomHandler;
        Scale previousScale = new Scale(1,1);
        Scale scale = new Scale(1,1);
        
                VBox VBox;

    public TheViewScene(PagesScene pagesPane, WordCloudsScene wordCloudsPane) {
        this.setId("theView");
        this.pagesPane = pagesPane;
        this.wordCloudsPane = wordCloudsPane;
        this.VBox=new VBox();
    }

    public void buildTheView() {
        //this.setAlignment(Pos.BOTTOM_CENTER);
        //this.setMinHeight(1);
        VBox.getChildren().add(pagesPane);
        VBox.getChildren().add(wordCloudsPane);
        // this.setAlignment(Pos.TOP_CENTER);

        this.VBox.setSpacing(170);
        pagesPane.setEvents(false);
        wordCloudsPane.setEvents(false);
        
        for (int j = 0; j <= maxScale; j++) {
            array[j] = Math.pow(1.05, j - 81);
        }
        
        this.defineEvents();
        this.setEvents();
this.getChildren().add(VBox);
    }

    private void move(double dx, double dy) {
        this.VBox.setLayoutX(this.VBox.getLayoutX() + dx);
        this.VBox.setLayoutY(this.VBox.getLayoutY() + dy);
    }

    public void zoom(double deltaY, double x, double y) {
        if (deltaY <= 0) {
            if (curScale < minScale + 1) {
            } else {
                curScale--;
            }
        } else {
            if (curScale > maxScale - 1) {
            } else {
                curScale++;
            }
        }

        previousScale = scale;
        scale = new Scale(array[curScale], array[curScale], x, y);

//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!"+stackPane.getTransforms().toString());

        TheViewScene.this.getTransforms().remove(previousScale);
        TheViewScene.this.getTransforms().add(scale);


        /*FlowReader.zoomLabel.setText("zoom: "
         + ((float) curScale / (float) maxScale) * 100 + "%\ncurScale: "
         + curScale + "\nmin Scale: " + minScale + "\nmax Scale: "
         + maxScale);*/
    }

    private void defineEvents() {
        swipeHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    previousEvent = event;
                } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

                    double dx = event.getX() - previousEvent.getX();
                    double dy = event.getY() - previousEvent.getY();
                    move(dx, dy);

                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), TheViewScene.this.VBox);
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
                zoom(delta, event.getX(), event.getY());
                event.consume();
            }
        };

    }

    private void setEvents() {
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
            this.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
            this.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
    }
}
