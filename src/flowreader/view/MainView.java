/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.utils.DocumentCreationTask;
import flowreader.utils.TextFileReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author D-Day
 */
public class MainView extends BorderPane {

    public HBox topBtnsBar; // the button bar at the top of the screen
    public HBox bottomBtnsBar; // the button bar at the bottom of the screen
    public VBox sideBtnsBar;
    private Button minBtn, closeBtn; // The buttons at the top of the page
    private Button homeButton, openFileButton, flowViewSceneButton, diveViewSceneButton, normalThemeButton, matrixThemeButton, zoomLockButton, resetButton, verticalLockButton, readingModeButton, GlowButton, ResetEffectButton; // The buttons at the bottom of the page
    private RibbonView ribbon; // The ribbon at the center of the page
    private ProgressIndicator pi;
    private TextFileReader fileReader;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private EventHandler<KeyEvent> keyHandler;
    private StackPane home;

    public MainView(Stage primaryStage, Scene scene) {
        this.setId("mainview");
        this.setUpEvents();
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage, scene);
        //this.setTop(topBtnsBar);
        //this.setBottom(bottomBtnsBar);

        ribbon = new RibbonView();
        this.pi = new ProgressIndicator(0.0);

        pi.setStyle(" -fx-progress-color: #005888;");
        // changing size without css
        pi.setPrefSize(100, 100);
        pi.setMinSize(100, 100);
        pi.setMaxSize(100, 100);
        this.buildHomeView();
        this.setCenter(home);

