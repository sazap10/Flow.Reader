/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader;

import flowreader.core.Page;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import flowreader.data.TextFileReader;
import flowreader.data.TextFileReader_WordCloud;
import flowreader.view.RibbonView;
import flowreader.view.WordCloudView;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author D-Day
 */
public class FlowReader extends Application {

	// Background
	private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	RibbonView ribbon;
	WordCloudView wordCloud;
	TextFileReader fileReader;
            TextFileReader_WordCloud fileReader_WordCloud;

	private Button minBtn, closeBtn, openFileButton, wordCloudButton,
			diffModeBtn;
	private VBox btnsBar;
	private StackPane stackPane;
	boolean wordCloudToggle, diffModeToggle;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Flow Reader");
		primaryStage.setFullScreen(true);
        fileReader_WordCloud = new TextFileReader_WordCloud();

		wordCloud = new WordCloudView(fileReader_WordCloud);
		wordCloudToggle = diffModeToggle = false;
		// ribbon = new RibbonView(wordCloud);
		fileReader = new TextFileReader(fileReader_WordCloud);

		setUpButtons();

		setUpButtonBar();

		BorderPane borderPane = new BorderPane();
		stackPane = new StackPane();

		borderPane.setCenter(stackPane);
		borderPane.setTop(btnsBar);

		BorderPane.setAlignment(stackPane, Pos.CENTER_LEFT);
		Scene scene = new Scene(borderPane, screenBounds.getWidth(),
				screenBounds.getHeight());
		scene.getStylesheets()
				.add(FlowReader.class.getResource("stylesheet.css")
						.toExternalForm());
		this.setButtonEvents(primaryStage);
		this.setSceneEvents(scene);

		primaryStage.getIcons().add(
				new Image(this.getClass().getResource("logo.png")
						.toExternalForm()));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
     *
     */
	private void setUpButtons() {
		closeBtn = new Button("x");
		closeBtn.setId("closeBtn");

		minBtn = new Button("_");
		minBtn.setId("minBtn");

		openFileButton = new Button("Open file");
		openFileButton.setId("openFileBtn");
		wordCloudButton = new Button("Word Cloud View");
		wordCloudButton.setId("wordCloudBtn");
		wordCloudButton.setDisable(true);

		diffModeBtn = new Button("Drag Mode");
		diffModeBtn.setId("diffModeBtn");
		diffModeBtn.setDisable(true);

	}

	private void setUpButtonBar() {
		/*
		 * flow = new FlowPane(); flow.setHgap(4);
		 * flow.setAlignment(Pos.TOP_RIGHT); flow.getChildren().addAll(minBtn,
		 * closeBtn);
		 * 
		 * flow2 = new FlowPane(); flow2.setHgap(4);
		 * flow2.setAlignment(Pos.BOTTOM_LEFT);
		 * flow2.getChildren().addAll(openFileButton, wordCloudButton);
		 */
		btnsBar = new VBox(10);
		HBox mainBtns = new HBox(10);
		mainBtns.getChildren().add(openFileButton);
		HBox winBtnBox = new HBox(10);
		winBtnBox.setAlignment(Pos.CENTER_RIGHT);
		winBtnBox.getChildren().addAll(minBtn, closeBtn);
		mainBtns.getChildren().add(winBtnBox);
		HBox.setHgrow(winBtnBox, Priority.ALWAYS);

		HBox modeBtns = new HBox(10);
		modeBtns.getChildren().add(wordCloudButton);
		HBox diffModeBox = new HBox(10);
		diffModeBox.setAlignment(Pos.CENTER_RIGHT);
		diffModeBox.getChildren().add(diffModeBtn);
		modeBtns.getChildren().add(diffModeBox);
		HBox.setHgrow(diffModeBox, Priority.ALWAYS);
		btnsBar.getChildren().addAll(mainBtns, modeBtns);
	}

	private void setButtonEvents(final Stage primaryStage) {
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primaryStage.close();
			}
		});

		minBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primaryStage.setIconified(true);
			}
		});

		openFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ribbon = new RibbonView(stackPane);
				stackPane.getChildren().add(ribbon);
				fileReader.startFileChooser(primaryStage);
				try {
					Page page = new Page(new Rectangle(0, 0, ribbon
							.getPageWidth(), ribbon.getPageHeight()));
					ArrayList<String> pages = new ArrayList<>();
                                        
                                                            fileReader_WordCloud.getCommonWords();

					pages = fileReader.readFile(page.getTextBound());

					ribbon.buildRibbon(pages.size());
					ribbon.setTexttoPages(pages);
					wordCloudButton.setDisable(false);
					diffModeBtn.setDisable(false);
					wordCloud.buildWordCloud(pages);

				} catch (Exception exception) {
					System.out.println(exception);
				}
			}
		});

		wordCloudButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (wordCloudToggle) {
					wordCloudToggle = false;
					stackPane.getChildren().remove(wordCloud);
					stackPane.getChildren().add(ribbon);
					wordCloudButton.setText("Word Cloud View");
				} else {
					wordCloudToggle = true;
					stackPane.getChildren().remove(ribbon);
					stackPane.getChildren().add(wordCloud);
					wordCloudButton.setText("Ribbon View");
				}
			}
		});

		diffModeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (diffModeToggle) {
					diffModeToggle = false;
					diffModeBtn.setText("Drag Mode");
				} else {
					diffModeToggle = true;
					diffModeBtn.setText("Pan Mode");
				}
			}
		});
	}

	private void setSceneEvents(final Scene scene) {
		// handles mouse scrolling
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (!event.isDirect()) {
					ribbon.zoom(event.getDeltaY(), event.getX(), event.getY());
				}
				event.consume();

			}
		});
		scene.setOnZoom(new EventHandler<ZoomEvent>() {
			@Override
			public void handle(ZoomEvent event) {
				double delta = event.getZoomFactor() - 1;
				ribbon.zoom(delta, event.getX(), event.getY());
				event.consume();
			}
		});

	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
