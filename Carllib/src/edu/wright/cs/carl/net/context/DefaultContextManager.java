
package edu.wright.cs.carl.net.context;

import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Default ContextManager implementation.
 *
 * @author Duane Bolick
 */
public class DefaultContextManager implements ContextManager
{
    private Map<String,Context> contexts;
    
    
    /**
     * Constructor.
     */
    public DefaultContextManager()
    {
        this.contexts = new ConcurrentHashMap<String,Context>();
    }

    /**
     * Add a new Context to the collection of active contexts.
     * 
     * @param   newContext      [in]    Supplies a reference to the context to
     *                                  be added.
     * 
     * @return  True if there isn't another Context with the same name, false
     *          otherwise.
     */
    public boolean addContext(Context newContext)
    {
        String contextID = newContext.getUniqueID();
        
        synchronized(this.contexts){
            if(this.contexts.containsKey(contextID)){
                return false;
            }
            this.contexts.put(contextID, newContext);
            return true;
        }       
    }
    
    /**
     * Remove the Context with the given name.
     * 
     * @param   contextID   [in]    Supplies the ID of the Context to be
     *                              removed.
     * 
     * @return  A reference to the removed Context, or null if a Context with
     *          the given ID does not exist.
     */
    public Context removeContext(String contextID)
    {
        return this.contexts.remove(contextID);
    }
    
    /**
     * Get a list of Context names.
     * 
     * @return  A List of Context names.
     */
    public List<String> getContextList()
    {
        return new ArrayList<String>(this.contexts.keySet());
    }
    
    /**
     * Get a reference to a Context with the given ID.
     * 
     * @param   contextID   [in]    Supplies the desired context ID.
     * 
     * @return  A reference to the specified Context, or null if no such Context
     *          exists.
     */
    public Context getContext(String contextID)
    {
        return this.contexts.get(contextID);
    }
    
    /**
     * Remove the client with the given username from all active
     * Contexts.  This is used when a user leaves the server, or a user's
     * connection is dropped.
     * 
     * @param   username    [in]    Supplies the username to be removed.
     */
    public void disconnectClientFromAll(String username)
    {
        Iterator<Context> it = this.contexts.values().iterator();
        while(it.hasNext()) {
            try {
                it.next().disconnectClient(username);
            }
            catch(ContextException e) {
                
            }
        }
    }
}
