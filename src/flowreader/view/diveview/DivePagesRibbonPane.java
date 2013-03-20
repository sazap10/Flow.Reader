/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Page;
import flowreader.utils.PageViewFactory;
import flowreader.utils.Parameters;
import flowreader.view.PageView;
import flowreader.view.RibbonElement;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Represent a ribbon of pages
 *
 * @author D-Day
 */
public class DivePagesRibbonPane extends DiveRibbonPane {

    public DivePagesRibbonPane(int index, ArrayList<Page> pages, double x, double y) {
        super(index, x, y, Parameters.PAGE_WIDTH, Parameters.PAGE_HEIGHT);
        // Creation of the pages
        for (Page p : pages) {
            PageView pv = PageViewFactory.getPageView(p);
            pv.relocate(x, y + 50 + (Parameters.PAGE_HEIGHT / 3));
            this.ribbonElements.add(pv);
            x = x + this.elementWidth + this.elementInterval;
        }

        for (RibbonElement dre : this.ribbonElements) {
            dre.setOpacity(0);
        }

        this.getChildren().clear();
        this.ribbon.getChildren().clear();
        this.ribbon.getChildren().addAll(this.ribbonElements);
        this.getChildren().add(this.ribbon);
        this.swipe();
    }

    @Override
    public void createRibbon(ArrayList<Integer> indexes) {
        this.selected = indexes;

        double focusPoint = this.getFocusPoint();
        double x = (Screen.getPrimary().getBounds().getWidth() / 2) - focusPoint;
        ArrayList<Integer> cullresult = this.culling();
        for (int i = 0; i < this.ribbonElements.size(); i++) {
            if (cullresult.contains(i)) {
                this.ribbonElements.get(i).setOpacity(1);
                //this.ribbonElts.get(i).setVisible(true);
            } else {
                this.ribbonElements.get(i).setOpacity(0);
                //this.ribbonElts.get(i).setVisible(false);
            }
        }

        this.setTranslateX(x);
    }

    @Override
    public double getFocusPoint() {
        //double numberOfPagesBefore = this.culling().size()/2;
        double numberOfPagesBefore = this.selected.get(0);
        double xSelectedElement = (this.elementWidth * numberOfPagesBefore) + (this.elementInterval * numberOfPagesBefore);
        double middleSelectedElement = this.elementWidth / 2.0;
        double focusPoint = xSelectedElement + middleSelectedElement;
        return focusPoint;
    }

    /**
     * Create and manage the swipe event
     */
    private void swipe() {
        EventHandler<MouseEvent> swipeHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                    previousEvent = event;
                } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                    if (selected.size() >= 1) {
                        double dx = event.getX() - previousEvent.getX();
                        move(dx, 0);
                        ArrayList<Integer> cullresult = culling();
                        for (int i = 0; i < ribbonElements.size(); i++) {
                            if (cullresult.contains(i)) {
                                ribbonElements.get(i).setOpacity(1);
                                //ribbonElts.get(i).setVisible(true);
                            } else {
                                ribbonElements.get(i).setOpacity(0);
                                //ribbonElts.get(i).setVisible(false);
                            }
                        }

                        TranslateTransition tt = new TranslateTransition(Duration.millis(100), DivePagesRibbonPane.this);
                        tt.setByX(dx);
                        //tt.setByY(dy);
                        tt.setCycleCount(0);
                        tt.setAutoReverse(true);
                        tt.play();
                    }
                }
                previousEvent = event;
                event.consume();
            }
        };

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
    }

    /**
     * @return the pages that should be displayed at any moment
     */
    private ArrayList<Integer> culling() {
        int numberOfPagesInScreen = (int) (Screen.getPrimary().getBounds().getWidth() / this.elementWidth);
        int numberOfPages = (numberOfPagesInScreen * 3);
        if (numberOfPages % 2 == 0) {
            numberOfPages += 1;
        }
        ArrayList<Integer> indexToDisplay = new ArrayList<Integer>();
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
            if (indexToAdd < this.ribbonElements.size()) {
                indexToDisplay.add(indexToAdd);
            }
        }
        return indexToDisplay;
    }
}
