/**
 *  Filename:  Messaging_Filters_Data_Champion.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  07/06/2010
 *  Date Last Modified:  07/06/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  file contains a data component for all filters related
 *      to messaging, designed for Champion
 */

//  package declaration
package rmischedule.messaging.datacomponents;

import java.util.Vector;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.main.Main_Window;
import rmischedule.messaging.components.MessagingSubForm;
import rmischedule.schedule.components.DShift;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.SShift;
import rmischedule.schedule.components.availability.Messaging_Availability;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Classname:  Messaging_Filters_Data_Champion
 *  Purpose of Class:   a data component for all filters related to messaging,
 *      designed for Champion
 */
public class Messaging_Filters_Data_Champion
{
    //  private variable declarations
    private boolean isArmed;
    private boolean isTrained;
    private boolean isNewEmployee;
    private int filter;
    private boolean isArmedBoth;
    private boolean isTrainedBoth;
    private boolean isNewEmployeeBoth;
    private Vector<EmployeeType> armedUnarmedDBVector;
    private MessagingSubForm myParent;

    //  private method implementations
     /**
     *  Method Name:  determineOvertimeHit
     *  Purpose of Method:  determines if the selected shifts would force
     *      an employee into overtime, then sets the data component inside
     *      Employee_Messaging_List_Data accordingly
     *  Arguments:  Messaging_Availability instance
     *  Returns:  none
     *  Preconditions:  data known to calculate overtime, calculations not
     *      performed
     *  Postconditions:  calculations performed, data components set accordingly
     */
    private void determineEmployeeOvertimeHit(Messaging_Availability myAvailability,
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
        int count = 0;
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching SEmployee object from myAvailability
                Vector<SEmployee> sEmpVector = myAvailability.getFullEmployeeList();
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < sEmpVector.size() && !isFound)
                {
                    if(sEmpVector.get(j).getId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }

                //  sum overtime from selected shifts
                Vector<SShift> sShiftVector = myAvailability.getSShiftOfSelectedShifts();
                double totalHoursFromSShiftsVector = 0;
                for(int k = 0;k < sShiftVector.size();k ++)
                    totalHoursFromSShiftsVector += sShiftVector.get(k).myShift.getNoHoursDouble();

                //  determine total number of hours
                SEmployee tempSEmployee = sEmpVector.get(j);
                int weekId = sShiftVector.get(0).myShift.getWeekNo();
                double totalHours = tempSEmployee.getHoursWorkedForWeek(weekId) + totalHoursFromSShiftsVector;
                if(totalHours > 40.0)
                {
                    count ++;
                    employeeListVector.get(i).setIsOvertimeHit(true);
                }
                    else
                    employeeListVector.get(i).setIsOvertimeHit(false);
            }
        }
    }

    /**
     *  Method Name:  determineIsArmed
     *  Purpose of Method:  compares Employee_Messaging_List_Data to SEmpolyee
     *      and determines whether the employee in EMLD isArmed, then sets
     *      the boolean value accordingly
     *  Arguments:  an instance of Messaging_Availability
     *  Returns:  void
     *  Preconditions:  Messaging_Availability knows whether emp is armed,
     *      internal data strucuture does not
     *  Postconditions:  internal data structure updated with proper data
     */
    private void determineIsEmployeeArmed(Messaging_Availability myAvailability,
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching EmployeeType object from internal vector
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < armedUnarmedDBVector.size() && !isFound)
                {
                    
                    if(armedUnarmedDBVector.get(j).getEmployeeTypeId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }
            
                //  determine if armed
                if(j < armedUnarmedDBVector.size())
                {
                    boolean isArmed = false;
                    Integer armedUnarmedDBVectorEmpTypeId = new Integer(
                        armedUnarmedDBVector.get(j).getEmployeeType());
                    if(armedUnarmedDBVectorEmpTypeId == 1)
                        isArmed = true;
                    else if (armedUnarmedDBVectorEmpTypeId == 2)
                        isArmed = false;

                    //  set employeeListData vector
                    if(isArmed)
                        employeeListVector.get(i).setIsArmed(true);
                    else
                        employeeListVector.get(i).setIsArmed(false);
                }
            }
        }
    }

    /**
     *  Method Name:  determineIsEmployeeAvailable
     *  Purpose of Method:  compares Employee_Messaging_List_Data to SEmpolyee
     *      and determines whether the employee in EMLD isAvailable, then sets
     *      the boolean value accordingly
     *  Arguments:  an instance of Messaging_Availability, the employeeListVector
     *  Returns:  void
     *  Preconditions:  Messaging_Availability knows whether emp is available,
     *      internal data strucuture does not
     *  Postconditions:  internal data structure updated with proper data
     */
    private void determineIsEmployeeAvailable(Messaging_Availability myAvailability,
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
        //  determine availability
        int count = 0;
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching SEmployee object from myAvailability
                Vector<SEmployee> sEmpVector = myAvailability.getFullEmployeeList();
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < sEmpVector.size() && !isFound)
                {
                    if(sEmpVector.get(j).getId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }

                //  determine availability per SShift (via DShift)
                SEmployee tempSEmployee = sEmpVector.get(j);
                Vector<SShift> sShiftVector = myAvailability.getSShiftOfSelectedShifts();
                //  if k = x after loop, employee is available
                int k = sShiftVector.size();
                int x = 0;
                boolean flag = true;
                while(x < sShiftVector.size() && flag)
                {
                    if (sShiftVector.get(x).myShift instanceof DShift) {
                        DShift testDShift = (DShift)sShiftVector.get(x).myShift;
                        if(tempSEmployee.isAvailable(testDShift)) {
                            x ++;
                        } else {
                            flag = false;
                        }
                    }
                }

                if(k == x)
                {
                    count ++;
                    employeeListVector.get(i).setIsAvailable(true);
                }
                    else
                    employeeListVector.get(i).setIsAvailable(false);
             }
         }
   }

    /**
     *  Method Name:  determineIsEmployeeTrained
     *  Purpose of Method:  compares Employee_Messaging_List_Data to SEmpolyee
     *      and determines whether the employee in EMLD isTrained, then sets
     *      the boolean value accordingly
     *  Arguments:  an instance of Messaging_Availability, the employeeListVector
     *  Returns:  void
     *  Preconditions:  Messaging_Availability knows whether emp is trained,
     *      internal data strucuture does not
     *  Postconditions:  internal data structure updated with proper data
     */
    private void determineIsEmployeeTrained(Messaging_Availability myAvailability,
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
        //  determine isEmployeeTrained
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching SEmployee object from myAvailability
                Vector<SEmployee> sEmpVector = myAvailability.getFullEmployeeList();
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < sEmpVector.size() && !isFound)
                {
                    if(sEmpVector.get(j).getId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }

                //  determine if employee is trained
                SEmployee tempSEmployee = sEmpVector.get(j);
                if(tempSEmployee.isShowBecauseOfAvailability())
                    employeeListVector.get(i).setIsTrained(true);
                else
                    employeeListVector.get(i).setIsTrained(false);
            }
        }
    }

    /**
     *  Method Name:  determineIsEmployeeNew
     *  Purpose of Method:  compares Employee_Messaging_List_Data to SEmpolyee
     *      and determines whether the employee in EMLD isNew, then sets
     *      the boolean value accordingly
     *  Arguments:  an instance of Messaging_Availability, the employeeListVector
     *  Returns:  void
     *  Preconditions:  Messaging_Availability knows whether emp isNew,
     *      internal data strucuture does not
     *  Postconditions:  internal data structure updated with proper data
     */
    private void determineIsEmployeeNew(Messaging_Availability myAvailability,
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
        //  determine availability
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching SEmployee object from myAvailability
                Vector<SEmployee> sEmpVector = myAvailability.getFullEmployeeList();
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < sEmpVector.size() && !isFound)
                {
                    if(sEmpVector.get(j).getId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }

                //  determine if employee is new
                SEmployee tempSEmployee = sEmpVector.get(j);
                int numOfDays = Main_Window.parentOfApplication.getNumberOfDaysNewEmployee();
                if(tempSEmployee.checkIfEmployeeHiredWithinLastXDays(numOfDays))
                    employeeListVector.get(i).setIsNewEmployee(true);
                else
                    employeeListVector.get(i).setIsNewEmployee(false);
            }
        }
    }

    //  filter methods for returning a vector of employees that have
    //      been properly filtered
    //  filters:  canMessage && isAvailable && !isOvertimeHit &&
    //      !isArmed && !isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter0(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
        //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() && 
                !tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && !isArmed && !isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter1(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                !tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && isArmed && isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter2(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && isArmed && !isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter3(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && isArmed && !isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter4(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && isArmed && isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter5(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && !isArmed && isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter6(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                !tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && !isArmed && isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter7(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
           if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                !tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && !isOvertimeHit
    //      && !isArmed && !isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter8(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                !tempEMLD.getIsOvertimeHit() &&
                !tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && isArmed && isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter9(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && isArmed && !isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter10(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && isArmed && !isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter11(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && isArmed && isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter12(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && !isArmed && isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter13(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                !tempEMLD.getIsArmed() &&
                tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && !isArmed && isTrained && !isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter14(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                !tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                !tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  filters:  canMessage && isAvailable && (isOvertimeHit || !isOvertimeHit)
    //      && !isArmed && !isTrained && isNewEmployee
    private Vector<Employee_Messaging_List_Data> getVectorFilter15(
        Vector<Employee_Messaging_List_Data> employeeListVector)
    {
       //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  iterate through full vector; if employee passes all filters,
        //      add to new vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  get data object @ i
            Employee_Messaging_List_Data tempEMLD = employeeListVector.get(i);

            //  filter
            if(tempEMLD.getCanMessage() &&
                tempEMLD.getIsAvailable() &&
                (tempEMLD.getIsOvertimeHit() || !tempEMLD.getIsOvertimeHit()) &&
                !tempEMLD.getIsArmed() &&
                !tempEMLD.getIsTrained() &&
                tempEMLD.getIsNewEmployee())
                returnVector.add(tempEMLD);
        }

        //  return newly filtered vector
        return returnVector;
    }

    //  public method implementations
    /**
     *  Method Name:  Messaging_Filters_Data_Champion
     *  Purpose of Method:  creates a default instance of this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables intialized
     */
    public Messaging_Filters_Data_Champion(MessagingSubForm tempMyParent)
    {
        //  initialize class variables
        this.isArmed = false;
        this.isTrained = false;
        this.isNewEmployee = false;
        this.isArmedBoth = false;
        this.isTrainedBoth = false;
        this.isNewEmployeeBoth = false;
        this.filter = 0;
        armedUnarmedDBVector = new Vector<EmployeeType>();

        //  set parent
        this.myParent = tempMyParent;
    }

    /**
     *  Method Name:  Messaging_Filters_Data_Champion
     *  Purpose of Method:  creates an instance of this class, using parameters
     *      to define the value of class variables
     *  Arguments:  three booleans describing the states of the three class
     *      variables
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, class variables set to parameters
     */
    public Messaging_Filters_Data_Champion(boolean tempIsArmed, boolean
        tempIsTrained, boolean tempIsNewEmployee, boolean tempIsArmedBoth,
        boolean tempIsTrainedBoth, boolean tempIsNewEmployeeBoth,
        MessagingSubForm tempMyParent)
    {
        this.isArmed = tempIsArmed;
        this.isTrained = tempIsTrained;
        this.isNewEmployee = tempIsNewEmployee;
        this.isArmedBoth = tempIsArmedBoth;
        this.isTrainedBoth = tempIsTrainedBoth;
        this.isNewEmployeeBoth = tempIsNewEmployeeBoth;
        armedUnarmedDBVector = new Vector<EmployeeType>();

        //  set filter to default
        this.filter = 0;

        //  set parent
        this.myParent = tempMyParent;
    }

    /**
     *  Method Name:  resetData
     *  Purpose of Method:  resets all data fields to false
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  data set to unknown value, needs to be reset
     *  Postconditions:  all data reset to false
     */
    public void resetData()
    {
        //  reset data
        this.isArmed = false;
        this.isTrained = false;
        this.isNewEmployee = false;
        this.filter = 0;
        armedUnarmedDBVector.removeAllElements();
    }

    /**
     *  Method Name:  getCompletedSearch
     *  Purpose of Method:  method compiles are filter parameters, then
     *      creates a new vector of Employee_Messaging_List_Data for those
     *      employees that have filtered through, then returns it so they
     *      can be displayed
     *  Arguments:  a vector of Employee_Messaging_List_Data
     *  Returns:  a vector with all employees that can be messaged after
     *      filtering
     *  Preconditions:  user has selected filters, and wishes to see filtered
     *      list of employees
     *  Postconditions:  filters run, vector returned
     */
    public Vector<Employee_Messaging_List_Data> getCompletedSearch(
        Vector<Employee_Messaging_List_Data> fullVector,
        Messaging_Availability myAvailability)
    {
        //  declaration of vector to return
        Vector<Employee_Messaging_List_Data> returnVector = new Vector<
            Employee_Messaging_List_Data>();

        //  determine booleans
        determineEmployeeOvertimeHit(myAvailability, fullVector);
        determineIsEmployeeArmed(myAvailability, fullVector);
        determineIsEmployeeAvailable(myAvailability, fullVector);
        determineIsEmployeeTrained(myAvailability, fullVector);
        determineIsEmployeeNew(myAvailability, fullVector);

        //  call filter methods based on filter
        switch(filter)
        {
            case 0:  returnVector = getVectorFilter0(fullVector);
                break;
            case 1:  returnVector = getVectorFilter1(fullVector);
                break;
            case 2:  returnVector = getVectorFilter2(fullVector);
                break;
            case 3:  returnVector = getVectorFilter3(fullVector);
                break;
            case 4:  returnVector = getVectorFilter4(fullVector);
                break;
            case 5:  returnVector = getVectorFilter5(fullVector);
                break;
            case 6:  returnVector = getVectorFilter6(fullVector);
                break;
            case 7:  returnVector = getVectorFilter7(fullVector);
                break;
            case 8:  returnVector = getVectorFilter8(fullVector);
                break;
            case 9:  returnVector = getVectorFilter8(fullVector);
                break;
            case 10:  returnVector = getVectorFilter10(fullVector);
                break;
            case 11:  returnVector = getVectorFilter11(fullVector);
                break;
            case 12:  returnVector = getVectorFilter12(fullVector);
                break;
            case 13:  returnVector = getVectorFilter13(fullVector);
                break;
            case 14:  returnVector = getVectorFilter14(fullVector);
                break;
            case 15:  returnVector = getVectorFilter15(fullVector);
                break;
        }

        //  return vector of employees to be selected
        return returnVector;
    }

    /**
     *  Method Name:  determineFilter
     *  Purpose of Method:  takes in filter information, button selection
     *      and assigns an int to filter
     *  Arguments:  2 booleans detailing what buttons have been pressed
     *  Returns:  void
     *  Preconditions:  user has made selections regarding sort, exact sort
     *      needs to be determined
     *  Postconditions:  sort determined, int returned
     */
    public void determineFilter(boolean isLessThan40, boolean isAllAvailable)
    {
        //  test for two default cases
        if(isLessThan40 && !isAllAvailable && !isArmed && isTrained && !isNewEmployee)
            filter = 0;
        else if(!isLessThan40 && isAllAvailable && !isArmed && isTrained && !isNewEmployee)
            filter = 1;
        //  test for all variations of isLessThan40
        else if(isLessThan40 && !isAllAvailable && isArmed && isTrained && isNewEmployee)
            filter = 2;
        else if(isLessThan40 && !isAllAvailable && isArmed && !isTrained && isNewEmployee)
            filter = 3;
        else if(isLessThan40 && !isAllAvailable && isArmed && !isTrained && !isNewEmployee)
            filter = 4;
        else if(isLessThan40 && !isAllAvailable && isArmed && isTrained && !isNewEmployee)
            filter = 5;
        else if(isLessThan40 && !isAllAvailable && !isArmed && isTrained && isNewEmployee)
            filter = 6;
        else if(isLessThan40 && !isAllAvailable && !isArmed && !isTrained && !isNewEmployee)
            filter = 7;
        else if(isLessThan40 && !isAllAvailable && !isArmed && !isTrained && isNewEmployee)
            filter = 8;
        //  test for all variations of isAllAvailable
        else if(!isLessThan40 && isAllAvailable && isArmed && isTrained && isNewEmployee)
            filter = 9;
        else if(!isLessThan40 && isAllAvailable && isArmed && !isTrained && isNewEmployee)
            filter = 10;
        else if(!isLessThan40 && isAllAvailable && isArmed && !isTrained && !isNewEmployee)
            filter = 11;
        else if(!isLessThan40 && isAllAvailable && isArmed && isTrained && !isNewEmployee)
            filter = 12;
        else if(!isLessThan40 && isAllAvailable && !isArmed && isTrained && isNewEmployee)
            filter = 13;
        else if(!isLessThan40 && isAllAvailable && !isArmed && !isTrained && !isNewEmployee)
            filter = 14;
        else if(!isLessThan40 && isAllAvailable && !isArmed && !isTrained && isNewEmployee)
            filter = 15;
    }

    /**
     *  Method Name:  hitDBEmployeeArmed
     *  Purpose of Method:  method hits the DB with a query to get each
     *      employee's type, which determines if the employee is armed, then
     *      saves it into an internal Vector of "Employee Types"
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has selected appropriate filters to run on search,
     *      armed/unarmed not determined
     *  Postconditiosn:  internal vector loaded with information from DB
     *      regarding employee armed/unarmed.  Final determination of whether
     *      employee is armed/unarmed occurs in determineEmployeArmed
     */
    public void hitDBEmployeeArmed()
    {
        //  ensure vector is empty for new query
        armedUnarmedDBVector.removeAllElements();

        //  create, prep query, Record_Set
        employee_armed_unarmed_champion_availability_query query =
            new employee_armed_unarmed_champion_availability_query();
        myParent.getMyConnection().prepQuery(query);
        Record_Set rs = null;

        //  execute query
        try
        {
            rs = myParent.getMyConnection().executeQuery(query);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //  load internal data structure with information from db
        if(rs.length() != 0)
        {
            //  iterate through record set
            do
            {
                EmployeeType tempET = new EmployeeType();
                tempET.setEmployeeTypeId(rs.getInt("emp_id"));
                tempET.setEmployeeType(rs.getString("emp_type_id"));

                //  add data component to vector
                armedUnarmedDBVector.add(tempET);
            }
            while(rs.moveNext());
        }

        System.out.println("Size of armedUnarmedDBVector:  " + armedUnarmedDBVector.size());
    }

    //  getters and setters implementation
    public boolean isIsArmed()
    {
        return this.isArmed;
    }

    public boolean isIsTrained()
    {
        return this.isTrained;
    }

    public boolean isIsNewEmployee()
    {
        return this.isNewEmployee;
    }

    public boolean isIsArmedBoth()
    {
        return this.isArmedBoth;
    }

    public boolean isIsTrainedBoth()
    {
        return this.isTrainedBoth;
    }

    public boolean isIsNewEmployeeBoth()
    {
        return this.isNewEmployeeBoth;
    }

    public void setIsArmed(boolean tempIsArmed)
    {
        this.isArmed = tempIsArmed;
    }

    public void setIsTrained(boolean tempIsTrained)
    {
        this.isTrained = tempIsTrained;
    }

    public void setIsNewEmployee(boolean tempIsNewEmployee)
    {
        this.isNewEmployee = tempIsNewEmployee;
    }

    public void setIsArmedBoth(boolean tempIsArmedBoth)
    {
        this.isArmedBoth = tempIsArmedBoth;
    }

    public void setIsTrainedBoth(boolean tempIsTrainedBoth)
    {
        this.isTrainedBoth = tempIsTrainedBoth;
    }

    public void setIsNewEmployeeBoth(boolean tempIsNewEmployeeBoth)
    {
        this.isNewEmployeeBoth = tempIsNewEmployeeBoth;
    }

    /**
     *  Class Name:  employee_armed_unarmed_champion_availability_query
     *  Purpose of Class:  class contains a static query that is used only
     *      for champion DB, and thus is placed in this package and not
     *      split out into RMIScheduleServer
     */
    private class employee_armed_unarmed_champion_availability_query
            extends GeneralQueryFormat
    {
        //  declarations of class variables
        private static final String QUERY = "SELECT " +
            "employee_types_to_employee.employee_id as emp_id, " +
            "employee_types_to_employee.employee_type_id as emp_type_id " +
            "FROM employee_types_to_employee " +
            "INNER JOIN employee " +
            "ON employee_types_to_employee.employee_id = employee.employee_id " +
            "WHERE " +
            "(employee_types_to_employee.employee_type_id = 1 OR employee_types_to_employee.employee_type_id = 2) " +
            "AND employee.employee_is_deleted = 0;";

        /**
         *  Method Name:  employee_armed_unarmed_champion_availability_query
         *  Purpose of Method:  creates a an instance of this object
         *  Arguments:  none
         *  Returns:  void
         *  Preconditions:  object DNE
         *  Postconditions:  object exists
         */
        public employee_armed_unarmed_champion_availability_query()
        {}

        /**
        *  Method Name:  toString
        *  Purpose of Method:  returns the query in a string format
        *  Arguments:  none
        *  Returns:  a string containing the current query
        *  Precondition:  query has at least initial format, desired by another
        *      piece of code
        *  Postcondition:  query has been returned
        *  Overrides:  toString from GeneralQueryFormat
        */
        @Override
        public String toString()
        {
            //  return query
            return QUERY;
        }

        /**
        *  Method Name:  hasAccess
        *  Purpose of Method:  a method to check whether or not the object has
        *      access
        *  Arguments:  none
        *  Return value:  returns a boolean value describing whether this object
        *      has access
        *  Precondition:  access unknown by user
        *  Postcondition:  access known by user
        *  Overrides:  hasAccess from GeneralQueryFormat
        */
        @Override
        public boolean hasAccess()
        {
            return true;
        }
    }
};