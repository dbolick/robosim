
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.awt.Graphics2D;

import java.io.Serializable;


/**
 * Interface for any object that needs to be drawn in the 2D sim.  Any
 * implementation of a DrawableObjectSim2D is completely responsible for
 * any images, colors, positioning, etc.  Graphics objects should conform to
 * the scale constants declared in edu.wright.cs.carl.wart.sim2D.ConstantsSim2D.
 * 
 * @author  Duane Bolick
 */
public interface DrawableObjectSim2D extends Serializable
{
    /**
     * Get the unique ID of this drawable object.  This should be the same
     * ID as the corresponding Sim2DObject
     * 
     * @return  The unique ID.
     */
    public String getID();
    
    /**
     * Send an update to the object.
     * 
     * @param   update  [in]    Supplies the update.
     */
    public void update(Object update);
    
    /**
     * Draw this component on the provided Graphics2D object.
     * 
     * @param   g       [in]    Supplies the graphics object.
     */
    public void render(Graphics2D g);
}
