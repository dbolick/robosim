
package edu.wright.cs.carl.wart.real;

import java.io.IOException;

import edu.wright.cs.carl.comm.Communicator;


/**
 * This is the interface that all real Agents must implement.  The
 * <i>isAgentType</i> method should send some sort of version-checking method
 * that uniquely identifies the agent type, and check it against a known
 * response.
 * 
 * @author  Duane Bolick
 */
public interface RealAgent
{
    /**
     * Given an open Communicator, determine if that Communicator is connected
     * to an agent of this type.
     * 
     * @param   comm    [in]    Supplies the Communicator.
     * 
     * @return  True if the Communicator is connected to an Agent of this type,
     *          false otherwise.
     */
    public boolean isAgentType(Communicator comm) throws IOException;
    
    /**
     * Set the Communicator.
     * 
     * @param   comm    [in]    Supplies the Communicator that is connected to
     *                          an agent of this type.
     */
    public void setCommunicator(Communicator comm);    
    
    /**
     * Start the agent.  If a communicator has not been set, this method does
     * nothing.
     */
    public void start();
    
    /**
     * Stop the agent.  If the agent has not been started, this method does
     * nothing.
     */
    public void stop();
}
