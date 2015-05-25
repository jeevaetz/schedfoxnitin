/*
 * StringOptionClass.java
 *
 * Created on September 1, 2005, 2:28 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options.optiontypeclasses;
import rmischedule.options.*;
import rmischedule.options.optionsgraphical.OptionsStringBox;
/**
 *
 * @author Ira Juneau
 */
public class StringOptionClass extends IndividualOption {
    
    /** Creates a new instance of StringOptionClass */
    public StringOptionClass(int managementCo, int UserId, String myOptionName, String category, String display) {
        super(managementCo, UserId, myOptionName, category, display);
    }

    public java.lang.Object convertFromDataToObject(String data) {
        return data;
    }

    public String convertFromObjectToData(Object object) {
        return (String)object;
    }

    public javax.swing.JPanel getGraphicalComponent(int AccessLevel) {
        return new OptionsStringBox(this, AccessLevel);
    }


    
}
