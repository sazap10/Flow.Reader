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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 *
 * @author Pat
 */
public class TheViewScene extends BorderPane {

    PagesScene pagesPane;
    WordCloudsScene wordCloudsPane;
    private EventHandler<MouseEvent> swipeHandler;

    public TheViewScene(PagesScene pagesPane, WordCloudsScene wordCloudsPane) {
        this.pagesPane = pagesPane;
        this.wordCloudsPane = wordCloudsPane;
    }

    public void buildTheView() {
        this.setCenter(pagesPane);
        this.setBottom(wordCloudsPane);
        
        pagesPane.setEvents(false);
        wordCloudsPane.setEvents(false);
        
        this.defineEvents();
        this.setEvents();

    }

    private void move(double dx, double dy) {
        this.setLayoutX(this.getLayoutX() + dx);
        this.setLayoutY(this.getLayoutY() + dy);
    }

    private void defineEvents() {
        swipeHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    previousEvent = event;
                } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

                    double dx = event.getScreenX() - previousEvent.getScreenX();
                    double dy = event.getScreenY() - previousEvent.getScreenY();
                    move(dx, dy);

                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), TheViewScene.this);
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

    private void setEvents() {
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
    }
}
