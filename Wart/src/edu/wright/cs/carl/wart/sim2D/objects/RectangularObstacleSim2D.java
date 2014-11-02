
package edu.wright.cs.carl.wart.sim2D.objects;

import java.awt.geom.Rectangle2D;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.AbstractObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.visualization.DrawableShapeSim2D;
import edu.wright.cs.carl.wart.sim2D.visualization.PositionUpdateSim2D;


/**
 * This class represents an obstacle object.
 *
 * @author  Duane Bolick
 */
public class RectangularObstacleSim2D extends AbstractObjectSim2D
{
    private PositionUpdateSim2D drawableUpdate;
    
    /**
     * Construct a new obstacle object.
     * 
     * @param   objectID            [in]    Supplies the object's unique ID.  
     * @param   startingPosition    [in]    Supplies the starting center point
     *                                      and rotation.
     * @param   xDimension          [in]    Supplies the number of pixels in the
     *                                      x dimension of the obstacle's size.
     * @param   yDimension          [in]    Supplies the number of pixels in the
     *                                      y dimension of the obstacle's size.
     */
    public RectangularObstacleSim2D(AbsolutePositionSim2D startingPosition, double xDimension, double yDimension)
    {
        super(startingPosition, xDimension, yDimension);
        
        this.drawableUpdate = new PositionUpdateSim2D();
    }
  
    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    public DrawableObjectSim2D getDrawable()
    {
        return new DrawableShapeSim2D( 
                    this.objectID,
                    new Rectangle2D.Double(0,0,xDimension,yDimension),
                    ConstantsSim2D.ObstacleColor);
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
