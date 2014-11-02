
package edu.wright.cs.carl.security;

import java.io.Serializable;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import java.security.Principal;
import java.security.acl.Group;

/**
 * This abstract class defines the isMember method.  Only classes that extend
 * AbstractGroup should be used in the Group hierarchy.  The isMember method
 * will not recursively search into Group objects that do not inherit from
 * this abstract class.  The reason for this is that if a method defines its
 * own isMember, there is the possibility of infinitely recursing if there's
 * a loop in the Group tree involving an AbstractGroup type, and another, non-
 * AbstractGroup type.
 *
 * @author Duane
 */
public abstract class AbstractGroup implements Group, Serializable
{
    protected Collection memberList;
    
    public AbstractGroup()
    {
        
    }
    
    final public boolean isMember(Principal p)
    {
        Vector<AbstractGroup> visited = new Vector<AbstractGroup>();
        visited.add(this);
        return isMemberHelper(p, visited);
    }
    
    final private boolean isMemberHelper(Principal p, Vector<AbstractGroup> visited)
    {
        //
        // If the top-level group contains the Principal we're looking for,
        // return true.
        //
        if(memberList.contains(p)){
            return true;
        }
        
        //
        // Otherwise, iterate over all the members, looking for AbstractGroup
        // types.
        //
        Enumeration<? extends Principal> e = this.members();
        boolean foundMember = false;
        while(e.hasMoreElements() && foundMember == false){
            Principal next = e.nextElement();
           
            //
            // If one of the top-level's members is an AbstractGroup, and we
            // haven't already seen it in this branch, recursively search it.
            //
            if(next instanceof AbstractGroup && !visited.contains(next)){
                visited.add((AbstractGroup)next);
                foundMember = ((AbstractGroup)next).isMemberHelper(p, visited);
            }
        }
        
        return foundMember;
    }
   
}
