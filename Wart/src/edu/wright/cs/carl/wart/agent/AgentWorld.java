
package edu.wright.cs.carl.wart.agent;

import java.io.Serializable;

import java.util.List;

import edu.wright.cs.carl.net.message.payload.ContextViewUpdate;


/**
 * Defines the common interface of an AgentWorld.  A "world" handles all the
 * states of all its objects, controller threads, agents, etc.  The way that
 * the application interacts with a "world" is to simply ask for a view update
 * every so often.  Exactly what that view update consists of is up to the
 * actual context type, of course.
 * 
 * @author  Duane Bolick
 */
public interface AgentWorld extends Serializable
{
    /**
     * See if the world is ready.
     * 
     * @return  True if ready false if not ready.
     */
    public boolean isReady();
    
    /**
     * Set the ready status of this world.
     * 
     * @param   isReady [in]    Supplies if the world is ready.
     */
    public void setIsReady(boolean isReady);    
    
    /**
     * Start the world.
     */
    public void start();
    
    /**
     * Stop the world.
     */
    public void stop();
    
    /**
     * Get the agents in the world.
     * 
     * @return  The agents.
     */
    public List<Agent> getAgents();
    
    /**
     * Get the initial view update.
     * 
     * @return  The initial view update.
     */
    public ContextViewUpdate getInitialViewSync();
    
    /**
     * Get the update for the visuallzation of the world.
     * 
     * @return  The update.
     */
    public ContextViewUpdate getViewUpdate();        
}
