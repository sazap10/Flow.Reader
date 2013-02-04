/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import flowreader.model.Document;
import flowreader.view.flowview.FlowViewScene;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author D-Day
 */
public class RibbonView extends StackPane {

        private PagesScene pagesPane;
        private WordCloudsScene wordCloudsPane;
        private FlowViewScene flowViewPane;
        
        public RibbonView(){
            
        }

	public RibbonView(Document document) {
            this.pagesPane = new PagesScene(document.getPages());
            this.wordCloudsPane = new WordCloudsScene(document.getWordClouds());
            this.flowViewPane = new FlowViewScene(document);
            this.getChildren().add(this.pagesPane);
	}

	public void switchToWordCloud() {
            this.getChildren().clear();
            this.getChildren().add(this.wordCloudsPane);
	}

	public void switchToPages() {
            this.getChildren().clear();
            this.getChildren().add(this.pagesPane);
	}
        
        public void switchToFlowView() {
            this.getChildren().clear();
            this.getChildren().add(this.flowViewPane);
	}
}
