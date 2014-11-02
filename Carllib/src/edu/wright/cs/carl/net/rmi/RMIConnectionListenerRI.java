
package edu.wright.cs.carl.net.rmi;

import edu.wright.cs.carl.net.connection.ConnectionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.security.PermissionException;


/**
 * Remote interface for RMIConnectionListener.
 *
 * @author  Duane Bolick
 * 
 * @see     ConnectionListener
 * @see     RMIConnectionListener
 */
public interface RMIConnectionListenerRI extends ConnectionListener, Remote
{
    /**
     * Get the server name.
     * 
     * @return  The server name.
     * 
     * @throws  java.rmi.RemoteException
     */
    public String getServerName() throws RemoteException;

    /**
     * Get the unique server ID.
     *
     * @return  The server ID.
     *
     * @throws  java.rmi.RemoteException
     */
    public String getServerID() throws RemoteException;
    
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
     * @throws  DuplicateHandleException if this user is already connected to
     *          the Server.
     * @throws  RemoteException in the event of a transport-layer communications
     *          failure.
     */
    public RMIServerHandleRI connect(RMIClientHandleRI clientHandle) throws PermissionException, DuplicateHandleException, RemoteException;
}
