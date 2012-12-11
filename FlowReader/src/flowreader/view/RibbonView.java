/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import flowreader.core.Page;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class RibbonView extends Group {

    private ArrayList<Page> pages;
    int pageWidth = 500;
    int pageHeight = 700;
    int pageInterval = 5;
    int pagesNumber = 30;
    int maxScale = 35;
    int minScale = 0;
    int curScale = 15;
    int opaqueScale =15;
StackPane stackPane;
    public RibbonView(StackPane stackPane) {
        this.pages = new ArrayList<Page>();
        this.stackPane = stackPane;
    }

    public void buildRibbon(int pagesNumber) {
        this.pagesNumber = pagesNumber;

        int i = 0;
        int x = 0;
        int y = 0;
        while (i < pagesNumber) {
            Page page = new Page(new Rectangle(x, y, pageWidth, pageHeight));
            this.pages.add(page);
            this.getChildren().add(page.getPage());
            x += pageWidth + pageInterval;
            i++;
        }
        this.setRibbonEvents();
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

    private void setRibbonEvents() {
        EventHandler<MouseEvent> swipeHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    previousEvent = event;
                    //System.out.println("PRESSED");
                } else if (event.getEventType()
                        .equals(MouseEvent.MOUSE_DRAGGED)) {

                    //System.out.println("DRAGGED");
                    double dx = event.getSceneX() - previousEvent.getSceneX();
                    double dy = event.getSceneY() - previousEvent.getSceneY();
                    stackPane.setLayoutX(stackPane.getLayoutX() + dx);
                    stackPane.setLayoutY(stackPane.getLayoutY() + dy);

                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), stackPane);
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

        RibbonView.this.setOnMouseDragged(swipeHandler);
        RibbonView.this.setOnMousePressed(swipeHandler);
        RibbonView.this.setOnMouseReleased(swipeHandler);
    }
}
