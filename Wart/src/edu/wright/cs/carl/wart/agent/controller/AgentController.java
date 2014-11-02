
package edu.wright.cs.carl.wart.agent.controller;

import edu.wright.cs.carl.wart.agent.Agent;

/**
 * An <i>AgentController</i> represents a control algorithm for a specific
 * type of <i>Agent</i>.
 * 
 * @author  Duane Bolick
 */
public interface AgentController extends Runnable, java.io.Serializable
{    
    /**
     * Set the Agent to be controlled.
     * 
     * @param   agent   [in]    Suppplies the Agent to be controlled.
     * 
     * @throws  AgentControllerException if the Agent is not compatible.
     */
    public void setAgent(Agent agent) throws AgentControllerException;
    
    /**
     * Start the controller.
     * 
     * @throws  AgentControllerException if it has not been provided a
     *          compatible Agent to control.
     */
    public void start() throws AgentControllerException;
    
    /**
     * Stop the controller.
     */
    public void stop();
    
    /**
     * Check to see if the controller is running.
     * 
     * @return  True if the controller is running, false otherwise.
     */
    public boolean isRunning();
    
    /**
     * Get the name of the controller.
     * 
     * @return The name of the controller.
     */
    public String getControllerName();
    
    /**
     * Get a description of what the controller is intended to do.
     * 
     * @return The controller description.
     */
    public String getControllerDescription();
        
}
