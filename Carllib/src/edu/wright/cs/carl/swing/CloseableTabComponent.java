
package edu.wright.cs.carl.swing;


/**
 * This interface should be implemented by components that are enclosed in a
 * closeable tabbed pane (this special type of tabbed pane is created using a
 * <i>TabbedPaneFactory</i>).
 *
 * @author  Duane Bolick
 *
 * @see     org.openide.awt.TabbedPaneFactory
 */
public interface CloseableTabComponent
{
    /**
     * Called when the enclosing tab in a JTabbedPane is closed by clicking the
     * x-button.
     */
    public void tabClosed();

    /**
     * Called when the tab should be closed programmatically (i.e., without
     * clicking the x-button).
     */
    public void pleaseClose();
}
