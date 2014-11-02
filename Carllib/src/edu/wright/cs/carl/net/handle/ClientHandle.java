
package edu.wright.cs.carl.net.handle;

import java.io.IOException;

import edu.wright.cs.carl.security.UserCredentials;


/**
 * <p>
 * The ClientHandle interface declares methods that a Server uses to interact
 * with a remote client.  A Server maintains a collection of handles to remote
 * clients, each representing a connection from a remote user.  Implementations
 * of ClientHandle may use different transport-layer protocols.
 * </p>
 * 
 * <p>
 * In order to do things, like send messages to a client, drop a client etc.,
 * the server application invokes methods of a ClientHandle-type object.
 * </p>
 *   
 * @author  Duane Bolick
 */
public interface ClientHandle extends RemoteHandle
{
    /**
     * Get the UserCredentials object associated with this ClientHandle.
     * 
     * @return  a reference to the UserCredentials associated with this ClientHandle.
     */
    public UserCredentials getCredentials() throws IOException;
}
