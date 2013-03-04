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
public class NewFlowViewScene extends StackPane {

    private NewFlowView rb;
    private Document document;

    public NewFlowViewScene(Document document) {
        ArrayList<ArrayList<WordCloud>> wordClouds = new ArrayList<>();
        wordClouds.add(document.getWordClouds().get(0));
        for (ArrayList<WordCloud> alwc : document.getWordClouds()) {
            wordClouds.add(alwc);
        }
        this.document = new Document(document.getPages(), wordClouds);
        rb = new NewFlowView(this);
        this.build();
    }

    public void build() {
        rb.buildRibbon(document);
        this.getChildren().add(rb);
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
}
