/*
 * AddContext.java
 *
 * Created on December 10, 2008, 10:39 PM
 */

package edu.wright.cs.carl.apps.wartapp;

import java.security.Principal;

import java.security.acl.Acl;

import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.handle.RemoteHandleManager;
import edu.wright.cs.carl.net.handle.DefaultRemoteHandleManager;

import edu.wright.cs.carl.wart.agent.Agent;
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

import edu.wright.cs.carl.wart.real.*;

import edu.wright.cs.carl.wart.sim2D.AgentWorldSim2D;
import edu.wright.cs.carl.wart.sim2D.ConstantsSim2D;


/**
 * Dialog box to create a new context.
 * 
 * @author  Duane Bolick
 */
public class AddContext extends javax.swing.JDialog {

    private Server server;
    private Principal user;
    
    /** Creates new form AddContext */
    public AddContext()
    {
        super(WartApp.getApplication().getMainFrame(), false);
    
        this.server = WartApp.getApplication().getServer();
        this.user = WartApp.getApplication().getLocalUser();       
        
        initComponents();
    }

    @Action
    public void closeAddContext()
    {
        this.setVisible(false);
        this.dispose();
    }
    
    @Action
    public void createContext()
    {
        //
        // [1/] Get the context constructor params from the dialog fields.
        //
        String contextType = (String) this.contextTypeComboBox.getSelectedItem();
        WartAppConstants.ContextType type = WartAppConstants.GetContextType(contextType);
        
        String contextName = this.contextNameField.getText();
        if(contextName.isEmpty()) {
            contextName = WartAppConstants.GetDefaultContextName(type);
        }
        
        String contextDescription = this.contextDescriptionField.getText();
        if(contextDescription.isEmpty()) {
            contextDescription = WartAppConstants.GetDefaultContextDescription(type);
        }
        
        String maxUsersString = this.maxUsersField.getText();
        
        //
        // By default, number of users is unlimited (a negative value indicates
        // this to the context constructor - see the javadoc for AbstractContext
        // and DefaultRemoteHandleManager for details.
        //
        int maxUsers = -1;
        try {
            maxUsers = Integer.parseInt(maxUsersString);
        }
        catch(NumberFormatException e) {
            maxUsers = -1;
        }

        //
        // [2/] Next, create the Acl.  If "Use Server Acl" was selected, we just
        // provide a reference to the Server Acl.  If not, we have to make a
        // copy of the Acl.
        //
        Acl serverAcl = null;
        Acl contextAcl = null;
        try {
            serverAcl = this.server.getAcl(this.user);
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
            return;
        }
        
        if(this.useServerAclCheckBox.isSelected()) {
            contextAcl = serverAcl;
        }
        else {
            contextAcl = new DefaultAcl((DefaultAcl) serverAcl);
        }
        
        
        //
        // [3/] Then, create a RemoteHandleManager.
        //
        RemoteHandleManager handles = new DefaultRemoteHandleManager(maxUsers);
        
        //
        // [4/] Then create the context instance.  Since all Context
        // implementations have a common constructor interface, we can do this
        // by Class...
        //
        Context newContext = WartAppConstants.GetContextInstance(
                                    type,
                                    contextName,
                                    contextDescription,
                                    serverAcl,
                                    handles,
                                    maxUsers);
        
        //
        // [5/] Now, do context-type specific initialization.
        //
        switch(WartAppConstants.GetContextType(newContext.getType()))
        {
            case CHAT_CONTEXT:
                //
                // Nothing extra.
                //
                break;
                
            case SIM_AGENT_CONTEXT:
                AgentContext simAgentContext = (AgentContext) newContext;
                AgentWorldSim2D simAgentWorld = new AgentWorldSim2D();
                AgentManager simAgentManager = new DefaultAgentManager();
                
                WartApp.getApplication().show(new EditSimWorld(simAgentWorld));
                
                // If we failed to create the agent world, then exit.
                if(simAgentWorld.isReady() == false) {
                    return;
                }
                
                // But if we did, then initialize everything.
                Iterator<Agent> it = simAgentWorld.getAgents().iterator();
                while(it.hasNext()) {
                    try {
                        simAgentManager.addAgent(it.next());
                    }
                    catch(ServerException e) {
                        JOptionPane.showMessageDialog(rootPane, e.getMessage());
                        return;
                    }
                }
                simAgentContext.initialize(simAgentManager, simAgentWorld, WartAppConstants.DefaultControlTimeLimitInSeconds);
                
                break;
                
            case REAL_AGENT_CONTEXT:
                //
                // Create the context, world, and agent manager.
                //
                AgentContext realAgentContext = (AgentContext) newContext;
                AgentWorldReal realAgentWorld = new AgentWorldReal();
                AgentManager realAgentManager = new DefaultAgentManager();

                //
                // Then autodetect agents, construct them, and allow user to 
                // select which agents to include.
                //
                WartApp.getApplication().show(new EditRealWorld(realAgentWorld));
                
                // If we failed to create the agent world, then exit.
                if(realAgentWorld.isReady() == false) {                   
                    return;
                }
                
                // But if we did, then initialize everything.
                Iterator<Agent> i = realAgentWorld.getAgents().iterator();
                while(i.hasNext()) {
                    try {
                        realAgentManager.addAgent(i.next());
                    }
                    catch(ServerException e) {
                        JOptionPane.showMessageDialog(rootPane, e.getMessage());
                        return;
                    }
                }
                realAgentContext.initialize(realAgentManager, realAgentWorld, WartAppConstants.DefaultControlTimeLimitInSeconds);
                
                break;                                
        }
        
       
        
        //
        // [5/] Add it to the Server.
        //
        try {
            this.server.addContext(this.user, newContext);
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
            return;
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
            return;
        }
        
        //
        // [6/] Now, if the "start activated" checkbox is selected, activate
        // the context.
        //
        if(this.startActiveCheckbox.isSelected()) {
            try {
                WartApp.getApplication().getServer().activateContext(newContext.getUniqueID());
            }
            catch(ServerException e) {
                JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
                return;
            }
            catch(ContextException e) {
                JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
                return;
            }
        }

        //
        // [7/] If we're here, we succeeded.  Close the dialog box.
        //
        this.closeAddContext();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        contextTypeComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        contextNameField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        contextDescriptionField = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        maxUsersField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        useServerAclCheckBox = new javax.swing.JCheckBox();
        startActiveCheckbox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(AddContext.class);
        setTitle(resourceMap.getString("AddContextForm.title")); // NOI18N
        setModal(true);
        setName("AddContextForm"); // NOI18N
        setResizable(false);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        contextTypeComboBox.setModel(new DefaultComboBoxModel(edu.wright.cs.carl.apps.wartapp.WartAppConstants.GetSupportedContextTypes()));
        contextTypeComboBox.setName("contextTypeComboBox"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        contextNameField.setText(resourceMap.getString("contextNameField.text")); // NOI18N
        contextNameField.setName("contextNameField"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        contextDescriptionField.setColumns(20);
        contextDescriptionField.setRows(5);
        contextDescriptionField.setName("contextDescriptionField"); // NOI18N
        jScrollPane1.setViewportView(contextDescriptionField);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        maxUsersField.setColumns(4);
        maxUsersField.setText(resourceMap.getString("maxUsersField.text")); // NOI18N
        maxUsersField.setName("maxUsersField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(AddContext.class, this);
        jButton1.setAction(actionMap.get("closeAddContext")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("createContext")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        useServerAclCheckBox.setSelected(true);
        useServerAclCheckBox.setText(resourceMap.getString("useServerAclCheckBox.text")); // NOI18N
        useServerAclCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        useServerAclCheckBox.setName("useServerAclCheckBox"); // NOI18N

        startActiveCheckbox.setSelected(true);
        startActiveCheckbox.setText(resourceMap.getString("startActiveCheckbox.text")); // NOI18N
        startActiveCheckbox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        startActiveCheckbox.setName("startActiveCheckbox"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(maxUsersField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addComponent(startActiveCheckbox)
                                .addGap(18, 18, 18)
                                .addComponent(useServerAclCheckBox))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(contextNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(contextTypeComboBox, 0, 274, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(contextTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(contextNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(maxUsersField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(useServerAclCheckBox)
                    .addComponent(startActiveCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea contextDescriptionField;
    private javax.swing.JTextField contextNameField;
    private javax.swing.JComboBox contextTypeComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField maxUsersField;
    private javax.swing.JCheckBox startActiveCheckbox;
    private javax.swing.JCheckBox useServerAclCheckBox;
    // End of variables declaration//GEN-END:variables

}
