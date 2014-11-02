
package edu.wright.cs.carl.net.connection;

import java.io.Serializable;
import java.io.IOException;

import edu.wright.cs.carl.net.server.ServerException;


/**
 * This interface defines methods all ConnectionListener implementations must
 * have, regardless of their underlying protocol.
 * 
 * @author  Duane Bolick
 */
public interface ConnectionListener extends Serializable
{
    /**
     * Start listening.
     * 
     * @throws  ServerException if already started.
     * @throws  IOException if the listener could not be started.
     */
    public void start() throws ServerException, IOException;
    
    /**
     * Stop listening.  This also releases all system resources.  Once a
     * listener has been stopped, it cannot be restarted.  Obtain a new instance
     * of the listener to restart.
     * 
     * @throws  ServerException if not started.
     * @throws  IOException if an error occurred when stopping the listener.
     */
    public void stop() throws ServerException, IOException;
    
    /**
     * Check if the ConnectionListener is listening (i.e., accepting incoming
     * connections).
     * 
     * @return  True if listening, false if not.
     */
    public boolean isListening() throws IOException;
    
    /**
     * Get the type of the ConnectionListener.
     * 
     * @return  The type.
     */
    public String getType() throws IOException;
    
    /**
     * Get the unique identifier of the ConnectionListener.  This method should
     * return an ID that is guaranteed to be unique among all ConnectionListener
     * objects on this Server.
     * 
     * @return  The unique identifier of the ConnectionListener.
     */
    public String getUniqueID() throws IOException;
    
    /**
     * Get the port.
     * 
     * @return  The port.
     */
    public int getPort() throws IOException;   
}
