
package edu.wright.cs.carl.wart.sim2D;

import java.awt.geom.Point2D;


/**
 * Plain-old-data class representing the center point of an object's location
 * and its rotation around that point.
 *
 * @author  Duane Bolick
 */
public class AbsolutePositionSim2D implements java.io.Serializable
{
    public Point2D.Double coordinates;
    public double angleInRadians;
    
    public AbsolutePositionSim2D()
    {
        this.coordinates = new Point2D.Double();
    }
    
    public AbsolutePositionSim2D(double x, double y, double angleInRadians)
    {
        this.coordinates = new Point2D.Double(x,y);
        this.angleInRadians = angleInRadians;
    }
}
