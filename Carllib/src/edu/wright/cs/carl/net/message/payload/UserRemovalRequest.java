
package edu.wright.cs.carl.net.message.payload;


/**
 *
 * @author  Duane Bolick
 */
public class UserRemovalRequest implements java.io.Serializable
{
    public String username;

    public UserRemovalRequest(String username)
    {
        this.username = username;
    }
}
