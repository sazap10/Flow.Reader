package project_1st_attempt;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author psupaong
 */
//Problems:
//- make new line after a word instead of cutting off
//
//i.e. hello
//world
//instead of:
//hello wo
//rld
//- how do you know what values to set to MAX_LINES_PER_PAGE and MAX_CHARACTERS_PER_LINE since different screens
//have different resolutions?
//- implement touch
//- search function?
public class Project_1st_attempt extends Application {

    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    // Get the current screen size
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    private Text text = new Text();
    ArrayList<String> array = new ArrayList<>();
    int current_page = 0;
    Label page_number = new Label();
    int array_size = 0;
    int MAX_LINES_PER_PAGE = 28;
    int MAX_CHARACTERS_PER_LINE = 85;

    @Override
    public void start(Stage primaryStage) {

        System.out.println("Screen size : " + scrnsize);

        text = TextBuilder.create().text("\nHello there, you have not selected a file to read! Why not?").build();
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

//http://docs.oracle.com/javafx/2/text/jfxpub-text.htm
    private void setText(Stage primaryStage) {

        //create text with chosen file
        text.setFont(Font.font(null, 20));

        text.setX(screenBounds.getWidth() * 0.35 / 2);
        text.setY(scrnsize.height * 0.04444);
        //text.setEffect(null);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        //text.setWrappingWidth(Text_Area.getWidth()-text.getFont().getSize()*10);
        //text.setTextOrigin(VPos.TOP);
        File f = startFileChooser(primaryStage);
        if (f != null) {
            readFile(parsefile(f));
            text.setText(array.get(current_page));
        } else {
            System.out.println("File chosen is null");
        }
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

    private File parsefile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        File new_file = new File("edited_" + file.getName());
        try {

            bufferedReader = new BufferedReader(new FileReader(file));
            int temp_int;
            char temp_text;
            int count_line = 0;
            int count_char = 0;
            int score = 0;
            String content = "";

            if (!new_file.exists()) {
                new_file.createNewFile();
            }

            FileWriter fw = new FileWriter(new_file.getAbsoluteFile());
            try (PrintWriter pw = new PrintWriter(fw)) {
                while ((temp_int = bufferedReader.read()) != -1) {
                    temp_text = (char) temp_int;
                    if (temp_text == '\n') {
                        count_line++;
                        pw.write(String.valueOf(temp_text));
                        count_char = 0;
                    } else {
                        if (count_char < MAX_CHARACTERS_PER_LINE) {
                            count_char++;
                            pw.write(String.valueOf(temp_text));
                        } else {
                            count_line++;
                            count_char = 0;

                            temp_int = bufferedReader.read();
                            if (temp_int != -1) {
                                //32 is space
                                if (temp_int != 32) {
                                    if (temp_text != ' ') {
                                        pw.write("–");//whats this for?
                                        pw.println();
                                        pw.write(String.valueOf(temp_text));
                                        temp_text = (char) temp_int;
                                        pw.write(String.valueOf(temp_text));
                                    } else {
                                        pw.println();
                                        temp_text = (char) temp_int;
                                        pw.write(String.valueOf(temp_text));
                                    }
                                } else {
                                    pw.write(String.valueOf(temp_text));
                                    pw.println();
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
                pw.write(content);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Project_1st_attempt.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Project_1st_attempt.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();


            } catch (IOException ex) {
                Logger.getLogger(Project_1st_attempt.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new_file;
    }

    //http://java-buddy.blogspot.co.uk/2012/05/read-text-file-with-javafx-filechooser.html
    private void readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String temp_text;
            int count_line = 0;
            while ((temp_text = bufferedReader.readLine()) != null) {

                if (count_line < MAX_LINES_PER_PAGE - 1) {
                    stringBuffer.append(temp_text).append("\n");
                    count_line++;
                } else {
                    stringBuffer.append(temp_text).append("\n");
                    array.add(stringBuffer.toString());
                    stringBuffer = new StringBuilder();
                    count_line = 0;
                }
            }
            array.add(stringBuffer.toString());



        } catch (FileNotFoundException ex) {
            Logger.getLogger(Project_1st_attempt.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Project_1st_attempt.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();


            } catch (IOException ex) {
                Logger.getLogger(Project_1st_attempt.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void setSceneEvents(final Scene scene) {
        //make text scale accordingly when resized
        //http://www.drdobbs.com/jvm/javafx-20-binding/231903245
        scene.widthProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        Double width = (Double) newValue;
                        text.setWrappingWidth(width * 0.60);
                        System.out.println("Height: " + scene.getHeight() + " Width: " + scene.getWidth());
                    }
                });

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
                                text.setText(array.get(current_page==(array.size() - 1)?(array.size() - 1):++current_page ));
                                //current_page++;
                                page_number.setText(Integer.toString(current_page + 1) + "/" + array_size);
                            }
                            if (event.getCode().toString().equals("LEFT")) {
                                text.setText(array.get(current_page==0?0:--current_page ));
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
                                text.setText(array.get(--current_page ));
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
