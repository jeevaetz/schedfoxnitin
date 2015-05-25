/*
 * Begin_Logon.java
 *
 * Created on February 2, 2005, 8:30 AM
 */

package rmischedule.login;
import rmischedule.main.*;
import java.lang.Thread;
/**
 * 
 * @author jason.allen
 */
public class Begin_Logon extends Thread{
    public boolean stop;
    public Login_Wait lw;
    public Login_Frame lf;
    public Begin_Logon(Main_Window parent, Login_Frame alf){
        lf = alf;
        lw = new Login_Wait();
        parent.desktop.add(lw);
    }

    public void run(){   
        lw.setVisible(true);
        
        while(!stop){
            try{
                Thread.sleep(25);
            }catch(InterruptedException e){

            }                  
            lw.incrementProgress();
        }
        lw.stopLoading();
    }
}
