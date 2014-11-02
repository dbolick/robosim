
package edu.wright.cs.carl.wart.agent.mod.khepera;

/**
 * 
 *
 * @author Duane
 */
public class KheperaConstants
{
    public static String TYPE = "khepera";
    
    public static class Core
    {
        public static String TYPE_ID = "core";
        
        public static int MOTOR_STOP_SPEED = 0;
        public static int MIN_MOTOR_SPEED = -5;
        public static int MAX_MOTOR_SPEED = 5;
    }
    
    public static class Gripper
    {
        public static String TYPE_ID = "gripper";
        
        public static int TURRET_ID = 1;

        public static int ARM_UP_POS = 180;
        public static int ARM_DOWN_POS = 255;
        public static int ARM_MID_POS = 215;

        public static int GRIP_OPEN_POS = 0;
        public static int GRIP_CLOSED_POS = 1;

        public static enum ArmState
        {
            UP,
            DOWN
        }

        public static enum GripState
        {
            OPEN,
            CLOSED,
            HOLDING
        }
    }
    
    public static class LVT
    {
        public static String TYPE_ID = "lvt";        
        public static int TURRET_ID = 2;
        
    }
            
}
