/*
 * ScheduleThread.java
 *
 * Created on July 12, 2005, 1:35 PM
 */

package rmischedule.schedule;

/**
 *
 * @author ira
 */
public abstract class ScheduleThread extends Thread {
    
    public ScheduleThread() {
        
    }
    
    public void registerMe(Schedule_View_Panel myView) {
        try {
            myView.registerNewThread(this);
        } catch (Exception e) {
            System.out.println("Failed to register thread");
        }
    }
    
    public abstract void killMe();
}
