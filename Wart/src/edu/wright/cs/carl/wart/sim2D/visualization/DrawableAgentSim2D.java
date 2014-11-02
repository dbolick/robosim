
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.awt.Graphics2D;


/**
 * This is the drawable object that represents a collection of multiple,
 * potentially drawable objects.
 *
 * @author  Duane Bolick
 */
public abstract class DrawableAgentSim2D implements DrawableObjectSim2D
{
    /**
     * Get the unique ID of this drawable object.  This should be the same
     * ID as the corresponding Sim2DObject
     * 
     * @return  The unique ID.
     */
    abstract public String getID();
    
    /**
     * Send an update to the object.
     * 
     * @param   update  [in]    Supplies the update.
     */
    abstract public void update(Object update);
    
    /**
     * Draw this component on the provided Graphics2D object.
     * 
     * @param   g       [in]    Supplies the graphics object.
     */
    abstract public void render(Graphics2D g);
}
