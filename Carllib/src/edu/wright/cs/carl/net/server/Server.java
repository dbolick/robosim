
package edu.wright.cs.carl.net.server;

import edu.wright.cs.carl.net.event.ServerEventListener;
import java.util.List;

import java.security.Principal;

import edu.wright.cs.carl.net.message.ServerMessage;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.message.MessageRecipient;

import edu.wright.cs.carl.security.accounts.AccountManager;
import edu.wright.cs.carl.security.UserCredentials;
import edu.wright.cs.carl.security.AccessControlled;
import edu.wright.cs.carl.security.PermissionException;


/**
 * <p>
 * The Server interface declares the basic functionality of a Server
 * implementation.  In this framework, all role-specific functionality is
 * provided through the use of <code>context</code>s.
 * </p>
 * 
 * <p>
 * Implementations of this interface represent the core logic module of the
 * application - it should provide basic authentication through the use of 
 * UserCredentials, manage the pool of connected clients, and manage all context
 * instances.
 * </p>
 * 
 * <p>
 * There are two varieties of methods defined in this interface - methods that
 * are intended to be invoked (indirectly, of course) by a remote client, and
 * methods that will only be called by the application.  In both cases, some
 * methods require that the caller has sufficient permissions.
 * </p>
 * 
 * <p>
 * A Server implementation doesn't do very much without at least one Context.
 * In order to define the specific functionality of a server, you need to
 * implement a Context providing that functionality (i.e., a chatroom Context, a
 * robot arena context, etc.)
 * </p>
 * 
 * <p>
 * Access to Context instances is obtained by using the connectClientToContext
 * method, which returns a reference to that Context.  Each Context is
 * responsible for controlling access to itself, as well as controlling access
 * to its resources.
 * </p>
 * 
 * @author  Duane Bolick
 * @see     Context
 */
public interface Server extends AccessControlled, MessageRecipient
{
    /**
     * Register a state change listener.
     * 
     * @param   listener    [in]    Supplies the listener.
     */
    public void addStateChangeListener(ServerEventListener listener);


    /**
     * Account Management
     */
     
    /**
     * Get the account manager of this server.
     * 
     * @return  The account manager.
     */
    public AccountManager getAccounts();
    
    /**
     * Add a new user account to this server.  The username contained in the
     * UserCredentials must be unique.
     * 
     * @param   caller      [in]    Provides a reference to the caller.
     * @param newAccount    [in]    Supplies the UserCredentials instance
     *                              representing the account to be added.
     * 
     * @return  True if the account was added successfully, false if the
     *          username supplied by the UserCredentials is not unique.
     * 
     * @throws  PermissionException if the caller lacks sufficient Permission
     */
    public boolean addAccount(Principal caller, UserCredentials newAccount) throws PermissionException;
    
    /**
     * Delete an existing account.
     * 
     * @param   caller      [in]    Provides a reference to the caller.
     * @param   username    [in]    Supplies the username of the account to be
     *                              deleted.
     * 
     * @return  The UserCredentials instance that was deleted.
     * 
     * @throws  ServerException if the account does not exist.
     * @throws  PermissionException if the caller lacks sufficient Permission
     */
    public UserCredentials deleteAccount(Principal caller, String username) throws ServerException, PermissionException;
   
    /**
     * Modify an existing account.
     * 
     * @param   caller          [in]    Provides a reference to the caller.
     * @param   username        [in]    Supplies the username of the account to
     *                                  be modified.
     * @param   newCredentials  [in]    Supplies the new UserCredentials
     *                                  instance for the account.
     * 
     * @return  The modified UserCredentials object.
     * 
     * @throws  ServerException if the username does not exist.
     * @throws  PermissionException if the caller lacks sufficient Permission
     */
    public UserCredentials modifyAccount(Principal caller, String username, UserCredentials newCredentials) throws ServerException, PermissionException;

    
    /**
     * Client Management
     */
    
    /**
     * Get the number of connected clients.
     * 
     * @return  The current number of clients.
     */
    public int getCurrentNumClients();
    
    /**
     * Get the maximum number of clients that can connect.
     * 
     * @return  The maximum number of clients.
     */
    public int getMaxNumClients();
    
    /**
     * Set the maximum number of clients.
     * 
     * @param   maxNumClients   [in]    Supplies the maximum number of clients.
     */
    public void setMaxNumClients(int maxNumClients);
    
    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList();
    
    /**
     * Get a reference to a ClientHandle instance by username.
     * 
     * @param   username    [in]    Supplies the username of the ClientHandle.
     * 
     * @return  A reference to the ClientHandle instance with the given
     *          username, or null if the ClientHandle does not exist.
     * 
     */
    public ClientHandle getClientHandle(String username);

    
    /**
     * Connection Management
     */
    
    /**
     * Get the unique ID of this Server.
     *
     * @return  The unique ID.
     */
    public String getUniqueID();
    
    /**
     * Get the Server name.
     * 
     * @return  The Server name.
     */
    public String getName();

    /**
     * Set the Server name.
     *
     * @param   newServerName   [in]    Supplies the new Server name.
     */
    public void setName(String newServerName);
    
    /**
     * Determine if a user is connected to this server.
     * 
     * @param   username    [in]    Supplies the username.
     * 
     * @return  True if a user with the provided username is connected, false
     *          otherwise.
     */
    public boolean isConnected(String username);  

