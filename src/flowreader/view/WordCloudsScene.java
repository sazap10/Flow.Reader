/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class WordCloudsScene extends StackPane {
    private ArrayList<WordCloudPane> wordCloudPanes;

    private int maxZoom;
    private int curZoom;
    private int minZoom = 0;
    
    	int maxScale = 100;
	int minScale = 0;
	int curScale = 80;
        	double[] array = new double[maxScale + 1];

    private int numberOfOpacitylevels = 4;
    
    private EventHandler<MouseEvent> swipeHandler;
    private EventHandler<ScrollEvent> scrollHandler;
    private EventHandler<ZoomEvent> zoomHandler;
    
    public WordCloudsScene(ArrayList<ArrayList<WordCloud>> wordClouds){
        // Creation of the word clouds
        this.wordCloudPanes = new ArrayList<>();
        double x=0, y=0;
        for(int i = 0; i<wordClouds.size(); i++){
            WordCloudPane wcp = new WordCloudPane(wordClouds.get(i), x, y);
            x = x+ (wcp.getGroupWidth()/4);
            this.wordCloudPanes.add(wcp);
        }
        
        this.getChildren().add(this.wordCloudPanes.get(this.wordCloudPanes.size()-1));
        
        // Create the zoom levels
        
        this.maxZoom = (wordClouds.size()-1)*(this.numberOfOpacitylevels);
        this.curZoom = this.maxZoom;
        
        		for (int j = 0; j <= maxScale; j++) {
			array[j] = Math.pow(1.05, j - 81);
		}
        this.defineEvents();
        this.setEvents();
    }
    
    public void zoom(double deltaY, double x, double y) {
        // Set the current zoom level
        if (deltaY <= 0) { // If scroll down
            if(this.curZoom>this.minZoom){
                this.curZoom--;
            }
        } 
        else{ // if we scroll up
            if(this.curZoom<this.maxZoom){
                this.curZoom++;
            }
        }
        this.getChildren().clear();
        //System.out.println("CurrentZoom "+this.curZoom+" - WordCloudLevel "+this.getWordCloudLevel(this.curZoom));
        if(this.getWordCloudLevel(this.curZoom)!=-1){ // If we are on a word cloud level
            if(this.curZoom == this.maxZoom){ // if this is the max word cloud level 
                this.getChildren().add(this.wordCloudPanes.get(0));
                this.wordCloudPanes.get(0).setOpacity(1);
            }
            else if(this.curZoom == this.minZoom){ // if this is the min word cloud level 
                this.getChildren().add(this.wordCloudPanes.get(this.wordCloudPanes.size()-1));
                this.wordCloudPanes.get(this.wordCloudPanes.size()-1).setOpacity(1);
            }
            else{ // if this is an other word cloud level
                //System.out.println(""+this.getWordCloudLevel(this.curZoom));
                this.getChildren().add(this.wordCloudPanes.get(this.getWordCloudLevel(this.curZoom)));
                this.wordCloudPanes.get(this.wordCloudPanes.size()-1).setOpacity(1);
            }
        }
        else{ // if we are between two levels
            int previous = this.getWordCloudLevel(this.getPreviousZoomWcLevel(this.curZoom));
            int next  = this.getWordCloudLevel(this.getNextZoomWcLevel(this.curZoom));
            this.getChildren().add(this.wordCloudPanes.get(previous));
            this.getChildren().add(this.wordCloudPanes.get(next));
            this.wordCloudPanes.get(previous).setOpacity(this.getOpacity(this.curZoom, this.getNextZoomWcLevel(this.curZoom)));
            this.wordCloudPanes.get(next).setOpacity(1-this.wordCloudPanes.get(previous).getOpacity());
        }
                
    }
	
    public void zoom_wordCloud(double deltaY, double x, double y) {
		if (deltaY <= 0) {
			if (curScale < minScale + 1) {
			} else {
				curScale--;
			}
		} else {
			if (curScale > maxScale - 1) {
			} else {
				curScale++;
			}
		}

		Scale scale = new Scale(array[curScale], array[curScale], x, y);

//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!"+stackPane.getTransforms().toString());
		
                this.getTransforms().clear();
		this.getTransforms().add(scale);

                                        
		/*FlowReader.zoomLabel.setText("zoom: "
				+ ((float) curScale / (float) maxScale) * 100 + "%\ncurScale: "
				+ curScale + "\nmin Scale: " + minScale + "\nmax Scale: "
				+ maxScale);*/
	}
    void setNewPosition(double posX, double posY) {
        for(WordCloudPane wcp : this.wordCloudPanes){
            wcp.setNewPosition(posX, posY);
        }
    }
    
    void move(double dX, double dY) {
        for(WordCloudPane wcp : this.wordCloudPanes){
            wcp.move(dX, dY);
        }
    }

    private int getWordCloudLevel(int zoomLevel) {
        int temp = this.minZoom;
        int i=this.wordCloudPanes.size()-1;
        while(temp<this.maxZoom && temp!=zoomLevel){
            temp = temp+this.numberOfOpacitylevels;
            i--;
        }
        if(temp==this.maxZoom && zoomLevel<this.maxZoom){
            i=-1;
        }
        return i;
    }

    private int getNextZoomWcLevel(int zoomLevel) {
        int temp = this.minZoom;
        int i=this.wordCloudPanes.size();
        while(temp<zoomLevel){
            temp = temp+this.numberOfOpacitylevels;
            i--;
        }
        return temp;
    }

    private int getPreviousZoomWcLevel(int zoomLevel) {
        int temp = this.maxZoom;
        int i=0;
        while(temp>zoomLevel){
            temp = temp-this.numberOfOpacitylevels;
            i++;
        }
        return temp;
    }

    private double getOpacity(int curZoom, int previous) {
        double dop = 1.0/(double)this.numberOfOpacitylevels;
        int delta = previous-curZoom;
        //System.out.println(""+dop);
        return delta*dop;
    }
    
        private void defineEvents() {
            swipeHandler = new EventHandler<MouseEvent>() {
                MouseEvent previousEvent;

                    @Override
                    public void handle(MouseEvent event) {
                            if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                                    previousEvent = event;
                                    // System.out.println("PRESSED");
                            } else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {

                                    // System.out.println("DRAGGED");
                                    double dx = event.getX() - previousEvent.getX();
                                    double dy = event.getY() - previousEvent.getY();
                                    move(dx, dy);
                                    
                                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), WordCloudsScene.this);
                                    tt.setByX(dx);
                                    tt.setByY(dy);
                                    tt.setCycleCount(0);
                                    tt.setAutoReverse(true);
                                    tt.play();
                            }
                            previousEvent = event;
                            event.consume();
                    }
            };
            
            scrollHandler = new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if(event.isControlDown()){
                            zoom_wordCloud(event.getDeltaY(),Screen.getPrimary().getBounds().getWidth()/2,Screen.getPrimary().getBounds().getHeight()/2);
                        }else{
                        if (!event.isDirect()) {
                                double height = WordCloudsScene.this.getLayoutBounds().getHeight();
                                double width = WordCloudsScene.this.getLayoutBounds().getWidth();
                                WordCloudsScene.this.zoom(event.getDeltaY(), event.getScreenX()/Screen.getPrimary().getBounds().getWidth()*width, event.getScreenY()/Screen.getPrimary().getBounds().getHeight()*height);
                        }
                        event.consume();
                    }}
            };

            zoomHandler = new EventHandler<ZoomEvent>() {
                    @Override
                    public void handle(ZoomEvent event) {
                        double delta = event.getZoomFactor() - 1;
                       WordCloudsScene.this.zoom(delta, event.getX(), event.getY());
                        event.consume();
                    }
            };
            
        }
        
        private void setEvents() {
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
            this.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
            this.addEventHandler(ZoomEvent.ZOOM, zoomHandler);
        }
}
