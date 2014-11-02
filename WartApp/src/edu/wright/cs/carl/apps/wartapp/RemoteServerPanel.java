
package edu.wright.cs.carl.apps.wartapp;

import edu.wright.cs.carl.net.event.ServerEvent;
import edu.wright.cs.carl.net.event.ServerEventListener;
import org.jdesktop.application.Action;

import java.io.IOException;

import java.util.*;

import javax.swing.*;

import edu.wright.cs.carl.net.context.*;

import edu.wright.cs.carl.net.message.*;

import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.net.view.*;

import edu.wright.cs.carl.security.*;

import edu.wright.cs.carl.swing.*;

import edu.wright.cs.carl.apps.wartapp.swing.*;


/**
 * TODO: Refresh user and context lists.
 *
 * @author  Duane Bolick
 */
public class RemoteServerPanel extends TabPanel implements ServerEventListener
{
    private String serverID;
    private String serverName;
    private String username;
    private String hostname;

    private DefaultListModel contextListModel;
    private DefaultListModel userListModel;


    public RemoteServerPanel(String serverID, String serverName, String username, String hostname)
    {
        this.serverID = serverID;
        this.serverName = serverName;
        this.username = username;
        this.hostname = hostname;

        this.contextListModel = new DefaultListModel();
        this.userListModel = new DefaultListModel();

        this.setName(hostname);

        initComponents();

        this.refreshContextListModel();
        this.refreshUserListModel();
    }

    @Override
    public String getName()
    {
        return "Remote Server: [" + this.serverName + "]";
    }

    /**
     * Called when the enclosing tab in a JTabbedPane is closed by clicking the
     * x-button.
     */
    @Override
    public void tabClosed()
    {
        this.disconnectFromServer();
    }

    @Override
    public void pleaseClose()
    {
        /*
        try {
            this.contextHandle.disconnect(null);
        }
        catch(IOException e) {
            e.printStackTrace();
            this.lostConnection(e);
        }
         */

        ((ScrollingTab)(this.getParent()).getParent()).pleaseClose();
    }

    private void lostConnection(IOException e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this.getRootPane(), "Lost Connection with the server.");
        WartApp.getApplication().getClient().serverConnectionLost(this.serverID);
        this.pleaseClose();
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

            case SERVER_CONNECTION_LOST:
                this.pleaseClose();
        }
    }

    public void refreshUserListModel()
    {
        this.userListModel.clear();

        Iterator<String> it = null;

        try {
            it = WartApp.getApplication().getClient().getServerHandle(this.serverID).getClientList().iterator();
        }
        catch(IOException e) {
            this.lostConnection(e);
            return;
        }

        while(it.hasNext()) {
            this.userListModel.addElement(it.next());
        }
    }

    public void refreshContextListModel()
    {
        this.contextListModel.clear();

        Iterator<String> it = null;
        
        try {
            it = WartApp.getApplication().getClient().getServerHandle(this.serverID).getContextList().iterator();
        }
        catch(IOException e) {
            this.lostConnection(e);
            return;
        }

        ContextInformation currentInfo = null;
        String currentContextID = null;

        while(it.hasNext()) {
            currentContextID = it.next();
            try {
                currentInfo =
                        WartApp.getApplication().getClient().getServerHandle(this.serverID).getContextInformation(currentContextID);
            }
            catch(IOException e) {
                this.lostConnection(e);
                return;
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        contextList = new javax.swing.JList();
        jToolBar2 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jLabel1.setText("Server Name: " + this.serverName);
        jLabel1.setName("jLabel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(RemoteServerPanel.class);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        userList.setModel(this.userListModel);
        userList.setName("userList"); // NOI18N
        jScrollPane1.setViewportView(userList);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(RemoteServerPanel.class, this);
        jButton1.setAction(actionMap.get("sendPrivateMessage")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        contextList.setModel(this.contextListModel);
        contextList.setName("contextList"); // NOI18N
        jScrollPane2.setViewportView(contextList);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        jButton2.setAction(actionMap.get("joinContext")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton2);

        jLabel3.setText("Connected to " + this.serverName + " as " + this.username);
        jLabel3.setName("jLabel3"); // NOI18N

        jButton3.setAction(actionMap.get("disconnectFromServer")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 441, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jButton3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void disconnectFromServer()
    {
        try {
            WartApp.getApplication().getClient().disconnectFromServer(this.serverID);
        }
        catch(ServerException e) {
            //
            // This should never happen.  disconnectFromServer only throws a
            // ServerException if we're not connected to the server.
            //
            throw new AssertionError(e);
        }
    }

    /**
     * This method joins a context, and creates all the required GUI components
     * for viewing it.  It then adds those components to the main GUI tabbed
     * pane.
     *
     * The View itself is responsible for cleanup (i.e., removing its own GUI
     * components).
     */
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
        View newContextView = WartAppConstants.GetViewInstance(type, this.serverID , newContextViewPanel);

        //
        // Join the context.
        //
        try {
            if(WartApp.getApplication().getClient().joinContext(this.serverID, contextID, newContextView, true) == false) {
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
        if(recipientName.equals(this.username)) {
            return;
        }

        String message = JOptionPane.showInputDialog(WartApp.getApplication().getMainFrame(), "Send Message to " + recipientName);

        PrivateMessage pm = new PrivateMessage(message, recipientName, this.username, this.serverName, this.serverID);

        try {
            WartApp.getApplication().getClient().getServerHandle(this.serverID).sendMessagePayload(pm);
        }
        catch(MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WartApp.getApplication().getMainFrame(), e.getMessage());
            return;
        }
        catch(MessageTypeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WartApp.getApplication().getMainFrame(), e.getMessage());
            return;
        }
        catch(PermissionException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WartApp.getApplication().getMainFrame(), e.getMessage());
            return;
        }
        catch(IOException e) {
            e.printStackTrace();
            this.lostConnection(e);
            return;
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList contextList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables

}
