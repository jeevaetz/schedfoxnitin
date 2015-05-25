/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class UserComboModel implements ComboBoxModel {

    private ArrayList<User> users;
    private Integer selectedItem;

    public UserComboModel() {
        users = new ArrayList<User>();
        selectedItem = 0;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for (int e = 0; e < users.size(); e++) {
            if (users.get(e).getUserId().equals(((User) anItem).getUserId())) {
                selectedItem = e;
            }
        }
    }

    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public Object getSelectedItem() {
        try {
            return users.get(selectedItem);
        } catch (Exception exe) {
            return "";
        }
    }

    @Override
    public int getSize() {
        return users.size();
    }

    @Override
    public Object getElementAt(int index) {
        return users.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

}
