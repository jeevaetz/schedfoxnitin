/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.xprint.data;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Ira
 */
public class RowComparator implements Comparator {
    public RowComparator() {

    }   
    
    public int compare(Object o1, Object o2) {
        int retVal = 0;
        if (o1 instanceof java.lang.Integer) {
            return ((Integer)o1).compareTo((Integer)o2);
        } else if (o1 instanceof java.lang.String) {
            return ((String)o1).compareTo((String)o2);
        } else if (o1 instanceof java.util.Date) {
            return ((Date)o1).compareTo((Date)o2);
        }
        return retVal;
    }
        
}
