
package edu.wright.cs.carl.wart.agent.controller;


/**
 * This exception class is thrown in the event of unexpected conditions in an
 * AgentController.
 *
 * @author  Duane Bolick
 * 
 * @see     Exception
 * @see     AgentController
 * @see     AgentControlInterface
 */
public class AgentControllerException extends Exception
{
    /**
     * No-argument constructor.  Calls the super-class constructor.
     */
    public AgentControllerException()
    {
        super("edu.wright.cs.carl.wart.controller.AgentControllerException");
    }
    
    /**
     * This constructor calls the parent classes' constructor that receives a
     * String argument.  The provided message is passed to that constructor.
     * 
     * @param   message [in]    Supplies the exception message.
     */
    public AgentControllerException(String message)
    {
        super(message);
    }
}
