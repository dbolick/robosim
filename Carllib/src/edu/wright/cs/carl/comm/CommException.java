
package edu.wright.cs.carl.comm;


/**
 * This exception class is thrown whenever an attempt to create a Communicator
 * object fails.
 *
 * @author  Duane Bolick
 * 
 * @see     Exception
 * @see     edu.wright.cs.carl.comm.Communicator
 */
public class CommException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public CommException()
    {
        super("edu.wright.cs.carl.comm.CommException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public CommException(String message)
    {
        super(message);
    }
}
