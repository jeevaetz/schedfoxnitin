/**
 *  Filename:  Send_New_User_Alerts.java
 *  Created by:  Jeffrey N. Davis
 *  Date Created:  08/06/2010
 *  Purpose of File:  File contains a class designed to send out both email
 *      and text alerts whenever a new user has logged in to test out
 *      SchedFox.  Class is called only in NewUserScreen.
 *  Modification Information:
 */
//  package declaration
package rmischedule.admin.newuser_alert;

//  import declarations
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import rmischedule.data_connection.Connection;
import rmischedule.messaging.email.SchedfoxEmail;
import schedfoxlib.sms.SmsSender;
import rmischedule.new_user.NewCompany;
import rmischedule.new_user.NewUser;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.new_user_alert.get_users_to_alert_query;

/**
 *  Class Name:  Send_New_User_Alerts
 *  Purpose of Class:  a class designed to send out both email and text
 *      alerts whenever a new user has logged in to test out SchedFox.
 *      Class is called only in NewUserScreen.
 */
public class Send_New_User_Alerts {
    //  private variable declarations

    Vector<NewUser_Alert_Data> dataVector;
    private boolean hasUsersToAlert;
    Connection myConnection;
    StringBuffer fullName;
    StringBuffer emailAddress;
    StringBuffer companyName;
    StringBuffer companyWebSite;
    StringBuffer companyPhone;
    StringBuffer timeDate;
    private static final String DATE_FORMAT_NOW = "HH:mm MM/dd/yyyy";
    private static final String EMAIL_SUBJECT = "SchedFox trial has a new user";
    private static final String PROGRAM_NAME = "SchedFox";

