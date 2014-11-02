
package edu.wright.cs.carl.wart.agent.mod.khepera.sim2D;

import java.io.IOException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import java.awt.image.BufferedImage;

import java.util.Iterator;

import javax.imageio.ImageIO;

import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;

import edu.wright.cs.carl.wart.sim2D.visualization.DrawableObjectSim2D;


/**
 * Represents a Khepera robot that can be drawn on a Graphics2D object.
 *
 * @author  Duane Bolick
 */
public class DrawableKheperaSim2D implements DrawableObjectSim2D
{
    private String objectID;
    
    private BufferedImage adgc, adgo, adgcoh, augo, augc, augcoh, currentImage;
    
    private int centerX = KheperaConstantsSim2D.KheperaBodyLengthInPixels/2;
    private int centerY = KheperaConstantsSim2D.ArmSpanInPixels/2;
       
    private KheperaUpdateSim2D currentUpdate;
    
    private boolean isFirstRendering = true;
    private boolean receivedUpdate = false;
    private boolean drawSensors = true;
    
   
    public DrawableKheperaSim2D(String objectID)
    {
        this.objectID = objectID;
        this.currentUpdate = new KheperaUpdateSim2D();
        this.currentUpdate.agentName = this.objectID;
    }    
    
    public void setDrawSensors(boolean drawSensors)
    {
        this.drawSensors = drawSensors;
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
            this.adgc = ImageIO.read(this.getClass().getResource("images/adgc.png"));
            this.adgo = ImageIO.read(this.getClass().getResource("images/adgo.png"));
            this.adgcoh = ImageIO.read(this.getClass().getResource("images/adgcoh.png"));

            this.augc = ImageIO.read(this.getClass().getResource("images/augc.png"));
            this.augo = ImageIO.read(this.getClass().getResource("images/augo.png"));
            this.augcoh = ImageIO.read(this.getClass().getResource("images/augcoh.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
                
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
     * Send an update to the object.
     * 
     * @param   update  [in]    Supplies the update.
     */
    public void update(Object update)
    {
        KheperaUpdateSim2D u = (KheperaUpdateSim2D)update;
        
        this.currentUpdate.armState = u.armState;
        this.currentUpdate.gripState = u.gripState;
        this.currentUpdate.position.angleInRadians = u.position.angleInRadians;
        this.currentUpdate.position.coordinates.x = u.position.coordinates.x;
        this.currentUpdate.position.coordinates.y = u.position.coordinates.y;
        
        this.currentUpdate.lightValues.clear();
        this.currentUpdate.lightValues.addAll(u.lightValues);
        
        this.currentUpdate.distanceValues.clear();
        this.currentUpdate.distanceValues.addAll(u.distanceValues);
        
        this.currentUpdate.lvtValues.clear();
        this.currentUpdate.lvtValues.addAll(u.lvtValues);
        
        this.currentUpdate.sensorPoints.clear();
        this.currentUpdate.sensorPoints.addAll(u.sensorPoints);

        this.currentUpdate.sensorEndPoints.clear();
        this.currentUpdate.sensorEndPoints.addAll(u.sensorEndPoints);
        
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
        
        switch(this.currentUpdate.armState)
        {
            case UP:
                switch(this.currentUpdate.gripState)
                {
                    case OPEN:
                        this.currentImage = this.augo;
                        break;
                        
                    case HOLDING:
                        this.currentImage = this.augcoh;
                        break;
                        
                    case CLOSED:
                        this.currentImage = this.augc;
                        break;
                }
                break;
            case DOWN:
                switch(this.currentUpdate.gripState)
                {
                    case OPEN:
                        this.currentImage = this.adgo;
                        break;
                        
                    case HOLDING:
                        this.currentImage = this.adgcoh;
                        break;
                        
                    case CLOSED:
                        this.currentImage = this.adgc;
                        break;
                }
                break;
               
        }
        
        //
        // Draw the image.
        //
        AffineTransform lastTransform = g.getTransform();
        
        g.rotate(   this.currentUpdate.position.angleInRadians,
                    this.currentUpdate.position.coordinates.x,
                    this.currentUpdate.position.coordinates.y);
        

        g.drawImage(this.currentImage,
                    (int) this.currentUpdate.position.coordinates.x - centerX,
                    (int) this.currentUpdate.position.coordinates.y - centerY,
                    null);
        
        g.setTransform(lastTransform); 
        
        
        
        //
        // Draw the label.
        //
        Color lastColor = g.getColor();
        
        g.setColor(ConstantsSim2D.LabelColor);
        
        g.drawString(   objectID,
                        (float) this.currentUpdate.position.coordinates.x - centerX,
                        (float) this.currentUpdate.position.coordinates.y - centerY);  
        
        
        
        
        //
        // Draw the sensors
        //
        if(this.drawSensors == true) {
            g.setColor(Color.BLUE);

            Iterator<Integer> dv = this.currentUpdate.distanceValues.iterator();
            Iterator<Point2D> sp = this.currentUpdate.sensorPoints.iterator();
            Iterator<Point2D> ep = this.currentUpdate.sensorEndPoints.iterator();
            Integer currentValue;
            Point2D currentStartPoint;
            Point2D currentEndPoint;
            
            while(sp.hasNext() && ep.hasNext()){
                currentValue = dv.next();
                currentStartPoint = sp.next();
                currentEndPoint = ep.next();
                g.drawLine( (int)currentStartPoint.getX(),
                            (int)currentStartPoint.getY(),
                            (int)currentEndPoint.getX(),
                            (int)currentEndPoint.getY());
                g.drawString(currentValue.toString(), (float)currentEndPoint.getX(), (float)currentEndPoint.getY());

            }
        }
                
        
               
        g.setColor(lastColor);        
    }
}
