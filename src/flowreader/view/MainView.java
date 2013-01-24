/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.utils.TextFileReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    private Button openFileButton, pagesSceneButton, wordCloudsSceneButton; // The buttons at the bottom of the page
    
    private RibbonView ribbon; // The ribbon at the center of the page
    
    public MainView(Stage primaryStage){
        this.setUpButtonBar();
        this.setButtonEvents(primaryStage);
        this.setTop(topBtnsBar);
        ribbon = new RibbonView();
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
        wordCloudsSceneButton.setDisable(false);
        
        pagesSceneButton = new Button("Pages");
        pagesSceneButton.setId("topbarbutton");
        pagesSceneButton.setDisable(false);
    }
    
    private void setUpButtonBar() {
        this.setUpButtons();

        topBtnsBar = new HBox(10);

        HBox mainBtns = new HBox(10);
        mainBtns.getChildren().add(openFileButton);
        mainBtns.getChildren().add(pagesSceneButton);
        mainBtns.getChildren().add(wordCloudsSceneButton);

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
                    fileReader.startFileChooser(primaryStage);     
                    Document document = fileReader.readFile(PageView.textBoundWidth, PageView.textBoundHeight);       

                    MainView.this.ribbon = new RibbonView(document);
                    
                    MainView.this.setCenter(MainView.this.ribbon);
                    BorderPane.setAlignment(ribbon, Pos.CENTER_LEFT);
                    MainView.this.ribbon.toBack();


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
    }

}
