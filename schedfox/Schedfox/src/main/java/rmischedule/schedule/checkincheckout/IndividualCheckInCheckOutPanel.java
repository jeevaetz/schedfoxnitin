/*
 * IndividualCheckInCheckOutPanel.java
 *
 * Created on June 23, 2005, 10:12 AM
 */

package rmischedule.schedule.checkincheckout;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import java.awt.Color;
import java.util.*;
import java.awt.event.*;
import java.text.*;
import schedfoxlib.model.AssembleCheckinScheduleType;
import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.util.get_branch_by_id_query;
/**
 *
 * @author  ira
 */
public class IndividualCheckInCheckOutPanel extends javax.swing.JPanel implements Comparable {

    public static final int COMPARE_NORMAL = 0;
    public static final int COMPARE_BY_EMPLOYEE_NAME = 1;
    public static final int COMPARE_BY_CLIENT_NAME = 2;

    public static final Color lightCheckInColor = new Color(255,255,255);
    public static final Color darkCheckInBlueColor  = new Color(221,217,233);
    public static final Color darkCheckInRedColor = new Color(221,177,183);
    public static final Color lightCheckInRedColor = new Color(255, 255, 255);
    public static final Color lightCheckInYellowColor = new Color(255,255,255);
    public static final Color darkCheckInYellowColor = new Color(255, 243, 214);

    private int myType;

    private CheckPanel Parent;
    private int currentSortType;

    private AssembleCheckinScheduleType checkinInfo;

    private boolean isCheckOutData;
    private boolean isHeader;
    public boolean isDeleted;

    public Calendar startDate = Calendar.getInstance();
    public Calendar endDate = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat altTimeFormat = new SimpleDateFormat("HHmm");

    public static Hashtable<String, Branch> branchInfo = new Hashtable<String, Branch>();;
    //private static TimeZone timezone = TimeZone.getTimeZone("CST");
    //Changed this to get the local timezone from the client machine DJA
    private static TimeZone timezone = Calendar.getInstance().getTimeZone();

    /** Creates new form IndividualCheckInCheckOutPanel */
    public IndividualCheckInCheckOutPanel(CheckPanel parent, AssembleCheckinScheduleType checkinInfo) {
        initComponents();

        this.checkinInfo = checkinInfo;
        startDate.setTimeInMillis(0);
        endDate.setTimeInMillis(0);
        myType = parent.IS_CHECKIN_DATA;
        Parent = parent;
        isHeader = false;
        currentSortType = COMPARE_NORMAL;

        if (Main_Window.parentOfApplication.getBranchNameById(checkinInfo.getBranch_id() + "").equals("")) {
            setVisible(false);
        }
        
        if (Parent.getType() == Parent.IS_CHECKOUT_DATA) {
            myType = Parent.IS_CHECKOUT_DATA;
        }
        
        EmployeeNameLabel.setText(checkinInfo.getEmp_last_name() + ", " + checkinInfo.getEmp_first_name());
        ClientNameLabel.setText(checkinInfo.getClient_name());

        setBranch(checkinInfo.getBranch_id() + "");
        setCompany(checkinInfo.getCompanyId());

        isCheckOutData = false;
        updateValuesWithRecordSet(checkinInfo);
        addMouseWheelListener(new MouseWheelListener() {
           public void mouseWheelMoved(MouseWheelEvent e) {
               Parent.myScrollPane.getMouseWheelListeners()[0].mouseWheelMoved(e);
           }
        });
    }

    public AssembleCheckinScheduleType getCheckinType() {
        return this.checkinInfo;
    }

    /**
     * Gets the branch information.
     * @param company_id
     * @param branch_id
     * @return
     */
    public static Branch getBranchInfo(int company_id, int branch_id) {
        if (branchInfo.get(company_id + "," + branch_id) == null) {
            get_branch_by_id_query branchQuery = new get_branch_by_id_query();
            branchQuery.setPreparedStatement(new Object[]{branch_id});
            Connection myConnection = new Connection();
            myConnection.setCompany(company_id + "");
            Record_Set rst = myConnection.executeQuery(branchQuery);
            Branch newBranch = new Branch(rst);
            branchInfo.put(company_id + "," + branch_id, newBranch);
        }
        return branchInfo.get(company_id + "," + branch_id);
    }

    public String getEmployeeName() {
        return EmployeeNameLabel.getText();
    }

    public String getClientName() {
        return ClientNameLabel.getText();
    }

    public String getId() {
        return checkinInfo.getSid();
    }

    public void setCompany(String id) {
        CompanyLabel.setText(Main_Window.parentOfApplication.getCompanyNameById(id));
    }

    public void setBranch(String id) {
        BranchLabel.setText(Main_Window.parentOfApplication.getBranchNameById(id));
    }

