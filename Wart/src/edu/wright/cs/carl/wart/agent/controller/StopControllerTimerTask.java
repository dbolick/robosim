
package edu.wright.cs.carl.wart.agent.controller;

import edu.wright.cs.carl.wart.agent.controller.DefaultControllerQueue;
import java.util.TimerTask;


/**
 * Stops the currently-running AgentController.
 *
 * @author  Duane Bolick
 */
public class StopControllerTimerTask extends TimerTask
{
    private DefaultControllerQueue queue;
    
    /**
     * Construct the timer task.
     * 
     * @param   queue   [in]    Provides a reference to the queue - this timer
     *                          task requires this reference to stop the
     *                          controller.
     */
    public StopControllerTimerTask(DefaultControllerQueue queue)
    {
        this.queue = queue;
    }
    
    public void run()
    {
        this.queue.endCurrentController();
    }
}
