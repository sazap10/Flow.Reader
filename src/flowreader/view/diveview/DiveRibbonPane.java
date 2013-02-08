/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public abstract class DiveRibbonPane extends StackPane {

    protected Group ribbon;
    protected ArrayList<DiveRibbonElement> ribbonElts;
    protected int elementInterval = 5;
    protected double elementWidth;
    protected double elementHeight;
    private ArrayList<Integer> selected;

    public DiveRibbonPane(double x, double y, double elementWidth, double elementHeight) {
        this.ribbon = new Group();
        this.ribbonElts = new ArrayList<>();
        this.ribbon.setLayoutX(x);
        this.ribbon.setLayoutY(y);
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.selected = new ArrayList<>();
        this.elementSelection();
        this.elementHovering();
    }

    public void createRibbon(ArrayList<Integer> indexes) {
        /*String s = "new group : ";
         for(Integer i: indexes){
         s+=i+" ";
         }
         System.out.println(s);*/
        this.ribbon.getChildren().clear();
        for (Integer i : indexes) {
            this.ribbon.getChildren().add(this.ribbonElts.get(i));
        }
        this.selected = indexes;
    }

    public double getRibbonLayoutX() {
        return this.ribbon.getLayoutX();
    }

    public void setNewPosition(double posX, double posY) {
        this.ribbon.setLayoutX(posX);
        this.ribbon.setLayoutY(posY);
    }

    public void move(double dX, double dY) {
        this.ribbon.setLayoutX(this.ribbon.getLayoutX() + dX);
        this.ribbon.setLayoutY(this.ribbon.getLayoutY() + dY);
    }

    public double getRibbonWidth() {
        return this.ribbon.getBoundsInLocal().getWidth();
    }

    private void elementHovering() {
        EventHandler<MouseEvent> hoveringHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_MOVED)) {
                    DiveRibbonPane.this.highlightHovered();
                }
            }
        };

        this.addEventHandler(MouseEvent.MOUSE_MOVED, hoveringHandler);
    }

    private void elementSelection() {
        EventHandler<ScrollEvent> selectionHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                DiveRibbonPane.this.highlightSelected();
            }
        };


        this.addEventHandler(ScrollEvent.SCROLL, selectionHandler);
        
        EventHandler<ZoomEvent> selectionZoomHandler = new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                DiveRibbonPane.this.highlightSelected();
            }
        };


        this.addEventHandler(ZoomEvent.ZOOM, selectionZoomHandler);
    }

    public void highlightSelected() {
        this.selected.clear();
        for (int i = 0; i < this.ribbonElts.size(); i++) {
            DiveRibbonElement dre = this.ribbonElts.get(i);
            if (dre.isHover()) {
                dre.setHighlight(true);
                this.selected.add(i);
            } else {
                dre.setHighlight(false);
            }
        }
    }

    public void highlightHovered() {
        for (int i = 0; i < this.ribbonElts.size(); i++) {
            DiveRibbonElement dre = this.ribbonElts.get(i);
            if (dre.isHover()) {
                dre.setHighlight(true);
            } else {
                dre.setHighlight(false);
            }
        }
    }

    public void highlightIndex(int index) {
        this.ribbonElts.get(index).setHighlight(true);
        this.selected.add(index);
    }

    public void highlightAll(boolean on) {
        this.selected.clear();
        for (int i = 0; i < this.ribbonElts.size(); i++) {
            this.ribbonElts.get(i).setHighlight(on);
            if (on) {
                this.selected.add(i);
            }
        }
    }

    public ArrayList<Integer> getSelectedIndexes() {
        return this.selected;
    }

    public int getNumberOfElements() {
        return this.ribbonElts.size();
    }

    public double getFocusPoint() {

        double focusSquareWidth = (this.elementWidth * this.ribbon.getChildren().size()) + (this.elementInterval * (this.ribbon.getChildren().size()));
        double focusPointInSquare = focusSquareWidth / 2.0;

        return focusPointInSquare;
    }

    public ParallelTransition appearTransitionDiveIn() {
        int duration = 1000;

        FadeTransition ft = new FadeTransition(Duration.millis(duration), this);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ScaleTransition st = new ScaleTransition(Duration.millis(duration), this);
        st.setFromX(0.0);
        st.setFromY(0.0);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft, st);
        pt.setCycleCount(1);
        return pt;
    }

    public ParallelTransition disappearTransitionDiveIn() {
        int duration = 1000;

        FadeTransition ft = new FadeTransition(Duration.millis(duration), this);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ScaleTransition st = new ScaleTransition(Duration.millis(duration), this);
        st.setToX(10f);
        st.setToY(10f);
        st.setCycleCount(1);
        st.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft, st);
        pt.setCycleCount(1);
        return pt;
    }

    public ParallelTransition appearTransitionDiveOut() {
        int duration = 1000;

        FadeTransition ft = new FadeTransition(Duration.millis(duration), this);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ScaleTransition st = new ScaleTransition(Duration.millis(duration), this);
        st.setFromX(10.0);
        st.setFromY(10.0);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft, st);
        pt.setCycleCount(1);
        return pt;
    }

    public ParallelTransition disappearTransitionDiveOut() {
        int duration = 1000;

        FadeTransition ft = new FadeTransition(Duration.millis(duration), this);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ScaleTransition st = new ScaleTransition(Duration.millis(duration), this);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(0.0);
        st.setToY(0.0);
        st.setCycleCount(1);
        st.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(ft, st);
        pt.setCycleCount(1);
        return pt;
    }
}
