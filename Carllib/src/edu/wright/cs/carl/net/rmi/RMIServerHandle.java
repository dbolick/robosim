
package edu.wright.cs.carl.net.rmi;

import java.util.*;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ContextHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.message.ClientMessage;
import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.net.server.Server;
import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.security.UserCredentials;
import edu.wright.cs.carl.security.PermissionException;


/**
 * Implementation of the RMIServerHandleRI remote interface.
 *
 * @author  Duane Bolick
 * 
 * @see     ServerHandle
 * @see     RMIServerHandleRI
 */
public class RMIServerHandle extends UnicastRemoteObject implements RMIServerHandleRI, Callback
{
    private Server server;
    private Map<String, ContextHandle> contextHandleCallbacks;
    private UserCredentials clientCredentials;
    private ClientMessage clientMessage;
            
    public RMIServerHandle(Server server, UserCredentials clientCredentials) throws RemoteException
    {
        this.server = server;
        this.contextHandleCallbacks = new HashMap<String, ContextHandle>();
        
        this.clientCredentials = clientCredentials;
        this.clientMessage = new ClientMessage(this.clientCredentials);
    }

    /**
     * Get the name of this server.
     *
     * @return  The server name.
     */
    public String getName()
    {
        return this.server.getName();
    }

    /**
     * Client Management
     */
    
    /**
     * Get the number of connected clients.
     * 
     * @return  The current number of clients.
     */
    public int getCurrentNumClients() throws RemoteException
    {
        return this.server.getCurrentNumClients();
    }
    
    /**
     * Get the maximum number of clients that can connect.
     * 
     * @return  The maximum number of clients.
     */
    public int getMaxNumClients() throws RemoteException
    {
        return this.server.getMaxNumClients();
    }
    
    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList() throws RemoteException
    {
        return this.server.getClientList();
    }


    /**
     * Context Management
     */

    /**
     * Get the name of the Context.
     *
     * @param   contextID   [in]    Supplies the context ID.
     *
     * @return  The Context name.
     */
    public String getContextName(String contextID)
    {
        return this.server.getContextName(contextID);
    }

    /**
     * Get the number of clients that have joined a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The number of clients who have joined the Context.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public int getCurrentNumContextClients(String contextID) throws ServerException, RemoteException
    {
        return this.server.getCurrentNumContextClients(contextID);
    }
    
    /**
     * Get the maximum number of clients that can join a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The maximum number of clients who can join the Context.
     * 
     * @throws ServerException if the Context does not exist.
     */
    public int getMaxNumContextClients(String contextID) throws ServerException, RemoteException
    {
        return this.server.getMaxNumContextClients(contextID);
    }
      
    /**
     * Get the Context type.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The Context type.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextType(String contextID) throws ServerException, RemoteException
    {
        return this.server.getContextType(contextID);
    }
    
    /**
     * Get the Context description
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The Context description.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextDescription(String contextID) throws ServerException, RemoteException
    {
        return this.server.getContextDescription(contextID);
    }

    /**
     * Get a list of IDs of all contexts on this server.
     * 
     * @return  A List of IDs of all contexts.
     */
    public List<String> getContextList() throws RemoteException
    {
        return this.server.getContextList();
    }
    
    /**
     * Get context information.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  The ContextInformation for the context.
     * 
     * @throws  IOException if the connection to the server is lost.
     */
    public ContextInformation getContextInformation(String contextID) throws ServerException, RemoteException
    {
        return this.server.getContextInformation(contextID);
    }

    /**
     * Check to see if the Context is active on this Server.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  True if the Context is active, false otherwise.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public boolean isContextActive(String contextID) throws ServerException, RemoteException
    {
        return this.server.isContextActive(contextID);
    }

    /**
     * Join a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the context.
     * 
     * @return  A handle to the Context, null if the Context was full.
     * 
     * @throws  ContextException if the Context is not active.
     * @throws  ServerException if either the context ID or client do not
     *          exist on this server.
     * @throws  DuplicateHandleException if the user has already joined.
     * @throws  PermissionException if the client does not have privileges to
     *          join the context.
     */
    public RMIContextHandleRI joinContext(String contextID) throws ContextException, ServerException, DuplicateHandleException, PermissionException, RemoteException
    {
        boolean joinSuccessful = this.server.connectClientToContext(contextID, this.clientCredentials.getName());
        
        if(joinSuccessful == false) {
            return null;
        }
        
        //
        // Create a new RMIContextHandleRI
        //
        Context joinedContext = this.server.getContext(contextID);
        

        
        RMIContextHandleRI newContextHandle = new RMIContextHandle(joinedContext, this.clientCredentials);

        //
        // Add it to the collection of callbacks.
        //
        if(this.contextHandleCallbacks.containsKey(contextID)) {
            //throw new AssertionError("Context callback already exists.");
        }

        this.contextHandleCallbacks.put(contextID, newContextHandle);

        return newContextHandle;
    }

    /**
     * Leave a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @throws  ContextException if the client is not connected to the Context.
     * @throws  ServerException if the Context does not exist.
     */   
    public void leaveContext(String contextID) throws ContextException, ServerException, RemoteException
    {
        this.contextHandleCallbacks.remove(contextID);
        this.server.getContext(contextID).clientLeft(this.clientCredentials.getName());
    }
    
    /**
     * Get the unique ID of the Server.
     *
     * @return  The unique ID of the Server.
     *
     * @throws  RemoteException if the connection is lost.
     */
    public String getUniqueID() throws RemoteException
    {
       return this.server.getUniqueID();
    }
    
    /**
     * Send the provided Object as the payload of a message.
     * 
     * @param   payload     [in]    Supplies the message payload.
     */
    public void sendMessagePayload(Object payload) throws MessagingException, MessageTypeException, PermissionException, RemoteException
    {
        this.clientMessage = new ClientMessage(this.clientCredentials);
        this.clientMessage.payload = payload;
        this.sendMessage(clientMessage);
    }     
    
    /**
     * Send a Message to the object to which this handle refers.
     * 
     * @param   message [in]    Supplies the Message to be sent.
     * 
     * @throws  MessagingException if the action is invalid.
     * @throws  MessageTypeException if the object cannot handle the given
     *          Message subclass.
     * @throws  IOException if the connection is lost.
     * @throws  PermissionException if the sender does not have sufficient
     *          privileges to enact whatever the Message is intended to do.
     */
    public void sendMessage(Message message) throws MessagingException, MessageTypeException, PermissionException, RemoteException
    {
        this.server.handleIncomingMessage(message);
    }
    
    /**
     * The remote entity calls this on us to sever the connection.  If a client
     * wants to sever its connection to a remote entity, it should call that
     * handle's disconnect method.
     *
     * @throws  IOException but we don't care.
     */
    public void disconnect(Object obj) throws RemoteException
    {

        this.server.clientHasLeftServer(this.clientCredentials.getName());
    }
}
