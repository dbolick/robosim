
package edu.wright.cs.carl.net.rmi;

import java.util.*;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

import edu.wright.cs.carl.net.context.Context;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ContextHandle;

import edu.wright.cs.carl.net.message.ClientMessage;
import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.UserCredentials;


/**
 * Implementation of the RMIContextHandleRI remote interface.
 *
 * @author  Duane Bolick
 * 
 * @see     ContextHandle
 * @see     RMIContextHandleRI
 */
public class RMIContextHandle extends UnicastRemoteObject implements RMIContextHandleRI, Callback
{
    protected Context context;
    protected UserCredentials clientCredentials;
    protected ClientMessage clientMessage;
    
    public RMIContextHandle(Context context, UserCredentials clientCredentials) throws RemoteException
    {
        this.context = context;
        this.clientCredentials = clientCredentials;
        this.clientMessage = new ClientMessage(this.clientCredentials);
    }

    /**
     * Get the name of this Context.
     *
     * @return  The Context name.
     */
    public String getName() throws RemoteException
    {
        return this.context.getName();
    }

    /**
     * Messaging.
     */
    
    /**
     * Send the provided Object as the payload of a message.
     * 
     * @param   payload     [in]    Supplies the message payload.
     */
    public void sendMessagePayload(Object payload) throws MessagingException, MessageTypeException, PermissionException, RemoteException
    {
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
        this.context.handleIncomingMessage(message);
    }
    
    /**
     * Get the unique ID of the Context.
     * 
     * @return  The unique ID of the Context.
     * 
     * @throws  RemoteException if the connection is lost.
     */
    public String getUniqueID() throws RemoteException
    {
       return this.context.getUniqueID();
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
        this.context.clientLeft(this.clientCredentials.getName());
    }
    
    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList() throws RemoteException
    {
        return this.context.getClientList();
    }
    
    /**
     * Stop receiving update messages from this context.
     * 
     * @throws java.io.IOException if connection is lost.
     */
    public void suspendUpdates() throws RemoteException
    {
        this.context.deactivateRemoteView(this.clientCredentials.getName());
    }
    
    /**
     * Start receiving update messages from this context.
     * 
     * @throws java.io.IOException if the connection is lost.
     */
    public void resumeUpdates() throws RemoteException
    {
        this.context.activateRemoteView(this.clientCredentials.getName());
    }
}
