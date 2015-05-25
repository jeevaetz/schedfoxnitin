/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.equipment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.EquipmentController;
import schedfoxlib.model.EmployeeEquipment;

/**
 *
 * @author user
 */
public class EmployeeEquipmentModel extends AbstractTableModel {

    private ArrayList<EmployeeEquipment> employeeEquipment;
    private ArrayList<Boolean> selectedEmails;
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");

    private EquipmentController controller;
    private EquipmentRefreshWindow window;

    private String companyId;

    public EmployeeEquipmentModel(EquipmentRefreshWindow window) {
        employeeEquipment = new ArrayList<EmployeeEquipment>();
        selectedEmails = new ArrayList<Boolean>();
        this.window = window;
    }
    
    public void toggleSelected(boolean selected) {
        for (int s = 0; s < selectedEmails.size(); s++) {
            selectedEmails.set(s, selected);
        }
        super.fireTableDataChanged();
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setEquipmentController(EquipmentController controller) {
        this.controller = controller;
    }

    public ArrayList<EmployeeEquipment> getEquipmentToBeEmailed() {
        ArrayList<EmployeeEquipment> retVal = new ArrayList<EmployeeEquipment>();
        for (int s = 0; s < this.selectedEmails.size(); s++) {
            if (selectedEmails.get(s)) {
                retVal.add(this.getEmployeeEquipmentAt(s));
            }
        }
        return retVal;
    }
    
    public EquipmentController getEquipmentController() {
        return this.controller;
    }

    public void addEquipment(EmployeeEquipment empEquip) {
        employeeEquipment.add(empEquip);
        selectedEmails.add(false);
        this.fireTableDataChanged();
    }

    public EmployeeEquipment getEmployeeEquipmentAt(int row) {
        return this.employeeEquipment.get(row);
    }

    public void clearEquipment() {
        employeeEquipment.clear();
        selectedEmails.clear();
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.employeeEquipment.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String retVal = "";
        if (columnIndex == 0) {
            return "Branch";
        } else if (columnIndex == 1) {
            return "Employee Name";
        } else if (columnIndex == 2) {
            return "Term Date";
        } else if (columnIndex == 3) {
            return "Phone";
        } else if (columnIndex == 4) {
            return "Balance";
        } else if (columnIndex == 5) {
            return "Returned On";
        } else if (columnIndex == 6) {
            return "Returned?";
        } else if (columnIndex == 7) {
            return "Remove?";
        } else if (columnIndex == 8) {
            return "Email?";
        }
        return retVal;
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        Class retVal = String.class;
        if (columnIndex == 6 || columnIndex == 7 || columnIndex == 8) {
            retVal = Boolean.class;
        } else if (columnIndex == 4) {
            retVal = BigDecimal.class;
        }
        return retVal;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value instanceof Boolean) {
            Boolean val = (Boolean) value;
            if (columnIndex == 6 && val.booleanValue()) {
                window.displayEquipmentReturnWindow(employeeEquipment.get(rowIndex),
                        controller.getCompanyId());
                window.refreshEquipmentData();
            } else if (columnIndex == 6 && !val.booleanValue()) {
                int result = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication,
                        "Mark the equipment as NOT returned?", "Mark NOT Returned?",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        controller.clearReturnEmployeeEquipment(employeeEquipment.get(rowIndex));
                        window.refreshEquipmentData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (columnIndex == 7 && val.booleanValue()) {
                int result = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, 
                        "Mark the equipment as forgiven and no longer require it to be returned?", "Mark Forgiven?",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        controller.waiveEmployeeEquipmentReturn(employeeEquipment.get(rowIndex), Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                        window.refreshEquipmentData();
                    } catch (Exception exe) {}
                }
            } else if (columnIndex == 8) {
                selectedEmails.set(rowIndex, val);
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 6 || columnIndex == 7 || columnIndex == 8) {
            return true;
        }
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EmployeeEquipment employeeEquipmentObj = employeeEquipment.get(rowIndex);
        String cellPhone = employeeEquipmentObj.getEmployee(companyId).getEmployeePhone();
        if (cellPhone == null || cellPhone.equals("")) {
            cellPhone = employeeEquipmentObj.getEmployee(companyId).getEmployeeCell();
        }
        if (cellPhone == null || cellPhone.equals("")) {
            cellPhone = employeeEquipmentObj.getEmployee(companyId).getEmployeePhone2();
        }
        if (columnIndex == 0) {
            return this.window.getBranches().get(employeeEquipmentObj.getEmployee(companyId).getBranchId()).getBranchName();
        } else if (columnIndex == 1) {
            return employeeEquipmentObj.getEmployee(companyId).getEmployeeLastName() + ", "
                    + employeeEquipmentObj.getEmployee(companyId).getEmployeeFirstName();
        } else if (columnIndex == 2) {
            try {
                return myFormat.format(employeeEquipmentObj.getEmployee(companyId).getEmployeeTermDate());
            } catch (Exception exe) {
                return "";
            }
        } else if (columnIndex == 3) {
            return cellPhone;
        } else if (columnIndex == 4) {
            return employeeEquipmentObj.getDeduction().getBalance();
        } else if (columnIndex == 5) {
            try {
                return myFormat.format(employeeEquipmentObj.getDateReturned());
            } catch (Exception e) {
                return "";
            }
        } else if (columnIndex == 6) {
            try {
                return !(employeeEquipmentObj.getDateReturned() == null);
            } catch (Exception exe) {
                return "";
            }
        } else if (columnIndex == 7) {
            return false;
        } else if (columnIndex == 8) {
            return this.selectedEmails.get(rowIndex);
        }
        return "";
    }

}
