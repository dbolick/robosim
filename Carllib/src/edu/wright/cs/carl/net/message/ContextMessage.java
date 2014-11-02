
package edu.wright.cs.carl.net.message;


/**
 * This message type represents messages from a Context.
 *
 * @author  Duane Bolick
 */
public class ContextMessage implements Message
{
    public String contextName;
    public String contextID;
    public Object payload;
    
    public ContextMessage(){}
    
    public ContextMessage(String contextName, String contextID)
    {
        this.contextName = contextName;
        this.contextID = contextID;
    }
    
    public ContextMessage(String contextName, String contextID, Object payload)
    {
        this.contextName = contextName;
        this.contextID = contextID;
        this.payload = payload;
    }
}
