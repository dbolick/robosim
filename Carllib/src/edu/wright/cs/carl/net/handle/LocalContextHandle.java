
package edu.wright.cs.carl.net.handle;

import java.io.IOException;

import java.util.*;

import edu.wright.cs.carl.net.context.Context;
import edu.wright.cs.carl.net.context.ContextException;

import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.ClientMessage;
import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;
import edu.wright.cs.carl.net.message.payload.SimpleText;

import edu.wright.cs.carl.security.UserCredentials;

import edu.wright.cs.carl.security.PermissionException;


/**
 * 
 *
 * @author Duane
 */
public class LocalContextHandle implements ContextHandle
{
    protected Context context;
    protected UserCredentials clientCredentials;
    protected ClientMessage clientMessage;
    
    public LocalContextHandle(Context context, UserCredentials clientCredentials)
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
    public String getName() throws IOException
    {
        return this.context.getName();
    }

    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList() throws IOException
    {
        return this.context.getClientList();
    }
    
    /**
     * Stop receiving update messages from this context.
     * 
     * @throws java.io.IOException if connection is lost.
     */
    public void suspendUpdates() throws IOException
    {
        this.context.deactivateRemoteView(this.clientCredentials.getName());
    }
    
    /**
     * Start receiving update messages from this context.
     * 
     * @throws java.io.IOException if the connection is lost.
     */
    public void resumeUpdates() throws IOException
    {
        this.context.deactivateRemoteView(this.clientCredentials.getName());
    }
    
    /**
     * Get the unique ID of the handle.
     *
     * @return  The unique ID of the handle.
     *
     * @throws  RemoteException if the connection is lost.
     */
    public String getUniqueID() throws IOException
    {
       return this.context.getUniqueID();
    }
    
    /**
     * Send the provided Object as the payload of a message.
     * 
     * @param   payload     [in]    Supplies the message payload.
     */
    public void sendMessagePayload(Object payload) throws MessagingException, MessageTypeException, PermissionException, IOException
    {
        this.clientMessage.payload = payload;
        this.sendMessage(clientMessage);
    }
    
    /**
     * Send a Message.  
     * 
     * @param   message [in]    Supplies the Message to be sent.
     * 
     * @throws  MessagingException if the action is invalid.
     * @throws  IOException if the connection is lost.
     * @throws  PermissionException if the sender does not have sufficient
     *          privileges to enact whatever the Message is intended to do.
     */
    public void sendMessage(Message message) throws MessagingException, MessageTypeException, PermissionException, IOException
    {
        this.context.handleIncomingMessage(message);
    }
    
    /**
     * Disconnect from the remote entity.
     * 
     * @throws  IOException but we don't care.
     */
    public void disconnect(Object obj) throws IOException
    {
        this.context.clientLeft(this.clientCredentials.getName());
    }
}
