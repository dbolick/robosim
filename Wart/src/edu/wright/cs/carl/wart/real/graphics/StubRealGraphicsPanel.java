
package edu.wright.cs.carl.wart.real.graphics;

/**
 *
 * @author dbolick
 */
public class StubRealGraphicsPanel extends RealGraphicsPanel
{
    /**
     * Check to see if this graphics panel has received its initial update from
     * the context.
     *
     * @return  True if the graphics panel has been initialized, false
     *          otherwise.
     */
    public boolean isInitialized()
    {
        return true;
    }

    /**
     * Check to see if the graphics panel has started to receive updates from
     * the context.
     *
     * @return  True if the graphics panel has received an update from the
     *          context, false otherwise.
     */
    public boolean isReceivingUpdates()
    {
        return true;
    }

    /**
     * Create the image of this frame to be drawn on the graphics component of
     * the panel.
     */
    public void render()
    {

    }

    /**
     * Update the graphics panel with the new state that defines both what
     * should be drawn, and where.
     *
     * @param   update      [in]    Supplies the update.
     */
    public void updateGraphics(RealGraphicsUpdate update)
    {
        
    }
}
