/**
 *  Filename:  User_Connected.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/08/2010
 *  Date Last Modified:  06/09/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains the User_Connected class, which is desgined
 *      to write to the control_DB to let a root user view who all is connected
 *      at any particular time
 */

//  package declaration 
package rmischedule.main;

//  import declarations

import java.util.logging.Level;
import java.util.logging.Logger;
import rmischedule.data_connection.Connection;
import rmischedule.security.User;
import rmischeduleserver.mysqlconnectivity.queries.connected_users.current_logged_in_query;


/**
 *  Class Name:  User_Connected
 *  Purpose of Class:  this class is desgined to write to the control_DB
 *      to let a root user view who all is connected at any particular time
 */
public class User_Connected extends Thread
{
    //  declaration of class variables
    private User currentUser;
    private Connection myConnection;
    
    //  private method implementations
    /**
     *  Method Name:  tellDbConnected
     *  Purpose of Method:  writes to the DB, so that a root user can view who
     *      is connected
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  all variables set, program needs to tell DB who is
     *      connected
     *  Postcondtions:  DB entry made
     */
    private void tellDbConnected()
    {
        //  convert userId to integer
        String userIdStr = currentUser.getUserId();
        Integer userId = new Integer(0);
        if (userIdStr.indexOf(":") > -1) {
            userId = -1 * new Integer(userIdStr.substring(userIdStr.indexOf(":") + 1));
        } else {
            userId = new Integer(currentUser.getUserId());
        }
        //  create query and execute
        current_logged_in_query query = new current_logged_in_query(userId);
        try
        {
            myConnection.prepQuery(query);
            myConnection.executeQuery(query);
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
    }
    
    //  public methods
    /**
     *  Method Name:  User_Connected
     *  Purpose of Method:  creates a default instance of this object
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, intial variables set
     */
    public User_Connected()
    {}

    /**
     *  Method Name:  User_Connected
     *  Purpose of Method:  creates an instance of this object using parameters
     *      to set class variables
     *  Arguments:  three strings containing the information to set userId,
     *      companyId, branchId
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial variables set
     */
    public User_Connected(User tempCurrentUser)
    {
        //  initialize class variables
        this.currentUser = tempCurrentUser;
        myConnection = new Connection();
    }

    /**
     *  Method Name:  run
     *  Purpose of Method:  runs this class in a separate thread
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  Object created, thread not yet started
     *  Postconditions:  this method runs as long as the program is active
     *  Overrides:  run from Thread
     */
    @Override
    public void run()
    {
        //  set thread priority
        setPriority(1);
        
        //  run while program is active
        while(true)
        {
            try
            {
                //  tell DB user is connected
                tellDbConnected();
                Thread.sleep(120000); // run every two minutes
            } catch (InterruptedException ex)
            {
                Logger.getLogger(User_Connected.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
};