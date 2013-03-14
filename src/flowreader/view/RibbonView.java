/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.view.diveview.DiveViewScene;
import flowreader.view.flowview.FlowViewScene;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

/**
 *
 * @author D-Day
 */
public class RibbonView extends StackPane {

    private DiveViewScene diveViewPane;
    private FlowViewScene newFlowPane;
    private Document document;
    private String currentView = "";
    private Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private boolean split_version;

    public RibbonView() {
    }

    public RibbonView(Document document, Boolean split_version) {
        this.diveViewPane = new DiveViewScene(document);
        this.newFlowPane = new FlowViewScene(document, split_version);
        this.getChildren().add(this.diveViewPane);
        this.document = document;
        this.split_version = split_version;

    }
public boolean toggleText(){
        return newFlowPane.toggleText();
    }
 public void goDown(){
        newFlowPane.goDown();
    }
    public void goUp(){
        newFlowPane.goUp();
    }
    public boolean toggleZoomCenter() {
        return newFlowPane.toggleZoomCenter();
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
        newFlowPane = new FlowViewScene(document, split_version);
        this.getChildren().add(newFlowPane);
    }

    public void goToReadingMode() {
        newFlowPane.goToReadingMode();
    }

    public void zoom(int i) {
        if (currentView.equals("FlowView") && newFlowPane.getOtherTransitionsFinished()) {
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

    public void setCurrentView(String s) {
        currentView = s;
    }
}
