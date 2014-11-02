
package edu.wright.cs.carl.wart.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.wart.agent.controller.ControllerQueue;
//import edu.wright.cs.carl.wart.agent.controller.DefaultControllerQueue;


/**
 * Default implementation of a ControlQueuedAgentManager.
 *
 * @author  Duane Bolick
 * 
 * @see     AgentManager
 * @see     AgentControl
 */
public class DefaultAgentManager implements ControlQueuedAgentManager
{
    private Map<String, Agent> agents;
    //private Map<String, ControllerQueue> controllerQueues;
    
    /**
     * Constructor.
     */
    public DefaultAgentManager()
    {
        this.agents = new ConcurrentHashMap<String, Agent>();
        //this.controllerQueues = new ConcurrentHashMap<String, ControllerQueue>();
    }
    
    
    /**
     * Add an agent.
     * 
     * @param   newAgent   [in]    Supplies a reference to an AgentControl.
     * 
     * @throws  ServerException if the AgentControl already exists.
     */
    public synchronized void addAgent(Agent newAgent) throws ServerException
    {
        String newAgentname = newAgent.getName();
        
        if(this.agents.containsKey(newAgentname) == true){
            throw new ServerException("DefaultAgentManager.addAgent: An Agent named " + newAgentname + " already exists.");
        }
        
        this.agents.put(newAgentname, newAgent);
        //this.controllerQueues.put(newAgentname, new DefaultControllerQueue(newAgent));
    }
  
    /**
     * Remove an agent.
     * 
     * @param   agent   [in]    Supplies a reference to an AgentControl to remove.
     * 
     * @throws  ServerException if the AgentControl does not exist.
     */
    public synchronized void removeAgent(String agentname) throws ServerException
    {
        if(this.agents.containsKey(agentname) == true){
            throw new ServerException("DefaultAgentManager.removeAgent: An Agent named " + agentname + " does not exist.");
        }
        
        this.agents.remove(agentname);
        //this.controllerQueues.remove(agentname);
    }
    
    /**
     * Get a reference to an AgentControl by its agentname.
     * 
     * @param     agentname     [in]    supplies the name associated with an
     *                                  AgentControl.
     * 
     * @return    a reference to the AgentControl with the given name, or null if
     *            that AgentControl doesn't exist.
     */
    public synchronized Agent getAgentByName(String agentname)
    {
        return this.agents.get(agentname);
    }
    
    /**
     * Get a list of names and descriptions of all Agents.
     * 
     * @return  A Map containing pairs of Strings.  The keys are the AgentControl
     *          names and the values are the AgentControl descriptions.
     */
    public synchronized List<String> getAgentList()
    {
        return new ArrayList<String>(this.agents.keySet());
    }

    /**
     * Get an Agent's ControllerQueue.
     * 
     * @param   agentName   [in]    Supplies the agent name.
     * 
     * @return  A reference to that Agent's ControllerQueue, or null if the
     *          agent does not exist.
     */
    public ControllerQueue getControllerQueue(String agentName)
    {
        return null;
        //return this.controllerQueues.get(agentName);
    }
}
