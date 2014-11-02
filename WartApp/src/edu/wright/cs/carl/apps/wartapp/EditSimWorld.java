/*
 * EditSimWorld.java
 *
 * Created on January 22, 2009, 12:25 AM
 */

package edu.wright.cs.carl.apps.wartapp;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionListener;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;       

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import edu.wright.cs.carl.wart.agent.AgentManager;
import edu.wright.cs.carl.wart.agent.DefaultAgentManager;

import edu.wright.cs.carl.net.server.Server;
import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.security.DefaultAcl;
import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.apps.wartapp.swing.*;

import edu.wright.cs.carl.apps.wartapp.WartAppConstants;

import edu.wright.cs.carl.apps.wartapp.context.agent.AgentContext;
import edu.wright.cs.carl.apps.wartapp.context.chat.ChatContext;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsPanel;

import edu.wright.cs.carl.wart.sim2D.visualization.Sim2DGraphicsInitializer;

import edu.wright.cs.carl.wart.sim2D.AbsolutePositionSim2D;
import edu.wright.cs.carl.wart.sim2D.AgentWorldSim2D;
import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;
import edu.wright.cs.carl.wart.sim2D.ObjectSim2D;

import edu.wright.cs.carl.wart.sim2D.objects.*;
import edu.wright.cs.carl.wart.agent.mod.khepera.sim2D.*;

import edu.wright.cs.carl.wart.sim2D.visualization.*;

import org.jdesktop.application.Action;

/**
 * Edit a simulated 2D environment.s
 * 
 * @author  Duane Bolick
 */
public class EditSimWorld extends javax.swing.JDialog implements Runnable {

    private AgentWorldSim2D agentWorld;
    
    private Map<String, DrawableObjectSim2D> drawables;
    
    private int dimensionsInPixels;    
    
    private Thread renderingThread;
    private boolean stopRequested = false;
    
    private Point2D mousePressPoint;
    private Point2D mouseCurrentDragPoint;
    private Point2D mouseReleasePoint;
    private Point2D mouseClickPoint;
    
    private ObjectSim2D selectedObject;
    private AbsolutePositionSim2D addPosition;
    private Point2D newObstaclePoint1;
    private Point2D newObstaclePoint2;
    private boolean addingObstacle = false;

    /** Creates new form EditSimWorld */
    public EditSimWorld(AgentWorldSim2D agentWorld) {
        super(WartApp.getApplication().getMainFrame(), false);
     
        this.agentWorld = agentWorld;

        this.dimensionsInPixels = agentWorld.getWorldDimensionInPixels();
        
        this.drawables = new HashMap<String, DrawableObjectSim2D>();
        this.initDrawables();
        
        this.mouseClickPoint = new Point2D.Double();
        this.mousePressPoint = new Point2D.Double();
        this.mouseReleasePoint = new Point2D.Double();
        this.mouseCurrentDragPoint = new Point2D.Double();
        
        this.addPosition = new AbsolutePositionSim2D();
        this.addPosition.angleInRadians = 0;
        this.addPosition.coordinates.x = this.dimensionsInPixels/2;
        this.addPosition.coordinates.y = this.dimensionsInPixels/2;

        this.selectedObject = null;
        this.newObstaclePoint1 = new Point2D.Double();
        this.newObstaclePoint2 = new Point2D.Double();
        
        initComponents();
        
        ((Sim2DGraphicsPanel)this.simPanel).updateGraphics(this.agentWorld.getInitialSyncPayload());        

        /*
        this.agentWorld.setIsReady(true);
        this.agentWorld.start();
         */
        
        this.renderingThread = new Thread(this);
        this.renderingThread.start();        
    }

    public void run()
    {
        while(!stopRequested) {
            try{
                Thread.sleep(20);
            }
            catch(InterruptedException e) {
                
            }
            ((Sim2DGraphicsPanel)this.simPanel).updateGraphics(this.agentWorld.getCurrentPayload());            
            ((Sim2DGraphicsPanel)this.simPanel).render();
            ((Sim2DGraphicsPanel)this.simPanel).repaint();
        }
    }    
    
