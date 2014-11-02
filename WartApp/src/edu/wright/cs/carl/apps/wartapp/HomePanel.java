
package edu.wright.cs.carl.apps.wartapp;

import edu.wright.cs.carl.net.event.ServerEvent;
import edu.wright.cs.carl.net.event.ServerEventListener;
import org.jdesktop.application.Action;

import java.io.IOException;

import java.util.*;

import javax.swing.*;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.handle.LocalServerHandle;

import edu.wright.cs.carl.net.message.*;
import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.net.view.*;

import edu.wright.cs.carl.security.PermissionException;

import edu.wright.cs.carl.swing.*;

import edu.wright.cs.carl.apps.wartapp.swing.*;


/**
 * This class represents a "homepage" tab in the main tabbed pane of the GUI.
 * All the controls for administering your own server are in this component.
 *
 * @author  Duane Bolick
 */
public class HomePanel extends TabPanel implements ServerEventListener
{
    private DefaultListModel contextListModel;
    private DefaultListModel userListModel;

    private String serverInactiveToggleButtonString = "Start Server";
    private String serverActiveToggleButtonString = "Stop Server";
    private String serverInactiveString = "Not Active";
    private String serverActiveString = "Active";
    private boolean isServerActive = false;
    private String serverStatus = serverInactiveString;

    /** Creates new form HomePanel */
    public HomePanel()
    {
        this.contextListModel = new DefaultListModel();
        this.userListModel = new DefaultListModel();

        initComponents();

        this.refreshContextListModel();
        this.refreshUserListModel();
    }

    /**
     * Receive a change in the Server's state.
     *
     * @param   stateChange     [in]    Supplies the change.
     */
    @Override
    public void serverEventOccurred(ServerEvent event)
    {
        switch(event.type) {
            case USER_JOINED:
                this.userListModel.addElement(event.eventString);
                break;

            case USER_LEFT:
                this.userListModel.removeElement(event.eventString);
                break;

            case CONTEXT_ADDED:
                this.refreshContextListModel();
                break;

            case CONTEXT_REMOVED:
                this.refreshContextListModel();
                break;

            case CONTEXT_EVENT:
                this.refreshContextListModel();
                break;
        }
    }

    public String getTabName()
    {
        return this.getName();
    }

    /**
     * Called when the enclosing tab in a JTabbedPane is closed by clicking the
     * x-button.  Since this is the "Home Tab," we can ignore this.
     */
    @Override
    public void tabClosed()
    {

    }

    /**
     * This method is called when a tab is closed by the program.  But since
     * this is the home tab, this'll never happen.
     */
    @Override
    public void pleaseClose()
    {
        
    }

