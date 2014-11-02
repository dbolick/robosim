
package edu.wright.cs.carl.wart.sim2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;


/**
 * Abstract class that implements methods and data structures common to all
 * 2D sim world objects.  Classes that extend <i>AbstractObjectSim2D</i>.
 * 
 * @author  Duane Bolick
 */
public abstract class AbstractObjectSim2D implements ObjectSim2D
{
    protected String objectID;
    protected AbsolutePositionSim2D position;      
    
    protected double xDimension;
    protected double yDimension;
    
    protected Rectangle2D boundingBox;
    
    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    abstract public DrawableObjectSim2D getDrawable();
    
    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    abstract public Object getDrawableUpdate();     
    
    /**
     * Create a 2D sim-world object.
     * 
     * @param   objectID            [in]    Supplies the ID
     * @param   startingPosition    [in]    Supplies the starting position.
     */
    public AbstractObjectSim2D( AbsolutePositionSim2D startingPosition,
                                double xDimension,
                                double yDimension)
    {
        this.position = new AbsolutePositionSim2D();
        this.position.angleInRadians = startingPosition.angleInRadians;
        this.position.coordinates.x = startingPosition.coordinates.x;
        this.position.coordinates.y = startingPosition.coordinates.y;
        
        this.xDimension = xDimension;
        this.yDimension = yDimension;
        
        this.boundingBox = new Rectangle2D.Double();
        this.objectID = new String();
    }    
    
    /**
     * Returns the bounding box for the object.
     * 
     * @return  The bounding box.
     */
    public Rectangle2D getBoundingBox()
    {
        this.boundingBox.setRect(
                            this.position.coordinates.x - this.xDimension/2,
                            this.position.coordinates.y - this.yDimension/2,
                            xDimension,
                            yDimension);
        
        return this.boundingBox;
    }
    
    /**
     * Check to see if the bounding box of the provided object intersects the
     * bounding area of this object.
     * 
     * @param   object      [in]    Supplies the object.
     * 
     * @return  True if the bounding box of the object intersects the bounding
     *          area of this object, false otherwise.
     */
    public boolean collidesWithObject(ObjectSim2D object)
    {
        return this.getBoundingBox().intersects(object.getBoundingBox());
    }
    
    /**
     * Get the unique ID of this object.
     * 
     * @return The unique String representing this object.
     */
    public String getID()
    {
        return this.objectID;
    }
    
    /**
     * Set the unique ID of this object.
     * 
     * @param   newID   [in]    Supplies the ID.
     */
    public void setID(String newID)
    {
        this.objectID = newID;
    }
    
    /**
     * Get the absolute position (center point and rotation) of this object.
     * 
     * @return  The absolute position.
     */
    public AbsolutePositionSim2D getAbsolutePosition()
    {
        return this.position;
    }

    /**
     * Set the absolute position of this object.
     * 
     * @param   newPosition     [in]    Supplies the new position.
     */
    public void setAbsolutePosition(AbsolutePositionSim2D newPosition)
    {
        this.setCenterPoint(newPosition.coordinates);       
        this.setAngle(newPosition.angleInRadians);
    }
    
    /**
     * Get the position of the center of this object.
     * 
     * @return  The Point2D representing the center of this object.
     */
    public Point2D getCenterPoint()
    {
        return this.position.coordinates;
    }
    
    /**
     * Set the center point of this object.
     * 
     * @param   newCenter   [in]    Supplies the new center point of this
     *                              object.
     */
    public void setCenterPoint(Point2D newCenter)
    {
        this.translate(
                (double) newCenter.getX() - this.position.coordinates.x,
                (double) newCenter.getY() - this.position.coordinates.y);
    }
    
    /**
     * Get the rotation of this object in user-space in radians.
     * 
     * @return  The rotation in radians.
     */
    public double getAngle()
    {
        return this.position.angleInRadians;
    }
    
    /**
     * Set the absolute rotation of this object in user-space in radians.
     * 
     * @param   rotationInRadians   [in]    Supplies the rotation in radians.
     */
    public void setAngle(double rotationInRadians)
    {
        this.rotate(rotationInRadians - this.position.angleInRadians);
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
    public void translate(double dx, double dy)
    {
        this.position.coordinates.x += dx;
        this.position.coordinates.y += dy;
    }
    
    /**
     * Rotate this object's center point.
     * 
     * @param   rotationInRadians   [in]    Supplies the rotation in radians.
     */
    public void rotate(double rotationInRadians)
    {
        this.position.angleInRadians += rotationInRadians;
        if(this.position.angleInRadians > (Math.PI * 2.0d)) {
            this.position.angleInRadians = this.position.angleInRadians % (Math.PI * 2.0d);
        }
        else if(this.position.angleInRadians < -(Math.PI * 2.0d)) {
            this.position.angleInRadians = this.position.angleInRadians % -(Math.PI * 2.0d);
        }        
    }
}
