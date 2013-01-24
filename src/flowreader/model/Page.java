package flowreader.model;

/**
 *
 * @author D-Day
 */
public class Page {

    private String text;


    public Page(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }


    @Override
    public String toString() {
        String s = "PAGE \n " + text + "\n";

    
        return s + "\n";
    }
}
