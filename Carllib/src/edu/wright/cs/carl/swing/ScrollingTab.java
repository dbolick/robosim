
package edu.wright.cs.carl.swing;

import javax.swing.*;


/**
 * This component is used specifically for a JScrollPane that's intended to
 * be a tab on a tabbed pane, and enclose at the top level, a single JPanel.
 *
 * @author  Duane Bolick
 */
public class ScrollingTab extends JScrollPane
{
    private TabPanel panel;

    public ScrollingTab(TabPanel panel)
    {
        super(panel);
        this.panel = panel;
        this.setName(this.panel.getName());
    }

    /**
     * Close the tab containing this scrolling pane.  This method calls the
     * tabClosed() method of its contained CloseableTabComponent which allows it to
     * perform any necessary cleanup.  This method then removes the actual
     * GUI element.
     */
    public void closeTab()
    {
        this.panel.tabClosed();
    }

    public void pleaseClose()
    {
        this.getParent().remove(this);
    }

    public void refreshTabContents()
    {
        this.panel.refreshPanelComponents();
    }
}
