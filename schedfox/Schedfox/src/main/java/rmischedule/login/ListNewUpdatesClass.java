/*
 * ListNewUpdatesClass.java
 *
 * Created on December 9, 2005, 12:14 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.login;
import java.util.Vector;

/**
 *
 * @author Ira Juneau
 */
public class ListNewUpdatesClass {
    
    public Vector<UpdateHistory> myChangeHistory;
    
    /** Creates a new instance of ListNewUpdatesClass */
    public ListNewUpdatesClass() {
        myChangeHistory = new Vector();
        HistoryDescription changeOne02152006 = new HistoryDescription("Many updates involving notes have been released. You now have several ways to easily view notes(Client notes, Employee notes and Shift notes) via the schedule window.", "To use look for small document icons in your schedule indicating the client, employee or shift has notes. Place your mouse over the icon for one second and the notes will load and display on the schedule. Look for more improvements of this feature over the next few days.");
        myChangeHistory.add(new UpdateHistory(1.15, "02-15-2006", changeOne02152006));
        HistoryDescription changeOne02032006 = new HistoryDescription("Fixed several bugs for less reliable connections", "Big Fix - Automatic");
        myChangeHistory.add(new UpdateHistory(1.14, "02-03-2006", changeOne02032006));
        HistoryDescription changeOne01312006 = new HistoryDescription("The employee edit window has been upgraded so that the data will load significantly faster", "Upgrade - Automatic");
        myChangeHistory.add(new UpdateHistory(1.13, "01-31-2006", changeOne01312006));
        HistoryDescription changeOne01302006 = new HistoryDescription("The employee search has been redesigned to allow you to search by several different characteristics. New searches include searching by first name, last name or city.", "Open the 'Employee Edit' window by selecting the 'Employees' menu then 'Add/Edit Employees'. When the Employee Edit window appears choose the 'Search Employees' menu and type in the item you wish to search for. SchedFox will then search all companies/branches for that employee and will display the employee if found.");
        myChangeHistory.add(new UpdateHistory(1.12, "01-30-2006", changeOne01302006));
        HistoryDescription changeOne01162006 = new HistoryDescription("The availability list, in the schedule, has been redesigned to provide more feedback.", "Upgrade - Automatic");
        myChangeHistory.add(new UpdateHistory(1.11, "01-16-2006", changeOne01162006));
        HistoryDescription changeOne01132006 = new HistoryDescription("The Zoom In/Zoom Out portion of the program has been redone to allow for faster/less intensive zooming. Several other small fixes have been implemented.", "Upgrade - Automatic");
        myChangeHistory.add(new UpdateHistory(1.10, "01-13-2006", changeOne01132006));
        HistoryDescription changeOne01092006 = new HistoryDescription("The client/employee/shift filters on the schedule have been redone to make using them easier.", "Open a schedule for a company. Up on the top of the schedule you will now notice three drop down boxes. The first box specifies what type of schedules you are interested in viewing, if you want to view open shifts for example use this box. The next box is the employee filter, to display a particular employees schedule use this box. The final box is for clients, use this box to display a particular clients schedule. With this new system it is also to combine this filter. For example to search for all open shifts for a specific client simply choose the client from the final combo box and choose open shifts from the first combo box.");
        myChangeHistory.add(new UpdateHistory(1.09, "01-09-2006", changeOne01092006));
        HistoryDescription changeOne01062006 = new HistoryDescription("You may now search for employees by Social Security.", "Open the 'Employee Edit' window by selecting the 'Employees' menu then 'Add/Edit Employees'. When the Employee Edit window appears choose the 'Search Employees' menu and type in the Social Security of the desired employee. SchedFox will then search all companies/branches for that employee and will display the employee if found.");
        myChangeHistory.add(new UpdateHistory(1.08, "01-06-2006", changeOne01062006));
        HistoryDescription changeOne01042006 = new HistoryDescription("New overtime report is available. This report will list all employees who work over 40 hours as well as the percentage of overtime hours versus standard hours", "To access click on the 'Reports' menu and choose 'OverTime Reports' choose as start and end date and click 'OK'");
        HistoryDescription changeTwo01042006 = new HistoryDescription("A minor bug which would not allow you to mark permanent shifts as Non-Billable or Non-Payable has been fixed.", "Bug Fix - Automatic");
        myChangeHistory.add(new UpdateHistory(1.07, "01-04-2006", changeOne01042006, changeTwo01042006));
        HistoryDescription changeOne12292005 = new HistoryDescription("New employee report for multiple weeks is available. This report will list an employees schedule for multiple weeks up to one month at a time.", "To access click on the 'Reports' menu and choose 'Employee Schedule Reports For Multiple Weeks' choose a start and end date from the window that appears and click 'OK'");
        myChangeHistory.add(new UpdateHistory(1.06, "12-29-2005", changeOne12292005));
        HistoryDescription changeOne12282005 = new HistoryDescription("A bug with loading schedules on year changed has been fixed. This bug was causing an entire year to load incorrectly when a schedule is loaded.", "Bug Fix - Automatic");
        myChangeHistory.add(new UpdateHistory(1.05, "12-28-2005", changeOne12282005));
        HistoryDescription changeOne12162005 = new HistoryDescription("SchedFox will now track all changes being made to your schedule.","SchedFox will automatically track all changes being made on your schedule. To view these changes double click on the shift that you would like to view the history of. A new tab will be available on the window that appears entitled 'Shift History', click this tab. All changes made from 12/16/2005 on will be shown in this window along with the time of the change and the login of the person making the change.");
        HistoryDescription changeTwo12162005 = new HistoryDescription("A minor bug with the printed employee schedules has been found and fixed.", "Bug Fix - Automatic");
        myChangeHistory.add(new UpdateHistory(1.04, "12-16-2005", changeOne12162005, changeTwo12162005));
        HistoryDescription changeOne12142005 = new HistoryDescription("You may now add memos to the employee reports. The memo will appear in the printed employee schedule on the right hand side.", "Open the employee report by choosing 'Reports' from the menu. Click 'Employee Schedule Reports', select your date range. A window will appear which will allow you to type in any memo that you wish. Click 'Print' when you are satisfied with your memo.");
        myChangeHistory.add(new UpdateHistory(1.03, "12-14-2005", changeOne12142005));
        HistoryDescription changeOne12132005 = new HistoryDescription("The Check In/Check Out window is now displaying properly when you Check-In an employee.", "Bug Fix - Automatic");
        HistoryDescription changeTwo12132005 = new HistoryDescription("You can now set the Trainer/Trainee for shifts.", "To set the trainer or trainee for a particular shift you must have the schedule open. Double click on the desired shift and select 'Training Shift' or 'Trainer' from the list of options. You may then specify a Trainer or Trainee using the button on the lower half of the window.");
        myChangeHistory.add(new UpdateHistory(1.02, "12-13-2005", changeOne12132005, changeTwo12132005));
        HistoryDescription changeOne12092005 = new HistoryDescription("You may now view employement statistics (Date first worked, Date last worked, Totals hours worked, Average hours per week) from the Employee Edit window.", "To access click on an employee via the employee add/edit window.");
        HistoryDescription changeTwo12092005 = new HistoryDescription("SchedFox now allows overriding shift conflicts.", "To override a conflict right click on the conflicting shift and select 'Mark this shift as not conflicted' and shift will no longer be purple.");
        myChangeHistory.add(new UpdateHistory(1.01, "12-09-2005", changeOne12092005, changeTwo12092005));
        HistoryDescription changeOne12082005 = new HistoryDescription("Reworked networking to ensure faster response times.", "Automatic");
        myChangeHistory.add(new UpdateHistory(1.00, "12-08-2005", changeOne12082005));
    }
    
    public class UpdateHistory {
        public double versionNumber;
        public String dateUpdated;
        public HistoryDescription[] changes;
        public UpdateHistory(double versionN, String date, HistoryDescription... updates) {
            versionNumber = versionN;
            dateUpdated = date;
            changes = updates;
        }
    }
    
    public class HistoryDescription {
        public String desc;
        public String howto;
        public HistoryDescription(String Desc, String Howto) {
            desc = Desc;
            howto = Howto;
        }
    }
    
}
