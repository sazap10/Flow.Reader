/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.view.RibbonElement;
import flowreader.model.WordCloud;
import flowreader.view.flowview.FlowView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class DiveWordCloud extends RibbonElement {

    private Rectangle wordCloudBoundary;
    private WordCloud wordCloud;
    private ArrayList<Text> words;
    private FlowPane cloud;
    private Integer maxFontSize = 90;
    private Integer minFontSize = 14;
    private Integer numOfWordsInCloud = 16;
    public static double width = 500;
    public static double heigth = 500;
    public int level = 1;
    private int maxFontLevel = 7;
    private FlowView nfv;
    private boolean flowview = false;

    public DiveWordCloud(WordCloud wc, double x, double y, double elementWidth, double elementHeigth) {
        wordCloudBoundary = new Rectangle(x, y, elementWidth, elementHeigth);
        wordCloudBoundary.setFill(Color.TRANSPARENT);
        this.wordCloud = wc;
        this.words = new ArrayList<Text>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());

        this.getChildren().addAll(wordCloudBoundary, cloud);
        renderWordCloud();
    }

    public DiveWordCloud(WordCloud wc, double x, double y, double elementWidth, double elementHeigth, int level, FlowView nfv) {
  wordCloudBoundary = new Rectangle(x, y, elementWidth, elementHeigth * 2);
        wordCloudBoundary.setFill(Color.TRANSPARENT);
        //wordCloudBoundary.setOpacity(0.5);
this.wordCloud = wc;
        this.words = new ArrayList<Text>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());
        this.cloud.setLayoutY(wordCloudBoundary.getY());

        this.cloud.setMinWidth(elementWidth);
       this.cloud.setPrefWidth(elementWidth);
       this.cloud.setMaxWidth(elementWidth);

        this.cloud.setMinHeight(elementHeigth);
        this.cloud.setPrefHeight(elementHeigth);
        this.cloud.setMaxHeight(elementHeigth);
 this.cloud.setPrefWrapLength(elementWidth);
        this.level = level;
        this.nfv = nfv;

        this.getChildren().addAll(wordCloudBoundary, cloud);
        cloud.setAlignment(Pos.TOP_CENTER);
        flowview = true;
setNumOfWordsInCloud(level);
         renderWordCloud();
     }
    
    public void setNumOfWordsInCloud(int level){
        numOfWordsInCloud = level*4;
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
        System.out.println("x: " + x + "pageBoundary Width: " + wordCloudBoundary.getWidth());

    }

    public double getX() {
        return wordCloudBoundary.getX();
    }

    private void renderWordCloud() {
        Set<Map.Entry<String, Integer>> w = this.wordCloud.getSortedWordOccurrences().entrySet();
        Integer[] values;
        Color col1 = Color.BLUE;
        Color col2 = Color.RED;
        if (w.size() > this.numOfWordsInCloud) {
            values = new Integer[this.numOfWordsInCloud];
        } else {
            values = new Integer[w.size()];
        }

        Iterator i = w.iterator();
        int j = 0;
        while (j < this.numOfWordsInCloud && i.hasNext()) {
            Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) i.next();
            values[j] = e.getValue();
            Text word = new Text(e.getKey());
            //col = col.brighter();
            //System.out.println(""+col.getBlue());
            //col = new Color(col.getRed(), col.getGreen()-(1.0/(this.numOfWordsInCloud.doubleValue()+1)), col.getBlue()- (1.0/(this.numOfWordsInCloud.doubleValue()+1)), 1);

            word.setFill(gradedValue(col2, col1, ((double) j / this.numOfWordsInCloud.doubleValue())));
            this.words.add(word);
            j++;
        }

        //System.out.println("New Cloud");
        for (int k = 0; k < values.length; k++) {
            this.setWordSize(this.words.get(k), values[k], values);
            this.words.get(k).setWrappingWidth(this.words.get(k).getLayoutBounds().getWidth() + 20);
            //System.out.println(this.words.get(k).getText()+" "+values[k]);
        }

        Collections.shuffle(this.words);
        //this.wordCloudBoundary.setWidth(500);
        //this.wordCloudBoundary.setHeight(500);
        //this.wordCloudBoundary.setArcHeight(20);
        //this.wordCloudBoundary.setArcWidth(20);
        //this.wordCloudBoundary.setOpacity(0.5);

        this.cloud.getChildren().addAll(this.words);
    }

    private Color gradedValue(Color beginColor, Color endColor, double percent) {
        double red = beginColor.getRed() + (double) (percent * (endColor.getRed() - beginColor.getRed()));
        double blue = beginColor.getBlue() + (double) (percent * (endColor.getBlue() - beginColor.getBlue()));
        double green = beginColor.getGreen() + (double) (percent * (endColor.getGreen() - beginColor.getGreen()));
        return new Color(red, green, blue, 1);
    }

    //assigns them a font size to a word object
    private void setWordSize(Text word, int wordFrequency, Integer[] values) {
        int current = wordFrequency - values[values.length - 1];

        int max = values[0] - values[values.length - 1];
        int percent;
        if (max != 0) {
            percent = (current * 100) / max;
        } else {
            percent = 50;
        }

        int adjustMaxFontSize = this.maxFontSize - this.minFontSize;
        int adjustCurrentFontSize = (percent * adjustMaxFontSize) / 100;
        int currentFontSize = (adjustCurrentFontSize + this.minFontSize) * (int) Math.pow(2, (level - 1));
        if (flowview) {
            currentFontSize = (int)((adjustCurrentFontSize + this.minFontSize) * getFontSize(level));
            //System.out.println("DDD "+calculateFontSizeFromLevel(level));
            //currentFontSize = (adjustCurrentFontSize + this.minFontSize) * (int) Math.pow(2, calculateFontSizeFromLevel(level));
        }
        word.setFont(new Font(currentFontSize));
    }
    public double getFontSize(int level){
        double maximumFontSize =6;
        int maxWordCloudLevel = nfv.getMaxZoomLevel();
                if (maxWordCloudLevel < maximumFontSize + 1) {
            maximumFontSize = maxWordCloudLevel -1;
        }
            double step = maximumFontSize / maxWordCloudLevel;
           double result = (int) Math.pow(2, level * step);
         
                //System.out.println(level);

        //System.out.println(step);
        
        //System.out.println(result);
        return result;
        
    }
    public double calculateFontSizeFromLevel(int level) {
        //System.out.println(":o"+level+" "+nfv.getMaxZoomLevel()+" "+maxFontLevel);

        return (((double) (level - 1) / (double) nfv.getMaxZoomLevel()) * (double) maxFontLevel);
    }

    @Override
    public void setHighlight(boolean on) {
        if (on) {
            this.setOpacity(1);
        } else {
            this.setOpacity(0.3);
        }

    }
}
