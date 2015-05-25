//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations
import java.util.Collection;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *  This class is a simple implementing class for a {@code ComboBoxModel}
 *
 * 
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.views.Client_Email_AddressBook_Dialog
 *  @since 03/29/2011
 */
public class Client_Email_AddressBook_ComboBoxModel extends DefaultComboBoxModel
{
    /*  object variable declarations    */
    private final Map<Integer, ?> dataMap;

    /*  object construction code    */
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private Client_Email_AddressBook_ComboBoxModel()
    {
        throw new UnsupportedOperationException( " Default instantiation of the object Client_Email_AddressBook_ComboBoxModel is not allowed." );
    }

    public Client_Email_AddressBook_ComboBoxModel ( Map<Integer, ?> dataMap )
    {
        this.dataMap = dataMap;
    }

    @Override
    public void setSelectedItem ( Object o )    {  super.setSelectedItem ( o ); }

    @Override
    public int getSize() { return this.dataMap.size();  }

    @Override
    public Object getSelectedItem() {  return super.getSelectedItem();  }

    @Override
    public Object getElementAt ( int i )
    {
        Collection<?> collection = this.dataMap.values();
        Object returnObject = null;
        int idx = 0;
        for ( Object element:  collection )
        {
            if ( idx == i )
                returnObject = element;
            idx ++;
        }

        return returnObject;
    }

    @Override
    public void addListDataListener(ListDataListener ll) {}

    @Override
    public void removeListDataListener(ListDataListener ll) {}
};