    /**
     * Connect a client to this server.  This may fail if the maximum number of
     * users is already connected, or if the client is not authorized to
     * connect to this Server.  
     * 
     * @param   clientHandle    [in]    Supplies a reference to the handle.
     * @param   serverCallback  [in]    Supplies a reference to the callback.
     * 
     * @return  The login message if successful, null if the server is full.
     * 
     * @throws  ServerException if there is an active ClientHandle with the same
     *          username as the supplied ClientHandle (i.e., if this user is
     *          already connected to this Server).
     * @throws  PermissionException if the client does not have permission to
     *          access this Server.
     */
    public ServerMessage connectClient(ClientHandle clientHandle, Callback serverCallback) throws DuplicateHandleException, PermissionException;
    
    /**
     * Disconnect the given client from the server.  The client should be logged
     * into this server.
     * 
     * @param   username   [in]     Supplies the username.
     * 
     * @throws  ServerException if the user is not active on this
     *          Server. 
     */ 
    public void disconnectClient(String username) throws ServerException;
    
    /**
     * Disconnect all clients.
     */
    public void disconnectAll();

    /**
     * Called when a client disconnects from this server.
     *
     * @param   username    [in]    Supplies the username.
     */
    public void clientHasLeftServer(String username);

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
    public String getContextName(String contextID);

    /**
     * Get information about a context.
     *
     * @param   contextID     [in]    Supplies the context ID.
     *
     * @return  The ContextInformation.
     *
     * @throws  ServerException if the context does not exist.
     */
    public ContextInformation getContextInformation(String contextID) throws ServerException;

    /**
     * Get the number of clients that have joined a Context.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  The number of clients who have joined the Context.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public int getCurrentNumContextClients(String contextID) throws ServerException;
    
    /**
     * Get the maximum number of clients that can join a Context.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  The maximum number of clients who can join the Context.
     * 
     * @throws ServerException if the Context does not exist.
     */
    public int getMaxNumContextClients(String contextID) throws ServerException;
    
    /**
     * Set the maximum number of clients for a Context.
     * 
     * @param   contextID               [in]    Supplies the context ID.
     * @param   maxNumContextClients    [in]    Supplies the maximum number of
     *                                          clients.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public void setMaxNumContextClients(String contextID, int maxNumContextClients) throws ServerException;
        
    /**
     * Get the Context type.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  The Context type.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextType(String contextID) throws ServerException;
    
    /**
     * Get the Context description
     * 
     * @param   contextName     [in]    Supplies the name of the Context.
     * 
     * @return  The Context description.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextDescription(String contextID) throws ServerException;

    /**
     * Get a list of IDs of all contexts on this server.
     * 
     * @return  A List of IDs of all contexts.
     */
    public List<String> getContextList();
    
    /**
     * Get a reference to a Context by ID.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  A reference to the Context.
     * 
     * @throws  ServerException if the Context with the given name does not
     *          exist.
     */
    public Context getContext(String contextID) throws ServerException;

    /**
     * Add a new Context to the collection of active contexts.
     * 
     * @param   caller      [in]    Provides a reference to the caller.
     * @param   newContext  [in]    Supplies a reference to the context to be
     *                              added.
     * 
     * @throws  ServerException if there exists a Context with the same name.
     * @throws  PermissionException if the caller lacks sufficient Permission.
     */
    public void addContext(Principal caller, Context newContext) throws ServerException, PermissionException;
       
    /**
     * Remove Context from the collection of active Contexts
     * 
     * @param   caller      [in]    Provides a reference to the caller.
     * @param   contextID   [in]    Supplies the context ID to be removed.
     * 
     * @return  The Context that was removed.
     * 
     * @throws  ServerException if the context does not exist.
     * @throws  PermissionException if the caller lacks sufficient Permission.
     */
    public Context removeContext(Principal caller, String contextID) throws ServerException, PermissionException;
    
    /**
     * Check to see if the Context is active on this Server.
     * 
     * @param   contextID     [in]    Supplies the context ID.
     * 
     * @return  True if the Context is active, false otherwise.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public boolean isContextActive(String contextID) throws ServerException;
    
    /**
     * Activate a Context.
     * 
     * @param   contextID     [in]    Supplies the context ID to be activated.
     * 
     * @throws  ServerException if the Context does not exist.
     * @throws  ContextException if the Context is already active.
     */
    public void activateContext(String contextID) throws ServerException, ContextException;

    /**
     * Deactivate a Context.
     * 
     * @param   contextID     [in]    Supplies the context ID to be deactivated.
     * 
     * @throws  ServerException if the Context does not exist.
     * @throws  ContextException if the Context is not active.
     */
    public void deactivateContext(String contextID) throws ServerException, ContextException;

    /**
     * Connect a client to a context.
     * 
     * @param   contextID       [in]    Supplies the context ID.
     * @param   username        [in]    Supplies username of the client.
     * 
     * @return  True if the client was connected to the Context, false if
     *          the Context is full.
     * 
     * @throws  ContextException if the Context is not active.
     * @throws  ServerException if either the context name or username do not
     *          exist on this server.
     * @throws  DuplicateHandleException if the user has already joined.
     * @throws  PermissionException if the client does not have privileges to
     *          join the context.
     */
    public boolean connectClientToContext(String contextID, String username) throws ContextException, ServerException, DuplicateHandleException, PermissionException;
     
    /**
     * Remove a client from a context.
     * 
     * @param   contextID       [in]    Supplies the context ID.
     * @param   username        [in]    Supplies the username of the client.
     * 
     * @throws  ServerException if either the context name or username do not
     *          exist on this server.
     * @throws  ContextException if the user has not joined the Context.
     */
    public void disconnectClientFromContext(String contextID, String username) throws ServerException, ContextException;

}
