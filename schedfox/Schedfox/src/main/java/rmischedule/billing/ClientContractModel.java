/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.billing;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.ClientContract;

/**
 *
 * @author user
 */
public class ClientContractModel extends AbstractTableModel {

    private ArrayList<ClientContract> clientContracts;
    private int companyId;
    private String compSchema;
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

    public ClientContractModel(int companyId, String compSchema) {
        clientContracts = new ArrayList<ClientContract>();
        this.companyId = companyId;
        this.compSchema = compSchema;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Contract Type";
        } else if (columnIndex == 1) {
            return "Start Date";
        } else if (columnIndex == 2) {
            return "End Date";
        } else if (columnIndex == 3) {
            return "Renewal Date";
        } else if (columnIndex == 4) {
            return "Projected Increase";
        } else if (columnIndex == 5) {
            return "Auto Renew?";
        } else if (columnIndex == 6) {
            return "File";
        }
        return "";
    }

    public ClientContract getClientContract(int row) {
        return clientContracts.get(row);
    }

    public void addClientContract(ClientContract clientContract) {
        clientContracts.add(clientContract);
        super.fireTableDataChanged();
    }

    public int getColumnCount() {
        return 7;
    }

    public void clearData() {
        clientContracts = new ArrayList();
        this.fireTableDataChanged();
    }

    public int getRowCount() {
        return clientContracts.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ClientContract clientContract = clientContracts.get(rowIndex);
        if (columnIndex == 0) {
            try {
                return clientContract.getClientContractType(companyId + "").getContractType();
            } catch (Exception e) {
                return "No Contract Type";
            }
        } else if (columnIndex == 1) {
            return myFormat.format(clientContract.getStartDate());
        } else if (columnIndex == 2) {
            return myFormat.format(clientContract.getEndDate());
        } else if (columnIndex == 3) {
            try {
                return myFormat.format(clientContract.getLastRenewed());
            } catch (Exception e) {
                return myFormat.format(clientContract.getStartDate());
            }
        } else if (columnIndex == 4) {
            return clientContract.getProjectedIncrease();
        } else if (columnIndex == 5) {
            return clientContract.getAutoRenew();
        } else if (columnIndex == 6) {
            return clientContract.getFileStringForContract(compSchema);
        }
        return "";
    }

    
}
