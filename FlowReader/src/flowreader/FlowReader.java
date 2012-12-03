/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import flowreader.view.*;
import flowreader.data.*;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    // Background
    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    RibbonView ribbon;
    FileReader fileReader;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);

        ribbon = new RibbonView();
        ribbon.buildRibbon();

        fileReader = new TextFileReader();

        Button openFileButton = new Button("Open file");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(ribbon.getRoot());
        borderPane.setBottom(openFileButton);
        Scene scene = new Scene(borderPane, screenBounds.getWidth(), screenBounds.getHeight());
        scene.getStylesheets().add(FlowReader.class.getResource("Background.css").toExternalForm());
        this.setOpenFileButtonEvent(openFileButton, primaryStage);
        this.setSceneEvents(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setOpenFileButtonEvent(Button button, final Stage primaryStage) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                fileReader.startFileChooser(primaryStage);
            }
        });
    }

    private void setSceneEvents(final Scene scene) {
        //handles mouse scrolling
        scene.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if (event.getDeltaY() > 0) {
                            ribbon.zoomIn();
                        } else {
                            ribbon.zoomOut();
                        }
                        event.consume();
                    }
                });

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
