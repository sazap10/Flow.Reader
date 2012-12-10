/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader;

import flowreader.core.Page;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import flowreader.data.TextFileReader;
import flowreader.view.RibbonView;
import flowreader.view.WordCloudView;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    // Background
    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    RibbonView ribbon;
    WordCloudView wordCloud;
    TextFileReader fileReader;
    private Button minBtn, closeBtn, openFileButton;
    private FlowPane flow;
    StackPane stackPane;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);

        wordCloud = new WordCloudView();
        //ribbon = new RibbonView(wordCloud);
        fileReader = new TextFileReader();

        setUpButtons();

        setUpButtonBar();

        BorderPane borderPane = new BorderPane();
        stackPane = new StackPane();
        
        borderPane.setCenter(stackPane);
        BorderPane.setAlignment(stackPane, Pos.CENTER_LEFT);
        borderPane.setBottom(openFileButton);

        BorderPane.setAlignment(flow, Pos.TOP_RIGHT);
        borderPane.setTop(flow);
        Scene scene = new Scene(borderPane, screenBounds.getWidth(), screenBounds.getHeight());
        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        this.setButtonEvents(primaryStage);
        this.setSceneEvents(scene);

        primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     *
     */
    private void setUpButtons() {
        closeBtn = new Button("x");
        closeBtn.setId("closeBtn");

        minBtn = new Button("_");
        minBtn.setId("minBtn");

        openFileButton = new Button("Open file");
    }

    private void setUpButtonBar() {
        flow = new FlowPane();
        flow.setHgap(4);
        flow.setAlignment(Pos.TOP_RIGHT);
        flow.getChildren().addAll(minBtn, closeBtn);
    }

    private void setButtonEvents(final Stage primaryStage) {
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
            }
        });

        minBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setIconified(true);
            }
        });

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	ribbon = new RibbonView(wordCloud);
            	stackPane.getChildren().add(ribbon);
                fileReader.startFileChooser(primaryStage);
                try {
                    Page page = new Page(new Rectangle(0, 0, ribbon.getPageWidth(), ribbon.getPageHeight()));
                    ArrayList<String> pages = new ArrayList<>();
                    pages = fileReader.readFile(page.getTextBound());
                    ribbon.buildRibbon(pages.size());
                    ribbon.setTexttoPages(pages);

                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });
    }

    private void setSceneEvents(final Scene scene) {
        //handles mouse scrolling
        scene.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        ribbon.zoom(event.getDeltaY(), event.getX(), event.getY(),stackPane);
                        event.consume();

                    }
                });
        scene.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                double delta = event.getZoomFactor() - 1;
                ribbon.zoom(delta, event.getX(), event.getY(),stackPane);
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
