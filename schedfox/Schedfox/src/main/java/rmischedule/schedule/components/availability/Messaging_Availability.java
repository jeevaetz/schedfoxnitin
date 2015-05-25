//  package
package rmischedule.schedule.components.availability;

import java.util.Vector;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.messaging.xMessagingEdit;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.CertificationClass;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.SShift;

public class Messaging_Availability extends GenericDisplayAvailabilityForShift
{

    private Schedule_View_Panel myViewPanel;
    private Vector<SEmployee> mySEmployeeVector;
    private boolean showTrainedEmployeesOnly;
    private boolean showAvailableEmployeesOnly;
    private Vector<EmployeeType> employeeTypesToHide;

    public Messaging_Availability()
    {
        myViewPanel = null;
        showTrainedEmployeesOnly = true;
        showAvailableEmployeesOnly = true;
        mySEmployeeVector = new Vector<SEmployee>();
        employeeTypesToHide = new Vector<EmployeeType>();
    }

    public Messaging_Availability(Schedule_View_Panel tempMyViewPanel)
    {
        this.myViewPanel = tempMyViewPanel;
        mySEmployeeVector = this.myViewPanel.getEmployeeList();
        employeeTypesToHide = new Vector<EmployeeType>();
        showTrainedEmployeesOnly = true;
        showAvailableEmployeesOnly = true;
    }

    public Schedule_View_Panel getViewPanel() {
        return this.myViewPanel;
    }

    public void setShowTrainedEmployeesOnly(boolean showTrainedEmployeesOnly) {
        this.showTrainedEmployeesOnly = showTrainedEmployeesOnly;
    }

    public void setAvailableEmployeesOnly(boolean val) {
        this.showAvailableEmployeesOnly = val;
    }

    public void setEmployeeTypeToShow(EmployeeType showType, Vector<EmployeeType> allTypes) {
        clearTypesToHide();
        for (int a = 0; a < allTypes.size(); a++) {
            if (allTypes.get(a).getEmployeeTypeId() != showType.getEmployeeTypeId() && showType.getEmployeeTypeId() != -1) {
                employeeTypesToHide.add(allTypes.get(a));
            }
        }
    }

    public void clearTypesToHide() {
        employeeTypesToHide = new Vector<EmployeeType>();
    }

    @Override
    public Vector<SEmployee> getFullEmployeeList()
    {
        return mySEmployeeVector;
    }

    @Override
    public Vector<EmployeeType> getEmployeeTypesToHide() {
        return employeeTypesToHide;
    }

    @Override
    public boolean showAvailableEmloyeesOnly() {
        return showAvailableEmployeesOnly;
    }

    @Override
    public Vector<CertificationClass> getCertificationsToHide() {
        return myViewPanel.getAcb().getUnSelectedCertificationClass();
    }

    @Override
    public Vector<SShift> getSShiftOfSelectedShifts()
    {
        Vector<SShift> mySShift = new Vector<SShift>();
        for (int s = 0; s < myViewPanel.getSelectedShifts().size(); s++)
            mySShift.add(myViewPanel.getSelectedShifts().get(s));


        return mySShift;
    }

    @Override
    public int getSortType()
    {
        return SEmployee.SORT_BY_AVAILABILITY;
    }

    @Override
    public boolean showTrainedEmployeesOnly() {
        return this.showTrainedEmployeesOnly;
    }

    @Override
    public void setSelectedCertifications(Vector<CertificationClass> certs) {
        
    }
};
