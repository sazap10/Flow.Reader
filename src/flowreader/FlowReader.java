package flowreader;

import flowreader.view.MainView;
import flowreader.view.PageView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    public static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    public static Scene scene;
    private MainView mainView;
private Group root;
private Pane rootPane;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);
        root = new Group();
        rootPane = new Pane();
        mainView = new MainView(primaryStage);
               //rootPane.getChildren().add(mainView.topBtnsBar);
                //rootPane.getChildren().add(mainView.bottomBtnsBar);

        rootPane.getChildren().add(mainView);
 
        root.getChildren().addAll(rootPane);
        //root.setAutoSizeChildren(true);
        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        PageView.setUpPageSize(500, 700);

        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
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
