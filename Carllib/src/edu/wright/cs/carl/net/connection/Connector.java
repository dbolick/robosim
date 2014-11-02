
package edu.wright.cs.carl.net.connection;

import java.io.IOException;

import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.ServerHandle;
import edu.wright.cs.carl.net.handle.DuplicateHandleException;

import edu.wright.cs.carl.security.PermissionException;


/**
 * This is a generic interface for classes that a Client uses to connect to a
 * Server.  
 * 
 * @author  Duane Bolick
 */
public interface Connector
{
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
    public ServerHandle connect(ClientHandle thisClient) throws PermissionException, DuplicateHandleException, IOException;
}
