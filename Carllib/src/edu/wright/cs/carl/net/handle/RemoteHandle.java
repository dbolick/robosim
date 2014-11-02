
package edu.wright.cs.carl.net.handle;

import java.io.IOException;

import java.util.UUID;

import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.MessagingException;
import edu.wright.cs.carl.net.message.MessageTypeException;

import edu.wright.cs.carl.security.PermissionException;


/**
 * This interface must be implemented by all classes representing remote
 * connections.  All methods of all RemoteHandle type interfaces and
 * implementations must throw IOException.
 * 
 * @author  Duane Bolick
 */
public interface RemoteHandle
{
    /**
     * Get the unique identifier of the object to which this handle refers.
     * 
     * @return  The unique identifier of the handle.
     * 
     * @throws  IOException if the connection is lost.
     */
    public String getUniqueID() throws IOException;

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
    public void sendMessage(Message message) throws MessagingException, MessageTypeException, PermissionException, IOException;   
    
    /**
     * The remote entity calls this on us to sever the connection.  If a client
     * wants to sever its connection to a remote entity, it should call that
     * handle's disconnect method.
     * 
     * @param   obj     [in]    Supplies an object as a parameter.
     *
     * @throws  IOException but we don't care.
     */
    public void disconnect(Object obj) throws IOException;
}
