/*  package declaration */
package rmischedule.utility;

/*  import declarations */
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import rmischedule.components.graphicalcomponents.GraphicalListComponent;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.schedule.components.SEmployee;
import rmischedule.security.User;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.EmailContact;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.employee.get_employees_worked_at_client_query;

/**
 *  This is a hack utility class because our managers won't give us the time
 *      to do things properly.  This is a volatile class, as it can easily be broken.
 *  <p><b>NOTE:  </b>the author of this class hates this class.
 *  @author Jeffrey N. Davis
 *  @since 05/06/2011
 */
public class FilterUtil 
{
    /*  A list of hacks!  These must change if code is changed  */
    private static final String MISTY_ZAVADIL_USER_ID = "2496";
    private static final String JEFFREY_DAVIS_USER_ID = "2284";  // user for developmental purposes only
    private static final String NORTH_HOUSTON_BRANCH_ID = "305";
    private static final String CHAMPION_DB_COMPANY_ID = "2";
    /*  the northwest houston hospital technical has 13 client ids   */
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_0 = 2634;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_1 = 2638;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_2 = 2764;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_3 = 2626;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_4 = 2636;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_5 = 2747;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_6 = 2622;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_7 = 2765;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_8 = 2630;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_9 = 3202;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_10 = 3189;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_11 = 2894;
    private static final int HOUSTON_NW_HOSPITAL_CLIENT_ID_12 = 3061;
    
    private static List<EmailContact> parseNWRecordSet ( Record_Set rs )
    {
        List<EmailContact> returnList = new LinkedList<EmailContact>();
        
        for ( int idx = 0; idx < rs.length(); idx ++)
        {
            returnList.add(new Employee(new Date(), rs));
            rs.moveNext();
        }
        
        return returnList;
    }
    
    private static void combineNWLists ( List<EmailContact> combinedList, List<EmailContact> listToCombine )
    {
        for ( EmailContact element:  listToCombine )
        {
            if ( !combinedList.contains(element) )
                combinedList.add(element);
        }
    }
    
    private static boolean doesNWCombinedListContain ( SEmployee emp, List<EmailContact> combinedList )
    {
        boolean flag = false;
        
        for ( EmailContact element:  combinedList )
        {
            if ( emp.getId() == element.getPrimaryId() )
                flag = true;
        }
        
        return flag;
    }
    
    public FilterUtil() {}
    
