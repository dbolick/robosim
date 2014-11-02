
package edu.wright.cs.carl.comm;

import java.io.IOException;

/**
 * This class is used to execute a synchronous write and read to a
 * communicator.  That is, the syncWrite method waits until it has received a
 * response before returning.
 *
 * @author  Duane Bolick
 */
public class SynchronousWrite implements CommEventListener, Runnable
{
    private Communicator c;
    private Thread readWaitThread;
    private static long readWaitThreadWaitInMillis = 1000;

    private String writeMessage = null;
    private String readOutput = null;
    private boolean outputReady = false;
    
    public SynchronousWrite(Communicator c)
    {
        this.c = c;
    }
    
    /**
     * Write to a communicator and wait until a response is received, then
     * return that response.
     * 
     * @param   message     [in]    Supplies the message to be sent.
     *      
     * @return  The response from the Communicator.
     */
    public String syncWrite(String message)
    {
        this.writeMessage = message;
        this.c.addCommEventListener(this);

        this.readWaitThread = new Thread(this);
        this.readWaitThread.start();
        try {
            this.readWaitThread.join();
        }
        catch(InterruptedException e) {
            
        }
        
        return this.readOutput;
    }
    
    /**
     * Implements Runnable.run().
     */
    public void run()
    {
        this.readOutput = null;
        this.outputReady = false;
        
        while(this.outputReady == false) {
            try {
                this.c.write(writeMessage);
                Thread.sleep(readWaitThreadWaitInMillis);
            }
            catch(InterruptedException e) {
                
            }
            catch(IOException e) {

            }
        }                
    }
    
    /**
     * Receive an event from a <i>Communicator</i>.
     * 
     * @param   evt     [in]    Supplies the event.
     */
    public void receiveCommEvent(Object event)
    {
        if(outputReady == false && event != null && event instanceof String) {
            this.readOutput = (String)event;            
            this.outputReady = true;
        }
    }
}
