/*
 * TimeEditTextField.java
 *
 * Created on March 11, 2005, 8:01 AM
 */
package rmischedule.components.graphicalcomponents;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.text.DefaultFormatterFactory;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.main.Main_Window;

/**
 *
 * @author ira
 */
public class TimeEditTextField extends JPanel {

    private boolean is12hour;
    private JFormattedTextField myField;
    private JComboBox myAMPMCombo;
    private BoxLayout myLayout;
    private String[] amPm = {"AM", "PM"};
    private JPanel spacer;
    private JFormattedTextField.AbstractFormatter myFormat;
    private SimpleDateFormat fullAmPmFormat = new SimpleDateFormat("hh:mm aa");
    private SimpleDateFormat amPmFormat = new SimpleDateFormat("hh:mm");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    private SimpleDateFormat amPmOnlyFormat = new SimpleDateFormat("aa");
    private SimpleDateFormat militaryFormat = new SimpleDateFormat("HHmm");
    private Calendar myTime;

    /** Creates a new instance of TimeEditTextField */
    public TimeEditTextField() {
        setOpaque(false);

        myTime = Calendar.getInstance();

        myField = new JFormattedTextField();
        myField.setMinimumSize(new Dimension(70, 28));
        myField.setMaximumSize(new Dimension(70, 28));
        myField.setPreferredSize(new Dimension(70, 28));
        myAMPMCombo = new JComboBox(amPm);
        myLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(myLayout);
        add(myField);
        is12hour = Main_Window.parentOfApplication.is12HourFormat();
        if (is12hour) {
            add(myAMPMCombo);
        }
        spacer = new JPanel();
        spacer.setOpaque(false);
        add(spacer);
        myFormat = new myFormatter();
        DefaultFormatterFactory factory = new DefaultFormatterFactory(myFormat, myFormat, myFormat);
        myField.setFormatterFactory(factory);
        myField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                runOnFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                runOnFocusLost(evt);
            }
        });

        if (is12hour) {
            this.setMinimumSize(new java.awt.Dimension(60, 28));
            this.setPreferredSize(new java.awt.Dimension(60, 28));
        } else {
            this.setMinimumSize(new java.awt.Dimension(40, 28));
            this.setPreferredSize(new java.awt.Dimension(40, 28));
        }
    }

    public void setValue(String newVal) {
        myField.setValue(newVal);
    }

    public Object getValue() {
        try {
            return myFormat.stringToValue(myField.getText());
        } catch (Exception e) {
            return "0";
        }
        //return myField.getValue();
    }

    public void selectAll() {
        myField.selectAll();
    }

    public void runOnFocusGained(java.awt.event.FocusEvent evt) {
        myField.setText(myField.getText());
        myField.selectAll();
    }

    public void runOnFocusLost(java.awt.event.FocusEvent evt) {
        myField.select(0, 0);
    }

    /**
     * Our formatter for our text fields....
     */
    private class myFormatter extends JFormattedTextField.AbstractFormatter {

        public Object stringToValue(String text) {
            Date tempDate = null;
            if (myAMPMCombo.isVisible()) {
                text = text + " " + myAMPMCombo.getSelectedItem();
            }
            try {
                tempDate = militaryFormat.parse(text);
            } catch (Exception e) {
            }
            if (tempDate == null) {
                try {
                    tempDate = fullAmPmFormat.parse(text);
                } catch (Exception e) {
                }
            }
            if (tempDate == null) {
                try {
                    tempDate = hourFormat.parse(text);
                } catch (Exception e) {
                }
            }

            if (tempDate == null) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Could not parse date!");
                return "0";
            } else {
                myTime.setTime(tempDate);
                if (myAMPMCombo.isVisible() && myTime.get(Calendar.HOUR_OF_DAY) > 12) {
                    myAMPMCombo.setSelectedItem(amPmOnlyFormat.format(myTime.getTime()));
                }
                return ((myTime.get(Calendar.HOUR_OF_DAY) * 60) + myTime.get(Calendar.MINUTE)) + "";
            }
        }

        public String valueToString(Object value) {
            try {
                int numberOfMinutes = Integer.parseInt(value.toString());
                myTime.set(Calendar.MINUTE, numberOfMinutes % 60);
                myTime.set(Calendar.HOUR_OF_DAY, numberOfMinutes / 60);
                if (myAMPMCombo.isVisible()) {
                    if (numberOfMinutes >= 770) {
                        myAMPMCombo.setSelectedItem("PM");
                    } else {
                        myAMPMCombo.setSelectedItem("AM");
                    }
                }
            } catch (Exception e) {
                myTime.set(Calendar.MINUTE, 0);
                myTime.set(Calendar.HOUR, 0);
            }
            if (is12hour) {
                return amPmFormat.format(myTime.getTime());
            } else {
                String data = militaryFormat.format(myTime.getTime());
                if (data.equalsIgnoreCase("0000")) {
                    data = "2400";
                }
                return data;
            }
        }
    }
}
