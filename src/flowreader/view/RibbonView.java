/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.view.diveview.DiveViewScene;
import flowreader.view.flowview.FlowViewScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The ribbon view is the main area in flowReader where you can manipulate
 * ribbon elements
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
    private boolean initialsed = false;

    public RibbonView() {
    }

    public RibbonView(FlowReader flowReader,Document document, Boolean split_version) {
            System.out.println(document.isEmpty() + "HAHAHAHAHA");
        if (document.isEmpty()) {
            //make a popup with document.getErrorMsq()
            System.out.println("making error popup");
            /*;*/
           flowReader.createErrorDialog(document);
        } else {
            this.initialsed = true;
            this.diveViewPane = new DiveViewScene(document);
            this.newFlowPane = new FlowViewScene(document, split_version);
            this.getChildren().add(this.diveViewPane);
            this.document = document;
            this.split_version = split_version;
        }

    }

    /**
     * Set the page width in the flowpane
     *
     * @param width
     */
    public void setPageWidth(int width) {
        newFlowPane.setPageWidth(width);
    }

    /**
     * Set the page height in the flowpane
     *
     * @param height
     */
    public void setPageHeight(int height) {
        newFlowPane.setPageHeight(height);
    }

    public boolean toggleWordCloud() {
        return newFlowPane.toggleWordCloud();

    }

    public boolean toggleText() {
        return newFlowPane.toggleText();
    }

    public void goDown() {
        newFlowPane.goDown();
    }

    public void goUp() {
        newFlowPane.goUp();
    }

    public boolean toggleZoomCenter() {
        return newFlowPane.toggleZoomCenter();
    }

    /**
     * Put the diveview in the ribbonview
     */
    public void switchToDiveView() {
        this.getChildren().clear();
        this.getChildren().add(this.diveViewPane);
        currentView = "DiveView";
    }

    /**
     * Put the flowview in the ribbonview
     */
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

    /**
     * Restart the document viewing from the biggest point of view (one word
     * cloud)
     */
    public void reset() {
        this.getChildren().clear();
        if (currentView.equals("FlowView")) {
            newFlowPane = new FlowViewScene(document, split_version);
            newFlowPane.setPageWidth(flowreader.FlowReader.pageWidth);
            this.getChildren().add(newFlowPane);
        } else if (currentView.equals("DiveView")) {
            this.diveViewPane = new DiveViewScene(document);
            this.getChildren().add(this.diveViewPane);
        }
    }

    /**
     * Go to the point where you can read the document page by page
     */
    public void goToReadingMode() {
        if (initialsed) {
            if (currentView.equals("FlowView")) {
                newFlowPane.goToReadingMode();
            } else if (currentView.equals("DiveView")) {
                this.diveViewPane.goToReadingMode();
            }
        }
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