    public synchronized void initDrawables()
    {
        Sim2DGraphicsInitializer initialUpdate = this.agentWorld.getInitialSyncPayload();
        Iterator<DrawableObjectSim2D> it = initialUpdate.drawableObjects.iterator();
        DrawableObjectSim2D currentObject = null;
        while(it.hasNext()) {
            currentObject = it.next();
            this.drawables.put(currentObject.getID(), currentObject);
        }
        return;        
    }
    
    public synchronized void updateDrawables()
    {
        Sim2DGraphicsUpdate sim2DUpdate = this.agentWorld.getCurrentPayload();
        Iterator<String> it = sim2DUpdate.updates.keySet().iterator();
        String currentID = null;
        Object currentUpdate = null;

        while(it.hasNext()) {
            currentID = it.next();
            currentUpdate = sim2DUpdate.updates.get(currentID);
            this.drawables.get(currentID).update(currentUpdate);
        }
        return;        
    }    

    /**
     * Update the Sim2DGraphicsPanel with the current state of the world.
     */
    public synchronized void worldChanged()
    {
        ((Sim2DGraphicsPanel)this.simPanel).updateGraphics(this.agentWorld.getInitialSyncPayload());
        ((Sim2DGraphicsPanel)this.simPanel).updateGraphics(this.agentWorld.getCurrentPayload());
    }

