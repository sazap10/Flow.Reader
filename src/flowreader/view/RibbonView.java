/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.view.diveview.DiveViewScene;
import flowreader.view.flowview.NewFlowViewScene;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public class RibbonView extends StackPane {

    private DiveViewScene diveViewPane;
    private NewFlowViewScene newFlowPane;
private Document document;
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
        this.document=document;
    }

    public void switchToDiveView() {
        this.getChildren().clear();
        this.getChildren().add(this.diveViewPane);
    }

    public void switchToFlowView() {
        this.getChildren().clear();
        this.getChildren().add(this.newFlowPane);

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
}
