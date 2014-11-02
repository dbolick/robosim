
package edu.wright.cs.carl.wart.sim2D.visualization;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsUpdate;
import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.geom.AffineTransform;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.wright.cs.carl.wart.sim.*;

import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;

/**
 * 2D JPanel visualization of the sim world.
 *
 * @author  Duane Bolick
 */
public class Sim2DGraphicsPanel extends SimGraphicsPanel
{ 
    private Map<String, DrawableObjectSim2D> drawables;
    
    private Graphics2D gr;
    private Image im = null;
    
    private int dimensions;
    
    private boolean isInitialized = false;
    private boolean isReceivingUpdates = false;
    
    public Sim2DGraphicsPanel()
    {
        this.drawables = new HashMap<String, DrawableObjectSim2D>(); 
        this.dimensions = ConstantsSim2D.MaxWorldWidthInPixels;
    }
    
    public boolean isInitialized()
    {
        return this.isInitialized;
    }
    
    public boolean isReceivingUpdates()
    {
        return this.isReceivingUpdates;
    }
    
    public void setDimensions(int dimensions)
    {
        this.dimensions = dimensions;  
        im = createImage(this.dimensions, this.dimensions);
        gr = (Graphics2D)im.getGraphics();        
    }
    
    public int getDimensions()
    {
        return this.dimensions;
    }
    
    public void setScaleFactor(double scaleFactor)
    {
        if(gr != null) {
            gr.scale(1, 1);
            gr.setColor(Color.white);
            gr.fillRect(0,0, this.dimensions, this.dimensions);
            gr.setTransform(AffineTransform.getScaleInstance(scaleFactor, scaleFactor));
        }        
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if(this.im != null) {
            g.drawImage(im, 0, 0, null);
        }
    }    
    
    public synchronized void render()
    {        
        if(im == null) {
            im = createImage(this.dimensions, this.dimensions);
            gr = (Graphics2D)im.getGraphics();
            if(gr == null) {
                return;
            }
        }
        
        gr.setColor(Color.white);
        gr.fillRect(0,0, this.dimensions, this.dimensions);

        Iterator<DrawableObjectSim2D> it = this.drawables.values().iterator();
        DrawableObjectSim2D currentObject = null;
        while(it.hasNext()) {
            currentObject = it.next();
            currentObject.render(gr);
        }                
    }    
    
    /**
     * Update the drawable objects with the provided SimGraphicsUpdate.
     * 
     * @param   update  [in]    Supplies the update.
     */
    public synchronized void updateGraphics(SimGraphicsUpdate update)
    {
        if(update instanceof Sim2DGraphicsInitializer) {
            Sim2DGraphicsInitializer initialUpdate = (Sim2DGraphicsInitializer) update;
            this.drawables.clear();
            Iterator<DrawableObjectSim2D> it = initialUpdate.drawableObjects.iterator();
            DrawableObjectSim2D currentObject = null;
            while(it.hasNext()) {
                
                currentObject = it.next();
                this.drawables.put(currentObject.getID(), currentObject);
            }
            
            this.isInitialized = true;
            
            return;
        }
    
        if(update instanceof Sim2DGraphicsUpdate) {
            
            Sim2DGraphicsUpdate sim2DUpdate = (Sim2DGraphicsUpdate) update;
            Iterator<String> it = sim2DUpdate.updates.keySet().iterator();
            String currentID = null;
            Object currentUpdate = null;
            DrawableObjectSim2D currentDrawable = null;
            
            while(it.hasNext()) {
                currentID = it.next();
                currentUpdate = sim2DUpdate.updates.get(currentID);
                currentDrawable = this.drawables.get(currentID);
                if(currentDrawable != null) {
                    currentDrawable.update(currentUpdate);
                }
            }
            
            if(this.isReceivingUpdates == false) {
                this.isReceivingUpdates = true;
            }
            
            return;
        }
        
    }    
}
