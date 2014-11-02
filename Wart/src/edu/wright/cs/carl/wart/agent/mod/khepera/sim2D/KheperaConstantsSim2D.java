
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D;


/**
 * Constants for the Khepera models in the <i>edu.wright.cs.carl.wart.sim2D</i>
 * engine.
 *
 * @author  Duane Bolick
 */
public class KheperaConstantsSim2D implements java.io.Serializable
{
    //
    // Khepera body size constants.
    //
    public static int KheperaBodyWidthInPixels = 70;
    public static int KheperaBodyLengthInPixels = 70;
    
    //
    // Khepera motor constants.
    //
    public static double AxleDiameterInPixels = 70;
    public static double WheelMovementPerSecondPerSpeedUnitInMillimeters = 8;
    
    //
    // Khepera sensor constants.
    //
    public static int NumSensors = 8;
    
    public static double SensorRelativeDistanceInPixels = 32;
    
    public static double[] SensorRelativeAngleInRadians =
    {
        (double) Math.toRadians(-75),
        (double) Math.toRadians(-45),
        (double) Math.toRadians(-10),
        (double) Math.toRadians(10),
        (double) Math.toRadians(45),
        (double) Math.toRadians(75),
        (double) Math.toRadians(-170),
        (double) Math.toRadians(170)
        /*
        (double) Math.toRadians(-75),
        (double) Math.toRadians(-45),
        (double) Math.toRadians(-10),
        (double) Math.toRadians(10),
        (double) Math.toRadians(45),
        (double) Math.toRadians(75),
        (double) Math.toRadians(-170),
        (double) Math.toRadians(-170)
         * */
    };
    
    public static double[] SensorFacingAngleInRadians =
    {   (double) Math.toRadians(-90),
        (double) Math.toRadians(-45),
        (double) Math.toRadians(0),
        (double) Math.toRadians(0),
        (double) Math.toRadians(45),
        (double) Math.toRadians(90),
        (double) Math.toRadians(-180),
        (double) Math.toRadians(-180)        
    };    

    //
    // These values determine the simulated
    //
    public static int[] DistanceValues = {1100, 1000, 725, 575, 400, 350, 200};
    public static int DistanceValuesIntervalInMM = 10;
    public static int DistanceValueMin = 100;
    public static int DistanceValueMax = 1100;
    public static int MaxDistanceSensorRangeInMillimeters = 60;
    
    
    //
    // I estimated the amount of percent decrease-per-degree, and it was about
    // 1.45% per degree off from center (0.0145).  So I multiplied that by 
    // degrees-per-radian (which is 57.30) to get this value:
    //
    public static double DistanceValueDecreaseFactorPerRadian = 0.83085f;
    
    //
    // Lowering this value makes sensors more sensitive when sensing things at
    // an angle - for example, this will make them more sensitive.
    //
    //public static double DistanceValueDecreaseFactorPerRadian = 0.63085f;
    
    
    //
    // This value sets the maximum angle range of a sensor's ability to detect
    // objects.  If a detected object is outside this bearing, it won't 
    // register on the sensor.  This is a +/- range, so 4f * PI is about 45 deg
    // to either side from the absolute heading of the sensor.
    //
    public static double DistanceSensorMaxBearingInRadians = (double)Math.PI / 4f;
    
    //
    // An integer between +/- DistanceSensorFuzz will be added to each sensor reading
    // to simulate imperfect sensors.
    //
    public static int DistanceSensorFuzz = 20;
    
    
    public static int[] LightValues = {75, 200, 350, 400, 425, 500};
    public static int LightValuesIntervalInMM = 200;
    public static int LightValueMax = 500;    
    public static int MaxLightSensorRangeInMillimeters = 1000;
    
    // I estimated the amount of percent decrease-per-degree, and it was about
    // 1.45% per degree off from center.  So I multiplied that by 
    // degrees-per-radian (which is 57.30) to get this value:
    public static double LightValueDecreaseFactorPerRadian = 0f;
    public static double LightSensorMaxBearingInRadians = (double)Math.PI / 4;
    
    // An integer between +/- LightSensorFuzz will be added to each sensor
    // reading to simulate imperfect sensors.
    public static int LightSensorFuzz = 20;    
    
    
    
    //
    // Khepera Gripper arm constants.
    //
    public static int GripTransitionTimeInMillis = 100;
    public static int ArmTransitionTimeInMillis = 100;
            
    public static int ClawLengthInPixels = 52;
    public static int ClawThicknessInPixels = 10;
    public static int OpenGripGapInPixels = 44;
    public static int HoldingGripGapInPixels = 30;
    public static int ClosedGripGapInPixels = 2;
    
    public static int ArmLengthInPixels = 76;
    public static int ArmSpanInPixels = 82;
    
    //
    // Constants for the relative positioning of a gripped object.
    //
    public static int GrippedObjectArmDownDistanceInPixels = 10;
    public static double GrippedObjectArmDownAngleInRadians = 0;
    
    public static int GrippedObjectArmUpDistanceInPixels = -30;
    public static double GrippedObjectArmUpAngleInRadians = 0;
    
    
    //
    // Khepera LVT constants.
    //
    public static int LVTNumPixels = 64;
    public static int LVTPixelMax = 255;
    public static int LVTPixelMin = 0;
}
