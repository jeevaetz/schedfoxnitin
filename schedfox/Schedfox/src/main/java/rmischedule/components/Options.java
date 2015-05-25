/*
 * options.java
 *
 * Created on October 14, 2004, 9:25 AM
 */

package rmischedule.components;

import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.components.*;
import java.util.*;
import java.awt.*;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.Connection;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
/**
 *
 * @author  jason.allen
 */

public class Options {
    
    /*
     *  Our statick stuff
     */
    
    public static final int OPTIONS_SIZE    = 100;
    public static final int OPTION_NOT_EXIST= 0;
    public static final int OPTIONS_BOOLEAN = 1;
    public static final int OPTIONS_INT     = 2;
    public static final int OPTIONS_STRING  = 3;
    public static final int OPTIONS_COLOR   = 4;
    public static final int OPTIONS_TIME    = 5;
    public static final int OPTIONS_WAIT    = 5 * 60 * 1000; //Five minutes to wait to reload
    
    /*
     *  Our options
     */
    
    public static final String OPTIONS_VAL_12HOUR   = "12hour";
    public static final String OPTIONS_SHIFT1_START = "firstshiftastarttime";
    public static final String OPTIONS_SHIFT1_END   = "firstshiftendtime";
    public static final String OPTIONS_SHIFT2_START = "secondshiftastarttime";
    public static final String OPTIONS_SHIFT2_END   = "secondshiftendtime";
    public static final String OPTIONS_SHIFT3_START = "thirdshiftastarttime";
    public static final String OPTIONS_SHIFT3_END   = "thirdshiftendtime";
    public static final String OPTIONS_SHIFT_BUFFER = "shiftbuffer";
    public static final String OPTIONS_8HR_NOTIFICATION = "8hournotification";
    
    
    /*
     *  Our Fruity variables.
     */
    private long[] options_last_updated;
    
    public optionVectorType            optionsVector;
    
    private long last_checked;
    
    private rmischedule.main.Main_Window parent;
    
    private Connection conn;
    private Record_Set rs;
    
    /**
     * Creates a new instance of options
     */
    public Options(rmischedule.main.Main_Window Parent) {
        parent = Parent;
        conn = new Connection();
        
        optionsVector            = new optionVectorType();
        
        last_checked = System.currentTimeMillis() - OPTIONS_WAIT;
        updateValues();
    }
    
    /*
     * Checks to see if options data has been updated as defined by OPTIONS_WAIT
     * Currently 5 minutes.
     */
    public boolean Check(String name) {
        if ((System.currentTimeMillis() - OPTIONS_WAIT) > last_checked) {
            updateValues();
        }
        return true;
    }
    
    /*
     * Process to retrieve boolean Options from database...
     */
    public boolean getBooleanOption(String name){
        boolean v = false;
        try {
            if ((Check(name) && optionsVector.get((name)).type == OPTIONS_BOOLEAN)){
                if (((String)optionsVector.get((name)).value).equals("true")) {
                    v = true;
                }
                
            }
        } catch (Exception e) {}
        return v;
    }
    
    /*
     * Process to retrieve int options from database...
     */
    public int getIntOption(String name){
        int v = 0;
        try {
            if ((Check(name) && optionsVector.get((name)).type == OPTIONS_INT)){
                v = Integer.parseInt((String)optionsVector.get((name)).value);
            }
        } catch (Exception e) {
        }
        return v;
    }
    
    /*
     * Returns color from database, will return black on error
     */
    public Color getColorOption(String name) {
        Color v = new Color(0,0,0);
        try {
            if ((Check(name) && optionsVector.get((name)).type == OPTIONS_COLOR)){
                
                v = Color.decode((String)optionsVector.get((name)).value);
                
            }
        } catch (Exception e) {}
        return v;
    }
    
    public String getTimeOption(String name) {
        String v = "";
        try {
            if ((Check(name) && optionsVector.get((name)).type == OPTIONS_TIME)) {
                v = ((String)optionsVector.get(optionsVector.indexOf(name)).value);
            }
        } catch (Exception e) {}
        return v;
    }
    
    /*
     * Should be called to set Colors to ensure data integrity
     */
    public void setColorOption(String name, Color color) {
        String val = Integer.toString(color.getRGB());
        setValue(name, val);
    }
    
    
    
    public void setTimeOption(String name, String time) {
        String val = StaticDateTimeFunctions.fromTextToTime(time);
        setValue(name, val);
    }
    
    public String getStringOption(String name){
        String v = "";
        if ((Check(name) && optionsVector.get((name)).type == OPTIONS_TIME)) {
            v = ((String)optionsVector.get(optionsVector.indexOf(name)).value);
        }
        return v;
    }
    
    public int getOptionType(String name) {
        return optionsVector.get((name)).type;
    }
    
    public void setValue(String name, String value) {
        update_existing_options_query mySaveQuery = new update_existing_options_query();
        //mySaveQuery.update(value, name);
        try {
            conn.executeUpdate(mySaveQuery);
            updateValues();
        } catch (Exception e) {
            System.out.println("Options could not be saved");
        }
    }
    
    /*
     * Updates our data, namely three objects, A vector object of type options
     * and two hash tables...
     */
    public void updateValues() {
        options_query myOptionsQuery = new options_query();
        rs = new Record_Set();
        try {
            rs = conn.executeQuery(myOptionsQuery);
        } catch (Exception e) {
            return;
        }
        int i = 0;
        try {
            optionsVector.removeAllElements();
            rs.moveToFront();
            while (!rs.getEOF()) {
                options currOption = new options();
                currOption.type     = rs.getInt("type_field");
                currOption.name     = rs.getString("name_field");
                currOption.value    = rs.getString("value_field");
                currOption.category = rs.getString("category_field");
                currOption.access   = rs.getString("access_field");
                currOption.display  = rs.getString("display_field");
                optionsVector.add(currOption);
                rs.moveNext();
            }
            last_checked = System.currentTimeMillis();
        } catch (Exception e) {}
    }
    
    /*
     * Returns a vector containing all names and types of options, useful for displaying out
     * controls in our options window...MmmmMMMmmm fruity
     */
    public Vector getOptionNames() {
        if (Check("")) {
            return optionsVector;
        }
        return optionsVector;
    }
    
    public class optionVectorType extends Vector {
        public optionVectorType() {
            super();
        }
        
        public boolean contains(String val) {
            boolean returnVal = false;
            for (int i = 0; i < this.size(); i++) {
                if (((options)get(i)).equals(val)) {
                    return true;
                }
            }
            return false;
        }
        
        public int indexOf(String val) {
            int returnVal = -1;
            for (int i = 0; i < this.size(); i++) {
                if (((options)get(i)).equals(val)) {
                    return i;
                }
            }
            return returnVal;
        }
        
        public options get(int index) {
            return (options)super.get(index);
        }
        
        public options get(String name) {
            for (int i = 0; i < this.size(); i++) {
                if (((options)get(i)).equals(name)) {
                    return (options)get(i);
                }
            }
            return null;
        }
    }
    
    public class options implements Comparable {
        public int type;
        public String display;
        public String name;
        public Object value;
        public String category;
        public String access;
        public options() {}
        
        public int compareTo(Object o) {
            return name.compareTo(((options)o).name);
        }
        
        public boolean equals(String name) {
            
            return this.name.equals(name);
        }
        
    }
    
}
