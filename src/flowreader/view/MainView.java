/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.model.TextDocument;
import flowreader.utils.DocumentCreationTask;
import flowreader.utils.FileReader;
import flowreader.utils.PDFFileReader;
import flowreader.utils.Reader;
import flowreader.utils.TextFileReader;
import java.util.List;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
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
import javafx.scene.text.TextAlignment;
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
    public Button minBtn, closeBtn; // The buttons at the top of the page
    private Button homeButton, openFileButton, flowViewSceneButton, diveViewSceneButton, normalThemeButton, matrixThemeButton, zoomLockButton, resetButton, verticalLockButton, readingModeButton, GlowButton, ResetEffectButton, fullScreenButton, splitButton, zoomAtMouseButton; // The buttons at the bottom of the page
    private RibbonView ribbon; // The ribbon at the center of the page
    private ProgressIndicator pi;
    private Reader fileReader;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private EventHandler<KeyEvent> keyHandler;
    private StackPane home;
    VBox introBox;
    private boolean split_version;
    private boolean toggle_buttons = false;

    public MainView(FlowReader fr, Stage primaryStage, Scene scene, Boolean split_version) {
        this.setId("mainview");
        this.split_version = split_version;
        this.setUpEvents(fr);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage, scene, fr);
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

    public void splitSetup(boolean split) {
        if (split) {
            topBtnsBar.setPrefWidth(screenBounds.getWidth() / 2);
            topBtnsBar.setMaxWidth(screenBounds.getWidth() / 2);
            topBtnsBar.setMinWidth(screenBounds.getWidth() / 2);

            bottomBtnsBar.setPrefWidth(screenBounds.getWidth() / 2);
            bottomBtnsBar.setMaxWidth(screenBounds.getWidth() / 2);
            bottomBtnsBar.setMinWidth(screenBounds.getWidth() / 2);
        } else {
            topBtnsBar.setPrefWidth(screenBounds.getWidth());
            topBtnsBar.setMaxWidth(screenBounds.getWidth());
            topBtnsBar.setMinWidth(screenBounds.getWidth());

            bottomBtnsBar.setPrefWidth(screenBounds.getWidth());
            bottomBtnsBar.setMaxWidth(screenBounds.getWidth());
            bottomBtnsBar.setMinWidth(screenBounds.getWidth());


        }

    }

    public void buildHomeView() {
        home = new StackPane();
        Group g = new Group();
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.1);

        Rectangle rect = new Rectangle();

        rect.widthProperty().bind(home.widthProperty());
        rect.heightProperty().bind(home.heightProperty());
        rect.setFill(Color.DARKSLATEBLUE);
        Text text = new Text();
        text.setText("Welcome to FlowReader.\n Enjoy! \n\nPress number 1 to see keyboard shortcuts.\n\nNote: In Mac OS X, dialogs do not appear in full screen mode. \n(Use mac's version of full screen instead seems to overcome this problem.) \n --The button at top right corner");

        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.ALICEBLUE);
        text.setFont(Font.font(null, FontWeight.BOLD, 20));
        //text.layoutXProperty().bind(home.widthProperty().divide(2).subtract(text.getBoundsInLocal().getWidth()/2));
        //text.layoutYProperty().bind(home.heightProperty().divide(2).subtract(text.getBoundsInLocal().getHeight()/2));
        text.setEffect(bloom);

        //    Group r = new Group() {};
//Scene scene = new Scene(r, 300, 250, Color.WHITE);
        Random rand = new Random(System.currentTimeMillis());
        home.getChildren().addAll(rect);
        List<String> fontList = Font.getFontNames();

        for (int i = 0; i < 150; i++) {
            int x = rand.nextInt((int) screenBounds.getWidth());
            int y = rand.nextInt((int) screenBounds.getHeight());
            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);
            int font = rand.nextInt(30);
            Text text2 = new Text(x, y, "FlowReader");
            //int rot = rand.nextInt(360);
            text2.setFill(Color.rgb(red, green, blue, .99));
            //text2.setRotate(rot);
            int randfont = rand.nextInt(fontList.size());
            text2.setFont(new Font(fontList.get(randfont), font));
//r.getChildren().add(text2);



            g.getChildren().add(text2);
        }
        introBox = new VBox(10);
        introBox.getChildren().add(pi);
        introBox.getChildren().add(text);
        home.getChildren().add(g);
        home.getChildren().add(introBox);
