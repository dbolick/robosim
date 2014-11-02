
package edu.wright.cs.carl.apps.wartapp.context.chat;

import edu.wright.cs.carl.net.message.payload.ContextViewUpdate;


/**
 * This update is intended to add a line to a chat window.
 *
 * @author  Duane Bolick
 */
public class ChatWindowUpdate implements ContextViewUpdate
{
    private String message;
    
    public ChatWindowUpdate(String message)
    {
        this.message = message;
    }
    
    public String getMessage()
    {
        return this.message;
    }
    
}
