package project_1st_attempt;

//imports for reading files
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author psupaong
 */

//Problems:
//- page spliting? (into arrays?)
//- implement touch

public class Project_1st_attempt extends Application {

    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    private Text text = new Text();

    @Override
    public void start(Stage primaryStage) {
        setText(primaryStage);

        StackPane root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(text);
        //create scene in full screen

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        setSceneEvents(scene);
        primaryStage.setTitle("Flow Reader");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setText(Stage primaryStage) {
        //http://docs.oracle.com/javafx/2/text/jfxpub-text.htm
        //create text with chosen file
        text.setFont(new Font("Verdana", 16));
        text.setEffect(null);
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.setWrappingWidth(screenBounds.getWidth() - screenBounds.getWidth() * 0.35);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        text.setText(readFile(startFileChooser(primaryStage)));

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
    
    //http://java-buddy.blogspot.co.uk/2012/05/read-text-file-with-javafx-filechooser.html
    private String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text+"\n");
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

    private void setSceneEvents(Scene scene) {
        //make text scale accordingly when resized
        //http://www.drdobbs.com/jvm/javafx-20-binding/231903245
        scene.widthProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        Double width = (Double) newValue;
                        text.setWrappingWidth(width * 0.65);
                    }
                });

        scene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(final KeyEvent event) {
                        if (event.getCode().toString().equals("HOME")) {
                            text.setTranslateY(0.0);
                        }
                        if (event.getCode().toString().equals("END")) {
                            System.out.println("END PRESSED");
                        }
                        event.consume();
                    }
                });

        scene.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        System.out.println("text.getTranslateY(): " + text.getTranslateY());
                        if (text.getTranslateY() == 0.0 && event.getDeltaY() > 0) {

                            System.out.println("Top of file!");
                        } else {
                            text.setTranslateY(text.getTranslateY() + event.getDeltaY());
                        }
                        event.consume();
                    }
                });
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