    /**
     * Gets employee Id for this checkin...
     */
    public String getEmployeeId() {
        return checkinInfo.getEid() + "";
    }

    /**
     * Gets client Id for this checkin
     */
    public String getClientId() {
        return checkinInfo.getCid() + "";
    }

    /**
     * Sets the parent and adds the child to it
     */
    public void setMyParent(CheckPanel myParent) {
        try {
            if (myParent.getType() == myParent.IS_CHECKOUT_DATA) {
                myType = myParent.IS_CHECKOUT_DATA;
            }
            Parent = myParent;
            setParent();
        } catch (Exception e) {
            System.out.println("Error trying to set parent");
        }
    }


    public void setCheckOutInfo(AssembleCheckinScheduleType scheduleType) {
        try {
            CheckedInLabel.setText(scheduleType.getPerson_checked_in() + "    (" + this.timeFormat.format(scheduleType.getDate()) + ")");
        } catch (Exception e) {
            CheckedInLabel.setText(scheduleType.getPerson_checked_in());
        }
    }


    public void setParent() {

        if (!isDeleted && Parent != null) {
            Parent.add(this);
        } else {
            setVisible(false);
        }
        if (myType == Parent.IS_CHECKOUT_DATA) {
            StartLabel.setText(altTimeFormat.format(endDate.getTime()));
            DateLabel.setText(dateFormat.format(endDate.getTime()));
        } else {
            StartLabel.setText(altTimeFormat.format(startDate.getTime()));
            DateLabel.setText(dateFormat.format(startDate.getTime()));
        }
        if (Main_Window.parentOfApplication.getBranchNameById(checkinInfo.getBranch_id() + "").equals("") ||
                checkinInfo.getCheck_out_option_id() == 2) {
            setVisible(false);
        }
    }

    public void updateValuesWithRecordSet(AssembleCheckinScheduleType checkinInfo) {
        if (Parent.getType() == CheckPanel.IS_CHECKOUT_DATA) {
            myType = CheckPanel.IS_CHECKOUT_DATA;
        }
        int startTime = 0;
        int endTime = 0;
        isDeleted = false;
        if (checkinInfo.getIsdeleted().equals(1)) {
            isDeleted = true;
        }
        if (!isDeleted) {

            startDate = Calendar.getInstance();
            endDate = Calendar.getInstance();
            startDate.clear();
            endDate.clear();

            try {
                Branch entryBranch =
                        getBranchInfo(Integer.parseInt(checkinInfo.getCompanyId()), checkinInfo.getBranch_id());
                int normalOffset = timezone.getOffset(startDate.getTimeInMillis());
                TimeZone entryTimeZone = TimeZone.getTimeZone(entryBranch.getTimezone());
                int entryOffset = entryTimeZone.getOffset(startDate.getTimeInMillis());
                int adjustOffset = normalOffset - entryOffset;
                if (adjustOffset != 0) {
                    startTime = checkinInfo.getStart_time() + (adjustOffset / 60 / 1000);
                    endTime = checkinInfo.getEnd_time() + (adjustOffset / 60 / 1000);
                } else {
                    startTime = checkinInfo.getStart_time();
                    endTime = checkinInfo.getEnd_time();
                }
            } catch (Exception e) {
                System.out.println("Could not determine source Company / Branch, therefore no offset was set.");
                startTime = checkinInfo.getStart_time();
                endTime = checkinInfo.getEnd_time();
            }

            try {
                startDate.setTime(checkinInfo.getDate());
                startDate.set(Calendar.HOUR_OF_DAY, (int)Math.floor(startTime / 60));
                startDate.set(Calendar.MINUTE, startTime % 60);
            } catch (Exception e) {
                
            }

            try {
                endDate.setTime(checkinInfo.getDate());
                endDate.set(Calendar.HOUR_OF_DAY, (int)Math.floor(endTime / 60));
                endDate.set(Calendar.MINUTE, endTime % 60);
                if(startTime > endTime) {
                    endDate.add(Calendar.DAY_OF_WEEK, 1);
                }
            } catch (Exception e) {
                System.out.println("Problem setting end date");
            }

            if (myType == Parent.IS_CHECKOUT_DATA) {
                StartLabel.setText(altTimeFormat.format(endDate.getTime()));
                DateLabel.setText(dateFormat.format(endDate.getTime()));
            } else {
                StartLabel.setText(altTimeFormat.format(startDate.getTime()));
                DateLabel.setText(dateFormat.format(startDate.getTime()));
            }
            EmployeeNameLabel.setText(checkinInfo.getEmp_last_name() + ", " + checkinInfo.getEmp_first_name());
            setVisible(true);
        } else {
            setVisible(false);
        }
       repaint();
    }

