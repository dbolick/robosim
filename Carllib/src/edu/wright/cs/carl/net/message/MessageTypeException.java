
package edu.wright.cs.carl.net.message;


/**
 * This exception class is thrown whenever MessageRecipient cannot handle the
 * Message subclass it has been sent.
 *
 * @author  Duane Bolick
 * 
 * @see     Exception
 */
public class MessageTypeException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public MessageTypeException()
    {
        super("edu.wright.cs.carl.net.MessageTypeException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public MessageTypeException(String message)
    {
        super(message);
    }
}
