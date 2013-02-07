package project_1st_attempt;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ExampleWithWordWrapping extends Application{
	int current_page = 0;
    Label page_number = new Label();
    int array_size = 0;
    Text text = new Text();
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    ArrayList<String> array;
	 public static void main(String[] args) {
	        launch(args);
	    }
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setFullScreen(true);
		
		File file = startFileChooser(primaryStage);
		Rectangle bounds = new Rectangle(screenBounds.getWidth()*0.9,screenBounds.getHeight()*0.9);
		WordWrap w = new WordWrap(file,bounds);
		 array = w.readFile();
		setText(primaryStage);
        System.out.println(array.size() + " Pages");

        array_size = array.size();
        page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(text);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(page_number);
        borderPane.setCenter(anchorPane);

        Scene scene = new Scene(borderPane, screenBounds.getWidth(), screenBounds.getHeight());
        setSceneEvents(scene);
        scene.getStylesheets().add(Project_1st_attempt.class.getResource("Background.css").toExternalForm());

        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	 private File startFileChooser(Stage primaryStage) {

	        //start file chooser
	        File f = new File(System.getProperty("user.dir"));
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Please choose a text file to read");
	        fileChooser.setInitialDirectory(f);

	        //Set extension filter
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	        fileChooser.getExtensionFilters().add(extFilter);

	        //Show save file dialog
	        File file = fileChooser.showOpenDialog(primaryStage);
	        return file;

	    }
	 private void setText(Stage primaryStage) {

	        //create text with chosen file

	        text.setX(20);

	        //text.setWrappingWidth(screenBounds.getWidth() * 0.35 / 2);
	        text.setY(20);
	        //text.setEffect(null);
	        //
	        text.setText(array.get(0));
	    }
	 
	 private void setSceneEvents(final Scene scene) {
	        //make text scale accordingly when resized
	        //http://www.drdobbs.com/jvm/javafx-20-binding/231903245

	        //handles UP, DOWN, LEFT, RIGHT key presses
	        //LEFT and RIGHT turn page to the left or right respectively
	        //UP takes you to the beginning and DOWN takes you to the end
	        scene.setOnKeyPressed(
	                new EventHandler<KeyEvent>() {
	                    @Override
	                    public void handle(final KeyEvent event) {
	                        try {
	                            if (event.getCode().toString().equals("UP")) {
	                                text.setText(array.get(0));
	                                current_page = 0;
	                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                            }
	                            if (event.getCode().toString().equals("DOWN")) {
	                                text.setText(array.get(array.size() - 1));
	                                current_page = array.size() - 1;
	                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                            }
	                            if (event.getCode().toString().equals("RIGHT")) {
	                                text.setText(array.get(current_page == (array.size() - 1) ? (array.size() - 1) : ++current_page));
	                                //current_page++;
	                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                            }
	                            if (event.getCode().toString().equals("LEFT")) {
	                                text.setText(array.get(current_page == 0 ? 0 : --current_page));
	                                //current_page--;
	                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                            }
	                            event.consume();
	                        } catch (Exception e) {
	                            System.out.println("Exception: " + e);
	                        }
	                    }
	                });

	        //handles mouse scrolling
	        scene.setOnScroll(
	                new EventHandler<ScrollEvent>() {
	                    @Override
	                    public void handle(ScrollEvent event) {
	                        if ((event.getDeltaY() > 0)) {
	                            if ((current_page > 0)) {
	                                text.setText(array.get(--current_page));
	                                //current_page--;
	                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                            }
	                        } else {
	                            if (current_page < array.size() - 1) {
	                                text.setText(array.get(++current_page));
	                                //current_page++;
	                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                            }
	                        }

	                        event.consume();
	                    }
	                });

	        scene.setOnSwipeRight(new EventHandler<SwipeEvent>() {
	            @Override
	            public void handle(SwipeEvent event) {
	                if ((current_page > 0)) {
	                    text.setText(array.get(--current_page));
	                    //current_page--;
	                    page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                }
	                event.consume();
	            }
	        });

	        scene.setOnSwipeRight(new EventHandler<SwipeEvent>() {
	            @Override
	            public void handle(SwipeEvent event) {
	                if (current_page < array.size() - 1) {
	                    text.setText(array.get(++current_page));
	                    //current_page++;
	                    page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
	                }
	                event.consume();
	            }
	        });
	    }
}
