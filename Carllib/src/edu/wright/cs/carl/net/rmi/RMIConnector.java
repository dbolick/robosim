
package edu.wright.cs.carl.net.rmi;

import java.util.Iterator;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.wright.cs.carl.net.connection.Connector;

import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.ServerHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.security.PermissionException;


/**
 * Implementation of Connector, using RMI.
 *
 * @author  Duane Bolick
 * 
 * @see     Connector
 */
public class RMIConnector implements Connector
{
    private String host;
    private int port;
    private String remoteObjectName;
    
    /**
     * Constructor.
     * 
     * @param   host                [in]    Supplies the server hostname.
     * @param   port                [in]    Supplies the server listening port.
     */
    public RMIConnector(String host, int port)
    {
        this.host = host;
        this.port = port;
        this.remoteObjectName = "RMI" + Integer.toString(this.port);
    }

    /**
     * Connect to a remote Server.
     * 
     * @param   thisClient      [in]    Supplies a handle to this client.
     * 
     * @return  A handle to the remote Server, or null if connection failed.
     * 
     * @throws  PermissionException if the client lacks permission to connect.
     * @throws  DuplicateHandleException if the client is already connected.
     * @throws  IOException if the connection is lost.
     */    
    public ServerHandle connect(ClientHandle thisClient) throws PermissionException, DuplicateHandleException, RemoteException
    {
        Registry registry = LocateRegistry.getRegistry(this.host, this.port);
        
        if(registry == null) {
            throw new RemoteException("Cannot Locate RMI Registry.");
        }
        
        try {
            //
            // This line throws NotBoundException.
            //
            RMIConnectionListenerRI connectionListener = (RMIConnectionListenerRI) registry.lookup(this.remoteObjectName);
            
            
            ((RMIClientHandle)thisClient).setServerID(connectionListener.getServerID());
            
            //
            // This line throws DuplicateHandleException.
            //
            ServerHandle serverHandle = connectionListener.connect((RMIClientHandleRI) thisClient);
            
            if(serverHandle == null ) {
                throw new RemoteException("Could not get a Server Handle");
            }
            
            return serverHandle;
        }
        catch(NotBoundException e) {
            throw new RemoteException("RMI Registry not bound,");
        }
    }
    
    /**
     * Get the remote Server host name.
     * 
     * @return The host name.
     */
    public String getHost()
    {
        return this.host;
    }
    
    /**
     * Set the remote Server host name.
     * 
     * @param   host    [in]    Supplies the host name.
     */
    public void setHost(String host)
    {
        this.host = host;
    }
    
    /**
     * Get the name the Connector uses to look up the remote connection listener
     * in the remote server's RMI registry.
     * 
     * @return  The name of the connection listener object.
     */
    public String getRemoteObjectName()
    {
        return this.remoteObjectName;
    }
    
    /**
     * Set the name we use to lookup the remote object.
     * 
     * @param   remoteObjectName    [in]    Supplies the name.
     */
    public void setRemoteObjectName(String remoteObjectName)
    {
        this.remoteObjectName = remoteObjectName;
    }
}
