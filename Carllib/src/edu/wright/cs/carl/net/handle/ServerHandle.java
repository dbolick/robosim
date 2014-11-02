
package edu.wright.cs.carl.net.handle;

import java.util.List;

import java.io.IOException;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.security.PermissionException;


/**
 * This interface represents the methods that a client will use to interact with
 * a Server.
 * 
 * @author  Duane Bolick
 */
public interface ServerHandle extends RemoteHandle
{
    /**
     * Get the name of this server.
     *
     * @return  The server name.
     */
    public String getName() throws IOException;

    /**
     * Send the provided Object as the payload of a message.
     * 
     * @param   payload     [in]    Supplies the message payload.
     */
    public void sendMessagePayload(Object payload) throws MessagingException, MessageTypeException, PermissionException, IOException;   
    
    /**
     * Client Management
     */
    
    /**
     * Get the number of connected clients.
     * 
     * @return  The current number of clients.
     */
    public int getCurrentNumClients() throws IOException;
    
    /**
     * Get the maximum number of clients that can connect.
     * 
     * @return  The maximum number of clients.
     */
    public int getMaxNumClients() throws IOException;
    
    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList() throws IOException;


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
    public String getContextName(String contextID) throws IOException;

    /**
     * Get the number of clients that have joined a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The number of clients who have joined the Context.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public int getCurrentNumContextClients(String contextID) throws ServerException, IOException;
    
    /**
     * Get the maximum number of clients that can join a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The maximum number of clients who can join the Context.
     * 
     * @throws ServerException if the Context does not exist.
     */
    public int getMaxNumContextClients(String contextID) throws ServerException, IOException;
      
    /**
     * Get the Context type.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The Context type.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextType(String contextID) throws ServerException, IOException;
    
    /**
     * Get the Context description
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  The Context description.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextDescription(String contextID) throws ServerException, IOException;

    /**
     * Get a list of IDs of all contexts on this server.
     * 
     * @return  A List of IDs of all contexts.
     */
    public List<String> getContextList() throws IOException;
    
    /**
     * Get context information.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  The ContextInformation for the context.
     * 
     * @throws  IOException if the connection to the server is lost.
     */
    public ContextInformation getContextInformation(String contextID) throws ServerException, IOException;

    /**
     * Check to see if the Context is active on this Server.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @return  True if the Context is active, false otherwise.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public boolean isContextActive(String contextID) throws ServerException, IOException;

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
    public ContextHandle joinContext(String contextID) throws ContextException, ServerException, DuplicateHandleException, PermissionException, IOException;
    
    /**
     * Leave a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context.
     * 
     * @throws  ContextException if the client is not connected to the Context.
     * @throws  ServerException if the Context does not exist.
     */
    public void leaveContext(String contextID) throws ContextException, ServerException, IOException;
    
}
