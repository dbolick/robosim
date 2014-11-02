/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.wright.cs.carl.apps.wartapp.swing;

import edu.wright.cs.carl.security.UserCredentials;

/**
 * 
 *
 * @author Duane
 */
public class AccountListEntry
{
    private UserCredentials userCredentials;
    
    public AccountListEntry(UserCredentials userCredentials)
    {
        this.userCredentials = userCredentials;
    }
    
    @Override
    public String toString()
    {
        return this.userCredentials.getName();
    }
    
    public UserCredentials getCredentials()
    {
        return this.userCredentials;
    }
}
