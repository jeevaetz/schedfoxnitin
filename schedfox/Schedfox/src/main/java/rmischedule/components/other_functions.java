/*
 * other_functions.java
 *
 * Created on August 18, 2004, 4:17 PM
 */

package rmischedule.components;
import java.util.StringTokenizer;
/**
 *
 * @author  jason.allen
 */
public class other_functions {
    
    /** Creates a new instance of other_functions */
    public other_functions() {
    }

    public static java.util.Calendar str2Cal(String date, java.util.Calendar cal) {
        if(date == null){
            return null;
        }
        
        if(date.equals("0000-00-00")){
            return cal;
        }
        StringTokenizer st = new StringTokenizer(date, " -");
        
        if(st.countTokens() < 3){
            return cal;
        }
        int year  = Integer.parseInt(st.nextToken()); 
        int month = Integer.parseInt(st.nextToken());
        int day   = Integer.parseInt(st.nextToken());

        
        cal.set(year, (month - 1), day);
        return cal;         
    }
    
    public static java.util.Calendar str2Cal(String date){
        java.util.Calendar Cal = java.util.Calendar.getInstance();
        return str2Cal(date, Cal);
    }

    public static void maxlength(javax.swing.JTextField ef, int length){
        if(ef.getText().length() > length){
            try{
                ef.setText(ef.getText(0,(length - 1)));
            }catch(Exception e){
                e.printStackTrace();
            }
        }           
    }
    
    public static String cal2Str(java.util.Calendar cal){
            if(cal == null){return "0000-00-00";}
            return(   
                  Integer.toString(cal.get(cal.YEAR)) + "-"
                + Integer.toString(cal.get(cal.MONTH) + 1) + "-"
                + Integer.toString(cal.get(cal.DATE))
            );        
    }
    
}
