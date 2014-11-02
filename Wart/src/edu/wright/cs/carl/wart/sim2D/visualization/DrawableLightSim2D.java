
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;


/**
 * This class represents a light object that can be drawn on a Sim2D's 
 * graphics object.  
 *
 * @author  Duane Bolick
 */
public class DrawableLightSim2D implements DrawableObjectSim2D
{
    protected String objectID;
    protected Ellipse2D lightShape;
    protected Ellipse2D auraShape;    
    protected Color lightColor;
    protected Color auraColor;  
    protected LightUpdateSim2D currentUpdate;
    protected boolean hasBeenUpdated = false;
    
    public DrawableLightSim2D(String objectID)
    {
        this.objectID = objectID;
        
        this.lightShape = new Ellipse2D.Double(0, 0, ConstantsSim2D.LightDimensionsInPixels, ConstantsSim2D.LightDimensionsInPixels);
        this.lightColor = ConstantsSim2D.LightColor;
        
        this.auraShape = new Ellipse2D.Double(   -(ConstantsSim2D.LightAuraDimensionsInPixels/2 - ConstantsSim2D.LightDimensionsInPixels/2),
                                                -(ConstantsSim2D.LightAuraDimensionsInPixels/2 - ConstantsSim2D.LightDimensionsInPixels/2),
                                                ConstantsSim2D.LightAuraDimensionsInPixels,
                                                ConstantsSim2D.LightAuraDimensionsInPixels);        
        
        this.auraColor = ConstantsSim2D.AuraColor;
        
        this.currentUpdate = new LightUpdateSim2D();
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
        LightUpdateSim2D lightUpdate = (LightUpdateSim2D)update;
        this.currentUpdate.position.coordinates.x = lightUpdate.position.coordinates.x;
        this.currentUpdate.position.coordinates.y = lightUpdate.position.coordinates.y;
        this.currentUpdate.position.angleInRadians = lightUpdate.position.angleInRadians;
        
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
        
        g.translate(this.currentUpdate.position.coordinates.x - this.lightShape.getCenterX(),
                    this.currentUpdate.position.coordinates.y - this.lightShape.getCenterY());
        
        if(this.currentUpdate.isLightOn) {            
            g.setColor(this.auraColor);
            g.fill(this.auraShape);
        }
        
        g.setColor(this.lightColor);
        g.fill(this.lightShape);
        
        g.setColor(lastColor);
        g.setTransform(lastTransform);
    }
}
