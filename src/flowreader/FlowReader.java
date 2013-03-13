package flowreader;

import flowreader.view.MainView;
import flowreader.view.TxtPageView;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author D-Day
 */
public class FlowReader extends Application {

    public static Scene scene;
    private MainView mainView;
    private MainView mainView2;
    private Group root;
    public static Pane rootPane;
    public static Pane rootPane2;
    private SplitPane splitPane;
    public Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private Stage priStage;
    public static boolean split_toggle = false, cancelled = false;

    @Override
    public void start(Stage primaryStage) {
        this.priStage = primaryStage;
        primaryStage.setTitle("Flow Reader");
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            primaryStage.setFullScreen(false);

        } else {
            primaryStage.setFullScreen(true);

        }
        root = new Group();
        rootPane = new Pane();
        rootPane2 = new Pane();

        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight(), Color.web("B8B8B8"));

        splitPane = new SplitPane();
        splitPane.prefWidthProperty().bind(scene.widthProperty());
        splitPane.prefHeightProperty().bind(scene.heightProperty());

        mainView = new MainView(this, primaryStage, scene, false);
        mainView2 = new MainView(this, primaryStage, scene, true);
        //prevent buttons overlapping credit:
        //http://stackoverflow.com/questions/9837529/how-to-solve-the-overlapping-of-the-controls-each-other-belonging-to-two-differe

        rootPane.getChildren().add(mainView);
        rootPane.getChildren().add(mainView.topBtnsBar);
        rootPane.getChildren().add(mainView.bottomBtnsBar);
        rootPane.getChildren().add(mainView.sideBtnsBar);


        mainView.topBtnsBar.layoutYProperty().bind(rootPane.layoutYProperty());
        mainView.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);

        mainView.sideBtnsBar.setLayoutX(screenBounds.getWidth() - 165);
        mainView.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26 * 9);

        rootPane2.getChildren().add(mainView2);
        rootPane2.getChildren().add(mainView2.topBtnsBar);
        rootPane2.getChildren().add(mainView2.bottomBtnsBar);
        rootPane2.getChildren().add(mainView2.sideBtnsBar);

        mainView2.topBtnsBar.layoutYProperty().bind(rootPane2.layoutYProperty());
        mainView2.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);

        mainView2.sideBtnsBar.setLayoutX(screenBounds.getWidth() - 165);
        mainView2.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26 * 9);

        mainView.prefWidthProperty().bind(rootPane.prefWidthProperty());
        mainView.prefHeightProperty().bind(rootPane.prefHeightProperty());
        mainView.minWidthProperty().bind(rootPane.minWidthProperty());
        mainView.minHeightProperty().bind(rootPane.minHeightProperty());
        mainView.maxWidthProperty().bind(rootPane.maxWidthProperty());
        mainView.maxHeightProperty().bind(rootPane.maxHeightProperty());

        mainView2.prefWidthProperty().bind(rootPane2.prefWidthProperty());
        mainView2.prefHeightProperty().bind(rootPane2.prefHeightProperty());
        mainView2.minWidthProperty().bind(rootPane2.minWidthProperty());
        mainView2.minHeightProperty().bind(rootPane2.minHeightProperty());
        mainView2.maxWidthProperty().bind(rootPane2.maxWidthProperty());
        mainView2.maxHeightProperty().bind(rootPane2.maxHeightProperty());

        rootPane.setMaxWidth(screenBounds.getWidth());
        rootPane.setMinWidth(screenBounds.getWidth());
        rootPane.setPrefWidth(screenBounds.getWidth());

        rootPane.setMaxHeight(screenBounds.getHeight());
        rootPane.setMinHeight(screenBounds.getHeight());
        rootPane.setPrefHeight(screenBounds.getHeight());

        rootPane2.setMaxWidth(screenBounds.getWidth() / 2);
        rootPane2.setMinWidth(screenBounds.getWidth() / 2);
        rootPane2.setPrefWidth(screenBounds.getWidth() / 2);

        rootPane2.setMaxHeight(screenBounds.getHeight());
        rootPane2.setMinHeight(screenBounds.getHeight());
        rootPane2.setPrefHeight(screenBounds.getHeight());

        root.getChildren().add(rootPane);
        TxtPageView.setUpPageSize(500, 700);

        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        scene.widthProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        Double width = (Double) newValue;
                        if (split_toggle) {
                            rootPane.setMaxWidth(width / 2);
                            rootPane.setMinWidth(width / 2);
                            rootPane.setPrefWidth(width / 2);
                            rootPane2.setMaxWidth(width / 2);
                            rootPane2.setMinWidth(width / 2);
                            rootPane2.setPrefWidth(width / 2);
                        } else {
                            rootPane.setMaxWidth(width);
                            rootPane.setMinWidth(width);
                            rootPane.setPrefWidth(width);

                            mainView.sideBtnsBar.setLayoutX(width - 300);



                        }
                    }
                });
        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        scene.heightProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        Double height = (Double) newValue;



                        if (split_toggle) {

                            rootPane.setMaxHeight(height / 2);
                            rootPane.setMinHeight(height / 2);
                            rootPane.setPrefHeight(height / 2);
                            rootPane2.setMaxHeight(height / 2);
                            rootPane2.setMinHeight(height / 2);
                            rootPane2.setPrefHeight(height / 2);
                        } else {
                            rootPane.setMaxHeight(height);
                            rootPane.setMinHeight(height);
                            rootPane.setPrefHeight(height);
                            mainView.bottomBtnsBar.setLayoutY(height - 26);
                            mainView.sideBtnsBar.setLayoutY(height - 26 * 9);


                        }
                    }
                });
        primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.png").toExternalForm()));
        primaryStage.setScene(scene);
        splitPane.getItems().add(rootPane);
        splitPane.getItems().add(rootPane2);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                mainView.closeBtn.fire();
                if (cancelled) {
                    e.consume();
                }
            }
        });
        primaryStage.show();

    }

    public void showShortcuts() {


        final Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initOwner(priStage);

        dialog.initModality(Modality.WINDOW_MODAL);

        EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode().equals(event.getCode().DIGIT1)) {
                    priStage.getScene().getRoot().setEffect(null);
                    dialog.close();
                    event.consume();
                }
            }
        };

        Scene dialog_scene = new Scene(
                HBoxBuilder.create().styleClass("modal-dialog").children(
                LabelBuilder.create().text("\n\nKeyboard shortcuts:\nF: Toggle fullscreen\nB: hide/show buttons\nH: Home\nW: Zoom In\nS: Zoom Out\nA: Move Left\nD: Move Right"
                + "\nM: Matrix Theme\nN: Normal Theme\nG: Glow!\nQ: Switch View\nR: Reset\nC: Reading Mode\nE: Reset Effect\nL: Vertical Lock\nZ: Zoom Lock\n Y: Split").textFill(Color.WHITE).build(),
                ButtonBuilder.create().id("ok").text("OK").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // take action and close the dialog.
                priStage.getScene().getRoot().setEffect(null);
                dialog.close();

            }
        }).build()).build(), Color.TRANSPARENT);
        dialog_scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);

        dialog.setScene(dialog_scene);

        dialog.getScene().getStylesheets().add(FlowReader.class.getResource("modal-dialog.css").toExternalForm());
        priStage.getScene().getRoot().setEffect(new BoxBlur());
        dialog.showAndWait();

    }

    public void split() {

        if (split_toggle) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());

            split_toggle = false;
            root.getChildren().clear();
            root.getChildren().add(rootPane);


            mainView.splitSetup(false);
            mainView2.splitSetup(false);

            rootPane.setMaxWidth(screenBounds.getWidth());
            rootPane.setMinWidth(screenBounds.getWidth());
            rootPane.setPrefWidth(screenBounds.getWidth());

            mainView.topBtnsBar.layoutYProperty().bind(rootPane.layoutYProperty());
            mainView.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);

            mainView.sideBtnsBar.setLayoutX(screenBounds.getWidth() - 165);
            mainView.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26 * 9);


            mainView2.topBtnsBar.layoutYProperty().bind(rootPane2.layoutYProperty());
            mainView2.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);

            mainView2.sideBtnsBar.setLayoutX(screenBounds.getWidth() - 165);
            mainView2.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26 * 9);


        } else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(FlowReader.class.getResource("stylesheet_split.css").toExternalForm());
            scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());

            split_toggle = true;
            root.getChildren().clear();
            splitPane.getItems().clear();
            splitPane.getItems().add(rootPane);
            splitPane.getItems().add(rootPane2);
            root.getChildren().add(splitPane);


            mainView.splitSetup(true);
            mainView2.splitSetup(true);




            rootPane.setMaxWidth(screenBounds.getWidth() / 2);
            rootPane.setMinWidth(screenBounds.getWidth() / 2);
            rootPane.setPrefWidth(screenBounds.getWidth() / 2);


            mainView.topBtnsBar.layoutYProperty().bind(rootPane.layoutYProperty());
            mainView.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);

            mainView.sideBtnsBar.setLayoutX(screenBounds.getWidth() / 2 - 165);
            mainView.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26 * 9);


            mainView2.topBtnsBar.layoutYProperty().bind(rootPane2.layoutYProperty());
            mainView2.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);

            mainView2.sideBtnsBar.setLayoutX(screenBounds.getWidth() / 2 - 165);
            mainView2.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26 * 9);


        }

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
