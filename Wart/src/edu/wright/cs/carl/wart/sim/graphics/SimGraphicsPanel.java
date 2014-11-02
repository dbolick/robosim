
package edu.wright.cs.carl.wart.sim.graphics;

import edu.wright.cs.carl.wart.sim.*;

/**
 * Tagging class for all sim view panels.
 *
 * @author  Duane Bolick
 */
public abstract class SimGraphicsPanel extends javax.swing.JPanel
{
    /**
     * Set the scale factor (zoom level) of the graphics view.
     * 
     * @param   scaleFactor     [in]    Supplies the zoom level of the simulated
     *                                  world.  If this value is greater than
     *                                  one, the view will zoom in.  If it's
     *                                  between one and zero, the view will
     *                                  zoom out.
     */
    public abstract void setScaleFactor(double scaleFactor);
    
    /**
     * Check to see if this graphics panel has received its initial update from
     * the context.
     * 
     * @return  True if the graphics panel has been initialized, false
     *          otherwise.
     */
    public abstract boolean isInitialized();
    
    /**
     * Check to see if the graphics panel has started to receive updates from
     * the context.
     * 
     * @return  True if the graphics panel has received an update from the
     *          context, false otherwise.
     */
    public abstract boolean isReceivingUpdates();
    
    /**
     * Create the image of this frame to be drawn on the graphics component of
     * the panel.
     */
    public abstract void render();
    
    /**
     * Update the graphics panel with the new state that defines both what
     * should be drawn, and where.
     * 
     * @param   update      [in]    Supplies the update.
     */
    public abstract void updateGraphics(SimGraphicsUpdate update);
    
    /**
     * Set the dimensions of the sim world.  
     * 
     * TODO: For now, this method assumes that the simulated world is a 2D world
     * with a square shape.
     * 
     * @param   dimensions  [in]    Supplies the dimensions in number of pixels.
     */
    public abstract void setDimensions(int dimensions);
    
    /**
     * Get the length of the sim world in pixels.
     * 
     * TODO: For now, this method assumes that the simulated world is a 2D world
     * with a square shape.
     * 
     * @return  The length of one side of the simulated world in pixels.
     */
    public abstract int getDimensions();
}
