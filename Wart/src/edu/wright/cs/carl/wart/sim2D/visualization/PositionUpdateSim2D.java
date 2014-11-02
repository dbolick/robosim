
package edu.wright.cs.carl.wart.sim2D.visualization;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;


/**
 * This class represents an update to a drawable object containing its new
 * absolute position.
 *
 * @author  Duane Bolick
 */
public class PositionUpdateSim2D implements java.io.Serializable
{
    public AbsolutePositionSim2D position;
    
    public PositionUpdateSim2D()
    {
        this.position = new AbsolutePositionSim2D();
    }
}
