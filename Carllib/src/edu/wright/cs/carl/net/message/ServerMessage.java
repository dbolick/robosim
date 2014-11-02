
package edu.wright.cs.carl.net.message;


/**
 * This message type represents messages from a Server.
 *
 * @author  Duane Bolick
 */
public class ServerMessage implements Message
{
    public String serverName;
    public String serverID;
    public Object payload;
    
    public ServerMessage(){}
    
    public ServerMessage(String serverName, String serverID)
    {
        this.serverName = serverName;
        this.serverID = serverID;
    }
    
    public ServerMessage(String serverName, String serverID, Object payload)
    {
        this.serverName = serverName;
        this.serverID = serverID;
        this.payload = payload;
    }
}