    public IndividualCheckInCheckOutPanel(int type, CheckPanel parent) {
        initComponents();
        myType = type;
        Parent = parent;
        isHeader = true;
        if (type == parent.IS_CHECKIN_DATA) {
            StartLabel.setText("xAAAStart");
        } else if (type == parent.IS_CHECKOUT_DATA) {
            StartLabel.setText("xBBBEnd");
            CheckedInLabel.setText("Checked In By");
        } else {
            StartLabel.setText("ZSSZSSStart");
        }
        ClientNameLabel.setText("Client Name");
        EmployeeNameLabel.setText("Employee Name");
        BranchLabel.setText("Branch");
        CompanyLabel.setText("Company");

    }

    public Connection getMyConn(GeneralQueryFormat mySaveQuery) {
        Connection myConn = new Connection();
        myConn.setBranch(checkinInfo.getBranch_id() + "");
        myConn.setCompany(checkinInfo.getCompanyId());
        myConn.prepQuery(mySaveQuery);
        return myConn;
    }

    /**
     * returns true if equals
     */
    public boolean equalsMe(IndividualCheckInCheckOutPanel myPanel) {
        boolean returnVal = false;
        if (getShiftId().equals(myPanel.getShiftId())) {
            myPanel.ClientNameLabel.setText(ClientNameLabel.getText());
            returnVal = true;
        }
        return returnVal;
    }

    public String getShiftId() {
        return checkinInfo.getSid();
    }

    /**
     * Returns true if equals...
     */
    public boolean equalsMe(Record_Set rs) {
        boolean returnVal = false;
        if (rs.getString("sid").equals(getShiftId())) {
            returnVal = true;
        }
        return returnVal;
        
    }

    public CheckPanel getMyParent() {
        return Parent;
    }

    /**
     * Sets the background to either a light or dark color depending on if it is a even
     * or odd row...called after sorting our ArrayList or data....
     */
    public void setBackground(int i, String currentTime, String currentDate) {
        if (isHeader || !isVisible()) {
            return;
        }

        Calendar compTime;
        if(Parent.getType() == CheckPanel.IS_CHECKOUT_DATA)
            compTime = endDate;
        else
            compTime= startDate;

        long timeDiff = compTime.getTime().getTime() - CheckPanel.Now.getTime().getTime();

        //HACK ALERT!  This is here to eliminate old shifts on the Never Checked In tab
        if(timeDiff < -10800000)
            setVisible(false);

        if(timeDiff < 0) {
            setAsAlarmColor(i);
            Main_Window.parentOfApplication.myOpenShiftAlertWindow.addOpenShift(this);
        }
        else if(timeDiff > 3600000) {
            setAsNormalColor(i);
        }
        else {
            setAsWarningColor(i);
            Main_Window.parentOfApplication.myOpenShiftAlertWindow.addOpenShift(this);
        }

    }

    /**
     * Sets colors to blue/white or whatever, normal shift no warnings...
     */
    public void setAsNormalColor(int i) {
        setBackground(darkCheckInBlueColor);
    }

    /**
     * Sets colors to yellow/white or whatever, shifts coming up withing buffer time...
     */
    public void setAsWarningColor(int i) {
        setBackground(darkCheckInYellowColor);
    }

    /**
     * Sets colors to red/white or whatever, shifts that have started...
     */
    public void setAsAlarmColor(int i) {
        setBackground(darkCheckInRedColor);
    }

    /**
     * Sorts by Date then Start/End Time, then Employee Name....
     */
    private int sortByNormal(IndividualCheckInCheckOutPanel compareTo) {
        try {
            int comp;
            if(Parent.getType() == CheckPanel.IS_CHECKOUT_DATA) {
                comp = endDate.compareTo(compareTo.endDate);
            } else {
                comp = startDate.compareTo(compareTo.startDate);
            }

            if(comp == 0) {
                return this.EmployeeNameLabel.getText().compareTo(compareTo.EmployeeNameLabel.getText());
            } else {
                return comp;
            }

        } catch(Exception ex) { return 0; }
    }


    /**
     * Used to sort these panels used by the parent class, which has vector of these
     * suckas...
     */
    public int compareTo(Object o) {
        if (isHeader) return 0;

        IndividualCheckInCheckOutPanel compareTo = null;
        try { compareTo = (IndividualCheckInCheckOutPanel)o; } catch (Exception e) {}

        return sortByNormal (compareTo);
    }

