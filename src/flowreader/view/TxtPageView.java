/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Page;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class TxtPageView extends PageView {

    private Rectangle textBound;
    private Text pageText;
    public static double textBoundWidth;
    public static double textBoundHeight;

    public TxtPageView(double x, double y, String text) {
        super(x, y);

        textBound = new Rectangle(textBoundWidth, textBoundHeight);
        textBound.setFill(Color.TRANSPARENT);
        pageText = new Text();
        pageText.setId("page_text");

        pageText.setX(pageBoundary.getX() + ((pageBoundary.getWidth() - textBound.getWidth()) * 0.5));
        pageText.setY(pageBoundary.getY() + ((pageBoundary.getHeight() - textBound.getHeight()) * 0.5));
        this.getChildren().add(pageText);
        pageText.setText(text);
    }

    public TxtPageView(Page page) {
        super(0, 0);
        textBound = new Rectangle(textBoundWidth, textBoundHeight);
        textBound.setFill(Color.TRANSPARENT);
        pageText = new Text();
        pageText.setId("page_text");

        pageText.setX(pageBoundary.getX() + ((pageBoundary.getWidth() - textBound.getWidth()) * 0.5));
        pageText.setY(pageBoundary.getY() + ((pageBoundary.getHeight() - textBound.getHeight()) * 0.5));
        this.getChildren().add(pageText);
        pageText.setText(page.getText());
    }

    public void toggleTextVisible(boolean visible) {
        pageText.setVisible(visible);
    }

    public static void setUpPageSize(double width, double height) {
        PageView.setUpPageSize(width, height);
        TxtPageView.textBoundWidth = width * 0.8;
        TxtPageView.textBoundHeight = height * 0.8;
    }

    public void setText(String text) {
        pageText.setText(text);
    }

    public String getText() {
        return pageText.getText();

    }

    public double getTextBoundWidth() {
        return textBoundWidth;
    }

    public double gettextBoundHeight() {
        return textBoundHeight;
    }

    public Rectangle getTextBound() {
        return textBound;
    }

    @Override
    public void setPageWidth(double width) {
        pageBoundary.setWidth(width);
        textBound.setWidth(width * 0.8);

    }

    @Override
    public void setPageHeight(double height) {
        pageBoundary.setHeight(height);
        textBound.setHeight(height * 0.8);
    }
}
