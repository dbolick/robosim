
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D;

import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wright.cs.carl.wart.sim2D.AbstractAgentSim2D;
import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.ComponentSim2D;
import edu.wright.cs.carl.wart.sim2D.ObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.PinPositionSim2D;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;
import edu.wright.cs.carl.wart.agent.mod.khepera.interfaces.KheperaStudent;


/**
 * Implementation of a 2-dimensional simulated Khepera.  This implements the
 * KheperaStudent interface, for use with controllers.  It also implements
 * the AgentSim2D interface, so that it will be compatible with the
 * simulated world.
 *
 * @author Duane
 */
public class KheperaSim2D extends AbstractAgentSim2D implements KheperaStudent
{
    private String description = "Standard Khepera II with Gripper and Linear Vision Turret.";
    
    private AbsolutePositionSim2D nextPosition;
    
    // The pinned components
    private KheperaBody body;   
    private KheperaGripper gripper;
    private KheperaLVT lvt;
    private List<KheperaSensor> sensors;
    private KheperaMotor motor;
    
    private KheperaUpdateSim2D drawableUpdate;
    
    public KheperaSim2D(String name, AbsolutePositionSim2D startingPosition)
    {
        super(startingPosition);
        
        this.setID(name);
        
        this.nextPosition = new AbsolutePositionSim2D();
        this.nextPosition.angleInRadians = this.position.angleInRadians;
        this.nextPosition.coordinates.x = this.position.coordinates.x;
        this.nextPosition.coordinates.y = this.position.coordinates.y;
        
        //
        // First create all the components.  It really doesn't matter what
        // starting position you give them, since that'll change when they're
        // pinned to this agent.
        //
        this.body = new KheperaBody(this.position);
        this.body.setID(name + "_BODY");
        
        this.gripper = new KheperaGripper(this.position);
        this.gripper.setID(name + "_GRIPPER");
        
        this.lvt = new KheperaLVT(this.position);
        this.lvt.setID(name + "_LVT");
        
        this.motor = new KheperaMotor();
        this.sensors = new ArrayList<KheperaSensor>();
        
        KheperaSensor currentSensor = null;
        for(int i=0; i< KheperaConstantsSim2D.NumSensors; ++i) {
            currentSensor = new KheperaSensor(this.position);
            currentSensor.setID(name + "_SENSOR_" + i);
            sensors.add(currentSensor);
        }
        
        //
        // Next, pin all the components.
        //
        PinPositionSim2D pinPosition = new PinPositionSim2D();
        pinPosition.angleInRadians = 0;
        pinPosition.distanceInPixels = 0;
        pinPosition.rotationInRadians = 0;
        
        this.addComponent(this.body, pinPosition);
        this.addComponent(this.lvt, pinPosition);
        
        
        pinPosition.distanceInPixels =
                KheperaConstantsSim2D.ArmLengthInPixels +
                KheperaConstantsSim2D.ClawLengthInPixels/2 - 
                KheperaConstantsSim2D.KheperaBodyLengthInPixels/2;
        this.addComponent(this.gripper, pinPosition);        

        
        PinPositionSim2D sensorPinPosition = new PinPositionSim2D();
        for(int i=0; i < KheperaConstantsSim2D.NumSensors; ++i) {
            
            sensorPinPosition.angleInRadians = KheperaConstantsSim2D.SensorRelativeAngleInRadians[i];
            sensorPinPosition.distanceInPixels = KheperaConstantsSim2D.SensorRelativeDistanceInPixels;
            sensorPinPosition.rotationInRadians = KheperaConstantsSim2D.SensorFacingAngleInRadians[i];
            
            this.addComponent(sensors.get(i), sensorPinPosition);
        }           

        //
        // Finally, create the drawable update.
        //
        this.drawableUpdate = new KheperaUpdateSim2D();
    }

    
    /**
     * Implementations of all required abstract methods.
     */
    
