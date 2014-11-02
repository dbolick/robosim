
package edu.wright.cs.carl.net.event;


/**
 * This interface must be implemented for any objects listening to a server
 * locally.
 *
 * @author  Duane Bolick
 */
public interface ServerEventListener
{
    /**
     * Receive a change in the Server's state.
     *
     * @param   stateChange     [in]    Supplies the change.
     */
    public void serverEventOccurred(ServerEvent event);
}
