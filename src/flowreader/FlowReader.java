package flowreader;

import flowreader.view.MainView;
import flowreader.view.PageView;
import javafx.application.Application;
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
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    public static boolean split_toggle = false;

    @Override
    public void start(Stage primaryStage) {
                this.priStage = primaryStage;

        primaryStage.setTitle("Flow Reader");
        if(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0){
                    primaryStage.setFullScreen(false);

        }else{
                    primaryStage.setFullScreen(true);

        }
        root = new Group();
        rootPane = new Pane();
                rootPane2 = new Pane();

        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight(), Color.web("B8B8B8"));
        
                splitPane = new SplitPane();
        splitPane.prefWidthProperty().bind(scene.widthProperty());
        splitPane.prefHeightProperty().bind(scene.heightProperty());
        
        mainView = new MainView(this,primaryStage, scene,false);
        mainView2 = new MainView(this, primaryStage, scene,true);

        //prevent buttons overlapping credit:
        //http://stackoverflow.com/questions/9837529/how-to-solve-the-overlapping-of-the-controls-each-other-belonging-to-two-differe

        rootPane.getChildren().add(mainView);
        rootPane.getChildren().add(mainView.topBtnsBar);
        rootPane.getChildren().add(mainView.bottomBtnsBar);
        rootPane.getChildren().add(mainView.sideBtnsBar);
        
        
        mainView.topBtnsBar.layoutYProperty().bind(rootPane.layoutYProperty());
        mainView.bottomBtnsBar.setLayoutY(screenBounds.getHeight() - 26);
        
                mainView.sideBtnsBar.setLayoutX(screenBounds.getWidth()-165);
                mainView.sideBtnsBar.setLayoutY(screenBounds.getHeight() - 26*9);
                                  
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
        PageView.setUpPageSize(500, 700);

        scene.getStylesheets().add(FlowReader.class.getResource("stylesheet.css").toExternalForm());
        primaryStage.getIcons().add(new Image(this.getClass().getResource("logo.png").toExternalForm()));
        primaryStage.setScene(scene);
        splitPane.getItems().add(rootPane);
        splitPane.getItems().add(rootPane2);
        
        primaryStage.show();
        
    }
    public void showShortcuts(){
        
                final Stage dialog = new Stage(StageStyle.TRANSPARENT);
                dialog.initOwner(priStage);
                
                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.setScene(
                        new Scene(
                        HBoxBuilder.create().styleClass("modal-dialog").children(
                        LabelBuilder.create().text("\n\nKeyboard shortcuts:\nF11: hide/show buttons\nH: Home\nW:Zoom In\nS:Zoom Out\nA: Move Left\nD:Move Right"
                + "\nM: Matrix Theme\nN: Normal Theme\nG: Glow!\nQ:Switch View\nF: Reset\nC: Reading Mode\nR: Reset Effect\nL: Vertical Lock\nZ: Zoom Lock").textFill(Color.WHITE).build(),
                        ButtonBuilder.create().id("ok").text("OK").defaultButton(true).onAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // take action and close the dialog.
                                                priStage.getScene().getRoot().setEffect(null);
                        dialog.close();

                    }
                }).build()).build(), Color.TRANSPARENT));
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
