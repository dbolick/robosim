
package edu.wright.cs.carl.wart.sim2D.models.collision;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Static fields and methods used in collision detection and resolution. 
 *
 * @author  Duane Bolick
 */
public class CollisionConstants
{
    public static int NudgeInPixels = 2;
    
    public static enum CollisionResult
    {
        OBJECT_MOVED,
        OBJECT_STUCK,
        OBSTACLE
    }
    
    public static int GetQuadrant(double angleInRadians)
    {
        return (int) (Math.toDegrees(angleInRadians)) / 90;
    }
    
    public static double GetVectorAngleInRadians(Point2D from, Point2D to)
    {
        return Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
    }
    
    public static double GetVectorAngleInRadians(double xFrom, double yFrom, double xTo, double yTo)
    {
        return Math.atan2(yTo-yFrom, xTo-xFrom);
    }
    
    public static double GetAngleDifferenceInRadians(double a1, double a2)
    {
        //
        // Get the difference.
        //
        double diff = Math.abs(a2 - a1);
        
        //
        // If it's greater than 180 degrees, then go the other way.
        //
        if(diff > Math.PI) {
            diff = (double)(Math.PI * 2f) - diff;
        }
       
        return Math.abs(diff);
    }
    
    public static double GetReflectionAngleInRadians(double surfaceAngleInRadians, double incidentAngleInRadians)
    {
        //
        // Get the reflection angle
        //
        double reflectionAngleInRadians = 2*surfaceAngleInRadians - incidentAngleInRadians;
        
        //
        // Get the modular value, i.e.: (0 <= angle <= 2*Math.PI)
        //
        double modReflectionAngleInRadians = reflectionAngleInRadians % (2*Math.PI);
        
        //
        // Then convert that to the Java coordinate system, in which deflections
        // below the x-axis are negative values, not positive values greater
        // than Math.PI).
        //
        if(modReflectionAngleInRadians > Math.PI) {
            modReflectionAngleInRadians = modReflectionAngleInRadians - (2*Math.PI);
        }
        
        return (double) modReflectionAngleInRadians;
    }
    
    public static double GetOutAngle(int outcode)
    {
        //
        // Get the individual x and y outcodes.  We know it can only
        // be one of each LEFT/RIGHT and TOP/BOTTOM.
        //
        int xOutcode = outcode & (java.awt.Rectangle.OUT_LEFT | java.awt.Rectangle.OUT_RIGHT);
        int yOutcode = outcode & (java.awt.Rectangle.OUT_BOTTOM | java.awt.Rectangle.OUT_TOP);

        //
        // If our x outcode is 0, that means we're either on top, on bottom,
        // or inside.
        //
        if(xOutcode == 0) {
            if(yOutcode == java.awt.Rectangle.OUT_TOP) {
                return (double) Math.PI * (6f/4f);                
            }
            else if(yOutcode == java.awt.Rectangle.OUT_BOTTOM) {
                return (double) Math.PI * (2f/4f); 
            }
            else {
                return -1;
            }
        }
        
        //
        // So we know we have a nonzero x outcode.  Now, if our y outcode is 0,
        // then we're either to the left or the right.
        //
        if(yOutcode == 0) {
            if(xOutcode == java.awt.Rectangle.OUT_LEFT) {
                return (double) Math.PI;                
            }
            else if(xOutcode == java.awt.Rectangle.OUT_RIGHT) {
                return 0;              
            }
        }
        
        //
        // If we got here, then x and y have both nonzero outcodes, so we're in
        // a corner.
        //
        if(yOutcode == java.awt.Rectangle.OUT_TOP) {
            
            if(xOutcode == java.awt.Rectangle.OUT_LEFT) {
                return (double) (Math.PI * (5f/4f));                
            }
            else if(xOutcode == java.awt.Rectangle.OUT_RIGHT) {
                return (double) (Math.PI * (7f/4f));              
            }              
        }
        
        else if(yOutcode == java.awt.Rectangle.OUT_BOTTOM) {
            if(xOutcode == java.awt.Rectangle.OUT_LEFT) {
                return (double) (Math.PI * (3f/4f));                
            }
            else if(xOutcode == java.awt.Rectangle.OUT_RIGHT) {
                return (double) (Math.PI * (1f/4f));              
            }            
        }
        
        throw new AssertionError("edu.wright.cs.carl.wart.sim2D.models.collision.CollisionConstants: Impossible outcode.");        
    }

