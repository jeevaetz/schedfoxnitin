/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DynamicFieldNumberType.java
 *
 * Created on Aug 18, 2010, 1:45:03 PM
 */
package rmischedule.components;

import java.awt.Dimension;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import rmischedule.xadmin.model.DynamicFieldValue;

/**
 *
 * @author user
 */
public class DynamicFieldNumberType extends javax.swing.JPanel implements DynamicFieldTypeInterface {

    private JTextField field;

    /** Creates new form DynamicFieldNumberType */
    public DynamicFieldNumberType(boolean enabled) {
        initComponents();

        field = this.getField();
        this.add(field);
    }

    public JTextField getField() {
        JFormattedTextField tmpVal = null;
        try {
            NumberFormatter defaultFormatter = new NumberFormatter();
            NumberFormatter displayFormatter = new NumberFormatter();
            NumberFormatter editFormatter = new NumberFormatter();
            defaultFormatter.setValueClass(Integer.class);
            displayFormatter.setValueClass(Integer.class);
            editFormatter.setValueClass(Integer.class);
            DefaultFormatterFactory numberFactory =
                    new DefaultFormatterFactory(defaultFormatter, displayFormatter, editFormatter);
            tmpVal = new JFormattedTextField(numberFactory);
        } catch (Exception e) {
            tmpVal = new JFormattedTextField(NumberFormat.getIntegerInstance());
        }
        tmpVal.setPreferredSize(new Dimension(100, 24));
        tmpVal.setMinimumSize(new Dimension(50, 24));
        tmpVal.setMaximumSize(new Dimension(1000, 24));
        return tmpVal;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(32767, 30));
        setMinimumSize(new java.awt.Dimension(0, 30));
        setPreferredSize(new java.awt.Dimension(0, 30));
        setLayout(new java.awt.GridLayout(1, 0));
    }// </editor-fold>//GEN-END:initComponents

    public String getValue() {
        if (field instanceof JFormattedTextField) {
            try {
                ((JFormattedTextField)field).commitEdit();
                return ((JFormattedTextField)field).getValue().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return field.getText();
        }
        return "";
    }

    public void setValue(DynamicFieldValue value) {
        Integer tmpVal = 0;
        try {
            tmpVal = Integer.parseInt(value.getDynamic_field_value());
        } catch (Exception e) {}
        if (field instanceof JFormattedTextField) {
            try {
                ((JFormattedTextField)field).setValue(tmpVal);
            } catch (Exception e) {}
        } else {
            field.setText("");
        }
    }

    public JComponent getComponent() {
        return this;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}