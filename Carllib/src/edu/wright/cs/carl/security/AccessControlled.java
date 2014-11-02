
package edu.wright.cs.carl.security;

import java.security.Principal;

import java.security.acl.Permission;
import java.security.acl.Acl;


/**
 * This interface declares methods that are required to implement code-level
 * access control to resources.
 * 
 * @author Duane
 */
public interface AccessControlled
{
    /**
     * Get a reference to the Access Control List (Acl) of this object.  The
     * caller must be an Owner of the Acl.
     * 
     * @param   caller  [in]    Supplies a reference to the Principal
     * 
     * @return  A reference to the Acl.
     * 
     * @throws  PermissionException if the caller is not an owner of the Acl.
     */
    public Acl getAcl(Principal caller) throws PermissionException;
    
    
    /**
     * Checks if the Principal has the given Permission in the context of this
     * object's Acl.
     * 
     * @param   principal   [in]    Supplies a reference to the Principal
     * @param   permission  [in]    Supplies a reference to the Permission
     * 
     * @return  True if the given Principal has the given Permission in this
     *          Acl, false otherwise.
     */
    public boolean hasPermission(Principal principal, Permission permission);
}
