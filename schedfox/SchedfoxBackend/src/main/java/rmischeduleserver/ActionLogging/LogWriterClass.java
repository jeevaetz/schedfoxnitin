/*
 * LogWriterClass.java
 *
 * Created on May 24, 2005, 2:08 PM
 */

package rmischeduleserver.ActionLogging;
import java.util.concurrent.ArrayBlockingQueue;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.io.*;
import java.util.Calendar;
/**
 *
 * @author ira 
 */
public class LogWriterClass {
    
    private ArrayBlockingQueue myLoggQueue;
    private myLoggingThread myThread;
    private FileOutputStream myFileWriter;
    private ObjectOutputStream myOutput;
    private Calendar myCal;
    
    /** Creates a new instance of LogWriterClass */
    public LogWriterClass() {
        myCal = Calendar.getInstance();
        String fileName = new String("Log" + myCal.get(Calendar.YEAR) + myCal.get(Calendar.MONTH) + myCal.get(Calendar.YEAR) + ".sfox");
        myLoggQueue = new ArrayBlockingQueue(1000);
        try {
            myFileWriter = new FileOutputStream(fileName);
            myOutput = new ObjectOutputStream(myFileWriter);
            myThread = new myLoggingThread();
            //myThread.start();
        } catch (Exception e) {
            System.out.println("Warning: Could not open logging file, logs are disabled!");
        }
        
    }
    
    public void addLogMessage(GeneralQueryFormat message) {
        //myLoggQueue.add(message);
    }
    
    public void writeMessageToFile(GeneralQueryFormat message) {
        LogClass myLog = new LogClass(message);
        try {
            myOutput.writeObject(myLog);
            myOutput.flush();
        } catch (Exception e) {
            System.out.println("Could not write Log to file...");
        }
    }
    
    private class myLoggingThread extends Thread {
        public myLoggingThread() {
            this.setPriority(Thread.MIN_PRIORITY);
        }
        
        public void run() {
            this.setPriority(Thread.MIN_PRIORITY);
            while(true) {
                try {
                    sleep(10);
                    writeMessageToFile((GeneralQueryFormat)myLoggQueue.take());
                } catch (Exception e) {}
            }
        }
    }
    
}
