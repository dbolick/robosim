
package edu.wright.cs.carl.net.event;


/**
 * This class represents a Server event.
 *
 * @author  Duane Bolick
 */
public class ServerEvent implements java.io.Serializable
{
    public static enum EventType
    {
        USER_JOINED,
        USER_LEFT,
        CONTEXT_ADDED,
        CONTEXT_REMOVED,
        CONTEXT_EVENT,
        SERVER_CONNECTION_LOST
    }

    public EventType type;
    public String eventString;

    public ServerEvent(EventType type, String eventString)
    {
        this.type = type;
        this.eventString = eventString;
    }
}
