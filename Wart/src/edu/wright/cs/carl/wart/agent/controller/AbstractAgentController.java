
package edu.wright.cs.carl.wart.agent.controller;

import edu.wright.cs.carl.wart.agent.controller.AgentControllerException;
import edu.wright.cs.carl.wart.agent.controller.AgentController;
import edu.wright.cs.carl.wart.agent.Agent;


/**
 * 
 *
 * @author  Duane Bolick
 */
public abstract class AbstractAgentController implements AgentController
{
    public static long AgentThreadSleepTimeInMillis = 40;

    protected boolean stopRequested = false;
    protected boolean isRunning = false;    
    protected Agent agent;
    
    /**
     * Set the Agent to be controlled.
     * 
     * @param   agent   [in]    Suppplies the Agent to be controlled.
     * 
     * @throws  AgentControllerException if the Agent is not compatible.
     */
    public void setAgent(Agent agent) throws AgentControllerException
    {
        this.agent = agent;        
    }

    //
    // TODO:  Do I really need start and stop methods for controllers?
    // I mean, they're just simple runnables.  If I have them, they should
    // be used...  OK, I know - stop() causes a natural death, but instead
    // in DefaultControllerQueue, the controller thread has its own
    // request stop boolean, etc.  That is totally redundant...  Fix this.
    //
    /**
     * Start the controller.
     * 
     * @throws  AgentControllerException if it has not been provided an
     *          AgentControlInterface.
     */
    public void start() throws AgentControllerException
    {
        if(this.agent == null) {
            throw new AgentControllerException("AbstractAgentController.start:  No agent has been provided.");
        }
        this.stopRequested = false;
        this.isRunning = true;
    }
    
    /**
     * Stop the controller.
     */
    public void stop()
    {
        this.stopRequested = true;
        this.isRunning = false;
    }
    
    /**
     * Check to see if the controller is running.
     * 
     * @return  True if the controller is running, false otherwise.
     */
    public boolean isRunning()
    {
        return this.isRunning;
    }
    
    public void run()
    {
        this.initialize();
        try { 
            this.start();
        }
        catch(AgentControllerException e) {
            e.printStackTrace();
            this.stopRequested = true;
        }
        while(this.stopRequested == false) {
           
            this.doWork();
            try {
                Thread.sleep(AgentThreadSleepTimeInMillis);
            }
            catch(InterruptedException e) {
                
            }
        }
    }
    
    public String getControllerName()
    {
        return this.getClass().getSimpleName();
    }
    
    public abstract void initialize();
    public abstract void doWork();
}
