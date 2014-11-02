
package edu.wright.cs.carl.net.server;

import edu.wright.cs.carl.net.event.ServerEventListener;
import edu.wright.cs.carl.net.event.ServerEvent;
import edu.wright.cs.carl.security.accounts.AccountManager;

import java.util.*;

import java.io.IOException;

import java.security.Principal;

import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;


import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.RemoteHandleManager;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.security.DefaultAclEntry;
import edu.wright.cs.carl.security.UserCredentials;
import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.security.permissions.ContextPermission;
import edu.wright.cs.carl.security.permissions.ServerPermission;


/**
 * Default Server implementation.
 *
 * @author  Duane Bolick
 * 
 * @see     Server
 */
public class DefaultServer implements Server
{
    String name;
    String uniqueID;

    AccountManager accounts;
    ContextManager contexts;
    RemoteHandleManager clients;
    Acl acl;
    Map<String, Callback> serverCallbacks;
    ServerEventListener listener;
    
    public DefaultServer(String name, AccountManager accountManager, ContextManager contextManager, RemoteHandleManager handleManager, Acl acl)
    {
        this.name = name;
        this.uniqueID = UUID.randomUUID().toString();

        this.accounts = accountManager;
        this.contexts = contextManager;
        this.clients = handleManager;
        this.acl = acl;
        this.serverCallbacks = new HashMap<String, Callback>();
    }
    
    /**
     * Register a state change listener.
     *
     * @param   listener    [in]    Supplies the listener.
     */
    public void addStateChangeListener(ServerEventListener listener)
    {
        this.listener = listener;
    }

    private void notifyListeners(ServerEvent event)
    {
        if(this.listener != null) {
            this.listener.serverEventOccurred(event);
        }
    }

    public String getName()
    {
        return this.name;
    }

    /**
     * Get the unique ID of this Server.
     *
     * @return  The unique ID.
     */
    public String getUniqueID()
    {
        return this.uniqueID;
    }


    /**
     * Set the Server name.
     *
     * @param   newServerName   [in]    Supplies the new Server name.
     */
    public void setName(String newServerName)
    {
        this.name = newServerName;
    }

    /**
     * AccessControlled interface methods.
     */
    
    /**
     * Checks if the Principal has the given Permission in the context of this
     * object's Acl.
     * 
     * @param   principal   [in]    Supplies a reference to the Principal
     * @param   permission  [in]    Supplies a reference to the Permission
     * 
     * @return  True if the given Principal has the given Permission in this
     *          Acl, false otherwise.
     */
    public boolean hasPermission(Principal principal, Permission permission)
    {
        return this.acl.checkPermission(principal, permission);
    }
    
