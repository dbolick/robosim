
package edu.wright.cs.carl.net.handle;

import java.io.IOException;

import java.util.List;

import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.security.PermissionException;


/**
 * This interface represents the methods that a remote client may invoke to
 * interact with a Context.
 * 
 * @author  Duane Bolick
 */
public interface ContextHandle extends RemoteHandle
{
    /**
     * Get the name of this Context.
     *
     * @return  The Context name.
     */
    public String getName() throws IOException;

    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList() throws IOException;
    
    /**
     * Stop receiving update messages from this context.
     * 
     * @throws java.io.IOException if connection is lost.
     */
    public void suspendUpdates() throws IOException;
    
    /**
     * Start receiving update messages from this context.
     * 
     * @throws java.io.IOException if the connection is lost.
     */
    public void resumeUpdates() throws IOException;
    
    /**
     * Send the provided Object as the payload of a message.
     * 
     * @param   payload     [in]    Supplies the message payload.
     */
    public void sendMessagePayload(Object payload) throws MessagingException, MessageTypeException, PermissionException, IOException;
}
