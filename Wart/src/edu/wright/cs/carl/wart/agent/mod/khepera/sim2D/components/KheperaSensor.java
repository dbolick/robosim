
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import java.util.Map;

import edu.wright.cs.carl.wart.sim2D.*;

import edu.wright.cs.carl.wart.sim2D.ObjectSim2D;

import edu.wright.cs.carl.wart.sim2D.models.collision.*;

import edu.wright.cs.carl.wart.sim2D.objects.LightSim2D;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;

import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D;
import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaSim2D;


/**
 * Implementation of an individual Khepera IR sensor.  The sensing model is 
 * extremely simple.  There are two pairs of arrays for each sensor, one pair
 * for distance, and one pair for light.  In each pair, there is one array
 * that represents sensor readings along the direction of the sensor, and one
 * that represents readings along a 45-degree angle from the direction of the
 * sensor.
 * 
 * The numbers in each array represent the reading at a specific linear distance
 * in millimeters from the center ponit of the sensor.  The readings are at a
 * regular interval, which is defined for each sensor type.  Since distance
 * measures in the sim world are calculated in pixels, we use the
 * pixels-per-millimeter constant in
 * <i>edu.wright.cs.carl.wart.sim2D.ConstantsSim2D</i> to convert.
 * 
 * Distance values that fall between two points are calculated by linear
 * interpolation.
 * 
 * @author  Duane Bolick
 */
public class KheperaSensor extends AbstractComponentSim2D
{
    private int currentDistanceValue;
    private int currentLightValue;
    
    private Point2D endPoint;
    private boolean isBlockedByGripper;

    public KheperaSensor(AbsolutePositionSim2D startingPosition)
    {
        super(startingPosition, 0, 0);        
        
        this.boundingBox = new Rectangle2D.Double();
        this.endPoint = new Point2D.Double();
    }
    
    public void setIsBlockedByGripper(boolean isBlockedByGripper)
    {
        this.isBlockedByGripper = isBlockedByGripper;
    }
    
    public int getDistanceValue()
    {
        return this.currentDistanceValue;
    }
    
    public int getLightValue()
    {
        return this.currentLightValue;
    }

    public Point2D getSensorEndPoint()
    {
        this.endPoint.setLocation(
                        this.getAbsolutePosition().coordinates.x + Math.cos(this.getAngle()) * 100,
                        this.getAbsolutePosition().coordinates.y + Math.sin(this.getAngle()) * 100);
        return this.endPoint;
    }
    
    /**
     * Calculate the distance value that this sensor would return given an
     * object located at the provided distance and bearing.
     * 
     * @param   distanceInPixels    [in]    Supplies the distance in pixels.
     * @param   bearingInRadians    [in]    Supplies the bearing in radians.
     * 
     * @return  The sensor value.
     */
    protected double calculateDistanceValue(double distanceInPixels, double bearingInRadians)
    {
        double distanceInMillimeters = distanceInPixels * ConstantsSim2D.PixelsPerMillimeter;
        
        if(distanceInMillimeters >= KheperaConstantsSim2D.MaxDistanceSensorRangeInMillimeters) {
            return KheperaConstantsSim2D.DistanceValueMin + sensorNoise(KheperaConstantsSim2D.DistanceSensorFuzz);
        }
        
        double distanceFromPreviousSensorValue = distanceInMillimeters % KheperaConstantsSim2D.DistanceValuesIntervalInMM;
        
        int previousSensorIndex =
                (int)((distanceInMillimeters - distanceFromPreviousSensorValue) / KheperaConstantsSim2D.DistanceValuesIntervalInMM);
        
        int nextSensorIndex = previousSensorIndex + 1;
        
        int previousSensorValue = KheperaConstantsSim2D.DistanceValues[previousSensorIndex];
        int nextSensorValue= KheperaConstantsSim2D.DistanceValues[nextSensorIndex];
        
        double sensorUnitsPerMM = (nextSensorValue - previousSensorValue) / KheperaConstantsSim2D.DistanceValuesIntervalInMM;
        
        int sensorReading = previousSensorValue + (int)(sensorUnitsPerMM * distanceFromPreviousSensorValue);
        
        sensorReading -= sensorReading * KheperaConstantsSim2D.DistanceValueDecreaseFactorPerRadian * Math.abs(bearingInRadians);

        
        return sensorReading + sensorNoise(KheperaConstantsSim2D.DistanceSensorFuzz);
    }
    
