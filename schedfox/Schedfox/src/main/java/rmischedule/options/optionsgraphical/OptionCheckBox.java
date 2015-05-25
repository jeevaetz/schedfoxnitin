/*
 * OptionCheckBox.java
 *
 * Created on November 8, 2004, 2:23 PM
 */

package rmischedule.options.optionsgraphical;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import rmischedule.options.*;
import rmischedule.options.optiontypeclasses.*;
/**
 *
 * @author  ira
 */
public class OptionCheckBox extends JPanel{
    
    private BooleanOptionClass options;
    private OptionsWindow parentWindow;
    private String name;
    private JCheckBox myCheck;
    private int access;
    
    /** Creates a new instance of OptionCheckBox */
    public OptionCheckBox(BooleanOptionClass myOption, int AccessLevel) {
        myCheck = new JCheckBox(myOption.getMyDisplay());
        name = myOption.getMyName();
        options = myOption;
        access = AccessLevel;
        this.setPreferredSize(new Dimension(240, 25));
        this.setMaximumSize(new Dimension(1000, 25));
        this.setLayout(new GridLayout(1,1));
        //myCheck.setHorizontalAlignment(this.LEADING);
        this.add(myCheck);
        myCheck.setAlignmentX(this.LEFT_ALIGNMENT);
        if (((Boolean)options.read(access)).booleanValue()) {
            myCheck.setSelected(true);
        } else {
            myCheck.setSelected(false);
        }
        myCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save();
            }
        });
    }
    
    private void save() {
        options.writeOption(myCheck.isSelected(), access);
    }
}
