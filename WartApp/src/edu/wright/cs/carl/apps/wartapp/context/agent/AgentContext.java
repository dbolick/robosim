
package edu.wright.cs.carl.apps.wartapp.context.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import java.io.IOException;

import java.security.Principal;

import java.security.acl.Acl;

import edu.wright.cs.carl.net.context.AbstractContext;
import edu.wright.cs.carl.net.context.ContextException;

import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;
import edu.wright.cs.carl.net.handle.RemoteHandleManager;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.UserCredentials;

import edu.wright.cs.carl.security.permissions.ContextPermission;

import edu.wright.cs.carl.apps.wartapp.*;
import edu.wright.cs.carl.apps.wartapp.context.chat.ChatWindowUpdate;
import edu.wright.cs.carl.net.message.payload.UserListUpdate;

import edu.wright.cs.carl.wart.agent.Agent;
import edu.wright.cs.carl.wart.agent.AgentManager;

import edu.wright.cs.carl.wart.agent.AgentWorld;

import edu.wright.cs.carl.wart.agent.controller.AgentController;
import edu.wright.cs.carl.wart.agent.controller.AgentControllerException;

import edu.wright.cs.carl.wart.agent.controller.ControllerQueue;
import edu.wright.cs.carl.wart.agent.controller.DefaultControllerQueue;


/**
 * Context for controlling robotic agents.
 *
 * @author  Duane Bolick
 */
public class AgentContext extends AbstractContext implements AgentContextInterface, Runnable
{
    protected boolean initialized = false;
    
    protected AgentManager agents;
    protected Map<String, ControllerQueue> controllerQueues;
    protected int controlTimeLimitInSeconds;
    
    protected AgentWorld world;
    protected List<ClientHandle> activeViews;
    
    protected Thread viewUpdateThread;
    protected boolean viewUpdateThreadStopRequested = false;
    
    protected int viewUpdatesPerSecond;
    protected long viewUpdateThreadSleepTimeInMillis;
    
    //protected int statusUpdatesPerSecond;
    protected long statusUpdateIntervalInMillis;
    
    protected ContextMessage agentStatusMessage;    
    protected ContextMessage chatMessage;
    protected ContextMessage viewInitializerMessage;
    protected ContextMessage viewUpdateMessage;
    
    protected AgentStatusUpdate currentStatus;

    protected final Object activeViewsLock = new Object();
    protected final Object controllerQueuesLock = new Object();
    
    /**
     * Construct an AgentContext instance.  Note that calling this constructor
     * does not make the AgentContext ready to run.  The <i>initialize</i>
     * method must be called before the context can be started.
     * 
     * @param name                  [in]    Supplies the context name.
     * @param description           [in]    Supplies the context description.
     * @param acl                   [in]    Supplies the Acl.
     * @param handleManager         [in]    Supplies the handle manager.
     * @param maxNumConnections     [in]    Supplies the max number of remote
     *                                      connections.
     */
    public AgentContext(String name,
                        String description,
                        Acl acl,
                        RemoteHandleManager handleManager,
                        int maxNumConnections,
                        WartAppConstants.ContextType type)
    {
        super(  name,
                description,
                WartAppConstants.GetContextTypeString(type),
                acl,
                handleManager,
                maxNumConnections);     
        
        this.activeViews = new ArrayList<ClientHandle>();

        this.agentStatusMessage = new ContextMessage(this.name, this.uniqueID);
        this.chatMessage = new ContextMessage(this.name, this.uniqueID);
        this.viewInitializerMessage = new ContextMessage(this.name, this.uniqueID);
        this.viewUpdateMessage = new ContextMessage(this.name, this.uniqueID);

        this.currentStatus = new AgentStatusUpdate();        
    }
    
