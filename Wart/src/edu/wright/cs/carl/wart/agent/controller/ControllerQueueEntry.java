
package edu.wright.cs.carl.wart.agent.controller;


/**
 * This class repressents entries in a Controller queue.
 *
 * @author  Duane Bolick
 */
public class ControllerQueueEntry implements java.io.Serializable
{
    private AgentController controller;
    private String username;
    private Integer timeLimitInSeconds;
    
    /**
     * Construct a queue entry.
     * 
     * @param   controller          [in]    Supplies a reference to a
     *                                      controller.
     * @param   username            [in]    Supplies the username of the client
     *                                      requesting control.
     * @param   timeLimitInSeconds  [in]    Supplies the time limit of this
     *                                      controller's run in seconds.
     */
    public ControllerQueueEntry(AgentController controller, String username, Integer timeLimitInSeconds)
    {
        this.controller = controller;
        this.username = username;
        this.timeLimitInSeconds = timeLimitInSeconds;
    }
    
    /**
     * Get the controller contained in this entry.
     * 
     * @return  The AgentController
     */
    public AgentController getController()
    {
        return this.controller;
    }
    
    /**
     * Get the username associated with this entry.
     * 
     * @return  The username
     */
    public String getUsername()
    {
        return this.username;
    }
    
    /**
     * Get the time limit of this entry.
     * 
     * @return  The time limit.
     */
    public Integer getTimeLimit()
    {
        return this.timeLimitInSeconds;
    }
    
    /**
     * Change the time limit of this entry.
     * 
     * @param   timeLimit   [in]    Supplies the new time limit in seconds.
     */
    public void setTimeLimit(Integer timeLimit)
    {
        this.timeLimitInSeconds = timeLimit;
    }
}
