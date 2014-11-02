
package edu.wright.cs.carl.security;

import java.io.Serializable;

import java.security.acl.Acl;

import edu.wright.cs.carl.security.accounts.AccountManager;

/**
 * 
 *
 * @author  Duane Bolick
 */
public class ServerSecurityManager implements Serializable
{
    private AccountManager accounts;
    private Acl serverAcl;
    private UserCredentials rootUser;
    
    public ServerSecurityManager(AccountManager accounts, Acl serverAcl, UserCredentials rootUser)
    {
        this.accounts = accounts;
        this.serverAcl = serverAcl;
        this.rootUser = rootUser;
    }
    
    public boolean isConsistent()
    {
        if(this.rootUser == null || this.accounts == null || this.serverAcl == null) {
            return false;
        }
        
        if(this.accounts.authenticate(this.rootUser) == null) {
            return false;
        }
        
        if(this.serverAcl.isOwner(this.rootUser) == false) {
            return false;
        }
        
        return true;
    }
    
    public UserCredentials getRootUser()
    {
        return this.rootUser;
    }
    
    public void setRootUser(UserCredentials rootUser)
    {
        this.rootUser = rootUser;
    }
    
    public AccountManager getAccounts()
    {
        return this.accounts;
    }
    
    public void setAccounts(AccountManager accounts)
    {
        this.accounts = accounts;
    }
    
    public Acl getServerAcl()
    {
        return this.serverAcl;
    }
    
    public void setServerAcl(Acl serverAcl)
    {
        this.serverAcl = serverAcl;
    }
}
