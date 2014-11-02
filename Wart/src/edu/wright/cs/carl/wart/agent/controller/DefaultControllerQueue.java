
package edu.wright.cs.carl.wart.agent.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import edu.wright.cs.carl.wart.agent.Agent;


/**
 * Default implementation of ControllerQueue.
 *
 * @author  Duane Bolick
 * 
 * @see     ControllerQueue
 */
public class DefaultControllerQueue implements ControllerQueue, Runnable
{
    public static int ControllerQueueWaitInMillis = 1000;
    
    private Agent agent;
    private List<ControllerQueueEntry> queue;
    private final Object queueLock = new Object();

    private Thread queueThread;
    private boolean queueStopRequested = true;

    private Thread controllerThread;
    private AgentController currentController;
    private boolean controllerThreadReady = false;
    
    private Timer timer;
    
    public DefaultControllerQueue(Agent agent)
    {
        this.agent = agent;
        // this.agent.rest();
        this.queue = new ArrayList<ControllerQueueEntry>();
    }
    

    public void run()
    {
        while(this.queueStopRequested == false) {
            if(this.controllerThreadReady == true && this.queue.isEmpty() == false) {
                
                this.controllerThreadReady = false;
                
                try {
                    Thread.sleep(DefaultControllerQueue.ControllerQueueWaitInMillis);
                }
                catch(InterruptedException e) {

                }                
                
                synchronized(this.queueLock) {
                    if(this.queue.isEmpty() == false) {
                        ControllerQueueEntry next = this.queue.remove(0);
                        this.currentController = next.getController();
                        this.controllerThread = new Thread(this.currentController);
                        this.timer.schedule(new StopControllerTimerTask(this), next.getTimeLimit() * 1000);
                        this.controllerThread.start();
                    }
                }
            }
            
            try {
                Thread.sleep(DefaultControllerQueue.ControllerQueueWaitInMillis);
            }
            catch(InterruptedException e) {
                
            }
            
            /*
             * TODO: Figure out what to do if a controller dies on its own.
             * Have to do something clever with the timer, since the existing
             * task will still execute on time.  Maybe have a flag in this
             * class that tells the timer task not to end the current controller
             * if the flag is set...  Dunno, not high priority unless it becomes
             * an issue.
            if(this.controllerThread.isAlive() == false) {
                this.endCurrentController();                
            }
             */ 
        }
        
        this.endCurrentController();
    }
    
    public synchronized void startQueue()
    {
        //
        // Create a new Timer.
        //
        this.timer = new Timer();
        
        //
        // Set state as ready to go.
        //
        this.queueStopRequested = false;
        this.controllerThreadReady = true;
        
        //
        // Create a new queue thread and start it.
        //
        this.queueThread = new Thread(this);
        this.queueThread.start();
    }
    
    public synchronized void stopQueue()
    {
        //
        // Stop the timer and get rid of the reference.
        //
        this.timer.cancel();
        this.timer = null;
        
        //
        // Stop the currently-executing controller.
        //
        this.endCurrentController();
        
        //
        // Stop the queue thread and get rid of the reference.
        //
        synchronized(this.queueLock) {
            this.queue.clear();
        }

        this.queueStopRequested = true;
        try {
            this.queueThread.join();
        }
        catch(InterruptedException e) {
            
        }
        this.queueThread = null;
    }
    
    public void endCurrentController()
    {        
        if(currentController == null || controllerThread == null) {           
            return;
        }
        //
        // Stop the thread.
        //
        this.currentController.stop();
        try {
            this.controllerThread.join();
        }
        catch(InterruptedException e) {
            
        }    
        
        //
        // Null references.
        //
        this.currentController = null;
        this.controllerThread = null;
       
        //
        // Put the agent in a resting state.
        //
        this.agent.rest();    
        
        //
        // Then set the controller thread to be ready.
        //
        this.controllerThreadReady = true;
    }
    
    public void addController(AgentController controller, String username, int timeLimitInSeconds)
    {
        try {
            controller.setAgent(this.agent);
        }
        catch(AgentControllerException e) {

        }

        synchronized(this.queueLock) {
            this.queue.add(new ControllerQueueEntry(controller, username, timeLimitInSeconds));
        }
    }
    
    public void removeController(int position)
    {
        synchronized(this.queueLock) {
            this.queue.remove(position);
        }
    }
    
    public void removeAllFromUser(String username)
    {
        synchronized(this.queueLock) {
            List<ControllerQueueEntry> newList = new ArrayList<ControllerQueueEntry>();
            Iterator<ControllerQueueEntry> it = this.queue.iterator();
            ControllerQueueEntry currentEntry = null;
            while(it.hasNext()) {
                currentEntry = it.next();
                if(currentEntry.getUsername().equals(username) == false) {
                    newList.add(currentEntry);
                }
            }
            this.queue = newList;
        }
    }
    
    public void moveUp(int position)
    {
        this.swapEntries(position, position-1);
    }
    
    public void moveDown(int position)
    {
        this.swapEntries(position, position+1);
    }
    
    private void swapEntries(int index1, int index2)
    {
        synchronized(this.queueLock) {
            ControllerQueueEntry temp1 = this.queue.get(index1);
            ControllerQueueEntry temp2 = this.queue.get(index2);
            this.queue.set(index1, temp2);
            this.queue.set(index2, temp1);
        }
    }
    
    public int getTimeLimit(int position)
    {
        return this.queue.get(position).getTimeLimit();
    }
    
    public void setTimeLimit(int position, int timeLimitInSeconds)
    {
        synchronized(this.queueLock) {
            this.queue.get(position).setTimeLimit(position);
        }
    }
    
    public List<String> getControllerList()
    {
        List<String> controllerList = null;

        synchronized(this.queueLock) {
            controllerList = new ArrayList<String>();
            Iterator<ControllerQueueEntry> it = this.queue.iterator();
            ControllerQueueEntry currentEntry = null;

            while(it.hasNext()) {
                currentEntry = it.next();
                controllerList.add(currentEntry.getController().getControllerName() + " [" + currentEntry.getUsername() + "]");
            }
        }

        return controllerList;
    }
}
