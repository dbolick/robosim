
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.util.ArrayList;
import java.util.List;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsUpdate;


/**
 * The payload of the initial ViewUpdateMessage a user receives upon joining
 * a context that uses the Sim2D world model.  It contains instantiations of
 * all Drawable objects in the sim world.  These are not intended to be
 * remote objects - they are just copies of the drawableObjects.  Subsequent
 * client-side updates will be sent using ViewUpdateSim2D message payloads.
 *
 * @author  Duane Bolick
 */
public class Sim2DGraphicsInitializer implements SimGraphicsUpdate
{
    public List<DrawableObjectSim2D> drawableObjects;
    
    public Sim2DGraphicsInitializer()
    {
        this.drawableObjects = new ArrayList<DrawableObjectSim2D>();
    }
}
