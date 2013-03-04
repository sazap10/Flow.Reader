/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.view.diveview.DiveViewScene;
import flowreader.view.flowview.NewFlowViewScene;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 *
 * @author D-Day
 */
public class RibbonView extends StackPane {

    private DiveViewScene diveViewPane;
    private NewFlowViewScene newFlowPane;
    private Document document;
    private String currentView = "";
    private Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private StackPane home;

    public RibbonView() {
    }

    public RibbonView(Document document) {
        //this.pagesPane = new PagesScene(document.getPages());
        //this.wordCloudsPane = new WordCloudsScene(document.getWordClouds());
        //this.flowViewPane = new FlowViewScene(document);
        this.diveViewPane = new DiveViewScene(document);
        //this.theViewPane = new TheViewScene(pagesPane,wordCloudsPane);
        this.newFlowPane = new NewFlowViewScene(document);
        this.getChildren().add(this.diveViewPane);
        this.document = document;
        this.buildHomeView();
    }

    public void buildHomeView() {
        home = new StackPane();

        Bloom bloom = new Bloom();
        bloom.setThreshold(0.1);

        Rectangle rect = new Rectangle();

                rect.setX(0);
        rect.setY(0);
        rect.setWidth(screenBounds.getWidth());
        rect.setHeight(screenBounds.getHeight());
        rect.setFill(Color.DARKSLATEBLUE);

        Text text = new Text();
        text.setText("Welcome to FlowReader. Enjoy!\n\nKeyboard shortcuts:\nW:Zoom In\nS:Zoom Out\nA: Move Left\nD:Move Right"
                + "\nM: Matrix Theme\nN: Normal Theme\nG: Glow!\nQ:Switch View\nF: Reset\nR: Reset Effect\nL: Vertical Lock\nZ: Zoom Lock");

        
        text.setFill(Color.ALICEBLUE);
        text.setFont(Font.font(null, FontWeight.BOLD, 20));
        text.setX(25);
        text.setY(65);
        text.setEffect(bloom);

        home.getChildren().addAll(rect,text);


    }

    public void switchToHomeView() {
        this.getChildren().clear();

        this.getChildren().add(home);
        currentView = "HomeView";
    }

    public void switchToDiveView() {
        this.getChildren().clear();
        this.getChildren().add(this.diveViewPane);
        currentView = "DiveView";
    }

    public void switchToFlowView() {
        this.getChildren().clear();
        this.getChildren().add(this.newFlowPane);
        currentView = "FlowView";

    }

    public boolean getZoomLock() {
        return newFlowPane.getZoomLock();
    }

    public void setZoomLock(boolean lock) {
        newFlowPane.setZoomLock(lock);
    }

    public boolean getVerticalLock() {
        return newFlowPane.getVerticalLock();
    }

    public void setVerticalLock(boolean lock) {
        newFlowPane.setVerticalLock(lock);
    }

    public void reset() {
        this.getChildren().clear();
        newFlowPane = new NewFlowViewScene(document);
        this.getChildren().add(newFlowPane);
    }

    public void goToReadingMode() {
        newFlowPane.goToReadingMode();
    }

    public void zoom(int i) {
        if (currentView.equals("FlowView")) {
            double x = screenBounds.getWidth() / 2;
            double y = (screenBounds.getHeight() / 2) - (screenBounds.getHeight() * 0.35);
            newFlowPane.zoom(i, x, y);
        }
    }

    public void swipe(String direction) {
        if (currentView.equals("FlowView")) {
            newFlowPane.swipe(direction);
        }

    }

    public String getCurrentView() {
        return currentView;
    }
}
