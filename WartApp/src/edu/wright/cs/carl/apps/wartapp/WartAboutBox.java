/*
 * WartAboutBox.java
 */

package edu.wright.cs.carl.apps.wartapp;

import org.jdesktop.application.Action;

public class WartAboutBox extends javax.swing.JDialog {

    public WartAboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(jButton1);
    }

    @Action public void closeAboutBox() {
        setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(WartAboutBox.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setModal(true);
        setName("aboutBox"); // NOI18N
        setResizable(false);

        jLayeredPane1.setBackground(resourceMap.getColor("jLayeredPane1.background")); // NOI18N
        jLayeredPane1.setName("jLayeredPane1"); // NOI18N
        jLayeredPane1.setOpaque(true);

        jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.setBounds(0, 0, 500, 380);
        jLayeredPane1.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(WartAboutBox.class, this);
        jButton1.setAction(actionMap.get("closeAboutBox")); // NOI18N
        jButton1.setBackground(resourceMap.getColor("jButton1.background")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setBounds(430, 350, 59, 23);
        jLayeredPane1.add(jButton1, javax.swing.JLayeredPane.PALETTE_LAYER);

        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jLabel2.setBounds(200, 260, 240, 40);
        jLayeredPane1.add(jLabel2, javax.swing.JLayeredPane.PALETTE_LAYER);

        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jLabel3.setBounds(200, 300, 290, 70);
        jLayeredPane1.add(jLabel3, javax.swing.JLayeredPane.PALETTE_LAYER);

        jLabel4.setForeground(resourceMap.getColor("jLabel4.foreground")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        jLabel4.setBounds(440, 220, 54, 14);
        jLayeredPane1.add(jLabel4, javax.swing.JLayeredPane.PALETTE_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLayeredPane jLayeredPane1;
    // End of variables declaration//GEN-END:variables
    
}
