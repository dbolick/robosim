
package edu.wright.cs.carl.wart.sim2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.Serializable;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;


/**
 * This is the base interface for all objects that exist in the Sim2D world.
 * All objects are defined by their center point, rotation, and bounding area.
 * 
 * @author  Duane Bolick
 */
public interface ObjectSim2D extends Serializable
{
    /**
     * Get the unique ID of this object.
     * 
     * @return The unique String representing this object.
     */
    public String getID();
    
    /**
     * Set the unique ID of this object.
     * 
     * @param   newID   [in]    Supplies the ID.
     */
    public void setID(String newID);
    
    /**
     * Get the absolute position (center point and rotation) of this object.
     * 
     * @return  The absolute position.
     */
    public AbsolutePositionSim2D getAbsolutePosition();

    /**
     * Set the absolute position of this object.
     * 
     * @param   newPosition     [in]    Supplies the new position.
     */
    public void setAbsolutePosition(AbsolutePositionSim2D newPosition);
    
    /**
     * Get the position of the center of this object.
     * 
     * @return  The Point2D representing the center of this object.
     */
    public Point2D getCenterPoint();
    
    /**
     * Set the center point of this object.
     * 
     * @param   newCenter   [in]    Supplies the new center point of this
     *                              object.
     */
    public void setCenterPoint(Point2D newCenter);
    
    /**
     * Get the angle of this object in user-space in radians.
     * 
     * @return  The rotation in radians.
     */
    public double getAngle();
    
    /**
     * Set the absolute angle of this object in user-space in radians.
     * 
     * @param   newAngleInRadians   [in]    Supplies the rotation in radians.
     */
    public void setAngle(double newAngleInRadians);    
    
    /**
     * Translate this object in user-space.  The provided changes in x and y
     * coordinates may be negative.
     * 
     * @param   dx  [in]    Supplies the number of pixels to translate this
     *                      object along the x-axis.
     * @param   dy  [in]    Supplies the number of pixels to translate this
     *                      object along the y-axis.
     */
    public void translate(double dx, double dy);
    
    /**
     * Rotate this object's center point.
     * 
     * @param   rotationInRadians   [in]    Supplies the rotation in radians.
     */
    public void rotate(double rotationInRadians);    
    
    /**
     * Returns the bounding box for the object.
     * 
     * @return  The bounding box.
     */
    public Rectangle2D getBoundingBox();
    
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
    public boolean collidesWithObject(ObjectSim2D object);
    
    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    public DrawableObjectSim2D getDrawable();    
    
    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    public Object getDrawableUpdate();    
}
