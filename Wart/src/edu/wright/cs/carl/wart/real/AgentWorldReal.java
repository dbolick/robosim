
package edu.wright.cs.carl.wart.real;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wright.cs.carl.net.message.payload.ContextViewUpdate;

import edu.wright.cs.carl.wart.agent.Agent;
import edu.wright.cs.carl.wart.agent.AgentWorld;


/**
 * A "world" of real, actual agents.  The view updates are of a webcam or
 * something like that.
 * 
 * @author  Duane Bolick
 */
public class AgentWorldReal implements AgentWorld
{
    Map<String, Agent> agents;
    
    private boolean isReady = false;
    
    public AgentWorldReal()
    {
        this.agents = new HashMap<String, Agent>();
    }
    
    /**
     * See if the world is ready.
     * 
     * @return  True if ready false if not ready.
     */
    public boolean isReady()
    {
        return this.isReady;
    }
    
    /**
     * Set the ready status of this world.
     * 
     * @param   isReady [in]    Supplies if the world is ready.
     */
    public void setIsReady(boolean isReady)
    {
        this.isReady = isReady;
    }
    
    public void start()
    {
        
    }
    
    public void stop()
    {
        
    }
    
    public void addAgent(Agent newAgent)
    {
        if(this.agents.containsKey(newAgent.getName())) {            
            throw new AssertionError("AgentWorldReal.addAgent: There is already an agent with this ID.");
        }
        
        this.agents.put(newAgent.getName(), newAgent);
    }
    
    public Agent removeAgent(String agentName)
    {
        return this.agents.remove(agentName);
    }
    
    /**
     * Get the agents in the world.
     * 
     * @return  The agents.
     */
    public List<Agent> getAgents()
    {
        List<Agent> agentList = new ArrayList<Agent>();
        
        Iterator<Agent> it = this.agents.values().iterator();
        while(it.hasNext()) {
            agentList.add(it.next());
            
        }
        
        return agentList;
    }
    
    /**
     * Get the initial view update.
     * 
     * @return  The initial view update.
     */
    public ContextViewUpdate getInitialViewSync()
    {
        return null;
    }
    
    /**
     * Get the update for the visuallzation of the world.
     * 
     * @return  The update.
     */
    public ContextViewUpdate getViewUpdate()
    {
        return null;
    }
}
