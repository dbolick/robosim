/**
 * TODO: Modular Robot Editor
 * TODO: Robot Instrumentation
 * TODO: Webcam/QT viewer for RealContext
 * TODO: Private Messaging
 */

package edu.wright.cs.carl.apps.wartapp;

import edu.wright.cs.carl.swing.ScrollingTab;
import org.openide.awt.TabbedPaneFactory;

import java.io.IOException;

import java.security.acl.*;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wright.cs.carl.net.connection.Connector;

import edu.wright.cs.carl.net.context.Context;
import edu.wright.cs.carl.net.context.ContextException;

import edu.wright.cs.carl.net.handle.*;

import edu.wright.cs.carl.net.server.ServerException;

import edu.wright.cs.carl.net.view.View;
import edu.wright.cs.carl.net.view.ViewPanel;

import edu.wright.cs.carl.security.*;

import edu.wright.cs.carl.apps.wartapp.swing.*;

import edu.wright.cs.carl.apps.wartapp.connection.SavedConnection;

import edu.wright.cs.carl.wart.sim2D.AgentWorldSim2D;
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

import edu.wright.cs.carl.swing.*;

import edu.wright.cs.carl.net.message.payload.*;

import edu.wright.cs.carl.net.server.*;

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
 * The application's main frame.
 */
public class WartView extends FrameView implements PrivateMessageListener
{
    //
    // GUI component data models.  Anytime you need to use a swing GUI component
    // to represent some data structure or "manager," you'll probably need a
    // swing data model for it.
    //
    private DefaultListModel        hostedContextListModel;
    public DefaultListModel         connectedUserListModel;
    private DefaultListModel        joinedServerListModel;        
    private DefaultListModel        joinedContextListModel;

    private DefaultComboBoxModel    savedConnectionComboBoxModel;

    private HomePanel homePanel;
    private boolean isHomePanelOpen;
    
