
package edu.wright.cs.carl.security.accounts;

import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ConcurrentHashMap;

import edu.wright.cs.carl.security.UserCredentials;


/**
 * Default Account Manager implementation.
 *
 * @author  Duane
 */
public class DefaultAccountManager implements AccountManager
{
    private ConcurrentHashMap<String,UserCredentials> authorizedUsers;

    /**
     * Constructor.
     */
    public DefaultAccountManager()
    {
        this.authorizedUsers = new ConcurrentHashMap<String,UserCredentials>();
    }
    
    /**
     * Authenticate the provided UserCredentials with the AccountManager.
     * 
     * @param   userCredentials [in]    Supplies authenticate credentials.
     * 
     * @return  The login message associated with the provided credentials if
     *          successful, or null if the UserCredentials do not match any
     *          stored in the AccountManager.
     */
    public Object authenticate(UserCredentials userCredentials)
    {
        //
        // The key values in the authorizedUsers Map are the usernames of the
        // stored UserCredentials.
        //
        UserCredentials storedCredentials = this.authorizedUsers.get(userCredentials.getName());

        //
        // This happens if the username wasn't found in the Map.
        //
        if(storedCredentials == null) {
            return null;
        }
        
        //
        // Now, this comparison is *very* important.  We must use the equals()
        // method of the storedCredentials, because we assume that those are all
        // legitimate UserCredentials objects, and implement whatever equality
        // check required by the UserCredentials implementation.  If we were to
        // do this the other way around:
        //
        //      return userCredentials.equals(storedCredentials);
        //
        // a malicious user could define a UserCredentials object whose equals()
        // method always returned true, and could authenticate themselves as any
        // Principal as long as they knew the username.
        //
        if(storedCredentials.equals(userCredentials)) {
            return storedCredentials.getLoginPayload();
        }
        else{
            return null;
        }
    }
    
    /**
     * Add an account with the given UserCredentials object to the collection of
     * allowed users.  Enforces uniqueness and non-emptiness.
     * 
     * @param   userCredentials [in]    Supplies the UserCredentials to be
     *                                  added.
     * 
     * @return  True if the account was added successfully, false if the
     *          username in the provided UserCredentials already exists. 
     */
    public boolean addUserAccount(UserCredentials userCredentials)
    {
        String newUsername = userCredentials.getName();
        
        //
        // The check for uniqueness of the new username and the addition of the
        // new UserCredentials must be atomic.
        //
        synchronized(this.authorizedUsers){
            if(!isAcceptable(newUsername) || !isUnique(newUsername)) {
                return false;
            }
        
            this.authorizedUsers.put(userCredentials.getName(), userCredentials);
        }
        
        return true;
    }
    
    /**
     * Remove the account associated with the given username.  An account with
     * the given username must exist.
     * 
     * @param   username    [in]    Supplies the username of the account to be
     *                              removed.
     * 
     * @return  A reference to the UserCredentials object that was removed, or
     *          null if the UserCredentials did not exist.
     */
    public UserCredentials removeUserAccount(String username)
    {
        return this.authorizedUsers.remove(username);        
    }
    
    /**
     * Get a List of usernames of all accounts.
     * 
     * @return  A List of usernames.
     */
    public List<String> getAccountList()
    {
        return new ArrayList<String> (this.authorizedUsers.keySet());
    }
    
    /**
     * Get an account by username.
     * 
     * @param   username    [in]    Supplies the username.
     * 
     * @return  The UserCredentials associated with the username, or null if
     *          the account does not exist.
     */
    public UserCredentials getAccount(String username)
    {
        return this.authorizedUsers.get(username);
    }
        
    /**
     * Private methods.
     */
    
    /**
     * Check to see if the supplied username already exists.
     * 
     * @param   username    [in]    Supplies the username
     * 
     * @return  True if the username is not in use, false otherwise.
     */
    public boolean isUnique(String username)
    {
        return (false == this.authorizedUsers.containsKey(username));
    }
    
    /**
     * Checks to see if the supplied username is acceptable for use on this
     * Server.  For this implementation, usernames can't be empty.  Thatisall.
     * 
     * @param    username   [in]    Supplies the username.
     * 
     * @return   True if the supplied username is acceptable, false otherwise.
     */
    private boolean isAcceptable(String username)
    {
        return (username.length() > 0);
    }
}
