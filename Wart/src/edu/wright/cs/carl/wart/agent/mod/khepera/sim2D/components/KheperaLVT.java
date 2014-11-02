
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components;

import java.awt.Rectangle;
import java.awt.Shape;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wright.cs.carl.wart.sim2D.*;

import edu.wright.cs.carl.wart.sim2D.ObjectSim2D;

import edu.wright.cs.carl.wart.sim2D.models.collision.*;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;

import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D;


/**
 * Implementation of a Khepera linear vision turret.
 *
 * @author  Duane Bolick
 */
public class KheperaLVT extends AbstractComponentSim2D
{
    private List<Integer> currentValues;
    private double minIntensity = 0;
    private double maxIntensity = 0;
    
    public KheperaLVT(AbsolutePositionSim2D startingPosition)
    {
        super(startingPosition, 0, 0);
        
        this.currentValues = new ArrayList<Integer>();
    }    
    
	/*
	 * Returns the most recent LVT array.
	 */
	public List<Integer> getLvtImage()
    {
        return this.currentValues;
    }    
    
	/*
	 * Returns the pixel with maximum intensity
	 */
	public double getPixelMaxIntensity()
    {
        return this.maxIntensity;
    }
	
	/*
	 * Returns the pixel with minimum intensity
	 */
	public double getPixelMinIntensity()
    {
        return this.minIntensity;
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
        // TODO:  Update the sensor values.
        //
        // For now, this doesn't do anything.
        //
    }
}