    public WartView(SingleFrameApplication app)
    {
        super(app);
        
        this.hostedContextListModel = new DefaultListModel();
        this.connectedUserListModel = new DefaultListModel();
        this.joinedServerListModel = new DefaultListModel();
        this.joinedContextListModel = new DefaultListModel();   
        this.savedConnectionComboBoxModel =
                new DefaultComboBoxModel(
                    new Vector<String>(WartApp.getApplication().getConfiguration().getSavedConnectionNames()));

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });      
    }

    /**
     * Receive and handle a PrivateMessage.
     *
     * @param   pm  [in]    Supplies the PrivateMessage.
     */
    @Override
    public void receivePrivateMessage(PrivateMessage pm)
    {
        JOptionPane.showMessageDialog(
                        WartApp.getApplication().getMainFrame(),
                        pm.text,
                        "Message From "+ pm.senderName + " on " + pm.serverName,
                        JOptionPane.QUESTION_MESSAGE);       
    }


    public HomePanel getHomePanel()
    {
        return this.homePanel;
    }

    /**
     * Create the "homepage" JPanel in the main tabbed pane.
     */
    public void createHomePanel()
    {
        this.isHomePanelOpen = true;
        this.homePanel = new HomePanel();
        ScrollingTab tab = new ScrollingTab(this.homePanel);
        this.mainTabbedPane.add(tab);
        this.mainTabbedPane.setSelectedComponent(tab);
    }

    @Action
    public void openHomepage()
    {
        //
        // Don't create more than one home tab.
        //
        if(this.isHomePanelOpen) {
            return;
        }

        this.isHomePanelOpen = true;
        ScrollingTab tab = new ScrollingTab(this.homePanel);
        this.mainTabbedPane.add(tab, 0);
        this.mainTabbedPane.setSelectedComponent(tab);
    }

    /**
     * Add a tab.
     * 
     * @param   tabPanel    [in]    Supplies the tab panel.
     * @param   selected    [in]    Supplies whether or not the newly-added tab
     *                              is selected.
     */
    public void addTab(TabPanel tabPanel, boolean selected)
    {
        ScrollingTab tab = new ScrollingTab(tabPanel);
        this.mainTabbedPane.add(tab);
        if(selected) {
            this.mainTabbedPane.setSelectedComponent(tab);
        }
    }


    public void removeTab()
    {
        //this.mainTabbedPane.re
    }



    /**
     * Get a reference to the main tabbed pane.  Other GUI components may need
     * this to manipulate tabs.  For example, a tab that's a remote server
     * lobby needs to be able to remove itself on disconnecting, and also
     * add and remove context tabs as the user joins and leaves contexts.
     * 
     * @return  A reference to the main tabbed pane.
     */
    public JTabbedPane getMainTabbedPane()
    {
        return this.mainTabbedPane;
    }

    /**
     * Refresh the data model for the address bar.
     */
    public void refreshSavedConnectionComboBoxModel()
    {
        this.savedConnectionComboBoxModel.removeAllElements();
        Iterator<String> it = WartApp.getApplication().getConfiguration().getSavedConnectionNames().iterator();
        while(it.hasNext()) {
            this.savedConnectionComboBoxModel.addElement(it.next());
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = WartApp.getApplication().getMainFrame();
            aboutBox = new WartAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        WartApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        mainTabbedPane = TabbedPaneFactory.createCloseButtonTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        addressBarComboBox = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        mainTabbedPane.setName("mainTabbedPane"); // NOI18N
        mainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabbedPaneStateChanged(evt);
            }
        });
        mainTabbedPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mainTabbedPanePropertyChange(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        addressBarComboBox.setEditable(true);
        addressBarComboBox.setModel(this.savedConnectionComboBoxModel);
        addressBarComboBox.setName("addressBarComboBox"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getActionMap(WartView.class, this);
        jButton1.setAction(actionMap.get("connectToServerByAddresBar")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.wright.cs.carl.apps.wartapp.WartApp.class).getContext().getResourceMap(WartView.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton5.setAction(actionMap.get("openHomepage")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressBarComboBox, 0, 655, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(addressBarComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)))
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator6.setName("jSeparator6"); // NOI18N
        jToolBar1.add(jSeparator6);

        jButton2.setAction(actionMap.get("chatroomShortcut")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jButton3.setAction(actionMap.get("simShortcut")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        jButton4.setAction(actionMap.get("realAgentShortcut")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar1.add(jSeparator7);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setAction(actionMap.get("startServer")); // NOI18N
        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setAction(actionMap.get("startServer")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAction(actionMap.get("stopServer")); // NOI18N
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenu1.add(jMenuItem2);

        jMenuItem4.setAction(actionMap.get("restartServer")); // NOI18N
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenu1.add(jMenuItem4);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jMenu1.add(jSeparator1);

        jMenuItem8.setAction(actionMap.get("manageAccounts")); // NOI18N
        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenu1.add(jMenuItem8);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jMenu1.add(jSeparator5);

        jMenuItem3.setAction(actionMap.get("editServerSettings")); // NOI18N
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenu1.add(jMenuItem3);

        menuBar.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenu2.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText(resourceMap.getString("jMenuItem6.text")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        jMenu2.add(jMenuItem6);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jMenu2.add(jSeparator2);

        jMenuItem7.setAction(actionMap.get("manageConnections")); // NOI18N
        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenu2.add(jMenuItem7);

        menuBar.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N

        jMenuItem9.setAction(actionMap.get("openMapEditor")); // NOI18N
        jMenuItem9.setText(resourceMap.getString("jMenuItem9.text")); // NOI18N
        jMenuItem9.setName("jMenuItem9"); // NOI18N
        jMenu3.add(jMenuItem9);

        menuBar.add(jMenu3);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 653, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void mainTabbedPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mainTabbedPanePropertyChange

        if(evt.getPropertyName().equals("close")) {
            ScrollingTab tab = (ScrollingTab) evt.getNewValue();
            tab.closeTab();
            this.mainTabbedPane.remove(tab);
            if(tab.getName().equals(this.homePanel.getName())) {
                this.isHomePanelOpen = false;
            }
        }
    }//GEN-LAST:event_mainTabbedPanePropertyChange

    private void mainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabbedPaneStateChanged
        
        if(this.mainTabbedPane.getSelectedComponent() != null) {
            ((ScrollingTab)this.mainTabbedPane.getSelectedComponent()).refreshTabContents();

            //
            // Switch active view here...
            //
        }
    }//GEN-LAST:event_mainTabbedPaneStateChanged

    
    @Action
    public void editServerSettings()
    {
        WartApp.getApplication().show(new ServerSettings());

        this.homePanel.updateServerNameField();
    }

    @Action
    public void manageConnections()
    {
        WartApp.getApplication().show(new ManageConnections());
    }

    @Action
    public void manageAccounts()
    {
      WartApp.getApplication().show(new ManageAccounts());  
    }

    @Action
    public void openMapEditor()
    {
        AgentWorldSim2D agentWorld = new AgentWorldSim2D();
        
        WartApp.getApplication().show(new EditSimWorld(agentWorld));        
    }

    /**
     * Connect to a server using the provided connection information.  A
     * <i>SavedConnection</i> object contains the server host, port, and
     * protocol type, as well as the <i>UserCredentials</i> object that
     * represents the user's login info to that server.
     *
     * @param   savedConnection     [in]    Supplies the connection information.
     */
    public boolean connectToServer(SavedConnection savedConnection)
    {
        //
        // Now, create the connector (the object that actually performs the
        // connection), and the handle to this client (which we need to give to
        // the connector when we connect).
        //
        Connector newConnector = WartAppConstants.GetConnectorInstance(
                                                    savedConnection.getType(),
                                                    savedConnection.getPort(),
                                                    savedConnection.getHost());

        ClientHandle newClientHandle = WartAppConstants.GetClientHandleInstance(
                                                            savedConnection.getType(),
                                                            WartApp.getApplication().getClient(),
                                                            savedConnection.getCredentials());


        //
        // Finally, connect - the result of this should be a valid handle to
        // the remote server.
        //
        ServerHandle newServerHandle = null;
        String serverName = null;
        String serverID = null;

        try {
            newServerHandle = newConnector.connect(newClientHandle);

            if(newServerHandle == null) {
                JOptionPane.showMessageDialog(this.getRootPane(), "Unable to connect: Server inactive.");
                return false;
            }

            serverName = newServerHandle.getName();
            serverID = newServerHandle.getUniqueID();
            WartApp.getApplication().getClient().addServerConnection(newServerHandle, (Callback) newClientHandle);
        }
        catch(PermissionException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return false;
        }
        catch(DuplicateHandleException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return false;
        }
        catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return false;
        }

        //
        // If we've made it here, we've successfully connected.
        //
        JOptionPane.showMessageDialog(this.getRootPane(), "Connected to " + serverName + " as " + savedConnection.getCredentials().getName());


        //
        // Now, create the remote server homepage GUI component.
        //
        RemoteServerPanel remoteServerPanel = new RemoteServerPanel(
                                                    serverID,
                                                    serverName,
                                                    savedConnection.getCredentials().getName(),
                                                    savedConnection.getHost());
       
        this.addTab(remoteServerPanel, true);

        //
        // Register it as a server event listener.
        //
        WartApp.getApplication().getClient().addServerEventListener(remoteServerPanel, serverID);

        //
        // And return success:
        //
        return true;
    }

    /**
     * Connect to the server using the hostname entered in the address bar.
     */
    @Action
    public void connectToServerByAddresBar()
    {
        String hostName = (String)this.addressBarComboBox.getSelectedItem();
        SavedConnection savedConnection = WartApp.getApplication().getConfiguration().getSavedConnection(hostName);

        //
        // If there isn't a connection saved with this host name, then open the
        // ManageConnections, and let them do their connecting through there.
        //
        // The host name is the unique key by which connections are stored - see
        // edu.wright.cs.carl.apps.wartapp.connection.SavedConnection and
        // edu.wright.cs.carl.net.connection.DefaultConnectionManager).
        //
        if(savedConnection == null) {
            //
            // Pop up a dialog box to get the login, port, and protocol.
            //
            WartApp.getApplication().show(new ManageConnections(hostName));
            this.refreshSavedConnectionComboBoxModel();
            return;
        }

        //
        // Otherwise, we've got everything we need to connect!
        //
        this.connectToServer(savedConnection);
    }

    /**
     * Start the server.
     */
    @Action
    public void startServer()
    {
        try {
            WartApp.getApplication().startServer();
        }
        catch(ServerException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
            return;
        }

        this.homePanel.setServerStatus(true);
    }

    /**
     * Stop the server.
     */
    @Action
    public void stopServer()
    {
        WartApp.getApplication().stopServer();
        this.homePanel.setServerStatus(false);
    }

    /**
     * Restart the server.
     */
    @Action
    public void restartServer()
    {
        this.stopServer();
        this.startServer();
    }



    private void createContext(WartAppConstants.ContextType type) throws PermissionException, ServerException, ContextException
    {
        //
        // Create the context components.
        //
        String contextName = WartAppConstants.GetDefaultContextName(type);
        String contextDescription = WartAppConstants.GetDefaultContextDescription(type);
        Acl serverAcl = WartApp.getApplication().getServer().getAcl(WartApp.getApplication().getLocalUser());
        RemoteHandleManager handles = new DefaultRemoteHandleManager(-1);


        //
        // Create the context instance.
        //
        Context newContext = WartAppConstants.GetContextInstance(
                                    type,
                                    contextName,
                                    contextDescription,
                                    serverAcl,
                                    handles,
                                    -1);

        //
        // Context-specific initialization.
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
                    simAgentManager.addAgent(it.next());                    
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
                    realAgentManager.addAgent(i.next());                    
                }
                realAgentContext.initialize(realAgentManager, realAgentWorld, WartAppConstants.DefaultControlTimeLimitInSeconds);
                
                break;
        }

        //
        // Add it to the Server.
        //
        WartApp.getApplication().getServer().addContext(WartApp.getApplication().getLocalUser(), newContext);
        
        //
        // Activate it.
        //
        WartApp.getApplication().getServer().activateContext(newContext.getUniqueID());

        //
        // Create GUI components.
        //
        ViewPanel newContextViewPanel = WartAppConstants.GetViewPanelInstance(type);

        newContextViewPanel.setName(contextName);

        View newContextView = WartAppConstants.GetViewInstance(
                                                type,
                                                WartApp.getApplication().getServer().getUniqueID(),
                                                newContextViewPanel);

        //
        // Join the context.
        //
        try {
            boolean joinSuccess = WartApp.getApplication().getClient().joinContext(
                                                            WartApp.getApplication().getServer().getUniqueID(),
                                                            newContext.getUniqueID(),
                                                            newContextView,
                                                            true);
            if(joinSuccess == false) {
                System.err.println("Joining own Context was full.");
                System.exit(1);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        //
        // Add the constructed GUI elements to the main tabbed pane.
        //
        ((WartView)WartApp.getApplication().getMainView()).addTab(newContextViewPanel, true);
    }

    @Action
    public void chatroomShortcut()
    {
        try {
            this.createContext(WartAppConstants.ContextType.CHAT_CONTEXT);
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
    }

    @Action
    public void simShortcut()
    {
        try {
            this.createContext(WartAppConstants.ContextType.SIM_AGENT_CONTEXT);
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
    }

    @Action
    public void realAgentShortcut()
    {
        try {
            this.createContext(WartAppConstants.ContextType.REAL_AGENT_CONTEXT);
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), e.getMessage());
        }
    }



    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox addressBarComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
