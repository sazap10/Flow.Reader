/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import flowreader.FlowReader;
import flowreader.model.Document;
import flowreader.view.MainView;
import flowreader.view.RibbonView;
import javafx.concurrent.Task;

/**
 * Task that creates a document and make the MainView react when the creation of the document is finished
 * @author D-Day
 */
public class DocumentCreationTask extends Task {

    private Document document = null;
    private FileReader fileReader;
    private MainView mainView;
    private boolean splitVersion;
    FlowReader flowReader;

    public DocumentCreationTask(FlowReader flowReader,FileReader fileReader, MainView mv, Boolean split_version) {
        this.fileReader = fileReader;
        this.flowReader = flowReader;
        this.mainView = mv;
        this.splitVersion = split_version;
    }

    @Override
    protected Document call() throws Exception {
        document = fileReader.readFile(Parameters.TEXT_BOUND_WIDTH, Parameters.TEXT_BOUND_HEIGHT);
        RibbonView ribbon = new RibbonView(flowReader,document, splitVersion);
        mainView.docOpenned(document, ribbon);
        mainView.homeButton.fire();
        return this.document;
    }
}
