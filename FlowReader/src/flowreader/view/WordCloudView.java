/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.view;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author D-Day
 */
public class WordCloudView extends Group {

 

    public void buildWordCloud(ArrayList<String> pages) {
        Text text = new Text();
        text.setText("WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n"+"WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!WordCloud!"+"\n");
        text.setFont(Font.font(null, 100));
        this.getChildren().add(text);
    }
}
