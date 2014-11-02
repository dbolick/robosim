
package edu.wright.cs.carl.net.context;

import java.util.List;

import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.message.MessageRecipient;

import edu.wright.cs.carl.security.AccessControlled;
import edu.wright.cs.carl.security.PermissionException;


/**
 * The basic Context interface.  A Context implementation should contain all of
 * the specific functionality and "business logic" of whatever it is that you're
 * trying to provide your users.  
 * 
 * @author  Duane Bolick
 */
public interface Context extends AccessControlled, MessageRecipient
{
    /**
     * Get the name of the Context.
     * 
     * @return  The name of the Context.
     */
    public String getName();

    /**
     * Get the unique ID of this Context.
     *
     * @return  The unique ID.
     */
    public String getUniqueID();
    
    /**
     * Get the text description of the Context.
     * 
     * @return  The text description of the Context.
     */
    public String getDescription();
    
    /**
     * Get the Context type.
     * 
     * @return  The Context type.
     */
    public String getType();
    
    
    /**
     * Client management.
     */
    
    /**
     * Get the number of joined clients.
     * 
     * @return  The current number of clients.
     */
    public int getCurrentNumClients();
    
    /**
     * Get the maximum number of clients that can join.
     * 
     * @return  The maximum number of clients.
     */
    public int getMaxNumClients();
    
    /**
     * Set the maximum number of client that may join the Context.
     * 
     * @param   maxNumClients   [in]    Supplies the maximum number of clients.
     */
    public void setMaxNumClients(int maxNumClients);
    
    /**
     * Get a List of all joined clients.
     * 
     * @return  A List of usernames of all joined clients.
     */
    public List<String> getClientList();
    
    /**
     * Add a client to a Context.
     * 
     * @param   clientHandle    [in]    Supplies a handle to the client.
     * 
     * @return  True if the client was added successfully, false if the Context
     *          is full.
     * 
     * @throws  ContextException if the Context is not active.
     * @throws  DuplicateHandleException if the user is already connected.
     * @throws  PermissionException if the user does not have access to this
     *          Context.
     */
    public boolean connectClient(ClientHandle clientHandle) throws ContextException, DuplicateHandleException, PermissionException;
    
    /**
     * Disconnect a client from a Context.
     * 
     * @param   username     [in]   Supplies the username.
     * 
     * @throws  ContextException if the user is not connected.
     */
    public void disconnectClient(String username) throws ContextException;
    
    /**
     * Disconnects all clients.
     */
    public void disconnectAll();

    /**
     * Called when a client leaves a context.
     *
     * @param   username    [in]    Supplies the username.
     */
    public void clientLeft(String username);

    /**
     * Activity management.
     */
    
    /**
     * Check to see if the Context is active.  Modifications to a context's
     * parameters should only be made when inactive.  No unprivileged actions
     * by clients (including joining) should be allowed when inactive.
     * 
     * @return  True if active, false otherwise.
     */
    public boolean isActive();
    
    /**
     * Activate the Context.
     * 
     * @param   caller  [in]    Supplies the caller.
     */
    public void activateContext();
    
    /**
     * Deactivate the Context.  Also disconnects all users from the Context.
     *
     * @param   caller  [in]    Supplies the caller.
     */
    public void deactivateContext();

    
    /**
     * View Management
     */
    
    /**
     * Starts sending view updates to the named user.
     * 
     * @param   username    [in]    Supplies username.
     */
    public void activateRemoteView(String username);
    
    /**
     * Stops sending view updates to the named user.
     * 
     * @param   username    [in]    Supplies username.
     */
    public void deactivateRemoteView(String username);
}
