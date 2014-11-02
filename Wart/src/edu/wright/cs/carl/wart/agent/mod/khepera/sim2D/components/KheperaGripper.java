
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components;

import java.util.Iterator;
import java.util.Map;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants;
import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.AbstractComponentSim2D;
import edu.wright.cs.carl.wart.sim2D.GrippableSim2D;
import edu.wright.cs.carl.wart.sim2D.ObjectSim2D;
import edu.wright.cs.carl.wart.sim2D.PinPositionSim2D;

import edu.wright.cs.carl.wart.sim2D.models.collision.CollisionConstants;


/**
 * Implementation of a Khepera Gripper turret.
 *
 * @author  Duane Bolick
 */
public class KheperaGripper extends AbstractComponentSim2D
{
    
    private KheperaConstants.Gripper.GripState currentGripState;
    private KheperaConstants.Gripper.GripState nextGripState;    
    private KheperaConstants.Gripper.ArmState currentArmState;
    private KheperaConstants.Gripper.ArmState nextArmState;
    
    private boolean isObjectPresent;
    private GrippableSim2D objectInGrip;
    
    private double intoGripperBearing;
    
    public KheperaGripper(AbsolutePositionSim2D startingPosition)
    {
        super(  startingPosition,
                KheperaConstantsSim2D.ClawLengthInPixels,
                KheperaConstantsSim2D.OpenGripGapInPixels);
        
        //
        // Set the starting state.
        //
        this.currentArmState = KheperaConstants.Gripper.ArmState.DOWN;
        this.nextArmState = KheperaConstants.Gripper.ArmState.DOWN;
        this.currentGripState = KheperaConstants.Gripper.GripState.OPEN;
        this.nextGripState = KheperaConstants.Gripper.GripState.OPEN;
        this.isObjectPresent = false;
        this.objectInGrip = null;
        
        //
        // Calculate the "into gripper" bearings.  This is the maximum angle
        // difference that an object can have from the bearing of the
        // agent and still be allowed into the grip.
        //
        double opposite = KheperaConstantsSim2D.OpenGripGapInPixels/2;
        double adjacent =
                KheperaConstantsSim2D.ArmLengthInPixels -
                KheperaConstantsSim2D.KheperaBodyLengthInPixels/2 +
                KheperaConstantsSim2D.ClawLengthInPixels;
                
                
        this.intoGripperBearing = (double) Math.atan(opposite/adjacent);
    }
    
    public GrippableSim2D getGrippedObject()
    {
        return this.objectInGrip;
    }
    
    /**
     * Check to see if the provided object collides with this object.  A
     * collision implies that two objects are occupying the same area, and
     * they should not be.  This is not necessarily semantically equivalent to
     * "intersects with bounding box of."  If you want to test bounding box
     * intersection, then use the getBoundingBox() methods of the objects in
     * question, and use the Rectangle2D methods to determine intersection.
     * 
     * @param   object      [in]    Supplies the object.
     * 
     * @return  True if the provided object collides with this one.
     */
    @Override
    public boolean collidesWithObject(ObjectSim2D object)
    {
        if(this.getBoundingBox().intersects(object.getBoundingBox())) {
            double objectHeadingInRadians = 
                    CollisionConstants.GetVectorAngleInRadians(
                        this.getAgent().getCenterPoint(),
                        object.getCenterPoint());
            
            double objectBearingInRadians = 
                    CollisionConstants.GetAngleDifferenceInRadians(
                        objectHeadingInRadians,
                        this.getAgent().getAbsolutePosition().angleInRadians);
            
            if( this.getArmState() == KheperaConstants.Gripper.ArmState.DOWN && 
                this.getGripperState() == KheperaConstants.Gripper.GripState.OPEN &&
                this.getBoundingBox().intersects(object.getBoundingBox()) &&
                object instanceof GrippableSim2D &&
                objectBearingInRadians <= this.intoGripperBearing) {
                    return false;                
            }
            
            return true;
        }

        return false;
    }   
    
