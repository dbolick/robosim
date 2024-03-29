/*
 * ManageConnections.java
 *
 * Created on December 8, 2008, 9:19 PM
 */

package edu.wright.cs.carl.apps.wartapp;

import org.jdesktop.application.Action;

import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;

import java.util.*;

import javax.swing.*;

import edu.wright.cs.carl.net.client.Client;

import edu.wright.cs.carl.security.SecUtils;

import edu.wright.cs.carl.apps.wartapp.connection.SavedConnection;


/**
 * Manage saved connections.
 *
 * @author  Duane Bolick
 */
public class ManageConnections extends javax.swing.JDialog
{
    private WartConfig config;
    private Client client;

    private DefaultListModel connectionListModel;
    private DefaultComboBoxModel protocolComboBoxModel;

    private String newConnectionHostname = "New Connection";

    public ManageConnections()
    {
        super(WartApp.getApplication().getMainFrame(), true);

        this.config = WartApp.getApplication().getConfiguration();
        this.client = WartApp.getApplication().getClient();

        this.connectionListModel = new DefaultListModel();
        this.refreshConnectionListModel();

        this.protocolComboBoxModel = new DefaultComboBoxModel(WartAppConstants.GetSupportedProtocolTypes());

        initComponents();
    }

    public ManageConnections(String hostName)
    {
        super(WartApp.getApplication().getMainFrame(), true);
        
        this.config = WartApp.getApplication().getConfiguration();
        this.client = WartApp.getApplication().getClient();

        this.connectionListModel = new DefaultListModel();
        this.refreshConnectionListModel();

        this.protocolComboBoxModel = new DefaultComboBoxModel(WartAppConstants.GetSupportedProtocolTypes());

        if(hostName != null && hostName.isEmpty() == false) {
            newConnectionHostname = hostName;
        }

        initComponents();
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
        connectionList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        portField = new javax.swing.JTextField();
        protocolComboBox = new javax.swing.JComboBox();
        usernameField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        passwordConfirmField = new javax.swing.JPasswordField();
        jButton6 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(ManageConnections.class);
        setTitle(resourceMap.getString("ConnectionManagerForm.title")); // NOI18N
        setName("ConnectionManagerForm"); // NOI18N
        setResizable(false);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        connectionList.setModel(this.connectionListModel);
        connectionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        connectionList.setName("connectionList"); // NOI18N
        jScrollPane1.setViewportView(connectionList);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        hostField.setText(this.newConnectionHostname);
        hostField.setName("hostField"); // NOI18N

        portField.setColumns(4);
        portField.setText(resourceMap.getString("portField.text")); // NOI18N
        portField.setName("portField"); // NOI18N

        protocolComboBox.setModel(this.protocolComboBoxModel);
        protocolComboBox.setName("protocolComboBox"); // NOI18N

        usernameField.setText(resourceMap.getString("usernameField.text")); // NOI18N
        usernameField.setName("usernameField"); // NOI18N

        passwordField.setText(resourceMap.getString("passwordField.text")); // NOI18N
        passwordField.setName("passwordField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(ManageConnections.class, this);
        jButton3.setAction(actionMap.get("deleteConnection")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        jButton4.setAction(actionMap.get("loadConnection")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        passwordConfirmField.setText(resourceMap.getString("passwordConfirmField.text")); // NOI18N
        passwordConfirmField.setName("passwordConfirmField"); // NOI18N

        jButton6.setAction(actionMap.get("saveConnection")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N

        jButton2.setAction(actionMap.get("connect")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jButton1.setAction(actionMap.get("closeConnectionManager")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(protocolComboBox, 0, 127, Short.MAX_VALUE))
                            .addComponent(hostField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(passwordConfirmField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addComponent(jLabel2)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(protocolComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(passwordConfirmField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-285)/2, (screenSize.height-396)/2, 285, 396);
    }// </editor-fold>//GEN-END:initComponents

    public void refreshConnectionListModel()
    {
        this.connectionListModel.clear();
        Iterator<String> it = this.config.getSavedConnectionNames().iterator();
        while(it.hasNext()) {
            this.connectionListModel.addElement(it.next());
        }
    }

    @Action
    public void connect()
    {
        //
        // Since the WartView.connectToServer method takes a SavedConnection as
        // an argument, let's save what we've got in the input fields:
        //
        this.saveConnection();

        //
        // Now that it's saved, let's get it, and try to connect.
        //
        SavedConnection toConnect = WartApp.getApplication().getConfiguration().getSavedConnection(this.hostField.getText());
        
        //
        // If success, close the window.
        //
        if(((WartView)WartApp.getApplication().getMainView()).connectToServer(toConnect) == true) {
            this.closeConnectionManager();
            return;
        }
    }

    @Action
    public void closeConnectionManager()
    {
        this.setVisible(false);
        this.dispose();
    }

    @Action
    public void saveConnection()
    {
        String host = this.hostField.getText();
        String port = this.portField.getText();
        String typeString = (String)this.protocolComboBox.getSelectedItem();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String confirmPassword = this.passwordConfirmField.getText();
        String passwordHash = null;

        if(password.equals(confirmPassword) == false) {
            JOptionPane.showMessageDialog(rootPane, "Passwords Don't Match!");
            return;
        }

        try {
            passwordHash = SecUtils.getHash(this.passwordField.getText(), "MD5");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(0);
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(0);
        }

        SavedConnection newConnection = new SavedConnection(host);
        newConnection.setPort(port);
        newConnection.setType(WartAppConstants.GetProtocolTypeFromString(typeString));
        newConnection.getCredentials().setName(username);
        newConnection.getCredentials().setPasswordHash(passwordHash);

        this.config.putSavedConnection(newConnection);
        this.refreshConnectionListModel();
    }

    @Action
    public void loadConnection()
    {
        if(this.connectionList.getSelectedIndex() < 0) {
            return;
        }

        String selectedName = (String)this.connectionList.getSelectedValue();

        SavedConnection connectionToLoad = this.config.getSavedConnection(selectedName);
        this.hostField.setText(connectionToLoad.getHost());
        this.portField.setText(connectionToLoad.getPort());
        this.protocolComboBox.setSelectedItem(connectionToLoad.getType());
        this.usernameField.setText(connectionToLoad.getCredentials().getName());
    }

    @Action
    public void deleteConnection()
    {
        if(this.connectionList.getSelectedIndex() < 0) {
            return;
        }

        String selectedName = (String)this.connectionList.getSelectedValue();

        this.config.getAllSavedConnectionMap().remove(selectedName);
        this.refreshConnectionListModel();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList connectionList;
    private javax.swing.JTextField hostField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField passwordConfirmField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField portField;
    private javax.swing.JComboBox protocolComboBox;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables

}
