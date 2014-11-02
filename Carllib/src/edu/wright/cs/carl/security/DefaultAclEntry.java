
package edu.wright.cs.carl.security;

import java.io.Serializable;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Enumeration;

import java.security.acl.AclEntry;
import java.security.acl.Permission;
import java.security.Principal;
import java.security.acl.Group;


/**
 * 
 *
 * @author Duane
 */
public class DefaultAclEntry implements AclEntry, Serializable
{
    private Set<Permission> permissionList = Collections.synchronizedSet(new HashSet<Permission>());
    private boolean isNegative = false;
    private Principal principal = null;
    
    public DefaultAclEntry()
    {
        
    }
    
    public DefaultAclEntry(Principal principal)
    {
        this.principal = principal;
    }
    
    public boolean addPermission(Permission permission)
    {
        return permissionList.add(permission);
    }
          
    public boolean removePermission(Permission permission)
    {
        return permissionList.remove(permission);
    }
    
    public boolean checkPermission(Permission permission)
    {
        return permissionList.contains(permission);
    }
          
    public Enumeration<Permission> permissions()
    {
        return Collections.enumeration(permissionList);
    }
    
    
    
    public Principal getPrincipal()
    {
        return principal;
    }

    
    public boolean setPrincipal(Principal principal)
    {
        this.principal = principal;
        return true;
    }
    
    
    
    public boolean isNegative() 
    {
        return isNegative;
    }
          
    public void setNegativePermissions()
    {
        isNegative = true;
    }
          
    
    
    @Override
    public String toString() 
    {
        StringBuffer s = new StringBuffer();
        if (isNegative){
            s.append("-");
        }
        else{
            s.append("+");
        }
        
        if (principal instanceof Group){
            s.append("Group.");
        }
        else{
            s.append("User.");
        }
        
        s.append(principal + "=");
        
        Enumeration<Permission> e = permissions();
        while (e.hasMoreElements()){
            Permission p = e.nextElement();
            s.append(p);
            if (e.hasMoreElements()){
                s.append(",");
            }
        }
        
        return new String(s);
    }

    @Override
    public Object clone()
    {
        DefaultAclEntry clonedEntry = new DefaultAclEntry(this.principal);
        clonedEntry.permissionList = (Set<Permission>)((HashSet<Permission>)this.permissionList).clone();
        clonedEntry.isNegative = this.isNegative;
        return clonedEntry;
    }
    
}
