/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.view.MainView;
import flowreader.view.RibbonView;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

/**
 *
 * @author D-Day
 */
public class DocumentCreationTask extends Task{
    private Document document = null;
    private TextFileReader fileReader;
    private MainView mv;

    public DocumentCreationTask(ProgressIndicator pi, TextFileReader fileReader, MainView mv) {
        this.fileReader = fileReader;
        this.mv = mv;
    }
    
    @Override
    protected Document call() throws Exception {
        document = (Document) fileReader.get();
        RibbonView ribbon = new RibbonView(document);
        mv.docOpenned(document, ribbon);
        return this.document;
    }
    
}