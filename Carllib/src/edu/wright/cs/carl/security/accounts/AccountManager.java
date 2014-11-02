
package edu.wright.cs.carl.security.accounts;

import java.util.List;

import java.io.Serializable;

import edu.wright.cs.carl.net.message.Message;

import edu.wright.cs.carl.security.UserCredentials;


/**
 * <p>
 * An AccountManager implementation is responsible for providing read/write
 * access to a persistent collection of UserCredentials, each of which represent
 * a user who is allowed to connect to the Server.
 * </p>
 * 
 * <p>
 * Additionally, an AccountManager implementation is responsible for enforcing
 * the uniqueness of each user's UserCredentials.  
 * </p>
 * 
 * <p>
 * Finally, an AccountManager implementation is responsible for conveying any
 * pertinent "at-authenticate" Messages, defined by the UserCredentials,
 * to users.  For example, a simple username/password-hash authenticate may have
 * a time, or number-of-logins limit until a password change is required, or a
 * one-time-password UserCredentials implementation may need to transmit the
 * next password back to the user each authenticate.
 * </p>
 * 
 * <p>
 * The intended use of an AccountManager is to compare user-provided
 * credentials (i.e., a UserCredentials object) to its stored collection of
 * UserCredentials, and if they matche one of those stored, return success.
 * </p>
 * 
 * <p>
 * Of note, more finely-grained access control should be implemented using 
 * groups, permissions, and access-control lists.  Default implementations of
 * these are provided in the edu.wright.carl.cs.security package.
 * </p>
 * 
 * @author  Duane Bolick
 * 
 * @see     edu.wright.cs.carl.security.UserCredentials
 * @see     edu.wright.cs.carl.security.DefaultGroup
 * @see     edu.wright.cs.carl.security.DefaultAcl
 */
public interface AccountManager extends Serializable
{
    /**
     * Authenticate the provided UserCredentials with the AccountManager.
     * 
     * @param   userCredentials [in]    Supplies authenticate credentials.
     * 
     * @return  The login payload associated with the provided credentials if
     *          successful, or null if the UserCredentials do not match any
     *          stored in the AccountManager.
     */
    public Object authenticate(UserCredentials userCredentials);
    
    /**
     * Add an account with the given UserCredentials object to the collection of
     * allowed users.  Should enforce uniqueness of username.
     * 
     * @param   userCredentials [in]    Supplies the UserCredentials to be
     *                                  added.
     * 
     * @return  True if the account was added successfully, false if the
     *          username in the provided UserCredentials already exists. 
     */
    public boolean addUserAccount(UserCredentials userCredentials);
    
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
    public UserCredentials removeUserAccount(String username);
    
    /**
     * Get a List of usernames of all accounts.
     * 
     * @return  A List of usernames.
     */
    public List<String> getAccountList();
    
    /**
     * Get an account by username.
     * 
     * @param   username    [in]    Supplies the username.
     * 
     * @return  The UserCredentials associated with the username, or null if
     *          the account does not exist.
     */
    public UserCredentials getAccount(String username);
}
