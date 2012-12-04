package project_1st_attempt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WordWrapMain extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(final Stage primaryStage) {

		primaryStage.setTitle("Hello World!");
		Rectangle r = new Rectangle(400, 500);
		final StackPane root = new StackPane();
		FlowPane flow = new FlowPane();
		flow.setPadding(new Insets(5, 0, 5, 0));
		flow.setHgap(4);
		r.setFill(Color.WHITE);
		r.setStroke(Color.BLACK);
		File file = openFile(primaryStage);
		WordWrap w = new WordWrap(file, r);
		ArrayList<String> pages = null;
		try {
			pages = w.readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String page:pages){
			flow.getChildren().add(new Text(page));
		}
		// flow.getChildren().addAll(pages);
		root.getChildren().add(flow);
		primaryStage.setScene(new Scene(root, 900, 600));
		primaryStage.show();

	}

	public File openFile(Stage primaryStage) {
		File f = new File(System.getProperty("user.dir"));
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Please choose a text file to read");
		fileChooser.setInitialDirectory(f);

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(primaryStage);
		return file;
	}
}