    @Action
    public void sendPrivateMessage()
    {
        int selectedIndex = this.userList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        String recipientName = (String) this.userList.getSelectedValue();

        //
        // Don't send messages to yourself.
        //
        if(recipientName.equals(WartApp.getApplication().getLocalUser().getName())) {
            return;
        }

        String message = JOptionPane.showInputDialog(WartApp.getApplication().getMainFrame(), "Send Message to " + recipientName);

        PrivateMessage pm = new PrivateMessage(
                                    message,
                                    recipientName,
                                    WartApp.getApplication().getLocalUser().getName(),
                                    WartApp.getApplication().getServer().getName(),
                                    WartApp.getApplication().getServer().getUniqueID());

        ClientMessage cm = new ClientMessage(WartApp.getApplication().getLocalUser(), pm);

        try {
            WartApp.getApplication().getServer().handleIncomingMessage(cm);
            //WartApp.getApplication().getClient().getServerHandle(WartApp.getApplication().getServer().getUniqueID()).sendMessagePayload(pm);
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WartApp.getApplication().getMainFrame(), e.getMessage());
            return;
        }
    }


    @Action
    public void kickUser()
    {
        int selectedIndex = this.userList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        String kickedUsername = (String) this.userList.getSelectedValue();

        //
        // Don't kick yourself.
        //
        if(kickedUsername.equals(WartApp.getApplication().getLocalUser().getName())) {
            return;
        }

        UserRemovalRequest r = new UserRemovalRequest(kickedUsername);

        ClientMessage cm = new ClientMessage(WartApp.getApplication().getLocalUser(), r);

        try {
            WartApp.getApplication().getServer().handleIncomingMessage(cm);
            //WartApp.getApplication().getClient().getServerHandle(WartApp.getApplication().getServer().getUniqueID()).sendMessagePayload(pm);
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WartApp.getApplication().getMainFrame(), e.getMessage());
            return;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        serverStatusLabel = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        jToolBar2 = new javax.swing.JToolBar();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        contextList = new javax.swing.JList();
        jToolBar3 = new javax.swing.JToolBar();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        serverNameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        serverToggleButton = new javax.swing.JToggleButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(HomePanel.class);
        setBackground(resourceMap.getColor("Home.background")); // NOI18N
        setAutoscrolls(true);
        setName("Home"); // NOI18N

        jLabel1.setText("You are logged in as " + WartApp.getApplication().getLocalUser().getName());
        jLabel1.setName("jLabel1"); // NOI18N

        serverStatusLabel.setText(this.serverStatus);
        serverStatusLabel.setName("serverStatusLabel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(HomePanel.class, this);
        jButton6.setAction(actionMap.get("editServerSettings")); // NOI18N
        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        userList.setModel(this.userListModel);
        userList.setName("userList"); // NOI18N
        jScrollPane1.setViewportView(userList);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        jButton8.setAction(actionMap.get("sendPrivateMessage")); // NOI18N
        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setName("jButton8"); // NOI18N
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton8);

        jButton9.setAction(actionMap.get("kickUser")); // NOI18N
        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setName("jButton9"); // NOI18N
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton9);

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        contextList.setModel(this.contextListModel);
        contextList.setName("contextList"); // NOI18N
        jScrollPane2.setViewportView(contextList);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setName("jToolBar3"); // NOI18N

        jButton7.setAction(actionMap.get("joinContext")); // NOI18N
        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setName("jButton7"); // NOI18N
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton7);

        jButton10.setAction(actionMap.get("activateContext")); // NOI18N
        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setName("jButton10"); // NOI18N
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton10);

        jButton11.setAction(actionMap.get("deactivateContext")); // NOI18N
        jButton11.setText(resourceMap.getString("jButton11.text")); // NOI18N
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setName("jButton11"); // NOI18N
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton11);

        jButton12.setAction(actionMap.get("addContext")); // NOI18N
        jButton12.setText(resourceMap.getString("jButton12.text")); // NOI18N
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setName("jButton12"); // NOI18N
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton12);

        jButton13.setAction(actionMap.get("removeContext")); // NOI18N
        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setName("jButton13"); // NOI18N
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton13);

        serverNameField.setText(WartApp.getApplication().getServer().getName());
        serverNameField.setName("serverNameField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        serverToggleButton.setAction(actionMap.get("toggleServer")); // NOI18N
        serverToggleButton.setText(this.serverInactiveToggleButtonString);
        serverToggleButton.setName("serverToggleButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverToggleButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 290, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverStatusLabel))
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(serverNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverToggleButton))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(serverStatusLabel)
                    .addComponent(jLabel6))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Toggle the server between running and not running.
     */
    @Action
    public void toggleServer()
    {
        if(this.isServerActive == false) {

            WartApp.getApplication().getConfiguration().setServerName(this.serverNameField.getText());

            try {
                WartApp.getApplication().startServer();
            }
            catch(ServerException e) {
                JOptionPane.showMessageDialog(WartApp.getApplication().getMainFrame(), e.getMessage());
                return;
            }
            this.setServerStatus(true);
            return;
        }
        else if(this.isServerActive == true) {
            WartApp.getApplication().stopServer();
            this.setServerStatus(false);
            return;
        }
    }

    /**
     * This method is used to set all the GUI components to indicate if the
     * server is running or not.  This method does not actually start or
     * stop the server. 
     * 
     * @param   isRunning   [in]    Supplies whether or not the server is
     *                              running, which will be used to set the UI
     *                              components appropriately.
     */
    public void setServerStatus(boolean isRunning)
    {
        if(isRunning == true) {
            this.serverToggleButton.setText(this.serverActiveToggleButtonString);
            this.serverStatusLabel.setText(this.serverActiveString);
            this.isServerActive = true;
            this.serverNameField.setEditable(false);
            return;
        }
        else if(isRunning == false) {
            this.serverToggleButton.setText(this.serverInactiveToggleButtonString);
            this.serverStatusLabel.setText(this.serverInactiveString);
            this.isServerActive = false;
            this.serverNameField.setEditable(true);
            this.updateServerNameField();
            return;
        }
    }

    /**
     * Update the server name field from the saved configuration.
     */
    public void updateServerNameField()
    {
        if(this.isServerActive == false) {
            this.serverNameField.setText(WartApp.getApplication().getConfiguration().getServerName());
        }
    }

    /**
     * Edit the server settings.
     */
    @Action
    public void editServerSettings()
    {
        WartApp.getApplication().show(new ServerSettings());

        this.updateServerNameField();        
    }

    /**
     * Activate the selected context.
     */
    @Action
    public void activateContext()
    {
        int selectedIndex = this.contextList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        String contextID = ((ContextListEntry)this.contextList.getSelectedValue()).getContextID();

        try {
            WartApp.getApplication().getServer().activateContext(contextID);
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }
        catch(ContextException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }

        this.refreshContextListModel();
    }

    /**
     * Deactivate the selected Context
     */
    @Action
    public void deactivateContext()
    {
        int selectedIndex = this.contextList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        String contextID = ((ContextListEntry)this.contextList.getSelectedValue()).getContextID();
        
        try {
            WartApp.getApplication().getServer().deactivateContext(contextID);
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }
        catch(ContextException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }

        this.refreshContextListModel();
    }

    /**
     * Create a new context and add it to this server.
     */
    @Action
    public void addContext()
    {
        WartApp.getApplication().show(new AddContext());
        this.refreshContextListModel();
    }

    /**
     * Remove the selected Context.
     */
    @Action
    public void removeContext()
    {
        int selectedIndex = this.contextList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        String contextID = ((ContextListEntry)this.contextList.getSelectedValue()).getContextID();
        Context removedContext = null;

        try {
            removedContext =
                    WartApp.getApplication().getServer().removeContext(WartApp.getApplication().getLocalUser(), contextID);
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }

        this.refreshContextListModel();
    }

    /**
     * Refresh the components of this panel.
     */
    @Override
    public void refreshPanelComponents()
    {
        this.refreshUserListModel();
        this.refreshContextListModel();
    }

    @Action
    public void refreshUserListModel()
    {
        this.userListModel.clear();

        Iterator<String> it = WartApp.getApplication().getServer().getClientList().iterator();
        while(it.hasNext()) {
            this.userListModel.addElement(it.next());
        }
    }

    /**
     * Refresh the list model containing all the hosted contexts.
     */
    public void refreshContextListModel()
    {
        this.contextListModel.clear();

        Iterator<String> it = WartApp.getApplication().getServer().getContextList().iterator();
        ContextInformation currentInfo = null;
        String currentcontextID = null;
        while(it.hasNext()) {
            currentcontextID = it.next();
            try {
                currentInfo = WartApp.getApplication().getServer().getContextInformation(currentcontextID);
            }
            catch(ServerException e) {
                //
                // This should never happen.
                //
                throw new AssertionError(e);
            }
            this.contextListModel.addElement(new ContextListEntry(currentInfo));
        }
    }

    @Action
    public void joinContext()
    {
        int selectedIndex = this.contextList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }

        //
        // Determine which context we're joining.
        //
        String contextID = ((ContextListEntry)this.contextList.getSelectedValue()).getContextID();
        WartAppConstants.ContextType type = ((ContextListEntry)this.contextList.getSelectedValue()).getContextType();

        //
        // Construct GUI components
        //
        ViewPanel newContextViewPanel = WartAppConstants.GetViewPanelInstance(type);
        View newContextView = WartAppConstants.GetViewInstance(type, WartApp.getApplication().getServer().getUniqueID() , newContextViewPanel);

        //
        // Join the context.
        //
        try {
            if(WartApp.getApplication().getClient().joinContext(WartApp.getApplication().getServer().getUniqueID(), contextID, newContextView, true) == false) {
                JOptionPane.showMessageDialog(this.getRootPane(), "Context is full.");
                return;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), "Failed Joining Context: " + e.getMessage());
            return;
        }

        //
        // Add the constructed GUI elements to the main tabbed pane.
        //
        ((WartView)WartApp.getApplication().getMainView()).addTab(newContextViewPanel, true);
    }

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList contextList;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JTextField serverNameField;
    private javax.swing.JLabel serverStatusLabel;
    private javax.swing.JToggleButton serverToggleButton;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables

}
