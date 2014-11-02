
package edu.wright.cs.carl.net.context;


/**
 * This class is a plain-old-data class for all the information a client might
 * need to know about a Context.
 *
 * @author  Duane Bolick
 */
public class ContextInformation implements java.io.Serializable
{
    public String id;
    public String name;
    public String description;
    public int maxNumClients;
    public int currentNumClients;
    public String type;
    public boolean isActive;

    public ContextInformation()
    {
    }

}
