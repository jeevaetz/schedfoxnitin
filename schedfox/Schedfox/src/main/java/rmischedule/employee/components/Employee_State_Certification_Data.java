/*
 *  Filename:  Employee_State_Certification_Data.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/07/2010
 *  Date last modified:  05/11/2010
 *  Last modified by:  Jeffrey N. Davis
 *  Pupose of File:  File contains a simple class for data abstraction
 */

//  package declaration
package rmischedule.employee.components;

//  import declarations

/*
 *  Class Name:  EmployeeStateCertificationData
 *  Purpose of Class:  a simple class that contains the two pieces of data
 *      needed to determine employee's state certification
 */
public class Employee_State_Certification_Data implements Comparable
{
    //  private variable declarations
    private String state;
    private String stateAB;
    private int stateID;
    private boolean isCertified;


    @Override
    public String toString(){
       return this.state;
    }
    //  public methods
    /*
     *  Method Name:  Employee_State_Certification_Data
     *  Purpose of Method:  creates a default instance of
     *      Employee_State_Certification_Data
     *  Precondition:  object not initiated
     *  Postcondition:  object created, variables set
     */
    public Employee_State_Certification_Data()
    {
        setState("NULL");
        setStateAB("NULL");
        setStateID(0);
        setIsCertified(false);
    }
    /*
     *  Method Name:  Employee_State_Certification_Data
     *  Purpose of Method:  creates an instance of
     *      Employee_State_Certification_Data
     *  Precondition:  object not initiated
     *  Postcondition:  object created, variables set
     */
    public Employee_State_Certification_Data(String tempState,
            boolean tempIsCertified)
    {
        setState(tempState);
        setIsCertified(tempIsCertified);
    }

    //  getter methods
    public String getState()
    {
        return state;
    }

    public String getStateAB()
    {
        return stateAB;
    }

    public int getStateID()
    {
        return stateID;
    }

    public boolean getIsCertified()
    {
        return isCertified;
    }

    //  setter methods
    public void setState(String tempState)
    {
        state = tempState;
    }

    public void setStateAB(String tempStateAB)
    {
        stateAB = tempStateAB;
    }

    public void setStateID(int tempStateID)
    {
        stateID = tempStateID;
    }

    public void setIsCertified(boolean tempIsCertified)
    {
        isCertified = tempIsCertified;
    }

    public int compareTo(Object o)
    {
    //  variable declaration
    int returnValue = 0;
    if(state.compareTo(o.toString()) > 0)
        returnValue = 1;
    else if(state.compareTo(o.toString()) < 0)
        returnValue = -1;

    //  return comparison int
    return returnValue;
    }
};


