/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.Client;

/**
 *
 * @author ira
 */
public class ClientTableModel extends AbstractTableModel {

    private ArrayList<Client> clients;
    private EventLoggerMainPanel parent;

    public ClientTableModel() {
        clients = new ArrayList<Client>();    
    }
    
    public void setMyParent(EventLoggerMainPanel parent) {
        this.parent = parent;
    }

    public Client getClientAt(Integer row) {
        return clients.get(row);
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        super.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return clients.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Client";
        } else if (column == 1) {
            return "Branch";
        } else if (column == 2) {
            return "Address";
        }
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Client currClient = clients.get(rowIndex);
        if (columnIndex == 0) {
            return currClient.getClientName();
        } else if (columnIndex == 1) {
            return this.parent.getCachedBranches().get(currClient.getBranchId()).getBranchName();
        } else if (columnIndex == 2) {
            return currClient.getAddress1() + " " + currClient.getCity() + ", " + currClient.getState() + " " + currClient.getZip();
        }
        return "";
    }
}
