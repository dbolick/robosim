
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.Map;

import edu.wright.cs.carl.wart.sim2D.*;

import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D;


/**
 * Simplified implementation of the Khepera body module.
 *
 * @author  Duane Bolick
 */
public class KheperaBody extends AbstractComponentSim2D
{
    /**
     * Create a Khepera body.
     * 
     * @param   objectID            [in]    Supplies the ID
     * @param   isSolid             [in]    Supplies the flag that indicates if
     *                                      this object is solid or not.
     * @param   startingPosition    [in]    Supplies the starting position.
     */    
    public KheperaBody(AbsolutePositionSim2D startingPosition)
    {
        super(  startingPosition,
                KheperaConstantsSim2D.KheperaBodyLengthInPixels,
                KheperaConstantsSim2D.KheperaBodyWidthInPixels);
    }

    
    /**
     * Update the state of this object.  An active object updates itself once
     * per world-update cycle.  Since world-update cycles occur a specified
     * number of times per second, we know the duration of a single update cycle
     * in milliseconds.  Implementations of ActiveSim2D should ensure that all
     * movement/state changes/etc. are, if applicable, executed based on the
     * provided duration.
     * 
     * @param   objects     [in]    Provides a map of the other objects in the
     *                              world.
     * @param timeInMillis  [in]    Supplies the duration of this update cycle
     *                              in milliseconds.
     */
    public void update(Map<String, ObjectSim2D> objects,long timeInMillis)
    {
        //
        // Don't do anything, since we took the sensors off this bad boy.
        //
    }
}
