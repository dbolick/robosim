
package edu.wright.cs.carl.net.client;

import edu.wright.cs.carl.net.event.ServerEventListener;
import java.io.IOException;

import java.util.List;

import edu.wright.cs.carl.net.context.ContextException;
import edu.wright.cs.carl.net.context.ContextInformation;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ServerHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.message.*;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.net.view.View;

import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.swing.*;

/**
 * <p>
 * The Client interface defines the interface to the controller.
 * </p>
 * 
 * @author  Duane Bolick
 */
public interface Client extends MessageRecipient
{
    /**
     * Add a listener that will receive any private messages this client
     * receives.
     * 
     * @param   pmListener  [in]    Supplies the PrivateMessageListener.
     */
    public void addPrivateMessageListener(PrivateMessageListener pmListener);

    /**
     * Add a listener that will receive ServerEvents from the given Server.
     *
     * @param   listener    [in]    Supplies the listener.
     * @param   serverID    [in]    Supplies the ID of the server whose events
     *                              will be sent to the listener.
     */
    public void addServerEventListener(ServerEventListener listener, String serverID);

    /**
     * Add a server connection.
     * 
     * @param   serverHandle    [in]    Supplies the handle to the remote
     *                                  Server.
     * @param   clientCallback  [in]    Supplies the callback to this Client.
     * 
     * @return  True if the connection was added successfully.
     * 
     * @throws  DuplicateHandleException if there exists a server handle with
     *          the same handle name.
     * 
     * @throws  IOException if the connection to the server was lost.
     */
    public boolean addServerConnection(ServerHandle serverHandle, Callback clientCallback) throws DuplicateHandleException, IOException;
    
    /**
     * Disconnect from a Server.  This method assumes that we are the ones
     * actively disconnecting from the Server.
     * 
     * @param   serverID    [in]    Supplies the server ID.
     * 
     * @throws  ServerException if we're not connected to that Server.
     */
    public void disconnectFromServer(String serverID) throws ServerException;

    /**
     * This method is called when a remote Server disconnects from *us*.  
     * Therefore, all we do in this method is cleanup.
     * 
     * @param   serverID    [in]    Supplies the server ID.
     */
    public void serverConnectionLost(String serverID);

    /**
     * Get the IDs of all the Servers we're connected to.
     * 
     * @return  A List of Server IDs.
     */
    public List<String> getServerIDs();
    
    /**
     * Get the ServerHandle of a Server we're connected to.
     * 
     * @param   serverID    [in]    Supplies the ID of the desired Server.
     *  
     * @return  A reference to the ServerHandle, or null if it doesn't exist.
     */
    public ServerHandle getServerHandle(String serverID);
    
    /**
     * Get a list of context IDs hosted on a server to which we're connected.
     * 
     * @param   serverID    [in]    Supplies the server name.
     * 
     * @return  A list of context IDs.
     *
     * @throws  ClientException if the connection to the Server has been lost.
     */
    public List<String> getContextListFromServer(String serverID) throws ClientException;

    /**
     * Get information about a context.
     *
     * @param   serverID      [in]    Supplies the server name.
     * @param   contextID     [in]    Supplies the context name.
     *
     * @return  The ContextInformation.
     *
     * @throws  ClientException if the connection to the Server has been lost.
     */
    public ContextInformation getContextInformation(String serverID, String contextID) throws ClientException;

    /**
     * Join a Context hosted on a given Server.
     *
     * @param   serverID        [in]    Supplies the server ID.
     * @param   contextID       [in]    Supplies the ID of the Context we
     *                                  want to join.
     * @param   view            [in]    Supplies a View object that represents
     *                                  the Context's remote user interface.
     * @param   setAsActiveView [in]    If true, the new View will be set as
     *                                  active (i.e., it'll be the only one
     *                                  we receive view updates on).
     *
     * @return  True if joining succeeded, false if the Context was full.
     *
     * @throws ContextException if the Context is not active.
     * @throws ServerException if we're not connected to the Server.
     * @throws DuplicateHandleException if we're already connected to the
     *          Context.
     * @throws PermissionException if we lack permission to join the Context.
     */
    public boolean joinContext(String serverID, String contextID, View view, boolean setAsActiveView)
            throws ContextException, ServerException, DuplicateHandleException, PermissionException, ClientException;
    
    /**
     * Exit a context.  This method assumes that we are the ones actively
     * leaving the Context.
     *
     * @param   contextID   [in]    Supplies the context ID.
     *
     * @throws  ServerException if we have not joined the context.
     */
    public void leaveContext(String contextID) throws ServerException;

    /**
     * This method is called when a remote Context disconnects from *us*.
     * Therefore, all we do in this method is cleanup.
     *
     * @param   contextID   [in]    Supplies the context ID.
     */
    public void contextConnectionLost(String contextID);
    
    /**
     * Set a given View to active.  This indicates to the remote context to
     * start sending view updates, and tells all other remote contexts to stop
     * sending view updates.  This is to conserve bandwidth.
     * 
     * @param   contextID   [in]    Supplies the Context ID.
     * 
     * @throws  ServerException if the View does not exist.
     */
    public void setActiveView(String contextID) throws ServerException;

}
