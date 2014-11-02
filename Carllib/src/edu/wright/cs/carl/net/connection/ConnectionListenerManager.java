
package edu.wright.cs.carl.net.connection;

import java.io.Serializable;

import java.util.List;

import edu.wright.cs.carl.net.server.ServerException;


/**
 * ConnectionListenerManager implementations are responsible for maintaining a
 * read/write collection of ConnectionListener objects.  It is also responsible
 * for ensuring the uniqueness of ConnectionListener objects.
 * 
 * @author  Duane Bolick
 */
public interface ConnectionListenerManager extends Serializable
{
    /**
     * Add a connection listener.
     * 
     * @param   connectionListener  [in]    Supplies a reference to the new
     *                                      listener.
     * 
     * @throws  ServerException if a listener with the same ID already exists.
     */
    public void add(ConnectionListener connectionListener) throws ServerException;
    
    /**
     * Remove a connection listener.
     * 
     * @param   listenerID  [in]    Supplies the unique ID of the connection
     *                              listener to be removed.
     * 
     * @return  A reference to the removed listener, or null if it didn't
     *          exist.
     */
    public ConnectionListener remove(String listenerID);
    
    /**
     * Check to see if the manager is empty.
     * 
     * @return  True if the manager does not contain any listeners, false if it
     *          contains at least one.
     */
    public boolean isEmpty();
    
    /**
     * Check to see if the listeners are active.
     * 
     * @return  True if the listeners are active, false if not.
     */
    public boolean isRunning();

    /**
     * Get a list of all connection listeners.
     * 
     * @return  A list of listeners.
     */
    public List<String> listeners();
    
    /**
     * Get a reference to a connection listener.
     * 
     * @param   listenerID  [in]    Supplies the unique ID of the listener.
     * 
     * @return  A reference to the listener, null if it does not exist.
     */
    public ConnectionListener getListener(String listenerID);
    
    /**
     * Start all listeners.  One or more listener may fail to start due to an
     * IOException.  If all listeners fail to start, this method throws a
     * ServerException.  If one or more listeners failed to start, but at
     * least one started successfully, this method will return normally, but
     * with a return value of false, so that the caller knows that one or
     * more listener failed to start, and can check the individual listeners
     * to see which failed, and why.  The way this would be done would be to
     * iterate over this object's collection of listeners, checking to see which
     * listeners failed to start.  When a non-started listener is encountered,
     * the caller would then attempt to start that listener individually, and
     * then handle the resulting IOException individually.  Finally, if all
     * listeners start successfully, this method returns true.
     *
     * @return  True if all listeners were started successfully, or false if
     *          one or more (but not all!) failed to start properly.
     *
     * @throws  ServerException if no listeners were started successfully.
     */
    public boolean startAll() throws ServerException;
    
    /**
     * Stop all listeners.
     */
    public void stopAll();
}