    /**
     * Initialize the Agent Context by providing it with an AgentManager full
     * of agents, an AgentWorld, and a time limit for how long a user may
     * control an agent.  This needs to be called before an AgentContext is
     * activated.
     * 
     * @param   agents              [in]    Supplies the agents.
     * @param   world               [in]    Supplies the agent world.
     * @param   controlTimeLimit    [in]    Supplies the time limit.
     */
    public void initialize(AgentManager agents, AgentWorld world, int controlTimeLimit)
    {
        this.agents = agents;
        this.controllerQueues = new HashMap<String, ControllerQueue>();
        
        Iterator<String> it = this.agents.getAgentList().iterator();
        Agent currentAgent = null;
        while(it.hasNext()) {            
            currentAgent = this.agents.getAgentByName(it.next());
            this.controllerQueues.put(currentAgent.getName(), new DefaultControllerQueue(currentAgent));
        }
        
        this.world = world;
        this.controlTimeLimitInSeconds = controlTimeLimit;
        
        this.viewUpdatesPerSecond = WartAppConstants.DefaultViewUpdatesPerSecond;
        this.viewUpdateThreadSleepTimeInMillis = 1000 / this.viewUpdatesPerSecond;
        this.statusUpdateIntervalInMillis = WartAppConstants.DefaultStatusUpdateIntervalInMillis;
        
        this.initialized = true;
    }

    /**
     * Check to see if this context has been initialized.
     * 
     * @return  True if it's been initialized, false if not.
     */
    public boolean isInitialized()
    {
        return this.initialized;
    }
    
    /**
     * Activate the Context.
     * 
     * @param   caller  [in]    Supplies the caller.
     * 
     * @throws  ContextException if the Context is already active.
     */
    @Override
    public void activateContext()
    {
        super.activateContext();
        
        //
        // Set the payload of the view initializer message.
        //
        this.viewInitializerMessage.payload = this.world.getInitialViewSync();
        
        //
        // Start the world model update thread.
        //
        this.world.start();
        
        //
        // Start sending out view updates.
        //
        this.startViewUpdater();
        
        //
        // Start the agents' controller queue threads.
        //
        Iterator<ControllerQueue> it = this.controllerQueues.values().iterator();
        while(it.hasNext()) {
            it.next().startQueue();
        }
        
        this.isActive = true;        
    }
    
    /**
     * Deactivate the Context.  Also disconnects all users from the Context.
     * 
     * @param   caller  [in]    Supplies the caller.
     * 
     * @throws  ContextException if the Context is already active.
     */
    @Override
    public void deactivateContext()
    {
        super.deactivateContext();
        this.world.stop();
        this.stopViewUpdater();

        Iterator<ControllerQueue> it = this.controllerQueues.values().iterator();
        while(it.hasNext()) {
            it.next().stopQueue();
        }
        
        this.isActive = false;
    }    
    