    /**
     * Get a reference to the Access Control List (Acl) of this object.  The
     * caller must be an Owner of the Acl.
     * 
     * @param   caller  [in]    Supplies a reference to the Principal
     * 
     * @return  A reference to the Acl.
     * 
     * @throws  PermissionException if the caller is not an owner of the Acl.
     */
    public Acl getAcl(Principal caller) throws PermissionException
    {
        if(this.acl.isOwner(caller) == false){
            throw new PermissionException(caller.getName(), "DefaultServer.getAcl", "You are not an owner of this access control list.");
        }
        return this.acl;
    }

    
    /**
     * Account Management
     */
     
    
    /**
     * Get the account manager of this server.
     * 
     * @return  The account manager.
     */
    public AccountManager getAccounts()
    {
        return this.accounts;
    }
    
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
    public boolean addAccount(Principal caller, UserCredentials newAccount) throws PermissionException
    {
        if(this.acl.isOwner(caller) == false) {
            throw new PermissionException(caller.getName(), "DefaultServer.addAccount", "You do not have permission to modify Access Control Lists on this Server.");
        }        
        
        if(this.acl.checkPermission(caller, new ServerPermission("modAccount")) == false){
            throw new PermissionException(caller.getName(), "DefaultServer.addAccount", "You do not have permission to modify user accounts on this Server.");
        }
        
        try {
            // TODO: Use the Acl instead of giving everybody all permissions.
            this.acl.addOwner(caller, newAccount);
            AclEntry newUserEntry = new DefaultAclEntry(newAccount);
            newUserEntry.addPermission(new ServerPermission("modAccount"));
            newUserEntry.addPermission(new ServerPermission("modContext"));
            newUserEntry.addPermission(new ServerPermission("sendMessage"));
            //newUserEntry.addPermission(new ServerPermission("kickUser"));
            newUserEntry.addPermission(new ContextPermission("join"));
            newUserEntry.addPermission(new ContextPermission("sendMessage"));
            //newUserEntry.addPermission(new ContextPermission("kickUser"));
            this.acl.addEntry(caller, newUserEntry);
        }
        catch(NotOwnerException e) {
            //
            // This should never happen.
            //
            e.printStackTrace();
            System.exit(1);            
        }
        
        return this.accounts.addUserAccount(newAccount);         
    }
    
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
    public UserCredentials deleteAccount(Principal caller, String username) throws ServerException, PermissionException
    {
        if(this.acl.isOwner(caller) == false) {
            throw new PermissionException(caller.getName(), "DefaultServer.deleteAccount", "You do not have permission to modify Access Control Lists on this Server.");
        }
        
        if(this.acl.checkPermission(caller, new ServerPermission("modAccount")) == false){
            throw new PermissionException(caller.getName(), "DefaultServer.deleteAccount", "You do not have permission to modify user accounts on this Server.");
        }
        
        UserCredentials account = this.accounts.removeUserAccount(username);
        
        if(account == null) {
            throw new ServerException("DefaultServer.deleteAccount: Account does not exist.");
        }
        
        AclEntry currentEntry = null;
        Enumeration<AclEntry> en = this.acl.entries();
        while(en.hasMoreElements()) {
            currentEntry = en.nextElement();
            if(currentEntry.getPrincipal().getName().equals(account.getName())) {
                try {
                    this.acl.removeEntry(caller, currentEntry);
                }
                catch(NotOwnerException e) {
                    //
                    // This should never happen.
                    //
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        
        return account;
    }
   
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
    public UserCredentials modifyAccount(Principal caller, String username, UserCredentials newCredentials) throws ServerException, PermissionException
    {
        if(this.acl.checkPermission(caller, new ServerPermission("modAccount")) == false){
            throw new PermissionException(caller.getName(), "DefaultServer.modifyAccount", "You do not have permission to modify user accounts on this Server.");
        }
        
        UserCredentials oldCredentials = this.accounts.getAccount(username);
        
        if(oldCredentials == null) {
            throw new ServerException("DefaultServer.modifyAccount:  Account with username " + username + " does not exist.");
        }
        
        try {
            this.deleteAccount(caller, oldCredentials.getName());
            this.addAccount(caller, newCredentials);
        }
        catch(Exception e) {
            //
            // At this point, neither of these methods should throw an
            // exception.
            //
            e.printStackTrace();
            System.exit(1);
        }
        
        return oldCredentials;        
    }

    
    /**
     * Client Management
     */
    
    /**
     * Get the number of connected clients.
     * 
     * @return  The current number of clients.
     */
    public int getCurrentNumClients()
    {
        return this.clients.getCurrentNumHandles();
    }
    
    /**
     * Get the maximum number of clients that can connect.
     * 
     * @return  The maximum number of clients.
     */
    public int getMaxNumClients()
    {
        return this.clients.getMaxNumHandles();
    }
    
    /**
     * Set the maximum number of clients.
     * 
     * @param   maxNumClients   [in]    Supplies the maximum number of clients.
     */
    public void setMaxNumClients(int maxNumClients)
    {
        this.clients.setMaxNumHandles(maxNumClients);
    }
    
    /**
     * Get a list of usernames of all connected client.
     * 
     * @return  A List containing usernames of all connected clients.
     */
    public List<String> getClientList()
    {
        return this.clients.getHandleIDs();
    }
    
    /**
     * Get a reference to a ClientHandle instance by username.
     * 
     * @param   username    [in]    Supplies the username of the ClientHandle.
     * 
     * @return  A reference to the ClientHandle instance with the given
     *          username, or null if the ClientHandle does not exist.
     * 
     */
    public ClientHandle getClientHandle(String username)
    {
        return (ClientHandle)this.clients.getHandle(username);
    }

    
    /**
     * Connection Management
     */
           
    /**
     * Determine if a user is connected to this server.
     * 
     * @param   username    [in]    Supplies the username.
     * 
     * @return  True if a user with the provided username is connected, false
     *          otherwise.
     */
    public boolean isConnected(String username)
    {
        if(this.clients.getHandle(username) == null) {
            return false;
        }
        
        return true;
    }

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
    public ServerMessage connectClient(ClientHandle clientHandle, Callback serverCallback) throws DuplicateHandleException, PermissionException
    {
        ServerMessage loginMessage = new ServerMessage(this.name, this.uniqueID);
        Object loginPayload = null;
        String clientHandleName = null;
        
        try {
            clientHandleName = clientHandle.getUniqueID();
            loginPayload = this.accounts.authenticate(clientHandle.getCredentials());
        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }
                
        if(loginPayload == null) {
            throw new PermissionException("Login credentials bad.  Please try again or contact the server administrator.");
        }
        
        loginMessage.payload = loginPayload;
        
        //
        // addHandle throws DuplicateHandleConnection
        //
        if(this.clients.addHandle(clientHandle) == false) {
            return null;
        }
        
        this.serverCallbacks.put(clientHandleName, serverCallback);


        this.notifyListeners(
                new ServerEvent(ServerEvent.EventType.USER_JOINED, clientHandleName));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.USER_JOINED, clientHandleName);
        ServerMessage sm = new ServerMessage(this.name, this.uniqueID, event);
        this.broadcastMessage(sm);

        return loginMessage;        
    }
    
    /**
     * Disconnect the given client from the server.  The client should be logged
     * into this server.
     * 
     * @param   username   [in]     Supplies the username.
     * 
     * @throws  ServerException if the user is not active on this
     *          Server. 
     */ 
    public void disconnectClient(String username) throws ServerException
    {
        if(this.isConnected(username) == false) {
            throw new ServerException("DefaultServer.disconnectClient: User is not connected.");
        }
        
        this.contexts.disconnectClientFromAll(username);
        
        ClientHandle removedHandle = (ClientHandle) this.clients.removeHandle(username);
        
        if(removedHandle == null) {
            throw new AssertionError();
        }
        
        if(this.serverCallbacks.remove(username) == null) {
            throw new AssertionError();
        }
        
        try {
            removedHandle.disconnect(null);
        }
        catch(IOException e) {
            e.printStackTrace();
            //
            // Do nothing, because if the connection is dropped, we don't care.
            //
        }

        this.notifyListeners(
                new ServerEvent(ServerEvent.EventType.USER_LEFT, username));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.USER_LEFT, username);
        ServerMessage sm = new ServerMessage(this.name, this.uniqueID, event);
        this.broadcastMessage(sm);
    }
    
    /**
     * Disconnect all clients.
     */
    public void disconnectAll()
    {
        Iterator<String> it = this.clients.getHandleIDs().iterator();
        String currentHandleName = null;
        while(it.hasNext()) {
            currentHandleName = it.next();
            this.contexts.disconnectClientFromAll(currentHandleName);
            try {
                this.disconnectClient(currentHandleName);
            }
            catch(ServerException e) {
                throw new AssertionError();
            }
        }
        this.clients.clear();
    }

    /**
     * Called when a client disconnects from this server.
     *
     * @param   username    [in]    Supplies the username.
     */
    public void clientHasLeftServer(String username)
    {
        Iterator<String> it = this.contexts.getContextList().iterator();
        while(it.hasNext()) {
            this.contexts.getContext(it.next()).clientLeft(username);
        }

        ClientHandle removedHandle = (ClientHandle) this.clients.removeHandle(username);

        if(removedHandle == null) {
            throw new AssertionError();
        }

        if(this.serverCallbacks.remove(username) == null) {
            throw new AssertionError();
        }

        this.listener.serverEventOccurred(
                new ServerEvent(ServerEvent.EventType.USER_LEFT, username));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.USER_LEFT, username);
        ServerMessage sm = new ServerMessage(this.name, this.uniqueID, event);
        this.broadcastMessage(sm);
    }
    
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
    public String getContextName(String contextID)
    {
        return this.contexts.getContext(contextID).getName();
    }

