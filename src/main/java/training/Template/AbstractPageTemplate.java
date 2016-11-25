package training.Template;


/**
 * This is template of usual this site chapter page.
 */

public abstract class AbstractPageTemplate implements Page {

    abstract protected void showBody();

    @Override
    public void show() {
        showHeader();
        showBody();
        showFooter();
    }

    public void showHeader() {
        //prints header of the page
    }

    public void showFooter() {
        // prints footer of the page
    }

}
