/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components.availability;

import rmischedule.schedule.components.*;
import java.util.Vector;
import javax.swing.SwingUtilities;
import rmischedule.employee.data_components.EmployeeType;

/**
 *
 * @author user
 */
public abstract class GenericDisplayAvailabilityForShift {

    protected int numberOfDaysWorked = 0;

    public void setNumberOfDaysWorked(int numberOfDaysWorked) {
        this.numberOfDaysWorked = numberOfDaysWorked;
    }

    public int getNumberOfDaysWorked() {
        return this.numberOfDaysWorked;
    }

    public abstract Vector<SEmployee> getFullEmployeeList();

    public abstract Vector<SShift> getSShiftOfSelectedShifts();

    public abstract boolean showTrainedEmployeesOnly();

    public abstract boolean showAvailableEmloyeesOnly();

    public abstract int getSortType();

    public abstract Vector<EmployeeType> getEmployeeTypesToHide();

    public abstract Vector<CertificationClass> getCertificationsToHide();

    public abstract void setSelectedCertifications(Vector<CertificationClass> certs);

    public void refreshListOfEmployees() {
        Vector<SEmployee> myEmployees = getFullEmployeeList();
        Vector<SShift> myShifts = getSShiftOfSelectedShifts();
        Vector<EmployeeType> types = this.getEmployeeTypesToHide();
        Vector<CertificationClass> certs = this.getCertificationsToHide();
        for (int i = 0; i < myEmployees.size(); i++) {
            myEmployees.get(i).setShowBecauseOfAvailability(true);
            myEmployees.get(i).setShowByClientList(true);
            myEmployees.get(i).setShowBecauseOfClientTraining(true);
            myEmployees.get(i).setShowBecauseOfEmployeeType(true);
            myEmployees.get(i).setShowBecauseOfEmployeeCertifications(true);
            myEmployees.get(i).setShowBecauseOfDaysWorked(true);
            myEmployees.get(i).setShowBecauseOfPartTimeHours(true);

            //Shift selection hide or show
            if (myShifts.size() > 0) {
                for (int x = 0; x < myShifts.size(); x++) {
                    myEmployees.get(i).checkIfShowByClientList(myShifts.get(x));
                    try {
                        if (myShifts.get(x).myShift instanceof DShift) {
                            DShift currShift = (DShift)myShifts.get(x).myShift;
                            if (this.showAvailableEmloyeesOnly()) {
                                if (!myEmployees.get(i).isAvailable(currShift)) {
                                    myEmployees.get(i).setShowBecauseOfAvailability(false);
                                }
                            }
                            if (this.showTrainedEmployeesOnly()) {
                                if (!myEmployees.get(i).isTrainedForShift(currShift.getClient(), currShift.getDatabaseFormatDate())) {
                                    myEmployees.get(i).setShowBecauseOfClientTraining(false);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Hide or show stuff that does not rely on shift selection
            if (!myEmployees.get(i).checkIfShouldShowBecauseOfCertifications(certs)) {
                myEmployees.get(i).setShowBecauseOfEmployeeCertifications(false);
            }
            if (!myEmployees.get(i).checkIfShouldShowBecauseOfEmployeeType(types)) {
                myEmployees.get(i).setShowBecauseOfEmployeeType(false);
            }
            if (!myEmployees.get(i).checkIfShouldShowBecauseOfPartTimeHours()) {
                myEmployees.get(i).setShowBecauseOfPartTimeHours(false);
            }
            //If we should filter by days worked
            if (this.getNumberOfDaysWorked() != 0) {
                myEmployees.get(i).setShowBecauseOfDaysWorked(myEmployees.get(i).checkIfEmployeeHiredWithinLastXDays(this.getNumberOfDaysWorked())); 
            }
        }

        this.removeBannedUncertifiedEmployees();
    }

    /**
     * Marks anyone that is banned or uncertified as being hidden.
     */
    public void removeBannedUncertifiedEmployees() {
        Vector<SEmployee> myemps = getFullEmployeeList();
        Vector<SShift> myShifts = getSShiftOfSelectedShifts();
        for (int s = 0; s < myShifts.size(); s++) {
            SShift currShift = myShifts.get(s);
            for (int i = 0; i < myemps.size(); i++) {
                try {
                    if (currShift.getClient().isEmployeeBanned(myemps.get(i).getName())) {
                        myemps.get(i).setShowBecauseOfBanning(false);
                    }
                } catch (Exception e) {
                    myemps.get(i).setShowBecauseOfBanning(true);
                }
            }
        }
    }
}
