package flowreader.model;

/**
 * A Page is the representation of a page in a document it contains the text of the page
 * @author D-Day
 */
public class Page {

    private String text;


    public Page(String text) {
        this.text = text;
    }

    /**
     * @return the text of the page
     */
    public String getText() {
        return this.text;
    }


    @Override
    public String toString() {
        String s = "PAGE \n " + text + "\n";

    
        return s + "\n";
    }
}
