
package edu.wright.cs.carl.net.message;

import java.io.Serializable;


/**
 * A Message represents a communication from a sender to a target.  For
 * security purposes, when you provide an implementation of a Message for you
 * Context, you should declare it as final so that any permission-checking
 * you include in your Context cannot be circumvented by subclassing.
 *
 * @author  Duane Bolick
 * @see     edu.wright.cs.carl.net.MessageRecipient
 */
public interface Message extends Serializable
{
}