    /**
     * Update the state of this object.  An active object updates itself once
     * per world-drawableUpdate cycle.  Since world-drawableUpdate cycles occur a specified
     * number of times per second, we know the duration of a single drawableUpdate cycle
     * in milliseconds.  Implementations of ActiveSim2D should ensure that all
     * movement/state changes/etc. are, if applicable, executed based on the
     * provided duration.
     * 
     * @param   objects     [in]    Provides a map of the other objects in the
     *                              world.
     * @param timeInMillis  [in]    Supplies the duration of this drawableUpdate cycle
     *                              in milliseconds.
     */
    public void update(Map<String, ObjectSim2D> objects, long timeInMillis)
    {
        //
        // First, update all the pinned components.
        //
        Iterator<ComponentSim2D> it = this.components.values().iterator();
        while(it.hasNext()) {
            it.next().update(objects, timeInMillis);
        }         
        
        //
        // Then get how much we're supposed to move, based on the motor.
        //
        this.motor.move(this.position, this.nextPosition, timeInMillis);
        
        //
        // And move.
        //
        this.setAbsolutePosition(this.nextPosition); 
    }
    
    /**
     * Get an instantiation of the DrawableSim2D that this object uses.
     * 
     * @return  The drawable object.
     */
    public DrawableObjectSim2D getDrawable()
    {
        return new DrawableKheperaSim2D(this.objectID);
    }
    
    /**
     * Get the current state of this object necessary to drawableUpdate the drawable
     * object that this object uses.
     * 
     * @return  The drawableUpdate.
     */
    public Object getDrawableUpdate()
    {        
        //
        // Update the position.
        //
        this.drawableUpdate.position = this.position;        
        
        //
        // Update the gripper arm state.
        //
        this.drawableUpdate.armState = this.getArmState();
        this.drawableUpdate.gripState = this.getGripperState();

        //
        // Update the sensor values of the drawableUpdate.
        //
        this.drawableUpdate.lightValues.clear();
        this.drawableUpdate.distanceValues.clear();
        this.drawableUpdate.sensorPoints.clear();
        this.drawableUpdate.sensorEndPoints.clear();
        
        KheperaSensor currentSensor = null;
        Iterator<KheperaSensor> it = this.sensors.iterator();
        while(it.hasNext()) {
            currentSensor = it.next();
            this.drawableUpdate.lightValues.add(currentSensor.getLightValue());
            this.drawableUpdate.distanceValues.add(currentSensor.getDistanceValue());            
            this.drawableUpdate.sensorPoints.add(currentSensor.getCenterPoint());
            this.drawableUpdate.sensorEndPoints.add(currentSensor.getSensorEndPoint());
        }

        //
        // Update the LVT values of the drawableUpdate.
        //
        this.drawableUpdate.lvtValues = this.lvt.getLvtImage();
        
        //
        // Update the sensor positions.
        //
        
        
        //
        // Finally, return it
        //
        return this.drawableUpdate;
    }

    
    /**
     * Additional Accessors
     */
    
    /**
     * Get a reference to the Gripper model.
     * 
     * @return  The gripper.
     */
    public ComponentSim2D getGripper()
    {
        return this.gripper;
    }
    
    /**
     * Get a reference to the Body Model.
     * 
     * @return  The body.
     */
    public ComponentSim2D getBody()
    {
        return this.body;
    }    
    
    
    /**
     * Agent methods
     */
    
    /**
     * Set the name of this agent.
     * 
     * @param   newName     [in]    Supplies the new name. 
     */
    public void setName(String newName)
    {
        this.objectID = newName;
    }
    
    /**
     * Get the agent's name.  Agent name is used for identification purposes,
     * and should be unique (one unique name per entity) in a given context.
     * 
     * @return  The agent's name.
     */
    public String getName()
    {
        return this.objectID;
    }
    
