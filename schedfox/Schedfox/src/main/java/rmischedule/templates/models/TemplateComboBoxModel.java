//  package declaration
package rmischedule.templates.models;

//  import declarations
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 * This class is a simple implementing class for a ComboBoxModel.
 *
 * <p>This class is designed to take an instance of
 * {@code LinkedHashMap<Integer, TemplateData>} It was written for the
 * TemplateSystem. <p><b>NOTE: </b>TemplateModel, also found in this package, is
 * a generic version utilizing {@code ArrayList<?>}. It has been left for legacy
 * systems that haven't been switched over yet. All future implementations
 * should use this object.
 *
 * @author Jeffrey N. Davis
 * @see rmischedule.templates.models.TemplateData
 * @since 03/07/2011
 */
public final class TemplateComboBoxModel extends DefaultComboBoxModel {
    /*  private variable declaration    */

    private Map<?, ?> dataMap;

    public TemplateComboBoxModel() {
    }

    public TemplateComboBoxModel(Map<?, ?> incomingMap) {
        this.dataMap = incomingMap;
    }

    public void setData(Map<?, ?> incomingMap) {
        this.dataMap = incomingMap;
    }
    
    @Override
    public int getSize() {
        return this.dataMap.size();
    }
    
    @Override
    public Object getElementAt(int idx) {
        Collection<?> collection = this.dataMap.values();
        Iterator<?> itr = collection.iterator();
        int count = 0;
        Object returnData = null;

        while (itr.hasNext()) {
            Object tempData = itr.next();
            if (count == idx) {
                returnData = tempData;
            }
            count++;
        }

        return returnData;
    }

    @Override
    public void setSelectedItem(Object o) {
        super.setSelectedItem(o);
    }

    @Override
    public void addListDataListener(ListDataListener ll) {
    }

    @Override
    public void removeListDataListener(ListDataListener ll) {
    }

    @Override
    public Object getSelectedItem() {
        return super.getSelectedItem();
    }
};