    public static void GetClosestPointOnRectangle(Rectangle2D rectangle, Point2D point, Point2D nearestPointOnRectangle)
    {
        //
        // Get the upper-left and lower-right corners of the rectangle for
        // convenience, later on in this method.
        //

        double ulcX = rectangle.getX();
        double ulcY = rectangle.getY();
        double lrcX = ulcX + rectangle.getWidth();
        double lrcY = ulcY + rectangle.getHeight();
        
        //
        // First, get the outcode.  See java.awt.Rectangle for more info. 
        //
        int outcode = rectangle.outcode(point);

        //
        // Now, get the individual x and y outcodes.  We know it can only
        // be one of each LEFT/RIGHT and TOP/BOTTOM.
        //
        int xOutcode = outcode & (java.awt.Rectangle.OUT_LEFT | java.awt.Rectangle.OUT_RIGHT);
        int yOutcode = outcode & (java.awt.Rectangle.OUT_BOTTOM | java.awt.Rectangle.OUT_TOP);
        
        switch(xOutcode)
        {
            //
            // If we're to the left of the rectangle...
            //
            case java.awt.Rectangle.OUT_LEFT:
                switch(yOutcode)
                {
                    case java.awt.Rectangle.OUT_BOTTOM:
                        //
                        // Bottom-left, so it's the corner.
                        //
                        
                        //nearestPointOnRectangle.setLocation(lrcX, lrcY);
                        nearestPointOnRectangle.setLocation(ulcX, lrcY);
                        return;
                        
                    case java.awt.Rectangle.OUT_TOP:
                        //
                        // Top-left, so it's that corner.
                        //
                        nearestPointOnRectangle.setLocation(ulcX, ulcY);
                        return;
                        
                    case 0:
                        //
                        // Just to the left.
                        //
                        nearestPointOnRectangle.setLocation(ulcX, point.getY());
                        return;
                }
                break;
               
            //
            // If we're to the right of the rectangle...
            //
            case java.awt.Rectangle.OUT_RIGHT:
                switch(yOutcode)
                {
                    case java.awt.Rectangle.OUT_BOTTOM:
                        //
                        // Bottom-right, so it's the corner.
                        //
                        nearestPointOnRectangle.setLocation(lrcX, lrcY);         
                        return;
                        
                    case java.awt.Rectangle.OUT_TOP:
                        //
                        // Top-right, so it's that corner.
                        //
                        nearestPointOnRectangle.setLocation(lrcX, ulcY);  
                        return;
                        
                    case 0:
                        //
                        // Just to the right.
                        //
                        nearestPointOnRectangle.setLocation(lrcX, point.getY());
                        return;        
                }                
                break;
                
            //
            // Finally, if we're not to the right, or the left...
            //
            case 0:
                switch(yOutcode)
                {
                    case java.awt.Rectangle.OUT_BOTTOM:
                        //
                        // Just plain Bottom.
                        //
                        nearestPointOnRectangle.setLocation(point.getX(), lrcY); 
                        return;
                        
                    case java.awt.Rectangle.OUT_TOP:
                        //
                        // Just plain top.
                        //
                        nearestPointOnRectangle.setLocation(point.getX(), ulcY); 
                        return;
                        
                    case 0:
                        //
                        // Inside!?!?!?  This is just too weird...
                        //
                        nearestPointOnRectangle.setLocation(point.getX(), point.getY()); 
                        return;
                }      
        } // End outer switch on xOutcode.
        
        //
        // If we get here, something's wrong.
        //
        assert(false);
    }
}
