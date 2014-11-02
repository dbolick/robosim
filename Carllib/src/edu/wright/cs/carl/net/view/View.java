
package edu.wright.cs.carl.net.view;

import java.io.IOException;

import javax.swing.JPanel;

import edu.wright.cs.carl.net.handle.ContextHandle;

import edu.wright.cs.carl.net.message.payload.ContextViewUpdate;


/**
 * This interface declares the methods common to all View implementations.
 * A View implementation is where the GUI element and a ContextHandle are
 * 'connected.'  
 * 
 * Each different type of context necessarily updates its view differently, so
 * we leave the creator of a context to implement that method.
 * 
 * @author  Duane Bolick
 */
public interface View
{
    /**
     * Initialize the view.  Must do this before using.
     * 
     * @param contextHandle
     * @throws java.io.IOException
     */
    public void initializeView(ContextHandle contextHandle) throws IOException;
    
    /**
     * Update the view panel.
     * 
     * @param   update  [in]    Supplies the view update.
     */
    public void update(ContextViewUpdate update);
    
    /**
     * Notify the actual Context to start sending view updates.
     * 
     * @throws java.io.IOException if the connection is lost.
     */
    public void setAsActiveView() throws IOException;
    
    /**
     * Notify the actual Context to stop sending view updates.
     * 
     * @throws java.io.IOException if the connection is lost.
     */
    public void setAsInactiveView() throws IOException;
    
    /**
     * Get the ContextHandle.
     * 
     * @return The ContextHandle.
     */
    public ContextHandle getContextHandle();
    
    /**
     * Set the ContextHandle.
     * 
     * @param   contextHandle   [in]    Supplies the context handle.
     */
    public void setContextHandle(ContextHandle contextHandle);

    /**
     * Get the GUI element associated with this view.
     * 
     * @return  The JPanel GUI component associated with this view.
     */
    public JPanel getViewPanel();
    
    /**
     * Set the GUI component associated with this view.
     * 
     * @param   viewPanel   [in]    Supplies the JPanel.
     */
    public void setViewPanel(ViewPanel viewPanel);

    /**
     * Disconnect from the context.  Calling this method actively disconnects.
     */
    public void disconnectFromContext();

    /**
     * Called when connection is lost.
     */
    public void connectionToContextLost();
    
    /**
     * Get the ID of the context.
     * 
     * @return  The context ID.
     */
    public String getContextID();
    
    /**
     * Get the ID of the remote server on which the context is being hosted.
     * 
     * @return  The name of the remote server.
     */
    public String getServerID();
}
