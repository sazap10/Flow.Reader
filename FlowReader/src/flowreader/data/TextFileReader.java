/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.data;

import flowreader.core.Page;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * @param bounds the boundaries of the page used to know how much text contains a page
     * @return a list of pages that contains the text of each page and the words occurrences
     * @throws IOException 
     */
    public ArrayList<Page> readFile(Rectangle bounds) throws IOException {
        ArrayList<Page> pages = new ArrayList<Page>();
        
        // Text Wrapper
        double boundWidth = bounds.getWidth(); // Width of the page
        double boundHeight = bounds.getHeight(); // Height of the page
        double spaceWidth = new Text(" ").getBoundsInLocal().getWidth(); // Width of a space
        double lineHeight = new Text("").getBoundsInLocal().getHeight(); // Height of a line
        double spaceLeft = bounds.getWidth(); // Width of the page

        Text tempPage = new Text("");
        String pageText = "";
        HashMap<String, Integer> wordsOccurrences = new HashMap<String, Integer>();
        
        LineNumberReader r = new LineNumberReader(new java.io.FileReader(file));
        String paragraph, word;
        while ((paragraph = r.readLine()) != null) {
            Scanner sc = new Scanner(paragraph); // scan the line word by word
            while (sc.hasNext()) { // while there is words in the line
                word = sc.next(); 
                double wordWidth = new Text(word).getBoundsInLocal().getWidth();
                double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
                if (textWithNewLine > boundHeight) {
                    pages.add(new Page(pageText, wordsOccurrences));
                    pageText = "";
                    wordsOccurrences = new HashMap<String, Integer>();
                }
                if (wordWidth + spaceWidth > spaceLeft) {
                    if (!(textWithNewLine > boundHeight)) {
                        pageText += "\n";
                    }
                    spaceLeft = boundWidth - wordWidth;
                } else {
                    spaceLeft = spaceLeft - (wordWidth + spaceWidth);
                }
                pageText += word + " ";
                if(wordsOccurrences.get(word)!=null){
                    wordsOccurrences.put(word, wordsOccurrences.get(word)+1);
                }
                else{
                    wordsOccurrences.put(word, 1);
                }
                tempPage.setText(pageText);
            }
            if (!((tempPage.getBoundsInLocal().getHeight() + lineHeight) > boundHeight)) {
                pageText += "\n";
                tempPage.setText(pageText);
                spaceLeft = boundWidth;
            }
            sc.close();
        }
        pages.add(new Page(pageText, wordsOccurrences));
        
        r.close();
        return pages;
    }
}
