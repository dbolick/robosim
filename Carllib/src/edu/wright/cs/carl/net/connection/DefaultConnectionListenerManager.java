
package edu.wright.cs.carl.net.connection;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import edu.wright.cs.carl.net.server.ServerException;


/**
 * Default implementation of ConnectionListenerManager
 *
 * @author  Duane Bolick
 * 
 * @see     ConnectionListenerManager
 */
public class DefaultConnectionListenerManager implements ConnectionListenerManager
{
    private Map<String, ConnectionListener> listenerMap;
    private boolean isRunning;
    
    public DefaultConnectionListenerManager()
    {
        this.listenerMap = new HashMap<String, ConnectionListener>();
    }
    
    /**
     * Add a connection listener.
     * 
     * @param   connectionListener  [in]    Supplies a reference to the new
     *                                      listener.
     * 
     * @throws  ServerException if a listener with the same ID already exists.
     */
    public synchronized void add(ConnectionListener connectionListener) throws ServerException
    {
        assert(connectionListener != null);
        
        String listenerID = null;
        
        try {
            listenerID = connectionListener.getUniqueID();
        }
        catch(IOException e) {
            //
            // Should not ever happen.  Debug if it does.
            //
            e.printStackTrace();
            System.exit(0);
        }
        
        if(this.listenerMap.containsKey(listenerID)) {
            throw new ServerException("DefaultConnectionListenerManager.add:  ConnectionListener with that name already exists.");
        }

        this.listenerMap.put(listenerID, connectionListener);
    }
    
    /**
     * Remove a connection listener.
     * 
     * @param   listenerID  [in]    Supplies the unique ID of the connection
     *                              listener to be removed.
     * 
     * @return  A reference to the removed listener, or null if it didn't
     *          exist.
     */
    public synchronized ConnectionListener remove(String listenerID)
    {
        assert(listenerID != null && !listenerID.isEmpty());
        
        return this.listenerMap.remove(listenerID);
    }
    
    /**
     * Check to see if the manager is empty.
     * 
     * @return  True if the manager does not contain any listenerMap, false if it
     *          contains at least one.
     */
    public synchronized boolean isEmpty()
    {
        return this.listenerMap.isEmpty();
    }

    /**
     * Check to see if the listenerMap are active.
     *
     * @return  True if the listenerMap are active, false if not.
     */
    public synchronized boolean isRunning()
    {
        return this.isRunning;
    }

    /**
     * Get a list of all connection listenerMap.
     * 
     * @return  A list of listenerMap.
     */
    public List<String> listeners()
    {
        return new ArrayList<String>(this.listenerMap.keySet());
    }
    
    /**
     * Get a reference to a connection listener.
     * 
     * @param   listenerID  [in]    Supplies the unique ID of the listener.
     * 
     * @return  A reference to the listener, null if it does not exist.
     */
    public ConnectionListener getListener(String listenerID)
    {
        return this.listenerMap.get(listenerID);
    }
    
    /**
     * Start all listenerMap.  One or more listener may fail to start due to an
     * IOException.  If all listenerMap fail to start, this method throws a
     * ServerException.  If one or more listenerMap failed to start, but at
     * least one started successfully, this method will return normally, but
     * with a return value of false, so that the caller knows that one or
     * more listener failed to start, and can check the individual listenerMap
     * to see which failed, and why.  The way this would be done would be to
     * iterate over this object's collection of listenerMap, checking to see which
     * listenerMap failed to start.  When a non-started listener is encountered,
     * the caller would then attempt to start that listener individually, and
     * then handle the resulting IOException individually.  Finally, if all
     * listenerMap start successfully, this method returns true.
     *
     * @return  True if all listenerMap were started successfully, or false if
     *          one or more (but not all!) failed to start properly.
     *
     * @throws  ServerException if no listenerMap were started successfully.
     */
    public boolean startAll() throws ServerException
    {
        boolean allStarted = true;
        boolean atLeastOneStarted = false;
        
        Iterator<ConnectionListener> it = this.listenerMap.values().iterator();
        while(it.hasNext()) {
            try {
                it.next().start();
                atLeastOneStarted = true;
            }
            catch(ServerException e) {
                //
                // This only happens if the listener is already started.  Since
                // it is possible for listenerMap to be started and stopped
                // individually, we ignore this.
                //
                atLeastOneStarted = true;
            }
            catch(IOException e) {
                //
                // This happens if the listener fails to start.  We need to
                // let the caller know if this is the case...
                //
                e.printStackTrace();
                allStarted = false;
            }
        }

        if(atLeastOneStarted == false) {
            this.isRunning = false;
            throw new ServerException("No listeners were sucessfully started.  Please check your server settings and try again.");
        }

        this.isRunning = true;
        return allStarted;
    }
    
    /**
     * Stop all listenerMap.
     */
    public void stopAll()
    {
        Iterator<ConnectionListener> it = this.listenerMap.values().iterator();
        while(it.hasNext()) {
            try {
                it.next().stop();
            }
            catch(ServerException e) {
                //
                // This happens if the listener is already stopped.  We don't
                // care if this happens, since one or more of the listenerMap may
                // have been stopped separately.
                //
            }
            catch(IOException e) {
                //
                // This happens if an IOException occurs on stopping a listener.
                // Since ConnectionListener.stop() handles this failure by
                // setting ConnectionListener.isListening() to false, there's
                // not anything else we can do in this case.
                //
                e.printStackTrace();
            }
        }

        this.isRunning = false;
    }
}
