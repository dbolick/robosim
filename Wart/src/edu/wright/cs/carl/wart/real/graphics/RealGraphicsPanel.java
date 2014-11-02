
package edu.wright.cs.carl.wart.real.graphics;

/**
 * Tagging class for all sim view panels.
 *
 * @author  Duane Bolick
 */
public abstract class RealGraphicsPanel extends javax.swing.JPanel
{
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
    public abstract void updateGraphics(RealGraphicsUpdate update);
}
