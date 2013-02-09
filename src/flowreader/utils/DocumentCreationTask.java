/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.model.Document;
import flowreader.view.MainView;
import flowreader.view.PageView;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

/**
 *
 * @author D-Day
 */
public class DocumentCreationTask extends Task{
    private Document document = null;
    private ProgressIndicator pi;
    private TextFileReader fileReader;
    private MainView mv;

    public DocumentCreationTask(ProgressIndicator pi, TextFileReader fileReader, MainView mv) {
        this.pi = pi;
        this.fileReader = fileReader;
        this.mv = mv;
    }
    
    @Override
    protected Document call() throws Exception {
        System.out.println("avant");
        //pi.progressProperty().bind(fileReader.progressProperty());
        System.out.println("apres");
        //Document docu = readFile(PageView.textBoundWidth, PageView.textBoundHeight);
        System.out.println("pass");
        
        document = (Document) fileReader.get();
        mv.docOpenned(document);
        return this.document;
    }
    
}
