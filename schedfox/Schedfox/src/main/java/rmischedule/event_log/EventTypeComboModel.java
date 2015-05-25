/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.EventType;

/**
 *
 * @author ira
 */
public class EventTypeComboModel implements ComboBoxModel {

    private ArrayList<EventType> eventTypes;
    private Integer selectedItem;

    public EventTypeComboModel() {
        eventTypes = new ArrayList<EventType>();
        selectedItem = 0;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for (int e = 0; e < eventTypes.size(); e++) {
            if (eventTypes.get(e).getEventTypeId().equals(((EventType) anItem).getEventTypeId())) {
                selectedItem = e;
            }
        }
    }

    public void addEventType(EventType type) {
        eventTypes.add(type);
    }

    @Override
    public Object getSelectedItem() {
        try {
            return eventTypes.get(selectedItem);
        } catch (Exception exe) {
            return "";
        }
    }

    @Override
    public int getSize() {
        return eventTypes.size();
    }

    @Override
    public Object getElementAt(int index) {
        return eventTypes.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

}
