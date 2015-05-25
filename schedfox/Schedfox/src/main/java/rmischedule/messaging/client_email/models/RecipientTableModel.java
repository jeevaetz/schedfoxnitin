/*  package declaration */
package rmischedule.messaging.client_email.models;

/*  import declarations */
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 *  A table model for the {@code jRecipientsTable} in {@code Confirm_Send_Client_Email_Dialog}.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @see rmischedule.messaging.client_email.views.Confirm_Send_Client_Email_Dialog
 */
public class RecipientTableModel extends AbstractTableModel {
    /*  object variable declaration */

    private ArrayList<Client_Email_Data> sendMap;

    public RecipientTableModel() {
        this.sendMap = new ArrayList<Client_Email_Data>();
    }

    /**
     *  Initializes this singleton instance
     *  @param sendMap a {@code Map<Integer, Client_Email_Data>} data structure
     *      to be displayed in this table model
     */
    public void init(Map<Integer, Client_Email_Data> argSendMap) {
        if (this.sendMap != null) {
            this.sendMap.clear();
        }
        Collection<Client_Email_Data> collection = argSendMap.values();
        for (Client_Email_Data element : collection) {
            this.sendMap.add(element);
        }
    }
    
    @Override
    public Class getColumnClass(int column) {
        if (column == 3) {
            return Boolean.class;
        }
        return String.class;
    }

    public Client_Email_Data getClientEmailAt(int row) {
        int idx = 0;
        Client_Email_Data retVal = null;
        for (Client_Email_Data element : sendMap) {
            if (idx == row) {
                retVal = element;
            }
            idx++;
        }
        return retVal;
    }

    @Override
    public int getRowCount() {
        return this.sendMap.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 3) {
            return true;
        }
        return false;
    }
    
    @Override
    public void setValueAt(Object value, int row, int column) {
        if (column == 3) {
            this.sendMap.get(row).setIsSelected((Boolean)value);
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int idx = 0;
        for (Client_Email_Data element : sendMap) {
            if (idx == rowIndex) {
                switch (columnIndex) {
                    case 0:
                        return element.getContactName();
                    case 1:
                        return element.getEmailAddress();
                    case 2:
                        return element.getClientName();
                    case 3:
                        return element.getIsSelected();
                }
            }
            idx++;
        }

        return null;
    }

    @Override
    public String getColumnName(int idx) {
        StringBuilder columnName = new StringBuilder();

        switch (idx) {
            case 0:
                columnName.append("Contact Name");
                break;
            case 1:
                columnName.append("Email Address");
                break;
            case 2:
                columnName.append("Client Name");
                break;
        }

        return columnName.toString();
    }
};
