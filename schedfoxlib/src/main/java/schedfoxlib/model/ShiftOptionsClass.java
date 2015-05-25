/*
 * ShiftOptionsClass.java
 *
 * Created on August 11, 2005, 10:48 AM
 *
 * Parses our options for both bill and pay
 * 
 * 
 */

package schedfoxlib.model;


/**
 *
 * @author jason.allen
 */
public class ShiftOptionsClass {
    
    private final static int BREAK_LEN_OFFSET = 0;
    private final static int BREAK_LEN_LENGTH = 3;

    private final static int BREAK_START_OFFSET = 3;
    private final static int BREAK_START_LENGTH = 4;
    
    
    private int myBreakLength;     //In Min 3 digit max
    private int myBreakStart;      //In Minutes
    
    /** Creates a new instance of ShiftOptionsClass */
    public ShiftOptionsClass(){
        myBreakLength = 0;
        myBreakStart  = 0;
    }
    
    /**
     *  Returns Break Length in minutes
     */
    public int getBreakLength(){ return myBreakLength; }

    /**
     *  Returns Break Start Time in minutes
     */
    public int getBreakStart() { return myBreakStart;  }
    
    /**
     *  Sets Break Length @parm in minutes
     */
    public void setBreakLength(int m){ myBreakLength = m; }

    /**
     *  Sets Break Start Time @parm in minutes
     */
    public void setBreakStart (int m){ myBreakStart = m; }
    
    /**
 *  Breaks down @parm to its based components.  Must be done before any value
     *  Will be returned.
     */
    public void parse(String value){
        if(value == null){  return; }
        if(value.length() < BREAK_LEN_LENGTH){return;}
        
        String s;

        /**
         * Take care of formatting issue 0300000 vs 300000
         * both should be the same...
         */
        int addOnVal = 0;
        if (value.length() == 7) {
            addOnVal = 1;
        }
        
        try{
            s = value.substring(BREAK_LEN_OFFSET + addOnVal, BREAK_LEN_LENGTH - 1 + addOnVal);
            myBreakLength = Integer.parseInt(s);
        }catch(Exception e){
            myBreakLength = 0;
        }
        

        try{
            s = value.substring(BREAK_START_OFFSET + addOnVal, BREAK_START_LENGTH + BREAK_START_OFFSET + addOnVal);
            myBreakStart = Integer.parseInt(s);
        }catch(Exception e){
            myBreakStart = 0;
        }
    }
    
    public String toString(){
        return getBreakLengthString() + getBreakStartString();
    }
    
    public String getBreakLengthString(){
        return convertIntToString(getBreakLength(), 3);
    }

    public String getBreakStartString(){
        return convertIntToString(getBreakStart(), 4);
    }
    
    /**
     * Returns n in string with leading zeroes depending on the no of 
     * digits needed.
     */    
    private String convertIntToString(int n, int digits){
        String t, s;

        t = "";
        s = String.valueOf(n);
        
        if(s.length() > digits){
            t = s.substring(s.length() - digits, digits);
        }else if(s.length() == digits){
            t = s;
        }else{
            for(int i=s.length(); i < digits; i++){
                t += "0";
            }
            t += s;
        }
        
        return t;        
    }
}
