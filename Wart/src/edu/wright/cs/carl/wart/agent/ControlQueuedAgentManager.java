
package edu.wright.cs.carl.wart.agent;

import edu.wright.cs.carl.wart.agent.AgentManager;
import edu.wright.cs.carl.wart.agent.controller.ControllerQueue;

/**
 * This variety of AgentManager adds the ability to manage queues of
 * AgentControllers for each Agent using manual control of the queues, as
 * well as automatic time limits on how long each controller may run.  All of
 * this functionality is provided through the ControllerQueue interface.
 * 
 * @author  Duane Bolick
 * 
 * @see     ControllerQueue
 */
public interface ControlQueuedAgentManager extends AgentManager
{
    /**
     * Get an Agent's ControllerQueue.
     * 
     * @param   agentName   [in]    Supplies the agent name.
     * 
     * @return  A reference to that Agent's ControllerQueue, or null if the
     *          agent does not exist.
     */
    public ControllerQueue getControllerQueue(String agentName);
}
