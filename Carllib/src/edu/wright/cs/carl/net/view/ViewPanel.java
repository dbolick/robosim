
package edu.wright.cs.carl.net.view;

import java.io.IOException;

import edu.wright.cs.carl.net.handle.ContextHandle;
import edu.wright.cs.carl.swing.TabPanel;


/**
 * This class is the supertype for all JPanels used in the main GUI to
 * interact with remote contexts.
 *
 * @author  Duane Bolick
 */
public abstract class ViewPanel extends TabPanel
{
    protected ContextHandle contextHandle;
    protected String contextName;
    protected String contextID;
    
    public void initializePanel(ContextHandle contextHandle) throws IOException
    {
        this.contextHandle = contextHandle;
        this.contextName = this.contextHandle.getName();
        this.contextID = this.contextHandle.getUniqueID();
        doInit();
    }

    @Override
    public String getName()
    {
        return this.contextName;
    }

    public String getID()
    {
        return this.contextID;
    }

    abstract public void doInit();    
}
