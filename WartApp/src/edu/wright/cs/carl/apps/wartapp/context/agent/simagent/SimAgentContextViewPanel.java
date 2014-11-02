
package edu.wright.cs.carl.apps.wartapp.context.agent.simagent;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.swing.*;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.wart.agent.controller.AgentController;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsUpdate;

import edu.wright.cs.carl.wart.sim.graphics.SimGraphicsPanel;

import edu.wright.cs.carl.apps.wartapp.*;

import edu.wright.cs.carl.apps.wartapp.context.agent.ControlRequest;
import edu.wright.cs.carl.apps.wartapp.context.agent.AgentStatusUpdate;

import edu.wright.cs.carl.apps.wartapp.swing.*;

import org.jdesktop.application.Action;


/**
 * This is the top-level JPanel that exists in in the main tabbed pane of the
 * GUI that represents a view of a simulated agent context.  One of its members
 * is the drawing pane of type
 * <i>edu.wright.cs.carl.wart.sim2D.visualization.Sim2DGraphicsPanel</i>.  This
 * class is responsible for maintaining the update/render/draw thread.
 * 
 * @author  Duane Bolick
 */
public class SimAgentContextViewPanel extends AbstractWartViewPanel implements Runnable
{
    
    public static long InitializeWaitInMillis = 1000;
    public static long RenderingWaitInMillis = 20;
    
    private Thread renderingThread;
    private boolean stopRequested = false;   
    
    private DefaultListModel userListModel;
    private DefaultListModel agentListModel;
    
    private UserStatusChange currentUserStatus;
    
    
    /** Creates new form SimAgentContextViewPanel */
    public SimAgentContextViewPanel()
    {
        this.userListModel = new DefaultListModel();
        this.agentListModel = new DefaultListModel();
        this.currentUserStatus = new UserStatusChange();        
    }

    @Override
    public void doInit()
    {
        initComponents();                
        
        this.renderingThread = new Thread(this);
        this.renderingThread.start();  
        
        this.simPanel.revalidate();

        this.refreshUserListModel();
    }       

    public void refreshUserListModel()
    {
        this.userListModel.removeAllElements();

        try {
            Iterator<String> it = this.contextHandle.getClientList().iterator();

            while(it.hasNext()) {
                this.userListModel.addElement(it.next());
            }
        }
        catch(IOException e) {
            this.lostConnection(e);
            return;
        }
    }

    /**
     * Refresh the components of this panel.
     */
    @Override
    public void refreshPanelComponents()
    {
        this.refreshUserListModel();
    }

    public void updateSimVisualization(SimGraphicsUpdate update)
    {
        ((SimGraphicsPanel)this.simPanel).updateGraphics(update);        
    }
    
    public void postMessage(String message)
    {
        this.chatOutputArea.append(message);
    }
    
    public void updateUserList(String username, UserStatusChange.Type actionType)
    {       
        if(actionType == UserStatusChange.Type.LOGON) {
            if(this.userListModel.contains(username) == false) {
                this.userListModel.addElement(username);
            }
        }
        else if (actionType == UserStatusChange.Type.LOGOFF) {
            this.userListModel.removeElement(username);
        }
    }
    
    public void updateAgentStatus(AgentStatusUpdate update) {
        //
        // TODO: Add the view queue feature, with the ability to actually
        // control the queue.  
        //
        List<String> updatedList = new ArrayList<String>();
        Iterator<String> it = update.controllerQueues.keySet().iterator();
        while(it.hasNext()) {
            updatedList.add(it.next());
        }
        
        String currentName = null;
        it = updatedList.iterator();
        while(it.hasNext()) {
            currentName = it.next();
            if(this.agentListModel.contains(currentName) == false) {
                this.agentListModel.addElement(currentName);
            }
        }
    }

