
package edu.wright.cs.carl.apps.wartapp;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import edu.wright.cs.carl.apps.wartapp.connection.SavedConnection;

/**
 * Configuration file for the WartApp application.
 *
 * @author  Duane Bolick
 */
public class WartConfig implements Serializable
{
    //
    // Serializable object path names
    //
    private String serverSecurityManagerPath = "ssm.ser";
    private String serverAclPath = "serverAcl.ser";
    private String connectionListenerManagerPath = "listeners.ser";
    
    //
    // Package/Class/File paths
    //
    private String mapsDirectory = "maps";
    private String agentModulePackage = "edu.wright.cs.carl.apps.wart.agent.modules";
    private String controlInterfacesPackage = ".interfaces";
    private String controllersDirectory = "controllers";
    private String controllersPackage = "edu.wright.cs.carl.wart.agent.mod.khepera.controllers.";

    //
    // Server configuration
    //
    private String serverName = "My Server";
    private boolean autoStartServer = false;
    private int maxNumHandles = 10;
    private int serverPort = 1099;
    private WartAppConstants.ConnectionProtocolType serverProtocol = WartAppConstants.ConnectionProtocolType.RMI;
    private boolean autoDetectAgents = false;

    //
    // Client configuration
    //
    private Map<String, SavedConnection> savedConnections;
    
    
    public WartConfig()
    {
        this.savedConnections = new HashMap<String, SavedConnection>();
    }
    
    public SavedConnection getSavedConnection(String name)
    {
        return this.savedConnections.get(name);
    }
    
    public void putSavedConnection(SavedConnection connection)
    {
        this.savedConnections.put(connection.getHost(), connection);
    }
    
    public Map<String, SavedConnection> getAllSavedConnectionMap()
    {
        return this.savedConnections;
    }
    
    public List<String> getSavedConnectionNames()
    {
        return new ArrayList<String>(this.savedConnections.keySet());
    }
    
    public String getConnectionListenerManagerPath()
    {
        return this.connectionListenerManagerPath;
    }
    
    public void setConnectionListenerManagerPath(String connectionListenerManagerPath)
    {
        this.connectionListenerManagerPath = connectionListenerManagerPath;
    }    
    
    public String getServerName()
    {
        return this.serverName;
    }
    
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }
    
    public int getServerPort()
    {
        return this.serverPort;
    }
    
    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }

    public WartAppConstants.ConnectionProtocolType getServerProtocol()
    {
        return this.serverProtocol;
    }

    public void serServerProtocol(WartAppConstants.ConnectionProtocolType newServerProtocol)
    {
        this.serverProtocol = newServerProtocol;
    }

    public int getMaxNumHandles()
    {
        return this.maxNumHandles;
    }
    
    public void setMaxNumHandles(int maxNumHandles)
    {
        this.maxNumHandles = maxNumHandles;
    }
    
    public String getServerSecurityManagerPath()
    {
        return this.serverSecurityManagerPath;
    }
    
    public String getMapsDirectory()
    {
        return this.mapsDirectory;
    }
    
    public String getAgentModulePackage()
    {
        return this.agentModulePackage;
    }
    
    public String getControlInterfacesPackage()
    {
        return this.controlInterfacesPackage;
    }
    
    public String getControllersDirectory()
    {
        return this.controllersDirectory;
    }

    public String getControllersPackagePrefix()
    {
        return this.controllersPackage;
    }
    
    public boolean getAutoDetectAgents()
    {
        return this.autoDetectAgents;
    }
    
    public boolean getAutoStartServer()
    {
        return this.autoStartServer;
    }
    
    public String getServerAclPath()
    {
        return this.serverAclPath;
    }
    
    public void setServerAclPath(String serverAclPath)
    {
        this.serverAclPath = serverAclPath;
    }
    
    public void setServerSecurityManagerPath(String serverSecurityManagerPath)
    {
        this.serverSecurityManagerPath = serverSecurityManagerPath;
    }
    
    public void setMapsDirectory(String mapsDirectory)
    {
        this.mapsDirectory = mapsDirectory;
    }
    
    public void setAgentModulePackage(String agentModulePackage)
    {
        this.agentModulePackage = agentModulePackage;
    }
    
    public void setControlInterfacesPackage(String controlInterfacesPackage)
    {
        this.controlInterfacesPackage = controlInterfacesPackage;
    }    
    
    public void setControllersPackage(String controllersPackage)
    {
        this.controllersDirectory = controllersPackage;
    }    
    
    public void setAutoDetectAgents(boolean autoDetectAgents)
    {
        this.autoDetectAgents = autoDetectAgents;
    }
    
    public void setAutoStartServer(boolean autoStartServer)
    {
        this.autoStartServer = autoStartServer;
    }
}
