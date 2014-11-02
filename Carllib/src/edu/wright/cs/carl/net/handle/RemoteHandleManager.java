
package edu.wright.cs.carl.net.handle;

import java.util.List;


/**
 * <p>
 * A RemoteHandleManager stores and provides access to a collection of
 * RemoteHandles.  It is responsible for ensuring that only one instance
 * of a given RemoteHandle exists, and for enforcing a maximum number of
 * RemoteHandles.
 * </p>
 * 
 * @author  Duane Bolick
 * 
 * @see     RemoteHandle
 */
public interface RemoteHandleManager
{
    /**
     * Set the maximum number of handles  If there are currently more
     * handles in the manager, the manager will not remove any.  It
     * will just reject the addition of any new handles.  If you want to
     * allow an unlimited number of handles, set this to a negative value.
     * 
     * @param   maxNumHandles   [in]    Suppies the new maximum number of
     *                                  handles.
     */
    public void setMaxNumHandles(int maxNumHandles);
    
    /**
     * Get the maximum number of handles that this RemoteHandleManager
     * can contain.  If there is no limit, this method will return -1.
     * 
     * @return  The maximum number of handles.
     */
    public int getMaxNumHandles();
    
    /**
     * Get the number of handles.
     * 
     * @return  The current number of handles.
     */
    public int getCurrentNumHandles();
    
    /**
     * Add a Handle
     *
     * @param   handle  [in]    Supplies a RemoteHandle.
     *
     * @return  True if the handle is added successfully, false if there are
     *          already the maximum number handles present, or connection to
     *          the handle was lost.
     *
     * @throws  DuplicateHandleException if a handle with the same ID exists.
     */
    public boolean addHandle(RemoteHandle handle) throws DuplicateHandleException;
  
    /**
     * Remove a handle.
     * 
     * @param   handleID    [in]    Supplies the handle ID.
     * 
     * @return  A reference to the removed RemoteHandle, or null if a handle
     *          with that ID did not exist.
     */
    public RemoteHandle removeHandle(String handleID);
    
    /**
     * Remove all Connections.
     */
    public List<RemoteHandle> clear();
    
    /**
     * Get a list of unique IDs of all RemoteHandles.
     * 
     * @return    A list of IDs of all RemoteHandles.
     */
    public List<String> getHandleIDs();
    
    /**
     * Get a handle by unique ID
     * 
     * @param   handleID    [in]  Supplies the unique handle ID.
     * 
     * @return  a reference to the RemoteHandle, or null if a handle with that
     *          unique ID did not exist.
     */
    public RemoteHandle getHandle(String handleID);
}
