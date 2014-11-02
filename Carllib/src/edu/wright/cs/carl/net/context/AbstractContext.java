
package edu.wright.cs.carl.net.context;

import java.util.*;

import java.io.IOException;

import java.security.Principal;

import java.security.acl.Acl;
import java.security.acl.Permission;

import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.RemoteHandle;
import edu.wright.cs.carl.net.handle.RemoteHandleManager;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.permissions.ContextPermission;
import edu.wright.cs.carl.security.UserCredentials;


/**
 * Abstract class that implements all of the methods defined by the Context
 * interface.  This class is declared abstract because, well, it just doesn't do
 * much of anything.  The AbstractContext class exists as a convenience to those
 * of you wanting to implement your own specialized Contexts.  If you subclass
 * AbstractContext, you won't have to implement any of the Context interface
 * methods - they're already done here.  You are always free, of course, to
 * <i>not</i> subclass AbstractContext and roll your own Context implementation.
 *
 * @author  Duane Bolick
 */
public abstract class AbstractContext implements Context
{
    protected String uniqueID;
    protected String name;
    protected String description;
    protected String type;
    
    protected Acl acl = null;
    protected RemoteHandleManager clients = null;
    
    protected boolean isActive = false;

    
    public AbstractContext()
    {
        throw new RuntimeException("Don't instantiate me this way!");
    }
    
    /**
     * Constructor.  Classes that extend AbstractContext should (at least) call
     * super(name, description, acl, clients, maxNumSessions) in their
     * constructors.
     * 
     * @param   name                [in]    Supplies the Context name.
     * @param   description         [in]    Supplies the Context description.
     * @param   acl                 [in]    Supplies the Context's Access Control
     *                                      List (java.security.acl.Acl)
     * @param   handleManager   [in]    Supplies a RemoteHandleManager.
     * @param   maxNumConnections   [in]    Supplies the maximum number of
     *                                      clients allowed.
     */
    public AbstractContext(String name, String description, String type, Acl acl, RemoteHandleManager handleManager, int maxNumConnections)
    {
        this.uniqueID = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.type = type;
        this.acl = acl;
        this.clients = handleManager;
        this.clients.setMaxNumHandles(maxNumConnections);        
    }

    /**
     * Get the unique ID of this Context.
     *
     * @return  The unique ID.
     */
    public String getUniqueID()
    {
        return this.uniqueID;
    }
    
    /**
     * Get the name of the Context.
     * 
     * @return  The name of the Context.
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Get the text description of the Context.
     * 
     * @return  The text description of the Context.
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * Get the Context type.
     * 
     * @return  The Context type.
     */
    public String getType()
    {
        return this.type;
    }
    
    
    /**
     * Client management.
     */
    
    /**
     * Get the number of joined clients.
     * 
     * @return  The current number of clients.
     */
    public int getCurrentNumClients()
    {
        return this.clients.getCurrentNumHandles();
    }
    
    /**
     * Get the maximum number of clients that can join.
     * 
     * @return  The maximum number of clients.
     */
    public int getMaxNumClients()
    {
        return this.clients.getMaxNumHandles();
    }
    
    /**
     * Set the maximum number of client that may join the Context.
     * 
     * @param   maxNumClients   [in]    Supplies the maximum number of clients.
     */
    public void setMaxNumClients(int maxNumClients)
    {
        this.clients.setMaxNumHandles(maxNumClients);
    }
    
    /**
     * Get a List of all joined clients.
     * 
     * @return  A List of usernames of all joined clients.
     */
    public List<String> getClientList()
    {
        return this.clients.getHandleIDs();
    }
    
