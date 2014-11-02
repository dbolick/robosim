
package edu.wright.cs.carl.net.rmi;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

import edu.wright.cs.carl.net.client.Client;

import edu.wright.cs.carl.net.handle.Callback;

import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.UserCredentials;


/**
 * Implementation of the RMIClientHandleRI remote interface.
 *
 * @author  Duane Bolick
 * 
 * @see     ClientHandle
 * @see     RMIClientHandleRI
 */
public class RMIClientHandle extends UnicastRemoteObject implements RMIClientHandleRI, Callback
{
    private Client client;
    private UserCredentials userCredentials;
    private String serverID;
    
    public RMIClientHandle(Client client, UserCredentials userCredentials) throws RemoteException
    {
        this.client = client;
        this.userCredentials = userCredentials;
    }

    /**
     * Set the server ID associated with this client handle.  This method
     * is necessary, because a client handle doesn't know the server ID when
     * it is first instantiated.  It must be set during the connection process.
     * 
     * @param   serverID    [in]    Supplies the server ID.
     * 
     * @throws  RemoteException
     */
    public void setServerID(String serverID) throws RemoteException
    {
        this.serverID = serverID;
    }
    
    /**
     * Get the UserCredentials object associated with this ClientHandle.
     * 
     * @return  a reference to the UserCredentials associated with this ClientHandle.
     */
    public UserCredentials getCredentials() throws RemoteException
    {
        return this.userCredentials;
    }
    
    /**
     * Get the ID of the handle.
     * 
     * @return  The ID of the handle.
     * 
     * @throws  IOException if the connection is lost.
     */
    public String getUniqueID() throws RemoteException
    {
        return this.userCredentials.getName();
    }
    
    /**
     * Send a Message to the object to which this handle refers.
     * 
     * @param   message [in]    Supplies the Message to be sent.
     * 
     * @throws  MessagingException if the action is invalid.
     * @throws  MessageTypeException if the object cannot handle the given
     *          Message subclass.
     * @throws  RemoteException if the connection is lost.
     * @throws  PermissionException if the sender does not have sufficient
     *          privileges to enact whatever the Message is intended to do.
     */
    public void sendMessage(Message message) throws MessagingException, MessageTypeException, PermissionException, RemoteException
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
    public void disconnect(Object obj) throws RemoteException
    {
        String s = null;

        if(obj instanceof String) {
            s = (String) obj;
        }

        if(s != null && s.isEmpty() == false) {
            this.client.contextConnectionLost(s);
        }
        else {
            this.client.serverConnectionLost(serverID);
        }
    }
}
