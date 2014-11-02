
package edu.wright.cs.carl.net.message.payload;

/**
 * 
 *
 * @author Duane
 */
public class SimpleText implements java.io.Serializable
{
    public String text = new String();
    
    public SimpleText(){}
    
    public SimpleText(String text)
    {
        this.text = text;
    }
}
