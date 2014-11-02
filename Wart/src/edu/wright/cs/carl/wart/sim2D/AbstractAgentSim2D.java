
package edu.wright.cs.carl.wart.sim2D;

import java.awt.geom.Rectangle2D;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Abstract implementation of AgentSim2D.
 *
 * @author  Duane Bolick
 */
public abstract class AbstractAgentSim2D extends AbstractObjectSim2D implements AgentSim2D
{
    protected Map<String, ComponentSim2D> components;

    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    abstract public Object getDrawableUpdate();   
    
    
    public AbstractAgentSim2D(AbsolutePositionSim2D startingPosition)
    {
        super(startingPosition, 0, 0);
        
        this.components = new HashMap<String, ComponentSim2D>();
    }
    
    /**
     * Overridden methods (from AbstractObjectSim2D)
     */

    /**
     * Returns the bounding box for the object.
     * 
     * @return  The bounding box.
     */
    @Override
    public Rectangle2D getBoundingBox()
    {
        this.boundingBox.setRect(   this.position.coordinates.x,
                                    this.position.coordinates.y,
                                    0,
                                    0);
        
        Iterator<ComponentSim2D> it = this.components.values().iterator();
        Rectangle2D currentBox = null;
        while(it.hasNext()) {
            currentBox = it.next().getBoundingBox();
            if(currentBox != null) {
                this.boundingBox.add(currentBox);
            }
        }
        return this.boundingBox;
    }    
    
    /**
     * Check to see if the provided object collides with this object.  A
     * collision implies that two objects are occupying the same area, and
     * they should not be.  This is not necessarily semantically equivalent to
     * "intersects with bounding box of."  If you want to test bounding box
     * intersection, then use the getBoundingBox() methods of the objects in
     * question, and use the Rectangle2D methods to determine intersection.
     * 
     * @param   object      [in]    Supplies the object.
     * 
     * @return  True if the provided object collides with this one.
     */
    @Override
    public boolean collidesWithObject(ObjectSim2D object)
    {
        Iterator<ComponentSim2D> it = this.components.values().iterator();
        while(it.hasNext()) {
            if(it.next().collidesWithObject(object)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Translate this object in user-space.  The provided changes in x and y
     * coordinates may be negative.
     * 
     * @param   dx  [in]    Supplies the number of pixels to translate this
     *                      object along the x-axis.
     * @param   dy  [in]    Supplies the number of pixels to translate this
     *                      object along the y-axis.
     */
    @Override
    public void translate(double dx, double dy)
    {
        super.translate(dx, dy);
        this.moveComponents();
    }
    
    /**
     * Rotate this object's center point.
     * 
     * @param   rotationInRadians   [in]    Supplies the rotation in radians.
     */
    @Override
    public void rotate(double rotationInRadians)
    {
        super.rotate(rotationInRadians);
        this.moveComponents();
    }
    
    
    /**
     * AgentSim2D methods.
     */
    
    protected void moveComponents()
    {
        Iterator<ComponentSim2D> it = this.components.values().iterator();
        while(it.hasNext()) {
            it.next().agentMoved();
        }        
    }    
    
    /**
     * Attach an object to this one.
     * 
     * @param   base        [in]    Supplies the object which will be pinned to
     *                              this one.
     * @param   position    [in]    Supplies the position, relative to the
     *                              absolute position of this object, where the
     *                              pinned object will be.
     */
    public void addComponent(ComponentSim2D component, PinPositionSim2D position)
    {
        this.components.put(component.getID(), component);
        component.pinToAgent(this, position);
        component.agentMoved();
    }
    
    /**
     * Get the objects that are pinned to this object.  The returned Map
     * contains the unique IDs of the pinned objects as keys and references to
     * the objects as values.
     * 
     * @return  A Map of the objects pinned to this object.
     */
    public Map<String, ComponentSim2D> getComponents()
    {
        return this.components;
    }
    
}
