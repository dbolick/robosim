/**
 * TODO: Shutdown behavior
 */

package edu.wright.cs.carl.apps.wartapp.context.agent.realagent;

import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.view.AbstractView;
import edu.wright.cs.carl.net.view.ViewPanel;

import edu.wright.cs.carl.apps.wartapp.context.chat.ChatWindowUpdate;
import edu.wright.cs.carl.net.message.payload.UserListUpdate;
import edu.wright.cs.carl.apps.wartapp.context.agent.AgentStatusUpdate;


/**
 * Implementation of a real Agent arena-specific view.
 * 
 * @author  Duane Bolick
 */
public class RealAgentContextView extends AbstractView
{
    /**
     * Create the View.
     * 
     * @param   serverID      [in]    Supplies the server name.
     * @param   viewPanel       [in]    Supplies the ViewPanel.
     */
    public RealAgentContextView(String serverName, ViewPanel viewPanel)
    {
        super(serverName, viewPanel);
    }
    
    /**
     * Update the view panel.
     * 
     * @param   update  [in]    Supplies the view update.
     */
    public void update(ContextViewUpdate update)
    {
        if(update == null) {
            return;
        }

       if(update instanceof ChatWindowUpdate) {
            ((RealAgentContextViewPanel) this.viewPanel).postMessage( ((ChatWindowUpdate)update).getMessage() );
        }
        
        else if(update instanceof UserListUpdate) {
            ((RealAgentContextViewPanel) this.viewPanel).postMessage( ((UserListUpdate)update).message );
            ((RealAgentContextViewPanel) this.viewPanel).updateUserList(((UserListUpdate)update).username, ((UserListUpdate)update).type);
        }
        
        else if(update instanceof AgentStatusUpdate) {
            ((RealAgentContextViewPanel) this.viewPanel).updateAgentStatus((AgentStatusUpdate)update);
        }
    }  
}
