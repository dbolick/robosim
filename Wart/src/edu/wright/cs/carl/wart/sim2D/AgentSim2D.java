
package edu.wright.cs.carl.wart.sim2D;

import java.util.Map;

import edu.wright.cs.carl.wart.agent.Agent;

/**
 * 
 * 
 * @author  Duane Bolick
 */
public interface AgentSim2D extends Agent, ObjectSim2D, ActiveSim2D
{
    /**
     * Attach an object to this one.
     * 
     * @param   base        [in]    Supplies the object which will be pinned to
     *                              this one.
     * @param   position    [in]    Supplies the position, relative to the
     *                              absolute position of this object, where the
     *                              pinned object will be.
     */
    public void addComponent(ComponentSim2D component, PinPositionSim2D position);

    /**
     * Get the objects that are pinned to this object.  The returned Map
     * contains the unique IDs of the pinned objects as keys and references to
     * the objects as values.
     * 
     * @return  A Map of the objects pinned to this object.
     */
    public Map<String, ComponentSim2D> getComponents();    
}
