
package edu.wright.cs.carl.net.handle;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;


/**
 * Default RemoteHandleManager implementation.
 *
 * @author  Duane
 */
public class DefaultRemoteHandleManager implements RemoteHandleManager
{
    private Map<String,RemoteHandle> handles;
    
    private int maxNumHandles;
    private int curNumHandles = 0;
    
    /**
     * Constructor.  If you don't want to limit the number of handles, then
     * give it a negative number.
     * 
     * @param   maxNumHandles   [in]    Supplies the maximum number of
     *                                  handles allowed.  If this is
     *                                  negative, there is no limit.
     */
    public DefaultRemoteHandleManager(int maxNumHandles)
    {
        handles = new HashMap<String,RemoteHandle>();
        this.maxNumHandles = maxNumHandles;
    }
    
    /**
     * Set the maximum number of handles  If there are currently more
     * handles in the manager, the manager will not remove any.  It
     * will just reject the addition of any new handles.  If you want to
     * allow an unlimited number of handles, set this to a negative value.
     * 
     * @param   maxNumHandles   [in]    Suppies the new maximum number of
     *                                  handles.
     */
    public void setMaxNumHandles(int maxNumHandles)
    {
        if(maxNumHandles < 0) {
            maxNumHandles = -1;
        }
        this.maxNumHandles = maxNumHandles;
    }
    
    /**
     * Get the maximum number of handles that this RemoteHandleManager
     * can contain.  If there is no limit, this method will return -1.
     * 
     * @return  The maximum number of handles.
     */
    public int getMaxNumHandles()
    {
        return this.maxNumHandles;
    }
    
    /**
     * Get the number of handles.
     * 
     * @return  The current number of handles.
     */
    public int getCurrentNumHandles()
    {
        return this.curNumHandles;
    }
    
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
    public boolean addHandle(RemoteHandle handle) throws DuplicateHandleException
    {
        if(this.maxNumHandles > 0 && this.curNumHandles >= this.maxNumHandles) {
            return false;
        }
        
        String handleID = null;
        
        try {
            handleID = handle.getUniqueID();
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        
        if(this.handles.get(handleID) != null) {
            throw new DuplicateHandleException("DefaultRemoteHandleManager.addHandle: Handle with ID " + handleID + " already exists.");
        }
        
        this.handles.put(handleID, handle);
        ++this.curNumHandles;

        return true;
    }
  
    /**
     * Remove a handle.
     *
     * @param   handleID    [in]    Supplies the handle ID.
     *
     * @return  A reference to the removed RemoteHandle, or null if a handle
     *          with that ID did not exist.
     */
    public RemoteHandle removeHandle(String handleID)
    {
        --this.curNumHandles;
        return this.handles.remove(handleID);
    }
    
    /**
     * Remove all handles.
     */
    public List<RemoteHandle> clear()
    {
        List<RemoteHandle> handleList = new ArrayList<RemoteHandle>(this.handles.values());
        this.handles.clear();
        this.curNumHandles = 0;
        return handleList;
    }
    
    /**
     * Get a list of unique IDs of all RemoteHandles.
     *
     * @return    A list of IDs of all RemoteHandles.
     */
    public List<String> getHandleIDs()
    {
        return new ArrayList<String>(this.handles.keySet());
    }
    
    /**
     * Get a handle by unique ID
     *
     * @param   handleID    [in]  Supplies the unique handle ID.
     *
     * @return  a reference to the RemoteHandle, or null if a handle with that
     *          unique ID did not exist.
     */
    public RemoteHandle getHandle(String handleID)
    {
        return this.handles.get(handleID);
    }
}
