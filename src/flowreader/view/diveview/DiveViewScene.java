/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import flowreader.model.Document;
import flowreader.model.Page;
import flowreader.model.WordCloud;
import flowreader.view.RibbonPane;
import flowreader.view.WordCloudPane;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author D-Day
 */
public class DiveViewScene extends StackPane{
    private ArrayList<RibbonPane> levels;
    private int currentLevel;
    
    private EventHandler<MouseEvent> swipeHandler;
    
    public DiveViewScene(Document document){
        ArrayList<ArrayList<WordCloud>> wordClouds = document.getWordClouds();
        ArrayList<Page> pages = document.getPages();
        
        double x=0, y=0;
        this.levels = new ArrayList<>();
        // Creation of the pages
        PagesPane pp = new PagesPane(pages, x, y);
        this.levels.add(pp);
        
        // Creation of the word clouds
        for(int i = 0; i<wordClouds.size(); i++){
            WordCloudPane wcp = new WordCloudPane(wordClouds.get(i), x, y);
            x = x+ (wcp.getGroupWidth()/4);
            this.levels.add(wcp);
        }
        this.currentLevel = this.levels.size()-1;
        this.getChildren().add(this.levels.get(this.currentLevel));
        
        this.defineEvents();
        this.setEvents();
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

                        TranslateTransition tt = new TranslateTransition(Duration.millis(100), DiveViewScene.this);
                        tt.setByX(dx);
                        tt.setByY(dy);
                        tt.setCycleCount(0);
                        tt.setAutoReverse(true);
                        tt.play();
                } else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

                    if(event.getClickCount()==2){
                        if(event.getButton().equals(MouseButton.PRIMARY) && DiveViewScene.this.currentLevel != 0){
                            DiveViewScene.this.currentLevel -=1;
                            DiveViewScene.this.getChildren().clear();
                            DiveViewScene.this.getChildren().add(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel));
                        }
                        else if(event.getButton().equals(MouseButton.SECONDARY) && DiveViewScene.this.currentLevel != DiveViewScene.this.levels.size()-1){
                            DiveViewScene.this.currentLevel +=1;
                            DiveViewScene.this.getChildren().clear();
                            DiveViewScene.this.getChildren().add(DiveViewScene.this.levels.get(DiveViewScene.this.currentLevel));
                        }
                        
                        
                    }
                      
                }
                previousEvent = event;
                event.consume();
            }
        };
    }
    
    private void move(double dX, double dY) {
        for(RibbonPane rp : this.levels){
            rp.move(dX, dY);
        }
    }
    
    private void setEvents() {
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, swipeHandler);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, swipeHandler);
    }
    
}
