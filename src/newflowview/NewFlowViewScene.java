/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newflowview;

import flowreader.model.Document;
import flowreader.model.WordCloud;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;

/**
 *
 * @author D-Day
 */
public class NewFlowViewScene extends StackPane{
    private NewFlowView rb;
    private Document document;
    
    public NewFlowViewScene(Document document){
        ArrayList<ArrayList<WordCloud>> wordClouds = new ArrayList<>();
        wordClouds.add(document.getWordClouds().get(0));
        for(ArrayList<WordCloud> alwc: document.getWordClouds()){
            wordClouds.add(alwc);
        }
        this.document = new Document(document.getPages(), wordClouds);
        rb = new NewFlowView(this);
        this.build();
    }
    
    public void build(){
       rb.buildRibbon(document);
       this.getChildren().add(rb);
    }
    
}

    