
package edu.wright.cs.carl.comm;


/**
 * This interface represents an object that can receive communications from a
 * <i>Communicator</i>.
 * 
 * @author  Duane Bolick
 */
public interface CommEventListener
{
    /**
     * Receive an event from a <i>Communicator</i>.
     * 
     * @param   evt     [in]    Supplies the event.
     */
    public void receiveCommEvent(Object event);
}
