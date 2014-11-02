
package edu.wright.cs.carl.wart.agent.controller;

import java.util.List;


/**
 * This interface defines the functionality of a self-managing queue of
 * AgentControllers.  When started, it will continually run each controller in
 * order, for the specified amount of time until there are no more controllers
 * to run.  Implementations of ControlQueue should enqure that the Agent is
 * returned to a "resting" state when not running.
 * 
 * @author  Duane Bolick
 */
public interface ControllerQueue extends java.io.Serializable
{
    public void startQueue();
    
    public void stopQueue();
    
    public void endCurrentController();
    
    public void addController(AgentController controller, String username, int timeLimitInSeconds);
    
    public void removeController(int position);
    
    public void removeAllFromUser(String username);
    
    public void moveUp(int position);
    
    public void moveDown(int position);
    
    public int getTimeLimit(int position);
    
    public void setTimeLimit(int position, int timeLimitInSeconds);
    
    public List<String> getControllerList();
}
