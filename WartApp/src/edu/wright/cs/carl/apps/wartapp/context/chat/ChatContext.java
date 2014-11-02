
package edu.wright.cs.carl.apps.wartapp.context.chat;

import java.security.acl.Acl;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.handle.RemoteHandleManager;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.permissions.ContextPermission;

import edu.wright.cs.carl.apps.wartapp.*;


/**
 * A chatroom-type Context implementation.
 *
 * @author  Duane Bolick
 */
public class ChatContext extends AbstractContext
{

    protected ContextMessage chatMessage;
    
    public ChatContext(String name, String description, Acl acl, RemoteHandleManager handleManager, int maxNumConnections)
    {
        super(  name,
                description,
                WartAppConstants.GetContextTypeString(WartAppConstants.ContextType.CHAT_CONTEXT),
                acl,
                handleManager,
                maxNumConnections);
        
        chatMessage = new ContextMessage(this.name, this.uniqueID);
    }

    
    /**
     * Handle the incoming message.
     * 
     * @param   message     [in] Supplies the message to be handled.
     * 
     * @throws  MessagingException if an error occurs in handling.
     * @throws  MessageTypeException if the Message is of an unexpected type.
     * @throws  PermissionException if the sender lacks permission to do the
     *          proposed action.
     */
    public void handleIncomingMessage(Message message) throws MessagingException, MessageTypeException, PermissionException
    {
        if(message instanceof ClientMessage) {
            ClientMessage clientMessage = (ClientMessage) message;
            
            if(this.hasPermission(clientMessage.clientCredentials, new ContextPermission("sendMessage")) == false) {
                throw new PermissionException("You lack permission to interact with this context.");
            }            
            
            if(clientMessage.payload instanceof SimpleText) {
                SimpleText textPayload = (SimpleText)clientMessage.payload;
                this.chatMessage.payload = new ChatWindowUpdate(
                                                        clientMessage.clientCredentials.getName() +
                                                        ": " + textPayload.text + "\n");
                this.broadcastMessage(this.chatMessage);
                return;                
            }

            else if(clientMessage.payload instanceof UserStatusChange) {
                UserStatusChange statusChange = (UserStatusChange) clientMessage.payload;

                StringBuffer statusMessage = new StringBuffer();
                statusMessage.append(clientMessage.clientCredentials.getName() + " ");
                if(statusChange.statusChangeType == UserStatusChange.Type.LOGOFF) {
                    statusMessage.append("has left.");
                }
                else if(statusChange.statusChangeType == UserStatusChange.Type.LOGON) {
                    statusMessage.append("has joined.");
                }
                this.chatMessage.payload = new UserListUpdate(
                                                    statusMessage.toString() + "\n",
                                                    clientMessage.clientCredentials.getName(),
                                                    statusChange.statusChangeType);
                
                this.broadcastMessage(this.chatMessage);
                return;   
            }
            
            else if(clientMessage.payload instanceof UserRemovalRequest) {

                //
                // Check permissions.
                //
                if(this.hasPermission(clientMessage.clientCredentials, new ContextPermission("kickUser")) == false) {
                    throw new PermissionException("You lack permission to kick other users.");
                }

                //
                // Remove the user.
                //
                UserRemovalRequest removalRequest = (UserRemovalRequest) clientMessage.payload;

                try {
                    WartApp.getApplication().getServer().disconnectClientFromContext(this.getUniqueID(), removalRequest.username);
                }
                catch(ServerException e) {
                    throw new AssertionError(e);
                }
                catch(ContextException e) {
                    throw new AssertionError(e);
                }

                //
                // And tell everybody about it.
                //
                this.chatMessage.payload = new UserListUpdate(
                                                    removalRequest.username + " has been kicked by " + clientMessage.clientCredentials.getName() + "\n",
                                                    removalRequest.username,
                                                    UserStatusChange.Type.LOGOFF);

                this.broadcastMessage(this.chatMessage);
            }
        }
        
        //
        // If we haven't handled it by now, it's a type we can't handle, so
        // throw.
        //
        else {
            throw new MessageTypeException("Unknown message type: " + message.getClass().toString());
        }
    }

     
    /**
     * Starts sending view updates to the named user.
     * 
     * @param   username    [in]    Supplies username.
     */
    public void activateRemoteView(String username)
    {
        //
        // Since this is relatively low-bandwidth (just textPayload messages),
        // we really don't stop sending updates.
        //
        /*
        if(this.clients.getHandle(username) == null) {
            throw new AssertionError();
        }
        this.activeViews.add(username);
         */
    }
    
    /**
     * Stops sending view updates to the named user.
     * 
     * @param   username    [in]    Supplies username.
     */
    public void deactivateRemoteView(String username)
    {
        //
        // Since this is relatively low-bandwidth (just textPayload messages),
        // we really don't stop sending updates.
        //
        /*        
        if(this.clients.getHandle(username) == null) {
            throw new AssertionError();
        }
        this.activeViews.remove(username); 
         */       
    }
}
