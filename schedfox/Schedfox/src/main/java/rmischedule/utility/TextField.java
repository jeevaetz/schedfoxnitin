/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.utility;

import com.creamtec.ajaxswing.AjaxSwingManager;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import rmischedule.ajaxswing.renderers.JTextFieldRenderer;

/**
 *
 * @author vnguyen
 */
public class TextField {

//    /**
//     * Method for returning a text field, returns a normal one if we are in the web
//     * cause the placeholders kill everything, otherwise if just swing returns w/
//     * placeholders.
//     * @return JTextField
//     */
    public static JTextField getPhoneTextField() {
        if (!AjaxSwingManager.isAjaxSwingRunning()) {
            try {
                javax.swing.text.MaskFormatter myPhoneFormatter = new javax.swing.text.MaskFormatter("###'-###'-####");
                myPhoneFormatter.setPlaceholderCharacter('_');
                return new JFormattedTextField(myPhoneFormatter);
            } catch (Exception e) {
                return new JTextField();
            }
        } else {
            JTextFieldValidatable myField = new JTextFieldValidatable();
            myField.setMask("###-###-####");
            return myField;
        }
    }

    public static JTextField getSSNNewTextField() {
        if (!AjaxSwingManager.isAjaxSwingRunning()) {
            try {
                javax.swing.text.MaskFormatter myPhoneFormatter = new javax.swing.text.MaskFormatter("AAA'-AA'-####");
                myPhoneFormatter.setValidCharacters("0123456789X");
                myPhoneFormatter.setPlaceholderCharacter('_');
                return new JFormattedTextField(myPhoneFormatter);
            } catch (Exception e) {
                return new JTextField();
            }
        } else {
            JTextFieldValidatable myField = new JTextFieldValidatable();
            myField.setMask("###-##-####");
            return myField;
        }
    }

    public static JFormattedTextField getZipTextField() {
        try {
            javax.swing.text.MaskFormatter myPhoneFormatter = new javax.swing.text.MaskFormatter("#####");
            myPhoneFormatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(myPhoneFormatter);
        } catch (Exception e) {
        }
        return new JFormattedTextField();
    }

    public static JTextField getStateTextField() {
        if (!AjaxSwingManager.isAjaxSwingRunning()) {
            try {
                javax.swing.text.MaskFormatter myPhoneFormatter = new javax.swing.text.MaskFormatter("UU");
                myPhoneFormatter.setPlaceholderCharacter('_');
                return new JFormattedTextField(myPhoneFormatter);
            } catch (Exception e) {
            }
        }
        return new JTextField();
    }

    public static JTextField getJFormattedTextField() {
        return new JFormattedTextField();
    }

    public static String editPhoneField(String number) {

        boolean sw = true;
        int hasDash = 2;
        StringBuilder strBld = new StringBuilder(number);

        if (strBld.charAt(0) == ' ') {
            strBld.deleteCharAt(0);
        }

        for (int i = 0; i < strBld.length(); i++) {
            if ((strBld.charAt(i) < '0') || (strBld.charAt(i) > '9')) {
                if (strBld.charAt(i) != '-') {
                    sw = false;
                } else {
                    hasDash -= 1;
                    if (i != 3 && i != 7) {
                        sw = false;
                    }
                }
            }
        }

        if (sw == true && (strBld.length() + hasDash != 12)) {
            sw = false;
        }

        if (sw == true && strBld.length() == 10) {
            strBld.insert(3, '-');
            strBld.insert(7, '-');
            return (new String(strBld.substring(0, 12)));
        }


        if (strBld.length() != 12) {
            sw = false;
        }
        if (sw == false) {
            return ("");
        }


        return (strBld.substring(0, 12));
    }
}
