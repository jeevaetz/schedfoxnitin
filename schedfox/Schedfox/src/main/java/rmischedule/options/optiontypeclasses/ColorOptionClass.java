/*
 * ColorOptionClass.java
 *
 * Created on September 1, 2005, 2:39 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options.optiontypeclasses;
import rmischedule.options.IndividualOption;
import rmischedule.options.optionsgraphical.*;
import java.awt.Color;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public class ColorOptionClass extends IndividualOption {
    
    /** Creates a new instance of ColorOptionClass */
    public ColorOptionClass(int managementCo, int UserId, String myOptionName, String category, String display) {
        super(managementCo, UserId, myOptionName, category, display);
    }
    
    public Object convertFromDataToObject(String data) {
        return (Color.decode(data));
    }

    public String convertFromObjectToData(Object object) {
        return Integer.toString(((Color)object).getRGB());
    }

    public javax.swing.JPanel getGraphicalComponent(int AccessLevel) {
        return new OptionColorPanel(this, AccessLevel);
    }
}
