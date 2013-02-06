/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view.diveview;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public abstract class DiveRibbonPane extends StackPane {

    protected Group ribbon;
    protected ArrayList<DiveRibbonElement> ribbonElts;
    protected int elementInterval = 5;
    protected double elementWidth;
    protected double elementHeight;
    private ArrayList<Integer> selected;

    public DiveRibbonPane(double x, double y, double elementWidth, double elementHeight) {
        this.ribbon = new Group();
        this.ribbonElts = new ArrayList<>();
        this.ribbon.setLayoutX(x);
        this.ribbon.setLayoutY(y);
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.selected = new ArrayList<>();
        this.elementSelection();
        this.elementHovering();
    }
    
    public void createRibbon(ArrayList<Integer> indexes){
        /*String s = "new group : ";
        for(Integer i: indexes){
            s+=i+" ";
        }
        System.out.println(s);*/
        this.ribbon.getChildren().clear();
        for(Integer i : indexes){
            this.ribbon.getChildren().add(this.ribbonElts.get(i));
        }
        this.selected = indexes;
    }

    public double getRibbonLayoutX(){
        return this.ribbon.getLayoutX();
    }

    public void setNewPosition(double posX, double posY) {
        this.ribbon.setLayoutX(posX);
        this.ribbon.setLayoutY(posY);
    }

    public void move(double dX, double dY) {
        this.ribbon.setLayoutX(this.ribbon.getLayoutX() + dX);
        this.ribbon.setLayoutY(this.ribbon.getLayoutY() + dY);
    }

    public double getRibbonWidth() {
        return this.ribbon.getBoundsInLocal().getWidth();
    }

    private void elementHovering() {
        EventHandler<MouseEvent> hoveringHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_MOVED)) {
                        DiveRibbonPane.this.highlightHovered();
                }
            }
        };

        this.addEventHandler(MouseEvent.MOUSE_MOVED, hoveringHandler);
    }
    
    private void elementSelection() {
        EventHandler<MouseEvent> selectionHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    if (event.getClickCount() == 1) {
                        DiveRibbonPane.this.highlightSelected();
                    }
                }
            }
        };


        this.addEventHandler(MouseEvent.MOUSE_CLICKED, selectionHandler);
    }

    public void highlightSelected() {
        this.selected.clear();
        for(int i=0; i<this.ribbonElts.size(); i++){
            DiveRibbonElement dre = this.ribbonElts.get(i);
            if(dre.isHover()){
                dre.setHighlight(true);
                this.selected.add(i);
            }
            else{
                dre.setHighlight(false);
            }
        }
    }
    
    public void highlightHovered() {
        for(int i=0; i<this.ribbonElts.size(); i++){
            DiveRibbonElement dre = this.ribbonElts.get(i);
            if(dre.isHover()){
                dre.setHighlight(true);
            }
            else{
                dre.setHighlight(false);
            }
        }
    }
    
    public void highlightIndex(int index) {
        this.ribbonElts.get(index).setHighlight(true);
        this.selected.add(index);
    }
    
    public void highlightAll(boolean on) {
        this.selected.clear();
        for(int i=0; i<this.ribbonElts.size(); i++){
            this.ribbonElts.get(i).setHighlight(on);
            if(on){
                this.selected.add(i);
            }
        }
    }
    
    public ArrayList<Integer> getSelectedIndexes(){
        return this.selected;
    }
    
    public int getNumberOfElements(){
        return this.ribbonElts.size();
    }
    
    public double getFocusPoint(){
        
        double focusSquareWidth = (this.elementWidth*this.ribbon.getChildren().size())+(this.elementInterval*(this.ribbon.getChildren().size()));
        double focusPointInSquare = focusSquareWidth/2.0;
        
        return focusPointInSquare;
    }
}