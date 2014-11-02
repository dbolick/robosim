
package edu.wright.cs.carl.apps.wartapp.context.agent;

import edu.wright.cs.carl.wart.agent.controller.AgentController;


/**
 * This class represents a request from a user to control an agent.
 *
 * @author  Duane Bolick
 */
public class ControlRequest implements java.io.Serializable
{
    public String agentName;
    public AgentController controller;
    
    public ControlRequest(String agentName)
    {
        this.agentName = agentName;
    }
    
    public ControlRequest(String agentName, AgentController controller)
    {
        this.agentName = agentName;
        this.controller = controller;
    }

}
