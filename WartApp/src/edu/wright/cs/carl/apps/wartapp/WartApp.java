/*
 * WartApp.java
 */

package edu.wright.cs.carl.apps.wartapp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import java.security.acl.NotOwnerException;

import java.util.Iterator;

import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import edu.wright.cs.carl.net.client.Client;
import edu.wright.cs.carl.net.client.DefaultClient;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.RemoteHandleManager;
import edu.wright.cs.carl.net.handle.DefaultRemoteHandleManager;
import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.LocalClientHandle;
import edu.wright.cs.carl.net.handle.ServerHandle;
import edu.wright.cs.carl.net.handle.LocalServerHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.net.context.ContextManager;
import edu.wright.cs.carl.net.context.DefaultContextManager;

import edu.wright.cs.carl.net.connection.*;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;

import edu.wright.cs.carl.security.DefaultAcl;
import edu.wright.cs.carl.security.DefaultAclEntry;
import edu.wright.cs.carl.security.DefaultGroup;
import edu.wright.cs.carl.security.UserCredentials;
import edu.wright.cs.carl.security.DefaultUserCredentials;
import edu.wright.cs.carl.security.ServerSecurityManager;
import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.security.accounts.AccountManager;
import edu.wright.cs.carl.security.accounts.DefaultAccountManager;

import edu.wright.cs.carl.security.permissions.ServerPermission;
import edu.wright.cs.carl.security.permissions.ContextPermission;

import edu.wright.cs.carl.wart.agent.AgentManager;
import edu.wright.cs.carl.wart.agent.DefaultAgentManager;


/**
 * The main class of the application.  This is the entry point of execution.
 * 
 * @author  Duane Bolick
 */
public class WartApp extends SingleFrameApplication
{
    // Configuration
    private String                      configFilePath = "wart.ser";
    private WartConfig                  appConfiguration;
    
    
    // Server
    private Server                      server;
    private ServerSecurityManager       securityManager;    
    private ConnectionListenerManager   listeners;

    
    // Client
    private Client                      client;
    
    
    // Local
    private UserCredentials             localUser;
    private ServerHandle                localServerHandle;
    private ClientHandle                localClientHandle;
    
    
    /**
     * ========================================================================
     * Application Accessors
     * ========================================================================
     */
    
    public Server getServer()
    {
        return this.server;
    }
    
    public ServerHandle getLocalServerHandle()
    {
        return this.localServerHandle;
    }
    
    public Client getClient()
    {
        return this.client;
    }
    
    public ClientHandle getLocalClientHandle()
    {
        return this.localClientHandle;
    }    
    
    public ConnectionListenerManager getListeners()
    {
        return this.listeners;
    }
    
    public UserCredentials getLocalUser()
    {
        return this.localUser;
    }


    /**
     * Start the server listening.
     *
     * @throws  ServerException if no listeners were sucessfully created or
     *          started.
     */
    public void startServer() throws ServerException
    {
        //
        // If we're already listening, then don't do anything.
        //
        if(this.listeners.isRunning()) {
            return;
        }

        //
        // First, apply the server parameters from the configuration.
        //
        this.server.setName(this.appConfiguration.getServerName());
        this.server.setMaxNumClients(this.appConfiguration.getMaxNumHandles());

        //
        // TODO: Allow listeners on multiple protocols.  All that needs to be
        // done here is to wrap this code in a loop and iterate over some
        // collection of listener parameters.
        //

        //
        // Next, create all the listeners.
        //
        WartAppConstants.ConnectionProtocolType protocol = this.appConfiguration.getServerProtocol();
        String protocolString = WartAppConstants.GetProtocolTypeString(protocol);
        ConnectionListener newListener = null;

        try {
            newListener =
                WartAppConstants.GetListenerInstance(   protocol,
                                                        this.server,
                                                        Integer.toString(this.appConfiguration.getServerPort()));
            
            if(newListener == null) {
                JOptionPane.showMessageDialog(
                                this.getMainFrame(),
                                "Protocol type: " + protocolString + 
                                " is not supported.  Try a different protocol type in Server Settings.");                
            }
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(
                                this.getMainFrame(),
                                "The listener of type: " + protocolString +
                                " failed to start due to an IOException: " +
                                e.getMessage() +
                                " See the stack trace for more detail.");
            e.printStackTrace();
            newListener = null;
        }

        if(newListener != null) {
            this.listeners.add(newListener);
        }

        //
        // TODO: This would be where the loop would end.  The next two statements
        // could be left alone.
        //

        
        if(this.listeners.isEmpty() == true) {
            throw new ServerException("Creation of listeners failed.  Please check your server settings and try again.");
        }

        //
        // This throws a Server Exception if all listeners fail on startup.
        //
        this.listeners.startAll();
    }