    /**
     * Handle an incoming message.
     * 
     * @param   caller      [in]    Supplies a reference to the sender.
     * @param   message     [in]    Supplies the Message.
     * 
     * @throws  MessagingException if the action is invalid.
     * @throws  ContextException if the Context is inactive.
     * @throws  PermissionException if the sender does not have sufficient
     *          privileges to enact whatever the Message is intended to do.
     */
    @Override
    public void handleIncomingMessage(Message message) throws MessagingException, MessageTypeException, PermissionException
    {
        if(message instanceof ClientMessage) {
            ClientMessage clientMessage = (ClientMessage) message;
            
            if(this.hasPermission(clientMessage.clientCredentials, new ContextPermission("sendMessage")) == false) {
                throw new PermissionException("You lack permission to interact with this context.");
            }            
            
            //
            // Simple text message payload.
            //
            if(clientMessage.payload instanceof SimpleText) {
                SimpleText textPayload = (SimpleText)clientMessage.payload;
                this.chatMessage.payload = new ChatWindowUpdate(
                                                        clientMessage.clientCredentials.getName() +
                                                        ": " + textPayload.text + "\n");
                this.broadcastMessage(this.chatMessage);
                return;                
            }
            
            //
            // User status change payload.
            //
            else if(clientMessage.payload instanceof UserStatusChange) {
                
                UserStatusChange statusChange = (UserStatusChange) clientMessage.payload;
                
                if(statusChange.statusChangeType == UserStatusChange.Type.LOGOFF) {
                    this.chatMessage.payload = new UserListUpdate(
                                                    clientMessage.clientCredentials.getName() + " has left.\n",
                                                    clientMessage.clientCredentials.getName(),
                                                    statusChange.statusChangeType); 
                    this.broadcastMessage(this.chatMessage);
                }
                else if(statusChange.statusChangeType == UserStatusChange.Type.LOGON) {
                    this.chatMessage.payload = new UserListUpdate(
                                                    clientMessage.clientCredentials.getName() + " has joined.\n",
                                                    clientMessage.clientCredentials.getName(),
                                                    statusChange.statusChangeType);  
                    this.broadcastMessage(this.chatMessage);
                } 
                else if(statusChange.statusChangeType == UserStatusChange.Type.INITIALIZE_ME) {
                    ContextMessage cm = new ContextMessage(this.name, this.uniqueID);
                    cm.payload = this.world.getInitialViewSync();
                    this.unicastMessage(cm, clientMessage.clientCredentials.getName());
                }
                else if(statusChange.statusChangeType == UserStatusChange.Type.INITIALIZED) {
                    this.activateRemoteView(clientMessage.clientCredentials.getName());
                }
            }          
            
            //
            // Control request payload.
            //
            else if(clientMessage.payload instanceof ControlRequest) {
                ControlRequest controlRequest = (ControlRequest)clientMessage.payload;
                ControllerQueue cq = this.controllerQueues.get(controlRequest.agentName);
                if(cq != null) {
                    cq.addController(   controlRequest.controller,
                                        clientMessage.recipientName,
                                        this.controlTimeLimitInSeconds);
                }
                this.chatMessage.payload =  new ChatWindowUpdate(clientMessage.clientCredentials.getName() + " has requested control of " + controlRequest.agentName + "\n");
                this.broadcastMessage(this.chatMessage);
            }

            //
            // Kicking User
            //
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
                    WartApp.getApplication().getServer().disconnectClientFromContext(this.uniqueID, removalRequest.username);
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
        } // End if it's a ClientMessage        
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
    @Override
    public boolean connectClient(ClientHandle clientHandle) throws ContextException, DuplicateHandleException, PermissionException
    {
        if(super.connectClient(clientHandle) == false) {
            return false;
        }
        
        return true;
    }

    /**
     * Called when a client leaves a context.
     *
     * @param   username    [in]    Supplies the username.
     */
    @Override
    public void clientLeft(String username)
    {
        ClientHandle departingClient = (ClientHandle) this.clients.removeHandle(username);

        if(departingClient == null) {
            throw new AssertionError("AgentContext: Client not connected.");
        }
        
        this.activeViews.remove(departingClient);
    }

    /**
     * Get a list of Agents by name.
     * 
     * @return  A list of Agent names.
     */
    public List<String> getAgentList()
    {
        return this.agents.getAgentList();
    }
    
    /**
     * Get a reference to an Agent by name.
     * 
     * @param   agentName   [in]    Supplies the name.
     * 
     * @return  The Agent, or null if the Agent doesn't exist.
     */
    public Agent getAgent(String agentName)
    {
        return this.agents.getAgentByName(agentName);
    }
    
    /**
     * Add an Agent to the context.
     * 
     * @param   caller      [in]    Supplies the caller.
     * @param   newAgent    [in]    Supplies the Agent.
     * 
     * @throws  ServerException if the Agent already exists.
     * @throws  PermissionException if the caller lacks permission.
     */
    public void addAgent(Principal caller, Agent newAgent) throws ServerException, PermissionException
    {
        boolean hasPermission = this.acl.checkPermission(caller, new ContextPermission("controlContext"));
        if(hasPermission == false){
            throw new PermissionException(caller.getName(), "AbstractAgentContext.addAgent", "You do not have permission to control this Context.");
        }
        
        if(this.isActive == true){
            throw new ServerException("AbstractAgentContext.addAgent:  Cannot add Agents to an active Context.");
        }
        
        this.agents.addAgent(newAgent);
        this.controllerQueues.put(newAgent.getName(), new DefaultControllerQueue(newAgent));
    }
    
    /**
     * Remove an Agent from the context.
     * 
     * @param   caller      [in]    Supplies the caller.
     * @param   agentname   [in]    Supplies the Agent name.
     * 
     * @throws  ServerException if the Agent does not exist.
     * @throws  PermissionException if the caller lacks permission.
     */    
    public void removeAgent(Principal caller, String agentname) throws ServerException, PermissionException
    {
        boolean hasPermission = this.acl.checkPermission(caller, new ContextPermission("controlContext"));
        if(hasPermission == false){
            throw new PermissionException(caller.getName(), "AbstractAgentContext.removeAgent", "You do not have permission to control this Context.");
        }          
        
        if(this.isActive == true){
            throw new ServerException("AbstractAgentContext.removeAgent:  Cannot remove Agents from an active Context.");
        }
        
        this.agents.removeAgent(agentname);
        this.controllerQueues.remove(agentname);
    }
    
    /**
     * Add a controller to an Agent's queue.
     * 
     * @param   caller      [in]    Supplies the caller (the owner of the
     *                              controller.
     * @param   agentName   [in]    Supplies the name of the Agent.
     * @param   controller  [in]    Supplies the controller.
     * 
     * @throws  AgentControllerException if the controller is not compatible.
     * @throws  ServerException if the Agent doesn't exist.
     * @throws  PermissionException if the caller lacks permission.
     */
    public void addControllerToQueue(ClientHandle caller, String agentName, AgentController controller) throws AgentControllerException, ServerException, PermissionException
    {
        if(this.isActive == false){
            throw new ServerException("AbstractAgentContext.addControllerToQueue:  Cannot add Controllers when Context is inactive.");
        }
        
        UserCredentials clientCredentials = null;
        
        try {
            clientCredentials = caller.getCredentials();
        
            boolean hasPermission = this.acl.checkPermission(clientCredentials, new ContextPermission("controlAgent"));
            if(hasPermission == false){
                throw new PermissionException(clientCredentials.getName(), "AbstractAgentContext.addControllerToQueue", "You do not have permission to control agents in this Context.");
            }    
        }
        catch(IOException e) {
            //
            // We've lost connection to the caller, so drop the ClientHandle.  We'll
            // let the Server handle the rest of the cleanup.
            //
            e.printStackTrace();
            this.clients.removeHandle(clientCredentials.getName());
            this.controllerQueues.get(agentName).removeAllFromUser(clientCredentials.getName());

            synchronized(this.activeViewsLock) {
                this.deactivateRemoteView(clientCredentials.getName());
            }
        }

        this.controllerQueues.get(agentName).addController(controller, name, this.controlTimeLimitInSeconds);
    }
    
    
    /**
     * Get a reference to the controller queue Map.  The key is the agent name,
     * and the value is the Agent's controller queue.  It is OK to directly
     * manipulate an Agent's ControllerQueue through a GUI, using the
     * ControllerQueue interface.
     * 
     * @return  A Map containing ControllerQueues.
     */
    public Map<String, ControllerQueue> getControllerQueues()
    {
        return this.controllerQueues;
    }
    
    /**
     * Start sending view updates to the provided client.
     * 
     * @param   username    [in]    Supplies the username.
     */
    @Override
    public void activateRemoteView(String username)
    {
        synchronized(this.activeViewsLock) {
            this.activeViews.add((ClientHandle) this.clients.getHandle(username));
        }
    }
    
    /**
     * Stop sending view updates to the specified client.
     * 
     * @param   username    [in]    Supplies the username.
     */
    @Override
    public void deactivateRemoteView(String username)
    {
        synchronized(this.activeViewsLock) {
            this.activeViews.remove(this.clients.getHandle(username));
        }
    }
    
    /**
     * Start sending view updates to clients.
     */
    public void startViewUpdater()
    {
        this.viewUpdateThreadStopRequested = false;
        this.viewUpdateThread = new Thread(this);
        this.viewUpdateThread.start();
    }
    
    /**
     * Stop sending view updates to clients.
     */
    public void stopViewUpdater()
    {
        this.viewUpdateThreadStopRequested = true;
        this.viewUpdateThread = null;
    }
    
    /**
     * Set the number of view updates this context sends out per second.
     * 
     * @param   viewUpdatesPerSecond    [in]    Supplies the number of updates per
     *                                      second.
     */
    public void setUpdateRate(int updatesPerSecond)
    {
        if(updatesPerSecond > WartAppConstants.MaxUpdatesPerSecond) {
            this.viewUpdatesPerSecond = WartAppConstants.MaxUpdatesPerSecond;
        }
        else if(updatesPerSecond < WartAppConstants.MaxUpdatesPerSecond) {
            this.viewUpdatesPerSecond = WartAppConstants.MinUpdatesPerSecond;
        }
        else {
            this.viewUpdatesPerSecond = updatesPerSecond;
        }
        
        this.viewUpdateThreadSleepTimeInMillis = 1000 / this.viewUpdatesPerSecond;
    }
    
    /**
     * Implements java.lang.Runnable.run()
     */
    @Override
    public void run()
    {
        Iterator<ClientHandle> it = null;
        ClientHandle currentHandle = null;
        String currentUsername = null;
        
        long lastStatusUpdateTime = System.currentTimeMillis();
        boolean sendStatusUpdate = false;
        
        while(this.viewUpdateThreadStopRequested == false) {
            try {
                Thread.sleep(this.viewUpdateThreadSleepTimeInMillis);
            }
            catch(InterruptedException e)
            {
                //
                // Do nothing.  If this thread is interrupted or awakened, just
                // keep running.  
                //
            }

            this.viewUpdateMessage.payload = this.world.getViewUpdate();
            
            if(System.currentTimeMillis() - lastStatusUpdateTime >= this.statusUpdateIntervalInMillis) {
                this.agentStatusMessage.payload = this.getStatusUpdate();
                sendStatusUpdate = true;
            }

            synchronized(this.activeViewsLock) {
                it = this.activeViews.iterator();
                while(it.hasNext()) {
                    currentHandle = it.next();
                    try {
                        currentUsername = currentHandle.getUniqueID();
                        currentHandle.sendMessage(this.viewUpdateMessage);
                        if(sendStatusUpdate == true) {
                            currentHandle.sendMessage(this.agentStatusMessage);
                        }
                    }
                    catch(IOException e) {
                        //
                        // Lost connection, so drop the ClientHandle.
                        //
                        e.printStackTrace();
                        this.deactivateRemoteView(currentUsername);
                        this.clients.removeHandle(currentUsername);
                    }
                    catch(MessagingException e) {

                    }
                    catch(MessageTypeException e) {

                    }
                    catch(PermissionException e) {

                    }
                } // End while loop over all client handles.
            } // End synchronized block on activeViewsLock
            
            if(sendStatusUpdate == true) {
                lastStatusUpdateTime = System.currentTimeMillis();
                sendStatusUpdate = false;
            }
        }
    }
    
    protected AgentStatusUpdate getStatusUpdate()
    {
        this.currentStatus.controllerQueues.clear();
        
        String currentAgentName = null;
        ControllerQueue currentControllerQueue = null;
        List<String> controllerList = new ArrayList<String>();
        
        Iterator<String> it = this.controllerQueues.keySet().iterator();
        while(it.hasNext()) {
            currentAgentName = it.next();           
            currentControllerQueue = this.controllerQueues.get(currentAgentName);
            controllerList.addAll(currentControllerQueue.getControllerList());
            this.currentStatus.controllerQueues.put(currentAgentName, controllerList);
            controllerList.clear();
        }

        return this.currentStatus;
    }
}