    //  private method implementations
    /**
     *  Method Name:  loadDataVector
     *  Purpose of Method:  hits the DB and loads into the class data structure
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object initialized, data needs to be retrieved from
     *      DB
     *  Postconditions:  data retrieved from DB, loaded into data structure
     */
    private void loadDataVector() {
        //  create Record_Set, create/prep query
        Record_Set rs = new Record_Set();
        get_users_to_alert_query query = new get_users_to_alert_query();
        myConnection.prepQuery(query);

        //  execute query
        try {
            rs = myConnection.executeQuery(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //  load data
        if (rs.length() > 0) {
            try {
                //  iterate through rs, loading data into data structure
                do {
                    //  declaration of data object; object will be returned
                    NewUser_Alert_Data data = new NewUser_Alert_Data();

                    //  load data into data object
                    data.setSSN(rs.getInt("user_ssn"));
                    data.setFirstName(rs.getString("user_first_name"));
                    data.setLastName(rs.getString("user_last_name"));
                    data.setCompany(rs.getString(("user_company")));
                    data.setPrimaryEmail(rs.getString("user_primary_email"));
                    data.setAlternateEmail(rs.getString("user_alternate_email"));
                    data.setTextNumber(rs.getString("user_text_number"));
                    data.setIsSendText(rs.getBoolean("user_send_text"));
                    data.setUseAlternateEmail(rs.getBoolean("user_use_alternate_email"));
                    data.setUseBothEmail(rs.getBoolean("user_use_both_email"));

                    //  add data object to vector
                    dataVector.add(data);
                } while (rs.moveNext());

                //  if complete, set safety check to true
                hasUsersToAlert = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *  Method Name:  formatName
     *  Purpose of Method:  formats the name from the user object
     *  Arguments:  a string containing the first name, a string containing
     *      the last name
     *  Returns:  void
     *  Preconditions:  name known by user object, not known by this
     *  Postconditions:  name formatted, assigned inside this object
     */
    private void formatName(String firstName, String lastName) {
        // format name, ensure no duplicates
        fullName.append(firstName);
        fullName.append(" ");
        fullName.append(lastName);
    }

    /**
     *  Method Name:  formatTimeDate
     *  Purpose of Method:  formats the current time/date, and places it within
     *      the previously declared stringbuffer
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  current time unknown
     *  Postconditions:  current time known, formatted into string
     */
    private void formatTimeDate() {
        //  get current unformatted time, date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);

        //  reset, append current time
        timeDate.append(simpleDateFormat.format(calendar.getTime()));
    }

    /**
     *  Method Name:  constructSMSMessage
     *  Purpose of Method:  constructs the message to send out via text, then
     *      returns it as a string
     *  Arguments:  an instance of NewUser_Alert_Data that contains all
     *      information needed constructing the message
     *  Returns:  a string containing the properly formatted message
     *  Preconditions:  text message about to be sent, message must be formatted
     *  Postconditions:  message formatted, returned
     */
    private String constructSMSMessage(NewUser_Alert_Data data) {
        //  declaration of message to return
        StringBuffer message = new StringBuffer();

        //  append message
        message.append("A new user has logged onto the trial version of ");
        message.append(PROGRAM_NAME);
        message.append(".  Please check ");
        message.append(data.getPrimaryEmail());
        if (data.isUseAlternateEmail()) {
            message.append(" or ");
            message.append(data.getAlternateEmail());
        }
        message.append(" for more information.");

        //  return formatted message
        return message.toString();
    }

    /**
     *  Method Name:  constructEmailMessage
     *  Purpose of Method:  constructs the message to send out via email, then
     *      returns it as a string
     *  Arguments:  none
     *  Returns:  a string containing the properly formatted message
     *  Preconditions:  text message about to be sent, message must be formatted
     *  Postconditions:  message formatted, returned
     */
    private String constructEmailMessage() {
        //  declaration of message to return
        StringBuffer message = new StringBuffer();

        message.append("<html> <body><h1 align = \"center\">A new user has logged in to the trial version of " + PROGRAM_NAME + " Schedfox.  "
                + "Their information is below:</h1><table border = 7><th>Time Created</th><th>Full Name</th><th>Email</th>"
                + "<th>Company Name</th><th>Company Phone</th><th>Company WebSite</th>");
        message.append("<tr><td>" + this.timeDate.toString() + "</td>");
        message.append("<td>" + this.fullName + "</td>");
        message.append("<td>" + this.emailAddress.toString() + "</td>");
        message.append("<td>" + this.companyName.toString() + "</td>");
        message.append("<td>" + this.companyPhone.toString() + "</td>");
        message.append("<td>" + this.companyWebSite.toString() + "</td>");
        message.append("</tr></table></body></html>");

        //  return formatted message
        return message.toString();
    }

    //  public method implementations
    /**
     *  Method Name:  Send_New_User_Alerts
     *  Purpose of Method:  creates a default instance of this class, initializes
     *      class variables.
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables initialized
     */
    public Send_New_User_Alerts() {
        //  initialize class variables
        dataVector = new Vector<NewUser_Alert_Data>();
        hasUsersToAlert = false;
        myConnection = new Connection();
        fullName = new StringBuffer(0);
        emailAddress = new StringBuffer(0);
        companyName = new StringBuffer(0);
        companyWebSite = new StringBuffer(0);
        companyPhone = new StringBuffer(0);
        timeDate = new StringBuffer(0);

        //  load data structure
        loadDataVector();
    }

    /**
     *  Method Name:  loadNewUserInformation
     *  Purpose of Method:  takes the information from the new user and loads
     *      it into the class variables for message construction
     *  Arguments:  a hastable containing the new user data
     *  Returns:  void
     *  Preconditions:  object initialized, data from db loaded, data about
     *      new user unknown
     *  Postconditions:  data about new user now know by object
     */
    public void loadNewUserInformation(Hashtable dataHash) {
        //  get objects from hastable
        NewUser user = (NewUser) dataHash.get("user");
        NewCompany company = (NewCompany) dataHash.get("company");

        //  set information from user
        formatName(user.getFirstName(), user.getLastName());
        emailAddress.append(user.getEmail());

        //  set information from company
        companyName.append(company.getCompanyName());
        companyWebSite.append(company.getCompanyWebsite());
        companyPhone.append(company.getCompanyPhone());

        //  format time/date
        formatTimeDate();
    }

    /**
     *  Method Name:  sendSMSAlerts
     *  Purpose of Method:  sends out an SMS alert to anyone in the DB that has
     *      request a text message when a new user signs up
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object has loaded all information about new user,
     *      needs to send text alerts
     *  Postconditions:  text alerts sent
     */
    public void sendSMSAlerts() {
        //  ensure safety
        if (hasUsersToAlert) {
            try {
                //  iterate through vector, sending sms
                for (int i = 0; i < dataVector.size(); i++) {
                    NewUser_Alert_Data data = dataVector.get(i);

                    //  if user has elected to send txt, send it
                    if (data.isIsSendText()) {
                        new SmsSender(data.getTextNumber(), constructSMSMessage(data));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *  Method Name:  sendEmailAlerts
     *  Purpose of Method:  sends out an email alert to anyone in the DB that has
     *      request a email message when a new user signs up
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object has loaded all information about new user,
     *      needs to send email alerts
     *  Postconditions:  email alerts sent
     */
    public void sendEmailAlerts() {
        //  ensure safety
        if (hasUsersToAlert) {
            //  iterate through vector, creating recipient list
            ArrayList<String> rec = new ArrayList();
            for (NewUser_Alert_Data data : dataVector) {
                rec.add(data.getPrimaryEmail());
                if (data.isUseAlternateEmail()) {
                    rec.add(data.getAlternateEmail());
                }
            }
            String[] recipents = new String[rec.size()];
            for(int i =0; i<rec.size();i++){
                recipents[i] = rec.get(i);
            }

            // create message,
            String message = constructEmailMessage();

            //  send message
            try {
                new SchedfoxEmail(EMAIL_SUBJECT, message, recipents);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
};
