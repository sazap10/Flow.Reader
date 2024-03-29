/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.utils.DocumentCreationTask;
import flowreader.utils.FileReader;
import flowreader.utils.PDFFileReader;
import flowreader.utils.TextFileReader;
import java.io.File;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author D-Day
 */
public final class MainView extends BorderPane {

    public HBox topBtnsBar; // the button bar at the top of the screen
    public HBox bottomBtnsBar; // the button bar at the bottom of the screen
    public VBox sideBtnsBar;
    public Button minBtn, closeBtn; // The buttons at the top of the page
    private Button openFileButton, flowViewSceneButton, diveViewSceneButton, ThemeButton, zoomLockButton, resetButton, verticalLockButton, readingModeButton, GlowButton, fullScreenButton, splitButton, zoomAtMouseButton, TextButton, upButton, downButton, pageWidthButton, pageHeightButton, closeDocButton, wordCloudButton, configButton; // The buttons at the bottom of the page
    public Button homeButton;
    private RibbonView ribbon; // The ribbon at the center of the page
    private ProgressIndicator progressIndicator;
    private FileReader fileReader;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private EventHandler<KeyEvent> keyHandler;
    private StackPane home;
    VBox introBox;
    private boolean split_version;
    private boolean toggle_buttons = false;
    private HBox mainBtns;
    private HBox effectBtns;
    private VBox configBtns;
    private boolean configOn = true;
    private boolean isGlow = false;

    public MainView(FlowReader fr, Stage primaryStage, Scene scene, Boolean split_version) {
        this.setId("mainview");
        this.split_version = split_version;
        this.setUpKeyEvents(fr);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage, scene, fr);

