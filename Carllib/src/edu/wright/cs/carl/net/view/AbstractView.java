
package edu.wright.cs.carl.net.view;

import java.io.IOException;

import javax.swing.JPanel;

import edu.wright.cs.carl.net.handle.ContextHandle;
import edu.wright.cs.carl.net.message.payload.ContextViewUpdate;

/**
 * Abstract implementation of View.
 *
 * @author  Duane Bolick
 */
public abstract class AbstractView implements View
{
    protected ContextHandle contextHandle;
    protected String contextID;
    protected ViewPanel viewPanel;
    protected String serverID;
    
    /**
     * Update the view panel.
     *
     * @param   update  [in]    Supplies the view update.
     */
    abstract public void update(ContextViewUpdate update);


    public AbstractView(String serverID, ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;
        this.serverID = serverID;
    }

    public void initializeView(ContextHandle contextHandle) throws IOException
    {
        this.contextHandle = contextHandle;
        this.viewPanel.initializePanel(contextHandle);
       
        //
        // This can throw IOException.
        //
        this.contextID = this.contextHandle.getUniqueID();
    }
    
    
    /**
     * Notify the actual Context to start sending view updates.
     * 
     * @throws java.io.IOException if the connection is lost.
     */    
    public void setAsActiveView() throws IOException
    {
        this.contextHandle.resumeUpdates();
    }
    
    /**
     * Notify the actual Context to stop sending view updates.
     * 
     * @throws java.io.IOException if the connection is lost.
     */    
    public void setAsInactiveView() throws IOException
    {
        this.contextHandle.suspendUpdates();
    }

    /**
     * Get the ContextHandle.
     * 
     * @return The ContextHandle.
     */    
    public ContextHandle getContextHandle()
    {
        return this.contextHandle;
    }
    
    /**
     * Set the ContextHandle.
     * 
     * @param   contextHandle   [in]    Supplies the context handle.
     */    
    public void setContextHandle(ContextHandle contextHandle)
    {
        this.contextHandle = contextHandle;
    }

    /**
     * Set the GUI component associated with this view.
     * 
     * @param   viewPanel   [in]    Supplies the JPanel.
     */    
    public void setViewPanel(ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;
    }
    
    /**
     * Get the GUI element associated with this view.
     * 
     * @return  The JPanel GUI component associated with this view.
     */    
    public JPanel getViewPanel()
    {
        return this.viewPanel;
    }

    /**
     * Disconnect from the context.  Calling this method actively disconnects.
     */
    public void disconnectFromContext()
    {
        try {
            this.contextHandle.disconnect(null);
        }
        catch(IOException e) {
            e.printStackTrace();

            //
            // This means we've lost connection to the server...
            // TODO:  Need to handle that. Or do we? the dropped connection will
            // surely be discovered soon in a code path where it'll be more
            // easily handled...  Hmm.
            //
        }
    }

    /**
     * Called when connection is lost.
     */
    public void connectionToContextLost()
    {
        this.viewPanel.pleaseClose();
    }
    
    /**
     * Get the ID of the context.
     *
     * @return  The context ID.
     */
    public String getContextID()
    {
        return this.contextID;
    }
    
    /**
     * Get the ID of the remote server on which the context is being hosted.
     *
     * @return  The name of the remote server.
     */
    public String getServerID()
    {
        return this.serverID;
    }
}
