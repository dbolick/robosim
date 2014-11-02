
package edu.wright.cs.carl.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.charset.Charset;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import gnu.io.*;


/**
 * This implementation of <i>Communicator</i> uses a serial port to send and
 * receive messages.
 * 
 * @author  Duane Bolick
 */
public class SerialCommunicator implements Communicator, SerialPortEventListener
{   
    /**
     * Default serial port parameters.
     */
    public static int DefaultSerialPortBaudRate = 9600;
    public static int DefaultSerialPortDataBits = SerialPort.DATABITS_8;
    public static int DefaultSerialPortStopBits = SerialPort.STOPBITS_1;
    public static int DefaultSerialPortParity = SerialPort.PARITY_NONE;
    public static String DefaultSerialPortCharset = "US-ASCII";    
    public static int ByteBufferSize = 20;
    
    private SerialPort serialPort;
    
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private byte[] byteBuffer;
    private StringBuffer readBuffer; 
    private String charset;
    
    private List<CommEventListener> listeners;
    

    
    /**
     * Create a new SerialCommunicator using the provided
     * <i>CommPortIdentifier</i>.  This constructor opens the port, sets the
     * default parameters, gets the stream objects that wrap the port, and adds
     * this as a SerialPortEventListener.  If any of these operations fail, the
     * exception is repackaged as a <i>CommException</i> and thrown to the
     * caller.
     * 
     * @param   portID  [in]    Supplies the CommPortIdentifier.
     * 
     * @throws  CommException if creation fails.
     */
    public SerialCommunicator(CommPortIdentifier portID) throws CommException
    {
        //
        // This try-catch-finally statement attempts to open the serial port,
        // set the initial default parameters, add this object as a listener
        // for <i>SerialPortEvent</i>s, and gets the stream objects necessary
        // for reading and writing.  If any of these steps fail, this
        // constructor will fail, too, and throw a CommException.
        //
        try {
            // Throws PortInUseException
            serialPort = (SerialPort) portID.open("SimpleReadApp", 2000);

            // Throws UnsupportedCommOperationException
            serialPort.setSerialPortParams(SerialCommunicator.DefaultSerialPortBaudRate,
                                           SerialCommunicator.DefaultSerialPortDataBits,
                                           SerialCommunicator.DefaultSerialPortStopBits,
                                           SerialCommunicator.DefaultSerialPortParity);

            // Throws TooManyListenersException
            serialPort.addEventListener(this);

            // Both of these throw IOException
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        }
        catch(PortInUseException e) {
            serialPort = null;
            CommException ce = new CommException("SerialCommunicator Constructor: The port is in use.");
            ce.setStackTrace(e.getStackTrace());
            throw ce;
        }
        catch(UnsupportedCommOperationException e) {
            serialPort.close();
            serialPort = null;
            CommException ce = new CommException("SerialCommunicator Constructor: Port parameters are invalid.");
            ce.setStackTrace(e.getStackTrace());
            throw ce;
        }
        catch(TooManyListenersException e) {
            serialPort.close();
            serialPort = null;
            CommException ce = new CommException("SerialCommunicator Constructor: There is already a listener on this port.");
            ce.setStackTrace(e.getStackTrace());
            throw ce;
        }
        catch(IOException e) {
            serialPort.close();
            serialPort = null;
            CommException ce = new CommException("SerialCommunicator Constructor: An IO Error occurred.");
            ce.setStackTrace(e.getStackTrace());
            throw ce;
        }

        
        //
        // If we've made it here, then the SerialCommunicator has been
        // successfully created, and is ready for use.  So set the serial port
        // notifications - these define when the serial port calls our
        // <i>serialEvent</i> method.
        //
        serialPort.notifyOnDataAvailable(true);
        
        //
        // This notification is only needed if you're using a serial-to-USB
        // converter.
        //
        //serialPort.notifyOnOutputEmpty(true);
        
        //
        // Then create the output read buffers.
        //
        this.byteBuffer = new byte[SerialCommunicator.ByteBufferSize];
        this.readBuffer = new StringBuffer();
        this.charset = SerialCommunicator.DefaultSerialPortCharset;
        
        //
        // Last, but not least, create our List of listeners.
        //
        this.listeners = new ArrayList<CommEventListener>();
    }
    
    /**
     * Set the serial port parameters.
     * 
     * @param baudRate
     * @param dataBits
     * @param stopBits
     * @param parity
     * 
     * @throws gnu.io.UnsupportedCommOperationException
     */
    public void setParams(int baudRate, int dataBits, int stopBits, int parity) throws UnsupportedCommOperationException
    {
        serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);     
    }
    
    /**
     * Set the Charset used to decode the bytes read from the serial port.
     * 
     * @param   newCharset  [in]    Supplies the new charset.
     */
    public void setCharset(String newCharset)
    {
        this.charset = newCharset;
    }
    
    /**
     * Get the Charset used to decode the bytes read from the serial port.
     * 
     * @return  The charset.
     */
    public String getCharset()
    {
        return this.charset;
    }
    
    /**
     * Write to the communicator.
     * 
     * @param   s   [in]    Supplies the message to be sent.
     */
    public void write(String message) throws IOException
    {
        this.outputStream.write(message.getBytes());
        return;
    }
    
    /**
     * Add a listener to this Communicator.
     * 
     * @param   listener    [in]    Supplies a reference to the listener.
     */
    public void addCommEventListener(CommEventListener listener)
    {
        this.listeners.add(listener);
        return;
    }

    /**
     * Remove a listener from this Communicator.
     * 
     * @param   listener    [in]    Supplies the listener to be removed.
     */
    public void removeCommEventListener(CommEventListener listener)
    {
        this.listeners.remove(listener);
        return;
    }
    
    /**
     * Implementation of the serialEvent method required by the
     * <i>SerialEventListener</i> interface.
     * 
     * @param   evt     [in]    Supplies the event.
     */
    public void serialEvent(SerialPortEvent evt)
    {
        Iterator<CommEventListener> it = null;
        
        switch (evt.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
                break;
                
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                it = this.listeners.iterator();
                while(it.hasNext()) {
                    it.next().receiveCommEvent(null);                    
                }
                break;
            
            case SerialPortEvent.DATA_AVAILABLE:               
                //
                // First, clear the read buffer.
                //
                this.readBuffer.delete(0, this.readBuffer.length());
                
                //
                // Then read from the stream in chunks that are the size of
                // byteBuffer, appending those bytes to the end of the
                // readBuffer, until there's no more to read.
                //
                int numBytesRead = 0;
                try {
                    while (inputStream.available() > 0) {
                        numBytesRead = inputStream.read(this.byteBuffer);
                        this.readBuffer.append(new String(this.byteBuffer, 0, numBytesRead, this.charset));
                    } 
                }
                catch (IOException e) {
                    e.printStackTrace();
                }               
                it = this.listeners.iterator();
                while(it.hasNext()) {
                    it.next().receiveCommEvent(this.readBuffer.toString());                    
                }
                
                break;
                
        } // End switch on SerialPortEvent type    
    }
  
}
