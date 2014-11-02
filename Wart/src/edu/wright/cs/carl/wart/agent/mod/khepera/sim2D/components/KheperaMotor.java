
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;

import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.KheperaConstantsSim2D;


/**
 * KheperaMotor represents the Khepera's mobility component.
 * 
 * @author  Duane Bolick
 */
public class KheperaMotor implements java.io.Serializable
{
    private int leftWheelSpeed;
    private int rightWheelSpeed;
    
    
    public KheperaMotor()
    {
        this.leftWheelSpeed = 0;
        this.rightWheelSpeed = 0;
    }
    
    /**
     * Move the given position, based on the current wheel speed settings, for
     * the given number of milliseconds.
     * 
     * TODO:  Since there are only 11 possible states for each wheel, re-do
     * the whole movement thing to use a pre-calculated lookup table.
     * 
     * @param   startingPosition    [in]    Supplies the starting position.
     * @param   nextPosition        [out]   Receives the new position.
     * @param   millis              [in]    Supplies the number of milliseconds
     *                                      to move.
     */
    public void move(AbsolutePositionSim2D startingPosition, AbsolutePositionSim2D nextPosition, long millis)
    {
        //
        // Calculate the distance per speed unit.
        //
        double distanceInPixelsPerSpeedUnit =
                KheperaConstantsSim2D.WheelMovementPerSecondPerSpeedUnitInMillimeters /
                (1000 / millis) *
                ConstantsSim2D.PixelsPerMillimeter;

        
        //
        // Get the difference in wheel speeds.  This defines the angle of
        // rotation.  In our case, we define clockwise axle rotation to be
        // negative.
        //
        int speedDiff = this.leftWheelSpeed - this.rightWheelSpeed;

        
        //
        // Calculate the angle of rotation.
        //
        double angleOfRotation = (double) Math.atan(speedDiff * distanceInPixelsPerSpeedUnit /KheperaConstantsSim2D.AxleDiameterInPixels);
   
        
        //
        // Now figure out the radius of the rotation circle - this is the
        // distance from the axle midpoint to the intersection of the line
        // defined by the old axle and the line defined by the new axle.
        //
        double radiusInAxleLengths;
        double radiusInPixels;
        int minSpeed = Math.min(Math.abs(this.rightWheelSpeed), Math.abs(this.leftWheelSpeed));

        if(speedDiff == 0) {
            nextPosition.angleInRadians = startingPosition.angleInRadians;
            double dx = distanceInPixelsPerSpeedUnit *  minSpeed * (double) Math.cos(nextPosition.angleInRadians);
            double dy = distanceInPixelsPerSpeedUnit *  minSpeed * (double) Math.sin(nextPosition.angleInRadians);
            
            nextPosition.coordinates.x = startingPosition.coordinates.x + dx;
            nextPosition.coordinates.y = startingPosition.coordinates.y + dy;
            return;
        }
        
        else if(this.rightWheelSpeed * this.leftWheelSpeed < 0) {
            radiusInAxleLengths = 0.5f - (minSpeed / speedDiff);
        }
        
        else {
            radiusInAxleLengths = 0.5f + (minSpeed / speedDiff);
        }

        radiusInPixels = radiusInAxleLengths * KheperaConstantsSim2D.AxleDiameterInPixels;

        
        //
        // Next calculate the chord length.
        //
        double chordLength = (double) (2 * radiusInPixels * Math.sin(angleOfRotation / 2));
        
        
        //
        // Now we have the chord length, which is how far the center point of
        // the axle moves, and we have the rotation angle - that's how
        // much it rotates.  Now, just update the nextPosition coordinates and
        // angle based on this.
        //
        nextPosition.angleInRadians = startingPosition.angleInRadians + angleOfRotation;
        
        double dx = chordLength * (double) Math.cos(nextPosition.angleInRadians);
        double dy = chordLength * (double) Math.sin(nextPosition.angleInRadians);
        
        nextPosition.coordinates.x = startingPosition.coordinates.x + dx;
        nextPosition.coordinates.y = startingPosition.coordinates.y + dy;
    }
    
    public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed)
    {
        this.leftWheelSpeed = leftWheelSpeed;
        this.rightWheelSpeed = rightWheelSpeed;
    }

    public void setLeftWheelSpeed(int leftWheelSpeed)
    {
        this.leftWheelSpeed = leftWheelSpeed;
    }
    
    public int getLeftWheelSpeed()
    {
        return this.leftWheelSpeed;
    }
    
    public void setRightWheelSpeed(int rightWheelSpeed)
    {
        this.rightWheelSpeed = rightWheelSpeed;
    }    
    
    public int getRightWheelSpeed()
    {
        return this.rightWheelSpeed;
    }
    
    
}