        ribbon = new RibbonView();
        this.progressIndicator = new ProgressIndicator(0.0);
        progressIndicator.getStyleClass().add("customProgressIndicator");
        progressIndicator.setStyle(" -fx-progress-color: #005888;");
        // changing size without css
        progressIndicator.setPrefSize(100, 100);
        progressIndicator.setMinSize(100, 100);
        progressIndicator.setMaxSize(100, 100);
        this.buildHomeView();
        this.setCenter(home);
        configButton.fire();
    }

    /**
     * Build the homepage when we start the application
     */
    private void buildHomeView() {
        home = new StackPane();
        Group g = new Group();
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.1);

        Rectangle rect = new Rectangle();

        rect.widthProperty().bind(home.widthProperty());
        rect.heightProperty().bind(home.heightProperty());
        rect.setFill(Color.DARKSLATEBLUE);
        Text text = new Text();
        text.setText("Welcome to FlowReader.\n Enjoy! \n\nPress number 1 to see keyboard shortcuts.\n\nNote: In Mac OS X, dialogs do not appear in full screen mode. \n(Using Mac's version of full screen instead seems to overcome this problem.) \n (The button at top right corner)");

        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.ALICEBLUE);
        text.setFont(Font.font(null, FontWeight.BOLD, 20));
        text.setEffect(bloom);

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
            text2.setFill(Color.rgb(red, green, blue, .99));
            int randfont = rand.nextInt(fontList.size());
            text2.setFont(new Font(fontList.get(randfont), font));
            g.getChildren().add(text2);
        }
        introBox = new VBox(10);
        introBox.getChildren().add(progressIndicator);
        introBox.getChildren().add(text);
        //home.getChildren().add(g);
         Image logo = new Image(this.getClass().getResource("logo.png").toExternalForm(),true);
        ImageView iv = new ImageView();
        iv.setImage(logo);
        home.getChildren().add(iv);
        home.getChildren().add(introBox);
        introBox.setAlignment(Pos.CENTER);
    }

    /**
     * Creates all the key events of the application
     *
     * @param fr
     */
    private void setUpKeyEvents(final FlowReader fr) {
        keyHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                OUTER:
                switch (event.getCode()) {
                case DIGIT1:
                    if (!split_version) {
                        fr.showShortcuts();
                    }
                    break;
                case F:
                    if (!split_version) {
                        fullScreenButton.fire();
                    }
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
                case T:
                    if (!split_version) {
                        ThemeButton.fire();
                    }
                    break;
                case G:
                    GlowButton.fire();
                    break;
                case R:
                    resetButton.fire();


                    break;
                case Y:
                    if (!split_version) {
                        splitButton.fire();
                    }
                    break;
                case V:
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
                            switch (ribbon.getCurrentView()) {
                                case "":
                                    diveViewSceneButton.fire();
                                    break OUTER;
                                case "DiveView":
                                    flowViewSceneButton.fire();
                                    break;
                                case "FlowView":
                                    homeButton.fire();
                                    break;
                                case "HomeView":
                                    diveViewSceneButton.fire();
                                    break;
                            }
                        }
                }
                event.consume();
            }
        };
    }

    /**
     * Creates all the buttons of the application
     */
    private void setUpButtons() {
        int minWidth = 200;
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
        Tooltip homeT = new Tooltip("Switch to Home");
        homeT.getStyleClass().add("Tooltip");
        homeButton.setTooltip(homeT);

        flowViewSceneButton = new Button("Flowing");
        flowViewSceneButton.setId("topbarbutton");
        Tooltip flowViewSceneT = new Tooltip("Switch to Flow View");
        flowViewSceneT.getStyleClass().add("Tooltip");
        flowViewSceneButton.setTooltip(flowViewSceneT);

        diveViewSceneButton = new Button("Diving");
        diveViewSceneButton.setId("topbarbutton");
        Tooltip diveViewSceneT = new Tooltip("Switch to Dive View");
        diveViewSceneT.getStyleClass().add("Tooltip");
        diveViewSceneButton.setTooltip(diveViewSceneT);

        ThemeButton = new Button("Change Theme");
        ThemeButton.setId("topbarbutton");
        Tooltip matrixThemeT = new Tooltip("Change theme style");
        matrixThemeT.getStyleClass().add("Tooltip");
        ThemeButton.setTooltip(matrixThemeT);
        ThemeButton.setMinWidth(minWidth);

        zoomLockButton = new Button("Zoom Lock: Off");
        zoomLockButton.setId("topbarbutton");
        Tooltip zoomLockT = new Tooltip("Toggle waiting for next word cloud level transition");
        zoomLockT.getStyleClass().add("Tooltip");
        zoomLockButton.setTooltip(zoomLockT);
        zoomLockButton.setMinWidth(minWidth);

        resetButton = new Button("Overview mode");
        resetButton.setId("topbarbutton");
        Tooltip resetT = new Tooltip("Top view(only one word cloud) of the document");
        resetT.getStyleClass().add("Tooltip");
        resetButton.setTooltip(resetT);

        verticalLockButton = new Button("Vertical Lock: Off");
        verticalLockButton.setId("topbarbutton");
        Tooltip verticalLockT = new Tooltip("Toggle enabling/disabling moving the ribbon vertically");
        verticalLockT.getStyleClass().add("Tooltip");
        verticalLockButton.setTooltip(verticalLockT);
        verticalLockButton.setMinWidth(minWidth);

        readingModeButton = new Button("Reading Mode");
        readingModeButton.setId("topbarbutton");
        Tooltip readingModeT = new Tooltip("Zoom in to the beginning");
        readingModeT.getStyleClass().add("Tooltip");
        readingModeButton.setTooltip(readingModeT);

        GlowButton = new Button("Glow!");
        GlowButton.setId("topbarbutton");
        Tooltip glowT = new Tooltip("Add the Glow effect!");
        glowT.getStyleClass().add("Tooltip");
        GlowButton.setTooltip(glowT);
        GlowButton.setMinWidth(minWidth);

        fullScreenButton = new Button("FullScreen: On");
        fullScreenButton.setId("topbarbutton");
        Tooltip fullScreenT = new Tooltip("Toggle full screen");
        fullScreenT.getStyleClass().add("Tooltip");
        fullScreenButton.setTooltip(fullScreenT);
        fullScreenButton.setMinWidth(minWidth);

        splitButton = new Button("Split!");
        splitButton.setId("topbarbutton");
        Tooltip splitT = new Tooltip("Create another instance of flowreader -- FlowReader x 2!");
        splitT.getStyleClass().add("Tooltip");
        splitButton.setTooltip(splitT);
        splitButton.setMinWidth(minWidth);


        zoomAtMouseButton = new Button("Zoom: Center");
        zoomAtMouseButton.setId("topbarbutton");
        Tooltip zoomAtMouseT = new Tooltip("Toggle between centering the zoom position at default screen location or at cursor position");
        zoomAtMouseT.getStyleClass().add("Tooltip");
        zoomAtMouseButton.setTooltip(zoomAtMouseT);
        zoomAtMouseButton.setMinWidth(minWidth);

        TextButton = new Button("Text: On");
        TextButton.setId("topbarbutton");
        Tooltip TextButtonT = new Tooltip("Make text in Flow View visible/invisible");
        TextButtonT.getStyleClass().add("Tooltip");
        TextButton.setTooltip(TextButtonT);
        TextButton.setMinWidth(minWidth);

        upButton = new Button("-");
        upButton.setId("topbarbutton");
        Tooltip upButtonT = new Tooltip("Jump up to next word cloud");
        upButtonT.getStyleClass().add("Tooltip");
        upButton.setTooltip(upButtonT);
        upButton.setMinWidth(minWidth);

        downButton = new Button("+");
        downButton.setId("topbarbutton");
        Tooltip downButtonT = new Tooltip("Jump down to next word cloud");
        downButtonT.getStyleClass().add("Tooltip");
        downButton.setTooltip(downButtonT);
        downButton.setMinWidth(minWidth);

        pageWidthButton = new Button("Set Page Width");
        pageWidthButton.setId("topbarbutton");
        Tooltip pageWidthButtonT = new Tooltip("Set page width in Flow View");
        pageWidthButtonT.getStyleClass().add("Tooltip");
        pageWidthButton.setTooltip(pageWidthButtonT);
        pageWidthButton.setMinWidth(minWidth);

        pageHeightButton = new Button("Set Page Height");
        pageHeightButton.setId("topbarbutton");
        Tooltip pageHeightButtonT = new Tooltip("Set page height in Flow View");
        pageHeightButtonT.getStyleClass().add("Tooltip");
        pageHeightButton.setTooltip(pageHeightButtonT);
        pageHeightButton.setMinWidth(minWidth);

        closeDocButton = new Button("Close Document");
        closeDocButton.setId("topbarbutton");
        Tooltip closeDocButtonT = new Tooltip("Close the current document");
        closeDocButtonT.getStyleClass().add("Tooltip");
        closeDocButton.setTooltip(closeDocButtonT);

        configButton = new Button("Show config");
        configButton.setId("topbarbutton");
        Tooltip configButtonT = new Tooltip("Show/hide config buttons");
        configButtonT.getStyleClass().add("Tooltip");
        configButton.setTooltip(configButtonT);
        configButton.setMinWidth(minWidth);

        wordCloudButton = new Button("Word Cloud: On");
        wordCloudButton.setId("topbarbutton");
        Tooltip wordCloudButtonT = new Tooltip("Show/hide word clouds in Flow View");
        wordCloudButtonT.getStyleClass().add("Tooltip");
        wordCloudButton.setTooltip(wordCloudButtonT);
        wordCloudButton.setMinWidth(minWidth);

        openFileButton.setDisable(false);
        homeButton.setDisable(true);
        flowViewSceneButton.setDisable(true);
        diveViewSceneButton.setDisable(true);

        resetButton.setDisable(true);
        readingModeButton.setDisable(true);

        ThemeButton.setDisable(false);
        GlowButton.setDisable(false);
        pageWidthButton.setDisable(false);
        pageHeightButton.setDisable(true);
        closeDocButton.setDisable(true);
        fullScreenButton.setDisable(false);

        zoomLockButton.setDisable(true);
        verticalLockButton.setDisable(true);
        zoomAtMouseButton.setDisable(true);
        TextButton.setDisable(true);
        upButton.setDisable(true);
        downButton.setDisable(true);
        wordCloudButton.setDisable(true);
    }

    /**
     * Set up all the buttons (top, bottom and side) bar of the application
     */
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


        mainBtns = new HBox(10);
        effectBtns = new HBox(10);
        configBtns = new VBox(10);
        mainBtns.getChildren().addAll(openFileButton, closeDocButton, homeButton, diveViewSceneButton, flowViewSceneButton);
        if (split_version) {
            effectBtns.getChildren().addAll(resetButton, readingModeButton);
            configBtns.getChildren().addAll(downButton, upButton, zoomLockButton, verticalLockButton,
                    zoomAtMouseButton, TextButton, wordCloudButton, pageWidthButton, GlowButton, splitButton, configButton);

        } else {
            effectBtns.getChildren().addAll(resetButton, readingModeButton);
            configBtns.getChildren().addAll(downButton, upButton, zoomLockButton,
                    verticalLockButton, zoomAtMouseButton, TextButton, wordCloudButton, pageWidthButton, GlowButton, splitButton, ThemeButton, fullScreenButton, configButton);

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

    /**
     * Set up events listener for all the buttons
     *
     * @param primaryStage
     * @param scene
     * @param fr
     */
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
                        FlowReader.cancelled = true;
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
                    docClosed();
                    progressIndicator = new ProgressIndicator(0.0);
                    progressIndicator.setStyle(" -fx-progress-color: #005888;");
                    // changing size without css
                    progressIndicator.setPrefSize(100, 100);
                    progressIndicator.setMinSize(100, 100);
                    progressIndicator.setMaxSize(100, 100);
                    progressIndicator.getStyleClass().add("customProgressIndicator");

                    introBox.getChildren().set(0, progressIndicator);

                    //setCenter(pi);
                    File file = startFileChooser(primaryStage);
                    String name = file.getName();
                    if (name.endsWith(".txt")) {
                        fileReader = new TextFileReader(file);
                    } else if (name.endsWith(".pdf")) {
                        fileReader = new PDFFileReader(file);
                    }
                    DocumentCreationTask dct = new DocumentCreationTask(fr, fileReader, MainView.this, split_version);
                    progressIndicator.progressProperty().bind(fileReader.progressProperty());
                    Thread t = new Thread(dct);
                    t.start();

                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                MainView.this.setCenter(home);
                ribbon.setCurrentView("HomeView");
                openFileButton.setDisable(false);
                homeButton.setDisable(false);
                flowViewSceneButton.setDisable(false);
                diveViewSceneButton.setDisable(false);

                resetButton.setDisable(true);
                readingModeButton.setDisable(true);

                ThemeButton.setDisable(false);
                GlowButton.setDisable(false);
                pageWidthButton.setDisable(true);
                pageHeightButton.setDisable(true);
                closeDocButton.setDisable(false);
                fullScreenButton.setDisable(false);

                zoomLockButton.setDisable(true);
                verticalLockButton.setDisable(true);
                zoomAtMouseButton.setDisable(true);
                TextButton.setDisable(true);
                upButton.setDisable(true);
                downButton.setDisable(true);
                wordCloudButton.setDisable(true);

            }
        });
        diveViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //MainView.this.getChildren().clear();

                MainView.this.setCenter(ribbon);
                ribbon.switchToDiveView();
                openFileButton.setDisable(false);
                homeButton.setDisable(false);
                flowViewSceneButton.setDisable(false);
                diveViewSceneButton.setDisable(false);

                resetButton.setDisable(false);
                readingModeButton.setDisable(false);

                ThemeButton.setDisable(false);
                GlowButton.setDisable(false);
                pageWidthButton.setDisable(true);
                pageHeightButton.setDisable(true);
                closeDocButton.setDisable(false);
                fullScreenButton.setDisable(false);

                zoomLockButton.setDisable(true);
                verticalLockButton.setDisable(true);
                zoomAtMouseButton.setDisable(true);
                TextButton.setDisable(true);
                upButton.setDisable(true);
                downButton.setDisable(true);
                wordCloudButton.setDisable(true);

            }
        });

        flowViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //MainView.this.getChildren().clear();

                MainView.this.setCenter(ribbon);
                ribbon.switchToFlowView();

                openFileButton.setDisable(false);
                homeButton.setDisable(false);
                flowViewSceneButton.setDisable(false);
                diveViewSceneButton.setDisable(false);

                resetButton.setDisable(false);
                readingModeButton.setDisable(false);

                ThemeButton.setDisable(false);
                GlowButton.setDisable(false);
                pageWidthButton.setDisable(true);
                pageHeightButton.setDisable(true);
                closeDocButton.setDisable(false);
                fullScreenButton.setDisable(false);

                zoomLockButton.setDisable(false);
                verticalLockButton.setDisable(false);
                zoomAtMouseButton.setDisable(false);
                TextButton.setDisable(false);
                upButton.setDisable(false);
                downButton.setDisable(false);
                wordCloudButton.setDisable(false);


            }
        });


        ThemeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                FlowReader.currentTheme = (FlowReader.currentTheme + 1) % FlowReader.themes.size();

                String theme = FlowReader.themes.get(FlowReader.currentTheme);
                FlowReader.scene.getStylesheets().clear();
                if (FlowReader.split_toggle) {
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource(theme).toExternalForm());
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource(theme.substring(0, theme.indexOf(".")).concat("_split.css")).toExternalForm());

                } else {
                    FlowReader.scene.getStylesheets().add(FlowReader.class.getResource(theme).toExternalForm());
                    if (theme.equals("stylesheet_matrix.css")) {
                        scene.setFill(Color.web("000000"));
                    } else {
                        if (theme.equals("stylesheet.css")) {
                            scene.setFill(Color.web("B8B8B8"));
                        }
                    }
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
                verticalLockButton.setText("Vertical Lock: Off");
                zoomLockButton.setText("Zoom Lock: Off");
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
                if (isGlow) {
                    ribbon.setEffect(null);

                } else {
                    ribbon.setEffect(new Glow(0.8));

                }
                isGlow = !isGlow;
            }
        });



        fullScreenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                    fullScreenButton.setText("FullScreen: Off");
                } else {
                    primaryStage.setFullScreen(true);
                    fullScreenButton.setText("FullScreen: On");

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
                if (ribbon.toggleZoomCenter()) {

                    zoomAtMouseButton.setText("Zoom: Cursor");
                } else {
                    zoomAtMouseButton.setText("Zoom: Center");

                }
            }
        });

        TextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (ribbon.toggleText()) {

                    TextButton.setText("Text: On");
                } else {
                    TextButton.setText("Text: Off");
                }
            }
        });

        upButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                ribbon.goUp();

            }
        });
        downButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                ribbon.goDown();


            }
        });
        pageWidthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                fr.setPageWidth(ribbon);

            }
        });

        pageHeightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                final Stage dialog = new Stage(StageStyle.TRANSPARENT);
                dialog.initOwner(primaryStage);

                dialog.initModality(Modality.WINDOW_MODAL);
                final TextArea ta = new TextArea();
                ta.setPrefColumnCount(5);
                ta.setPrefRowCount(1);
                Scene dialog_scene = new Scene(
                        HBoxBuilder.create().styleClass("modal-dialog").children(
                        LabelBuilder.create().text("Please enter new page height (recommended range: 400-2000):").textFill(Color.WHITE).build(),
                        ta,
                        ButtonBuilder.create().id("ok").text("OK").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // take action and close the dialog.
                        ribbon.setPageHeight(Integer.valueOf(ta.getText()));
                        primaryStage.getScene().getRoot().setEffect(null);
                        dialog.close();

                    }
                }).build()).build(), Color.TRANSPARENT);
                dialog_scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);

                dialog.setScene(dialog_scene);

                dialog
                        .getScene().getStylesheets().add(FlowReader.class
                        .getResource("modal-dialog.css").toExternalForm());
                primaryStage.getScene()
                        .getRoot().setEffect(new BoxBlur());
                dialog.showAndWait();
            }
        });

        closeDocButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                docClosed();
            }
        });

        wordCloudButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (ribbon.toggleWordCloud()) {

                    TextButton.setText("Ribbon: On");
                } else {
                    TextButton.setText("Ribbon: Off");
                }
            }
        });

        configButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                configOn = !configOn;
                if (configOn) {
                    configButton.setText("Hide config");
                } else {
                    configButton.setText("Show config");
                }
                for (int i = 0; i < configBtns.getChildren().size(); i++) {
                    if (!configBtns.getChildren().get(i).equals(configButton)) {
                        configBtns.getChildren().get(i).setVisible(configOn);
                    }
                }
            }
        });


    }

    /**
     * Activate everything that should be activated when a document is open
     *
     * @param doc
     * @param ribbon
     */
    public void docOpenned(Document doc, RibbonView ribbon) {
        this.ribbon = ribbon;
        ribbon.setPageWidth(FlowReader.pageWidth);
        BorderPane.setAlignment(ribbon, Pos.CENTER_LEFT);
        this.ribbon.toBack();
    }

    /**
     * Desactivate all that should be desactivated when we close a document
     */
    public void docClosed() {
        homeButton.fire();

        this.ribbon = new RibbonView();

        System.gc();
        openFileButton.setDisable(false);
        homeButton.setDisable(true);
        flowViewSceneButton.setDisable(true);
        diveViewSceneButton.setDisable(true);
        ThemeButton.setDisable(true);
        zoomLockButton.setDisable(true);
        resetButton.setDisable(true);
        verticalLockButton.setDisable(true);
        readingModeButton.setDisable(true);
        GlowButton.setDisable(true);
        zoomAtMouseButton.setDisable(true);
        TextButton.setDisable(true);
        upButton.setDisable(true);
        downButton.setDisable(true);
        pageHeightButton.setDisable(false);
        pageWidthButton.setDisable(false);
        closeDocButton.setDisable(true);
        wordCloudButton.setDisable(true);
    }

    /**
     * Creates and start a file chooser.
     *
     * @param primaryStage
     * @return
     */
    public File startFileChooser(Stage primaryStage) {
        //start file chooser
        File f = new File(System.getProperty("user.dir"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please choose a text file to read");
        fileChooser.setInitialDirectory(f);

        //Set extension filter
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add("*.pdf");
        extensions.add("*.txt");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf), Text files (*.txt)", extensions);
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        f = fileChooser.showOpenDialog(primaryStage);
        return f;

    }
}
