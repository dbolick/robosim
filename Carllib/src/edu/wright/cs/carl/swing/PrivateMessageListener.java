
package edu.wright.cs.carl.swing;

import edu.wright.cs.carl.net.message.payload.PrivateMessage;


/**
 * This interface must be implemented for any object that wants to handle
 * private messages.
 *
 * @author  Duane Bolick
 */
public interface PrivateMessageListener
{
    /**
     * Receive and handle a PrivateMessage.
     *
     * @param   pm  [in]    Supplies the PrivateMessage.
     */
    public void receivePrivateMessage(PrivateMessage pm);
}
