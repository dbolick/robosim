
package edu.wright.cs.carl.comm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gnu.io.*;


/**
 * This class contains static fields and utility methods related to local
 * communications.
 *
 * @author  Duane Bolick
 */
public class CommUtils
{
    
    /**
     * This static method polls all specified communications ports and returns
     * a list of Communicator objects representing these communications ports.
     * 
     * @return  A list of Communicators representing open ports.
     */
    public static List<Communicator> GetAvailableCommunicators()
    {
        List<Communicator> availableCommunicators = new ArrayList<Communicator>();
       
        availableCommunicators.addAll(CommUtils.GetSerialCommunicators());
        
        return availableCommunicators;
    }
    
    /**
     * This static method polls all available Serial ports, and returns a list
     * of SerialCommunicator objects representing these communication ports.
     * 
     * @return  A list of Communicators representing open Serial ports.
     */
    public static List<Communicator> GetSerialCommunicators()
    {
        List<Communicator> serialCommunicators = new ArrayList<Communicator>();
        
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier currentPortID = null;
        Communicator newCommunicator = null;
        while(ports.hasMoreElements()) {
            currentPortID = (CommPortIdentifier)ports.nextElement();
            if(currentPortID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                try {
                    newCommunicator = new SerialCommunicator(currentPortID);
                }
                catch(CommException e) {
                    e.printStackTrace();
                    newCommunicator = null;
                }
                finally {
                    if(newCommunicator != null) {
                        serialCommunicators.add(newCommunicator);
                    }
                }
            }
        }
        
        return serialCommunicators;
    }      
}