    public void stopServer()
    {
        //
        // Stop all the listeners.
        //
        this.listeners.stopAll();

        //
        // Clear out the listener manager.
        //
        Iterator<String> it = this.listeners.listeners().iterator();
        while(it.hasNext()) {
            this.listeners.remove(it.next());
        }
    }

    /**
     * ========================================================================
     * Application Lifecycle
     * ========================================================================
     */
    
    public static void main(String[] args)
    {
        launch(WartApp.class, args);
    }    
    
    @Override
    protected void initialize(String[] args)
    {
        //
        // [1/] Load configuration.
        //
        this.appConfiguration = (WartConfig) this.loadFile(this.configFilePath);

        //
        // If there isn't one saved, create a new one and save it.
        //
        if(this.appConfiguration == null) {
            this.appConfiguration = new WartConfig();
            this.saveFile(configFilePath, this.appConfiguration);
        }
    }    

    @Override
    protected void startup()
    {
        //
        // Put this thingy after all the other stuff in ready.  Maybe.
        //
        show(new WartView(this));
    }

    @Override
    protected void ready()
    {
        //
        // [1/] Load the server security manager.
        //
        this.securityManager = (ServerSecurityManager) this.loadFile(this.appConfiguration.getServerSecurityManagerPath());
        
        //
        // If there isn't one saved, or it has somehow become inconsistent,
        // create a new one and save it.
        //        
        if(this.securityManager == null || this.securityManager.isConsistent() == false) {
            
            AccountManager accounts = new DefaultAccountManager();
            UserCredentials rootUser = new DefaultUserCredentials();
            
            show(new InitialSetup(null, true, accounts, rootUser));
            
            Group serverAclGroup = new DefaultGroup("serverAclOwnerGroup");
            serverAclGroup.addMember(rootUser);
            Acl serverAcl = new DefaultAcl("server", serverAclGroup);  

            // TODO: Use the Acl instead of giving everybody full permissions.
            AclEntry rootUserEntry = new DefaultAclEntry(rootUser);
            rootUserEntry.addPermission(new ServerPermission("modAccount"));
            rootUserEntry.addPermission(new ServerPermission("modContext"));
            rootUserEntry.addPermission(new ServerPermission("sendMessage"));
            rootUserEntry.addPermission(new ServerPermission("kickUser"));
            rootUserEntry.addPermission(new ContextPermission("join"));
            rootUserEntry.addPermission(new ContextPermission("sendMessage"));
            rootUserEntry.addPermission(new ContextPermission("kickUser"));

            try {
                serverAcl.addEntry(rootUser, rootUserEntry);
            }
            catch(NotOwnerException e) {
                throw new AssertionError(e);
            }
            
            this.securityManager = new ServerSecurityManager(accounts, serverAcl, rootUser);
            
            //
            // Finally, check one last time for consistency before saving.
            //
            if(this.securityManager.isConsistent() == false) {
                JOptionPane.showMessageDialog(this.getMainFrame(), "Security Manager Inconsistent!");
                System.exit(1);
            }
            
            this.saveFile(this.appConfiguration.getServerSecurityManagerPath(), this.securityManager);
        }
        
        //
        // [2/] Create Server components.
        //
        ContextManager contexts = new DefaultContextManager();
        RemoteHandleManager clients = new DefaultRemoteHandleManager(this.appConfiguration.getMaxNumHandles());
        
        //
        // [3/] Create Server.
        //
        this.server = new DefaultServer(
                            this.appConfiguration.getServerName(),
                            this.securityManager.getAccounts(),
                            contexts,
                            clients,
                            this.securityManager.getServerAcl());        
        
        //
        // [4/] Create Client components.
        //
        RemoteHandleManager servers = new DefaultRemoteHandleManager(-1);

        //
        // [5/] Create Client.
        //
        this.client = new DefaultClient(servers);
        
        //
        // [6/] Create connection listener manager.
        //
        this.listeners = new DefaultConnectionListenerManager();

        //
        // [7/] Login locally - this requires two steps:  First, connect to the
        // Server as a local client.  Then, add that server connection to the
        // Client.
        //
        ServerMessage loginMessage = null;
        boolean loginSuccess = false;
        
        this.localUser = new DefaultUserCredentials();
        this.localClientHandle = null;
        this.localServerHandle = null;
        
        while(loginSuccess == false) {

            //
            // Get the user's login.
            //
            this.show(new LocalLogin(this.getMainFrame(), true, this.localUser));

            /*
            try {
               this.localUser.setName("p");
               this.localUser.setPasswordHash(edu.wright.cs.carl.security.SecUtils.getHash("p", "MD5"));
            }
            catch(Exception e) {

            }
             */

            //
            // Create local handles.
            //
            localClientHandle = new LocalClientHandle(this.client, this.localUser);
            localServerHandle = new LocalServerHandle(this.server, this.localUser);   
            
            //
            // Perform the login.
            //
            try {
                loginMessage = this.server.connectClient(this.localClientHandle, (Callback) this.localServerHandle);
                loginSuccess = true;
            }
            catch(DuplicateHandleException e) {
                JOptionPane.showMessageDialog(this.getMainFrame(), e.getMessage());
                System.exit(1);
            }
            catch(PermissionException e) {
                JOptionPane.showMessageDialog(this.getMainFrame(), e.getMessage());
                loginSuccess = false;
            }
        }
        
        if(loginMessage == null) {
            JOptionPane.showMessageDialog(this.getMainFrame(), "Initial Login Message should mot be null!");
            System.exit(1);
        }

        //
        // [8/] Now, add the server connection to the client.
        //
        try {
            this.client.addServerConnection(this.localServerHandle, (Callback) this.localClientHandle);
        }
        catch(DuplicateHandleException e) {
            JOptionPane.showMessageDialog(this.getMainFrame(), e.getMessage());
            System.exit(1);
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this.getMainFrame(), e.getMessage());
            e.printStackTrace();
            return;
        }

