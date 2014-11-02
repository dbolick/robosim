
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;


/**
 * This class represents a world object that can be drawn on a Sim2D's 
 * graphics object as a geometric shape.  In order to properly use this class,
 * an <i>ObjectSim2D</i> must provide its unique ID, a <i>RectangularShape</i>
 * that represents the image of this object, and its <i>Color</i>.  The
 * shape that is passed to the constructor of this class must be the
 * un-transformed shape, with its upper-left corner at the origin of user-space,
 * and a rotation of zero.
 *
 * @author  Duane Bolick
 */
public class DrawableShapeSim2D implements DrawableObjectSim2D
{
    protected String objectID;
    protected RectangularShape shape;
    protected Color color;
    protected PositionUpdateSim2D currentPosition;
    protected boolean hasBeenUpdated = false;
    
    public DrawableShapeSim2D(String objectID, RectangularShape shape, Color color)
    {
        this.objectID = objectID;
        this.shape = shape;
        this.color = color;
        this.currentPosition = new PositionUpdateSim2D();
    }
    
    /**
     * Get the unique ID of this drawable object.  This should be the same
     * ID as the corresponding Sim2DObject
     * 
     * @return  The unique ID.
     */
    public String getID()
    {
        return this.objectID;
    }
    
    /**
     * Send an update to the object.
     * 
     * @param   update  [in]    Supplies the update.
     */
    public void update(Object update)
    {
        PositionUpdateSim2D positionUpdate = (PositionUpdateSim2D)update;
        this.currentPosition.position.coordinates.x = positionUpdate.position.coordinates.x;
        this.currentPosition.position.coordinates.y = positionUpdate.position.coordinates.y;
        this.currentPosition.position.angleInRadians = positionUpdate.position.angleInRadians;
        this.hasBeenUpdated = true;
    }
    
    /**
     * Draw this component on the provided Graphics2D object.
     * 
     * @param   g       [in]    Supplies the graphics object.
     * @param   update  [in]    Supplies an update to the drawable.
     */
    public void render(Graphics2D g)
    {
        if(this.hasBeenUpdated == false) {
            return;
        }
        
        Color lastColor = g.getColor();
        AffineTransform lastTransform = g.getTransform();
        
        g.setColor(this.color);
        
        g.translate((int) this.currentPosition.position.coordinates.x - this.shape.getCenterX(),
                    (int) this.currentPosition.position.coordinates.y - this.shape.getCenterY());        
        
        //g.rotate(   this.currentPosition.position.angleInRadians,
          //          this.currentPosition.position.coordinates.x- this.shape.getCenterX(),
            //        this.currentPosition.position.coordinates.y- this.shape.getCenterY());     
        
        g.fill(this.shape);
        
        g.setColor(lastColor);
        g.setTransform(lastTransform);
    }
}
