
package edu.wright.cs.carl.net.server;


/**
 * This exception class is thrown whenever unexpected conditions are encountered
 * in the normal execution of a Server
 *
 * @author  Duane Bolick
 * 
 * @see     Exception
 * @see     edu.wright.cs.carl.net.Server
 */
public class ServerException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public ServerException()
    {
        super("edu.wright.cs.carl.net.ServerException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public ServerException(String message)
    {
        super(message);
    }
}
