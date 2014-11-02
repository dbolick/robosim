
package edu.wright.cs.carl.wart.agent.mod.khepera.controllers;

import java.util.ArrayList;
import java.util.List;

import edu.wright.cs.carl.wart.agent.controller.AbstractAgentController;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;

import edu.wright.cs.carl.wart.agent.mod.khepera.interfaces.KheperaStudent;


/**
 * Sample control algorithm class.  You must specify the required control
 * interface in the constructor, as shown here and cast the provided interface
 * to the required type in the initialize method.  Your control logic goes in
 * the doWork method.
 *
 * @author  Duane Bolick
 */
public class Avoider extends AbstractAgentController
{
    private KheperaStudent khepera;
    private List<Integer> distanceValues;
    
    public Avoider()
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
        return "This controller allows a Khepera to move about, avoiding things.";
    }
    
    public void initialize()
    {
        this.khepera = (KheperaStudent)this.agent;
    }
    
    public void doWork()
    {        
        if(khepera.getArmState() == KheperaConstants.Gripper.ArmState.DOWN) {
            khepera.setArmState(KheperaConstants.Gripper.ArmState.UP);
        }
        
        this.distanceValues.clear();
        this.distanceValues.addAll(this.khepera.getDistanceValues());
        
        if (getDistanceValue(3) > 400) {
            this.khepera.setMotorSpeeds(-2, 2);
        }
        else if (getDistanceValue(4) > 400) {
            this.khepera.setMotorSpeeds(-3, 3);
        }
        else  if (getDistanceValue(5) > 400) {
            this.khepera.setMotorSpeeds(-3, 3);
        }
        else {
            this.khepera.setMotorSpeeds(5, 5);
        }

     
    }
    
    private int getDistanceValue(int sensorNumber) {
        return this.distanceValues.get(sensorNumber);
    }
}
