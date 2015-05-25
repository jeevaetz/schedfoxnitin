/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.client.components;

import schedfoxlib.model.PhoneContact;

/**
 *
 * @author user
 */
public class PhoneLabel extends ContactLabel {

    public PhoneLabel(Object contact) {
        super(contact);
    }
    
    @Override
    public String getContactLabel(Object contact) {
        return ((PhoneContact)contact).getFullName();
    }

    @Override
    public String getContact(Object contact) {
        return ((PhoneContact)contact).getPhoneNumber();
    }

    @Override
    public boolean isValid(Object contact) {
        return true;
    }

}
