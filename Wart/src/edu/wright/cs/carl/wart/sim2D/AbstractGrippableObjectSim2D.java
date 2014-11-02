
package edu.wright.cs.carl.wart.sim2D;

/**
 * Abstract implementation of the GrippableSim2D interface.
 *
 * @author  Duane Bolick
 */
public abstract class AbstractGrippableObjectSim2D extends AbstractObjectSim2D implements GrippableSim2D
{
    protected ComponentSim2D gripper;
    protected PinPositionSim2D pinPosition;
    protected AbsolutePositionSim2D newPosition;
    
    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    abstract public Object getDrawableUpdate();       
    
    public AbstractGrippableObjectSim2D(AbsolutePositionSim2D startingPosition, double xDimension, double yDimension)
    {
        super(startingPosition, xDimension, yDimension);
        
        this.gripper = null;
        
        this.pinPosition = new PinPositionSim2D();
        this.newPosition = new AbsolutePositionSim2D();        
    }
    
    /**
     * Check to see if this object is gripped.
     * 
     * @return  True if it's gripped, false otherwise.
     */
    public boolean isGripped()
    {
        if(this.gripper == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Update the position, bounding box, and drawable update to be current
     * with the base's.  This method does nothing if the object is not pinned
     * to a ComponentSim2D.
     */
    public void gripperMoved()
    {
        double absoluteAngle = 
                this.gripper.getAbsolutePosition().angleInRadians +
                this.pinPosition.angleInRadians +
                this.pinPosition.rotationInRadians;
        
        double angleFromCenter = this.gripper.getAngle() + this.pinPosition.angleInRadians;
        
        double absoluteX =
                this.gripper.getAbsolutePosition().coordinates.x +
                (this.pinPosition.distanceInPixels * (double) Math.cos(angleFromCenter));

        double absoluteY =
                this.gripper.getAbsolutePosition().coordinates.y +
                (this.pinPosition.distanceInPixels * (double) Math.sin(angleFromCenter));

        this.newPosition.angleInRadians = absoluteAngle;
        this.newPosition.coordinates.x = absoluteX;
        this.newPosition.coordinates.y = absoluteY;
        
        this.setAbsolutePosition(newPosition);        
    }
    
    /**
     * Grip this object.
     * 
     * @param   relativePosition    [in]    Supplies the relative position.
     */
    public void grip(ComponentSim2D gripper, PinPositionSim2D pinPosition)
    {
        this.gripper = gripper;
        
        this.pinPosition.angleInRadians = pinPosition.angleInRadians;
        this.pinPosition.distanceInPixels = pinPosition.distanceInPixels;
        this.pinPosition.rotationInRadians = pinPosition.rotationInRadians;
        
        this.gripperMoved();
    }
    
    /**
     * Drop this object.
     */
    public void drop()
    {
        this.gripper = null;
    }
    
    /**
     * Get the gripping component to which this object is pinned.
     * 
     * @return  A reference to the ComponentSim2D to which this object is
     *          currently pinned, or null if it is not pinned to any object.
     */
    public ComponentSim2D getGripper()
    {
        return this.gripper;
    }
    
    /**
     * Get the position relative to the gripper of this object.
     * 
     * @return  The relative position, or null if the object is not pinned to
     *          another.
     */
    public PinPositionSim2D getPinPosition()
    {
        return this.pinPosition;
    }
    
    /**
     * Set the position relative to the gripper of this object.
     * 
     * @param   newRelativePosition     [in]    Sets the new relative position.
     */
    public void setPinPosition(PinPositionSim2D newPinPosition)
    {
        this.pinPosition.angleInRadians = newPinPosition.angleInRadians;
        this.pinPosition.distanceInPixels = newPinPosition.distanceInPixels;
        this.pinPosition.rotationInRadians = newPinPosition.rotationInRadians;
        
        this.gripperMoved();        
    }
}
