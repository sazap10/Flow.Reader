package flowreader;

import flowreader.model.Page;
import flowreader.utils.TextFileReader;
import flowreader.view.PageView;
import flowreader.view.RibbonView;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    // Background and main scene
    public static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    private Scene scene;
    // Elements
    private StackPane mainPane; // The main pane that contains the ribbon or the word cloud
    private StackPane pane2;
    private RibbonView ribbon; // The ribbon at the center of the page
    //private WordCloudView wordCloud; // The word cloud view at the center of the page
    //private ComparisonView comparisonView; // The buckets to compare pages in the bottom
    private HBox topBtnsBar; // the button bar at the top of the screen
    private Button minBtn, closeBtn; // The buttons at the top of the page
    private Button openFileButton, wordCloudButton, diffModeBtn; // The buttons at the bottom of the page
    public static Text zoomLabel;
    private EventHandler<ScrollEvent> scrollHandler;
    private EventHandler<ZoomEvent> zoomHandler;
    boolean wordCloudToggle, diffModeToggle;
    public static Group the_Group;
public static  BorderPane borderPane;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);
        the_Group=new Group();
         borderPane = new BorderPane();
        scene = new Scene(borderPane, screenBounds.getWidth(), screenBounds.getHeight());

        mainPane = new StackPane();
pane2=new StackPane();
        zoomLabel = new Text("zoom label");
        setUpButtonBar();
        this.setButtonEvents(primaryStage);
the_Group.getChildren().addAll(mainPane,pane2);
        borderPane.setCenter(this.the_Group);
        
        borderPane.setTop(topBtnsBar);
        BorderPane.setAlignment(mainPane, Pos.CENTER_LEFT);
        borderPane.setBottom(zoomLabel);
        wordCloudToggle = diffModeToggle = false;

        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setUpButtons() {
        closeBtn = new Button("x");
        closeBtn.setId("closeBtn");

        minBtn = new Button("_");
        minBtn.setId("minBtn");

        openFileButton = new Button("Open file");
        openFileButton.setId("openFileBtn");
        wordCloudButton = new Button("WordCloud View");
        wordCloudButton.setId("wordCloudBtn");
        wordCloudButton.setDisable(false);

        diffModeBtn = new Button("Drag Mode");
        diffModeBtn.setId("diffModeBtn");
        diffModeBtn.setDisable(true);
    }

    private void setUpButtonBar() {
        this.setUpButtons();

        topBtnsBar = new HBox(10);

        HBox mainBtns = new HBox(10);
        mainBtns.getChildren().add(openFileButton);
        mainBtns.getChildren().add(wordCloudButton);
        mainBtns.getChildren().add(diffModeBtn);

        HBox winBtnBox = new HBox(10);
        winBtnBox.setAlignment(Pos.CENTER_RIGHT);
        winBtnBox.getChildren().addAll(minBtn, closeBtn);
        mainBtns.getChildren().add(winBtnBox);
        HBox.setHgrow(winBtnBox, Priority.ALWAYS);

        topBtnsBar.getChildren().addAll(mainBtns, winBtnBox);
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
                try {
                    TextFileReader fileReader = new TextFileReader();
                    mainPane.getChildren().clear();
                    // wordCloud.getChildren().clear();
                    // fileReader_WordCloud.wordObjects.clear();
                    wordCloudToggle = false;
                    wordCloudButton.setText("WordCloud View");

                    ribbon = new RibbonView(mainPane,pane2);
                    ArrayList<Page> pages;
                    PageView page = new PageView(new Rectangle(0, 0, ribbon.getPageWidth(), ribbon.getPageHeight()));

                    mainPane.getChildren().add(ribbon);
                    //comparisonView.setPageSize(ribbon.getPageWidth(), ribbon.getPageHeight());

                    fileReader.startFileChooser(primaryStage);

                    // fileReader_WordCloud.getCommonWords();
                    // returns all pages
                    pages = fileReader.readFile(page.getTextBound());
                    ribbon.buildRibbon(pages);

                    diffModeBtn.setDisable(false);

                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });

        wordCloudButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (wordCloudToggle) {
                    wordCloudToggle = false;
                    ribbon.switchToPages();
                    wordCloudButton.setText("Word Cloud View");
                } else {
                    wordCloudToggle = true;
                    ribbon.switchToWordCloud();
                    wordCloudButton.setText("Ribbon View");
                }
            }
        });

        diffModeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (diffModeToggle) {
                    diffModeToggle = false;
                    diffModeBtn.setText("Drag Mode");
                    ribbon.setRibbonEvents(true);
                    ribbon.setPageDragEvent(false);
                    //comparisonView.setDragEvents(false);
                } else {
                    diffModeToggle = true;
                    diffModeBtn.setText("Pan Mode");
                    ribbon.setRibbonEvents(false);
                    ribbon.setPageDragEvent(true);
                    //comparisonView.setDragEvents(true);
                }
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
