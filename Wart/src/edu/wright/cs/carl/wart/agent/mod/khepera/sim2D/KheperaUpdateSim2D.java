
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;


/**
 * This is the "payload" object used to send an update about the position and
 * state of the Khepera to a View of the sim world, so that it can be rendered.
 *
 * @author  Duane Bolick
 */
public class KheperaUpdateSim2D implements java.io.Serializable
{
    public String agentName;
    public String userInControl;
    
    public AbsolutePositionSim2D position;
    
    public KheperaConstants.Gripper.GripState gripState;
    public KheperaConstants.Gripper.ArmState armState;
    
    public List<Integer> lightValues;
    public List<Integer> distanceValues;
    public List<Integer> lvtValues;
    
    public List<Point2D> sensorPoints;
    public List<Point2D> sensorEndPoints;
    
    public KheperaUpdateSim2D()
    {
        this.position = new AbsolutePositionSim2D();
        
        this.lightValues = new ArrayList<Integer>();
        this.distanceValues = new ArrayList<Integer>();
        this.lvtValues = new ArrayList<Integer>();
        
        this.sensorPoints = new ArrayList<Point2D>();
        this.sensorEndPoints = new ArrayList<Point2D>();
    }
}
