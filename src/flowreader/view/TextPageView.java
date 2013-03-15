/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Page;
import flowreader.utils.Parameters;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Visual representation of a page of a text file
 * @author D-Day
 */
public class TextPageView extends PageView {
    private Text pageText;

    public TextPageView(Page page) {
        super(0, 0);
        Rectangle textBound = new Rectangle(Parameters.textBoundWidth, Parameters.textBoundHeight);
        textBound.setFill(Color.TRANSPARENT);
        pageText = new Text();
        pageText.setId("page_text");

        pageText.setX(pageBoundary.getX() + ((pageBoundary.getWidth() - textBound.getWidth()) * 0.5));
        pageText.setY(pageBoundary.getY() + ((pageBoundary.getHeight() - textBound.getHeight()) * 0.5));
        this.getChildren().add(pageText);
        pageText.setText(page.getText());
    }

    /**
     * if visible displays the text in the page, else don't display the text
     * @param visible 
     */
    public void toggleTextVisible(boolean visible) {
        pageText.setVisible(visible);
    }
}
