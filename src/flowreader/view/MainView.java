/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.utils.DocumentCreationTask;
import flowreader.utils.TextFileReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 *
 * @author D-Day
 */
public class MainView extends BorderPane{
    
    private HBox topBtnsBar; // the button bar at the top of the screen
    private Button minBtn, closeBtn; // The buttons at the top of the page
    private Button openFileButton, flowViewSceneButton, diveViewSceneButton; // The buttons at the bottom of the page
    
    private RibbonView ribbon; // The ribbon at the center of the page
    private ProgressIndicator pi;
    private TextFileReader fileReader;
    
    public MainView(Stage primaryStage){
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage);
        this.setTop(topBtnsBar);
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

        minBtn = new Button("_");
        minBtn.setId("minBtn");

        openFileButton = new Button("Open file");
        openFileButton.setId("topbarbutton");
        
        flowViewSceneButton = new Button("Flowing");
        flowViewSceneButton.setId("topbarbutton");
        flowViewSceneButton.setDisable(true);
        
        diveViewSceneButton = new Button("Diving");
        diveViewSceneButton.setId("topbarbutton");
        diveViewSceneButton.setDisable(true);
    }
    
    private void setUpButtonBar() {
        this.setUpButtons();

        topBtnsBar = new HBox(10);

        HBox mainBtns = new HBox(10);
        mainBtns.getChildren().add(openFileButton);
        mainBtns.getChildren().add(diveViewSceneButton);
        mainBtns.getChildren().add(flowViewSceneButton);

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
                    pi = new ProgressIndicator(0.0);
                    pi.setStyle(" -fx-progress-color: #005888;");
                    // changing size without css
                    pi.setPrefSize(100, 100);
                    pi.setMinSize(100, 100);
                    pi.setMaxSize(100, 100);
                    setCenter(pi);
                    flowViewSceneButton.setDisable(true);
                    diveViewSceneButton.setDisable(true);
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
    }

    public void docOpenned(Document doc, RibbonView ribbon) {
        this.ribbon = ribbon;
        BorderPane.setAlignment(ribbon, Pos.CENTER_LEFT);
        this.ribbon.toBack();
        
        flowViewSceneButton.setDisable(false);
        diveViewSceneButton.setDisable(false);
    }

    

}
