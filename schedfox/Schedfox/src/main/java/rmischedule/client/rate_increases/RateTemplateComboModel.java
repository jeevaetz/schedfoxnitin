/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.client.rate_increases;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import schedfoxlib.model.ClientRateTemplates;

/**
 *
 * @author user
 */
public class RateTemplateComboModel extends AbstractListModel implements ComboBoxModel{

    private ArrayList<ClientRateTemplates> clientRateTemplates;

    private int selectedIndex = -1;

    public RateTemplateComboModel() {
        clientRateTemplates = new ArrayList<ClientRateTemplates>();
    }

    public int getSize() {
        return clientRateTemplates.size();
    }

    public Object getElementAt(int index) {
        return clientRateTemplates.get(index);
    }

    public void add(ClientRateTemplates clientRateTemplates) {
        
        this.clientRateTemplates.add(clientRateTemplates);
        int origSize = this.clientRateTemplates.size();
        this.fireContentsChanged(this, 0, origSize);
    }

    public void clear() {
        this.clientRateTemplates.clear();
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    public void setSelectedItem(Object anItem) {
        for (int cr = 0; cr < clientRateTemplates.size(); cr++) {
            if (anItem instanceof String) {
                if (clientRateTemplates.get(cr).getTemplateName().equals(anItem.toString())) {
                    selectedIndex = cr;
                }
            } else if (anItem instanceof ClientRateTemplates) {
                if (clientRateTemplates.get(cr).equals(anItem)) {
                    selectedIndex = cr;
                }
            }
        }
    }

    public ClientRateTemplates getSelectedTemplate() {
        return (ClientRateTemplates)getSelectedItem();
    }

    public Object getSelectedItem() {
        if (selectedIndex < 0) {
            ClientRateTemplates clientRate = new ClientRateTemplates();
            clientRate.setClientRateTemplateId(-1);
            clientRate.setTemplateName("");
            return clientRate;
        } else {
            return this.clientRateTemplates.get(selectedIndex);
        }
    }
}
