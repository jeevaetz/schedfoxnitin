/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.log;


import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;




import org.apache.log4j.FileAppender;

import org.apache.log4j.PatternLayout;



/**
 *
 * @author dalbers
 */
public class MyLogger {
    public static MyLogger INSTANCE = new MyLogger();
    private Logger log = Logger.getLogger("IVRLogger");



    private MyLogger(){
        PatternLayout myLayout = new PatternLayout("%p | %d | %t | %c - %m%n");
        try{
            BasicConfigurator.configure(new FileAppender(myLayout,"/var/log/freeswitch/freeswitch.log"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static MyLogger getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MyLogger();
        }
        return INSTANCE;
    }

    public Logger getLog(){

        return log;
    }

    public void logException(Class _class,String method,Exception e){
        log.info("Exception from class: "+_class.getSimpleName()+":"+method);
        log.error(e);
    }

    public void logMessage(String message){
        log.info(message);
    }

    

}
