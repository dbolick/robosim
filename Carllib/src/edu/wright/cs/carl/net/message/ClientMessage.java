
package edu.wright.cs.carl.net.message;

import edu.wright.cs.carl.security.UserCredentials;

/**
 * This message type represents messages from a Client.
 *
 * @author  Duane Bolick
 */
public class ClientMessage implements Message
{
    public UserCredentials clientCredentials;
    public String recipientName = null;
    public Object payload = null;
    
    public ClientMessage(){}
    
    public ClientMessage(UserCredentials clientCredentials)
    {
        this.clientCredentials = clientCredentials;
    }
    
    public ClientMessage(UserCredentials clientCredentials, Object payload)
    {
        this.clientCredentials = clientCredentials;
        this.payload = payload;
    }
}
