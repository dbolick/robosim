
package edu.wright.cs.carl.wart.sim2D;


/**
 * This interface should be implemented by all of the individual agent
 * components.  An agent is composed of a nummber of components that give it
 * shape, mobility, and sensing and effecting capabilities.  Components are
 * "pinned" to an agent, and move as one, rigidly-attached shape.
 * 
 * @author  Duane Bolick
 */
public interface ComponentSim2D extends ObjectSim2D, ActiveSim2D
{
    /**
     * Update the position, bounding box, and drawable update to be current
     * with the base's.  This method does nothing if the object is not pinned
     * to a BaseObjectSim2D.
     */
    public void agentMoved();
    
    /**
     * Pin this object to a base.  This method should update the object's
     * absolute position, its bounding area, and its drawingUpdate.
     * 
     * @param   relativePosition    [in]    Supplies the relative position.
     */
    public void pinToAgent(AgentSim2D base, PinPositionSim2D relativePosition);
    
    /**
     * Get the base object to which this object is pinned.
     * 
     * @return  A reference to the base ObjectSim2D to which this object is
     *          currently pinned, or null if it is not pinned to any object.
     */
    public AgentSim2D getAgent(); 
    
    /**
     * Get the position relative to the base of this object.
     * 
     * @return  The relative position, or null if the object is not pinned to
     *          another.
     */
    public PinPositionSim2D getPinPosition();     
    
    /**
     * Set the position relative to the base of this object.
     * 
     * @param   newRelativePosition     [in]    Sets the new relative position.
     */
    public void setPinPosition(PinPositionSim2D newRelativePosition);      
}
