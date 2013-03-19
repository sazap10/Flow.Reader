/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.view.MainView;
import flowreader.view.RibbonView;
import flowreader.view.TextPageView;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * Task that creates a document and make the mainview react when the creation of the document is finished
 * @author D-Day
 */
public class DocumentCreationTask extends Task {

    private Document document = null;
    private FileReader fileReader;
    private MainView mv;
    private boolean split_version;
    FlowReader flowReader;

    public DocumentCreationTask(FlowReader flowReader,FileReader fileReader, MainView mv, Boolean split_version) {
        this.fileReader = fileReader;
        this.flowReader = flowReader;
        this.mv = mv;
        this.split_version = split_version;
    }

    @Override
    protected Document call() throws Exception {
        document = fileReader.readFile(Parameters.textBoundWidth, Parameters.textBoundHeight);
        RibbonView ribbon = new RibbonView(flowReader,document, split_version);
        mv.docOpenned(document, ribbon);
        mv.homeButton.fire();
        return this.document;
    }
}
