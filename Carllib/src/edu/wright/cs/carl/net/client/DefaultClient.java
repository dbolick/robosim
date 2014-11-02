
package edu.wright.cs.carl.net.client;

import edu.wright.cs.carl.net.event.ServerEventListener;
import edu.wright.cs.carl.net.event.ServerEvent;
import java.io.IOException;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

import edu.wright.cs.carl.net.context.ContextException;
import edu.wright.cs.carl.net.context.ContextInformation;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ContextHandle;
import edu.wright.cs.carl.net.handle.RemoteHandleManager;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;
import edu.wright.cs.carl.net.handle.ServerHandle;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.net.view.View;

import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.swing.*;


/**
 * Default implementation of the Client interface.
 *
 * @author  Duane Bolick
 */
public class DefaultClient implements Client
{
    RemoteHandleManager servers;
    Map<String, View> views;
    View activeView;
    Map<String, Callback> clientCallbacks;

    private PrivateMessageListener pmListener;
    private Map<String, ServerEventListener> serverEventListeners;


    /**
     * Constructor.  This receives a RemoteHandleManager, but immediately sets
     * it to unlimited handles.
     * 
     * @param   servers     [in]    Supplies a RemoteHandleManager.
     */
    public DefaultClient(RemoteHandleManager servers)
    {
        this.servers = servers;
        this.views = new ConcurrentHashMap<String, View>();
        this.activeView = null;
        this.clientCallbacks = new ConcurrentHashMap<String, Callback>();

        this.serverEventListeners = new HashMap<String, ServerEventListener>();

        this.servers.setMaxNumHandles(-1);
    }
    
    /**
     * Add a listener that will receive any private messages this client
     * receives.
     * 
     * @param   pmListener  [in]    Supplies the PrivateMessageListener.
     */
    public void addPrivateMessageListener(PrivateMessageListener pmListener)
    {
        this.pmListener = pmListener;
    }

    /**
     * Add a listener that will receive ServerEvents from the given Server.
     *
     * @param   listener    [in]    Supplies the listener.
     * @param   serverID    [in]    Supplies the ID of the server whose events
     *                              will be sent to the listener.
     */
    public void addServerEventListener(ServerEventListener listener, String serverID)
    {
        this.serverEventListeners.put(serverID, listener);
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
        if(message instanceof ServerMessage) {
            ServerMessage serverMessage = (ServerMessage)message;
            Object payload = serverMessage.payload;

            if(payload instanceof PrivateMessage) {
                PrivateMessage pm = (PrivateMessage)payload;
                this.pmListener.receivePrivateMessage(pm);
                return;
            }

            else if(payload instanceof ServerEvent) {
                ServerEvent event = (ServerEvent)payload;
                ServerEventListener s = this.serverEventListeners.get(serverMessage.serverID);
                if(s != null) {
                    s.serverEventOccurred(event);
                }                
            }
        }
        
        else if(message instanceof ContextMessage) {
            ContextMessage contextMessage = (ContextMessage)message;
            Object payload = contextMessage.payload;
            
        
            if(payload instanceof ContextViewUpdate) {
                ContextViewUpdate contextUpdate = (ContextViewUpdate) payload;

                View targetView = views.get(contextMessage.contextID);

                if(targetView == null) {
                    //
                    // Sometimes we receive messages from Contexts to which
                    // we're no longer connected.  For now, we just ignore that,
                    // but...
                    //
                    // TODO: Modify Context implementations so they no longer
                    // send messages to clients that are gone.  Also, make sure
                    // that only appropriate messages are sent to Clients in the
                    // process of connecting or disconnecting.
                    //
                    // throw new AssertionError("Null Target View.");

                    return;
                }
                targetView.update(contextUpdate);       
            }
        }
    
    }
    
