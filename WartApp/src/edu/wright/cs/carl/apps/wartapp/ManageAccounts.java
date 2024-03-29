/*
 * ManageAccounts.java
 *
 * Created on January 17, 2009, 6:54 PM
 */

package edu.wright.cs.carl.apps.wartapp;

import edu.wright.cs.carl.apps.wartapp.swing.AccountListEntry;
import java.security.acl.AclEntry;
import java.security.acl.NotOwnerException;

import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import edu.wright.cs.carl.apps.wartapp.swing.*;

import edu.wright.cs.carl.net.server.Server;
import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.security.accounts.AccountManager;
import edu.wright.cs.carl.security.permissions.*;
import edu.wright.cs.carl.security.*;

import org.jdesktop.application.Action;

/**
 *
 * @author  Duane
 */
public class ManageAccounts extends javax.swing.JDialog {

    private Server server;
    private UserCredentials localUser;
    
    private DefaultListModel accountListModel;
    
    /** Creates new form ManageAccounts */
    public ManageAccounts()
    {
        super(WartApp.getApplication().getMainFrame(), true);
        
        this.server = WartApp.getApplication().getServer();
        this.localUser = WartApp.getApplication().getLocalUser();
        
        this.accountListModel = new DefaultListModel();
        
        //
        // Build accountListModel
        //
        Iterator<String> it = this.server.getAccounts().getAccountList().iterator();
        while(it.hasNext()) {
            this.accountListModel.addElement(new AccountListEntry(this.server.getAccounts().getAccount(it.next())));
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

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        accountList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(ManageAccounts.class);
        setTitle(resourceMap.getString("ManageAccountsForm.title")); // NOI18N
        setName("ManageAccountsForm"); // NOI18N
        setResizable(false);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(ManageAccounts.class, this);
        jButton1.setAction(actionMap.get("createNewAccount")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("editAccount")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setAction(actionMap.get("removeAccount")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        jButton4.setAction(actionMap.get("closeManageAccounts")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        accountList.setModel(this.accountListModel);
        accountList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        accountList.setName("accountList"); // NOI18N
        jScrollPane1.setViewportView(accountList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void removeAccount()
    {
        int index = this.accountList.getSelectedIndex();
        if(index < 0) {
            return;
        }
        
        try {
            this.server.deleteAccount(localUser, ((AccountListEntry)this.accountList.getSelectedValue()).getCredentials().getName());
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage()); 
            return;
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }
        
        this.accountListModel.remove(index);
    }

    @Action
    public void editAccount()
    {
        int selectedIndex = this.accountList.getSelectedIndex();
        if(selectedIndex < 0) {
            return;
        }
        
        UserCredentials modifiedAccount = new DefaultUserCredentials();
        UserCredentials originalAccount = ((AccountListEntry)this.accountList.getSelectedValue()).getCredentials();
        modifiedAccount.setName(originalAccount.getName());
        
        WartApp.getApplication().show(new EditAccount(
                                                WartApp.getApplication().getMainFrame(),
                                                true,
                                                modifiedAccount));    
        
        if(modifiedAccount == null) {
            return;
        }
 
        try {
            this.server.modifyAccount(localUser, originalAccount.getName(), modifiedAccount);
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage()); 
            return;
        }        
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }         
        
        this.accountListModel.remove(selectedIndex);
        this.accountListModel.addElement(new AccountListEntry(modifiedAccount));
    }

    @Action
    public void createNewAccount()
    {
        UserCredentials newAccount = new DefaultUserCredentials();
        WartApp.getApplication().show(new EditAccount(
                                                WartApp.getApplication().getMainFrame(),
                                                true,
                                                newAccount)); 
        
        if(newAccount == null || newAccount.getName() == null) {
            return;
        }
        
        boolean success = false;
        
        try {
            success = this.server.addAccount(localUser, newAccount);
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }        
        
        if(success == false) {
            JOptionPane.showMessageDialog(this.getRootPane(), "An account with this name already exists.");
            return;
        }
        
        this.accountListModel.addElement(new AccountListEntry(newAccount));
    }

    @Action
    public void closeManageAccounts()
    {
        this.setVisible(false);
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList accountList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
