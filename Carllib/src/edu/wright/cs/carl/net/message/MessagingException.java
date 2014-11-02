/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.wright.cs.carl.net.message;

/**
 * 
 *
 * @author Duane
 */
public class MessagingException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public MessagingException()
    {
        super("edu.wright.cs.carl.net.MessagingException");
    }
    
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public MessagingException(String message)
    {
        super(message);
    }
}
