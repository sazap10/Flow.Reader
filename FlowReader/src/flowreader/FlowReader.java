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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import flowreader.data.TextFileReader;
import flowreader.view.RibbonView;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    // Background
    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    RibbonView ribbon;
    TextFileReader fileReader;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);

        ribbon = new RibbonView();

        fileReader = new TextFileReader();

        Button openFileButton = new Button("Open file");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(ribbon.getRoot());
        borderPane.setBottom(openFileButton);
        Button closeBtn = new Button("x");
        closeBtn.setId("closeBtn");
        Button minBtn = new Button("_");
		minBtn.setId("minBtn");
		FlowPane flow = new FlowPane();
		flow.setHgap(4);
		flow.setAlignment(Pos.TOP_RIGHT);
		flow.getChildren().addAll(minBtn,closeBtn);
		BorderPane.setAlignment(flow, Pos.TOP_RIGHT);
		borderPane.setTop(flow);
        Scene scene = new Scene(borderPane, screenBounds.getWidth(), screenBounds.getHeight());
        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        this.setOpenFileButtonEvent(openFileButton, primaryStage);
        this.setSceneEvents(scene);
        this.setCloseBtnEvent(closeBtn, primaryStage);
        this.setMinBtnEvent(minBtn, primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void setCloseBtnEvent(Button button, final Stage primaryStage){
    	button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
            }
        });
    }
    
    private void setMinBtnEvent(Button button, final Stage primaryStage){
    	button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setIconified(true);
            }
        });
    }

    private void setOpenFileButtonEvent(Button button, final Stage primaryStage) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
                    	ribbon.zoom(event.getDeltaY());
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
