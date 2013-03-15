/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.view.RibbonElement;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Represents a ribbon of RibbonElement
 * @author D-Day
 */
public abstract class DiveRibbonPane extends StackPane {

    protected Group ribbon;
    protected ArrayList<RibbonElement> ribbonElts;
    protected int elementInterval = 5;
    protected double elementWidth;
    protected double elementHeight;
    protected ArrayList<Integer> selected;
    protected int index;

    public DiveRibbonPane(int index, double x, double y, double elementWidth, double elementHeight) {
        this.ribbon = new Group();
        this.ribbonElts = new ArrayList<RibbonElement>();
        this.ribbon.setLayoutX(x);
        this.ribbon.setLayoutY(y);
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.selected = new ArrayList<Integer>();
        this.elementSelection();
        this.index = index;
    }

    /**
     * Create the ribbon with the element having the indexes contains in indexes
     * @param indexes 
     */
    public abstract void createRibbon(ArrayList<Integer> indexes);

    /**
     * Move the entire ribbon
     * @param dX
     * @param dY 
     */
    public void move(double dX, double dY) {
        this.ribbon.setLayoutX(this.ribbon.getLayoutX() + dX);
        this.ribbon.setLayoutY(this.ribbon.getLayoutY() + dY);
    }

    /**
     * Create the event handler for the selection of an element
     */
    private void elementSelection() {
        EventHandler<MouseEvent> hoveringHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_MOVED)) {
                    DiveRibbonPane.this.highlightSelected();
                }
            }
        };
        this.addEventHandler(MouseEvent.MOUSE_MOVED, hoveringHandler);
    }

    /**
     * Hightlight the elements selected
     */
    public void highlightSelected() {
        this.selected.clear();
        for (int i = 0; i < this.ribbonElts.size(); i++) {
            RibbonElement dre = this.ribbonElts.get(i);
            if (dre.isHover()) {
                dre.setHighlight(true);
                this.selected.add(i);
            } else {
                dre.setHighlight(false);
            }
        }
    }

    /**
     * @return the indexes selected
     */
    public ArrayList<Integer> getSelectedIndexes() {
        return this.selected;
    }

    /**
     * @return the total number of elements
     */
    public int getNumberOfElements() {
        return this.ribbonElts.size();
    }

    /**
     * @return the point where the view should focus according to the selected elements
     */
    public abstract double getFocusPoint();

    /**
     * @return a transiton of a ribbon appearing during a dive in
     */
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

    /**
     * @return a transiton of a ribbon disappearing during a dive in
     */
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

    /**
     * @return a transiton of a ribbon appearing during a dive out
     */
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

    /**
     * @return a transiton of a ribbon disappearing during a dive out
     */
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
