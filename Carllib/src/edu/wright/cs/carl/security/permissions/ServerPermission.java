
package edu.wright.cs.carl.security.permissions;


/**
 * Basic named Permission type for use in top-level Server access management.
 *
 * @author  Duane Bolick
 */
public class ServerPermission extends AbstractAclPermission
{
    /**
     * Constructor.
     * 
     * @param   name    [in]    Supplies the name of the Permission.
     */
    public ServerPermission(String name)
    {
        super(name);
    }
}
