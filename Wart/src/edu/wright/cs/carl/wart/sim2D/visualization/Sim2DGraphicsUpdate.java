
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.util.HashMap;
import java.util.Map;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsUpdate;


/**
 * This is the "payload" object for a <i>ViewUpdateMessage</i> sent to a
 * <i>SimAgentContextView</i> 
 *
 * @author  Duane Bolick
 */
public class Sim2DGraphicsUpdate implements SimGraphicsUpdate
{
    /**
     * Contains updates for all object.  The key is the ID of the object, and
     * the value is the update.
     */
    public Map<String, Object> updates;
    
    public Sim2DGraphicsUpdate()
    {
        this.updates = new HashMap<String, Object>();
    }
}
