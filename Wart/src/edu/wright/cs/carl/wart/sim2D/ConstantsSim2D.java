
package edu.wright.cs.carl.wart.sim2D;

import java.awt.Color;


/**
 * This class contains public, static fields (i.e., "constants") specific to the
 * Sim2D simulation model.
 *
 * @author  Duane Bolick
 */
public class ConstantsSim2D
{
    /**
     * Extension of saved Sim2D maps.
     */
    public static String MapFileExtension = "s2d";
    public static String DefaultMapName = "New Sim2D Map";
    
    /**
     * Size of the world in pixels.  The world is always a square.
     */
    public static int DefaultWorldWidthInPixels = 500;
    public static int MinWorldWidthInPixels = 500;
    public static int MaxWorldWidthInPixels = 1000;
    
    /**
     * Defines the amount of real time per "tick" of world's internal clock.
     */
    public static int DefaultUpdatesPerSecond = 25;
    public static int MinUpdatesPerSecond = 20;
    public static int MaxUpdatesPerSecond = 100;    
    
    /**
     * WorldModel constants.
     */
    public static int DefaultOuterWallThicknessInPixels = 20;
    public static int MinObstacleDimensionInPixels = 10;
    
    /**
     * Collision constants.
     */
    public static double CollisionNudgeInPixels = 2;
    
    /**
     * Specific object-type constants.
     */
    public static enum ObjectType
    {
        EMPTY,
        WALL,
        BALL,
        LIGHT,
        CAP,
        AGENT,
        USER_DEFINED
    }
    
    public static int BallDimensionsInPixels = 36;
    public static Color BallColor = Color.BLUE;
    
    public static int CapDimensionsInPixels = 36;
    public static Color CapColor = Color.GREEN;
    
    public static int LightDimensionsInPixels = 30;
    public static int LightAuraDimensionsInPixels = 70;
    public static Color LightColor = Color.getHSBColor(0.15f, 1f, 1f);
    public static Color AuraColor = Color.getHSBColor(0.15f, 0.3f, 1f);
    
    public static Color ObstacleColor = Color.PINK;
    
    public static Color LabelColor = Color.DARK_GRAY;
    
    /**
     * <p>
     * For visualization purposes, defines the number of pixels that represent
     * a millimeter.  All static images used as sprites should be scaled to this
     * as well.
     * </p>
     * 
     * <p>
     * <b>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!! IMPORTANT NOTE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * </b>
     * </p>
     * 
     * <p>
     * Before taking on this next 'TODO,' read and fully understand this:
     * The dimensions of the bounding boxes in the Sim2D models <i>DO NOT NEED
     * TO BE CHANGED</i> in order to implement zoomable graphics!!!  You should
     * only be changing things in the
     * <i>edu.wright.cs.carl.wart.sim2D.visualization</i> package, the
     * objects that implement the <i>DrawableObjectSim2D</i> interface, and the
     * drawable updates.  That having been said,
     * </p>  
     * 
     * <p>
     * <b>TODO:</b> Make the visualizations scalable.  I would need to alter all
     * the measurements, dimensions, etc, of the Drawable objects to be in
     * millimeters, and then use a mutable pixels-per-millimeter variable in
     * the Sim2DViewPanel and all the DrawableSim2D objects.  Also, I'd need to
     * have some way of translating the model coordinates (which are in pixels)
     * to the different scale of the visualization - probably through the
     * drawing updates.
     * </p>
     */
    public static int PixelsPerMillimeter = 1;
}
