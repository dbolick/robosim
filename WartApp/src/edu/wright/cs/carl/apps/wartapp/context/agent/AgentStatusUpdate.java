
package edu.wright.cs.carl.apps.wartapp.context.agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wright.cs.carl.net.message.payload.ContextViewUpdate;


/**
 * This class is the ViewUpdateMessage payload for status updates regarding
 * agents and their control queues.
 *
 * @author  Duane Bolick
 */
public class AgentStatusUpdate implements ContextViewUpdate
{
    public Map<String, List<String>> controllerQueues;

    public AgentStatusUpdate()
    {
        this.controllerQueues = new HashMap<String, List<String>>();
    }
}