    /**
     * Add a client to a Context.
     * 
     * @param   clientHandle    [in]    Supplies a handle to the client.
     * 
     * @return  True if the client was added successfully, false if the Context
     *          is full.
     * 
     * @throws  ContextException if the Context is not active.
     * @throws  DuplicateHandleException if the user is already connected.
     * @throws  PermissionException if the user does not have access to this
     *          Context.
     */
    public boolean connectClient(ClientHandle clientHandle) throws ContextException, DuplicateHandleException, PermissionException
    {
        if(this.isActive == false){
            throw new ContextException("AbstractContext.connectClient:  Cannot join an inactive Context.");
        }
        
        String username = null;
        UserCredentials credentials = null;
        
        try {
            //
            // Check permissions.
            //
            username = clientHandle.getUniqueID();
            credentials = clientHandle.getCredentials();
            
            boolean hasPermission = this.acl.checkPermission(credentials, new ContextPermission("join"));
            
            if(hasPermission == false){
                throw new PermissionException(username, "AbstractContext.addActiveSession", "You do not have permission to join this Context.");
            }
        }
        catch(IOException e) {
            //
            // Both the getCredentials and getName methods used above throw
            // IOExceptions if the connection has been lost.  If that's the
            // case, we'll end up here before the session has been added, so
            // there's no cleanup.  Just return.
            //
            ContextException c = new ContextException("AbstractContext.connectClient:  IOException");
            c.setStackTrace(e.getStackTrace());
            throw c;           
        }
        
        //
        // Finally, try to add the new ClientHandle.  This operation throws
        // DuplicateHandleException if the client is already connected, or
        // returns false if the Context is full.
        //
        boolean success = this.clients.addHandle(clientHandle);
        
        if(success) {
            UserStatusChange statusUpdate = new UserStatusChange(UserStatusChange.Type.LOGON);
            this.broadcastMessage(new ContextMessage(this.name, this.uniqueID, statusUpdate));
        }
        
        return success;
    }
    
    /**
     * Disconnect a client from a Context.
     * 
     * @param   username     [in]   Supplies the username.
     * 
     * @throws  ContextException if the user is not connected.
     */
    public void disconnectClient(String username) throws ContextException
    {
        ClientHandle removedHandle = (ClientHandle) this.clients.removeHandle(username);

        if(removedHandle == null) {
            throw new ContextException("AbstractContext.disconnectClient:  Client is not connected.");
        }
        
        try {
            removedHandle.disconnect(this.getUniqueID());
        }
        catch(IOException e) {
            e.printStackTrace();
            //
            // Don't care.  We're disconnecting them anyway.
            //
        }
    }

    /**
     * Called when a client leaves a context.
     *
     * @param   username    [in]    Supplies the username.
     */
    public void clientLeft(String username)
    {
        this.clients.removeHandle(username);
        UserStatusChange statusUpdate = new UserStatusChange(UserStatusChange.Type.LOGOFF);
        this.broadcastMessage(new ContextMessage(this.name, this.uniqueID, statusUpdate));
    }

    public void unicastMessage(Message message, String username)
    {
        ClientHandle client = (ClientHandle) this.clients.getHandle(username);
        
        if(client == null) {
            throw new AssertionError("AbstractContext.unicastMessage: Client " + username + " does not exist.");
        }
        
        try {
            client.sendMessage(message);
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
            this.clientLeft(username);
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
            this.clientLeft(it.next());
        }
    }
    
    /**
     * Disconnects all clients.
     */
    public void disconnectAll()
    {
        List<RemoteHandle> allClients = this.clients.clear();
        Iterator<RemoteHandle> it = allClients.iterator();
        while(it.hasNext()) {
            try {
                System.out.println("Disconnecting Client.");
                it.next().disconnect(this.uniqueID);
                System.out.println("Done Disconnecting Client.");
            }
            catch(IOException e) {
                e.printStackTrace();
                //
                // Don't care.  We're disconnecting them anyway.
                //
            }
        }        
    }
    
    
    /**
     * Activity management.
     */
    
    /**
     * Check to see if the Context is active.  Modifications to a context's
     * parameters should only be made when inactive.  No unprivileged actions
     * by clients (including joining) should be allowed when inactive.
     * 
     * @return  True if active, false otherwise.
     */
    public boolean isActive()
    {
        return this.isActive;
    }
    
    /**
     * Activate the Context.
     * 
     * @param   caller  [in]    Supplies the caller.    
     */
    public void activateContext()
    {
        this.isActive = true;        
    }
    
    /**
     * Deactivate the Context.  Also disconnects all users from the Context.
     * 
     * @param   caller  [in]    Supplies the caller.
     */
    public void deactivateContext()
    {
        this.isActive = false;
        this.disconnectAll();
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
            throw new PermissionException(caller.getName(), "AbstractContext.getAcl", "You are not an owner of this access control list.");
        }
        return this.acl;
    }
    
    
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
}
