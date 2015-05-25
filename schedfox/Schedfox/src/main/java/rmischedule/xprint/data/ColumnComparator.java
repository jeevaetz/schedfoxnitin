/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.xprint.data;

import java.util.Comparator;
import rmischedule.main.Main_Window;

/**
 *
 * @author Ira
 */
public class ColumnComparator implements Comparator {
    
    public ColumnComparator() {

    }   
    
    public int compare(Object o1, Object o2) {
            int pos1 = 0;
            int pos2 = 0;
            String column1 = (String)o1;
            String column2 = (String)o2;
            String columnHeaders[] = Main_Window.lastColumnsForGenericReport;
            for (int i = 0; i < columnHeaders.length; i++) {
                if (columnHeaders[i].equals(column1)) {
                    pos1 = i;
                } 
                if (columnHeaders[i].equals(column2)) {
                    pos2 = i;
                }
            }
            return (pos1 - pos2);
        }
        
        
    }
