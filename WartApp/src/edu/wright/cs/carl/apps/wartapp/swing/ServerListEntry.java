/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.wright.cs.carl.apps.wartapp.swing;

import java.io.IOException;

import edu.wright.cs.carl.net.handle.ServerHandle;

/**
 * 
 *
 * @author Duane
 */
public class ServerListEntry
{
    private ServerHandle handle;
    private String serverName;
    
    public ServerListEntry(ServerHandle handle) throws IOException
    {
        this.handle = handle;
        this.serverName = this.handle.getUniqueID();
    }
    
    public ServerHandle getHandle()
    {
        return this.handle;
    }
    
    @Override
    public String toString()
    {
        String serverDisplayString = new String(this.serverName);
        
        try {
            int cur = this.handle.getCurrentNumClients();
            int max = this.handle.getMaxNumClients();
            serverDisplayString = serverDisplayString.concat(" [" + cur + "/" + max + "]");            
        }
        catch(IOException e) {
            e.printStackTrace();
            serverDisplayString = serverDisplayString.concat(" [Connection Lost]");
        }
        
        return serverDisplayString;
    }
}
