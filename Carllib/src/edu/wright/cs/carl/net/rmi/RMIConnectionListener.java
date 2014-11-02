
package edu.wright.cs.carl.net.rmi;

import java.io.IOException;

import java.net.MalformedURLException;

import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import edu.wright.cs.carl.net.server.Server; 
import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.net.message.Message;
import edu.wright.cs.carl.net.message.MessageTypeException;
import edu.wright.cs.carl.net.message.MessagingException;

import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.security.PermissionException;
import edu.wright.cs.carl.security.UserCredentials;


/**
 * <p>
 * An RMIConnectionListener handles incoming RMI connections.  Its
 * <i>connect</i> method receives a reference to the client's RMI callback
 * object (i.e., its remote interface).  Using the methods provided by the
 * client's remote interface, it authenticates the client.  If authentication is
 * successful, the RMI connection listener adds the client callback object to
 * the list of active sessions, and returns a reference to the server's remote
 * interface to the client.
 * </p>
 * 
 * @author  Duane Bolick
 */
public final class RMIConnectionListener extends UnicastRemoteObject implements RMIConnectionListenerRI
{
    private Server server;
    private int port;
    private Registry registry;
    private boolean isListening = false;
    
    
    public RMIConnectionListener(Server server, int port) throws RemoteException
    {
        this.server = server;
        this.port = port;
        try {
            this.registry = LocateRegistry.createRegistry(port);
        }
        catch(ExportException e) {
            this.registry = LocateRegistry.getRegistry(port);
        }        
    }

    /**
     * Get the server name.
     * 
     * @return  The server name.
     * 
     * @throws java.rmi.RemoteException
     */
    public String getServerName() throws RemoteException
    {
        return this.server.getName();
    }

    /**
     * Get the unique server ID.
     *
     * @return  The server ID.
     *
     * @throws  java.rmi.RemoteException
     */
    public String getServerID() throws RemoteException
    {
        return this.server.getUniqueID();
    }

    /**
     * Connect to the Server.  This method can fail (and will return null) if
     * there are already the maximum number of active Sessions connected to the
     * Server.
     * 
     * @param   client       [in]    Supplies a reference to the client's
     *                               remote interface.
     * 
     * @return  A reference to the Server's remote interface or null if 
     *          the Server is full.
     * 
     * @throws  PermissionException if this user
     * @throws  DuplicateHandleException if this user is already connected to
     *          the Server.
     * @throws  RemoteException in the event of a transport-layer communications
     *          failure.
     */
    public RMIServerHandleRI connect(RMIClientHandleRI clientHandle) throws PermissionException, DuplicateHandleException, RemoteException
    {

        UserCredentials clientCredentials = null;
        String username = new String();
        try {
            clientCredentials = clientHandle.getCredentials();
            username = clientCredentials.getName();

            //
            // First, create the Callback (which happens to be the remote object
            // itself in this case).
            //
            RMIServerHandleRI serverHandle = new RMIServerHandle(this.server, clientCredentials);
            assert (serverHandle != null);
                
            
            //
            // Then, try to connect.  This line throws DuplicateHandleException.
            //
            Message loginMessage = this.server.connectClient(clientHandle, serverHandle);
            
            //
            // This happens if the server is full.
            //
            if(loginMessage == null) {
                throw new RemoteException("Server is full.");
            }
            
            //
            // If everything is successful, send the login message and return
            // a reference to the server handle.
            //
            clientHandle.sendMessage(null);
            return serverHandle;
        }
        catch(IOException e) {
            e.printStackTrace();
            //
            // This happens if the connection to the client is lost.
            //
            if(username.isEmpty() == false) {
                this.server.clientHasLeftServer(username);
            }
            
            throw new RemoteException("Connection Lost.");
        }
        catch(MessageTypeException e) {
            //
            // The ClientHandle implementation of sendMessage declares, but
            // never actually throws this exception.  If we get this exception,
            // something impossible has happened.
            //
            throw new AssertionError(e);
        }
        catch(MessagingException e) {
            throw new AssertionError(e);
        }

    }
    
    /**
     * Start listening.
     * 
     * @throws  ServerException if already started.
     * @throws  IOException if an error occurred when starting the listener.
     */
    public void start() throws ServerException, IOException
    {
        if(this.isListening == true) {
            throw new ServerException("RMIConnectionListener.start: Already listening.");
        }
        
        try {        
            java.rmi.Naming.bind(this.getUniqueID(), this);
        }
        catch(AlreadyBoundException e) {
            throw new AssertionError(e);
        }
        catch(MalformedURLException e) {
            throw new AssertionError(e);
        }
        
        this.isListening = true;
    }
    
    /**
     * Stop listening.
     * 
     * @throws  ServerException if not started.
     * @throws  IOException if an error occurred when stopping the listener.
     */
    public void stop() throws ServerException, IOException
    {
        if(this.isListening == false) {
            throw new ServerException("RMIConnectionListener.start: Not listening.");
        }        
        
        this.isListening = false;

        try {
            java.rmi.Naming.unbind(this.getUniqueID());
        }
        catch(NotBoundException e) {
            throw new AssertionError(e);
        }
    }
    
    /**
     * Check if the ConnectionListener is listening (i.e., accepting incoming
     * connections).
     * 
     * @return  True if listening, false if not.
     */
    public boolean isListening() throws RemoteException
    {
        return this.isListening;
    }
    
    /**
     * Get the type of the ConnectionListener.
     * 
     * @return  The type.
     */
    public String getType() throws RemoteException
    {
        return "RMI";
    }
    
    /**
     * Get the unique identifier of the ConnectionListener.  This method should
     * return an ID that is guaranteed to be unique among all ConnectionListener
     * objects on this Server.  Since no two RMI Connection Listeners can be
     * active on the same port, the type plus the port number is enough to
     * uniquely identify it.
     * 
     * @return  The unique identifier of the ConnectionListener.
     */
    public String getUniqueID() throws RemoteException
    {
        return new String(this.getType() + Integer.toString(this.port));
    }
    
    /**
     * Get the port.
     * 
     * @return  The port.
     */    
    public int getPort() throws RemoteException
    {
        return this.port;
    }
}
