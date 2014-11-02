
package edu.wright.cs.carl.security;

import edu.wright.cs.carl.net.message.payload.*;


/**
 * <p>
 * AnonymousUser lets you allow anonymous access to your Server (generally not
 * a good idea).  In order to do this, simply create an AnonymousUser and
 * add that user to your AccountManager implementation.  Users then login
 * anonymously by providing an AnonymousUser as their UserCredentials.
 * </p>
 *
 * @author Duane
 */
public class AnonymousUser implements UserCredentials
{
    private String username;
    
    /**
     * Constructor.
     */
    public AnonymousUser()
    {
        //
        // DSB 4 Aug 08
        // Use the name "anonymous" as the userID.  This prevents anyone from
        // registering this username as a side-effect.
        //
        username = "anonymous";
    }
  
    public String getName()
    {
        return username;
    }

    public void setName(String username)
    {
        
    }
    
    public void setPasswordHash(String passwordHash)
    {
        
    }
    
    public Object getLoginPayload()
    {
        return new SimpleText("Anonymous login successful.");
    }
    
    public Object getLogoutPayload()
    {
        return new SimpleText("Anonymous logout successful.");
    }
    
    @Override
    public boolean equals(Object object)
    {
        UserCredentials login = (UserCredentials) object;
        return this.hashCode() == login.hashCode();
    }

    @Override
    public int hashCode()
    {
        //
        // DSB 4 Aug 08
        // This fulfills the hashCode contract and is correct.  It's probably
        // not good form, though.
        //
        return 0;
    }
}
