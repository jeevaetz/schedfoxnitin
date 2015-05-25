/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.client.rate_increases;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import rmischeduleserver.control.BranchController;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContract;
import schedfoxlib.model.User;

/**
 *
 * @author user
 */
public class RateIncreaseModel extends AbstractTableModel {

    private ArrayList<ClientContract> clientContracts;
    private int companyId;
    private String compSchema;
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
    private ArrayList<Boolean> isSelected = new ArrayList<Boolean>();
    private HashMap<Integer, Branch> branches = new HashMap<Integer, Branch>();
    private HashMap<Integer, User> dms = new HashMap<Integer, User>();

    public RateIncreaseModel(int companyId, String compSchema) {
        clientContracts = new ArrayList<ClientContract>();
        this.companyId = companyId;
        this.compSchema = compSchema;
        try {
            ArrayList<Branch> branches = BranchController.getInstance(this.companyId + "").getBranches();
            for (int b = 0; b < branches.size(); b++) {
                this.branches.put(branches.get(b).getBranchId(), branches.get(b));
            }
        } catch (Exception exe) {
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Client Name";
        } else if (columnIndex == 1) {
            return "Branch";
        } else if (columnIndex == 2) {
            return "DM";
        } else if (columnIndex == 3) {
            return "Contract Type";
        } else if (columnIndex == 4) {
            return "Last Renewal";
        } else if (columnIndex == 5) {
            return "Projected Increase";
        } else if (columnIndex == 6) {
            return "Auto Renew?";
        } else if (columnIndex == 7) {
            return "Select?";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex < 5) {
            return String.class;
        } else if (columnIndex == 5) {
            return BigDecimal.class;
        }
        return Boolean.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 7 || columnIndex == 5) {
            return true;
        }
        return false;
    }

    public ClientContract getClientContract(int row) {
        return clientContracts.get(row);
    }

    /**
     * Get all selected contract from the model, based off the selected column
     *
     * @return ArrayList<ClientContract>
     */
    public ArrayList<ClientContract> getSelectedContracts() {
        ArrayList<ClientContract> retVal = new ArrayList<ClientContract>();
        for (int s = 0; s < isSelected.size(); s++) {
            if (isSelected.get(s)) {
                retVal.add(clientContracts.get(s));
            }
        }
        return retVal;
    }

    public void addClientContract(ClientContract clientContract) {
        clientContracts.add(clientContract);
        isSelected.add(false);
        super.fireTableDataChanged();
    }

    public int getColumnCount() {
        return 8;
    }

    public void clearData() {
        clientContracts = new ArrayList();
        isSelected = new ArrayList<Boolean>();
        this.fireTableDataChanged();
    }

    public int getRowCount() {
        return clientContracts.size();
    }

    public void selectUnselect(boolean selected) {
        for (int s = 0; s < isSelected.size(); s++) {
            isSelected.set(s, selected);
        }
        super.fireTableDataChanged();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        ClientContract clientContract = clientContracts.get(rowIndex);
        if (columnIndex == 0) {
            return clientContract.getClient(this.companyId + "").getClientName();
        } else if (columnIndex == 1) {
            try {
                return branches.get(clientContract.getClient().getBranchId()).getBranchName();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 2) {
            try {
                return clientContract.getClient().getDmAssociatedWithAccount().getUserFullName();
            } catch (Exception exe) {
                return "";
            }
        } else if (columnIndex
                == 3) {
            try {
                if (clientContract.getClientContractType(companyId + "").getContractType().length() == 0) {
                    throw new Exception();
                }
                return clientContract.getClientContractType(companyId + "").getContractType();
            } catch (Exception e) {
                return "No Contract Type";
            }
        } else if (columnIndex
                == 4) {
            try {
                return myFormat.format(clientContract.getLastRenewed());
            } catch (Exception e) {
                return "No Renewal Date";
            }
        } else if (columnIndex
                == 5) {
            return clientContract.getProjectedIncrease();
        } else if (columnIndex
                == 6) {
            return clientContract.getAutoRenew();
        } else if (columnIndex
                == 7) {
            return isSelected.get(rowIndex);
        }

        return "";
    }

    @Override
    public void setValueAt(Object obj, int rowIndex, int columnIndex) {
        ClientContract clientContract = clientContracts.get(rowIndex);
        if (columnIndex == 5) {
            try {
                clientContract.setProjectedIncrease(new BigDecimal(Integer.parseInt(obj.toString())));
            } catch (Exception e) {
            }
        } else if (columnIndex == 7) {
            isSelected.set(rowIndex, !isSelected.get(rowIndex));
        }
    }
}
