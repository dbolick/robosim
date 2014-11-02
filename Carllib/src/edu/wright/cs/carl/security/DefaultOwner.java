
package edu.wright.cs.carl.security;

import java.io.Serializable;

import java.util.Collections;

import java.security.acl.Owner;
import java.security.acl.Group;
import java.security.Principal;
import java.security.acl.NotOwnerException;
import java.security.acl.LastOwnerException;

/**
 * 
 *
 * @author Duane
 */
public class DefaultOwner implements Owner, Serializable
{
    protected Group owners = new DefaultGroup("Owners");
    
    public DefaultOwner()
    {
        
    }
    
    public DefaultOwner(Principal owner)
    {
        owners.addMember(owner);
    }
    
    public boolean addOwner(Principal caller, Principal owner) throws NotOwnerException
    {
        if(!isOwner(caller)){
            throw new NotOwnerException();
        }
        
        return owners.addMember(owner);
    }
        
    public boolean deleteOwner(Principal caller, Principal owner) throws NotOwnerException, LastOwnerException
    {
        if(!isOwner(caller)){
            throw new NotOwnerException();
        }
        
        if(Collections.list(owners.members()).size() < 2){
            throw new LastOwnerException();
        }
            
        return owners.removeMember(owner);
    }
        
    public boolean isOwner(Principal owner)
    {
        return owners.isMember(owner);
    }
         
}
