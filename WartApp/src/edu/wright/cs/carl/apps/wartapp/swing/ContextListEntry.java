
package edu.wright.cs.carl.apps.wartapp.swing;

import edu.wright.cs.carl.net.context.ContextInformation;

import edu.wright.cs.carl.apps.wartapp.WartAppConstants;


/**
 * This class represents a Context as an entry in a Swing ListModel.
 *
 * @author  Duane Bolick
 */
public class ContextListEntry
{
    private String contextID;
    private String contextName;
    private String contextType;
    private int maxUsers;
    private int curUsers;
    private boolean isActive;

    public ContextListEntry(ContextInformation c)
    {
       this.contextID = c.id;
       this.contextName = c.name;
       this.contextType = c.type;
       this.curUsers = c.currentNumClients;
       this.maxUsers = c.maxNumClients;
       this.isActive = c.isActive;
    }

    public ContextListEntry(String contextID,
                            String contextName,
                            String contextType,
                            int maxUsers,
                            int curUsers,
                            boolean isActive)
    {
        this.contextID = contextID;
        this.contextName = contextName;
        this.contextType = contextType;
        this.maxUsers = maxUsers;
        this.curUsers = curUsers;
        this.isActive = isActive;
    }

    public String getContextID()
    {
        return this.contextID;
    }
    
    public String getContextName()
    {
        return this.contextName;
    }

    public String getContextTypeString()
    {
        return this.contextType;
    }

    public WartAppConstants.ContextType getContextType()
    {
        return WartAppConstants.GetContextType(this.contextType);
    }

    @Override
    public String toString()
    {
        String activityString = null;
        if(this.isActive == true) {
            activityString = "Active";
        }
        else {
            activityString = "Not Active";
        }
        
        return new String(  this.contextName +
                            " [" + this.contextType + "]" +
                            " [" + this.curUsers + "/" + this.maxUsers + "]" +
                            " [" + activityString + "]");
    }
}
