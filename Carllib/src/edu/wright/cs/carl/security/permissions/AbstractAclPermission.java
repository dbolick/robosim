
package edu.wright.cs.carl.security.permissions;

import java.io.Serializable;

import java.security.acl.Acl;
import java.security.acl.Permission;


/**
 * Abstract class for basic named Permission types for use in access control
 * lists (<i>Acl</i>).
 *
 * @author  Duane Bolick
 * 
 * @see     Acl
 * @see     Permission
 */
public class AbstractAclPermission implements Permission, Serializable
{
    private String name;
    
    
    /**
     * Constructor.
     * 
     * @param   name    [in]    Supplies the name of the Permission.
     */
    public AbstractAclPermission(String name)
    {
        this.name = name;
    }

    
    /**
     * Returns true if the object passed matches the permission represented 
     * in this interface.
     * 
     * @param another the Permission object to compare with.
     * 
     * @return true if the Permission objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj){
            return true;
        }
        
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        
        return this.hashCode() == obj.hashCode();
    }
    
    
    /**
     * Returns the hashcode value of this object.
     * 
     * @return  Hashcode of this object.
     */
    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }
    
    /**
     * Prints a string representation of this permission.
     * 
     * @return the string representation of the permission.
     */
    @Override
    public String toString()
    {
        return this.getClass().getCanonicalName().toString() + "." + this.name;
    }
}
