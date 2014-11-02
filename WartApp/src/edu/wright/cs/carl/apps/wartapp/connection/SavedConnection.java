
package edu.wright.cs.carl.apps.wartapp.connection;

import java.io.Serializable;

import edu.wright.cs.carl.security.DefaultUserCredentials;
import edu.wright.cs.carl.security.UserCredentials;

import edu.wright.cs.carl.apps.wartapp.WartAppConstants;

/**
 * 
 *
 * @author  Duane Bolick
 */
public class SavedConnection implements Serializable
{
    private String host;
    private String port;
    private WartAppConstants.ConnectionProtocolType type;
    
    private UserCredentials credentials;

    public SavedConnection(String host)
    {
        this.host = host;
        this.port = new String();
        this.type = WartAppConstants.DefaultProtocolType;

        this.credentials = new DefaultUserCredentials();
    }

    public SavedConnection(String host, String port, WartAppConstants.ConnectionProtocolType type, UserCredentials credentials)
    {
        this.host = host;
        this.port = port;
        this.type = type;
        this.credentials = credentials;
    }

    public String getConnectionID()
    {
        return this.host + " as " + this.credentials.getName();
    }

    public String getHost()
    {
        return this.host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public String getPort()
    {
        return this.port;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public WartAppConstants.ConnectionProtocolType getType()
    {
        return this.type;
    }
    
    public void setType(WartAppConstants.ConnectionProtocolType type)
    {
        this.type = type;
    }
    
    public UserCredentials getCredentials()
    {
        return this.credentials;
    }
    
    public void setCredentials(UserCredentials credentials)
    {
        this.credentials = credentials;
    }
}
