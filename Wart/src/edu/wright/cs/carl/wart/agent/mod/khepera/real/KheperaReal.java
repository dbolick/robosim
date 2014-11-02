
package edu.wright.cs.carl.wart.agent.mod.khepera.real;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

import edu.wright.cs.carl.comm.Communicator;
import edu.wright.cs.carl.comm.CommEventListener;
import edu.wright.cs.carl.comm.SynchronousWrite;


import edu.wright.cs.carl.wart.agent.Agent;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;
import edu.wright.cs.carl.wart.agent.mod.khepera.Utils;
import edu.wright.cs.carl.wart.agent.mod.khepera.interfaces.KheperaStudent;

import edu.wright.cs.carl.wart.real.RealAgent;

import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D;

/**
 * This class represents the interface to a real Khepera.
 *
 * @author  Duane Bolick
 */
public class KheperaReal implements Agent, RealAgent, KheperaStudent, Runnable, CommEventListener
{
    public static long ResponseProcessingWaitInMillis = 40;
    
    private Communicator comm = null;
    
    private String name;
    private String description = "Standard Khepera II with Gripper and Linear Vision Turret.";
    
    private int leftMotorSpeed = 0;
    private int rightMotorSpeed = 0;
    
    private List<Integer> distanceValues;
    private List<Integer> lightValues;
    private List<Integer> lvtValues;
    private int lvtPixelMax;
    private int lvtPixelMin;
    
    private KheperaConstants.Gripper.ArmState currentArmState;
    private KheperaConstants.Gripper.GripState currentGripState;
    private boolean objectPresent;
    private boolean objectHeld;
    
    private Thread updateThread;
    private boolean stopRequested = true;
    
    private Queue<String> responseQueue;
    private StringBuffer currentResponse;
    private boolean responseComplete;
    
    /**
     * Construct a Khepera with the default description.
     * 
     * @param   name    [in]    Supplies the name.
     */
    public KheperaReal(String name)
    {
        this.name = name;
        this.distanceValues = new ArrayList<Integer>();
    }
    
    /**
     * Construct a Khepera.
     * 
     * @param   name        [in]    Supplies the name of the Khepera.
     * @param   description [in]    Supplies the description of this Khepera.
     */
    public KheperaReal(String name, String description)
    {
        this.name = name;
        this.description = description;
    }    

    /**
     * Given an open Communicator, determine if that Communicator is connected
     * to an agent of this type.
     * 
     * @param   comm    [in]    Supplies the Communicator.
     * 
     * @return  True if the Communicator is connected to an Agent of this type,
     *          false otherwise.
     */
    public boolean isAgentType(Communicator comm) throws IOException
    {
        SynchronousWrite sw = new SynchronousWrite(comm);
        String response = sw.syncWrite("B\n");
        if(response.charAt(0) == 'b') {
            return true;
        }
        
        return false;
    }
    
    /**
     * Set the Communicator.
     * 
     * @param   comm    [in]    Supplies the Communicator that is connected to
     *                          an agent of this type.
     */
    public void setCommunicator(Communicator comm)
    {
        this.comm = comm;
        this.comm.addCommEventListener(this);
    }
    
    /**
     * Start the agent.  If a communicator has not been set, this method does
     * nothing.
     */
    public void start()
    {
        if(this.comm == null) {
            return;
        }
        
        //
        // Initialize the response queue and related fields.
        //
        this.responseQueue = new LinkedList<String>();
        this.responseComplete = false;
        this.currentResponse = new StringBuffer();        
        
        //
        // Initialize the sensors, in case anyone asks for sensor values before
        // we've received ones from the actual Khepera.  We'll initialize them
        // all to the "nothing sensed" values to start off with.
        //
        this.lightValues = new ArrayList<Integer>();
        this.distanceValues = new ArrayList<Integer>();
        this.lvtValues = new ArrayList<Integer>();
        
        for(int i=0; i<KheperaConstantsSim2D.NumSensors; ++i) {
            this.lightValues.add(KheperaConstantsSim2D.LightValueMax);
            this.distanceValues.add(KheperaConstantsSim2D.DistanceValueMin);
        }
        
        for(int i=0; i<KheperaConstantsSim2D.LVTNumPixels; ++i) {
            this.lvtValues.add(KheperaConstantsSim2D.LVTPixelMin);
        }
        
        this.lvtPixelMax = KheperaConstantsSim2D.LVTPixelMin;
        this.lvtPixelMin = KheperaConstantsSim2D.LVTPixelMin;
        
        //
        // Put the Khepera into a resting position.  This initializes the
        // gripper and arm states, and the wheel speeds.
        //
        this.rest();
        
        //
        // Initialize the object present and held flags.
        //
        this.objectHeld = false;
        this.objectPresent = false;
        
        //
        // Now all the state variables are initialized, so start the update
        // thread.
        //
        this.stopRequested = false;
        this.updateThread = new Thread(this);
        this.updateThread.start();
    }
    
