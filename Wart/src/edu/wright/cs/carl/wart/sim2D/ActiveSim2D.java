
package edu.wright.cs.carl.wart.sim2D;

import java.util.Map;

/**
 * Interface for all active objects.  An active object is one that can change
 * its own state, based on user input, a timer, etc.  An object that implements
 * this interface will have its update method called once per world update
 * cycle.
 * 
 * @author  Duane Bolick
 */
public interface ActiveSim2D
{
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
    public void update(Map<String, ObjectSim2D> objects, long timeInMillis);
}
