/*
 * ScheduleThreadQueue.java
 *
 * Created on July 24, 2006, 8:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.schedule;

import java.util.*;

/**
 *
 * @author shawn
 */
public class ScheduleThreadQueue {
    
    private static RunThread myThread = new RunThread();    
    public static void invokeLater(Runnable r) { ScheduleThreadQueue.myThread.add(r); }
    public static void waitForQueue() {
        while(ScheduleThreadQueue.myThread.isWorking()) {
            try { Thread.sleep(100); } catch(InterruptedException ex) { }
        }
    }
    
    
    private static class RunThread extends Thread {
        
        private LinkedList<Runnable> threadQueue = new LinkedList();
        
        public RunThread() { this.start(); }        
        public boolean isWorking() { return this.threadQueue.size() > 0; }
        
        public void add(Runnable r) {
            this.threadQueue.add(r);
            this.interrupt();
        }
        
        public void run() {
            while(true) {
                try { this.sleep(100000); } catch (InterruptedException ex) { }

                while(this.threadQueue.size() > 0) {
                    Runnable runMe = this.threadQueue.remove();
                    try { runMe.run(); } catch(Exception ex) { }
                }
            }
        }
    }
    
}
