package edu.wright.cs.carl.security;

import java.security.GeneralSecurityException;

/**
 * This exception class is thrown whenever a user attempts to login to a
 * Server without proper UserCredentials
 *
 * @author  Duane Bolick
 * 
 * @see     GeneralSecurityException
 * @see     java.security.Principal
 * @see     edu.wright.cs.carl.security.UserCredentials
 */
public class AuthenticationException extends GeneralSecurityException
{
    /**
     * No-argument constructor.  Calls the super-class,
     * java.security.GeneralSecurityException constructor.
     */
    public AuthenticationException()
    {
        super("edu.wright.cs.carl.security.AuthenticationException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public AuthenticationException(String message)
    {
        super(message);
    }
}

