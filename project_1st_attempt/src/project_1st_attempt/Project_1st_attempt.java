package project_1st_attempt;

//imports for reading files
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author psupaong
 */
public class Project_1st_attempt extends Application {

    @Override
    public void start(Stage primaryStage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        final Slider page_slider = new Slider(0, 1, 1);
        File file = init_file_chooser(primaryStage);

        //create text with chosen file
        Text text = new Text();
        text.setFont(new Font(18));
        text.setWrappingWidth(screenBounds.getWidth() - screenBounds.getWidth() * 0.35);
        text.setTextAlignment(TextAlignment.JUSTIFY);

        text.setText(readFile(file));
        BorderPane root = new BorderPane();

        StackPane content = new StackPane();
        content.getChildren().add(text);

        root.setCenter(content);
        root.setBottom(page_slider);

        //create scene in full screen
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        primaryStage.setTitle("Flow Reader");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private File init_file_chooser(Stage primaryStage) {

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

    private String readFile(File file) {
        //got from http://java-buddy.blogspot.co.uk/2012/05/read-text-file-with-javafx-filechooser.html
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Project_1st_attempt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Project_1st_attempt.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Project_1st_attempt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stringBuffer.toString();
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
