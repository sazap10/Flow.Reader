package project_1st_attempt;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

 
public class WordWrap extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public ArrayList<Text> readFile(Rectangle bound){
    	ArrayList<Text> pages = new ArrayList<Text>();
    	double boundWidth = bound.getWidth();
    	double boundHeight = bound.getHeight();
    	double spaceLeft = boundWidth;
    	double spaceWidth = new Text(" ").getBoundsInLocal().getWidth();
    	double lineHeight = new Text("").getBoundsInLocal().getWidth();
    	pages.add( new Text(""));
    	//File file = new File("./test.txt");
    	int i = 0;
		try {
			LineNumberReader r = new LineNumberReader(new FileReader("test.txt"));
			String l, s;
			Text temp= new Text();
			while ((l = r.readLine()) != null) {
				Scanner sc = new Scanner(l);			
		    	while(sc.hasNext())
		    	{
		    		s=sc.next();
		    		temp.setText(s);
		    		double wordWidth = temp.getBoundsInLocal().getWidth();
		    		if(wordWidth+spaceWidth>spaceLeft){
		    			if(pages.get(i).getBoundsInLocal().getHeight()+lineHeight>boundHeight){
		    				pages.add(new Text());
		    				i++;
		    			}else{
		    				pages.get(i).setText(pages.get(i).getText()+"\n");
		    			}
		    			spaceLeft = boundWidth-wordWidth;
		    		}else{
		    			spaceLeft = spaceLeft - (wordWidth + spaceWidth);
		    		}
		    		pages.get(i).setText(pages.get(i).getText()+s+ " ");
		    	}
			    if(!((pages.get(i).getBoundsInLocal().getHeight()+lineHeight)>boundHeight)){
			    	pages.get(i).setText(pages.get(i).getText()+"\n");
			    	spaceLeft = boundWidth;
		    	}
		    	sc.close();
	
			}
			r.close();
		} catch (IOException e) {
				e.printStackTrace();
		}
    	return pages;
    }
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        final Rectangle r = new Rectangle(400,500);
        final StackPane root = new StackPane();
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setHgap(4);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        ArrayList<Text> pages = readFile(r);
        	flow.getChildren().addAll(pages);
        root.getChildren().add(flow);
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        
    }
}