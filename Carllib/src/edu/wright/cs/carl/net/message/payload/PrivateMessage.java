
package edu.wright.cs.carl.net.message.payload;


/**
 * This class represents a message payload of a private message between two
 * users.
 *
 * @author  Duane Bolick
 */
public class PrivateMessage implements java.io.Serializable
{
    public String text;
    public String recipientName;
    public String senderName;
    public String serverName;
    public String serverID;

    public PrivateMessage(String text, String recipientName, String senderName, String serverName, String serverID)
    {
        this.text = text;
        this.recipientName = recipientName;
        this.senderName = senderName;
        this.serverName = serverName;
        this.serverID = serverID;
    }

}