        //this.setCenter(this.pi);

    }

    public void buildHomeView() {
        home = new StackPane();

        Bloom bloom = new Bloom();
        bloom.setThreshold(0.1);

        Rectangle rect = new Rectangle();

        rect.setX(0);
        rect.setY(0);
        rect.setWidth(screenBounds.getWidth());
        rect.setHeight(screenBounds.getHeight());
        rect.setFill(Color.web("B8B8B8"));

        Text text = new Text();
        text.setText("Welcome to FlowReader. Enjoy!\n\nKeyboard shortcuts:\nW:Zoom In\nS:Zoom Out\nA: Move Left\nD:Move Right"
                + "\nM: Matrix Theme\nN: Normal Theme\nG: Glow!\nQ:Switch View\nF: Reset\nC: Reading Mode\nR: Reset Effect\nL: Vertical Lock\nZ: Zoom Lock");


        text.setFill(Color.ALICEBLUE);
        text.setFont(Font.font(null, FontWeight.BOLD, 20));
        text.setX(25);
        text.setY(65);
        text.setEffect(bloom);

        home.getChildren().addAll(rect, text);


    }

    private void setUpEvents() {
        keyHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //System.out.println(event.getCode().toString());
                switch (event.getCode().toString()) {

                    case "w":
                    case "W":
                        ribbon.zoom(1);
                        break;

                    case "s":
                    case "S":
                        ribbon.zoom(-1);
                        break;

                    case "a":
                    case "A":
                        ribbon.swipe("left");
                        break;

                    case "d":
                    case "D":
                        ribbon.swipe("right");
                        break;

                    case "M":
                        matrixThemeButton.fire();
                        break;

                    case "N":
                        normalThemeButton.fire();
                        break;

                    case "G":
                        GlowButton.fire();
                        break;
                    case "R":
                        ResetEffectButton.fire();

                        break;
                    case "F":
                        resetButton.fire();

                        break;
                    case "L":
                        verticalLockButton.fire();
                        break;

                    case "Z":
                        zoomLockButton.fire();
                        break;
                        
                    case "C":
                        readingModeButton.fire();
                        break;
                    case "Q":
                        if (!homeButton.disableProperty().get()) {
                            if (ribbon.getCurrentView().equals("")) {
                                diveViewSceneButton.fire();
                                break;
                            } else if (ribbon.getCurrentView().equals("DiveView")) {
                                flowViewSceneButton.fire();

                            } else if (ribbon.getCurrentView().equals("FlowView")) {
                                homeButton.fire();
                            } else if (ribbon.getCurrentView().equals("HomeView")) {
                                diveViewSceneButton.fire();
                            }
                        }
                }
//event.consume();
            }
        };
    }

    private void setUpButtons() {
        closeBtn = new Button("x");
        closeBtn.setId("closeBtn");
        closeBtn.setCancelButton(true);

        minBtn = new Button("_");
        minBtn.setId("minBtn");

        openFileButton = new Button("Open file");
        openFileButton.setId("topbarbutton");
        openFileButton.setDefaultButton(true);

        homeButton = new Button("Home");
        homeButton.setId("topbarbutton");
        homeButton.setDisable(true);

        flowViewSceneButton = new Button("Flowing");
        flowViewSceneButton.setId("topbarbutton");
        flowViewSceneButton.setDisable(true);

        diveViewSceneButton = new Button("Diving");
        diveViewSceneButton.setId("topbarbutton");
        diveViewSceneButton.setDisable(true);

        normalThemeButton = new Button("Original Theme");
        normalThemeButton.setId("topbarbutton");
        normalThemeButton.setDisable(true);

        matrixThemeButton = new Button("Matrix Theme");
        matrixThemeButton.setId("topbarbutton");
        matrixThemeButton.setDisable(true);

        zoomLockButton = new Button("Zoom Lock: Off");
        zoomLockButton.setId("topbarbutton");
        zoomLockButton.setDisable(true);

        resetButton = new Button("Reset");
        resetButton.setId("topbarbutton");
        resetButton.setDisable(true);

        verticalLockButton = new Button("Vertical Lock: On");
        verticalLockButton.setId("topbarbutton");
        verticalLockButton.setDisable(true);

        readingModeButton = new Button("Reading Mode");
        readingModeButton.setId("topbarbutton");
        readingModeButton.setDisable(true);

        GlowButton = new Button("Glow!");
        GlowButton.setId("topbarbutton");
        GlowButton.setDisable(true);

        ResetEffectButton = new Button("Reset Effects");
        ResetEffectButton.setId("topbarbutton");
        ResetEffectButton.setDisable(true);

    }

    private void setUpButtonBar() {
        this.setUpButtons();

        topBtnsBar = new HBox(10);
        bottomBtnsBar = new HBox(10);
        sideBtnsBar = new VBox(10);
        topBtnsBar.setPrefWidth(screenBounds.getWidth());
        topBtnsBar.setMaxWidth(screenBounds.getWidth());
        topBtnsBar.setMinWidth(screenBounds.getWidth());

        bottomBtnsBar.setPrefWidth(screenBounds.getWidth());
        bottomBtnsBar.setMaxWidth(screenBounds.getWidth());
        bottomBtnsBar.setMinWidth(screenBounds.getWidth());


        HBox mainBtns = new HBox(10);
        HBox effectBtns = new HBox(10);
        VBox configBtns = new VBox(10);
        mainBtns.getChildren().addAll(openFileButton, homeButton, diveViewSceneButton, flowViewSceneButton);
        effectBtns.getChildren().addAll(normalThemeButton, matrixThemeButton, GlowButton, ResetEffectButton);
        configBtns.getChildren().addAll(zoomLockButton, verticalLockButton, readingModeButton, resetButton);
        HBox winBtnBox = new HBox(10);
        winBtnBox.setAlignment(Pos.CENTER_RIGHT);
        winBtnBox.getChildren().addAll(minBtn, closeBtn);
        mainBtns.getChildren().add(winBtnBox);
        HBox.setHgrow(winBtnBox, Priority.ALWAYS);

        topBtnsBar.getChildren().addAll(mainBtns, winBtnBox);
        bottomBtnsBar.getChildren().addAll(effectBtns);
        sideBtnsBar.getChildren().add(configBtns);
        configBtns.setAlignment(Pos.CENTER_RIGHT);
    }

    private void setButtonEvents(final Stage primaryStage, final Scene scene) {

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                //credit: https://gist.github.com/jewelsea/2992072

                final Stage dialog = new Stage(StageStyle.TRANSPARENT);
                dialog.initOwner(primaryStage);

                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.setScene(
                        new Scene(
                        HBoxBuilder.create().styleClass("modal-dialog").children(
                        LabelBuilder.create().text("Are you sure you want to quit FlowReader?").build(),
                        ButtonBuilder.create().text("OK").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // take action and close the dialog.
                        System.exit(0);
                    }
                }).build(),
                        ButtonBuilder.create().text("Cancel").cancelButton(true).onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // abort action and close the dialog.
                        dialog.close();
                        primaryStage.setFullScreen(true);
                        primaryStage.getScene().getRoot().setEffect(null);
                    }
                }).build()).build(), Color.TRANSPARENT));
                dialog
                        .getScene().getStylesheets().add(FlowReader.class
                        .getResource("modal-dialog.css").toExternalForm());
                primaryStage.getScene()
                        .getRoot().setEffect(new BoxBlur());
                dialog.showAndWait();
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
                    pi = new ProgressIndicator(0.0);
                    pi.setStyle(" -fx-progress-color: #005888;");
                    // changing size without css
                    pi.setPrefSize(100, 100);
                    pi.setMinSize(100, 100);
                    pi.setMaxSize(100, 100);
                    setCenter(pi);
                    flowViewSceneButton.setDisable(true);
                    diveViewSceneButton.setDisable(true);
                    homeButton.setDisable(true);
                    normalThemeButton.setDisable(true);
                    matrixThemeButton.setDisable(true);
                    zoomLockButton.setDisable(true);
                    resetButton.setDisable(true);
                    verticalLockButton.setDisable(true);
                    readingModeButton.setDisable(true);
                    GlowButton.setDisable(true);
                    ResetEffectButton.setDisable(true);
                    fileReader = new TextFileReader(MainView.this, pi);
                    DocumentCreationTask dct = new DocumentCreationTask(pi, fileReader, MainView.this);
                    fileReader.startFileChooser(primaryStage);

                    pi.progressProperty().bind(fileReader.progressProperty());
                    Thread t = new Thread(fileReader);
                    t.start();
                    Thread t2 = new Thread(dct);
                    t2.start();

                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //MainView.this.getChildren().clear();
                MainView.this.setCenter(home);
                ribbon.setCurrentView("HomeView");

            }
        });
        diveViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //MainView.this.getChildren().clear();

                MainView.this.setCenter(ribbon);
                ribbon.switchToDiveView();
            }
        });

        flowViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //MainView.this.getChildren().clear();

                MainView.this.setCenter(ribbon);
                ribbon.switchToFlowView();
            }
        });

        normalThemeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FlowReader.scene.getStylesheets().clear();

                FlowReader.scene.getStylesheets().add(FlowReader.class
                        .getResource("stylesheet.css").toExternalForm());
                ribbon.setEffect(
                        null);
                scene.setFill(Color.web("B8B8B8"));

            }
        });

        matrixThemeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FlowReader.scene.getStylesheets().clear();
                FlowReader.scene.getStylesheets().add(FlowReader.class
                        .getResource("stylesheet_matrix.css").toExternalForm());

                scene.setFill(Color.web("000000"));
            }
        });


        zoomLockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (ribbon.getZoomLock()) {
                    ribbon.setZoomLock(false);
                    zoomLockButton.setText("Zoom Lock: Off");
                } else {
                    ribbon.setZoomLock(true);
                    zoomLockButton.setText("Zoom Lock: On");

                }

            }
        });
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.reset();

            }
        });
        verticalLockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (ribbon.getVerticalLock()) {
                    ribbon.setVerticalLock(false);
                    verticalLockButton.setText("Vertical Lock: Off");
                } else {
                    ribbon.setVerticalLock(true);
                    verticalLockButton.setText("Vertical Lock: On");

                }

            }
        });

        readingModeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.goToReadingMode();

            }
        });

        GlowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.setEffect(new Glow(0.8));

            }
        });

        ResetEffectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.setEffect(null);

            }
        });
    }

    public void docOpenned(Document doc, RibbonView ribbon) {
        this.ribbon = ribbon;
        BorderPane.setAlignment(ribbon, Pos.CENTER_LEFT);
        this.ribbon.toBack();
        homeButton.setDisable(false);
        flowViewSceneButton.setDisable(false);
        diveViewSceneButton.setDisable(false);
        normalThemeButton.setDisable(false);
        matrixThemeButton.setDisable(false);
        zoomLockButton.setDisable(false);
        resetButton.setDisable(false);
        verticalLockButton.setDisable(false);
        readingModeButton.setDisable(false);
        GlowButton.setDisable(false);
        ResetEffectButton.setDisable(false);
    }
}