    /**
     * Upon user click saves data into our database...
     */
    public void saveCheckIn() {
        save_check_in_query mySaveQuery = new save_check_in_query();
        Connection myConn = new Connection();
        myConn.setBranch(checkinInfo.getBranch_id() + "");
        myConn.setCompany(checkinInfo.getCompanyId());
        myConn.prepQuery(mySaveQuery);
        mySaveQuery.update(StaticDateTimeFunctions.convertCalendarToDatabaseFormat(Calendar.getInstance()), checkinInfo.getSid(),
                checkinInfo.getEnd_time() + "", checkinInfo.getStart_time() + "", checkinInfo.getEid().toString(),"", System.currentTimeMillis() +"");

        try {
            myConn.executeUpdate(mySaveQuery);

        } catch (Exception e) {}
    }

    /**
     * Upon user clicks saves data into our database, as checked out
     */
    public void saveCheckOut() {
        save_check_out_query mySaveQuery = new save_check_out_query();
        Connection myConn = new Connection();
        myConn.setBranch(checkinInfo.getBranch_id() + "");
        myConn.setCompany(checkinInfo.getCompanyId());
        myConn.prepQuery(mySaveQuery);
        mySaveQuery.update(checkinInfo.getSid(), StaticDateTimeFunctions.convertCalendarToDatabaseFormat(Calendar.getInstance()));

        try {
            myConn.executeUpdate(mySaveQuery);
        } catch (Exception e) {}
    }

    /**
     * Toggles Graphical Selection characteristics for our options....
     */
    public void setBorderToSelected(boolean isSelected) {
        if (isSelected) {
            setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        } else {
            setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        }
        Parent.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StartLabel = new javax.swing.JLabel();
        DateLabel = new javax.swing.JLabel();
        EmployeeNameLabel = new javax.swing.JLabel();
        ClientNameLabel = new javax.swing.JLabel();
        BranchLabel = new javax.swing.JLabel();
        CompanyLabel = new javax.swing.JLabel();
        CheckedInLabel = new javax.swing.JLabel();

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMaximumSize(new java.awt.Dimension(10000, 22));
        setMinimumSize(new java.awt.Dimension(0, 22));
        setPreferredSize(new java.awt.Dimension(100, 22));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showCheckInOptions(evt);
            }
        });
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        StartLabel.setMaximumSize(new java.awt.Dimension(40, 100));
        StartLabel.setMinimumSize(new java.awt.Dimension(40, 0));
        StartLabel.setPreferredSize(new java.awt.Dimension(40, 0));
        add(StartLabel);

        DateLabel.setMaximumSize(new java.awt.Dimension(70, 20));
        DateLabel.setMinimumSize(new java.awt.Dimension(70, 20));
        DateLabel.setPreferredSize(new java.awt.Dimension(70, 20));
        add(DateLabel);

        EmployeeNameLabel.setMaximumSize(new java.awt.Dimension(160, 18));
        EmployeeNameLabel.setMinimumSize(new java.awt.Dimension(160, 0));
        EmployeeNameLabel.setPreferredSize(new java.awt.Dimension(160, 0));
        add(EmployeeNameLabel);

        ClientNameLabel.setMaximumSize(new java.awt.Dimension(160, 100));
        ClientNameLabel.setMinimumSize(new java.awt.Dimension(160, 0));
        ClientNameLabel.setPreferredSize(new java.awt.Dimension(160, 0));
        add(ClientNameLabel);

        BranchLabel.setMaximumSize(new java.awt.Dimension(100, 100));
        BranchLabel.setMinimumSize(new java.awt.Dimension(100, 0));
        BranchLabel.setPreferredSize(new java.awt.Dimension(100, 0));
        add(BranchLabel);

        CompanyLabel.setMaximumSize(new java.awt.Dimension(150, 100));
        CompanyLabel.setMinimumSize(new java.awt.Dimension(150, 0));
        CompanyLabel.setPreferredSize(new java.awt.Dimension(150, 0));
        add(CompanyLabel);

        CheckedInLabel.setMaximumSize(new java.awt.Dimension(200, 100));
        CheckedInLabel.setMinimumSize(new java.awt.Dimension(150, 0));
        CheckedInLabel.setPreferredSize(new java.awt.Dimension(200, 0));
        add(CheckedInLabel);
    }// </editor-fold>//GEN-END:initComponents

    private void showCheckInOptions(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showCheckInOptions
        if(this.isHeader)
            return;

        this.Parent.getCheckInOptions().showData(this);
    }//GEN-LAST:event_showCheckInOptions


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel BranchLabel;
    private javax.swing.JLabel CheckedInLabel;
    public javax.swing.JLabel ClientNameLabel;
    public javax.swing.JLabel CompanyLabel;
    private javax.swing.JLabel DateLabel;
    public javax.swing.JLabel EmployeeNameLabel;
    public javax.swing.JLabel StartLabel;
    // End of variables declaration//GEN-END:variables

}
