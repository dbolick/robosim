/**
 * TODO: shutdown behavior (also for the sim and real views.)
 */


package edu.wright.cs.carl.apps.wartapp.context.chat;

import edu.wright.cs.carl.net.message.payload.UserListUpdate;
import edu.wright.cs.carl.net.view.AbstractView;
import edu.wright.cs.carl.net.view.ViewPanel;

import edu.wright.cs.carl.net.message.payload.*;

/**
 * Implementation of a Chatroom-specific view.
 * 
 * @author  Duane Bolick
 */
public class ChatContextView extends AbstractView
{
    /**
     * Create the View.
     * 
     * @param   serverID      [in]    Supplies the server name.
     * @param   viewPanel       [in]    Supplies the ViewPanel.
     */
    public ChatContextView(String serverName, ViewPanel viewPanel)
    {
        super(serverName, viewPanel);
    }
    
    /**
     * Update the view panel.
     * 
     * @param   update  [in]    Supplies the view update.
     */
    @Override
    public void update(ContextViewUpdate update)
    {
        if(update instanceof ChatWindowUpdate) {
            ((ChatContextViewPanel) this.viewPanel).postMessage( ((ChatWindowUpdate)update).getMessage());
        }
        
        else if(update instanceof UserListUpdate) {
            ((ChatContextViewPanel) this.viewPanel).postMessage( ((UserListUpdate)update).message );
            ((ChatContextViewPanel) this.viewPanel).updateUserList(((UserListUpdate)update).username, ((UserListUpdate)update).type);
        }
    }
}
