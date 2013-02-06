/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class DiveViewScene extends StackPane {

    private ArrayList<DiveRibbonPane> levels;
    private int currentLevel;
    private BorderPane bp;
    private LiftPane lp;

    public DiveViewScene(Document document) {
        bp = new BorderPane();

        ArrayList<ArrayList<WordCloud>> wordClouds = document.getWordClouds();
        ArrayList<Page> pages = document.getPages();
        
        lp = new LiftPane(wordClouds.size()+1);

        double x = 0, y = 0;
        this.levels = new ArrayList<>();
        // Creation of the pages
        DiveRibbonPane pagesLevel = new DivePagesRibbonPane(pages, x, y);
        this.levels.add(pagesLevel);

        // Creation of the word clouds
        for (int i = 0; i < wordClouds.size(); i++) {
            DiveRibbonPane wcp = new DiveWcRibbonPane(wordClouds.get(i), x, y);
            x = x + (wcp.getRibbonWidth() / 4);
            this.levels.add(wcp);
        }
        this.currentLevel = this.levels.size() - 1;
        ArrayList<Integer> ali = new ArrayList<>();
        ali.add(0);
        this.levels.get(this.currentLevel).createRibbon(ali);
                
        bp.setCenter(this.levels.get(this.currentLevel));
        this.lp.setHighLight(currentLevel);
        bp.setRight(lp);
        this.getChildren().add(bp);
        //this.swipe();
        this.diveIn();
        this.diveOut();
        
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
                    double dy = event.getY() - previousEvent.getY();
                    for (DiveRibbonPane rp : DiveViewScene.this.levels) {
                        rp.move(dx, dy);
                    }

                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), DiveViewScene.this);
                    tt.setByX(dx);
                    tt.setByY(dy);
                    tt.setCycleCount(0);
                    tt.setAutoReverse(true);
                    tt.play();
                } else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

                    if (event.getClickCount() == 2) {
                        if (event.getButton().equals(MouseButton.PRIMARY) && DiveViewScene.this.currentLevel != 0) {
                            DiveViewScene.this.currentLevel -= 1;
                            DiveViewScene.this.getChildren().clear();
                            DiveViewScene.this.getChildren().add(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel));
                        } else if (event.getButton().equals(MouseButton.SECONDARY) && DiveViewScene.this.currentLevel != DiveViewScene.this.levels.size() - 1) {
                            DiveViewScene.this.currentLevel += 1;
                            DiveViewScene.this.getChildren().clear();
                            DiveViewScene.this.getChildren().add(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel));
                        }


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

    private void diveIn() {
        EventHandler<MouseEvent> diveInHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    if (event.getClickCount() == 2) {
                        if (event.getButton().equals(MouseButton.PRIMARY) && DiveViewScene.this.currentLevel != 0) {
                            //Collect data from the previous level
                            double previousFocusPoint = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).getFocusPoint();
                            int previousSelectedIndex = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).getSelectedIndexes().get(0);

                            // Go to the next level
                            DiveViewScene.this.currentLevel -= 1;
                            DiveViewScene.this.lp.setHighLight(currentLevel);
                            if (DiveViewScene.this.currentLevel == 0) {
                                ArrayList<Integer> ali = new ArrayList<>();
                                ali.add(previousSelectedIndex);
                                DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).createRibbon(ali);
                            } else {
                                DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).createRibbon(getIndexesCurrentLevel(previousSelectedIndex));
                            }
                            double focusPoint = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).getFocusPoint();
                            double x = 0 + (previousFocusPoint-focusPoint);
                            DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).setNewPosition(x, 0);
                            DiveViewScene.this.bp.setCenter(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel));
                        }
                    }

                }
                previousEvent = event;
                event.consume();
            }
        };
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, diveInHandler);
    }

    private void diveOut() {
        EventHandler<MouseEvent> diveOutHandler = new EventHandler<MouseEvent>() {
            MouseEvent previousEvent;

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    if (event.getClickCount() == 2) {
                        if (event.getButton().equals(MouseButton.SECONDARY) && DiveViewScene.this.currentLevel != DiveViewScene.this.levels.size() - 1) {
                            DiveViewScene.this.currentLevel += 1;
                            DiveViewScene.this.lp.setHighLight(currentLevel);
                            DiveViewScene.this.bp.setCenter(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel));
                        }
                    }

                }
                previousEvent = event;
                event.consume();
            }
        };
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, diveOutHandler);
    }

    private ArrayList<Integer> getIndexesCurrentLevel(int previousSelectedIndex) {
        ArrayList<Integer> temp = new ArrayList<>();

        temp.add(previousSelectedIndex * 2);
        temp.add((previousSelectedIndex * 2) + 1);
        
        if((this.levels.get(currentLevel).getNumberOfElements()%2 == 1) && (this.levels.get(currentLevel+1).getNumberOfElements()-1 == previousSelectedIndex)){
            temp.add((previousSelectedIndex * 2) + 2);
        }
        
        /*String s = "previous " + previousSelectedIndex + " current ";
        for (Integer i : temp) {
            s += i + " ";
        }
        System.out.println(s);*/
        return temp;
    }
}