
package edu.wright.cs.carl.wart.sim2D.visualization;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;


/**
 * Update to a drawable light.
 *
 * @author  Duane Bolick
 */
public class LightUpdateSim2D implements java.io.Serializable
{
    public AbsolutePositionSim2D position;
    public boolean isLightOn;
    
    public LightUpdateSim2D()
    {
        this.position = new AbsolutePositionSim2D();
        this.isLightOn = true;
    }
}
