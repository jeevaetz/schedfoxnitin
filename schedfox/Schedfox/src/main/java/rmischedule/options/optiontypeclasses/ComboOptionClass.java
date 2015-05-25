/*
 * ComboOptionClass.java
 *
 * Created on October 6, 2005, 12:13 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options.optiontypeclasses;
import rmischedule.options.optionsgraphical.*;
import rmischedule.options.IndividualOption;
import javax.swing.*;
import java.util.*;
/**
 *
 * @author Ira Juneau
 */
public class ComboOptionClass extends IndividualOption {
    
    /** Creates a new instance of ComboOptionClass */
    public ComboOptionClass(int managementCo, int UserId, String myOptionName, String category, String display) {
        super(managementCo, UserId, myOptionName, category, display);
    }
    
    /**
     * Ack how crappy is this, well returns a vector, the first entry being currently selected
     * option, the next being the option choices....yeah it sucks...
     */
    public java.lang.Object convertFromDataToObject(String data) {
        StringTokenizer sToken = new StringTokenizer(data, "|:");
        Vector returnData = new Vector();
        returnData.add(sToken.nextToken());
        for (int i = 0; i <= sToken.countTokens(); i++) {
            returnData.add(sToken.nextToken());
        }
        return returnData;
    }
    
    /**
     * Takes in a combo box...outputs a String as follows
     * Current Selected Item|combochoice|combochoice|combochoice
     */
    public String convertFromObjectToData(Object object) {
        JComboBox myCombo = (JComboBox)object;
        String returnData = myCombo.getSelectedItem() + "|";
        for (int i = 0; i < myCombo.getItemCount(); i++) {
            returnData = returnData + myCombo.getItemAt(i) + ":";
        }
        returnData = returnData.substring(0, returnData.length() - 1);
        return returnData;
    }

    public javax.swing.JPanel getGraphicalComponent(int AccessLevel) {
        return new OptionComboBox(this, AccessLevel);
    }    
}
