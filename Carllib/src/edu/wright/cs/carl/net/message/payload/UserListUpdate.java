
package edu.wright.cs.carl.net.message.payload;


/**
 * 
 *
 * @author Duane
 */
public class UserListUpdate implements ContextViewUpdate
{
    public String message;
    public String username;
    public UserStatusChange.Type type;
    
    public UserListUpdate(String message, String username, UserStatusChange.Type type)
    {
        this.message = message;
        this.username = username;
        this.type = type;
    }
}
