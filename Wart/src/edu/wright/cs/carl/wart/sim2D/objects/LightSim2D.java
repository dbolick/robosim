
package edu.wright.cs.carl.wart.sim2D.objects;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.AbstractObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableLightSim2D;
import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.visualization.LightUpdateSim2D;


/**
 * Represents a Light in the 2D sim world model.
 *
 * @author  Duane Bolick
 */
public class LightSim2D extends AbstractObjectSim2D
{
    private LightUpdateSim2D lightUpdate;
    private boolean lightOn;
    
    
    public LightSim2D(AbsolutePositionSim2D startingPosition)
    {
        super(startingPosition, ConstantsSim2D.LightDimensionsInPixels, ConstantsSim2D.LightDimensionsInPixels);
       
        this.lightUpdate = new LightUpdateSim2D();
        
        this.lightOn = true;
    }
    
    /**
     * Check to see if the light object is on.
     * 
     * @return  True if it's on, false if not.
     */
    public boolean isLightOn()
    {
        return this.lightOn;
    }
    
    /**
     * Turn the light on or off.
     * 
     * @param   lightOn     [in]    Supplies whether or not the light should be
     *                              on - true turns it on, false turns it off.
     */
    public void setLightOn(boolean lightOn)
    {
        this.lightOn = lightOn;
    }

    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    public DrawableObjectSim2D getDrawable()
    {
        return new DrawableLightSim2D(this.objectID);
    }    
    
    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    public Object getDrawableUpdate()
    {
        this.lightUpdate.position.angleInRadians = this.position.angleInRadians;
        this.lightUpdate.position.coordinates.x = this.position.coordinates.x;
        this.lightUpdate.position.coordinates.y = this.position.coordinates.y;
        this.lightUpdate.isLightOn = this.lightOn;
        return this.lightUpdate;
    } 
}
