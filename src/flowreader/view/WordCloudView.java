/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

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
 * Visual representation of a word cloud
 *
 * @author D-Day
 */
public class WordCloudView extends RibbonElement {

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
    private FlowView nfv;
    private boolean flowview = false;

    public WordCloudView(WordCloud wc, double x, double y, double elementWidth, double elementHeigth) {
        wordCloudBoundary = new Rectangle(x, y, elementWidth, elementHeigth);
        wordCloudBoundary.setFill(Color.TRANSPARENT);
        this.wordCloud = wc;
        this.words = new ArrayList<Text>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());

        this.getChildren().addAll(wordCloudBoundary, cloud);
        renderWordCloud();
    }

    public WordCloudView(WordCloud wc, double x, double y, double elementWidth, double elementHeigth, int level, FlowView nfv) {
        wordCloudBoundary = new Rectangle(x, y, elementWidth, elementHeigth * 2);
        wordCloudBoundary.setFill(Color.TRANSPARENT);
        //wordCloudBoundary.setOpacity(0.5);
        this.wordCloud = wc;
        this.words = new ArrayList<Text>();
        this.cloud = new FlowPane();
        this.cloud.setLayoutX(wordCloudBoundary.getX());
        this.cloud.setLayoutY(wordCloudBoundary.getY());
        this.cloud.setPrefWrapLength(elementWidth);
        this.level = level;
        this.nfv = nfv;

        this.getChildren().addAll(wordCloudBoundary, cloud);
        cloud.setAlignment(Pos.TOP_CENTER);
        flowview = true;
        if (flowview) {
            setNumOfWordsInCloud(level);
        }
        renderWordCloud();
    }

    /**
     * Set the numbers of words that you want to display in the word cloud
     *
     * @param level
     */
    public void setNumOfWordsInCloud(int level) {
        numOfWordsInCloud = level * 3;
    }

    /**
     * Creates the word cloud visualisation
     */
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
            word.setFill(gradedValue(col2, col1, ((double) j / this.numOfWordsInCloud.doubleValue())));
            this.words.add(word);
            j++;
        }

        for (int k = 0; k < values.length; k++) {
            this.setWordSize(this.words.get(k), values[k], values);
            this.words.get(k).setWrappingWidth(this.words.get(k).getLayoutBounds().getWidth() + 20);
        }

        Collections.shuffle(this.words);
        this.cloud.getChildren().addAll(this.words);
    }

    /**
     * @param beginColor
     * @param endColor
     * @param percent
     * @return a color that is between begincolor and endcolor by percent
     */
    private Color gradedValue(Color beginColor, Color endColor, double percent) {
        double red = beginColor.getRed() + (double) (percent * (endColor.getRed() - beginColor.getRed()));
        double blue = beginColor.getBlue() + (double) (percent * (endColor.getBlue() - beginColor.getBlue()));
        double green = beginColor.getGreen() + (double) (percent * (endColor.getGreen() - beginColor.getGreen()));
        return new Color(red, green, blue, 1);
    }

    /**
     * assigns them a font size to a word object
     *
     * @param word
     * @param wordFrequency
     * @param values
     */
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
            currentFontSize = (int) ((adjustCurrentFontSize + this.minFontSize) * getFontSize(level));
        }
        word.setFont(new Font(currentFontSize));
    }

    /**
     * @param level
     * @return the font size corresponding to the level
     */
    public double getFontSize(int level) {
        double maximumFontSize = 6;
        int maxWordCloudLevel = nfv.getMaxZoomLevel();
        if (maxWordCloudLevel < maximumFontSize + 1) {
            maximumFontSize = maxWordCloudLevel - 1;
        }
        double step = maximumFontSize / maxWordCloudLevel;
        double result = (int) Math.pow(2, level * step);
        return result;
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