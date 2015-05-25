/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.components.availability;

import java.util.Vector;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.schedule.components.AvailabilityComboBox;
import rmischedule.schedule.components.CertificationClass;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.SShift;

/**
 *
 * @author user
 */
public class ScheduleDisplayAvailabilityForShift extends GenericDisplayAvailabilityForShift {

    public AvailabilityComboBox parent;

    public ScheduleDisplayAvailabilityForShift(AvailabilityComboBox availComboBox) {
        this.parent = availComboBox;
    }

    @Override
    public Vector<SEmployee> getFullEmployeeList() {
        return parent.getEmployeesFromScheduleView();
    }

    @Override
    public Vector<SShift> getSShiftOfSelectedShifts() {
        AvailabilityComboBox.mySShiftVector myShiftsToCheck = parent.getSelectedShifts();
        Vector<SShift> retVal = new Vector<SShift>();
        for (int s = 0; s < myShiftsToCheck.size(); s++) {
            retVal.add(myShiftsToCheck.get(s));
        }
        return retVal;
    }

    @Override
    public int getSortType() {
        return parent.getSortType();
    }

    @Override
    public boolean showAvailableEmloyeesOnly() {
        return parent.showAvailableEmloyeesOnly();
    }

    @Override
    public boolean showTrainedEmployeesOnly() {
        return parent.showTrainedEmployeesOnly();
    }

    @Override
    public Vector<EmployeeType> getEmployeeTypesToHide() {
        return parent.getUnSelectedEmployeeTypes();
    }

    @Override
    public Vector<CertificationClass> getCertificationsToHide() {
        return parent.getUnSelectedCertificationClass();
    }

    @Override
    public void setSelectedCertifications(Vector<CertificationClass> certs) {
        parent.setSelectedCertifications(certs);
    }

}
