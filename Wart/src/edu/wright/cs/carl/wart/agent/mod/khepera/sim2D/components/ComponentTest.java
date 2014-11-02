/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.components;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;

/**
 * 
 *
 * @author Duane
 */
public class ComponentTest
{
    public static void main(String[] args)
    {
        KheperaMotor m = new KheperaMotor();
        m.setLeftWheelSpeed(0);
        m.setRightWheelSpeed(5);
        
        AbsolutePositionSim2D p1 = new AbsolutePositionSim2D();
        AbsolutePositionSim2D p2 = new AbsolutePositionSim2D();
        
        p1.angleInRadians = (double) Math.toRadians(0);
        p1.coordinates.x = 100;
        p1.coordinates.y = 100;
        
        m.move(p1, p2, 100);
        
        System.out.println("X: " + p2.coordinates.x + "  Y: " + p2.coordinates.y + "  Angle:" + Math.toDegrees(p2.angleInRadians));
    }
}
