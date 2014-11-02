
package edu.wright.cs.carl.net.handle;

import java.io.IOException;

import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.net.context.ContextException;

import edu.wright.cs.carl.security.UserCredentials;


/**
 * This interface represents the methods a user with administrative privileges
 * may invoke on a Server remotely.
 * 
 * @author  Duane Bolick
 */
public interface ServerAdminHandle extends ServerHandle
{
    /**
     * Account Management
     */
     
    /**
     * Add a new user account to this server.  The username contained in the
     * UserCredentials must be unique.
     * 
     * @param   caller      [in]    Provides a reference to the caller.
     * @param   newAccount  [in]    Supplies the UserCredentials instance
     *                              representing the account to be added.
     * 
     * @return  True if the account was added successfully, false if the
     *          username supplied by the UserCredentials is not unique.
     * 
     */
    public boolean addAccount(UserCredentials newAccount) throws IOException;
    
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
     */
    public UserCredentials deleteAccount(String username) throws ServerException, IOException;
   
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
     */
    public UserCredentials modifyAccount(String username, UserCredentials newCredentials) throws ServerException, IOException;
    
    
    /**
     * Client Management
     */       
    
    /**
     * Set the maximum number of clients.
     * 
     * @param   maxNumClients   [in]    Supplies the maximum number of clients.
     */
    public void setMaxNumClients(int maxNumClients) throws IOException;
    
    
    /**
     * Connection Management
     */
    
    /**
     * Disconnect the given client from the server.  The client should be logged
     * into this server.
     * 
     * @param   username   [in]     Supplies the username.
     * 
     * @throws  ServerException if the user is not active on this Server.
     */ 
    public void disconnectClient(String username) throws ServerException, IOException;    
    
    
    /**
     * Context Management
     */    
    
    /**
     * Set the maximum number of clients for a Context.
     * 
     * @param   contextName             [in]    Supplies the name of the
     *                                          Context.
     * @param   maxNumContextClients    [in]    Supplies the maximum number of
     *                                          clients.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public void setMaxNumContextClients(String contextName, int maxNumContextClients) throws ServerException, IOException;
    
    /**
     * Activate a Context.
     * 
     * @param   contextName     [in]    Supplies the name of the Context to be
     *                                  activated.
     * 
     * @throws  ServerException if the Context does not exist.
     * @throws  ContextException if the Context is already active.
     */
    public void activateContext(String contextName) throws ServerException, ContextException, IOException;      

    /**
     * Deactivate a Context.
     * 
     * @param   contextName     [in]    Supplies the name of the Context to be
     *                                  deactivated.
     * 
     * @throws  ServerException if the Context does not exist.
     * @throws  ContextException if the Context is not active.
     */
    public void deactivateContext(String contextName) throws ServerException, ContextException, IOException;    
    
    /**
     * Remove a client from a context.
     * 
     * @param   contextName     [in]    Supplies the name of the context.
     * @param   username        [in]    Supplies the username of the client.
     * 
     * @throws  ServerException if either the context name or username do not
     *          exist on this server.
     * @throws  ContextException if the user has not joined the Context.
     */
    public ClientHandle removeClientFromContext(String contextName, String username) throws ServerException, ContextException, IOException;
}