    public void addObject(ObjectSim2D newObject)
    {
        this.agentWorld.addObjectSim2D(newObject);
        this.worldChanged();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        simPanel = new edu.wright.cs.carl.wart.sim2D.visualization.Sim2DGraphicsPanel();
        jPanel3 = new javax.swing.JPanel();
        worldSizeSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        worldZoomSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        addKheperaButton = new javax.swing.JButton();
        addCapButton = new javax.swing.JButton();
        addBallButton = new javax.swing.JButton();
        addLightButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        objectRotationSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        mapNameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(EditSimWorld.class);
        setTitle(resourceMap.getString("EditSimWorldForm.title")); // NOI18N
        setModal(true);
        setName("EditSimWorldForm"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        simPanel.setBackground(resourceMap.getColor("simPanel.background")); // NOI18N
        simPanel.setToolTipText(resourceMap.getString("simPanel.toolTipText")); // NOI18N
        simPanel.setAutoscrolls(true);
        simPanel.setName("simPanel"); // NOI18N
        simPanel.setPreferredSize(new java.awt.Dimension(1000, 1000));
        simPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                simPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                simPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                simPanelMouseReleased(evt);
            }
        });
        simPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                simPanelMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout simPanelLayout = new javax.swing.GroupLayout(simPanel);
        simPanel.setLayout(simPanelLayout);
        simPanelLayout.setHorizontalGroup(
            simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        simPanelLayout.setVerticalGroup(
            simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(simPanel);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        worldSizeSlider.setMajorTickSpacing(100);
        worldSizeSlider.setMaximum(ConstantsSim2D.MaxWorldWidthInPixels);
        worldSizeSlider.setMinimum(ConstantsSim2D.MinWorldWidthInPixels);
        worldSizeSlider.setMinorTickSpacing(25);
        worldSizeSlider.setPaintLabels(true);
        worldSizeSlider.setPaintTicks(true);
        worldSizeSlider.setValue(ConstantsSim2D.DefaultWorldWidthInPixels);
        worldSizeSlider.setName("worldSizeSlider"); // NOI18N
        worldSizeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                worldSizeSliderStateChanged(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        worldZoomSlider.setMaximum(0);
        worldZoomSlider.setMinimum(-30);
        worldZoomSlider.setMinorTickSpacing(10);
        worldZoomSlider.setPaintTicks(true);
        worldZoomSlider.setEnabled(false);
        worldZoomSlider.setName("worldZoomSlider"); // NOI18N
        worldZoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                worldZoomSliderStateChanged(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel2))
                    .addComponent(jLabel4))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(worldZoomSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                    .addComponent(worldSizeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(worldSizeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(worldZoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(EditSimWorld.class, this);
        addKheperaButton.setAction(actionMap.get("addKhepera")); // NOI18N
        addKheperaButton.setFocusable(false);
        addKheperaButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addKheperaButton.setName("addKheperaButton"); // NOI18N
        addKheperaButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(addKheperaButton);

        addCapButton.setAction(actionMap.get("addCap")); // NOI18N
        addCapButton.setText(resourceMap.getString("addCapButton.text")); // NOI18N
        addCapButton.setFocusable(false);
        addCapButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addCapButton.setName("addCapButton"); // NOI18N
        addCapButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(addCapButton);

        addBallButton.setAction(actionMap.get("addBall")); // NOI18N
        addBallButton.setFocusable(false);
        addBallButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addBallButton.setName("addBallButton"); // NOI18N
        addBallButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(addBallButton);

        addLightButton.setAction(actionMap.get("addLight")); // NOI18N
        addLightButton.setText(resourceMap.getString("addLightButton.text")); // NOI18N
        addLightButton.setFocusable(false);
        addLightButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addLightButton.setName("addLightButton"); // NOI18N
        addLightButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(addLightButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        objectRotationSlider.setMajorTickSpacing(90);
        objectRotationSlider.setMaximum(360);
        objectRotationSlider.setMinorTickSpacing(45);
        objectRotationSlider.setPaintLabels(true);
        objectRotationSlider.setPaintTicks(true);
        objectRotationSlider.setValue(0);
        objectRotationSlider.setName("objectRotationSlider"); // NOI18N
        objectRotationSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                objectRotationSliderStateChanged(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jButton5.setAction(actionMap.get("deleteObject")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(objectRotationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(objectRotationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );

        jButton6.setAction(actionMap.get("cancelEditWorld")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N

        jButton7.setAction(actionMap.get("okEditWorld")); // NOI18N
        jButton7.setName("jButton7"); // NOI18N

        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        jButton3.setAction(actionMap.get("loadWorld")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton3);

        jButton4.setAction(actionMap.get("saveWorld")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton4);

        jButton2.setAction(actionMap.get("relaxWorld")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jToolBar2.add(jButton2);

        jButton1.setAction(actionMap.get("resetWorld")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jToolBar2.add(jButton1);

        mapNameField.setText(ConstantsSim2D.DefaultMapName);
        mapNameField.setName("mapNameField"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jButton8.setAction(actionMap.get("toggleKheperaGripState")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N

        jButton9.setAction(actionMap.get("toggleKheperaArmState")); // NOI18N
        jButton9.setName("jButton9"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(81, 81, 81))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mapNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mapNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton8)
                            .addComponent(jButton9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton7)
                            .addComponent(jButton6))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void simPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simPanelMouseClicked
    this.mouseClickPoint.setLocation(evt.getPoint());
    ObjectSim2D selected = this.agentWorld.getObjectAtPoint(mousePressPoint);
    if(selected != null) {
        this.selectedObject = selected;
    }
}//GEN-LAST:event_simPanelMouseClicked

private void simPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simPanelMousePressed
    this.mousePressPoint.setLocation(evt.getPoint());
    ObjectSim2D selected = this.agentWorld.getObjectAtPoint(mousePressPoint);
    this.selectedObject = selected;
    if(this.selectedObject != null) {
        
    }
    else {
        this.newObstaclePoint1.setLocation(mousePressPoint);
        this.addingObstacle = true;
    }
    //Iterator<ObjectSim2D> it = this.agentWorld

}//GEN-LAST:event_simPanelMousePressed

private void simPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simPanelMouseReleased
    this.mouseReleasePoint.setLocation(evt.getPoint());
    if(this.selectedObject != null) {
         this.selectedObject.translate( (double) this.mouseReleasePoint.getX() - (double) this.mousePressPoint.getX(),
                                        (double) this.mouseReleasePoint.getY() - (double) this.mousePressPoint.getY());
         this.worldChanged();         
    }
    else {
        this.addingObstacle = false;
        this.newObstaclePoint2.setLocation(mouseReleasePoint);
        double xDimension = (double) this.mouseReleasePoint.getX() - (double) this.mousePressPoint.getX();
        double yDimension = (double) this.mouseReleasePoint.getY() - (double) this.mousePressPoint.getY();
        
        if(xDimension < ConstantsSim2D.MinObstacleDimensionInPixels || xDimension < ConstantsSim2D.MinObstacleDimensionInPixels) {
            return;
        }
        
        if(xDimension < 0 || yDimension < 0) {
            return;
        }
        
        this.addPosition.angleInRadians = 0;
        this.addPosition.coordinates.x = (double) this.mousePressPoint.getX() + xDimension/2;
        this.addPosition.coordinates.y = (double) this.mousePressPoint.getY() + yDimension/2;
        this.agentWorld.addObjectSim2D(
                            new RectangularObstacleSim2D(
                                    this.addPosition,
                                    xDimension,
                                    yDimension));
              
        this.worldChanged();
        
        this.addPosition.angleInRadians = 0;
        this.addPosition.coordinates.x = this.dimensionsInPixels/2;
        this.addPosition.coordinates.y = this.dimensionsInPixels/2;        
    }

}//GEN-LAST:event_simPanelMouseReleased

private void worldSizeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_worldSizeSliderStateChanged
    if(((JSlider)evt.getSource()).getValueIsAdjusting() == false) {
        int newSize = this.worldSizeSlider.getValue();
        this.agentWorld.resizeWorldDimensionInPixels(newSize);
        ((Sim2DGraphicsPanel)this.simPanel).setDimensions(newSize);
        this.worldChanged();
        this.dimensionsInPixels = this.worldSizeSlider.getValue();
        this.addPosition.coordinates.x = this.dimensionsInPixels/2;
        this.addPosition.coordinates.y = this.dimensionsInPixels/2;        
    }
}//GEN-LAST:event_worldSizeSliderStateChanged

private void objectRotationSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_objectRotationSliderStateChanged
   
    if(((JSlider)evt.getSource()).getValueIsAdjusting() == false) {
        int newRotationInDegrees = this.objectRotationSlider.getValue();
        if(this.selectedObject != null) {
            if(this.selectedObject instanceof RectangularObstacleSim2D) {
                return;
            }
            this.selectedObject.setAngle((double) Math.toRadians(newRotationInDegrees));
            this.worldChanged();
        }
        
    }
}//GEN-LAST:event_objectRotationSliderStateChanged

private void simPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simPanelMouseDragged

    this.mouseCurrentDragPoint.setLocation(evt.getPoint());
    
}//GEN-LAST:event_simPanelMouseDragged

private void worldZoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_worldZoomSliderStateChanged

    if(((JSlider)evt.getSource()).getValueIsAdjusting() == false) {
        ((SimGraphicsPanel)this.simPanel).setScaleFactor((double) Math.pow(2.0f, (double)this.worldZoomSlider.getValue()/10f));
    } 
}//GEN-LAST:event_worldZoomSliderStateChanged

    @Action
    public void addKhepera()
    {
        //
        // Normally, we wouldn't care about an object's ID.  The AgentWorldSim2D
        // creates UUIDs for unnamed objects for us.  But Khepera need to have
        // human-readable names, so we create them here.
        //
        String kheperaName = JOptionPane.showInputDialog(this, "Please name this Khepera:");
        
        if(this.agentWorld.getObjectByID(kheperaName) != null) {
            JOptionPane.showMessageDialog(this, "That name is already in use.  Please select another.");
            return;
        }
        
        this.addObject(new KheperaSim2D(kheperaName, this.addPosition));
    }

    @Action
    public void addCap()
    {
        this.addObject(new CapSim2D(this.addPosition));       
    }

    @Action
    public void addBall()
    {
        this.addObject(new BallSim2D(this.addPosition));         
    }

    @Action
    public void addLight()
    {
        this.addObject(new LightSim2D(this.addPosition));            
    }

    @Action
    public void resetWorld()
    {
        this.agentWorld.resetWorld();
        this.worldChanged();
    }

    @Action
    public void relaxWorld()
    {
        this.agentWorld.relax();
        this.worldChanged();
    }

    @Action
    public void deleteObject()
    {
        if(this.selectedObject != null) {
            this.agentWorld.removeObjectSim2D(this.selectedObject.getID());
            this.worldChanged();
        }        
    }

    @Action
    public void loadWorld()
    {
        StringBuffer path = new StringBuffer();
        path.append(WartApp.getApplication().getConfiguration().getMapsDirectory());
        path.append(File.separator);    
        
        JFileChooser fc = new JFileChooser();
        File mapsDir = new File(path.toString());
        fc.setCurrentDirectory(mapsDir);
        fc.showOpenDialog(this);  
        
        String mapName = fc.getSelectedFile().getName();
        path.append(mapName);
        
        //
        // Load the world, then copy its stuff into the one here.
        //
        AgentWorldSim2D loadedWorld = (AgentWorldSim2D)WartApp.getApplication().loadFile(path.toString());
        
        this.agentWorld.resetWorld();
        this.agentWorld.resizeWorldDimensionInPixels(loadedWorld.getWorldDimensionInPixels());
        Iterator<ObjectSim2D> it = loadedWorld.getAllObjects().iterator();
        while(it.hasNext()) {
            this.agentWorld.addObjectSim2D(it.next());
        }
        
        this.worldChanged();
        this.mapNameField.setText(mapName.substring(0, mapName.lastIndexOf("." + ConstantsSim2D.MapFileExtension)));      
    }

    @Action
    public synchronized void saveWorld()
    {
        StringBuffer path = new StringBuffer();
        path.append(WartApp.getApplication().getConfiguration().getMapsDirectory());
        path.append(File.separator);        
        path.append(this.mapNameField.getText() + "." + ConstantsSim2D.MapFileExtension);
        
        WartApp.getApplication().saveFile(path.toString(), this.agentWorld);
    }

    @Action
    public void cancelEditWorld()
    {
        this.stopRequested = true;
        try {
            this.renderingThread.join();
        }
        catch(InterruptedException e) {
            
        }
        
        this.agentWorld.setIsReady(false);
        this.setVisible(false);
        this.dispose();
        return;
    }

    @Action
    public void okEditWorld()
    {
        this.stopRequested = true;
        try {
            this.renderingThread.join();
        }
        catch(InterruptedException e) {
            
        }
        
        this.agentWorld.setIsReady(true);
        this.saveWorld();
        this.setVisible(false);
        this.dispose();
        return;
    }

    @Action
    public void toggleKheperaArmState()
    {
        if(this.selectedObject != null && this.selectedObject instanceof KheperaSim2D) {
            KheperaSim2D khepera = (KheperaSim2D)this.selectedObject;
            if(khepera.getArmState() == edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants.Gripper.ArmState.DOWN) {
                khepera.setArmState(edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants.Gripper.ArmState.UP);
            }
            else {
                khepera.setArmState(edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants.Gripper.ArmState.DOWN);
            }
            this.worldChanged();
        }         
    }

    @Action
    public void toggleKheperaGripState()
    {
        if(this.selectedObject != null && this.selectedObject instanceof KheperaSim2D) {
            KheperaSim2D khepera = (KheperaSim2D)this.selectedObject;
            if(khepera.getGripperState() == edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants.Gripper.GripState.OPEN) {
                khepera.setGripperState(edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants.Gripper.GripState.CLOSED);
            }
            else {
                khepera.setGripperState(edu.wright.cs.carl.wart.agent.mod.khepera.KheperaConstants.Gripper.GripState.OPEN);
            }
            this.worldChanged();
        }          
    }
    
    




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBallButton;
    private javax.swing.JButton addCapButton;
    private javax.swing.JButton addKheperaButton;
    private javax.swing.JButton addLightButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTextField mapNameField;
    private javax.swing.JSlider objectRotationSlider;
    private javax.swing.JPanel simPanel;
    private javax.swing.JSlider worldSizeSlider;
    private javax.swing.JSlider worldZoomSlider;
    // End of variables declaration//GEN-END:variables

}
