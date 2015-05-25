/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.equipment;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.EntityEquipment;

/**
 *
 * @author user
 */
public class EquipmentTableModel extends AbstractTableModel {

    private String companyId;
    private ArrayList<EntityEquipment> equipment = new ArrayList<EntityEquipment>();

    public void clear() {
        equipment = new ArrayList<EntityEquipment>();
        super.fireTableDataChanged();
    }
    
    public void setCompany(String companyId) {
        this.companyId = companyId;
    }

    public void addEquipment(EntityEquipment entityEquipment) {
        equipment.add(entityEquipment);
        super.fireTableDataChanged();
    }

    public int getRowCount() {
        return equipment.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            EntityEquipment selectedEquipment = equipment.get(rowIndex);
            if (columnIndex == 0) {
                return selectedEquipment.getEquipment(companyId).getEquipmentName();
            } else if (columnIndex == 1) {
                return selectedEquipment.getEntity(companyId).getName();
            } else if (columnIndex == 2) {
                return selectedEquipment.getId();
            } else if (columnIndex == 3) {
                return selectedEquipment.getDateIssued();
            }
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Equipment";
        } else if (columnIndex == 1) {
            return "Currently At";
        } else if (columnIndex == 2) {
            return "Identifier";
        } else {
            return "Date Issued";
        }
    }
}
