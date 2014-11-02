
package edu.wright.cs.carl.net.handle;


/**
 * <p>
 * This interface is a tagging interface that identifies implementing classes as
 * representing the "receiving end" of a ConnectionHandle.  Since different
 * transport-layer protocols must necessarily implement their callbacks in very
 * different ways, this interface does not define any methods.
 * </p>
 * 
 * <p>
 * Even if your implementation of a ConnectionHandle does not require the
 * callback to do anything (i.e., when using RMI the handle objects <i>are</i>
 * the callback objects), you must still imlpement this interface so that the
 * callback objects may be referenced and stored by the Callback
 * type.
 * </p>
 * 
 * <p>
 * The only common element among all Callback implementations is that they will
 * have a reference to the target object they represent so that when a method is
 * invoked on the corresponding ConnectionHandle, the Callback is able to
 * produce the results of that remote invocation.
 * </p>
 * 
 * @author  Duane Bolick
 * 
 * @see     RemoteHandle
 * @see     CallbackManager
 */
public interface Callback
{

}
