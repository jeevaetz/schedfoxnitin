/*
 * TimeOptionClass.java
 *
 * Created on September 1, 2005, 2:42 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options.optiontypeclasses;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.options.IndividualOption;
import rmischedule.components.*;
import rmischedule.options.optionsgraphical.*;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public class TimeOptionClass extends IndividualOption {
    
    /** Creates a new instance of TimeOptionClass */
    public TimeOptionClass(int managementCo, int UserId, String myOptionName, String category, String display) {
        super(managementCo, UserId, myOptionName, category, display);
    }
    
    public Object convertFromDataToObject(String data) {
        return data;
    }

    public String convertFromObjectToData(Object object) {
        return StaticDateTimeFunctions.fromTextToTime((String)object);
    }

    public javax.swing.JPanel getGraphicalComponent(int AccessLevel) {
        return new OptionTimeDisplay(this, AccessLevel);
    }
}
