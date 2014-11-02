
package edu.wright.cs.carl.comm;

import java.net.URI;

/**
 * 
 *
 * @author Duane
 */
public class TCPIPCommunicator
{
    private URI target;
    private int port;
    
    public TCPIPCommunicator(URI target, int port)
    {
        this.target = target;
        this.port = port;
    }

    public void initialize()
    {

    }

    public boolean open()
    {
        return true;
    }

    public void close()
    {

    }

    public String write(String s)
    {
        return "Not Done";
    }
}