//introBox.autosize();
        introBox.setAlignment(Pos.CENTER);
    }

    private void setUpEvents(final FlowReader fr) {
        keyHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DIGIT1:
                        if (!split_version) {
                            fr.showShortcuts();
                        }
                        break;

                    case F:
                        fullScreenButton.fire();
                        break;
                    case B:
                        if (toggle_buttons) {
                            topBtnsBar.setVisible(true);
                            sideBtnsBar.setVisible(true);
                            bottomBtnsBar.setVisible(true);
                            toggle_buttons = false;
                        } else {
                            topBtnsBar.setVisible(false);
                            sideBtnsBar.setVisible(false);
                            bottomBtnsBar.setVisible(false);
                            toggle_buttons = true;
                        }
                        break;
                    case W:
                    case UP:
                    case KP_UP:
                        ribbon.zoom(1);
                        break;

                    case S:
                    case DOWN:
                    case KP_DOWN:
                        ribbon.zoom(-1);
                        break;

                    case A:
                    case LEFT:
                    case KP_LEFT:
                        ribbon.swipe("left");
                        break;

                    case D:
                    case RIGHT:
                    case KP_RIGHT:
                        ribbon.swipe("right");
                        break;

                    case M:
                        matrixThemeButton.fire();
                        break;

                    case N:
                        normalThemeButton.fire();
                        break;

                    case G:
                        GlowButton.fire();
                        break;
                    case R:
                        resetButton.fire();


                        break;

                    case Y:
                        if(!split_version){
                        splitButton.fire();
                        }
                        break;

                    case E:
                        ResetEffectButton.fire();

                        break;
                    case L:
                        verticalLockButton.fire();
                        break;

                    case Z:
                        zoomLockButton.fire();
                        break;
                    case H:
                        homeButton.fire();
                        break;
                    case C:
                        readingModeButton.fire();
                        break;
                    case Q:
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
                event.consume();
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
        Tooltip openFileT = new Tooltip("Press this button to switch to Home");
        openFileT.getStyleClass().add("Tooltip");
        openFileButton.setTooltip(openFileT);

        homeButton = new Button("Home");
        homeButton.setId("topbarbutton");
        homeButton.setDisable(true);
        Tooltip homeT = new Tooltip("Switch to Home");
        homeT.getStyleClass().add("Tooltip");
        homeButton.setTooltip(homeT);

        flowViewSceneButton = new Button("Flowing");
        flowViewSceneButton.setId("topbarbutton");
        flowViewSceneButton.setDisable(true);
        Tooltip flowViewSceneT = new Tooltip("Switch to Flow View");
        flowViewSceneT.getStyleClass().add("Tooltip");
        flowViewSceneButton.setTooltip(flowViewSceneT);

        diveViewSceneButton = new Button("Diving");
        diveViewSceneButton.setId("topbarbutton");
        diveViewSceneButton.setDisable(true);
        Tooltip diveViewSceneT = new Tooltip("Switch to Dive View");
        diveViewSceneT.getStyleClass().add("Tooltip");
        diveViewSceneButton.setTooltip(diveViewSceneT);

        normalThemeButton = new Button("Original Theme");
        normalThemeButton.setId("topbarbutton");
        normalThemeButton.setDisable(true);
        Tooltip normalThemeT = new Tooltip("Change theme style to the orginal Flow Reader's great theme");
        normalThemeT.getStyleClass().add("Tooltip");
        normalThemeButton.setTooltip(normalThemeT);

        matrixThemeButton = new Button("Matrix Theme");
        matrixThemeButton.setId("topbarbutton");
        matrixThemeButton.setDisable(true);
        Tooltip matrixThemeT = new Tooltip("Change theme style to the awesome Matrix theme");
        matrixThemeT.getStyleClass().add("Tooltip");
        matrixThemeButton.setTooltip(matrixThemeT);

        zoomLockButton = new Button("Zoom Lock: Off");
        zoomLockButton.setId("topbarbutton");
        zoomLockButton.setDisable(true);
        Tooltip zoomLockT = new Tooltip("Toggle waiting for next word cloud level transition");
        zoomLockT.getStyleClass().add("Tooltip");
        zoomLockButton.setTooltip(zoomLockT);

        resetButton = new Button("Reset");
        resetButton.setId("topbarbutton");
        resetButton.setDisable(true);
        Tooltip resetT = new Tooltip("Reset: restart FlowReader with the current book");
        resetT.getStyleClass().add("Tooltip");
        resetButton.setTooltip(resetT);

        verticalLockButton = new Button("Vertical Lock: Off");
        verticalLockButton.setId("topbarbutton");
        verticalLockButton.setDisable(true);
        Tooltip verticalLockT = new Tooltip("Toggle enabling/disabling moving the ribbon vertically");
        verticalLockT.getStyleClass().add("Tooltip");
        verticalLockButton.setTooltip(verticalLockT);

        readingModeButton = new Button("Reading Mode");
        readingModeButton.setId("topbarbutton");
        readingModeButton.setDisable(true);
        Tooltip readingModeT = new Tooltip("Zoom in to the beginning");
        readingModeT.getStyleClass().add("Tooltip");
        readingModeButton.setTooltip(readingModeT);

        GlowButton = new Button("Glow!");
        GlowButton.setId("topbarbutton");
        GlowButton.setDisable(true);
        Tooltip glowT = new Tooltip("Add the Glow effect!");
        glowT.getStyleClass().add("Tooltip");
        GlowButton.setTooltip(glowT);

        ResetEffectButton = new Button("Reset Effects");
        ResetEffectButton.setId("topbarbutton");
        ResetEffectButton.setDisable(true);
        Tooltip ResetEffectT = new Tooltip("Remove any effect previously enabled");
        ResetEffectT.getStyleClass().add("Tooltip");
        ResetEffectButton.setTooltip(ResetEffectT);

        fullScreenButton = new Button("FullScreen");
        fullScreenButton.setId("topbarbutton");
        fullScreenButton.setDisable(false);
        Tooltip fullScreenT = new Tooltip("Toggle full screen");
        fullScreenT.getStyleClass().add("Tooltip");
        fullScreenButton.setTooltip(fullScreenT);

        splitButton = new Button("Split!");
        splitButton.setId("topbarbutton");
        splitButton.setDisable(false);
        Tooltip splitT = new Tooltip("Create another instance of flowreader -- FlowReader x 2!");
        splitT.getStyleClass().add("Tooltip");
        splitButton.setTooltip(splitT);

        
        zoomAtMouseButton= new Button("Zoom method: default center");
        zoomAtMouseButton.setId("topbarbutton");
        zoomAtMouseButton.setDisable(true);
        Tooltip zoomAtMouseT = new Tooltip("Toggle between centering the zoom position at default screen location or at cursor position");
        zoomAtMouseT.getStyleClass().add("Tooltip");
        zoomAtMouseButton.setTooltip(zoomAtMouseT);

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
        if (split_version) {
            effectBtns.getChildren().addAll(GlowButton, ResetEffectButton);
            configBtns.getChildren().addAll(zoomLockButton, verticalLockButton,zoomAtMouseButton, readingModeButton, splitButton, resetButton);

        } else {
            effectBtns.getChildren().addAll(normalThemeButton, matrixThemeButton, GlowButton, ResetEffectButton);
            configBtns.getChildren().addAll(fullScreenButton, zoomLockButton, verticalLockButton, zoomAtMouseButton,readingModeButton, splitButton, resetButton);

        }


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

    private void setButtonEvents(final Stage primaryStage, final Scene scene, final FlowReader fr) {

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
                        LabelBuilder.create().text("Are you sure you want to quit FlowReader?").textFill(Color.WHITE).build(),
                        ButtonBuilder.create().id("ok").text("OK").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // take action and close the dialog.
                        System.exit(0);
                    }
                }).build(),
                        ButtonBuilder.create().id("cancel").text("Cancel").cancelButton(true).onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // abort action and close the dialog.
                        dialog.close();
                                fr.cancelled=true;
                        primaryStage.getScene().getRoot().setEffect(null);
                    }
                }).build()).build(), Color.TRANSPARENT));
                dialog.getScene().getStylesheets().add(FlowReader.class.getResource("modal-dialog.css").toExternalForm());
                primaryStage.getScene().getRoot().setEffect(new BoxBlur());
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
                    homeButton.fire();
                    pi = new ProgressIndicator(0.0);
                    pi.setStyle(" -fx-progress-color: #005888;");
                    // changing size without css
                    pi.setPrefSize(100, 100);
                    pi.setMinSize(100, 100);
                    pi.setMaxSize(100, 100);
                    introBox.getChildren().set(0, pi);

                    //setCenter(pi);
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
zoomAtMouseButton.setDisable(true);
                    fileReader = new PDFFileReader();
                    DocumentCreationTask dct = new DocumentCreationTask(pi, fileReader, MainView.this, split_version);
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
                if (FlowReader.split_toggle) {
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet_split.css").toExternalForm());
                } else {
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
                    scene.setFill(Color.web("B8B8B8"));
                }


            }
        });

        matrixThemeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                FlowReader.scene.getStylesheets().clear();
                if (FlowReader.split_toggle) {
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet_matrix.css").toExternalForm());
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet_matrix_split.css").toExternalForm());

                } else {
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet_matrix.css").toExternalForm());

                    scene.setFill(Color.web("000000"));
                }
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
                verticalLockButton.setText("Vertical Lock: On");
                zoomLockButton.setText("Zoom Lock: On");
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

        fullScreenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                    fullScreenButton.setText("FullScreen");
                } else {
                    primaryStage.setFullScreen(true);
                    fullScreenButton.setText("FullScreen");

                }
            }
        });
        splitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                fr.split();
            }
        });
        
        zoomAtMouseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(ribbon.toggleZoomCenter()){

zoomAtMouseButton.setText("Zoom method: cursor position(ish)");
            }
                else{
                    zoomAtMouseButton.setText("Zoom method: default center");

                }}
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
        zoomAtMouseButton.setDisable(false);
    }
}
