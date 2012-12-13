/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.data;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author D-Day
 */
public class TextFileReader implements FileReader {

    File file;
    TextFileReader_WordCloud fileReader_WordCloud;

    public TextFileReader(TextFileReader_WordCloud fileReader_WordCloud) {
        this.fileReader_WordCloud = fileReader_WordCloud;
    }

    @Override
    public void startFileChooser(Stage primaryStage) {
        //start file chooser
        File f = new File(System.getProperty("user.dir"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please choose a text file to read");
        fileChooser.setInitialDirectory(f);

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        file = fileChooser.showOpenDialog(primaryStage);

    }

    public ArrayList<String> readFile(Rectangle bounds) throws IOException {
        ArrayList<String> pages = new ArrayList<String>();
        double boundWidth = bounds.getWidth();
        double boundHeight = bounds.getHeight();
        double spaceWidth = new Text(" ").getBoundsInLocal().getWidth();
        double lineHeight = new Text("").getBoundsInLocal().getHeight();
        double spaceLeft = bounds.getWidth();


        Text tempPage = new Text("");
        String page = "";
        LineNumberReader r = new LineNumberReader(new java.io.FileReader(file));
        String paragraph, word;
        while ((paragraph = r.readLine()) != null) {
            //fileReader_WordCloud.readLine(paragraph);

            Scanner sc = new Scanner(paragraph);
            while (sc.hasNext()) {
                word = sc.next();
                double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                if (textWithNewLine > boundHeight) {
                    pages.add(page);
                    page = "";
                }
                if (wordWidth + spaceWidth > spaceLeft) {
                    if (!(textWithNewLine > boundHeight)) {
                        page += "\n";
                    }
                    spaceLeft = boundWidth - wordWidth;
                } else {
                    spaceLeft = spaceLeft - (wordWidth + spaceWidth);
                }
                page += word + " ";
                tempPage.setText(page);
            }
            if (!((tempPage.getBoundsInLocal().getHeight() + lineHeight) > boundHeight)) {
                page += "\n";
                tempPage.setText(page);
                spaceLeft = boundWidth;
            }
            sc.close();
        }
        pages.add(page);
        r.close();
        return pages;
    }
}