    /**
     *  Method filters out guards that are primarily assigned to Houston NorthWest hospital
     *  <p><b>NOTE:  </b>guards will be filtered out unless {@code User = Misty Zapadil}
     *  <p><b>NOTE:  </b>this method is a request because our dispatchers are lazy 
     *      worthless morons.  Instead of using their brains to deselect 3 people
     *      and NOT message them at night, we are instead going to attempt to filter them out.
     */
    public static void filterNorthHoustonNorthWestHospitalSMS( List<GraphicalListComponent> list )
    {
        /*  if statement ensures that Misty Zavadil is still able to access employees   */
        User user = Main_Window.parentOfApplication.myUser;
        if ( !user.getUserId().equalsIgnoreCase( MISTY_ZAVADIL_USER_ID  ))
        {
            get_employees_worked_at_client_query client0Query =
                new get_employees_worked_at_client_query();
            client0Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_0 );
            get_employees_worked_at_client_query client1Query =
                new get_employees_worked_at_client_query();
            client1Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_1 );
            get_employees_worked_at_client_query client2Query =
                new get_employees_worked_at_client_query();
            client2Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_2 );
            get_employees_worked_at_client_query client3Query =
                new get_employees_worked_at_client_query();
            client3Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_3 );
            get_employees_worked_at_client_query client4Query =
                new get_employees_worked_at_client_query();
            client4Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_4 );
            get_employees_worked_at_client_query client5Query =
                new get_employees_worked_at_client_query();
            client5Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_5 );
            get_employees_worked_at_client_query client6Query =
                new get_employees_worked_at_client_query();
            client6Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_6 );
            get_employees_worked_at_client_query client7Query =
                new get_employees_worked_at_client_query();
            client7Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_7 );
            get_employees_worked_at_client_query client8Query =
                new get_employees_worked_at_client_query();
            client8Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_8 );
            get_employees_worked_at_client_query client9Query =
                new get_employees_worked_at_client_query();
            client9Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_9 );
            get_employees_worked_at_client_query client10Query =
                new get_employees_worked_at_client_query();
            client10Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_10 );
            get_employees_worked_at_client_query client11Query =
                new get_employees_worked_at_client_query();
            client11Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_11 );
            get_employees_worked_at_client_query client12Query =
                new get_employees_worked_at_client_query();
            client12Query.setPreparedStatement( HOUSTON_NW_HOSPITAL_CLIENT_ID_12 );
            
            Connection connection = new Connection();
            connection.setCompany(CHAMPION_DB_COMPANY_ID);
            connection.setBranch(NORTH_HOUSTON_BRANCH_ID);
            
            Record_Set rst0 = null;
            Record_Set rst1 = null;
            Record_Set rst2 = null;
            Record_Set rst3 = null;
            Record_Set rst4 = null;
            Record_Set rst5 = null;
            Record_Set rst6 = null;
            Record_Set rst7 = null;
            Record_Set rst8 = null;
            Record_Set rst9 = null;
            Record_Set rst10 = null;
            Record_Set rst11 = null;
            Record_Set rst12 = null;
            
            try
            {
                rst0 = connection.executeQuery ( client0Query );
                rst1 = connection.executeQuery ( client1Query );
                rst2 = connection.executeQuery ( client2Query );
                rst3 = connection.executeQuery ( client3Query );
                rst4 = connection.executeQuery ( client4Query );
                rst5 = connection.executeQuery ( client5Query );
                rst6 = connection.executeQuery ( client6Query );
                rst7 = connection.executeQuery ( client7Query );
                rst8 = connection.executeQuery ( client8Query );
                rst9 = connection.executeQuery ( client9Query );
                rst10 = connection.executeQuery ( client10Query );
                rst11 = connection.executeQuery ( client11Query );
                rst12 = connection.executeQuery ( client12Query );
            }
            catch ( Exception ex )
            {
                System.out.println( "An error occured retrieving the client schedules for employees.");
            }
            
            List<EmailContact> list0 = FilterUtil.parseNWRecordSet ( rst0 );
            List<EmailContact> list1 = FilterUtil.parseNWRecordSet ( rst1 );
            List<EmailContact> list2 = FilterUtil.parseNWRecordSet ( rst2 );
            List<EmailContact> list3 = FilterUtil.parseNWRecordSet ( rst3 );
            List<EmailContact> list4 = FilterUtil.parseNWRecordSet ( rst4 );
            List<EmailContact> list5 = FilterUtil.parseNWRecordSet ( rst5 );
            List<EmailContact> list6 = FilterUtil.parseNWRecordSet ( rst6 );
            List<EmailContact> list7 = FilterUtil.parseNWRecordSet ( rst7 );
            List<EmailContact> list8 = FilterUtil.parseNWRecordSet ( rst8 );
            List<EmailContact> list9 = FilterUtil.parseNWRecordSet ( rst9 );
            List<EmailContact> list10 = FilterUtil.parseNWRecordSet ( rst10 );
            List<EmailContact> list11 = FilterUtil.parseNWRecordSet ( rst11 );
            List<EmailContact> list12 = FilterUtil.parseNWRecordSet ( rst12 );
            
//            System.out.println( "List 0 size:  " + list0.size() );
//            System.out.println( "List 1 size:  " + list1.size() );
//            System.out.println( "List 2 size:  " + list2.size() );
//            System.out.println( "List 3 size:  " + list3.size() );
//            System.out.println( "List 4 size:  " + list4.size() );
//            System.out.println( "List 5 size:  " + list5.size() );
//            System.out.println( "List 6 size:  " + list6.size() );
//            System.out.println( "List 7 size:  " + list7.size() );
//            System.out.println( "List 8 size:  " + list8.size() );
//            System.out.println( "List 9 size:  " + list9.size() );
//            System.out.println( "List 10 size:  " + list10.size() );
//            System.out.println( "List 11 size:  " + list11.size() );
//            System.out.println( "List 12 size:  " + list12.size() );
            
            /*  combine all lists */
            List<EmailContact> combinedList = new LinkedList<EmailContact>();
            for ( EmailContact element:  list0 )
                combinedList.add(element);
            FilterUtil.combineNWLists(combinedList, list1);
            FilterUtil.combineNWLists(combinedList, list2);
            FilterUtil.combineNWLists(combinedList, list3);
            FilterUtil.combineNWLists(combinedList, list4);
            FilterUtil.combineNWLists(combinedList, list5);
            FilterUtil.combineNWLists(combinedList, list6);
            FilterUtil.combineNWLists(combinedList, list7);
            FilterUtil.combineNWLists(combinedList, list8);
            FilterUtil.combineNWLists(combinedList, list9);
            FilterUtil.combineNWLists(combinedList, list10);
            FilterUtil.combineNWLists(combinedList, list11);
            FilterUtil.combineNWLists(combinedList, list12);
            
            //System.out.println("Size of combined list:  " + combinedList.size());
            
            int count = 0;
            for ( GraphicalListComponent element:  list )
            {
                SEmployee employee = ( SEmployee ) element.getObject();
                if ( FilterUtil.doesNWCombinedListContain(employee, combinedList))
                {
                    count ++;
                    element.setVisible(false);
                }
            }
            
           // System.out.println( "Count:  " + count );
        }
    }
}
