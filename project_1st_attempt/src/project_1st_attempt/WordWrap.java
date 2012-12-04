package project_1st_attempt;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class WordWrap {
	ArrayList<String> pages;
	File file;
	Rectangle bounds;
	double boundWidth, boundHeight, spaceWidth, lineHeight;

	public WordWrap(File file, Rectangle bounds) {
		pages = new ArrayList<String>();
		this.file = file;
		this.bounds = bounds;
		boundWidth = bounds.getWidth();
		boundHeight = bounds.getHeight();
		spaceWidth = new Text(" ").getBoundsInLocal().getWidth();
		lineHeight = new Text("").getBoundsInLocal().getHeight();
	}

	public ArrayList<String> readFile() throws IOException {
		double spaceLeft = boundWidth;
		Text tempPage = new Text("");
		String page = "";
		LineNumberReader r = new LineNumberReader(new FileReader(file));
		String paragraph, word;
		while ((paragraph = r.readLine()) != null) {
			Scanner sc = new Scanner(paragraph);
			while (sc.hasNext()) {
				word = sc.next();
				double wordWidth = new Text(word).getBoundsInLocal().getWidth();
				double textWithNewLine = tempPage.getBoundsInLocal().getHeight() + lineHeight;
				if (textWithNewLine > boundHeight) {
					pages.add(page);
					page = "";
				}
				if (wordWidth + spaceWidth > spaceLeft) {
					if(!(textWithNewLine > boundHeight))
						page += "\n";
					spaceLeft = boundWidth - wordWidth;
				} else {
					spaceLeft = spaceLeft - (wordWidth + spaceWidth);
				}
				page += word + " ";
				tempPage.setText(page);
			}
			if (!((tempPage.getBoundsInLocal().getHeight() + lineHeight) > boundHeight)) {
				page += "\n";
				tempPage.setText(page);
				spaceLeft = boundWidth;
			}
			sc.close();
		}
		pages.add(page);
		r.close();
		return pages;
	}
}
