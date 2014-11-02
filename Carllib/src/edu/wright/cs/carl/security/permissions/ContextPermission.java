
package edu.wright.cs.carl.security.permissions;


/**
 * Basic named Permission type for use in Context access management.
 *
 * @author  Duane Bolick
 */
public class ContextPermission extends AbstractAclPermission
{
    /**
     * Constructor.
     * 
     * @param   name    [in]    Supplies the name of the Permission.
     */
    public ContextPermission(String name)
    {
        super(name);
    }
}
