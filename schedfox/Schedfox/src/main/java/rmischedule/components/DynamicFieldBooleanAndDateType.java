/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DynamicFieldBolleanAndDateType.java
 *
 * Created on Aug 18, 2010, 2:28:11 PM
 */

package rmischedule.components;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComponent;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.xadmin.model.DynamicFieldValue;

/**
 *
 * @author user
 */
public class DynamicFieldBooleanAndDateType extends javax.swing.JPanel implements DynamicFieldTypeInterface {

    private JCalendarComboBox myCal;
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

    /** Creates new form DynamicFieldBolleanAndDateType */
    public DynamicFieldBooleanAndDateType(boolean enabled) {
        initComponents();

        myCal = new JCalendarComboBox();
        this.add(myCal);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(myCheckBox);

        jPanel1.setMaximumSize(new java.awt.Dimension(100, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 23));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox myCheckBox;
    // End of variables declaration//GEN-END:variables

    public String getValue() {
        String value = "";
        String dateValue = myFormat.format(((JCalendarComboBox)myCal).getCalendar().getTime());
        if (myCheckBox.isSelected()) {
            value += "yes";
        } else {
            value += "no";
        }
        return value + ":" + dateValue;
    }

    public void setValue(DynamicFieldValue value) {
        String[] values = value.getDynamic_field_value().split(":");
        if (values.length > 0) {
            if (values[0] != null && values[0].equals("yes")) {
                myCheckBox.setSelected(true);
            }
        }
        if (values.length > 1 && values[1] != null) {
            try {
                Date dateToSet = new Date();
                Calendar myCalendar = Calendar.getInstance();
                try {
                    dateToSet = myFormat.parse(values[1]);
                } catch (Exception e) {

                }
                myCalendar.setTime(dateToSet);
                ((JCalendarComboBox)myCal).setCalendar(myCalendar);
            } catch (Exception e) {}
        }
    }

    public JComponent getComponent() {
        return this;
    }

}
