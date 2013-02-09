/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.utils.DocumentCreationTask;
import flowreader.utils.TextFileReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
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
    private Button openFileButton, pagesSceneButton, wordCloudsSceneButton, flowViewSceneButton, diveViewSceneButton, seamlessSceneButton; // The buttons at the bottom of the page
    
    private RibbonView ribbon; // The ribbon at the center of the page
    private ProgressIndicator pi;
    private TextFileReader fileReader;
    
    public MainView(Stage primaryStage){
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage);
        this.setTop(topBtnsBar);
        ribbon = new RibbonView();
        this.pi = new ProgressIndicator(0.0);
        this.setBottom(this.pi);
    }
    
    private void setUpButtons() {
        closeBtn = new Button("x");
        closeBtn.setId("closeBtn");

        minBtn = new Button("_");
        minBtn.setId("minBtn");

        openFileButton = new Button("Open file");
        openFileButton.setId("topbarbutton");
        wordCloudsSceneButton = new Button("WordClouds");
        wordCloudsSceneButton.setId("topbarbutton");
        wordCloudsSceneButton.setDisable(true);
        
        flowViewSceneButton = new Button("FlowView");
        flowViewSceneButton.setId("topbarbutton");
        flowViewSceneButton.setDisable(true);
        
        diveViewSceneButton = new Button("Diving");
        diveViewSceneButton.setId("topbarbutton");
        diveViewSceneButton.setDisable(true);
        
        pagesSceneButton = new Button("Pages");
        pagesSceneButton.setId("topbarbutton");
        pagesSceneButton.setDisable(true);
        
        seamlessSceneButton = new Button("Flowing");
        seamlessSceneButton.setId("topbarbutton");
        seamlessSceneButton.setDisable(true);
        
    }
    
    private void setUpButtonBar() {
        this.setUpButtons();

        topBtnsBar = new HBox(10);

        HBox mainBtns = new HBox(10);
        mainBtns.getChildren().add(openFileButton);
        //mainBtns.getChildren().add(pagesSceneButton);
        //mainBtns.getChildren().add(wordCloudsSceneButton);
        //mainBtns.getChildren().add(flowViewSceneButton);
        mainBtns.getChildren().add(diveViewSceneButton);
        mainBtns.getChildren().add(seamlessSceneButton);

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
                    pagesSceneButton.setDisable(true);
                    wordCloudsSceneButton.setDisable(true);
                    flowViewSceneButton.setDisable(true);
                    diveViewSceneButton.setDisable(true);
                    seamlessSceneButton.setDisable(true);
                    fileReader = new TextFileReader(MainView.this, pi);
                    fileReader.startFileChooser(primaryStage);
                                  
                    new Thread(fileReader).start();
                    
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });

        wordCloudsSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.switchToWordCloud();
            }
        });
        
        pagesSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.switchToPages();
            }
        });
        
        flowViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.switchToFlowView();
            }
        });
        
        diveViewSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.switchToDiveView();
            }
        });
        
                seamlessSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ribbon.switchToTheView();
            }
        });
    }

    public void docOpenned(Document doc) {
        this.ribbon = new RibbonView(doc);
        System.out.println("pass1");
        this.setCenter(this.ribbon);
        System.out.println("pass2");
        BorderPane.setAlignment(ribbon, Pos.CENTER_LEFT);
        MainView.this.ribbon.toBack();

        pagesSceneButton.setDisable(false);
        wordCloudsSceneButton.setDisable(false);
        flowViewSceneButton.setDisable(false);
        diveViewSceneButton.setDisable(false);
        seamlessSceneButton.setDisable(false);
        
    }

    

}
