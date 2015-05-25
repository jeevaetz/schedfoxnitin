/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.client.components;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.ClientContractType;

/**
 *
 * @author user
 */
public class ClientContractTypeModel implements ComboBoxModel {

    private ArrayList<ClientContractType> clientContractTypes = new ArrayList<ClientContractType>();
    private int selectedIndex = -1;

    public int getSize() {
        return clientContractTypes.size();
    }

    public Object getElementAt(int index) {
        return clientContractTypes.get(index);
    }

    public void add(ClientContractType clientType) {
        clientContractTypes.add(clientType);
    }

    public void setSelectedItem(Object anItem) {
        if (anItem instanceof ClientContractType) {
            ClientContractType clientContractType = (ClientContractType)anItem;
            for (int c = 0; c < clientContractTypes.size(); c++) {
                if (clientContractType.getClientContractTypeId() == clientContractTypes.get(c).getClientContractTypeId()) {
                    this.selectedIndex = c;
                }
            }
        }
    }

    public Object getSelectedItem() {
        if (selectedIndex == -1) {
            return "Select a contract type";
        } else {
            return clientContractTypes.get(selectedIndex);
        }
    }

    public void addListDataListener(ListDataListener l) {

    }

    public void removeListDataListener(ListDataListener l) {

    }
}
