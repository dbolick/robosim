
package edu.wright.cs.carl.net.rmi;

import java.rmi.Remote;

import edu.wright.cs.carl.net.handle.Callback;
import edu.wright.cs.carl.net.handle.ServerHandle;


/**
 * <p>
 * This interface defines the RMI Remote Interface for a ServerHandle.
 * </p>
 * <p>
 * Here's some background for anyone not familiar with the way Java RMI works.
 * In order to make some methods of a class available for remote invocation,
 * you need to "mark" them specially.  Here are the requirements for making
 * a class and some of its methods remotely available:
 * <ol>
 * <li>First, you must create what's called a "Remote Interface" that declares
 * all of those methods.  A "Remote Interface" is simply an interface that
 * extends <i>java.rmi.Remote</i>.</li>
 * <li>Next, all the methods declared in your remote interface must throw
 * java.rmi.RemoteException, or a superclass of that RemoteException.</li>
 * <li>The class that implements your remote interface should extend
 * <i>java.rmi.server.UnicastRemoteObject</i>.  There are other ways to make a
 * class remotely available, but this is the most straightforward of them.<li>
 * <li>The constructor(s) of your implementing class must throw
 * <i>java.rmi.RemoteException</i>.</li>
 * </ol>
 * </p>
 * The class that implements your remote interface can have other methods - they
 * just won't be available to remote clients.  Also, your remote interface can
 * extend another interface, but <b>every</b> method of the interface it extends
 * must throw <i>java.rmi.RemoteException</i> or a superclass of
 * RemoteException, or else the JVM will throw an IllegalRemoteMethodException
 * when it tries to bind the object (the same thing that would happen if you
 * declared a method in your remote interface that didn't throw
 * RemoteException.)
 * </p>
 * 
 * @author  Duane Bolick
 * @see     ServerHandle
 * @see     java.rmi.Remote
 */
public interface RMIServerHandleRI extends ServerHandle, Callback, Remote
{

}
