/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.utils.DocumentCreationTask;
import flowreader.utils.TextFileReader;
import javafx.beans.DefaultProperty;
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
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
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
    private Button minBtn, closeBtn; // The buttons at the top of the page
    private Button openFileButton, flowViewSceneButton, diveViewSceneButton, normalThemeButton, matrixThemeButton, zoomLockButton, centerButton, verticalLockButton,readingModeButton,GlowButton,ResetEffectButton; // The buttons at the bottom of the page
    private RibbonView ribbon; // The ribbon at the center of the page
    private ProgressIndicator pi;
    private TextFileReader fileReader;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    public MainView(Stage primaryStage, Scene scene) {
        this.setId("mainview");
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage,scene);
        //this.setTop(topBtnsBar);
        //this.setBottom(bottomBtnsBar);

        ribbon = new RibbonView();
        this.pi = new ProgressIndicator(0.0);

        pi.setStyle(" -fx-progress-color: #005888;");
        // changing size without css
        pi.setPrefSize(100, 100);
        pi.setMinSize(100, 100);
        pi.setMaxSize(100, 100);
        //this.setCenter(ribbon);
        this.setCenter(this.pi);

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

        centerButton = new Button("Reset");
        centerButton.setId("topbarbutton");
        centerButton.setDisable(true);

        verticalLockButton = new Button("Vertical Lock: Off");
        verticalLockButton.setId("topbarbutton");
        verticalLockButton.setDisable(true);
      
        readingModeButton = new Button("Reading Mode");
        readingModeButton.setId("topbarbutton");
        readingModeButton.setDisable(true);
        
        GlowButton= new Button("Glow!");
        GlowButton.setId("topbarbutton");
        GlowButton.setDisable(true);
      
        ResetEffectButton= new Button("Reset Effects");
        ResetEffectButton.setId("topbarbutton");
        ResetEffectButton.setDisable(true);
      
    }

    private void setUpButtonBar() {
        this.setUpButtons();

        topBtnsBar = new HBox(10);
        bottomBtnsBar = new HBox(10);
        
        topBtnsBar.setPrefWidth(screenBounds.getWidth());
topBtnsBar.setMaxWidth(screenBounds.getWidth());
topBtnsBar.setMinWidth(screenBounds.getWidth());

bottomBtnsBar.setPrefWidth(screenBounds.getWidth());
bottomBtnsBar.setMaxWidth(screenBounds.getWidth());
bottomBtnsBar.setMinWidth(screenBounds.getWidth());

        
        HBox mainBtns = new HBox(10);
        HBox configBtns = new HBox(10);
        mainBtns.getChildren().add(openFileButton);
        mainBtns.getChildren().add(diveViewSceneButton);
        mainBtns.getChildren().add(flowViewSceneButton);
        configBtns.getChildren().add(normalThemeButton);
        configBtns.getChildren().add(matrixThemeButton);
        configBtns.getChildren().add(zoomLockButton);
        configBtns.getChildren().add(centerButton);
        configBtns.getChildren().add(verticalLockButton);
        configBtns.getChildren().add(readingModeButton);
        configBtns.getChildren().add(GlowButton);
        configBtns.getChildren().add(ResetEffectButton);

        HBox winBtnBox = new HBox(10);
        winBtnBox.setAlignment(Pos.CENTER_RIGHT);
        winBtnBox.getChildren().addAll(minBtn, closeBtn);
        mainBtns.getChildren().add(winBtnBox);
        HBox.setHgrow(winBtnBox, Priority.ALWAYS);

        topBtnsBar.getChildren().addAll(mainBtns, winBtnBox);
        bottomBtnsBar.getChildren().addAll(configBtns);
    }

    private void setButtonEvents(final Stage primaryStage,final Scene scene) {

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
            @Override public void handle(ActionEvent actionEvent) {
              // take action and close the dialog.
                primaryStage.close();

              dialog.close();
            }
          }).build(),
          ButtonBuilder.create().text("Cancel").cancelButton(true).onAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
              // abort action and close the dialog.
              dialog.close();
              primaryStage.setFullScreen(true);
                            primaryStage.getScene().getRoot().setEffect(null);
            }
          }).build()
        ).build()
        , Color.TRANSPARENT
      )
    );
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
                    pi = new ProgressIndicator(0.0);
                    pi.setStyle(" -fx-progress-color: #005888;");
                    // changing size without css
                    pi.setPrefSize(100, 100);
                    pi.setMinSize(100, 100);
                    pi.setMaxSize(100, 100);
                    setCenter(pi);
                    flowViewSceneButton.setDisable(true);
                    diveViewSceneButton.setDisable(true);
                    normalThemeButton.setDisable(true);
                    matrixThemeButton.setDisable(true);
                    zoomLockButton.setDisable(true);
                    centerButton.setDisable(true);
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

        diveViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                MainView.this.setCenter(ribbon);
                ribbon.switchToDiveView();
            }
        });

        flowViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                MainView.this.setCenter(ribbon);
                ribbon.switchToFlowView();
            }
        });

        normalThemeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FlowReader.scene.getStylesheets().clear();

                FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
                                ribbon.setEffect(null);

            }
        });

        matrixThemeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FlowReader.scene.getStylesheets().clear();
                FlowReader.scene.getStylesheets().add(FlowReader.class.getResource("stylesheet_matrix.css").toExternalForm());
                
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
        centerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.goToCenter();

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
        flowViewSceneButton.setDisable(false);
        diveViewSceneButton.setDisable(false);
        normalThemeButton.setDisable(false);
        matrixThemeButton.setDisable(false);
        zoomLockButton.setDisable(false);
        centerButton.setDisable(false);
        verticalLockButton.setDisable(false);
        readingModeButton.setDisable(false);
        GlowButton.setDisable(false);
        ResetEffectButton.setDisable(false);
    }
}
