/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.Group;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class GroupComboModel implements ComboBoxModel {

    private ArrayList<Group> groups;
    private Integer selectedItem;

    public GroupComboModel() {
        groups = new ArrayList<Group>();
        selectedItem = 0;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for (int e = 0; e < groups.size(); e++) {
            if (groups.get(e).getGroupId() == ((Group) anItem).getGroupId()) {
                selectedItem = e;
            }
        }
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    @Override
    public Object getSelectedItem() {
        try {
            return groups.get(selectedItem);
        } catch (Exception exe) {
            return "";
        }
    }

    @Override
    public int getSize() {
        return groups.size();
    }

    @Override
    public Object getElementAt(int index) {
        return groups.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

}
