
package edu.wright.cs.carl.apps.wartapp.swing;

import java.io.IOException;

import javax.swing.*;

import edu.wright.cs.carl.net.server.*;

import edu.wright.cs.carl.net.view.*;

import edu.wright.cs.carl.swing.*;

import edu.wright.cs.carl.apps.wartapp.*;


/**
 * 
 *
 * @author Duane
 */
public abstract class AbstractWartViewPanel extends ViewPanel
{
    @Override
    public void tabClosed()
    {
        try {
            WartApp.getApplication().getClient().leaveContext(this.contextID);
        }
        catch(ServerException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void pleaseClose()
    {
        //
        // DSB: pleaseClose implies that all the necessary cleanup has occurred
        // with the client-to-context connection, and handles only the actual
        // closing of the GUI components.  The commented code below was removed
        // because it was trying to sever the connection from the client side,
        // when it had already been severed context-side.
        //
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

    protected void lostConnection(IOException e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this.getRootPane(), "Lost Connection with the Context.");
        WartApp.getApplication().getClient().contextConnectionLost(this.contextID);
    }
}
