/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Page;
import flowreader.view.PageView;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class DivePagesRibbonPane extends DiveRibbonPane {

    public DivePagesRibbonPane(int index, ArrayList<Page> pages, double x, double y) {
        super(index, x, y, PageView.width, PageView.height);
        // Creation of the pages
        for (Page p : pages) {
            this.ribbonElts.add(new DivePage(p.getText(), x, y, this.elementWidth, this.elementHeight));
            x = x + this.elementWidth + this.elementInterval;
        }

        for (DiveRibbonElement dre : this.ribbonElts) {
            dre.setOpacity(0);
        }
        
        this.getChildren().clear();
        this.ribbon.getChildren().clear();
        this.ribbon.getChildren().addAll(this.ribbonElts);
        this.getChildren().add(this.ribbon);
        this.swipe();
    }

    @Override
    public void createRibbon(ArrayList<Integer> indexes) {
        this.selected = indexes;

        double focusPoint = this.getFocusPoint();
        double x = (Screen.getPrimary().getBounds().getWidth() / 2) - focusPoint;
        ArrayList<Integer> cullresult = this.culling();
        for (int i = 0; i < this.ribbonElts.size(); i++) {
            if (cullresult.contains(i)) {
                this.ribbonElts.get(i).setOpacity(1);
            } else {
                this.ribbonElts.get(i).setOpacity(0);
            }
        }

        this.setTranslateX(x);
    }

    @Override
    public double getFocusPoint() {
        double xSelectedElement = (this.elementWidth * this.selected.get(0)) + (this.elementInterval * (this.selected.get(0)));
        double middleSelectedElement = this.elementWidth / 2.0;
        double focusPoint = xSelectedElement + middleSelectedElement;
        return focusPoint;
    }

    private void swipe() {
        EventHandler<MouseEvent> swipeHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    previousEvent = event;
                    // System.out.println("PRESSED");
                } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

                    // System.out.println("DRAGGED");
                    double dx = event.getX() - previousEvent.getX();
                    //double dy = event.getY() - previousEvent.getY();
                    move(dx, 0);
                    ArrayList<Integer> cullresult = culling();
                    for (int i = 0; i < ribbonElts.size(); i++) {
                        if (cullresult.contains(i)) {
                            ribbonElts.get(i).setOpacity(1);
                        } else {
                            ribbonElts.get(i).setOpacity(0);
                        }
                    }

                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), DivePagesRibbonPane.this);
                    tt.setByX(dx);
                    //tt.setByY(dy);
                    tt.setCycleCount(0);
                    tt.setAutoReverse(true);
                    tt.play();
                }
                previousEvent = event;
                event.consume();
            }
        };

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
    }

    private ArrayList<Integer> culling() {
        int numberOfPages = 11;
        ArrayList<Integer> indexToDisplay = new ArrayList<>();
        Integer select = this.selected.get(0);
        for (int i = 0; i < (numberOfPages / 2); i++) {
            int indexToAdd = select - (i + 1);
            if (indexToAdd >= 0) {
                indexToDisplay.add(indexToAdd);
            }
        }
        indexToDisplay.add(select);
        for (int i = 0; i < (numberOfPages / 2); i++) {
            int indexToAdd = select + (i + 1);
            if (indexToAdd < this.ribbonElts.size()) {
                indexToDisplay.add(indexToAdd);
            }
        }
        return indexToDisplay;
    }
}
