
package edu.wright.cs.carl.wart.sim2D;

import java.util.Map;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;

/**
 * Abstract implementation of ComponentSim2D.
 *
 * @author  Duane Bolick
 */
public abstract class AbstractComponentSim2D extends AbstractObjectSim2D implements ComponentSim2D
{
    protected PinPositionSim2D pinPosition;
    protected AbsolutePositionSim2D newPosition;
    
    protected AgentSim2D agent;
    
    /**
     * Update the state of this object.  An active object updates itself once
     * per world-update cycle.  Since world-update cycles occur a specified
     * number of times per second, we know the duration of a single update cycle
     * in milliseconds.  Implementations of ActiveSim2D should ensure that all
     * movement/state changes/etc. are, if applicable, executed based on the
     * provided duration.
     * 
     * @param   objects     [in]    Provides a map of the other objects in the
     *                              world.
     * @param timeInMillis  [in]    Supplies the duration of this update cycle
     *                              in milliseconds.
     */
    abstract public void update(Map<String, ObjectSim2D> objects,long timeInMillis);    
    
    /**
     * Abstract constructor.
     * 
     * @param   objectID    [in]    Supplies the object ID.
     */
    public AbstractComponentSim2D(AbsolutePositionSim2D startingPosition, double xDimension, double yDimension)
    {
        super(startingPosition, xDimension, yDimension);
        
        this.pinPosition = new PinPositionSim2D();
        this.newPosition = new AbsolutePositionSim2D();
    }
    
    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    public DrawableObjectSim2D getDrawable()
    {
        return null;
    }
    
    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    public Object getDrawableUpdate()
    {
        return null;
    }    
    
    /**
     * Update the position, bounding box, and drawable update to be current
     * with the base's.  This method does nothing if the object is not pinned
     * to a BaseObjectSim2D.
     */
    public void agentMoved()
    {
        //double absoluteAngle = this.agent.getAbsolutePosition().angleInRadians + this.pinPosition.angleInRadians + this.pinPosition.rotationInRadians;
        
        double absoluteAngle = this.agent.getAngle() + this.pinPosition.rotationInRadians;
        
        double angleFromCenter = this.agent.getAngle() + this.pinPosition.angleInRadians;
        
        double absoluteX = this.agent.getAbsolutePosition().coordinates.x + (this.pinPosition.distanceInPixels * (double) Math.cos(angleFromCenter));
        double absoluteY = this.agent.getAbsolutePosition().coordinates.y + (this.pinPosition.distanceInPixels * (double) Math.sin(angleFromCenter));
                
        this.newPosition.angleInRadians = absoluteAngle;
        this.newPosition.coordinates.x = absoluteX;
        this.newPosition.coordinates.y = absoluteY;
        
        this.setAbsolutePosition(newPosition);
    }
    
    /**
     * Pin this object to a base.  This method should update the object's
     * absolute position, its bounding area, and its drawingUpdate.
     * 
     * @param   newPinPosition    [in]    Supplies the relative position.
     */
    public void pinToAgent(AgentSim2D agent, PinPositionSim2D pinPosition)
    {
        this.agent = agent;
        
        this.pinPosition.angleInRadians = pinPosition.angleInRadians;
        this.pinPosition.distanceInPixels = pinPosition.distanceInPixels;
        this.pinPosition.rotationInRadians = pinPosition.rotationInRadians;
        
        this.agentMoved();
    }
    
    /**
     * Get the base object to which this object is pinned.
     * 
     * @return  A reference to the base ObjectSim2D to which this object is
     *          currently pinned, or null if it is not pinned to any object.
     */
    public AgentSim2D getAgent()
    {
        return this.agent;
    }
    
    /**
     * Get the position relative to the base of this object.
     * 
     * @return  The relative position, or null if the object is not pinned to
     *          another.
     */
    public PinPositionSim2D getPinPosition()
    {
        return this.pinPosition;
    }
    
    /**
     * Set the position relative to the base of this object.
     * 
     * @param   newRelativePosition     [in]    Sets the new relative position.
     */
    public void setPinPosition(PinPositionSim2D newPinPosition)
    {
        this.pinPosition.angleInRadians = newPinPosition.angleInRadians;
        this.pinPosition.distanceInPixels = newPinPosition.distanceInPixels;
        this.pinPosition.rotationInRadians = newPinPosition.rotationInRadians;
        
        this.agentMoved();
    }
    
}
