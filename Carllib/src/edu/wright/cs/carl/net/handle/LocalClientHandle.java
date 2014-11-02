
package edu.wright.cs.carl.net.handle;

import java.io.IOException;

import edu.wright.cs.carl.net.client.Client;

import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.UserCredentials;


/**
 * This class is an implementation of a ClientHandle for local connections.
 *
 * @author  Duane Bolick
 */
public class LocalClientHandle implements ClientHandle, Callback
{    
    private Client client;
    private UserCredentials userCredentials;
    
    public LocalClientHandle(Client client, UserCredentials userCredentials)
    {
        this.client = client;
        this.userCredentials = userCredentials;
    }

    /**
     * Get the UserCredentials object associated with this ClientHandle.
     * 
     * @return  a reference to the UserCredentials associated with this ClientHandle.
     */
    public UserCredentials getCredentials() throws IOException
    {
        return this.userCredentials;
    }

    /**
     * Get the name of the handle.
     * 
     * @return  The name of the handle.
     * 
     * @throws  IOException if the connection is lost.
     */
    public String getUniqueID() throws IOException
    {
        return this.userCredentials.getName();
    }
    
    /**
     * Send a Message to the Client object to which this handle refers.
     * 
     * @param   message [in]    Supplies the Message to be sent.
     * 
     * @throws  MessagingException if the action is invalid.
     * @throws  MessageTypeException if the incoming Message is an invalid type.
     * @throws  PermissionException if the sender does not have sufficient
     *          privileges to enact whatever the Message is intended to do.
     * @throws  IOException if the connection is lost.
     */
    public void sendMessage(Message message) throws MessagingException, MessageTypeException, PermissionException, IOException
    {
        this.client.handleIncomingMessage(message);
    }
    
    /**
     * The remote entity calls this on us to sever the connection.  If a client
     * wants to sever its connection to a remote entity, it should call that
     * handle's disconnect method.
     *
     * @throws  IOException but we don't care.
     */
    public void disconnect(Object obj) throws IOException
    {
        String s = null;

        if(obj instanceof String) {
            s = (String) obj;
        }

        if(s != null && s.isEmpty() == false) {
            this.client.contextConnectionLost(s);
        }
        else {
            //
            // This would be where the remote server drops us, but...  We don't
            // want to do that - this is the local client, and this connection
            // is to the local server.
            //
            //this.client.serverConnectionLost(serverHandleName);

            throw new AssertionError("Attempted to sever local client-server connection.  Do not do that!");
        }
    }
}