    /**
     * Get a text description of the agent.  This should, but doesn't have to,
     * include the components and capabilities of the Agent, including the
     * available control interfaces.
     * 
     * @return  A description of the Agent.
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * Set the Agent to a resting state.  This state should probably be a
     * non-moving, low-power state that can be sustained without causing damage
     * to the Agent.
     */
    public void rest()
    {
        this.motor.setWheelSpeeds(KheperaConstants.Core.MOTOR_STOP_SPEED, KheperaConstants.Core.MOTOR_STOP_SPEED);
        this.gripper.setArmState(KheperaConstants.Gripper.ArmState.DOWN);
        this.gripper.setGripperState(KheperaConstants.Gripper.GripState.OPEN);
    }

    /**
     * Set the motor speeds.
     * 
     * @param   left    [in]    Supplies the desired left motor speed.
     * @param   right   [in]    Supplies the desired right motor speed.
     */
    public void setMotorSpeeds(int left, int right)
    {
        this.motor.setWheelSpeeds(left, right);
    }
    
    /**
     * Set the left motor speed.
     * 
     * @param   speed   [in]    Supplies the motor speed.
     */
    public void setLeftMotorSpeed(int speed)
    {
        this.motor.setLeftWheelSpeed(speed);
    }

    /**
     * Set the right motor speed.
     * 
     * @param   speed   [in]    Supplies the motor speed.
     */    
    public void setRightMotorSpeed(int speed)
    {
        this.motor.setRightWheelSpeed(speed);
    }
    
    /**
     * Get the light sensor values.
     * 
     * @return  A list containing the sensor values.
     */
	public List<Integer> getLightValues()
    {
        List<Integer> lightValues = new ArrayList<Integer>();
        Iterator<KheperaSensor> it = this.sensors.iterator();
        while(it.hasNext()) {
            lightValues.add(it.next().getLightValue());
        }
        return lightValues;
    }
    
    /**
     * Get the distance sensor values.
     * 
     * @return  An array containing the sensor values.
     */	
	public List<Integer> getDistanceValues()
    {
        List<Integer> distanceValues = new ArrayList<Integer>();
        Iterator<KheperaSensor> it = this.sensors.iterator();
        while(it.hasNext()) {
            distanceValues.add(it.next().getDistanceValue());
        }
        return distanceValues;        
    }

    /**
     * Get the state of the gripper turret arm.
     * 
     * @return  The arm state.
     */
    public KheperaConstants.Gripper.ArmState getArmState()
    {
        return this.gripper.getArmState();
    }
    
    /**
     * Set the state of the gripper turret arm.
     * 
     * @param   armState    [in]    Supplies the arm state.
     */
	public void setArmState(KheperaConstants.Gripper.ArmState armState)
    {
        this.gripper.setArmState(armState);
    }
    
    /**
     * Get the state of the gripper turret gripping-claw.
     * 
     * @return  The state of the gripper turret gripping-claw.
     */
    public KheperaConstants.Gripper.GripState getGripperState()
    {
        return this.gripper.getGripperState();
    }
    
    /**
     * Set the state of the gripper turret gripping-claw.
     * 
     * @param   gripState   [in]    Supplies the claw-state.
     */
    public void setGripperState(KheperaConstants.Gripper.GripState gripState)
    {
        this.gripper.setGripperState(gripState);
    }
			
    /**
     * Determine if an object is present in the gripper claw.
     * 
     * @return  True if an object is present, false otherwise.
     */
	public boolean isObjectPresent()
    {
        return this.gripper.isObjectPresent();
    }
    
    /**
     * Determine if an object is gripped in the gripper claw.
     * 
     * @return  True if an object is held, false otherwise.s
     */
    public boolean isObjectHeld()
    {
        return this.gripper.isObjectHeld();
    }
    
	/*
	 * Returns the most recent LVT array.
	 */
	public List<Integer> readLvtImage()
    {
        return this.lvt.getLvtImage();
    }
    
	/*
	 * Returns the pixel with maximum intensity
	 */
	public double getPixelMaxIntensity()
    {
        return this.lvt.getPixelMaxIntensity();
    }
	
	/*
	 * Returns the pixel with minimum intensity
	 */
	public double getPixelMinIntensity()
    {
        return this.lvt.getPixelMinIntensity();
    }
    
}
