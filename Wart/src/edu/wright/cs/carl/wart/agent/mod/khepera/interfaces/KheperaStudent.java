
package edu.wright.cs.carl.wart.agent.mod.khepera.interfaces;

import java.util.List;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;


/**
 * This is a simplified control interface for a Khepera for use by
 * students and people you don't trust with your Khepera.
 *
 * @author  Duane Bolick
 */
public interface KheperaStudent
{
    /**
     * Set the motor speeds.
     * 
     * @param   left    [in]    Supplies the desired left motor speed.
     * @param   right   [in]    Supplies the desired right motor speed.
     */
    public void setMotorSpeeds(int left, int right);
    
    /**
     * Set the left motor speed.
     * 
     * @param   speed   [in]    Supplies the motor speed.
     */
    public void setLeftMotorSpeed(int speed);

    /**
     * Set the right motor speed.
     * 
     * @param   speed   [in]    Supplies the motor speed.
     */    
    public void setRightMotorSpeed(int speed);
    
    /**
     * Get the light sensor values.
     * 
     * @return  A list containing the sensor values.
     */
	public List<Integer> getLightValues();
    
    /**
     * Get the distance sensor values.
     * 
     * @return  An array containing the sensor values.
     */	
	public List<Integer> getDistanceValues();

    /**
     * Get the state of the gripper turret arm.
     * 
     * @return  The arm state.
     */
    public KheperaConstants.Gripper.ArmState getArmState();
    
    /**
     * Set the state of the gripper turret arm.
     * 
     * @param   armState    [in]    Supplies the arm state.
     */
	public void setArmState(KheperaConstants.Gripper.ArmState armState);
    
    /**
     * Get the state of the gripper turret gripping-claw.
     * 
     * @return  The state of the gripper turret gripping-claw.
     */
    public KheperaConstants.Gripper.GripState getGripperState();
    
    /**
     * Set the state of the gripper turret gripping-claw.
     * 
     * @param   gripState   [in]    Supplies the claw-state.
     */
    public void setGripperState(KheperaConstants.Gripper.GripState gripState);
			
    /**
     * Determine if an object is present in the gripper claw.
     * 
     * @return  True if an object is present, false otherwise.
     */
	public boolean isObjectPresent();
    
    /**
     * Determine if an object is gripped in the gripper claw.
     * 
     * @return  True if an object is held, false otherwise.s
     */
    public boolean isObjectHeld();
    
	/*
	 * Returns the most recent LVT array.
	 */
	public List<Integer> readLvtImage();
    
	/*
	 * Returns the pixel with maximum intensity
	 */
	public double getPixelMaxIntensity();
	
	/*
	 * Returns the pixel with minimum intensity
	 */
	public double getPixelMinIntensity();
}
