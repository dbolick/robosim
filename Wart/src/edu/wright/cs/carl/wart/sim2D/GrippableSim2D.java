
package edu.wright.cs.carl.wart.sim2D;


/**
 * This interface should be implemented by any object that can be held by a
 * Khepera gripper turret.
 * 
 * @author  Duane Bolick
 */
public interface GrippableSim2D
{
    /**
     * Check to see if this object is gripped.
     * 
     * @return  True if it's gripped, false otherwise.
     */
    public boolean isGripped();
    
    /**
     * Update the position, bounding box, and drawable update to be current
     * with the base's.  This method does nothing if the object is not pinned
     * to a ComponentSim2D.
     */
    public void gripperMoved();
    
    /**
     * Grip this object.
     * 
     * @param   relativePosition    [in]    Supplies the relative position.
     */
    public void grip(ComponentSim2D gripper, PinPositionSim2D relativePosition);
    
    /**
     * Drop this object.
     */
    public void drop();
    
    /**
     * Get the base object to which this object is ultimately pinned.
     * 
     * @return  A reference to the ComponentSim2D to which this object is
     *          currently pinned, or null if it is not pinned to any object.
     */
    public ComponentSim2D getGripper(); 
    
    /**
     * Get the position relative to the gripper of this object.
     * 
     * @return  The relative position, or null if the object is not pinned to
     *          another.
     */
    public PinPositionSim2D getPinPosition();     
    
    /**
     * Set the position relative to the gripper of this object.
     * 
     * @param   newRelativePosition     [in]    Sets the new relative position.
     */
    public void setPinPosition(PinPositionSim2D newRelativePosition);          
}
