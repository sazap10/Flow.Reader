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
public class DocumentCreationTask extends Task {

    private Document document = null;
    private FileReader fileReader;
    private MainView mv;
    private boolean split_version;

    public DocumentCreationTask(ProgressIndicator pi, FileReader fileReader, MainView mv, Boolean split_version) {
        this.fileReader = fileReader;
        this.mv = mv;
        this.split_version = split_version;

    }

    @Override
    protected Document call() throws Exception {
        document = (Document) fileReader.call();
        RibbonView ribbon = new RibbonView(document, split_version);
        mv.docOpenned(document, ribbon);
        return this.document;
    }
}
