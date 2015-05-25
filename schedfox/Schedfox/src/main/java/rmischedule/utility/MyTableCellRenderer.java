/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.utility;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dalbers
 */
public class MyTableCellRenderer extends JLabel implements TableCellRenderer {
    
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

        if(vColIndex == 1){
            this.setForeground(Color.BLUE);
        }
        
        if (isSelected) {
            this.setForeground(Color.BLUE.darker());
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(table.getBackground());
        }

        if (hasFocus) {
        }

        this.setForeground(Color.BLUE);
        
        
        setText(value.toString());

        return this;
    }

    public void changeBackground(Color color){
        this.setBackground(color);
    }


    // The following methods override the defaults for performance reasons
    public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
}
