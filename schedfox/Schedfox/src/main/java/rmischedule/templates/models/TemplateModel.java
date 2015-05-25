//  package declaration
package rmischedule.templates.models;

//  import declarations
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *  This class is a simple implementing class for a ComboBoxModel.
 *
 *  <p>This class is designed initially to take an <code>ArrayList<?></code>.
 *      It was written with <code>EditTemplateDiag.jComboBox</code> in mind.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.client.components.EditTemplateDiag
 *  @since 02/08/2011
 */
public final class TemplateModel extends DefaultComboBoxModel
{
    //  private variable decaration
    private final ArrayList<?> dataList;

    /**
     *  Default instantiation not allowed
     *  @throws UnsupportedOperationException
     */
    private TemplateModel()
    {
        throw new UnsupportedOperationException( "Default instantiation not allowed" );
    }

    public TemplateModel ( ArrayList<?> dataList)   {   this.dataList = dataList; }


    @Override
    public void setSelectedItem(Object o) {
        super.setSelectedItem(o);
    }

    @Override
    public int getSize() {  return this.dataList.size(); }

    @Override
    public Object getElementAt(int i) { return this.dataList.get(i); }

    @Override
    public void addListDataListener(ListDataListener ll) {}

    @Override
    public void removeListDataListener(ListDataListener ll) {}

    @Override
    public Object getSelectedItem() {
        return super.getSelectedItem();
    }
};
