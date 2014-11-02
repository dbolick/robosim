
package edu.wright.cs.carl.security;

import java.io.Serializable;

import java.security.Principal;


/**
 * UserCredentials
 *
 * @author  Duane Bolicks
 */
public interface UserCredentials extends Principal, Serializable
{
    public void setName(String username);
    public void setPasswordHash(String passwordHash);
    public Object getLoginPayload();
    public Object getLogoutPayload();
}