    /**
     * Calculate the light value that this sensor would return given a
     * light source located at the provided distance and bearing.
     * 
     * @param   distanceInPixels    [in]    Supplies the distance in pixels.
     * @param   bearingInRadians    [in]    Supplies the bearing in radians.
     * 
     * @return  The sensor value.
     */
    protected double calculateLightValue(double distanceInPixels, double bearingInRadians)
    {
        double distanceInMillimeters = distanceInPixels * ConstantsSim2D.PixelsPerMillimeter;
        
        if(distanceInMillimeters >= KheperaConstantsSim2D.MaxLightSensorRangeInMillimeters) {
            return KheperaConstantsSim2D.LightValueMax + sensorNoise(KheperaConstantsSim2D.LightSensorFuzz);
        }
        
        double distanceFromPreviousSensorValue = distanceInMillimeters % KheperaConstantsSim2D.LightValuesIntervalInMM;
        
        int previousSensorIndex =
                (int)((distanceInMillimeters - distanceFromPreviousSensorValue) / KheperaConstantsSim2D.LightValuesIntervalInMM);
        
        int nextSensorIndex = previousSensorIndex + 1;
        
        int previousSensorValue = KheperaConstantsSim2D.LightValues[previousSensorIndex];
        int nextSensorValue= KheperaConstantsSim2D.LightValues[nextSensorIndex];
        
        double sensorUnitsPerMM = (nextSensorValue - previousSensorValue) / KheperaConstantsSim2D.LightValuesIntervalInMM;
        
        int sensorReading = previousSensorValue + (int)(sensorUnitsPerMM * distanceFromPreviousSensorValue);
        
        sensorReading -= sensorReading * KheperaConstantsSim2D.LightValueDecreaseFactorPerRadian * Math.abs(bearingInRadians);        
        
        return sensorReading + sensorNoise(KheperaConstantsSim2D.LightSensorFuzz);
    }    
    
    /**
     * Get a random amount of sensor noise.
     * 
     * @param   fuzz    [in]    Supplies the fuzz.
     * 
     * @return  An integer in the interval [-fuzz, +fuzz)
     */
    public static int sensorNoise(int fuzz)
    {
        if(Math.random() > 0.5) {
            return (int) (Math.random() * fuzz);
        }
        
        return (int) (-Math.random() * fuzz);
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
        // If the gripper arm is down, the sensor returns nothing useful.
        //
        if(((KheperaSim2D)this.agent).getArmState() == KheperaConstants.Gripper.ArmState.DOWN && this.isBlockedByGripper) {
            this.currentDistanceValue = KheperaConstantsSim2D.DistanceValueMax + sensorNoise(KheperaConstantsSim2D.DistanceSensorFuzz);
            this.currentLightValue = KheperaConstantsSim2D.LightValueMax + sensorNoise(KheperaConstantsSim2D.LightSensorFuzz);
            return;
        }
        
        
        //
        // Populate the sensor values with default "nothing-sensed" values in
        // case there's nothing to sense nearby.
        //
        int maxDistanceValue = KheperaConstantsSim2D.DistanceValueMin + sensorNoise(KheperaConstantsSim2D.DistanceSensorFuzz);
        int minLightValue = KheperaConstantsSim2D.LightValueMax + sensorNoise(KheperaConstantsSim2D.LightSensorFuzz);
        
        //
        // Iterate over all the objects in the world.
        //
        Point nearestPoint = new Point();
        ObjectSim2D currentObject = null;
        double headingToObjectInRadians;
        double bearingToObjectInRadians;        
        double distanceToObjectInPixels;
        int currentValue;
        
        Iterator<ObjectSim2D> it = objects.values().iterator();
        while(it.hasNext()) {
            currentObject = it.next();
            
            //
            // Exclude this object (no "self-collision")
            //
            if(currentObject != this.agent) {
                CollisionConstants.GetClosestPointOnRectangle(
                                        currentObject.getBoundingBox(),
                                        this.position.coordinates,
                                        nearestPoint);
                
                headingToObjectInRadians = CollisionConstants.GetVectorAngleInRadians(
                                                                this.position.coordinates.x,
                                                                this.position.coordinates.y,
                                                                nearestPoint.x,
                                                                nearestPoint.y);
                
                bearingToObjectInRadians = CollisionConstants.GetAngleDifferenceInRadians(
                                                                this.position.angleInRadians,
                                                                headingToObjectInRadians);
                
                distanceToObjectInPixels = this.position.coordinates.distance((Point2D) nearestPoint);
                
                
                
                // Distance sensor calculation
                if( bearingToObjectInRadians <= KheperaConstantsSim2D.DistanceSensorMaxBearingInRadians &&
                    distanceToObjectInPixels <= KheperaConstantsSim2D.MaxDistanceSensorRangeInMillimeters *
                                                ConstantsSim2D.PixelsPerMillimeter)
                {
                    currentValue = (int) this.calculateDistanceValue(distanceToObjectInPixels, bearingToObjectInRadians);
                    if(currentValue > maxDistanceValue) {
                        maxDistanceValue = currentValue;
                    }
                }
                
                // Light sensor calculation
                if(bearingToObjectInRadians <= KheperaConstantsSim2D.LightSensorMaxBearingInRadians && 
                   (currentObject instanceof LightSim2D) &&
                   ((LightSim2D)currentObject).isLightOn())
                {
                    currentValue = (int) this.calculateLightValue(distanceToObjectInPixels, bearingToObjectInRadians);
                    if(currentValue < minLightValue) {
                        minLightValue = currentValue;
                    }
                }
            } // End if this isn't the base object
        }
        
        this.currentDistanceValue = maxDistanceValue;

    }
}
