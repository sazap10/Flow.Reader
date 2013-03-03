package flowreader;

import flowreader.view.MainView;
import flowreader.view.PageView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    public static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
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
        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight(), Color.web("B8B8B8"));
        mainView = new MainView(primaryStage, scene);

        //prevent buttons overlapping credit:
        //http://stackoverflow.com/questions/9837529/how-to-solve-the-overlapping-of-the-controls-each-other-belonging-to-two-differe

        rootPane.getChildren().add(mainView);
        rootPane.getChildren().add(mainView.topBtnsBar);
        rootPane.getChildren().add(mainView.bottomBtnsBar);
        rootPane.getChildren().add(mainView.sideBtnsBar);
        
        mainView.topBtnsBar.layoutYProperty().bind(rootPane.layoutYProperty());
        mainView.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);
        
                mainView.sideBtnsBar.setLayoutX(screenBounds.getWidth()-165);
                mainView.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26*5);
        
        mainView.prefWidthProperty().bind(rootPane.widthProperty());
        mainView.prefHeightProperty().bind(rootPane.heightProperty());
        root.getChildren().add(rootPane);
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
