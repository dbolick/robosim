
package edu.wright.cs.carl.security;

import edu.wright.cs.carl.net.message.payload.*;


/**
 * 
 *
 * @author Duane
 */
public class DefaultUserCredentials implements UserCredentials
{
    private String username;
    private String passwordHash;
    
    public DefaultUserCredentials()
    {
        this.username = new String();
        this.passwordHash = new String();
    }
    
    public DefaultUserCredentials(String username, String passwordHash)
    {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public void setName(String username)
    {
        this.username = username;
    }
    
    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }
    
    public String getName()
    {
        return this.username;
    }
    
    public Object getLoginPayload()
    {
        return new SimpleText("Login Successful.");
    }
    
    public Object getLogoutPayload()
    {
        return new SimpleText("Logout Successful.");
    }
    
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

    @Override
    public int hashCode()
    {
        return (username + passwordHash).toString().hashCode();
    }
}
