/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.view.RibbonElement;
import flowreader.model.Page;
import flowreader.utils.PageViewFactory;
import flowreader.utils.Parameters;
import flowreader.view.PageView;
import flowreader.view.TextPageView;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class DivePagesRibbonPane extends DiveRibbonPane {

    public DivePagesRibbonPane(int index, ArrayList<Page> pages, double x, double y) {
        super(index, x, y, Parameters.pageWidth, Parameters.pageHeight);
        // Creation of the pages
        for (Page p : pages) {
            System.out.println("pages ");
            PageView pv = PageViewFactory.getPageView(p); 
            pv.relocate(x,y + 50 + (Parameters.pageHeight / 3));
            this.ribbonElts.add(pv);
            x = x + this.elementWidth + this.elementInterval;
        }

        for (RibbonElement dre : this.ribbonElts) {
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
                //this.ribbonElts.get(i).setVisible(true);
            } else {
                this.ribbonElts.get(i).setOpacity(0);
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
                            //ribbonElts.get(i).setVisible(true);
                        } else {
                            ribbonElts.get(i).setOpacity(0);
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
                previousEvent = event;
                event.consume();
            }
        };

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
    }

    private ArrayList<Integer> culling() {
        int numberOfPagesInScreen = (int) (Screen.getPrimary().getBounds().getWidth()/this.elementWidth);
        int numberOfPages = (numberOfPagesInScreen*3);
        if(numberOfPages%2==0){
            numberOfPages+=1;
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
            if (indexToAdd < this.ribbonElts.size()) {
                indexToDisplay.add(indexToAdd);
            }
        }
        return indexToDisplay;
    }
}
