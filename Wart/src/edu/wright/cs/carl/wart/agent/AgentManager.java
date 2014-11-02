
package edu.wright.cs.carl.wart.agent;

import java.util.List;

import edu.wright.cs.carl.net.server.ServerException;


/**
 * This interface provides read/write access to a collection of Agent objects.
 * It is responsible for enforcing uniqueness of Agent names.
 * 
 * @author  Duane Bolick
 * 
 * @see     Agent
 */
public interface AgentManager
{
    /**
     * Add an agent.
     * 
     * @param   newAgent   [in]    Supplies a reference to an Agent.
     * 
     * @throws  ServerException if the Agent already exists.
     */
    public void addAgent(Agent newAgent) throws ServerException;
  
  
    /**
     * Remove an agent.
     * 
     * @param   agent   [in]    Supplies the name of the Agent to remove.
     * 
     * @throws  ServerException if the Agent does not exist.
     */
    public void removeAgent(String agentname) throws ServerException;

    
    /**
     * Get a reference to an Agent by its agentname.
     * 
     * @param     agentname     [in]    supplies the name associated with an
     *                                  Agent.
     * 
     * @return    a reference to the Agent with the given name, or null if
     *            that Agent doesn't exist.
     */
    public Agent getAgentByName(String agentname);

    
    /**
     * Get a list of names of all Agents.
     * 
     * @return  A List of agent names.
     */
    public List<String> getAgentList();
}
