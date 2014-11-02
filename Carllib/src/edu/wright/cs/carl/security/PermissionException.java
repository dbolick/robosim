
package edu.wright.cs.carl.security;

import java.security.GeneralSecurityException;

/**
 * This exception class is thrown whenever a user attempts to perform an
 * operation for which they do not possess the appropriate Permission.
 *
 * @author  Duane Bolick
 * 
 * @see     GeneralSecurityException
 * @see     java.security.Permission
 */
public class PermissionException extends GeneralSecurityException
{
    /**
     * No-argument constructor.  Calls the super-class,
     * java.security.GeneralSecurityException constructor.
     */
    public PermissionException()
    {
        super("edu.wright.cs.carl.security.PermissionException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public PermissionException(String message)
    {
        super(message);
    }
    
    /**
     * Constructor for a uniformly formatted PermissionException message.
     * 
     * @param   principalName       [in]    Name of the Principal
     * @param   canonicalMethodName [in]    Name of the method
     * @param   message             [in]    Reason for failure
     */
    public PermissionException(String principalName, String canonicalMethodName, String message)
    {
        super("PermissionException: Username[" + principalName + "]  Method[" + canonicalMethodName + "]: " + message);
    }
}
