/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.flowview;

import flowreader.model.Document;
import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public class FlowViewScene extends StackPane {

    private FlowView rb;
    private Document document;
    private boolean split_version;

    public FlowViewScene(Document document, Boolean split_version) {
        this.split_version = split_version;

        ArrayList<ArrayList<WordCloud>> wordClouds = new ArrayList<ArrayList<WordCloud>>();
        wordClouds.add(document.getWordClouds().get(0));
        for (ArrayList<WordCloud> alwc : document.getWordClouds()) {
            wordClouds.add(alwc);
        }
        this.document = new Document(document.getPages(), wordClouds);
        //rb = new FlowView(this, split_version);
        //this.build();
    }

    public void build() {
        rb.buildRibbon(document);
        this.getChildren().add(rb);
    }

    public void setPageWidth(int width) {
        rb = new FlowView(this, split_version, width, 700);
        this.getChildren().clear();
        build();
    }

    public void setPageHeight(int height) {
        rb = new FlowView(this, split_version, 500, height);
        this.getChildren().clear();
        build();
    }

    public boolean toggleWordCloud() {
        return rb.toggleWordCloud();
    }

    public boolean toggleText() {
        return rb.toggleText();
    }

    public void goUp() {
        rb.goUp();
    }

    public void goDown() {
        rb.goDown();
    }

    public boolean toggleZoomCenter() {
        return rb.toggleZoomCenter();
    }

    public boolean getZoomLock() {
        return rb.getZoomLock();
    }

    public void setZoomLock(boolean lock) {
        rb.setZoomLock(lock);
    }

    public boolean getVerticalLock() {
        return rb.getVerticalLock();
    }

    public void setVerticalLock(boolean lock) {
        rb.setVerticalLock(lock);
    }

    public void goToReadingMode() {
        rb.goToReadingMode();
    }

    public void zoom(int i, double x, double y) {
        rb.zoom(i, x, y);
    }

    public void swipe(String direction) {
        if (direction.equals("left")) {
            rb.setXCoord(-50);
        } else if (direction.equals("right")) {
            rb.setXCoord(50);

        }

    }

    public boolean getOtherTransitionsFinished() {
        return rb.getOtherTransitionsFinished();
    }
}