    /**
     * Get information about a context.
     *
     * @param   contextID   [in]    Supplies the context ID.
     *
     * @return  The ContextInformation.
     *
     * @throws  ServerException if the context does not exist.
     */
    public ContextInformation getContextInformation(String contextID) throws ServerException
    {
        ContextInformation info = new ContextInformation();

        //
        // All of these throw a ServerException if the context with the given
        // name do not exist.
        //
        info.id = contextID;
        info.currentNumClients = this.getCurrentNumContextClients(contextID);
        info.maxNumClients = this.getMaxNumContextClients(contextID);
        info.name = this.getContextName(contextID);
        info.description = this.getContextDescription(contextID);
        info.type = this.getContextType(contextID);
        info.isActive = this.isContextActive(contextID);

        return info;
    }

    /**
     * Get the number of clients that have joined a Context.
     *
     * @param   contextID     [in]    Supplies the context ID.
     *
     * @return  The number of clients who have joined the Context.
     *
     * @throws  ServerException if the Context does not exist.
     */
    public int getCurrentNumContextClients(String contextID) throws ServerException
    {
        //
        // Throws ServerException if the context with this name doesn't exist.
        //
        return this.getContext(contextID).getCurrentNumClients();
    }
    
    /**
     * Get the maximum number of clients that can join a Context.
     *
     * @param   contextID     [in]    Supplies the context ID.
     *
     * @return  The maximum number of clients who can join the Context.
     *
     * @throws ServerException if the Context does not exist.
     */
    public int getMaxNumContextClients(String contextID) throws ServerException
    {
        //
        // Throws ServerException if the context with this name doesn't exist.
        //
        return this.getContext(contextID).getMaxNumClients();

    }
    