    /**
     * Returns the bounding box for the object.
     * 
     * @return  The bounding box.
     */
    @Override
    public Rectangle2D getBoundingBox()
    {
        if(this.currentArmState == KheperaConstants.Gripper.ArmState.UP) {
            this.boundingBox.setRect(
                                this.agent.getAbsolutePosition().coordinates.x,
                                this.agent.getAbsolutePosition().coordinates.y,
                                0,
                                0);
        }
        
        else {
            this.boundingBox.setRect(
                                this.position.coordinates.x - this.xDimension/2,
                                this.position.coordinates.y - this.yDimension/2,
                                xDimension,
                                yDimension);
        }
        
        return this.boundingBox;
    }    
    
    
    /**
     * Translate this object in user-space.  The provided changes in x and y
     * coordinates may be negative.
     * 
     * @param   dx  [in]    Supplies the number of pixels to translate this
     *                      object along the x-axis.
     * @param   dy  [in]    Supplies the number of pixels to translate this
     *                      object along the y-axis.
     */
    @Override
    public void translate(double dx, double dy)
    {
        this.position.coordinates.x += dx;
        this.position.coordinates.y += dy;
        
        if(this.isObjectHeld()) {
            this.objectInGrip.gripperMoved();    
        }
    }
    
    /**
     * Rotate this object's center point.
     * 
     * @param   rotationInRadians   [in]    Supplies the rotation in radians.
     */
    @Override
    public void rotate(double rotationInRadians)
    {
        this.position.angleInRadians += rotationInRadians;

        if(this.isObjectHeld()) {
            this.objectInGrip.gripperMoved();
        }
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
     * @param   currentArmState    [in]    Supplies the arm state.
     */
	public void setArmState(KheperaConstants.Gripper.ArmState armState)
    {
        this.nextArmState = armState;
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
     * @param   newGripState   [in]    Supplies the claw-state.
     */
    public void setGripperState(KheperaConstants.Gripper.GripState gripState)
    {
        this.nextGripState = gripState;
    }
			
    /**
     * Determine if an object is present in the gripper claw.
     * 
     * @return  True if an object is present, false otherwise.
     */
	public boolean isObjectPresent()
    {
        return this.isObjectPresent;
    }
    
    /**
     * Determine if an object is gripped in the gripper claw.
     * 
     * @return  True if an object is held, false otherwise.s
     */
    public boolean isObjectHeld()
    {
        if(this.objectInGrip == null) {
            return false;
        }
        
        return true;
    }
    
    
    /**
     * <p>
     * Update the state of this object.  An active object updates itself once
     * per world-update cycle.  Since world-update cycles occur a specified
     * number of times per second, we know the duration of a single update cycle
     * in milliseconds.  Implementations of ActiveSim2D should ensure that all
     * movement/state changes/etc. are, if applicable, executed based on the
     * provided duration.
     * </p>
     * 
     * <p>
     * TODO:  Arm and gripper delay times - they don't open and close
     * instantly on the Khepera, so we should incorporate a time delay here.
     * There are constants for this in
     * <i>edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D</i>
     * called <i>ArmTransitionTimeInMillis</i> and
     * <i>GripTransitionTimeInMillis</i>, which you should use in combination
     * with the <i>UpdatesPerSecond</i> constant in
     * <i>edu.wright.cs.carl.wart.sim2D.ConstantsSim2D</i>.
     * </p>
     * 

     * @param   objects     [in]    Provides a map of the other objects in the
     *                              world.
     * @param timeInMillis  [in]    Supplies the duration of this update cycle
     *                              in milliseconds.
     */
    public void update(Map<String, ObjectSim2D> objects,long timeInMillis)
    {

        if(this.currentArmState != this.nextArmState) {
            
            this.currentArmState = this.nextArmState;
            
            switch(this.currentArmState) {

                // Raising the arm.
                case UP:
                    if(this.isObjectHeld()) {
                        this.objectInGrip.getPinPosition().distanceInPixels =
                          KheperaConstantsSim2D.GrippedObjectArmUpDistanceInPixels;                        
                        this.objectInGrip.gripperMoved();
                    }
                    break;


                // Lowering the arm.
                case DOWN:
                    if(this.isObjectHeld()) {
                        this.objectInGrip.getPinPosition().distanceInPixels =
                            KheperaConstantsSim2D.GrippedObjectArmDownDistanceInPixels;
                        this.objectInGrip.gripperMoved();
                    }      
                    
            } // End switch on arm state.            
        } // End changing arm state.
            
        
        if(this.currentGripState != this.nextGripState) {
            
            switch(this.nextGripState) {
            
                // Closing the grip...
                case CLOSED:

                    // If we're already holding something, don't do anything.
                    if(this.currentGripState == KheperaConstants.Gripper.GripState.HOLDING) {
                        break;
                    }

                    // Otherwise, it depends on the arm state.
                    if(this.currentArmState == KheperaConstants.Gripper.ArmState.UP) {
                        this.currentGripState = KheperaConstants.Gripper.GripState.CLOSED;
                    }
                    else {
                        Iterator<ObjectSim2D> it = objects.values().iterator();
                        ObjectSim2D currentObject = null;
                        boolean grippedSomething = false;
                        
                        while(it.hasNext() && grippedSomething == false) {
                            currentObject = it.next();

                            //
                            // If there's something in our grabbing area,
                            // we can grab, grab it!
                            //
                            if(this.getBoundingBox().contains(currentObject.getCenterPoint())) {
                                if(currentObject instanceof GrippableSim2D) {
                                    GrippableSim2D grippableObject = (GrippableSim2D) currentObject;
                                    this.objectInGrip = grippableObject;

                                    PinPositionSim2D grippedPosition = new PinPositionSim2D();
                                    grippedPosition.angleInRadians = 0;
                                    grippedPosition.distanceInPixels = KheperaConstantsSim2D.GrippedObjectArmDownDistanceInPixels;
                                    grippedPosition.rotationInRadians = currentObject.getAngle() - this.getAngle();

                                    grippableObject.grip(this, grippedPosition);

                                    this.currentGripState = KheperaConstants.Gripper.GripState.HOLDING;
                                    
                                    grippedSomething = true;
                                }
                                else {
                                    this.currentGripState = KheperaConstants.Gripper.GripState.OPEN;
                                    grippedSomething = true;
                                }
                            }
                        } // End iteration over all objects to see if we can grab them.

                        if(grippedSomething == false) {
                            this.currentGripState = KheperaConstants.Gripper.GripState.CLOSED;
                        }
                    } // End else
                    break; // out of case CLOSED:
    
                case OPEN:
                    // Drop object if held
                    if(this.isObjectHeld()) {
                        //
                        // First, make sure we're not dropping it on top of
                        // outselves.  Move it out in front.
                        //
                        this.objectInGrip.getPinPosition().distanceInPixels = 
                                KheperaConstantsSim2D.GrippedObjectArmDownDistanceInPixels;
                        this.objectInGrip.gripperMoved();

                        //
                        // Then drop it.
                        //
                        this.objectInGrip.drop();
                        
                        this.objectInGrip = null;   
                    }
                    
                    this.currentGripState = KheperaConstants.Gripper.GripState.OPEN;
                        
                    break;

            } // End switch on grip state.        
        } // End changing grip state.
        
       
        //
        // Finally, update the is object present sensor.
        //
        this.isObjectPresent = false;
        
        // If we're holding one, it's true.
        if(this.isObjectHeld() == true) {
            this.isObjectPresent = true;
            return;
        }
        
        // If the grip is closed (and therefore not HOLDING), it's false.
        if(this.currentGripState == KheperaConstants.Gripper.GripState.CLOSED) {
            this.isObjectPresent = false;
            return;
        }
        
        // At this point, we know the grip is OPEN.
        
        // If the arm is up and open, it's false.
        if(this.currentArmState == KheperaConstants.Gripper.ArmState.UP) {
            this.isObjectPresent = false;
            return;
        }
        
        // Now we know that the arm is DOWN, and the grip is OPEN.  Check to
        // see if anything's in there.
        Iterator<ObjectSim2D> it = objects.values().iterator();
        ObjectSim2D currentObject = null;
        while(it.hasNext()) {
            currentObject = it.next();

            //
            // If there's something in our grabbing area, it's true!
            //
            if(this.getBoundingBox().contains(currentObject.getCenterPoint())) {
                this.isObjectPresent = true;
                return;
            }
        }        
    }
}