    @Override
    public void run()
    {
        //
        // Until we've received the SimGraphicsInitializer, we can't draw
        // anything.  So send the remote context our status of 
        // <i>Type.INITIALIZE_ME</i> until it has sent the initializer.  We'll
        // know we've been initialized when the graphics panel tells us so.
        //
        this.currentUserStatus.statusChangeType = UserStatusChange.Type.INITIALIZE_ME;
        this.graphicsPanelStatusLabel.setText("Waiting for initialization from remote context...");
        
        while(((SimGraphicsPanel)this.simPanel).isInitialized() == false && !stopRequested) {
            try{
                Thread.sleep(SimAgentContextViewPanel.InitializeWaitInMillis);
                this.contextHandle.sendMessagePayload(this.currentUserStatus);
            }
            catch(InterruptedException e) {
                
            }
            catch(MessagingException e) {
                
            }
            catch(MessageTypeException e) {
                
            }
            catch(PermissionException e) {
                
            }
            catch(IOException e) {
                
            }         
        }
        
        //
        // Now, once we're initialized, let the remote context know so that it
        // can start sending updates.
        //
        this.currentUserStatus.statusChangeType = UserStatusChange.Type.INITIALIZED;
        this.graphicsPanelStatusLabel.setText("Waiting to receive updates...");
        
        while(((SimGraphicsPanel)this.simPanel).isReceivingUpdates() == false && !stopRequested) {

            try{
                Thread.sleep(SimAgentContextViewPanel.InitializeWaitInMillis);
                this.contextHandle.sendMessagePayload(this.currentUserStatus);
            }
            catch(InterruptedException e) {
                
            }            
            catch(MessagingException e) {
                
            }
            catch(MessageTypeException e) {
                
            }
            catch(PermissionException e) {
                
            }
            catch(IOException e) {
                
            }
        }
        
        //
        // Finally, we're initialized and we're receiving updates, so start
        // rendering the world.
        //

        while(!stopRequested) {

            try{
                Thread.sleep(SimAgentContextViewPanel.RenderingWaitInMillis);
            }
            catch(InterruptedException e) {
                
            }
            ((SimGraphicsPanel)this.simPanel).render();
            ((SimGraphicsPanel)this.simPanel).repaint();            
        }
        this.graphicsPanelStatusLabel.setText("");
        this.graphicsPanelStatusLabel.setVisible(false);
    }     
    
 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        agentList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        messageInputField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatOutputArea = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane4 = new javax.swing.JScrollPane();
        simPanel = new edu.wright.cs.carl.wart.sim2D.visualization.Sim2DGraphicsPanel();
        graphicsPanelStatusLabel = new javax.swing.JLabel();
        worldScaleSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        agentList.setModel(this.agentListModel);
        agentList.setName("agentList"); // NOI18N
        jScrollPane1.setViewportView(agentList);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(SimAgentContextViewPanel.class, this);
        jButton1.setAction(actionMap.get("requestControl")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("viewControllerQueue")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        userList.setModel(this.userListModel);
        userList.setName("userList"); // NOI18N
        jScrollPane2.setViewportView(userList);

        jButton4.setAction(actionMap.get("kickUser")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        jButton5.setAction(actionMap.get("sendChat")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(SimAgentContextViewPanel.class);
        messageInputField.setText(resourceMap.getString("messageInputField.text")); // NOI18N
        messageInputField.setName("messageInputField"); // NOI18N
        messageInputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageInputFieldKeyPressed(evt);
            }
        });

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        chatOutputArea.setColumns(20);
        chatOutputArea.setFont(resourceMap.getFont("chatOutputArea.font")); // NOI18N
        chatOutputArea.setLineWrap(true);
        chatOutputArea.setRows(5);
        chatOutputArea.setName("chatOutputArea"); // NOI18N
        jScrollPane3.setViewportView(chatOutputArea);

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1002, 1002));

        simPanel.setBackground(resourceMap.getColor("simPanel.background")); // NOI18N
        simPanel.setName("simPanel"); // NOI18N
        simPanel.setPreferredSize(new java.awt.Dimension(1000, 1000));

        graphicsPanelStatusLabel.setFont(resourceMap.getFont("graphicsPanelStatusLabel.font")); // NOI18N
        graphicsPanelStatusLabel.setText(resourceMap.getString("graphicsPanelStatusLabel.text")); // NOI18N
        graphicsPanelStatusLabel.setName("graphicsPanelStatusLabel"); // NOI18N

        javax.swing.GroupLayout simPanelLayout = new javax.swing.GroupLayout(simPanel);
        simPanel.setLayout(simPanelLayout);
        simPanelLayout.setHorizontalGroup(
            simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graphicsPanelStatusLabel)
                .addContainerGap(990, Short.MAX_VALUE))
        );
        simPanelLayout.setVerticalGroup(
            simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graphicsPanelStatusLabel)
                .addContainerGap(989, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(simPanel);

        worldScaleSlider.setMaximum(0);
        worldScaleSlider.setMinimum(-30);
        worldScaleSlider.setMinorTickSpacing(10);
        worldScaleSlider.setPaintLabels(true);
        worldScaleSlider.setPaintTicks(true);
        worldScaleSlider.setName("worldScaleSlider"); // NOI18N
        worldScaleSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                worldScaleSliderStateChanged(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(worldScaleSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addComponent(jButton4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(messageInputField, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(worldScaleSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(messageInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void messageInputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageInputFieldKeyPressed

    if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        this.sendChat();
    }    
}//GEN-LAST:event_messageInputFieldKeyPressed

private void worldScaleSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_worldScaleSliderStateChanged
// TODO add your handling code here:
    if(((JSlider)evt.getSource()).getValueIsAdjusting() == false) {
        ((SimGraphicsPanel)this.simPanel).setScaleFactor((double) Math.pow(2.0f, (double)this.worldScaleSlider.getValue()/10f));
    }    
}//GEN-LAST:event_worldScaleSliderStateChanged

    @Action
    public void requestControl()
    {
        int selectedIndex = this.agentList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }
        
        String agentName = (String) this.agentList.getSelectedValue();
        
        ControlRequest controlRequest = new ControlRequest(agentName);

        JFileChooser fc = new JFileChooser(
                WartApp.getApplication().getConfiguration().getControllersDirectory());
    
        fc.showOpenDialog(WartApp.getApplication().getMainFrame());
        String controllerName = fc.getSelectedFile().getName();
        controllerName = controllerName.substring(0, controllerName.indexOf('.'));

        AgentController c = null;

        try {
            c = (AgentController) Class.forName(WartApp.getApplication().getConfiguration().getControllersPackagePrefix() + controllerName).newInstance();
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(InstantiationException e) {
            e.printStackTrace();
        }
        catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        
        controlRequest.controller = c;
        
        try {
            this.contextHandle.sendMessagePayload(controlRequest);
        }
        catch(MessagingException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(MessageTypeException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Lost Connection with the Context.");
            
            //
            // TODO: Cleanup
            //
                    
        }        
    }

    @Action
    public void sendChat()
    {
        String chatInput = this.messageInputField.getText();
        
        if(chatInput.isEmpty()) {
            return;
        }
        
        if(this.contextHandle == null) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Context Handle is null!");
            return;
        }
        
        try {            
            this.contextHandle.sendMessagePayload(new SimpleText(chatInput));
        }
        catch(MessagingException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(MessageTypeException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Lost Connection with the Context.");
            
            //
            // TODO: Cleanup
            //
                    
        }
        
        this.messageInputField.setText(null);
    }

    @Action
    public void kickUser()
    {
        int selectedIndex = this.userList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        String kickedUsername = (String) this.userList.getSelectedValue();

        try {
            this.contextHandle.sendMessagePayload(new UserRemovalRequest(kickedUsername));
        }
        catch(MessagingException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(MessageTypeException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
        catch(IOException e) {
            this.lostConnection(e);
            return;
        }


    }

    @Action
    public void viewControllerQueue() {
        // TODO - view controller queue
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList agentList;
    private javax.swing.JTextArea chatOutputArea;
    private javax.swing.JLabel graphicsPanelStatusLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField messageInputField;
    private javax.swing.JPanel simPanel;
    private javax.swing.JList userList;
    private javax.swing.JSlider worldScaleSlider;
    // End of variables declaration//GEN-END:variables
    
}
