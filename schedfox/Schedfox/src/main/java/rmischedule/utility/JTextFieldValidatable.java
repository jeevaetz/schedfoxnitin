/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.utility;

import com.creamtec.ajaxswing.support.validation.Validatable;
import javax.swing.JTextField;

/**
 *
 * @author hal
 */
public class JTextFieldValidatable extends JTextField implements Validatable {

    private String displayName;
    private String errorMessage;
    private String mask = Validatable.DEFUALT_MASK;
    private boolean required = Validatable.DEFUALT_REQUIRED;
    private int maxLength = Validatable.DEFUALT_MAX_LENGTH;
    private int minLength = Validatable.DEFUALT_MIN_LENGTH;

    public static JTextFieldValidatable getPhonetextField() {

        JTextFieldValidatable myPhoneFormatter = new JTextFieldValidatable();
        myPhoneFormatter.setRequired(true);
        myPhoneFormatter.setMask(Validatable.MASK_TELEPHONE);
        myPhoneFormatter.setErrorMessage("Invalid Phone#");
        return (myPhoneFormatter);

    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
