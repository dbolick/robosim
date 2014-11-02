
package edu.wright.cs.carl.net.message.payload;


/**
 * This Message payload indicates a change in a user's status.
 *
 * @author  Duane Bolick
 */
public final class UserStatusChange implements java.io.Serializable
{
    public static enum Type
    {
        LOGON,
        INITIALIZE_ME,
        INITIALIZED,
        LOGOFF
    
    }
    
    public Type statusChangeType;
    //public String username;

    public UserStatusChange()
    {
        //this.username = new String();
    }
    
    //public UserStatusChange(String username)
    //{
        //this.username = username;
    //}
    
    public UserStatusChange(Type statusChangeType)
    {
        this.statusChangeType = statusChangeType;
        //this.username = username;
    }

}
