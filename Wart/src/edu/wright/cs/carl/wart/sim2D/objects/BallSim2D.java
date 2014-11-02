
package edu.wright.cs.carl.wart.sim2D.objects;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.AbstractGrippableObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.visualization.DrawableBallSim2D;
import edu.wright.cs.carl.wart.sim2D.visualization.PositionUpdateSim2D;


/**
 * This class represents a ball object.
 *
 * @author  Duane Bolick
 */
public class BallSim2D extends AbstractGrippableObjectSim2D
{
    private PositionUpdateSim2D drawableUpdate;
    
    public BallSim2D(AbsolutePositionSim2D startingPosition)
    {
        super(  startingPosition,
                ConstantsSim2D.BallDimensionsInPixels,
                ConstantsSim2D.BallDimensionsInPixels);
        
        this.drawableUpdate = new PositionUpdateSim2D();
    }
    
    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    public DrawableObjectSim2D getDrawable()
    {
        return new DrawableBallSim2D(this.objectID);        
    }
    
    /**
     * Get the current state of this object necessary to update the drawable
     * object that this object uses.
     * 
     * @return  The update.
     */
    public Object getDrawableUpdate()
    {
        this.drawableUpdate.position.angleInRadians = this.position.angleInRadians;
        this.drawableUpdate.position.coordinates.x = this.position.coordinates.x;
        this.drawableUpdate.position.coordinates.y = this.position.coordinates.y;
        
        return this.drawableUpdate;
    }
}
