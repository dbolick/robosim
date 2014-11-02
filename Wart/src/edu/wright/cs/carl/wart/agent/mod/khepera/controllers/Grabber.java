
package edu.wright.cs.carl.wart.agent.mod.khepera.controllers;

import java.util.ArrayList;
import java.util.List;

import edu.wright.cs.carl.wart.agent.controller.AbstractAgentController;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;

import edu.wright.cs.carl.wart.agent.mod.khepera.interfaces.KheperaStudent;

/**
 * 
 *
 * @author Duane
 */
public class Grabber extends AbstractAgentController
{
        
    private KheperaStudent khepera;
    private List<Integer> distanceValues;
    
    public Grabber()
    {
        this.distanceValues = new ArrayList<Integer>();
    }
    
    /**
     * Get a description of what the controller is intended to do.
     * 
     * @return The controller description.
     */
    public String getControllerDescription()
    {
        return "This controller grabs something.";
    }
    
    public void initialize()
    {
        this.khepera = (KheperaStudent)this.agent;
    }
    
    public void doWork()
    {
        if(!this.khepera.isObjectHeld()) {
            this.khepera.setArmState(KheperaConstants.Gripper.ArmState.DOWN);
            this.khepera.setGripperState(KheperaConstants.Gripper.GripState.OPEN);
            this.khepera.setMotorSpeeds(1, 1);
            if(this.khepera.isObjectPresent()) {
                this.khepera.setMotorSpeeds(0, 0);
                this.khepera.setGripperState(KheperaConstants.Gripper.GripState.CLOSED);
                try {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e) {

                }
            }
        }
        
        else {
            if(this.khepera.isObjectHeld()) {
                khepera.setArmState(KheperaConstants.Gripper.ArmState.UP);
            }
            
            this.distanceValues.clear();
            this.distanceValues.addAll(this.khepera.getDistanceValues());

            /*
            if (getDistanceValue(2) > 300) this.khepera.setMotorSpeeds(2, -3); else
            if (getDistanceValue(3) > 300) this.khepera.setMotorSpeeds(-3, 2); else
            if (getDistanceValue(1) > 300) this.khepera.setMotorSpeeds(3, -3); else
            if (getDistanceValue(4) > 300) this.khepera.setMotorSpeeds(-3, 3); else
            if (getDistanceValue(0) > 300) this.khepera.setMotorSpeeds(3, -3); else
            if (getDistanceValue(5) > 300) this.khepera.setMotorSpeeds(-3, 3); else        
            */
        
            this.khepera.setMotorSpeeds(3, 2);
        }
        

    }
    
    private int getDistanceValue(int sensorNumber) {
        return this.distanceValues.get(sensorNumber);
    }
}