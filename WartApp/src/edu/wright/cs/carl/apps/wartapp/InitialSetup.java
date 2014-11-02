/*
 * InitialSetup.java
 *
 * Created on November 23, 2008, 5:35 PM
 */

package edu.wright.cs.carl.apps.wartapp;

import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

import java.security.NoSuchAlgorithmException;

import edu.wright.cs.carl.security.SecUtils;
import edu.wright.cs.carl.security.DefaultUserCredentials;
import edu.wright.cs.carl.security.UserCredentials;

import edu.wright.cs.carl.security.accounts.AccountManager;



/**
 *
 * @author  Duane
 */
public class InitialSetup extends javax.swing.JDialog {

    private AccountManager accounts;
    private UserCredentials rootUser;
    
    /** Creates new form InitialSetup */
    public InitialSetup(java.awt.Frame parent, boolean modal, AccountManager accounts, UserCredentials rootUser)
    {
        super(parent, modal);
        this.accounts = accounts;
        this.rootUser = rootUser;

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
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        passwordConfirm = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(InitialSetup.class);
        setTitle(resourceMap.getString("InitialSetupForm.title")); // NOI18N
        setModal(true);
        setName("InitialSetupForm"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        username.setText(resourceMap.getString("username.text")); // NOI18N
        username.setName("username"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        password.setText(resourceMap.getString("password.text")); // NOI18N
        password.setName("password"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel5.setName("jLabel5"); // NOI18N

        passwordConfirm.setText(resourceMap.getString("passwordConfirm.text")); // NOI18N
        passwordConfirm.setName("passwordConfirm"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(InitialSetup.class, this);
        jButton1.setAction(actionMap.get("createRootAccount")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MD5 Hashed Password", "SHA Hashed Password", "One-Time Password" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setName("jLabel6"); // NOI18N

        jButton2.setAction(actionMap.get("quit")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                            .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, 243, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passwordConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(284, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InitialSetup dialog = new InitialSetup(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

     */
    
    @Action
    public void createRootAccount()
    {
        String rootPassword = this.password.getText();
        String confirmPassword = this.passwordConfirm.getText();
        
        if(rootPassword.equals(confirmPassword) == false) {
            JOptionPane.showMessageDialog(rootPane, "Passwords Don't Match!");
            return;
        }
        
        try {
            rootUser.setName(username.getText());
            rootUser.setPasswordHash(SecUtils.getHash(rootPassword, "MD5"));
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        
        this.accounts.addUserAccount(rootUser);
        
        this.setVisible(false);
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField password;
    private javax.swing.JPasswordField passwordConfirm;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables

}