
package edu.wright.cs.carl.apps.wartapp.context.agent;

import java.util.List;

import java.security.Principal;

import edu.wright.cs.carl.net.server.ServerException;
import edu.wright.cs.carl.net.handle.ClientHandle;

import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.wart.agent.Agent;

import edu.wright.cs.carl.wart.agent.controller.AgentController;
import edu.wright.cs.carl.wart.agent.controller.AgentControllerException;

/**
 * This interface provides interaction with a Context used for control of
 * Agents.
 * 
 * @author  Duane Bolick
 */
public interface AgentContextInterface
{
    /**
     * Get a list of Agents by name.
     * 
     * @return  A list of Agent names.
     */
    public List<String> getAgentList();
    
    /**
     * Get a reference to an Agent by name.
     * 
     * @param   agentName   [in]    Supplies the name.
     * 
     * @return  The Agent.
     */
    public Agent getAgent(String agentName);
    
    /**
     * Add an Agent to the context.
     * 
     * @param   caller      [in]    Supplies the caller.
     * @param   newAgent    [in]    Supplies the Agent.
     * 
     * @throws  ServerException if the Agent already exists.
     * @throws  PermissionException if the caller lacks permission.
     */
    public void addAgent(Principal caller, Agent newAgent) throws ServerException, PermissionException;
    
    /**
     * Remove an Agent from the context.
     * 
     * @param   caller      [in]    Supplies the caller.
     * @param   agentname   [in]    Supplies the Agent name.
     * 
     * @throws  ServerException if the Agent does not exist.
     * @throws  PermissionException if the caller lacks permission.
     */    
    public void removeAgent(Principal caller, String agentname) throws ServerException, PermissionException;
    
    /**
     * Add a controller to an Agent's queue.
     * 
     * @param   caller      [in]    Supplies the caller (the owner of the
     *                              controller.
     * @param   agentName   [in]    Supplies the name of the Agent.
     * @param   controller  [in]    Supplies the controller.
     * 
     * @throws  AgentControllerException if the controller is not compatible.
     * @throws  ServerException if the Agent doesn't exist.
     * @throws  PermissionException if the caller lacks permission.
     */
    public void addControllerToQueue(ClientHandle caller, String agentName, AgentController controller) throws AgentControllerException, ServerException, PermissionException;
}
