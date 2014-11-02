
package edu.wright.cs.carl.security;

import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;

import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Permission;
import java.security.Principal;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;


/**
 * <p>
 * This default implementation of an access control list 
 * (<code>java.security.acl.Acl</code>) is intentionally restrictive in that it
 * only recognizes "allow" entries.  In other words, if you haven't been
 * granted explicit permission, you can't do it.
 * </p>
 * 
 * <p>
 * This forces administrators to be more careful when assigning a
 * <code>Principal</code> to a <code>Group</code> and mitigates the risk of
 * forgetting to "deny" a given permission.  If you don't want a
 * <code>Principal</code> to have all the permissions of a given 
 * <code>Group</code> (even though you want him to have all but one of the
 * <code>Group</code> permissions), then don't assign him to that 
 * <code>Group</code>.  Either assign the permissions separately, or create a
 * new <code>Group</code> with the desired permissions.
 * </p>
 * 
 * @author Duane
 */
public class DefaultAcl extends DefaultOwner implements Acl
{
    private String name = null;
    
    private Map<Principal,AclEntry> userEntries = new HashMap<Principal,AclEntry>();
    private Map<AbstractGroup,AclEntry> groupEntries = new HashMap<AbstractGroup,AclEntry>();
    
    public DefaultAcl(DefaultAcl acl)
    {
        super(acl.owners);
        this.name = new String(acl.getName());
        this.userEntries = new HashMap<Principal, AclEntry>(acl.userEntries);
        this.groupEntries = new HashMap<AbstractGroup,AclEntry>(acl.groupEntries);
    }
    
    public DefaultAcl(String name, Group owners)
    {
        super(owners);
        this.name = name;
    }
    
    
    public boolean addEntry(Principal caller, AclEntry entry) throws NotOwnerException
    {
        if(!owners.isMember(caller)){
            throw new NotOwnerException();
        }
        
        Principal p = entry.getPrincipal();
            
        if(p instanceof AbstractGroup && !groupEntries.containsKey(p)){
            groupEntries.put((AbstractGroup)p, entry);
            return true;
        }
        else if(p instanceof Principal && !userEntries.containsKey(p)){
            userEntries.put(p, entry);
            return true;
        }
        
        return false;
    }
         
    public boolean removeEntry(Principal caller, AclEntry entry) throws NotOwnerException
    {
        if(!owners.isMember(caller)){
            throw new NotOwnerException();
        }
        
        Principal p = entry.getPrincipal();
            
        if(p instanceof AbstractGroup && groupEntries.containsKey(p)){
            groupEntries.remove(p);
            return true;
        }
        else if(p instanceof Principal && userEntries.containsKey(p)){
            userEntries.remove(p);
            return true;
        }
        
        return false;
    }

    
    public Enumeration<AclEntry> entries()
    {
        Vector<AclEntry> allEntries = new Vector<AclEntry>();
        
        Iterator<AclEntry> it = this.userEntries.values().iterator();
        while(it.hasNext()) {
            allEntries.add(it.next());
        }
        
        it = this.groupEntries.values().iterator();
        while(it.hasNext()) {
            allEntries.add(it.next());
        }
        
        return allEntries.elements();
    }
    
    public boolean checkPermission(Principal principal, Permission permission)
    {
       Enumeration<Permission> e = this.getPermissions(principal);
       while(e.hasMoreElements()){
           if(((Permission)e.nextElement()).equals(permission)){
               return true;
           }
       }
       return false;
    }
               
    public Enumeration<Permission> getPermissions(Principal user)
    {
       Vector<Permission> userPermissions = new Vector<Permission>();
       
       //
       // Get all of the permissions specific to the user.
       //
       userPermissions.addAll(Collections.list(userEntries.get(user).permissions()));
       
       //
       // Get all of the permissions for all the Groups the user belongs to.
       //
       Iterator<AbstractGroup> it = groupEntries.keySet().iterator();
       AbstractGroup currentGroup;
       while(it.hasNext()){
           currentGroup = (AbstractGroup)it.next();
           if(currentGroup.isMember(user)){
               userPermissions.addAll(Collections.list(groupEntries.get(currentGroup).permissions()));
           }
       }
       
       return userPermissions.elements();
    }
    
    public String getName() 
    {
        return this.name;
    }
          
              
    public void setName(Principal caller, String name) throws NotOwnerException
    {
        if(!owners.isMember(caller)){
            throw new NotOwnerException();
        }
        
        this.name = name;
    }

    @Override
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        Enumeration<AclEntry> allEntries = this.entries();
        while(allEntries.hasMoreElements()){
            s.append(allEntries.toString() + "\n");
        }
        return s.toString();
    }
   
}
