
package edu.wright.cs.carl.security;

import java.util.HashSet;
import java.util.Collections;
import java.util.Enumeration;

import java.security.Principal;
import java.security.acl.Group;

/**
 * 
 *
 * @author Duane
 */
public class DefaultGroup extends AbstractGroup
{
    private String groupName;
    
    public DefaultGroup(String groupName)
    {
        this.memberList = Collections.synchronizedSet(new HashSet<Principal>());
        if(groupName == null){
            throw new NullPointerException("DefaultGroup Constructor: Name is null.");
        }
        this.groupName = groupName;
    }
            
    public boolean addMember(Principal p)
    {
        if(p.equals(this)){
            throw new IllegalArgumentException("DefaultGroup addMember: Can't add a Group to itself.");
        }
        return memberList.add(p);
    }
          
    public boolean removeMember(Principal p)
    {
        return memberList.remove(p);
    }
    
        
    public Enumeration<? extends Principal> members()
    {
        return Collections.enumeration(memberList);
    }
        
   
      public String getName()
    {
        return groupName;
    }


    @Override
    public String toString()
    {
        return "Group." + groupName;
    }


    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Group){
            Group g = (Group)o;
            return this.toString().equals(g.toString());
        }
        return false;
    }
 

    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    
}
