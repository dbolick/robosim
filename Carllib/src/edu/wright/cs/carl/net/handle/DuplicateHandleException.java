
package edu.wright.cs.carl.net.handle;


/**
 * This exception class is thrown whenever an attempt is made to add a
 * duplicate ConnectionHandle.
 *
 * @author  Duane Bolick
 * 
 * @see     Exception
 * @see     edu.wright.cs.carl.wart.handle.ConnectionHandle
 */
public class DuplicateHandleException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public DuplicateHandleException()
    {
        super("edu.wright.cs.carl.net.DuplicateHandleException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public DuplicateHandleException(String message)
    {
        super(message);
    }
}
