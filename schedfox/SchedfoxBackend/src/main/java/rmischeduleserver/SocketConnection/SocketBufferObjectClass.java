/*
 * SocketBufferObjectClass.java
 *
 * Created on December 5, 2005, 9:32 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.SocketConnection;
import java.nio.*;
import java.io.*;
import java.nio.channels.*;
import rmischeduleserver.data_connection_types.*;

import javax.swing.*;
/**
 *
 * @author Ira Juneau
 * This is a class I created to help associate a ByteBuffer, ObjectInputStream,
 * ObjectOutputStream, ByteBufferOutputStream and ByteBufferInputStream to make
 * our channel programming just a tad easier.
 *
 */
public class SocketBufferObjectClass {
    
    public ByteBuffer myBuffer;
    public ObjectOutputStream myObjectOutputStream;
    public ObjectInputStream myObjectInputStream;
    public ByteArrayOutputStream myByteArrayOutputStream;
    public ByteArrayInputStream myByteArrayInputStream;
    public SocketChannel myChannel;
    public String userId;
    private boolean isClosedConnection;
    
    public static final int BUFFER_CAPACITY = 32768;
    public static final int MARK_AS_VALID_SCHEDFOX_PACKET = -1;
    public static final int MARK_AS_A_PING_PACKET = 1;
    
    /** Creates a new instance of SocketBufferObjectClass */
    public SocketBufferObjectClass() {
        myBuffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
        isClosedConnection = false;
    }
    
    /**
     * Sets the active Connection for this object, may have to be redone on reconnect
     * and definitely upon initial use...
     */
    public void updateActiveChannel(SocketChannel chan) {
        myChannel = chan;
        isClosedConnection = false;
    }
    
    /**
     * Used to link this data structure to the SocketClient, so can disconnect appropriate
     * user and so on...
     */
    public void updateWithUserInformation(ClientConnection myClientAssociatedWith) {
        userId = myClientAssociatedWith.getUserDatabaseId();
    }
    
    /**
     * Converts given object into a byte[] which could be used to place in this
     * ByteBuffer or another one...
     */
    public byte[] convertObjectToByteArray(Object myObject) throws IOException {
        byte[] myReturnVal = null;
        
        try {
            myByteArrayOutputStream = new ByteArrayOutputStream();
            myObjectOutputStream = new ObjectOutputStream(myByteArrayOutputStream);
            myObjectOutputStream.writeObject(myObject);
            myReturnVal = myByteArrayOutputStream.toByteArray();
        }
        catch (IOException e) { throw(e); }
        
        return myReturnVal;
    }
    
    /**
     * Tells whether or not this connection has been detected as Closed via the constructObjectThroughChannel
     * method
     */
    public boolean isClosedConnection() {
        return isClosedConnection;
    }
    
    /**
     * Flags connection as closed to prevent infinite read from dead socket
     */
    public void setIsClosedConnection() {
        isClosedConnection = true;
    }
    
    /**
     * Using the myBuffer Object to buffer and send, this method will take the object
     * convert it to a byte array then loop through using the myBuffer to write the ByteBuffer
     * to the given SocketChannel. The Data is as followes the first byte is reserved for an integer
     * to say how big the object we are sending is in bytes...
     */
    public void sendObjectThroughChannel(Object myObject) throws IOException {
        byte[] mybytearray = convertObjectToByteArray(myObject);
        int sizeOfObject = mybytearray.length;
        int currPosition = 0;
        int nextPos = 0;
        while (currPosition < mybytearray.length) {
            if (currPosition == 0) {
                myBuffer.putInt(MARK_AS_VALID_SCHEDFOX_PACKET);
                myBuffer.putInt(sizeOfObject);
            }
            
            nextPos = myBuffer.limit() - myBuffer.position();
            if (nextPos + currPosition > mybytearray.length) {
                nextPos = mybytearray.length - currPosition;
            }
            
            myBuffer.put(mybytearray, currPosition, nextPos);
            currPosition += nextPos;
            
            try {
                myBuffer.flip();
                int numSent = myChannel.write(myBuffer);
            }
            catch (IOException e) { currPosition = Integer.MAX_VALUE; throw(e); }
            finally { myBuffer.clear(); }
        }
    }
    
    /**
     * Uses the ByteBuffer in this class to loop through the SocketChannels read
     * until the data stops arriving and then runs it through a ObjectInput and so
     * on to construct a object...
     */
    public Object constructObjectFromDataInChannel() throws IOException, ClassNotFoundException {
        if(isClosedConnection) {
            if(myChannel.isOpen())
                myChannel.close();
            return null;
        }
        
        int readValue = 0;
        Long firstBlank = null;
        try {
            byte[] myFullByte = null;
            int currPosition = 0;
            int incrementSize = BUFFER_CAPACITY;
            boolean doneReading = false;
            myBuffer.clear();
            do {
                readValue = myChannel.read(myBuffer);
                if(readValue == -1) {
                    if(!myChannel.isConnected()) {
                        isClosedConnection = true;
                    }
                    return null;
                }
                
                if (readValue == 0) {
                    if (firstBlank == null) {
                        firstBlank = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - firstBlank > 10000) {
                        throw(new IOException("constructObjectFromDataInChannel: Read timed out"));
                    }
                } else {
                    firstBlank = null;
                    myBuffer.flip();
                    
                    if (currPosition == 0) {
                        int packetType = myBuffer.getInt();
                        if (packetType != MARK_AS_VALID_SCHEDFOX_PACKET) {
                            if (packetType == MARK_AS_A_PING_PACKET) {
                                return new PingConnectionClass();
                            }
                            return null;
                        }
                        int SizeOfArray = myBuffer.getInt();
                        myFullByte = new byte[SizeOfArray];
                    }
                    incrementSize = myBuffer.limit() - myBuffer.position();
                    if (incrementSize > myFullByte.length) {
                        incrementSize = myFullByte.length;
                    }
                    myBuffer.get(myFullByte, currPosition, incrementSize);
                    myBuffer.clear();
                    currPosition += incrementSize;
                    if (currPosition >= myFullByte.length) {
                        doneReading = true;
                    }
                }
            } while (readValue > -1 && !doneReading);
            myByteArrayInputStream = new ByteArrayInputStream(myFullByte);
            myObjectInputStream = new ObjectInputStream(myByteArrayInputStream);
            return myObjectInputStream.readObject();
            
       }
        catch (IOException e)               { isClosedConnection = true; throw(e); }
        catch (ClassNotFoundException e)    { isClosedConnection = true; throw(e); }            
        finally { myBuffer.clear(); }
    }
    
}
