/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.core;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class Page {

    private Rectangle pageBoundary, textBound;
    private Text pageText;
    private Group page;
    private double textBoundWidth;
    private double textBoundHeight;
    private double textScale;

    public Page(Rectangle boundary) {
        pageBoundary = boundary;
        pageBoundary.setFill(Color.FLORALWHITE);
        textBoundWidth = boundary.getWidth() * 0.8;
        textBoundHeight = boundary.getHeight() * 0.8;
        textBound = new Rectangle(textBoundWidth, textBoundHeight);
        textBound.setFill(Color.TRANSPARENT);
        //textBound.setStroke(Color.BLACK);
        pageText = new Text();
        pageText.setX(pageBoundary.getX() + ((pageBoundary.getWidth() - textBound.getWidth()) * 0.5));
        pageText.setY(pageBoundary.getY() + ((pageBoundary.getHeight() - textBound.getHeight()) * 0.5));
        page = new Group();
        page.getChildren().addAll(pageBoundary, pageText);
       // setTextBoundXEvents();
        //setTextBoundYEvents();
        textScale = 1;
    }
/*
    public void setTextBoundXEvents() {
        textBound.xProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue ov, Object oldValue, Object newValue) {
                        try {
                            pageText.setX((double) newValue);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

    }

    public void setTextBoundYEvents() {
        textBound.yProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue ov, Object oldValue, Object newValue) {
                        try {
                            pageText.setY((double) newValue);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

    }
*/
    public void setText(String text) {
        pageText.setText(text);
    }

    public double getPageWidth() {
        return pageBoundary.getWidth();
    }

    public double getPageHeight() {
        return pageBoundary.getHeight();
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

    public void setPageWidth(double width) {
        pageBoundary.setWidth(width);
        textBound.setWidth(width * 0.8);

    }

    public void setPageHeight(double height) {
        pageBoundary.setHeight(height);
        textBound.setHeight(height * 0.8);

    }

    public Group getPage() {
        return page;
    }

    public void setX(double x) {
        pageBoundary.setX(x);
        textBound.setX(x + (pageBoundary.getWidth() * 0.1));
        textBound.setY(pageBoundary.getY() + pageBoundary.getHeight() * 0.1);
        System.out.println("x: " + x + "pageBoundary Width: " + pageBoundary.getWidth());

    }

    public void setTextScale(boolean in) {
        if (in) {
            textScale = textScale * (getPageWidth() / (getPageWidth() - 10));
            pageText.setScaleX(textScale);
            pageText.setScaleY(textScale);

        } else {
            textScale = textScale * (getPageWidth() / (getPageWidth() + 10));
            pageText.setScaleX(textScale);
            pageText.setScaleY(textScale);

        }
    }

    public double getX() {
        return pageBoundary.getX();
    }
}
