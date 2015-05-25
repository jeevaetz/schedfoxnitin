/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.client.components;

import org.apache.commons.validator.EmailValidator;
import schedfoxlib.model.EmailContact;

/**
 *
 * @author user
 */
public class EmailLabel extends ContactLabel {

    private static EmailValidator emailValidator = EmailValidator.getInstance();

    public EmailLabel(Object contact) {
        super(contact);
    }
    
    @Override
    public String getContactLabel(Object contact) {
        return ((EmailContact)contact).getFullName();
    }

    @Override
    public String getContact(Object contact) {
        return ((EmailContact)contact).getEmailAddress();
    }

    @Override
    public boolean isValid(Object contact) {
        return emailValidator.isValid(((EmailContact)contact).getEmailAddress());
    }

}
