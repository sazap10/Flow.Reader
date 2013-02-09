/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.view.diveview.DiveViewScene;
import flowreader.view.flowview.FlowViewScene;
import flowreader.view.seamlessview.TheViewScene;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author D-Day
 */
public class RibbonView extends StackPane {

        private PagesScene pagesPane;
        private WordCloudsScene wordCloudsPane;
        private FlowViewScene flowViewPane;
        private DiveViewScene diveViewPane;
        private TheViewScene theViewPane;
        public RibbonView(){
            
        }

	public RibbonView(Document document) {
            //this.pagesPane = new PagesScene(document.getPages());
            //this.wordCloudsPane = new WordCloudsScene(document.getWordClouds());
            //this.flowViewPane = new FlowViewScene(document);
            this.diveViewPane = new DiveViewScene(document);
            this.theViewPane = new TheViewScene(pagesPane,wordCloudsPane);

            this.getChildren().add(this.diveViewPane);
	}

	public void switchToWordCloud() {
            this.getChildren().clear();
            this.getChildren().add(this.wordCloudsPane);
            this.wordCloudsPane.setEvents(true);
	}

	public void switchToPages() {
            this.getChildren().clear();
            this.getChildren().add(this.pagesPane);
            this.pagesPane.setEvents(true);

	}
        
        public void switchToFlowView() {
            this.getChildren().clear();
            this.getChildren().add(this.flowViewPane);
	}

        public void switchToDiveView() {
            this.getChildren().clear();
            this.getChildren().add(this.diveViewPane);
        }
        
        public void switchToTheView() {
            this.getChildren().clear();
            this.getChildren().add(this.theViewPane);
            this.theViewPane.buildTheView();
        }
}