    /**
     * Stop the agent.  If the agent has not been started, this method does
     * nothing.
     */
    public void stop()
    {
        this.stopRequested = true;
        try {
            this.updateThread.join();
        }
        catch(InterruptedException e) {
            
        }
    }
    
    public void run()
    {
        while(this.stopRequested == false) {            
            while(this.responseQueue.isEmpty() == false) {
                this.processResponse(this.responseQueue.poll());
            }
            
            try {
                Thread.sleep(KheperaReal.ResponseProcessingWaitInMillis);
            }
            catch(InterruptedException e) {
                
            }
            
        }
        
        this.rest();
    }
    
    protected synchronized void processResponse(String response)
    {
        if(response == null) {
            return;
        }

        if(response.length() < 2) {
            return;
        }
       
        //
        // Parse a turret command.
        //
        if(response.charAt(0) == 't') {
            
            //
            // Gripper turret.
            //
            if(response.charAt(2) == '1') {
                if(response.charAt(4) == 'g') {
                    if(response.charAt(6) == '0') {
                       this.objectPresent = false;
                    }
                    else {
                        this.objectPresent = true;
                    }
                    return;
                }
                return;
            } // End Gripper Turret.

            //
            // Linear Vision Turret.
            //
            if(response.charAt(2) == '2') {
                if(response.charAt(4) == 'n') {
                    this.lvtValues = Utils.ParseResultList(response, "n", 64);
                    return;
                }
                if(response.charAt(4) == 'o') {
                    this.lvtPixelMax = response.charAt(5);
                    return;
                }
                if(response.charAt(4) == 'n') {
                    this.lvtPixelMin = response.charAt(5);
                    return;
                }
                return;
            } // End LVT.   
            return;
        } // End extension turrets.
        
        //
        // Parse light values.
        //
        if(response.charAt(0) == 'o') {           
            this.lightValues = Utils.ParseResultList(response, "o", 8);
            return;
        }
        
        //
        // Parse distance values.
        //
        if(response.charAt(0) == 'n') {            
            this.distanceValues = Utils.ParseResultList(response, "n", 8);
            return;
        }
    }
    
    /**
     * Receive an event from a <i>Communicator</i>.
     * 
     * @param   evt     [in]    Supplies the event.
     */
    public void receiveCommEvent(Object event)
    {        
        if(event == null) {
            return;
        }
        
        if(event instanceof String == false) {
            return;
        }
        
        String message = (String)event;
        
        //
        // Now, we're not sure if this message is an entire response (i.e., a
        // '\n'-terminated string.  So check to see if there's an endline ('\n')
        // in there...
        //
        int responseEndline = message.indexOf('\n');
       
        //
        // As long as there's another endline (i.e., the end of a response...)
        //
        while(responseEndline >= 0) {
        
            //
            // Append the end to the current message, and then add that message
            // to the processing queue.
            //
            this.currentResponse.append(message.substring(0, responseEndline + 1));
            this.responseQueue.add(this.currentResponse.toString());
           
            //
            // Then clear the current response, and shift the incoming message
            // to exclude the consumed characters.
            //
            this.currentResponse.delete(0, this.currentResponse.length());
            message = message.substring(responseEndline + 1);
            
            
            responseEndline = message.indexOf('\n');
        }
         
        //
        // So now we've got part of a message that doesn't terminate in an
        // endline, so append that to the current message buffer.
        //
        this.currentResponse.append(message);
    }
    
    /**
     * Set the name of this agent.
     * 
     * @param   newName     [in]    Supplies the new name. 
     */
    public void setName(String newName)
    {
        this.name = newName;
    }
    
    /**
     * Get the agent's name.  Agent name is used for identification purposes,
     * and should be unique (one unique name per entity) in a given context.
     * 
     * @return  The agent's name.
     */
    public String getName()
    {
        return this.name;
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
        this.setMotorSpeeds(KheperaConstants.Core.MOTOR_STOP_SPEED, KheperaConstants.Core.MOTOR_STOP_SPEED);
        this.setGripperState(KheperaConstants.Gripper.GripState.OPEN);
        this.setArmState(KheperaConstants.Gripper.ArmState.DOWN);
    }
    
