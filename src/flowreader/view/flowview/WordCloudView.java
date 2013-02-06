package flowreader.view.flowview;

import flowreader.model.WordCloud;
import flowreader.utils.ValueComparatorInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class WordCloudView extends Group{

    private Rectangle wordCloudBoundary;
    private WordCloud wordCloud;
    private ArrayList<Word> wordObjects; //string with font size pairing
    private ArrayList<Text> words;
    private FlowPane cloud;
    private Integer maxFontSize = 30;
    private Integer minFontSize = 14;
    private Integer numOfWordsInCloud = 10;
    private Integer normalizationConstant = 1;

    public WordCloudView( WordCloud wordCloud, Rectangle boundary, int level) {
        wordCloudBoundary = boundary;
        wordCloudBoundary.setFill(Color.ALICEBLUE);
        this.wordCloud = wordCloud;
        this.words = new ArrayList<>();
        this.wordObjects = new ArrayList<Word>();
        this.cloud = new FlowPane();
        this.maxFontSize *= level;
        this.minFontSize *= level;
        //this.cloud.setLayoutX(wordCloudBoundary.getX());
        
        this.getChildren().addAll(wordCloudBoundary, cloud);     
        createWordObjects();      
        renderWordCloud();       
    }
    
  
    
    public double getPageWidth() {
        return wordCloudBoundary.getWidth();
    }

    public double getPageHeight() {
        return wordCloudBoundary.getHeight();
    }
    
    public void setPageWidth(double width) {
        wordCloudBoundary.setWidth(width);
    }

    public void setPageHeight(double height) {
        wordCloudBoundary.setHeight(height);
    }

    public void setX(double x) {
        wordCloudBoundary.setX(x);
       

    }

    public double getX() {
        return wordCloudBoundary.getX();
    }

   
   public void createWordObjects(){
        Word word;
        int count = 0;
          
        Set<Map.Entry<String, Integer>> w = wordCloud.getSortedWordOccurrences().entrySet();

        Iterator i = w.iterator();
        while (i.hasNext() && (count < numOfWordsInCloud)){
            
             Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)i.next();
             
             int fontSize = getFontSize(e.getValue());
             word = new Word(fontSize, e.getKey());
             this.wordObjects.add(word);
             count++;
        }
   }

    
   /* void renderWordCloud(){
        TreeMap<String, Integer> sortedWordOccurrences = sortWordsOccurrences(wordCloud.getWordOccurrences());
        Set<Map.Entry<String, Integer>> w = sortedWordOccurrences.entrySet();
        Iterator i = w.iterator();
        int j = 0;
        while(j<this.numOfWordsInCloud && i.hasNext()) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>)i.next();
            Text word = new Text(e.getKey());
            //this.setWordSizes(word, e.getValue());
            word.setFont(new Font(30));
            word.setWrappingWidth(word.getLayoutBounds().getWidth()+10);
            this.words.add(word);
            j++;
	}
        Collections.shuffle(this.words);
        this.cloud.getChildren().addAll(this.words);
    }
*/    
    
     

   //very big function for rendering the wordclouds
    //(returns a group containing the rendered words;

      public void renderWordCloud(){

   //ArrayList<Integer> indexes = new ArrayList<Integer>();
   // ArrayList<Word> wordObjects = new ArrayList<Word>();
   //  for (int i = 0; i <= wordObjects.size() -1; i ++){
  ////       indexes.add(i, i);

   //  }

   Collections.shuffle(wordObjects);
 	
  //  System.out.println("PRINTING INDEXES!");
  //   System.out.println(indexes);
   //create the basic rectangle (usually in terms of class attributes)	
       
   //these would normally be inputs to the function

   double maxWidth = wordCloudBoundary.getWidth();	
   double maxHeight = wordCloudBoundary.getHeight();
   double spacing = 10;	
   double currX = wordCloudBoundary.getX();	
   double currY = wordCloudBoundary.getY();
   double currHighest = 0;
   System.out.println("originx: " + currX);
   System.out.println("originy: " + currY);
   
   //create a  list to store a list for each line of the cloud
   ArrayList<ArrayList<Word>> lines = new ArrayList<ArrayList<Word>>();	
   ArrayList<Double> highest = new ArrayList<Double>();	
   
   int currentLine = 0;
   
   //add the first line
   lines.add(new ArrayList<Word>());
	
  for (Word word : wordObjects ){
    //set the text and font size for a text object
    Text currText = new Text();   	
    currText.setText(word.getText());	
    currText.setFont(new Font(word.getFontSize()));
    
    //get the dimensions of the text object
    double wordWidth = currText.getBoundsInLocal().getWidth();
    double wordHeight = currText.getBoundsInLocal().getHeight();
    
    //if too long for the line, start a new line
     if ((wordWidth + currX) <= (wordCloudBoundary.getX() + maxWidth)){
	
      //fits on the same line
         if (wordHeight > currHighest){
             currHighest = wordHeight;
	
         }

          currText.setX(currX);	
          currText.setY(currY);
         currX = wordWidth + currX + spacing;
	
         ArrayList<Word> tmpLine = lines.get(currentLine);
	
         tmpLine.add(word);	
         lines.add(currentLine, tmpLine);
	
    }else{
       //  System.out.println("Starting a new Line");
         //if too tall to start a new line, finish
         if((wordHeight + currY) <= maxHeight){
 	
             //start new line
	
             currY = wordHeight + currY;
             currX = wordCloudBoundary.getX();
            // System.out.println("currX on new line: " + currX);
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
         
         //if too big to start a new line, finish
         else{
 	
            //System.out.println("finished, because it doesn't fit at all!");
            break; //doesn't fit
 
         }
	
     }

   }
	
    double renderX;
    double renderY = wordCloudBoundary.getY();
    for (int i = 0; i <= currentLine-1; i++){
        renderY += highest.get(i);  
        renderX = wordCloudBoundary.getX();
 	
        for (Word word : lines.get(i)){
             Text currText = new Text();   
             currText.setText(word.getText());
             currText.setFont(new Font(word.getFontSize()));
             double wordWidth = currText.getBoundsInLocal().getWidth();
             //System.out.println("renderX set to " + renderX);
             currText.setX(renderX);
             currText.setY(renderY);
             this.getChildren().add(currText);
             renderX += (wordWidth + spacing) ;
             //System.out.println("renderX noew set to " + renderX);
       }
 
   }


    //this.words.getChildren().add(r);

 }
    //iterates through the word objects and assigns them a font size
    private int getFontSize(int numberOfOccurrences) {
        int countDiff = numberOfOccurrences - this.wordCloud.getSortedWordOccurrences().lastEntry().getValue();
        if (countDiff == 0){
            countDiff = 1;
        }
       
        int totalCountDiff = this.wordCloud.getSortedWordOccurrences().firstEntry().getValue() - this.wordCloud.getSortedWordOccurrences().lastEntry().getValue();      
        if (totalCountDiff == 0){
            totalCountDiff = 1;
        }
        int fontSize = ((this.maxFontSize * countDiff) / (this.normalizationConstant * totalCountDiff));
        if (fontSize < this.minFontSize) {
            fontSize = this.minFontSize;
        }
        return fontSize;
    }

    
     private TreeMap<String, Integer> sortWordsOccurrences(HashMap<String, Integer> wordsOccurrences){
        ValueComparatorInteger bvc =  new ValueComparatorInteger(wordsOccurrences);
        TreeMap<String,Integer> sortedWordsOccurrences = new TreeMap<>(bvc);
        sortedWordsOccurrences.putAll(wordsOccurrences);
        
        //System.out.println("unsorted map: "+wordsOccurrences);
        //System.out.println("results: "+sortedWordsOccurrences);
       
        return sortedWordsOccurrences;
    }
}