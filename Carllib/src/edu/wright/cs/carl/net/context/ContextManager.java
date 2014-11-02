
package edu.wright.cs.carl.net.context;

import java.util.List;


/**
 * <p>
 * ContextManager is responsible for maintaining a read/write collection of
 * Context objects, and ensuring the uniqueness of context IDs.
 * </p>
 * 
 * <p>
 * Access to the ContextManager is the responsibility of whoever uses it.
 * Granting or denying access to the Context objects themselves is the
 * responsiblity of those Context objects.
 * </p>
 *
 * @author  Duane
 * 
 * @see     Context
 */
public interface ContextManager
{
    /**
     * Add a new Context to the collection of active contexts.
     * 
     * @param   newContext      [in]    Supplies a reference to the context to
     *                                  be added.
     * 
     * @return  True if there isn't another Context with the same name, false
     *          otherwise.
     */
    public boolean addContext(Context newContext);
    
    /**
     * Remove the Context with the given name.
     * 
     * @param   contextID   [in]    Supplies the ID of the Context to be
     *                              removed.
     * 
     * @return  A reference to the removed Context, or null if a Context with
     *          the given contextName does not exist.
     */
    public Context removeContext(String contextID);
    
    /**
     * Get a list of Context IDs.
     * 
     * @return  A List of Context IDs.
     */
    public List<String> getContextList();
    
    /**
     * Get a reference to a Context with the given ID.
     * 
     * @param   contextID   [in]    Supplies the desired context ID.
     * 
     * @return  A reference to the specified Context, or null if no such Context
     *          exists.
     */
    public Context getContext(String contextID);
    
    /**
     * Remove the client with the given username from all active
     * Contexts.  This is used when a user leaves the server, or a user's
     * connection is dropped.
     * 
     * @param   username    [in]    Supplies the username to be removed.
     */
    public void disconnectClientFromAll(String username);
}
