/*  package declaration */
package rmischedule.reports.email.models;

/*  import declarations */
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.Company;

/**
 *  A simple object to extend and implement {@code DefaultComboBoxModel}
 *  @author Jeffrey N. Davis
 *  @see rmischedule.reports.email.controllers.EmailReportController
 *  @since 04/21/2011
 */
public class EmailReportCompanyComboBoxModel extends DefaultComboBoxModel
{
    /*  object variable declarations    */
    private final List<Company> companyList;
    
    /*  object instantiation code   */
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private EmailReportCompanyComboBoxModel()
    {   
        throw new UnsupportedOperationException( " Default instantiation of the object EmailReportCompanyComboBoxModel is not allowed." );
    }
    
    /**
     *  Instantiates this object by setting {@code this.companyList} to 
     *      {@code EmailReportController.companyList}.
     *  @param companyList an implementation of {@code List<Company}
     */
    public EmailReportCompanyComboBoxModel ( List<Company> incomingList )
    {
        this.companyList = incomingList;
    }
    
    
    @Override
    public void setSelectedItem ( Object o )    {  super.setSelectedItem ( o ); }

    @Override
    public int getSize() { return this.companyList.size();  }

    @Override
    public Object getSelectedItem() {  return super.getSelectedItem();  }

    @Override
    public Object getElementAt ( int idx )
    {
        Object returnObject = null;
        
        int counter = 0;
        for ( Object element: this.companyList )
        {
            if ( counter == idx )
                returnObject = this.companyList.get( idx );
            
            counter ++;
        }
        
        return returnObject;
    }
    
    @Override
    public void addListDataListener(ListDataListener ll) {}

    @Override
    public void removeListDataListener(ListDataListener ll) {}
};
