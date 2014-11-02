
package edu.wright.cs.carl.comm;

import java.io.IOException;


/**
 * This interface represents a means of communicating over some means of
 * transmitting and receiving messages.
 * 
 * @author  Duane Bolick
 */
public interface Communicator
{
    /**
     * Write to the communicator.
     * 
     * @param   s   [in]    Supplies the message to be sent.
     */
    public void write(String message) throws IOException;
    
    /**
     * Add a listener to this Communicator.
     * 
     * @param   listener    [in]    Supplies a reference to the listener.
     */
    public void addCommEventListener(CommEventListener listener);
    
    /**
     * Remove a listener from this Communicator.
     * 
     * @param   listener    [in]    Supplies the listener to be removed.
     */
    public void removeCommEventListener(CommEventListener listener);    
}
