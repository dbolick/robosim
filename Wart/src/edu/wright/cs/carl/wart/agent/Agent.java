
package edu.wright.cs.carl.wart.agent;

import java.io.Serializable;

/**
 * Defines the basic methods required by all Agent implementations.
 * 
 * @author  Duane Bolick
 */
public interface Agent extends Serializable
{
    /**
     * Get the agent's name.  Agent name is used for identification purposes,
     * and should be unique (one unique name per entity) in a given context.
     * 
     * @return  The agent's name.
     */
    public String getName();
    
    /**
     * Set the name of this agent.
     * 
     * @param   newName     [in]    Supplies the new name. 
     */
    public void setName(String newName);
    
    /**
     * Get a text description of the agent.  This should, but doesn't have to,
     * include the components and capabilities of the Agent, including the
     * available control interfaces.
     * 
     * @return  A description of the Agent.
     */
    public String getDescription();
    
    /**
     * Set the Agent to a resting state.  This state should probably be a
     * non-moving, low-power state that can be sustained without causing damage
     * to the Agent.
     */
    public void rest();
}
