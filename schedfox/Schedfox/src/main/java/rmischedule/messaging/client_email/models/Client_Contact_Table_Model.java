//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations
import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *  This class defines an extension of {@code AbstractTableModel} in order
 *      to properly display {@code ClientContactData} within {@code Client_Email_AddressBook_Dialog}
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @see rmischedule.messaging.client_email.models.ClientContactData
 *  @see rmischedule.messaging.client_email.views.Client_Email_AddressBook_Dialog
 */
public class Client_Contact_Table_Model extends AbstractTableModel
{
    /*  object vaiable declarations */
    private Map<Integer, ClientContactData> contactMap;
    private String client;
    private JLabel primaryAlternate;

    /*  singleton instantiation code   */
    private static final Client_Contact_Table_Model INSTANCE = new Client_Contact_Table_Model();

    /**
     *  Default instantiation of this object
     */
    private Client_Contact_Table_Model() 
    {
        this.contactMap = new LinkedHashMap<Integer, ClientContactData>();
        this.client = "";
        this.primaryAlternate = new JLabel();
    }

    /**
     *  Return the one true instance of {@code Client_Contact_Table_Model}
     *  @return INSTANCE
     */
    public static Client_Contact_Table_Model getInstance()  { return INSTANCE; }

    /**
     *  Initializes this singleton instance
     *  @param clientMap a {@code Map<Integer, ClientContactData>} data structure to
     *      be display by this model
     */
    public void init ( Map<Integer, ClientContactData> contactMap, String client )
    {
        this.contactMap = contactMap;
        this.client = null;
        this.client = client;
    }

    public static MyCellRenderer getCellRenderer()  { return new MyCellRenderer(); }

    @Override
    public int getRowCount() {  return this.contactMap.size();  }

    @Override
    public int getColumnCount() {  return 6;  }

    @Override
    public Object getValueAt(int row, int column)
    {
        Collection<ClientContactData> collection = this.contactMap.values();
        int idx = 0;
        for ( ClientContactData element:  collection )
        {
            if ( idx == row )
            {
                if ( element.isIsContactPrimary() )
                {
                    this.primaryAlternate.setText( "PRIMARY" );
                    this.primaryAlternate.setForeground( Color.green.darker() );
                }
                else
                {
                    this.primaryAlternate.setText( "ALTERNATE" );
                    this.primaryAlternate.setForeground( Color.RED.darker() );
                }

                switch ( column )
                {
                    case 0:  return  this.client;
                    case 1:  return  element.getContactFirstName() + " " + element.getContactLastName();
                    case 2:  return  element.getContactEmailAddress();
                    case 3:  return  element.getContactPhone();
                    case 4:  return  element.getClientContactTitle();
                    case 5:  return  this.primaryAlternate;
                }
            }
            idx ++;
        }

        return null;
    }

    @Override
    public String getColumnName ( int idx )
    {
        StringBuilder columnName = new StringBuilder();

        switch ( idx )
        {
            case 0:  columnName.append( "Client" );
                break;
            case 1:  columnName.append( "Name" );
                break;
            case 2:  columnName.append( "Email Address" );
                break;
            case 3:  columnName.append( "Phone" );
                break;
            case 4:  columnName.append( "Contact Title" );
                break;
            case 5:  columnName.append( "Primary / Alternate" );
                break;
            
        }

        return columnName.toString();
    }

    /**
      * Class Name:  MyCellRenderer
      * Purpose of Class:  defines how each cell will be rendered within the table
      */
    public static class MyCellRenderer extends DefaultTableCellRenderer
    {
     @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column)
            {
                return ((JLabel) value);
            }
        }
};
