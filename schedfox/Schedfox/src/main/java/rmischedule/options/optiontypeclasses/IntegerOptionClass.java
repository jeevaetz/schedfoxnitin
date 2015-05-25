/*
 * IntegerOptionClass.java
 *
 * Created on September 1, 2005, 2:36 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options.optiontypeclasses;
import rmischedule.options.IndividualOption;
import rmischedule.options.optionsgraphical.*;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public class IntegerOptionClass extends IndividualOption {
    
    /** Creates a new instance of IntegerOptionClass */
    public IntegerOptionClass(int managementCo, int UserId, String myOptionName, String category, String display) {
        super(managementCo, UserId, myOptionName, category, display);
    }
    
    public Object convertFromDataToObject(String data) {
        return (Integer.parseInt(data));
    }

    public String convertFromObjectToData(Object object) {
        return (((Integer)object).intValue()) + "";
    }

    public javax.swing.JPanel getGraphicalComponent(int AccessLevel) {
        return new OptionsIntBox(this, AccessLevel);
    }
    
}