    /**
     * Set the motor speeds.
     * 
     * @param   left    [in]    Supplies the desired left motor speed.
     * @param   right   [in]    Supplies the desired right motor speed.
     */
    public void setMotorSpeeds(int left, int right)
    {
        this.leftMotorSpeed = left;
        this.rightMotorSpeed = right;
        
        try {
            this.comm.write("D" + "," + this.leftMotorSpeed + "," + this.rightMotorSpeed + "\n");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Set the left motor speed.
     * 
     * @param   speed   [in]    Supplies the motor speed.
     */
    public void setLeftMotorSpeed(int speed)
    {
        this.leftMotorSpeed = speed;
        this.setMotorSpeeds(this.leftMotorSpeed, this.rightMotorSpeed);
    }

    /**
     * Set the right motor speed.
     * 
     * @param   speed   [in]    Supplies the motor speed.
     */    
    public void setRightMotorSpeed(int speed)
    {
        this.rightMotorSpeed = speed;
        this.setMotorSpeeds(this.leftMotorSpeed, this.rightMotorSpeed);
    }
    
    /**
     * Get the light sensor values.
     * 
     * @return  A list containing the sensor values.
     */
	public List<Integer> getLightValues()
    {
        try {
            this.comm.write("O\n");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return this.lightValues;       
    }
    
    /**
     * Get the distance sensor values.
     * 
     * @return  An array containing the sensor values.
     */	
	public List<Integer> getDistanceValues()
    {   
        try {
            this.comm.write("N\n");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return this.distanceValues;
    }
    
    /**
     * Get the state of the gripper turret arm.
     * 
     * @return  The arm state.
     */
    public KheperaConstants.Gripper.ArmState getArmState()
    {
        return this.currentArmState;
    }
    
    /**
     * Set the state of the gripper turret arm.
     * 
     * @param   armState    [in]    Supplies the arm state.
     */
	public void setArmState(KheperaConstants.Gripper.ArmState armState)
    {
        if(armState != this.currentArmState) {
            try {
                if(armState == KheperaConstants.Gripper.ArmState.UP) {
                    this.comm.write(gripperCommand("E," + KheperaConstants.Gripper.ARM_UP_POS));
                }
                else{
                    this.comm.write(gripperCommand("E," + KheperaConstants.Gripper.ARM_DOWN_POS));
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            this.currentArmState = armState;
        }
    }
    
    /**
     * Get the state of the gripper turret gripping-claw.
     * 
     * @return  The state of the gripper turret gripping-claw.
     */
    public KheperaConstants.Gripper.GripState getGripperState()
    {
        return this.currentGripState;        
    }
    
    /**
     * Set the state of the gripper turret gripping-claw.
     * 
     * @param   gripState   [in]    Supplies the claw-state.
     */
    public void setGripperState(KheperaConstants.Gripper.GripState gripState)
    {
        if(gripState != this.currentGripState) {
            try {
                if(gripState == KheperaConstants.Gripper.GripState.OPEN) {
                    this.comm.write(gripperCommand("D," + KheperaConstants.Gripper.GRIP_OPEN_POS));
                }
                else{
                    this.comm.write(gripperCommand("D," + KheperaConstants.Gripper.GRIP_CLOSED_POS));
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            this.currentGripState = gripState;
        }
    }
			
    /**
     * Determine if an object is present in the gripper claw.
     * 
     * @return  True if an object is present, false otherwise.
     */
	public boolean isObjectPresent()
    {
        try {
            this.comm.write(gripperCommand("G"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return this.objectPresent;       
    }
    
    public boolean isObjectHeld()
    {
        if(this.isObjectPresent() && (this.getGripperState() == KheperaConstants.Gripper.GripState.CLOSED)) {
            return true;
        }
        
        return false;        
    }
	
    /**
     * Returns the most recent lvt array.
     * 
     * @return  The most recent lvt array.
     */
	public List<Integer> readLvtImage()
    {
        try {
            this.comm.write(this.LVTCommand("N"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return this.lvtValues;        
    }
    
    /**
     * Get the pixel with the maximum intensity.
     * 
     * @return  The pixel with the maximum intensity.
     */
	public double getPixelMaxIntensity()
    {
        try {
            this.comm.write(this.LVTCommand("O"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
     
        return this.lvtPixelMax;          
    }
	
    /**
     * Get the pixel with the minimum intensity.
     * 
     * @return  The pixel with the minimum intensity.
     */
	public double getPixelMinIntensity()
    {
        try {
            this.comm.write(this.LVTCommand("P"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return this.lvtPixelMin;              
    }

    /**
     * Get the String prefix for all commands to the gripper turret.
     * 
     * @return
     */
    private String gripperPrefix()
    {
        return "T," + KheperaConstants.Gripper.TURRET_ID + ",";
    }
    
    /**
     * Get a complete gripper turret gripperCommand, including the appropriate turret
     * prefix and any necessary gripperCommand string suffixes (i.e., a newline).
     * 
     * @param   gripperCommand     [in]    Supplies the gripperCommand.
     * 
     * @return  The complete gripper turret gripperCommand.
     */
    private String gripperCommand(String command)
    {
        return this.gripperPrefix() + command + "\n";
    }    
    
    /**
     * Get the String prefix for all commands to the linear vision turret.
     * 
     * @return
     */
    private String LVTPrefix()
    {
        return "T," + KheperaConstants.LVT.TURRET_ID + ",";
    }
    
    /**
     * Get a complete gripper turret LVTCommand, including the appropriate turret
     * prefix and any necessary LVTCommand string suffixes (i.e., a newline).
     * 
     * @param   LVTCommand     [in]    Supplies the LVTCommand.
     * 
     * @return  The complete gripper turret LVTCommand.
     */
    private String LVTCommand(String command)
    {
        return this.LVTPrefix() + command + "\n";
    }    
        
}