    /**
     * Add a server connection.
     * 
     * @param   serverHandle    [in]    Supplies the handle to the remote
     *                                  Server.
     * @param   clientCallback  [in]    Supplies the callback to this Client.
     * 
     * @return  True if the connection was added successfully, false if we
     *          lost connection to the server.
     * 
     * @throws  DuplicateHandleException if there exists a server handle with
     *          the same handle name.
     */
    public boolean addServerConnection(ServerHandle serverHandle, Callback clientCallback) throws DuplicateHandleException, IOException
    {
        String serverID = serverHandle.getUniqueID();
                    
        //
        // Try to add the serverHandle - this will throw a 
        // DuplicateHandleException if there's already a Server with the same
        // handle name in the handle manager.  It should never return false,
        // because we've set the RemoteHandleManager to an unlimited number
        // of handles in the Constructor.
        //
        if(this.servers.addHandle(serverHandle) == false) {
            throw new AssertionError();
        }

        //
        // Now, add the callback to the callback collection.  This better not
        // fail.
        //
        if(this.clientCallbacks.containsKey(serverID) == true) {
            throw new AssertionError();
        }

        this.clientCallbacks.put(serverID, clientCallback);
        
        return true;    
    }
    
    /**
     * Disconnect from a Server.  This method assumes that we are the ones
     * actively disconnecting from the Server.
     *
     * @param   serverID    [in]    Supplies the server ID.
     *
     * @throws  ServerException if we're not connected to that Server.
     */
    public void disconnectFromServer(String serverID) throws ServerException
    {
        //
        // First, remove the handle from our local collection.
        //
        ServerHandle handle = (ServerHandle) this.servers.removeHandle(serverID);
        if(handle == null) {
            throw new ServerException("DefaultClient.disconnectFromServer: Not connected to that Server.");
        }
        
        //
        // Next, remove the callback.
        //
        if(this.clientCallbacks.remove(serverID) == null) {
            throw new AssertionError();
        }
        
        //
        // Next, remove all Views that are on that Server.  We'll have to build
        // a Map of the Views that we want to keep, since we can't remove Views
        // from the existing one while we're iterating over it.
        //
        Map<String, View> remainingViews = new ConcurrentHashMap<String, View>();
        
        Iterator<View> it = this.views.values().iterator();
        View currentView = null;
        while(it.hasNext()) {
            currentView = it.next();
            
            //
            // If the current View is on the Server we're disconnecting from,
            // d/c it.
            //
            if(currentView.getServerID().equals(serverID)) {
                currentView.disconnectFromContext();
            }
            
            //
            // Otherwise, we put it into the Map of the Views we're keeping.
            //
            else {
                String contextID = null;
                contextID = currentView.getContextID();
                
                //
                // If we're still connected, keep it.
                //
                if(contextID != null) {
                    remainingViews.put(contextID, currentView);
                }
            }
        }
        
        //
        // Now replace our collection of views with the one we just built.
        //
        this.views = remainingViews;

        //
        // Finally, disconnect from the Server.
        //
        try {
            handle.disconnect(null);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when a remote Server disconnects from *us*.
     * Therefore, all we do in this method is cleanup.
     *
     * @param   serverID    [in]    Supplies the server ID.
     */
    public void serverConnectionLost(String serverID)
    {
        //
        // First, remove the handle from our local collection.
        //
        ServerHandle handle = (ServerHandle) this.servers.removeHandle(serverID);
        if(handle == null) {
            throw new AssertionError("DefaultClient.serverConnectionLost: Not connected to that Server.");
        }

        //
        // Notify the appropriate listener.
        //
        this.serverEventListeners.get(serverID).serverEventOccurred(new ServerEvent(ServerEvent.EventType.SERVER_CONNECTION_LOST, serverID));

        //
        // Next, remove the callback.
        //
        if(this.clientCallbacks.remove(serverID) == null) {
            throw new AssertionError();
        }

        //
        // Next, remove all Views that are on that Server.  We'll have to build
        // a Map of the Views that we want to keep, since we can't remove Views
        // from the existing one while we're iterating over it.
        //
        Map<String, View> remainingViews = new ConcurrentHashMap<String, View>();

        Iterator<View> it = this.views.values().iterator();
        View currentView = null;
        while(it.hasNext()) {
            currentView = it.next();

            //
            // If the current View is on the Server we're disconnecting from,
            // d/c it.
            //
            if(currentView.getServerID().equals(serverID)) {
                currentView.connectionToContextLost();
            }

            //
            // Otherwise, we put it into the Map of the Views we're keeping.
            //
            else {
                String contextID = currentView.getContextID();

                //
                // If we're still connected, keep it.
                //
                if(contextID != null) {
                    remainingViews.put(contextID, currentView);
                }
            }
        }

        //
        // Now replace our collection of views with the one we just built.
        //
        this.views = remainingViews;
        
    }

    /**
     * Get the IDs of all the Servers we're connected to.
     *
     * @return  A List of Server IDs.
     */
    public List<String> getServerIDs()
    {
        return this.servers.getHandleIDs();
    }
    
    /**
     * Get the ServerHandle of a Server we're connected to.
     *
     * @param   serverID    [in]    Supplies the ID of the desired Server.
     *
     * @return  A reference to the ServerHandle, or null if it doesn't exist.
     */
    public ServerHandle getServerHandle(String serverID)
    {
        return (ServerHandle) this.servers.getHandle(serverID);
    }

    /**
     * Get a list of context IDs hosted on a server to which we're connected.
     *
     * @param   serverID    [in]    Supplies the server name.
     *
     * @return  A list of context IDs.
     *
     * @throws  ClientException if the connection to the Server has been lost.
     */
    public List<String> getContextListFromServer(String serverID) throws ClientException
    {
        ServerHandle s = (ServerHandle) this.servers.getHandle(serverID);
        List<String> contextList = null;

        try {
            contextList = s.getContextList();
        }
        catch(IOException e) {
            e.printStackTrace();
            //
            // We lost connection to the server.  So disconnectFromContext and clean up.
            //
            this.serverConnectionLost(serverID);

            //
            // Then rethrow, so the caller knows to clean up whatever they need
            // to, since the connection has been lost.
            //
            throw new ClientException("Connection to " + serverID + " has been lost.");
        }

        return contextList;
    }

    /**
     * Get information about a context.
     *
     * @param   serverID      [in]    Supplies the server name.
     * @param   contextID     [in]    Supplies the context name.
     *
     * @return  The ContextInformation.
     *
     * @throws  ClientException if the connection to the Server has been lost.
     */
    public ContextInformation getContextInformation(String serverID, String contextID) throws ClientException
    {
        ServerHandle s = (ServerHandle) this.servers.getHandle(serverID);

        ContextInformation info = new ContextInformation();

        try {
            info.id = contextID;
            info.currentNumClients = s.getCurrentNumContextClients(contextID);
            info.maxNumClients = s.getMaxNumContextClients(contextID);
            info.name = s.getContextName(contextID);
            info.description = s.getContextDescription(contextID);
            info.type = s.getContextType(contextID);
            info.isActive = s.isContextActive(contextID);
        }
        catch(ServerException e) {
            //
            // This should never happen.
            //
            throw new AssertionError(e);
        }
        catch(IOException e) {
            e.printStackTrace();
            this.serverConnectionLost(serverID);
        }

        return info;
    }

    /**
     * Join a Context hosted on a given Server.
     * 
     * @param   serverID        [in]    Supplies the server ID.
     * @param   contextID       [in]    Supplies the ID of the Context we
     *                                  want to join.
     * @param   view            [in]    Supplies a View object that represents
     *                                  the Context's remote user interface.
     * @param   setAsActiveView [in]    If true, the new View will be set as
     *                                  active (i.e., it'll be the only one
     *                                  we receive view updates on).
     * 
     * @return  True if joining succeeded, false if the Context was full.
     * 
     * @throws ContextException if the Context is not active.
     * @throws ServerException if we're not connected to the Server.
     * @throws DuplicateHandleException if we're already connected to the
     *          Context.
     * @throws PermissionException if we lack permission to join the Context.
     */
    public boolean joinContext(String serverID, String contextID, View view, boolean setAsActiveView)
            throws ContextException, ServerException, DuplicateHandleException, PermissionException, ClientException
    {
        //
        // If this Client already has a view with the same unique ID, then that
        // implies we're already connected to this Context.
        //
        if(views.containsKey(contextID)) {
            throw new DuplicateHandleException("DefaultClient.joinContext: View map already contains this context's unique ID.");
        }
                    
        //
        // Get a reference to the handle of the named server.
        //
        ServerHandle serverHandle = (ServerHandle) this.servers.getHandle(serverID);
        if(serverHandle == null) {
            throw new ServerException("DefaultClient.joinContext: Not connected to this Server.");
        }
        
        try {
            
            //
            // This throws ContextException, ServerException,
            // DuplicateHandleException, and PermissionException, which are just
            // passed on by this method for the App to handle (i.e., display
            // the appropriate error messages.
            //
            ContextHandle contextHandle = serverHandle.joinContext(contextID);
            
            //
            // If the Context was full, return false.
            //
            if(contextHandle == null) {
                return false;
            }

            view.initializeView(contextHandle);
            views.put(contextID, view);
            if(setAsActiveView == true) {
                this.setActiveView(contextID);
            }
            
            UserStatusChange logonUpdate = new UserStatusChange();
            logonUpdate.statusChangeType = UserStatusChange.Type.LOGON;
            try {
                contextHandle.sendMessagePayload(logonUpdate);
            }
            catch(MessagingException e) {
                e.printStackTrace();
            }
            catch(MessageTypeException e) {
                e.printStackTrace();
            }
            
            return true;
        }
        catch(IOException e) {
            e.printStackTrace();
            //
            // We lost connection to the server.  So disconnectFromContext and clean up.
            //
            this.serverConnectionLost(serverID);
            return false;
        }
    }
    
    /**
     * Exit a context.  This method assumes that we are the ones actively
     * leaving the Context.
     *
     * @param   contextID   [in]    Supplies the context ID.
     *
     * @throws  ServerException if we have not joined the context.
     */
    public void leaveContext(String contextID) throws ServerException
    {
        View view = this.views.remove(contextID);
        if(view == null) {
            throw new ServerException("DefaultClient.leaveContext: Haven't joined this Context.");
        }
        
        if(this.activeView != null && this.activeView.getContextID().equals(contextID)) {
            try {
                view.setAsInactiveView();
            }
            catch(IOException e) {
                throw new AssertionError(e);
            }
            this.activeView = null;
        }

        ContextHandle contextHandle = view.getContextHandle();

        UserStatusChange logonUpdate = new UserStatusChange();
        logonUpdate.statusChangeType = UserStatusChange.Type.LOGOFF;
        try {
            contextHandle.sendMessagePayload(logonUpdate);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(PermissionException e) {
            e.printStackTrace();
        }
        catch(MessagingException e) {
            e.printStackTrace();
        }
        catch(MessageTypeException e) {
            e.printStackTrace();
        }

        view.disconnectFromContext();
    }

    /**
     * This method is called when a remote Context disconnects from *us*.
     * Therefore, all we do in this method is cleanup.
     *
     * @param   contextID   [in]    Supplies the context ID.
     */
    public void contextConnectionLost(String contextID)
    {
        View view = this.views.remove(contextID);

        if(view == null) {
            throw new AssertionError("DefaultClient.contextConnectionLost:  Haven't joined this Context.");
        }

        if(this.activeView.getContextID().equals(contextID)) {
            try {
                view.setAsInactiveView();
            }
            catch(IOException e) {
                throw new AssertionError(e);
            }
            this.activeView = null;
        }

        view.connectionToContextLost();
    }


    /**
     * Set a given View to active.  This indicates to the remote context to
     * start sending view updates, and tells all other remote contexts to stop
     * sending view updates.  This is to conserve bandwidth.
     *
     * @param   contextID   [in]    Supplies the Context ID.
     *
     * @throws  ServerException if the View does not exist.
     */
    public void setActiveView(String contextID) throws ServerException
    {
        View newActiveView = views.get(contextID);
        if(newActiveView == null) {
            throw new ServerException("DefaultClient.setActiveView: View does not exist.");
        }
            
        if(this.activeView != null) {
            try {
                this.activeView.setAsInactiveView();
            }
            catch(IOException e) {
                e.printStackTrace();
                this.activeView.connectionToContextLost();
            }
        }
        
        this.activeView = newActiveView;
    }
}
