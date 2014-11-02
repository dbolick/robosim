
package edu.wright.cs.carl.net.context;


/**
 * This exception class is thrown whenever unexpected conditions are encountered
 * when interacting with a Context.
 *
 * @author  Duane Bolick
 * 
 * @see     Exception
 * @see     edu.wright.cs.carl.net.Context
 */
public class ContextException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public ContextException()
    {
        super("edu.wright.cs.carl.wart.context.ContextException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public ContextException(String message)
    {
        super(message);
    }
}
