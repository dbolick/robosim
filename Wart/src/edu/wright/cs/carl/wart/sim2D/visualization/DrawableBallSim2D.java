
package edu.wright.cs.carl.wart.sim2D.visualization;

import java.io.IOException;

import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;


/**
 * This is the drawable representation of a Ball.
 *
 * @author  Duane Bolick
 */
public class DrawableBallSim2D implements DrawableObjectSim2D
{
    protected String objectID;
    
    protected PositionUpdateSim2D currentPosition;
    
    private BufferedImage image;
    
    private int centerX = ConstantsSim2D.BallDimensionsInPixels/2;
    private int centerY = ConstantsSim2D.BallDimensionsInPixels/2;
    
    private boolean isFirstRendering = true;
    private boolean receivedUpdate = false;    
    
    public DrawableBallSim2D(String objectID)
    {
        this.objectID = objectID;
        this.currentPosition = new PositionUpdateSim2D();
    }
    
    /**
     * Get the unique ID of this drawable object.  This should be the same
     * ID as the corresponding Sim2DObject
     * 
     * @return  The unique ID.
     */
    public String getID()
    {
        return this.objectID;
    }
    
    /**
     * Since an instance of this drawable will be sent over the wire to all
     * remote clients, we don't want to send the entire buffered images.
     * The remote clients need to call this before they use it.
     */
    public void loadImages()
    {
        if(this.isFirstRendering == false) {
            return;
        }
        
        this.isFirstRendering = false;
        
        try {
            this.image = ImageIO.read(this.getClass().getResource("images/ball.png"));           
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
                
    }    
    
    
    /**
     * Send an update to the object.
     * 
     * @param   update  [in]    Supplies the update.
     */
    public void update(Object update)
    {
        PositionUpdateSim2D positionUpdate = (PositionUpdateSim2D)update;
        this.currentPosition.position.coordinates.x = positionUpdate.position.coordinates.x;
        this.currentPosition.position.coordinates.y = positionUpdate.position.coordinates.y;
        this.currentPosition.position.angleInRadians = positionUpdate.position.angleInRadians;
        this.receivedUpdate = true;
    }
    
    /**
     * Draw this component on the provided Graphics2D object.
     * 
     * @param   g       [in]    Supplies the graphics object.
     * @param   update  [in]    Supplies an update to the drawable.
     */
    public void render(Graphics2D g)
    {
        //
        // If we haven't received our first update yet, don't try to render.
        //
        if(this.receivedUpdate == false) {
            return;
        }
        
        //
        // If the images haven't been loaded by now, load them.
        //
        if(this.isFirstRendering) {
            this.loadImages();
        }
        
        
        //
        // Draw the image.
        //
        AffineTransform lastTransform = g.getTransform();
        
        g.rotate(   this.currentPosition.position.angleInRadians,
                    this.currentPosition.position.coordinates.x,
                    this.currentPosition.position.coordinates.y);
        

        g.drawImage(this.image,
                    (int) this.currentPosition.position.coordinates.x - centerX,
                    (int) this.currentPosition.position.coordinates.y - centerY,
                    null);

        g.setTransform(lastTransform); 
     
    }    
}
