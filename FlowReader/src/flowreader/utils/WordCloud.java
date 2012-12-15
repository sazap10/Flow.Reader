/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.model;
import flowreader.utils.Word;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 *
 * @author jim
 */
public class WordCloud {

    private HashMap<String, Integer> words;
    private Integer numOfWordsInCloud = 30;
    private Integer maxFontSize = 500;
    public Integer minFontSize = 14;
    private Integer maxCount;
    private Integer minCount;
    private Integer normalizationConstant = 3;
    public ArrayList<Word> wordObjects;  //only public for basic testing
    private HashMap<String, Integer> commonWords;

    
    //constructor with a single page as input
    public WordCloud(Page page) {
        this.words = new HashMap<>();
        this.wordObjects = new ArrayList<>();
        this.commonWords = new HashMap<>();
        countWords(page);
    }
    
    
    
    //constructor which merges two clouds
    public WordCloud(WordCloud a, WordCloud b){
        this.words = a.getWords();
         Iterator it = b.getWords().entrySet().iterator();
        while (it.hasNext()){
             Map.Entry pairs = (Map.Entry) it.next();
             if (this.words.containsKey(pairs.getKey().toString())){
                 int count = this.words.get(pairs.getKey().toString());
                 count += b.getWords().get(pairs.getKey().toString());
             }
             else{
                 this.words.put(pairs.getKey().toString(),Integer.parseInt(pairs.getValue().toString()));
             }         
            
        }
        
    }
    
    
    //returns the words
     public HashMap<String, Integer> getWords(){
        return this.words;
    }

     
    //counts the words in an input page 
    private void countWords(Page page) {
        String text = page.getText();
        if (text != null) {
            ArrayList<String> characters;
            for (String i : text.split(" ")) {
                i = this.trimPunctuation(i);
                if (!(i.equals(""))) {
                    if (this.words.get(i) == null) {
                        words.put(i, 1);
                    } else {
                        if (!this.commonWords.containsKey(i)) {
                            Integer count = this.words.get(i);
                            count++;
                            this.words.put(i, count);
                        }
                    }
                }
            }
        }
    }

    
      //reads in the common words from file
      public void getCommonWords() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(System.getProperty("user.dir")+System.getProperty("file.separator")+"CommonEnglishWords.txt"));
            
            String temp_text;
            while ((temp_text = bufferedReader.readLine()) != null) {
                System.out.println(temp_text);
                this.commonWords.put(temp_text, 1);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't find the file!");
        } catch (IOException ex) {
            System.out.println("no idea!.. some IOException");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                System.out.println("couldn't close the file!");
            }
        }


    }

      
     //removes punctuation from any words found
    public String trimPunctuation(String word) {
        return word.replaceAll("\\W", "");

    }

    
    //creates Word objects with the top n most common words
    public String getNodes() {
        String output = "";
        String maxKey = "";
        int tmpCount;
        boolean maxCount = false;
        boolean minCount = false;
        for (int i = 0; i <= this.numOfWordsInCloud; i++) {
            Integer maxInMap = Collections.max(this.words.values());
            Iterator it = this.words.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pairs = (Map.Entry) it.next();
                //System.out.println(pairs.getKey() + " = " + pairs.getValue());

                tmpCount = Integer.parseInt(pairs.getValue().toString());
                if (tmpCount == maxInMap) {
                    output += (pairs.getKey().toString() + ":" + "        " + tmpCount + "\n");
                    maxKey = pairs.getKey().toString();
                    this.wordObjects.add(new Word(maxKey, tmpCount));
                    if (!(maxCount)) {
                        this.maxCount = tmpCount;
                        maxCount = true;
                    }
                }

            }
            this.words.remove(maxKey);
            this.minCount = maxInMap;
        }

        return output;
    }

    
    //iterates through the word objects and assigns them a font size
    public void setWordSizes() {
        for (Word word : wordObjects) {
            int countDiff = word.getCount() - this.minCount;
            int totalCountDiff = this.maxCount - this.minCount;
            int fontSize = ((this.maxFontSize * countDiff) / (this.normalizationConstant * totalCountDiff));
            if (fontSize < this.minFontSize) {
                fontSize = this.minFontSize;
            }
            word.setFontSize(fontSize);

        }
    }
    
    
    
    //very big function for rendering the wordclouds
    //(returns a group containing the rendered words;
      public Group renderCloud(ArrayList<Word> wordObjects ){
    //ArrayList<Integer> indexes = new ArrayList<Integer>();
   // ArrayList<Word> wordObjects = new ArrayList<Word>();
   //  for (int i = 0; i <= wordObjects.size() -1; i ++){
  ////       indexes.add(i, i);
   //  }
     Collections.shuffle(wordObjects);
   //  System.out.println("PRINTING INDEXES!");
  //   System.out.println(indexes);
    //create the basic rectangle (usually in terms of class attributes)
    Group cloud = new Group();
    Rectangle r = new Rectangle();
    r.setX(0);
    r.setY(0);
    r.setWidth(1500);
    r.setHeight(500);
    r.setArcWidth(100);
    r.setArcHeight(100);
    r.setFill(Color.WHITE);
    
    //these would normally be inputs to the function
    
    
    double maxWidth = 1000;
    double maxHeight = 800;
    double spacing = 20;
    double originX = (Screen.getPrimary().getVisualBounds().getWidth() / 2) - (maxWidth / 2);
    double originY = (Screen.getPrimary().getVisualBounds().getHeight() / 2) - (maxHeight / 2);
    double currX = originX;
    double currY = originY;
    double currHighest = 0;
    ArrayList<ArrayList<Word>> lines = new ArrayList<ArrayList<Word>>();
    ArrayList<Double> highest = new ArrayList<Double>();
    int currentLine = 0;
    lines.add(new ArrayList<Word>());
    
    for (Word word : wordObjects ){
     Text currText = new Text();   
     currText.setText(word.getText());
     currText.setFont(new Font(word.getFontSize()));
     double wordWidth = currText.getBoundsInLocal().getWidth();
     double wordHeight = currText.getBoundsInLocal().getHeight();
     
     if ((wordWidth + currX) <= maxWidth){
      //fits on the same line
         if (wordHeight > currHighest){
             currHighest = wordHeight;
         
         }
          currText.setX(currX);
          currText.setY(currY);
          currX = wordWidth + currX + spacing;
          //this.words.getChildren().add(currText);       
         ArrayList<Word> tmpLine = lines.get(currentLine);
         tmpLine.add(word);
         lines.add(currentLine, tmpLine);
         
     }else{
         if((wordHeight + currY) <= maxHeight){
             
             //start new line
         
             currY = wordHeight + currY;
             currX = originX;
             currText.setX(currX);
             currText.setY(currY);
             //this.words.getChildren().add(currText);
             currX += wordWidth + spacing;
              //add current highest to previous
           
            
             currentLine++;
             highest.add(currHighest);
             ArrayList<Word> tmpLine = new ArrayList<Word>();
             tmpLine.add(word);
             lines.add(currentLine, tmpLine);
             currHighest = 0;
           
             
             //need some kind of 'renderWord(x,y)' here
         }
         else{
             System.out.println("finished, because it doesn't fit at all!");
             break; //doesn't fit
         }
     }
    
    }
    
    int renderX = 0;
    int renderY = 0;
    for (int i = 0; i <= currentLine-1; i++){
       
           renderY += highest.get(i);  
        
       
        renderX = 0;
        for (Word word : lines.get(i)){
             Text currText = new Text();   
             currText.setText(word.getText());
             currText.setFont(new Font(word.getFontSize()));
             double wordWidth = currText.getBoundsInLocal().getWidth();
             currText.setX(renderX);
             currText.setY(renderY);
             cloud.getChildren().add(currText);
             renderX += (wordWidth + spacing) ;
        }
    }
    return cloud;
    //this.words.getChildren().add(r);
   
 }
}