    /**
     * Set the maximum number of clients for a Context.
     * 
     * @param   contextID               [in]    Supplies the ID of the
     *                                          Context.
     * @param   maxNumContextClients    [in]    Supplies the maximum number of
     *                                          clients.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public void setMaxNumContextClients(String contextID, int maxNumContextClients) throws ServerException
    {
        //
        // Throws ServerException if the context with this name doesn't exist.
        //
        this.getContext(contextID).setMaxNumClients(maxNumContextClients);

    }        
        
    /**
     * Get the Context type.
     * 
     * @param   contextID       [in]    Supplies the ID of the Context.
     * 
     * @return  The Context type.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextType(String contextID) throws ServerException
    {
        //
        // Throws ServerException if the context with this name doesn't exist.
        //
        return this.getContext(contextID).getType();

    }          
    
    /**
     * Get the Context description
     * 
     * @param   contextID           [in]    Supplies the ID of the Context.
     * 
     * @return  The Context description.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public String getContextDescription(String contextID) throws ServerException
    {
        //
        // Throws ServerException if the context with this name doesn't exist.
        //
        return this.getContext(contextID).getDescription();

    }      

    /**
     * Get a list of IDs of all contexts on this server.
     * 
     * @return  A List of IDs of all contexts.
     */
    public List<String> getContextList()
    {
        return this.contexts.getContextList();
    }
   
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
    public Context getContext(String contextID) throws ServerException
    {
        Context context = this.contexts.getContext(contextID);
        
        if(context == null) {
            throw new ServerException("Context does not exist.");
        }
        
        return context;
    }

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
    public void addContext(Principal caller, Context newContext) throws ServerException, PermissionException
    {
        if(this.acl.checkPermission(caller, new ServerPermission("modContext")) == false){
            throw new PermissionException(caller.getName(), "DefaultServer.addContext", "You do not have permission to add Contexts to this Server.");
        }
        
        if(this.contexts.addContext(newContext) == false) {
            throw new ServerException("DefaultServer.addContext: Context with the same name already exists.");
        }

        this.notifyListeners(
                new ServerEvent(ServerEvent.EventType.CONTEXT_ADDED, newContext.getName()));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.CONTEXT_ADDED, newContext.getName());
        ServerMessage sm = new ServerMessage(this.name, this.uniqueID, event);
        this.broadcastMessage(sm);
    }
       
    /**
     * Remove Context from the collection of active Contexts
     * 
     * @param   caller      [in]    Provides a reference to the caller.
     * @param   contextID   [in]    Supplies the ID of the Context to be
     *                              removed.
     * 
     * @return  The Context that was removed.
     * 
     * @throws  ServerException if the context does not exist.
     * @throws  PermissionException if the caller lacks sufficient Permission.
     */
    public Context removeContext(Principal caller, String contextID) throws ServerException, PermissionException
    {
        if(this.acl.checkPermission(caller, new ServerPermission("modContext")) == false){
            throw new PermissionException(caller.getName(), "DefaultServer.removeContext", "You do not have permission to remove Contexts from this Server.");
        }
        
        try {
            this.deactivateContext(contextID);
        }
        catch(ContextException e) {
            throw new AssertionError(e);
        }

        Context removedContext = this.contexts.removeContext(contextID);
        if(removedContext == null) {
            throw new ServerException("DefaultServer.removeContext: Context does not exist on this server.");
        }
        
        //removedContext.deactivateContext();
        
        this.notifyListeners(
                new ServerEvent(ServerEvent.EventType.CONTEXT_REMOVED, contextID));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.CONTEXT_REMOVED, contextID);
        ServerMessage sm = new ServerMessage(this.name, this.uniqueID, event);
        this.broadcastMessage(sm);

        return removedContext;         
    }
    
    /**
     * Check to see if the Context is active on this Server.
     * 
     * @param   contextID       [in]    Supplies the ID of the Context.
     * 
     * @return  True if the Context is active, false otherwise.
     * 
     * @throws  ServerException if the Context does not exist.
     */
    public boolean isContextActive(String contextID) throws ServerException
    {
        return this.contexts.getContext(contextID).isActive();
    }
    
    /**
     * Activate a Context.
     * 
     * @param   contextID       [in]    Supplies the ID of the Context.
     * 
     * @throws  ServerException if the Context does not exist.
     * @throws  ContextException if the Context is already active.
     */
    public void activateContext(String contextID) throws ServerException, ContextException
    {
        this.contexts.getContext(contextID).activateContext();
        this.notifyListeners(
                new ServerEvent(ServerEvent.EventType.CONTEXT_EVENT, contextID));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.CONTEXT_EVENT, contextID);
        ServerMessage sm = new ServerMessage(this.name, this.uniqueID, event);
        this.broadcastMessage(sm);
    }

    /**
     * Deactivate a Context.
     * 
     * @param   contextID     [in]    Supplies the ID of the Context to be
     *                                deactivated.
     * 
     * @throws  ServerException if the Context does not exist.
     * @throws  ContextException if the Context is not active.
     */
    public void deactivateContext(String contextID) throws ServerException, ContextException
    {        
        this.contexts.getContext(contextID).deactivateContext();

        this.notifyListeners(
                new ServerEvent(ServerEvent.EventType.CONTEXT_EVENT, contextID));

        ServerEvent event = new ServerEvent(ServerEvent.EventType.CONTEXT_EVENT, contextID);
        ServerMessage sm = new ServerMessage(this.name,this.uniqueID, event);
        this.broadcastMessage(sm);
    }

    /**
     * Add a client to a context.  Context implementations are AccessControlled
     * objects, and may limit access by Principal.
     * 
     * @param   contextID   [in]    Supplies the ID of the context.
     * @param   username    [in]    Supplies username of the client.
     * 
     * @return  True if the client was added successfully, false if the Context
     *          was full.
     * 
     * @throws  ContextException if the Context is not active.
     * @throws  ServerException if either the context name or username do not
     *          exist on this server.
     * @throws  DuplicateHandleException if the user has already joined.
     * @throws  PermissionException if the client does not have privileges to
     *          join the context.
     */
    public boolean connectClientToContext(String contextID, String username) throws ContextException, ServerException, DuplicateHandleException, PermissionException
    {
        ClientHandle handle = (ClientHandle)this.clients.getHandle(username);
        if(handle == null) {
            throw new ServerException("DefaultServer.addClientToContext: User is not connected.");
        }
        
        //
        // Throws ServerException (from getContext), DuplicateHandleException,
        // and PermissionException (the last two are both from connectClient).
        //
        return this.getContext(contextID).connectClient(handle);
    }
     
    /**
     * Remove a client from a context.
     * 
     * @param   contextID   [in]    Supplies the ID of the context.
     * @param   username    [in]    Supplies the username of the client.
     * 
     * @throws  ServerException if either the context ID or username do not
     *          exist on this server.
     * @throws  ContextException if the user has not joined the Context.
     */
    public void disconnectClientFromContext(String contextID, String username) throws ServerException, ContextException
    {
        if(this.clients.getHandle(username) == null) {
            throw new ServerException("DefaultServer.removeClientFromContext: Client does not exist.");
        }

        //
        // Throws ServerException if context does not exist.
        //
        Context context = this.contexts.getContext(contextID);
        if(context == null) {
            throw new ServerException("DefaultServer.removeClientFromContext: Context does not exist.");
        }
        
        context.disconnectClient(username);
    }


    /**
     * Messaging.
     */

    
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
    public void handleIncomingMessage(Message message)
            throws MessagingException, MessageTypeException, PermissionException
    {
        if(message instanceof ClientMessage) {
            ClientMessage clientMessage = (ClientMessage) message;

            if(this.hasPermission(clientMessage.clientCredentials, new ServerPermission("sendMessage")) == false) {
                throw new PermissionException("You lack permission to send top-level messages on this server.");
            }

            //
            // Private Message
            //
            if(clientMessage.payload instanceof PrivateMessage) {
                PrivateMessage pm = (PrivateMessage)clientMessage.payload;
                ServerMessage sm = new ServerMessage(this.name, this.uniqueID, pm);
                this.unicastMessage(sm, pm.recipientName);
                return;
            }

            //
            // Kicking User
            //
            else if(clientMessage.payload instanceof UserRemovalRequest) {

                //
                // Check permissions.
                //
                if(this.hasPermission(clientMessage.clientCredentials, new ServerPermission("kickUser")) == false) {
                    throw new PermissionException("You lack permission to kick other users.");
                }

                //
                // Remove the user.
                //
                UserRemovalRequest removalRequest = (UserRemovalRequest) clientMessage.payload;

                try {
                    this.disconnectClient(removalRequest.username);
                }
                catch(ServerException e) {
                    throw new AssertionError(e);
                }

                //
                // And tell everybody about it.
                //
                UserListUpdate update = new UserListUpdate(
                        removalRequest.username + " has been kicked by " + clientMessage.clientCredentials.getName(),
                        removalRequest.username,
                        UserStatusChange.Type.LOGOFF);
                
                ServerMessage sm = new ServerMessage(this.name, this.uniqueID, update);
                this.broadcastMessage(sm);

                ServerEvent event = new ServerEvent(ServerEvent.EventType.USER_LEFT, removalRequest.username);
                sm = new ServerMessage(this.name, this.uniqueID, event);
                this.broadcastMessage(sm);
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


    public void unicastMessage(Message message, String username)
            throws MessagingException, MessageTypeException, PermissionException
    {
        ClientHandle client = (ClientHandle) this.clients.getHandle(username);

        if(client == null) {
            throw new AssertionError("DefaultServer.unicastMessage: Client " + username + " does not exist.");
        }

        try {
            client.sendMessage(message);
        }
        catch(IOException e) {
            e.printStackTrace();
            this.clientHasLeftServer(username);
        }
    }

    /**
     * Send message to all clients.
     *
     * @param   message     [in]    Supplies the message to be sent.
     */
    public void broadcastMessage(Message message)
    {
        Iterator<String> it = this.clients.getHandleIDs().iterator();
        String currentUsername = null;
        ClientHandle currentClient = null;

        List<String> lostClients = new ArrayList<String>();

        //
        // Iterate over all the clients, sending the message to them all.
        //
        while(it.hasNext()) {
            currentUsername = it.next();
            currentClient = (ClientHandle) this.clients.getHandle(currentUsername);

            try {
                currentClient.sendMessage(message);
            }
            catch(MessageTypeException e) {

            }
            catch(MessagingException e) {
                throw new AssertionError(e);
            }
            catch(PermissionException e) {

            }
            catch(IOException e) {
               e.printStackTrace();
               lostClients.add(currentUsername);
            }
        }

        //
        // Finally, drop any clients we lost from this Context.
        //
        it = lostClients.iterator();
        while(it.hasNext()) {
            this.clientHasLeftServer(it.next());
        }
    }
}
