
package edu.wright.cs.carl.net.message;

import edu.wright.cs.carl.security.PermissionException;

/**
 * This interface defines the required implementation for a participating
 * object in the message-passing system.
 * 
 * @author  Duane Bolick
 */
public interface MessageRecipient
{

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
    public void handleIncomingMessage(Message message) throws MessagingException, MessageTypeException, PermissionException;
}
