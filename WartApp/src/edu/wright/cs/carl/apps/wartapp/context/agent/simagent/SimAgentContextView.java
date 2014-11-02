/**
 * TODO: Shutdown behavior.
 */

package edu.wright.cs.carl.apps.wartapp.context.agent.simagent;

import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.view.AbstractView;
import edu.wright.cs.carl.net.view.ViewPanel;

import edu.wright.cs.carl.apps.wartapp.context.chat.ChatWindowUpdate;
import edu.wright.cs.carl.net.message.payload.UserListUpdate;
import edu.wright.cs.carl.apps.wartapp.context.agent.AgentStatusUpdate;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsUpdate;


/**
 * Implementation of a Simulated Agent arena-specific view.
 * 
 * @author  Duane Bolick
 */
public class SimAgentContextView extends AbstractView
{
    /**
     * Create the View.
     * 
     * @param   serverID      [in]    Supplies the server name.
     * @param   viewPanel       [in]    Supplies the ViewPanel.
     */
    public SimAgentContextView(String serverName, ViewPanel viewPanel)
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
        if(update instanceof SimGraphicsUpdate) {
            ((SimAgentContextViewPanel)this.viewPanel).updateSimVisualization((SimGraphicsUpdate) update);
            return;
        }
        
        if(update instanceof ChatWindowUpdate) {
            ((SimAgentContextViewPanel) this.viewPanel).postMessage( ((ChatWindowUpdate)update).getMessage() );
        }
        
        else if(update instanceof UserListUpdate) {
            ((SimAgentContextViewPanel) this.viewPanel).postMessage( ((UserListUpdate)update).message );
            ((SimAgentContextViewPanel) this.viewPanel).updateUserList(((UserListUpdate)update).username, ((UserListUpdate)update).type);
        }
        
        else if(update instanceof AgentStatusUpdate) {
            ((SimAgentContextViewPanel) this.viewPanel).updateAgentStatus((AgentStatusUpdate)update);
        }
    }    
}