        //
        // [9/] On login success, show the login message.
        //        
        JOptionPane.showMessageDialog(this.getMainFrame(), ((SimpleText)loginMessage.payload).text);
        
        
        //
        // [10/] Perform any additional work as specified in the config file.
        //
        if(this.appConfiguration.getAutoStartServer() == true) {
            try {
                this.startServer();
            }
            catch(ServerException e) {
                JOptionPane.showMessageDialog(this.getMainFrame(), e.getMessage());
            }
        }

        //
        // [11/] Create the "homepage" tab.
        //
        ((WartView)this.getMainView()).createHomePanel();

        //
        // [12/] Register listeners.
        //
        this.client.addPrivateMessageListener((WartView)WartApp.getApplication().getMainView());
        this.server.addStateChangeListener(((WartView)WartApp.getApplication().getMainView()).getHomePanel());
    }
    
    @Override
    protected void shutdown()
    {
        super.shutdown();
        this.saveFile(this.configFilePath, this.appConfiguration);
        this.saveFile(this.appConfiguration.getServerSecurityManagerPath(), this.securityManager);
    }
    /**
     * ========================================================================
     * End Application Lifecycle 
     * ========================================================================
     */    
    
    
    
    
    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root)
    {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of WartApp
     */
    public static WartApp getApplication()
    {
        return Application.getInstance(WartApp.class);
    }

    public WartConfig getConfiguration()
    {
        return this.appConfiguration;
    }
    
    
    
    
    public Object loadFile(String path)
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        Object fileReference = null;
        
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            fileReference = ois.readObject();
            ois.close();
            return fileReference;
        }
        catch(IOException e) {
            e.printStackTrace();

        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();

        }
        
        return fileReference;
    }
    
    public void saveFile(String path, Serializable obj)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        
        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }       
    }

}
