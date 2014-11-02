
package edu.wright.cs.carl.apps.wartapp;

import java.io.IOException;

import java.security.acl.Acl;

import java.util.Vector;

import edu.wright.cs.carl.net.client.Client;

import edu.wright.cs.carl.net.connection.Connector;
import edu.wright.cs.carl.net.connection.ConnectionListener;

import edu.wright.cs.carl.net.context.Context;

import edu.wright.cs.carl.net.handle.ClientHandle;
import edu.wright.cs.carl.net.handle.RemoteHandleManager;

import edu.wright.cs.carl.net.rmi.*;

import edu.wright.cs.carl.net.server.Server;

import edu.wright.cs.carl.net.view.View;
import edu.wright.cs.carl.net.view.ViewPanel;

import edu.wright.cs.carl.security.UserCredentials;


import edu.wright.cs.carl.apps.wartapp.context.chat.*;
import edu.wright.cs.carl.apps.wartapp.context.agent.AgentContext;
import edu.wright.cs.carl.apps.wartapp.context.agent.simagent.*;
import edu.wright.cs.carl.apps.wartapp.context.agent.realagent.*;


/**
 * This class contains constants and static factory methods for obtaining
 * instances of classes that have various "types."
 *
 * @author  Duane Bolick
 */
public class WartAppConstants
{
    public static int MinUpdatesPerSecond = 5;
    public static int MaxUpdatesPerSecond = 100;
    public static int DefaultViewUpdatesPerSecond = 30;
    public static int DefaultStatusUpdateIntervalInMillis = 5000;
    public static int DefaultControlTimeLimitInSeconds = 120;


    public static ConnectionProtocolType DefaultProtocolType = ConnectionProtocolType.RMI;

    public static enum ConnectionProtocolType
    {
        TCP,
        RMI,
        SERIAL
    }

    public static Vector<String> GetSupportedProtocolTypes()
    {
        Vector<String> types = new Vector<String>();
        for(ConnectionProtocolType type : ConnectionProtocolType.values()) {
            types.add(GetProtocolTypeString(type));
        }
        return types;
    }

    public static ConnectionProtocolType GetProtocolTypeFromString(String typeString)
    {
        for(ConnectionProtocolType type : ConnectionProtocolType.values()) {
            if(GetProtocolTypeString(type).equals(typeString)) {
                return type;
            }
        }
        return null;
    }

    public static String GetProtocolTypeString(ConnectionProtocolType type)
    {
        switch(type)
        {
            case TCP:
                return "TCP";

            case RMI:
                return "RMI";

            case SERIAL:
                return "SERIAL";

            default:
                return "Unknown Type";
        }
    }

    public static ConnectionListener GetListenerInstance(ConnectionProtocolType protocol, Server server, String port) throws IOException
    {
        switch(protocol) {
            case RMI:
                return new RMIConnectionListener(server, Integer.parseInt(port));
                
            case TCP:
                return null;

            case SERIAL:
                return null;

            default:
                return null;
        }
    }

    public static Connector GetConnectorInstance(   ConnectionProtocolType protocol,
                                                    String port,
                                                    String host)
    {
        switch(protocol) {
            case RMI:
               return new RMIConnector(host, Integer.parseInt(port));

            case TCP:
                return null;

            case SERIAL:
                return null;

            default:
                return null;
        }
    }

    public static ClientHandle GetClientHandleInstance(ConnectionProtocolType protocol, Client client, UserCredentials userCredentials)
    {
        ClientHandle newClientHandle = null;
        switch(protocol) {
            case RMI:
                try {
                    newClientHandle = new RMIClientHandle(client, userCredentials);
                    return newClientHandle;
                }
                catch(IOException e) {
                    //
                    // This should never happen.
                    //
                    throw new AssertionError(e);
                }

            case TCP:
                return null;

            case SERIAL:
                return null;

            default:
                return null;
        }
    }

    public static enum ContextType
    {
        CHAT_CONTEXT,
        REAL_AGENT_CONTEXT,
        SIM_AGENT_CONTEXT
    }
    
    public static ContextType GetContextType(String typeString)
    {
        for(ContextType type : ContextType.values()) {
            if(GetContextTypeString(type).equals(typeString)) {
                return type;
            }
        }
        return null;
    }

    public static Vector<String> GetSupportedContextTypes()
    {
        Vector<String> types = new Vector<String>();
        for(ContextType type : ContextType.values()) {
            types.add(GetContextTypeString(type));
        }
        return types;
    }
    
    public static String GetContextTypeString(ContextType type)
    {
        switch(type)
        {
            case CHAT_CONTEXT:
                return "Chat";
                
            case REAL_AGENT_CONTEXT:
                return "Real Agent";
                
            case SIM_AGENT_CONTEXT:
                return "Simulated Agent";
                
            default:
                return "Unknown Type";
        }
    }    
    
    public static String GetDefaultContextName(ContextType type)
    {
        switch(type)
        {
            case CHAT_CONTEXT:
                return "My Chatroom";
                
            case REAL_AGENT_CONTEXT:
                return "My Real Agent Arena";
                
            case SIM_AGENT_CONTEXT:
                return "My Simulated Agent Arena";
                
            default:
                return "Unknown Type";
        }
    }
    
    public static String GetDefaultContextDescription(ContextType type)
    {
        switch(type)
        {
            case CHAT_CONTEXT:
                return "A chatroom for chatting.";
                
            case REAL_AGENT_CONTEXT:
                return "Control real robotic agents and watch on a webcam.";
                
            case SIM_AGENT_CONTEXT:
                return "Control simulated robotic agents in a simulated world.";
                
            default:
                return "Unknown Type";
        }
    }
    
    public static Context GetContextInstance(   ContextType type,
                                                String name,
                                                String description,
                                                Acl acl,
                                                RemoteHandleManager handleManager,
                                                int maxNumConnections)
    {
        switch(type)
        {
            case CHAT_CONTEXT:
                return new ChatContext(                                    
                                    name,
                                    description,
                                    acl,
                                    handleManager,
                                    maxNumConnections);
                
            case REAL_AGENT_CONTEXT:
                return new AgentContext(                                    
                                    name,
                                    description,
                                    acl,
                                    handleManager,
                                    maxNumConnections,
                                    WartAppConstants.ContextType.REAL_AGENT_CONTEXT);
                
            case SIM_AGENT_CONTEXT:
                return new AgentContext(                                    
                                    name,
                                    description,
                                    acl,
                                    handleManager,
                                    maxNumConnections,
                                    WartAppConstants.ContextType.SIM_AGENT_CONTEXT);
                
            default:
                return null;
        }        
    }
    
    public static ViewPanel GetViewPanelInstance(ContextType type)
    {
        switch(type)
        {
            case CHAT_CONTEXT:
                return new ChatContextViewPanel();
                
            case REAL_AGENT_CONTEXT:
                return new RealAgentContextViewPanel();
                
            case SIM_AGENT_CONTEXT:
                return new SimAgentContextViewPanel();
                
            default:
                return null;
        }         
    }
    
    public static View GetViewInstance(ContextType type, String serverID, ViewPanel viewPanel)
    {
        switch(type)
        {
            case CHAT_CONTEXT:
                return new ChatContextView(serverID, viewPanel);
                
            case REAL_AGENT_CONTEXT:
                return new RealAgentContextView(serverID, viewPanel);
                
            case SIM_AGENT_CONTEXT:
                return new SimAgentContextView(serverID, viewPanel);
                
            default:
                return null;
        }         
    }
    
}
