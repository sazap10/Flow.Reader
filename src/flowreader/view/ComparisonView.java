/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import java.util.ArrayList;

import flowreader.model.Page;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

/**
 * 
 * @author D-Day
 */
public class ComparisonView extends FlowPane {
	ArrayList<Page> pages;
	double pageWidth, pageHeight;
	private EventHandler<DragEvent> dragExitedHandler, dragEnteredHandler,
			dragOverHandler, dragDroppedHandler;

	public ComparisonView(int pageGap) {
		super(pageGap, 0);
		this.pageHeight =this.pageWidth = 0;
		pages = new ArrayList<Page>();
		defineDragEvent();
		this.setMinSize(Screen.getPrimary().getVisualBounds().getWidth(), 200);
		this.setStyle("-fx-background-color: #B8B8B8");
	}
	
	public void setPageSize(double pageWidth, double pageHeight){
		this.pageHeight = pageWidth;
		this.pageWidth = pageHeight;
	}

	/*public void createPage(String text) {
		PageView page = new PageView(new Rectangle(pageWidth, pageHeight));
		page.setText(text);
		page.setScaleX(0.25);
		page.setScaleY(0.25);
		this.getChildren().add(page);
	}*/

	private void defineDragEvent() {
		dragOverHandler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data is dragged over the target */
				System.out.println("dragOver");
				/*
				 * accept it only if it is not dragged from the same node and if
				 * it has a string data
				 */
				if (event.getGestureSource() != ComparisonView.this
						&& event.getDragboard().hasString()) {
					/* allow for both copying and moving, whatever user chooses */
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}

				event.consume();
			}
		};
		dragEnteredHandler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("dragEnter");
				/* show to the user that it is an actual gesture target */
				if (event.getGestureSource() != ComparisonView.this
						&& event.getDragboard().hasString()) {
					ComparisonView.this.setStyle("-fx-background-color: green");
				}

				event.consume();
			}
		};
		dragExitedHandler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("dragExit");
				/* mouse moved away, remove the graphical cues */
				ComparisonView.this.setStyle("-fx-background-color: #B8B8B8");

				event.consume();
			}
		};

		dragDroppedHandler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("dragDropped");
				/* if there is a string data on dragboard, read it and use it */
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					//createPage(db.getString());
					success = true;
				}
				/*
				 * let the source know whether the string was successfully
				 * transferred and used
				 */
				event.setDropCompleted(success);

				event.consume();
			}
		};
		/*
		 * bottom.setOnDragOver(new EventHandler <DragEvent>() {
		 * 
		 * if (event.getGestureSource() != bottom &&
		 * event.getDragboard().hasString()) {
		 * 
		 * event.acceptTransferModes(TransferMode.COPY_OR_MOVE); }
		 * 
		 * event.consume(); } });
		 */
		/*
		 * bottom.setOnDragEntered(new EventHandler <DragEvent>() { public void
		 * handle(DragEvent event) {
		 * 
		 * if (event.getGestureSource() != bottom &&
		 * event.getDragboard().hasString()) {
		 * bottom.setStyle("-fx-background-color: green"); }
		 * 
		 * event.consume(); } });
		 */
		/*
		 * bottom.setOnDragExited(new EventHandler <DragEvent>() { public void
		 * handle(DragEvent event) {
		 * bottom.setStyle("-fx-background-color: grey"); event.consume(); } });
		 */
		/*
		 * bottom.setOnDragDropped(new EventHandler <DragEvent>() { public void
		 * handle(DragEvent event) { Dragboard db = event.getDragboard();
		 * boolean success = false; if (db.hasString()) {
		 * createPage(db.getString()); success = true; }
		 * event.setDropCompleted(success);
		 * 
		 * event.consume(); } });
		 */
	}

	public void setDragEvents(boolean setFlag) {
		if (setFlag) {
			ComparisonView.this.addEventHandler(DragEvent.DRAG_DROPPED,
					dragDroppedHandler);
			ComparisonView.this.addEventHandler(DragEvent.DRAG_OVER,
					dragOverHandler);
			ComparisonView.this.addEventHandler(DragEvent.DRAG_ENTERED,
					dragEnteredHandler);
			ComparisonView.this.addEventHandler(DragEvent.DRAG_EXITED,
					dragExitedHandler);
		}else{
			ComparisonView.this.removeEventHandler(DragEvent.DRAG_DROPPED,
					dragDroppedHandler);
			ComparisonView.this.removeEventHandler(DragEvent.DRAG_OVER,
					dragOverHandler);
			ComparisonView.this.removeEventHandler(DragEvent.DRAG_ENTERED,
					dragEnteredHandler);
			ComparisonView.this.removeEventHandler(DragEvent.DRAG_EXITED,
					dragExitedHandler);
		}
	}

}
