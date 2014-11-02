
package edu.wright.cs.carl.swing;


/**
 * This abstract class represents a JPanel that is intended to be the main
 * Component of a ScrollableTab.
 *
 * @author  Duane Bolick
 */
public abstract class TabPanel extends javax.swing.JPanel implements CloseableTabComponent
{
    /**
     * Refresh the components of this panel.
     */
    abstract public void refreshPanelComponents();
}
