/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public class DiveViewScene extends StackPane {

    private ArrayList<DiveRibbonPane> levels;
    private int currentLevel;
    private BorderPane bp;
    private StackPane contentPane;
    private LiftPane lp;
    private boolean otherTransitionsFinished = true;

    public DiveViewScene(Document document) {
        bp = new BorderPane();
        this.contentPane = new StackPane();

        ArrayList<ArrayList<WordCloud>> wordClouds = document.getWordClouds();
        ArrayList<Page> pages = document.getPages();

        lp = new LiftPane(wordClouds.size() + 1);

        double x = 0, y = 0;
        this.levels = new ArrayList<DiveRibbonPane>();
        // Creation of the pages
        DiveRibbonPane pagesLevel = new DivePagesRibbonPane(0, pages, x, y);
        this.levels.add(pagesLevel);

        // Creation of the word clouds
        for (int i = 0; i < wordClouds.size(); i++) {
            DiveRibbonPane wcp = new DiveWcRibbonPane(i + 1, wordClouds.get(i), x, y);
            this.levels.add(wcp);
        }
        this.currentLevel = this.levels.size() - 1;
        ArrayList<Integer> ali = new ArrayList<Integer>();
        ali.add(0);
        this.levels.get(this.currentLevel).createRibbon(ali);

        this.contentPane.getChildren().add(this.levels.get(this.currentLevel));
        bp.setCenter(this.contentPane);
        this.lp.setHighLight(currentLevel);
        bp.setRight(lp);
        this.getChildren().add(bp);
        this.setEvents();
    }

    private void diveIn() {
        // if not on the last level and an index is selected
        if (DiveViewScene.this.currentLevel != 0 && DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).getSelectedIndexes().size() > 0) {
            DiveViewScene.this.otherTransitionsFinished = false; // Start the transition

            //Collect data from the previous level
            DiveRibbonPane previous = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel);
            int previousSelectedIndex = previous.getSelectedIndexes().get(0);

            // Set up current level
            DiveViewScene.this.currentLevel -= 1;
            DiveViewScene.this.lp.setHighLight(currentLevel);
            DiveRibbonPane current = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel);

            if (DiveViewScene.this.currentLevel == 0) { // if we are on the pages
                ArrayList<Integer> ali = new ArrayList<Integer>();
                ali.add(previousSelectedIndex); // we select the index selected on the previous level
                current.createRibbon(ali);
            } else {
                current.createRibbon(getIndexesCurrentLevelDiveIn(previousSelectedIndex, DiveViewScene.this.currentLevel)); // We display only the indexes corresponding to the index previously selected
            }

            //We add the current level in the scene
            DiveViewScene.this.contentPane.getChildren().add(current);

            // Run the transition effects
            ParallelTransition at = current.appearTransitionDiveIn();
            ParallelTransition dt = previous.disappearTransitionDiveIn();
            at.play();
            dt.play();

            // When the transition is finished we remove the previous level
            dt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DiveViewScene.this.contentPane.getChildren().remove(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel + 1));
                    DiveViewScene.this.otherTransitionsFinished = true; // Transition is finished
                }
            });
        }
    }

    private void diveOut() {
        // if not on the first level and an index is selected
        if (DiveViewScene.this.currentLevel != DiveViewScene.this.levels.size() - 1 && DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel).getSelectedIndexes().size() > 0) {
            DiveViewScene.this.otherTransitionsFinished = false; // Start the transition

            //Collect data from the previous level
            DiveRibbonPane previous = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel);
            int previousSelectedIndex = previous.getSelectedIndexes().get(0);

            // Set up current level
            DiveViewScene.this.currentLevel += 1;
            DiveViewScene.this.lp.setHighLight(currentLevel);
            DiveRibbonPane current = DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel);

            current.createRibbon(getIndexesCurrentLevelDiveOut(previousSelectedIndex, DiveViewScene.this.currentLevel));

            DiveViewScene.this.contentPane.getChildren().add(current);
            ParallelTransition at = current.appearTransitionDiveOut();
            ParallelTransition dt = previous.disappearTransitionDiveOut();
            at.play();
            dt.play();

            dt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DiveViewScene.this.contentPane.getChildren().remove(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel - 1));
                    DiveViewScene.this.otherTransitionsFinished = true;
                }
            });
        }
    }

    private ArrayList<Integer> getIndexesCurrentLevelDiveIn(int previousSelectedIndex, int level) {
        ArrayList<Integer> temp = new ArrayList<Integer>();

        temp.add(previousSelectedIndex * 2);
        temp.add((previousSelectedIndex * 2) + 1);

        if ((this.levels.get(level).getNumberOfElements() % 2 == 1) && (this.levels.get(level + 1).getNumberOfElements() - 1 == previousSelectedIndex)) {
            temp.add((previousSelectedIndex * 2) + 2);
        }
        return temp;
    }

    private ArrayList<Integer> getIndexesCurrentLevelDiveOut(int previousSelectedIndex, int level) {
        ArrayList<Integer> temp = new ArrayList<Integer>();

        if (level == this.levels.size() - 1) {
            temp.add(0);
        } else if (level == 1) {
            temp = getIndexesCurrentLevelDiveIn(previousSelectedIndex / 2, level);
        } else {
            if ((this.levels.get(level - 1).getNumberOfElements() % 2 == 1) && (this.levels.get(level - 1).getNumberOfElements() - 1 == previousSelectedIndex)) {
                previousSelectedIndex -= 1;
            }
            temp = getIndexesCurrentLevelDiveIn(previousSelectedIndex / 4, level - 1);
        }
        return temp;
    }

    private void setEvents() {
        EventHandler<ScrollEvent> diveOutScrollHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() < 0 && DiveViewScene.this.otherTransitionsFinished) {
                    diveOut();
                }
                event.consume();
            }
        };
        this.addEventHandler(ScrollEvent.SCROLL, diveOutScrollHandler);

        EventHandler<ZoomEvent> diveOutZoomHandler = new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                double delta = event.getZoomFactor() - 1;
                if (delta < 0 && DiveViewScene.this.otherTransitionsFinished) {
                    diveOut();
                }
                event.consume();
            }
        };
        //this.addEventHandler(ZoomEvent.ZOOM, diveOutZoomHandler);

        EventHandler<ScrollEvent> diveInScrollHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0 && DiveViewScene.this.otherTransitionsFinished) { // Scroll forward
                    diveIn();
                }
                event.consume();
            }
        };
        this.addEventHandler(ScrollEvent.SCROLL, diveInScrollHandler);

        EventHandler<ZoomEvent> diveInZoomHandler = new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                double delta = event.getZoomFactor() - 1;
                if (delta > 0 && DiveViewScene.this.otherTransitionsFinished) { // Scroll forward
                    diveIn();
                }
                event.consume();
            }
        };
        //this.addEventHandler(ZoomEvent.ZOOM, diveInZoomHandler);
    }
}
