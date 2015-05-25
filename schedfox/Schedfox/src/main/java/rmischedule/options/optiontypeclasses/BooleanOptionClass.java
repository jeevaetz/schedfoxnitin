/*
 * BooleanOptionClass.java
 *
 * Created on September 1, 2005, 2:32 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options.optiontypeclasses;
import rmischedule.options.optionsgraphical.*;
import rmischedule.options.IndividualOption;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public class BooleanOptionClass extends IndividualOption { 
    
    /** Creates a new instance of BooleanOptionClass */
    public BooleanOptionClass(int managementCo, int UserId, String myOptionName, String category, String display) {
        super(managementCo, UserId, myOptionName, category, display);
    }

    public java.lang.Object convertFromDataToObject(String data) {
        if (data.equals("true")) {
            return true;
        }
        return false;
    }

    public String convertFromObjectToData(Object object) {
        if ((Boolean)object.equals(true)) {
            return "true";
        }
        return "false";
    }

    public javax.swing.JPanel getGraphicalComponent(int AccessLevel) {
        return new OptionCheckBox(this, AccessLevel);
    }
}
