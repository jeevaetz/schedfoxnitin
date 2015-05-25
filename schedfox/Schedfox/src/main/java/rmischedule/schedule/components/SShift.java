/*
 * SShift.java
 *
 * Created on April 13, 2004, 2:59 PM
 */
package rmischedule.schedule.components;

import schedfoxlib.model.ShiftTypeClass;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import rmischedule.schedule.schedulesizes.ComponentDimensions;
import rmischedule.schedule.schedulesizes.ZoomListener;
import rmischedule.security.*;
import rmischedule.schedule.Schedule_Main_Form;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.schedule.Schedule_View_Panel;
import java.lang.Comparable;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import rmischedule.data_connection.Connection;
import rmischedule.event_log.EventLoggerInternalFrame;
import schedfoxlib.model.util.Record_Set;
import rmischedule.schedule.components.notes.*;
import rmischedule.main.Main_Window;
import schedfoxlib.model.RateCode;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.availability.get_availability_note_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.notes.*;
import rmischeduleserver.util.StaticDateTimeFunctions;

/**
 *
 * @author jason.allen
 */
public class SShift extends DragAndDropLabel implements DragAndDropContainer, Comparable {

    public static final Color ConflictedColor = new Color(255, 153, 255);
    public static final Color OpenColor = new Color(255, 51, 51);
    public static final Color TempOpenColor = new Color(250, 199, 195);
    public static final Color TempShiftOpenColor = new Color(248, 252, 255);
    public static final Color UnconfirmedColor = new Color(255, 255, 153);
    public static final Color selectedColor = new Color(0, 153, 255);
    private boolean hasData;
    private SMainComponent myClient;
    private SEmployee myEmployee;
    public Schedule_View_Panel parent;
    public SRow myRow;
    private int day_code;
    private int week_no;
    private String str_week_no;
    public boolean isSelected;
    public boolean isHovor;
    private boolean isInstantiated;
    public myNoteIconLabel noteIconLabel;
    private DShift newDshift;
    public UnitToDisplay myShift;
    /**
     * Needed by Creamtec
     */
    public shiftBackgroundCharacteristics myShiftChar;
    private long lastCheckedShiftInPast;
    private boolean isShiftInPast;
    private boolean shiftInvisible;
    private int MousePressedXLocation;
    private int MousePressedYLocation;
    private javax.swing.border.AbstractBorder innerBorder;
    private javax.swing.border.AbstractBorder outerBorder;
    private SShift thisObject;

    public SShift(int DayCode, int weekNo, SRow row) {
        super(row.myWeek.mySched.myParent.getLayeredPane(), row.myWeek.mySched.myParent.FloatContainerPanel);
        //Above line sometimes causes Event Thread to error, in the DragAndDropLabel init method
        thisObject = this;
        isInstantiated = false;
        hasData = false;
        day_code = DayCode;

        week_no = weekNo;
        myRow = row;
        str_week_no = String.valueOf(week_no);
        myClient = myRow.myWeek.mySched.getClient();
        myEmployee = myRow.myWeek.mySched.getEmployee();
        parent = myRow.myWeek.mySched.myParent;
        myShift = null;
        isSelected = false;
        shiftInvisible = false;
        myShiftChar = new shiftBackgroundCharacteristics();
        super.setDragEnabled(false);

    }

    /**
     * These dispose methods are so very important... We have a massive memory
     * leak since it appears java's GC cannot handle all of our bidirection
     * references... Therefore it is very very important as you add classes that
     * you properly dispose all new private class, and remove all objects from
     * sub panels. Please verify from time to time in HEAP stack that this is
     * still working.
     */
    public void dispose() {
        super.dispose();
        myClient = null;
        myEmployee = null;
        parent = null;
        myRow = null;
        noteIconLabel = null;
        newDshift = null;
        myShift = null;
        myShiftChar = null;
        thisObject = null;
        this.removeAll();
    }

    public boolean isHasData() {
        return this.hasData;
    }

    public boolean isInstantiated() {
        return this.isInstantiated;
    }

    public void instantiate() {
        if (isInstantiated) {
            return;
        }

        isInstantiated = true;
        isShiftInPast = false;
        lastCheckedShiftInPast = 0;
        this.setFocusable(true);

        setFont(Main_Window.myShiftTotalFont);

        this.addMouseListener(new SShift.mouseHandler(this));

        //this.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_CLICK);
        //this.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_DOUBLE_CLICK);
        //this.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_MOUSE_DOWN);
        setBG();
        outerBorder = new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED);
        setBorder(new javax.swing.border.CompoundBorder(outerBorder, innerBorder));

        setOpaque(true);

    }

    public void setNewShift(DShift newS) {
        newDshift = newS;
    }

    public void commitNewShift() {
        this.buildSShift(newDshift);
    }

    public void paint(Graphics g) {
        if (!isInstantiated) {
            instantiate();
        }
        super.paint(g);
    }

    /**
     * Grabs notes for given shift
     */
    private ArrayList<NoteData> getNotesForShift(String shiftId) {
        get_notes_for_shift_query getNotes = new get_notes_for_shift_query();
        getNotes.update(shiftId);
        Record_Set rs = new Record_Set();
        ArrayList<NoteData> retVal = new ArrayList<NoteData>();
        try {
            rs = myRow.myWeek.mySched.myParent.getConnection().executeQuery(getNotes);
            for (int r = 0; r < rs.length(); r++) {
                NoteData noteData = new NoteData();
                noteData.setDate(rs.getDate("notes_date_time"));
                noteData.setNote(rs.getString("notes"));
                noteData.setNoteType(rs.getString("note_type_name"));
                noteData.setUserLogin(rs.getString("user_login"));
                retVal.add(noteData);
                rs.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     * Grabs notes for given shift
     */
    private ArrayList<NoteData> getAvailNotes(Integer shiftId) {
        get_availability_note_query getNotes = new get_availability_note_query();
        getNotes.setPreparedStatement(new Object[]{shiftId});
        Record_Set rs = new Record_Set();
        ArrayList<NoteData> retVal = new ArrayList<NoteData>();
        try {
            rs = myRow.myWeek.mySched.myParent.getConnection().executeQuery(getNotes);
            for (int r = 0; r < rs.length(); r++) {
                NoteData noteData = new NoteData();
                noteData.setDate(rs.getDate("date_entered"));
                noteData.setNote(rs.getString("note"));
                noteData.setNoteType("Avail Note");
                noteData.setUserLogin("");
                retVal.add(noteData);

                rs.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public boolean displayRateCode() {
        return Main_Window.parentOfApplication.getShowRateCodes();
    }

    public void paintComponent(Graphics g) {
        if (!isInstantiated) {
            super.paintComponent(g);
            return;
        }
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = (AffineTransform) g2d.getTransform().clone();

        g2d.scale(ComponentDimensions.getScaleValue(), ComponentDimensions.getScaleValue());

        if (myShiftChar.getIcon() != null && !this.shiftInvisible) {
            myShiftChar.getIcon().paintIcon(this, g, 4, 2);
        }

        if (hasData && !this.shiftInvisible) {
            g.setFont(Main_Window.shift_font);
            //g.setColor(new Color(102,102,155));//mod
            g.setColor(Color.black);//mod
            g.drawString(myShift.getFormattedStartTime(), 4, 2 + Main_Window.shift_font.getSize());
            g.drawString(myShift.getFormattedEndTime(), 4, 55);
            if (this.displayRateCode() && myShift instanceof DShift) {
                g.setColor(Color.RED);
                RateCode rate = parent.getRateCode(((DShift) myShift).getRateCode());
                if (rate != null) {
                    g.drawString(rate.getUskedRateCode(), 5, 35);
                }
            }
            g.setColor(Color.GRAY);

            try {
                boolean shouldDisplayEyeball = true;
                if (Main_Window.parentOfApplication.getUser().getUserId().equals("43") || Main_Window.parentOfApplication.getUser().getUserId().equals("2795")
                        || Main_Window.parentOfApplication.getUser().getUserId().equals("2941") || Main_Window.parentOfApplication.getUser().getUserId().equals("8275")) {
                    shouldDisplayEyeball = false;
                }
                if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_VIEWED_BY_EMPLOYEE) && !myShift.isMaster() && shouldDisplayEyeball) {
                    g.drawImage(Main_Window.Shift_Viewed_Icon.getImage(), 1, 22, SShift.this);
                }
            } catch (Exception exe) {
            }

            g.drawString(myShift.getNoHoursDouble() + "", 30, 35);
            if (myShift.hasNote() && !Main_Window.isClientLoggedIn() && !Main_Window.isEmployeeLoggedIn()) {
                if (noteIconLabel == null) {
                    noteIconLabel = new shiftNotesLabel("");
                    setLayout(null);
                    add(noteIconLabel);
                    noteIconLabel.setBounds(ComponentDimensions.currentSizes.get("SShift").width - 17, 1, 16, 16);
                    noteIconLabel.setOpaque(false);
                }
                noteIconLabel.setIcon(Main_Window.Note16x16);

            }
        }

        g2d.setTransform(oldTransform);
        if (myShiftChar.hasChanged()) {
            myShiftChar.hasChanged = false;
        }
    }

    /**
     * Checks a variety of conditions to see if this SShift can be dropped on,
     * ie: is employee banned, is employee terminated? etc...
     */
    public boolean canNotWorkThisShift() {
        return checkIfEmployeeTerminated() && checkIfClientTerminated();
    }

    /**
     * Checks if employee has not started work yet, or if they have been
     * terminated...
     */
    public boolean checkIfEmployeeTerminated() {
        if (myEmployee.getTerm() == null) {
            return false;
        }
        if (myEmployee.isDeleted()) {
            if (myEmployee.getTerm().compareTo(getMyDate().getTime()) <= 0) {
                return true;
            }
        }
        if (myEmployee.getHire().compareTo(getMyDate().getTime()) > 0) {
            return true;
        }
        return false;
    }

    public boolean checkIfClientTerminated() {
        if (myClient instanceof STimeOff) {
            return false;
        } else {
            if (myClient.getClientData().getClientEndDate() == null) {
                return false;
            }
            if (myClient.isDeleted()) {
                if (myClient.getClientData().getClientEndDate().compareTo(getMyDate().getTime()) < 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public Dimension getMaximumSize() {
        return new Dimension(ComponentDimensions.currentSizes.get("SShift").width, ComponentDimensions.currentSizes.get("SShift").height);
    }

    public Dimension getMinimumSize() {
        return new Dimension(ComponentDimensions.currentSizes.get("SShift").width, ComponentDimensions.currentSizes.get("SShift").height);
    }

    public Dimension getPreferredSize() {
        return new Dimension(ComponentDimensions.currentSizes.get("SShift").width, ComponentDimensions.currentSizes.get("SShift").height);
    }

    private boolean checkIfOverPartTimeHours(SEmployee empToCheck, DShift shift) {
        boolean returnVal = true;
        if (empToCheck == null) {
            empToCheck = getEmployee();
        }
        try {
            if (empToCheck.getEmployee().getFullTime() != true) {
                double hoursWorked = empToCheck.getHoursWorkedForWeek(week_no);
                if (hoursWorked + shift.getNoHoursDouble() > 32.0) {
                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication, empToCheck.getName() + " is a part time employee and this shift would put them over 32 hours for the week. \r\nThis is not allowed!", "ERROR!", JOptionPane.OK_OPTION);
                    returnVal = false;
                }
            }
        } catch (Exception exe) {
        }
        return returnVal;
    }

    /**
     * Checks Availability, Banned and Certifications and prompts user the
     * result if not allowed for whatever reason...
     */
    private boolean checkAvailabilityBannedAndCerts(SEmployee empToCheck) {
        boolean returnVal = false;
        if (empToCheck == null) {
            empToCheck = getEmployee();
        }

        if (getClient() instanceof SMainComponent) {
            SMainComponent currClient = getClient();
            Vector<CertificationClass> missingCerts = new Vector<CertificationClass>();
            if (currClient instanceof SClient) {
                try {
                    missingCerts = ((SClient) currClient).getUnfilledCertifications(empToCheck.getCertifications());
                } catch (Exception exe) {
                }
            }
            if (empToCheck.getName().equals("")) {
                return true;
            } else if (this.myRow.myWeek.mySched.myParent.isClientEmpBanned(currClient, empToCheck) && !this.isPast()) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, empToCheck.getName() + " has been banned from working for this location!", "ERROR!", JOptionPane.OK_OPTION);
            } else if (missingCerts.size() > 0) {
                StringBuilder certText = new StringBuilder();
                certText.append(empToCheck.getName() + " is missing the following certifications to work here!\n");
                for (int i = 0; i < missingCerts.size(); i++) {
                    certText.append(i + ")  " + missingCerts.get(i).getName() + "\n");
                }
                certText.append("Allow employee to work here anyway?");
                int response = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, certText, "Missing Certifications!", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    returnVal = true;
                }
            } else {
                returnVal = true;
            }
        }
        return returnVal;
    }

    public void buildSShift(UnitToDisplay dShift) {
        buildSShift(dShift, true);
    }

    /**
     * Method allows you control over if old info should be cleared, this fixes
     * a problem with consolidateRowsAtDays....
     */
    public void buildSShift(UnitToDisplay dShift, boolean clearVal) {
        if (!isInstantiated) {
            instantiate();
        }

        hasData = true;
        myShift = dShift;
        dShift.setShift(this, clearVal);
        //Adds to Our Vector So that we can know what shifts are associated w/ which Employee...
        myEmployee.addShift(this.myShift);
        if (parent.isInitialized() && !myShift.isCurrentlyFilteringDontSave()) {
            isHovor = false;
            isSelected = false;
        }
        if (dShift.getShift() != null) {
            dShift.getShift().setBG();
        }
        myShift.recalTimes();
        if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
            super.setDragEnabled(true);
        }
        parent.addShiftToHash(dShift);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setVisible(true);
                outerBorder = new javax.swing.border.CompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                setBorder(new javax.swing.border.CompoundBorder(outerBorder, innerBorder));
                repaint();
                if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                    ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObject, true);
                }

            }
        });

    }

    public void updateInfo() {
        try {
            if (myShift.isDeleted()) {
                myShift.getShift().cleareInfo();
                return;
            }
        } catch (Exception e) {
        }
        setBG();
        validate();
    }

    /**
     * Should make rendering faster both in swing and in creamtec by stating
     * that non of my children will overlap.
     *
     * @return
     */
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return true;
    }

    public boolean hasData() {
        return hasData;
    }

    public void cleareInfo() {
        if (hasData) {
            cleareInfo(true);
        }
    }

    public synchronized void cleareInfo(boolean shouldConsolidateRows) {
        try {
            if (this.myShift != null) {
                this.parent.removeShift(this.myShift);
                myRow.removeShiftFromHash(getDayCode());
                SEmployee myEmp = this.myEmployee;
                this.myEmployee.removeShift(this.myShift);
                parent.CheckScheduleForConflicts(myEmp.getId() + "");
                if (noteIconLabel != null) {
                    this.remove(noteIconLabel);
                    noteIconLabel = null;
                }
            }
            myShift.zeroTimes();
            super.setDragEnabled(false); //Shut off drag enabled from the DragLabel class...
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    outerBorder = new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED);
                    setBackground(Schedule_View_Panel.blank_color);
                    setBorder(new javax.swing.border.CompoundBorder(outerBorder, innerBorder));
                    if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                        repaint();
                        ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObject, true);
                    }

                }
            });

        } catch (Exception e) {
        } finally {
            myShift = null;
            hasData = false;
            setBG();
            repaint();
            myRow.myWeek.consolidateRowsAtDay(this.getDayCode());
        }
    }

    /**
     * property returns
     */
    public int getDayCode() {
        return day_code;
    }

    public int getOffset() {
        //Now lines up w/ Calendar int for DOW
        int day = (this.getDayCode() % 7) + 1;
        int value = (day - parent.getBegOfWeek()) % 7;
        if (value < 0) {
            value = 7 + value;
        }
        return value;
    }

    public int getWeekNo() {
        return week_no;
    }

    public SMainComponent getClient() {
        return myClient;
    }

    public String getWeekStr() {
        return str_week_no;
    }

    public SEmployee getEmployee() {
        return myRow.myWeek.mySched.getEmployee();
    }

    public boolean isLastWeek() {
        Calendar begThisWeek = StaticDateTimeFunctions.getBegOfWeek(Calendar.getInstance(), Integer.parseInt(this.parent.getCompany()));
        Calendar weekBefore = StaticDateTimeFunctions.getBegOfWeek(Calendar.getInstance(), Integer.parseInt(this.parent.getCompany()));
        weekBefore.add(Calendar.DAY_OF_YEAR, -7);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String endStr = format.format(begThisWeek.getTime());
        String startStr = format.format(weekBefore.getTime());

        if (this.myShift.getDatabaseDate().compareTo(startStr) >= 0 && this.myShift.getDatabaseDate().compareTo(endStr) < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPast() {
        /* this if didn't make sense */
        if (parent.getDateByWeekDay(week_no, this.getOffset()).compareTo(parent.currentDate) >= 0) {
            isShiftInPast = false;
        } else {
            isShiftInPast = true;
        }
        return isShiftInPast;
    }

    public Calendar getMyDate() {
        return parent.getDate(week_no, day_code);
    }

    public String getMyDatabaseDate() {
        return parent.getDateByWeekDay(week_no, this.getOffset());
    }

    public void setRow(SRow r) {
        myRow = r;
    }

    /**
     * Used really with our filters to not show shifts you don't want too...
     */
    public void setNonVisibleShift(boolean nonVis) {
        shiftInvisible = nonVis;
    }

    public boolean shiftNonVisible() {
        return shiftInvisible;
    }

    private void setBGDAvailability() {
        if (isSelected) {
            myShiftChar.setCharacteristics(selectedColor);
        } else if (myEmployee.getName().length() == 0) {
            if (myShift.isMaster()) {
                myShiftChar.setCharacteristics(OpenColor, Main_Window.Warning_Large_Icon);
            } else {
                myShiftChar.setCharacteristics(TempOpenColor, Main_Window.Warning_Large_Icon);
            }
        } else if (myShift.getType() != null) {
            if (!myShift.isMaster()) {
                myShiftChar.setCharacteristics(TempShiftOpenColor, null);//modified
            } else {
                myShiftChar.setCharacteristics(Schedule_View_Panel.master_color, null);
            }
        } else {
            if (!myShift.isMaster() && myShift.getRealMasterId().equals("0")) {
                myShiftChar.setCharacteristics(TempShiftOpenColor, null);//modified
            } else {
                myShiftChar.setCharacteristics(Schedule_View_Panel.master_color, null);
            }
        }
    }

    private void setBGDShift() {
        if (isSelected) {
            myShiftChar.setCharacteristics(selectedColor);
        } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_RECONCILED)) {
            if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
                myShiftChar.setCharacteristics(Schedule_View_Panel.training_color, Main_Window.Lock_Time_Icon);
            } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)) {
                myShiftChar.setCharacteristics(Schedule_View_Panel.trainer_color, Main_Window.Lock_Time_Icon);
            } else {
                myShiftChar.setCharacteristics(Schedule_View_Panel.master_color, Main_Window.Lock_Time_Icon);
            }
        } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
            myShiftChar.setCharacteristics(Schedule_View_Panel.training_color, Main_Window.Shift_Training_Icon);
        } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)) {
            myShiftChar.setCharacteristics(Schedule_View_Panel.trainer_color, Main_Window.Shift_Training_Icon);
        } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_EXTRA_COVERAGE_SHIFT)) {
            myShiftChar.setCharacteristics(Schedule_View_Panel.extra_coverage_color, null);
        } else if (myEmployee.getName().length() == 0) {
            if (myShift.isMaster()) {
                myShiftChar.setCharacteristics(OpenColor, Main_Window.Warning_Large_Icon);
            } else {
                myShiftChar.setCharacteristics(TempOpenColor, Main_Window.Warning_Large_Icon);
            }
        } else if (myShift.hasConflict()) {
            myShiftChar.setCharacteristics(ConflictedColor, Main_Window.Warning_Large_Icon);
        } else if (myShift.isOverwrittingBuffer()) {
            myShiftChar.setCharacteristics(Color.ORANGE);
        } else if (myShift.getType() != null) {
            if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_UNCONFIRMED)
                    && myRow.myWeek.mySched.myParent.shouldMarkUnconShifts()) {
                myShiftChar.setCharacteristics(UnconfirmedColor, Main_Window.Mobile_Phone_3_Icon);
            } else {
                if (!myShift.isMaster()) {
                    myShiftChar.setCharacteristics(TempShiftOpenColor, null);//modified
                } else {
                    myShiftChar.setCharacteristics(Schedule_View_Panel.master_color, null);
                }
            }
        } else {
            if (!myShift.isMaster() && myShift.getRealMasterId().equals("0")) {
                myShiftChar.setCharacteristics(TempShiftOpenColor, null);//modified
            } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
                myShiftChar.setCharacteristics(Schedule_View_Panel.training_color, Main_Window.Shift_Training_Icon);
            } else if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)) {
                myShiftChar.setCharacteristics(Schedule_View_Panel.trainer_color, Main_Window.Shift_Training_Icon);
            } else {
                myShiftChar.setCharacteristics(Schedule_View_Panel.master_color, null);
            }
        }
    }

    /**
     * Our new setBG method, uses a class to hold a color and icon, and some
     * settings look at bottom of file for more information...pretty fast now...
     */
    public void setBG() {
        if (!isInstantiated) {
            return;
        }
        if (myEmployee.isDeleted()) {
            myShiftChar.setCharacteristics(Schedule_View_Panel.employee_del_color, null);
        }
        if (myShift != null) {
            if (myShift instanceof DShift) {
                this.setBGDShift();
            } else if (myShift instanceof DAvailability) {
                this.setBGDAvailability();
            }
        } else {
            if (getEmployee().getShiftsAvailability(this) == null) {
                myShiftChar.setCharacteristics(Schedule_View_Panel.blank_color, null);
            } else {
                if (getClient() instanceof SClient) {
                    myShiftChar.setCharacteristics(Schedule_View_Panel.blank_color, getEmployee().getShiftsAvailability(this).getAvailIcon());
                } else if (getClient() instanceof STimeOff) {
                    myShiftChar.setCharacteristics(Schedule_View_Panel.blank_color, null);
                }
            }
        }

        if ((checkIfEmployeeTerminated() || checkIfClientTerminated()) && !hasData) {
            myShiftChar.setCharacteristics(Schedule_View_Panel.blank_color, Main_Window.Deleted_Shift_Icon);
        }

        if (shiftInvisible) {
            myShiftChar.setCharacteristics(Schedule_View_Panel.blank_color.darker());
        } else if (isPast()) {
            myShiftChar.setCharacteristics(myShiftChar.getColor().darker());
        }

        this.setBackground(myShiftChar.getColor());
    }

    public void setCanLandShift(boolean canILand) {
        try {
            myShiftChar.setLandIcon(canILand);
        } catch (Exception e) {
        }
        repaint();
    }

    public Color getMyBackground() {
        return myShiftChar.getColor();
    }

    public void setBlank() {
        setBackground(parent.blank_color);
    }

    public void setActive() {
        repaint();
    }

    public boolean isShiftSelected() {
        return isSelected;
    }

    /**
     * Returns Vector of all Selected shifts from our AvailabilityComboBox,
     * called by AEmployee to determine what other shifts should have data set
     * with current employee.
     */
    public AvailabilityComboBox.mySShiftVector getVectorOfAllSelectedShifts() {
        return parent.getSelectedShifts();
    }

    public void unselectGraphics() {
        isSelected = false;
    }

    public void selectThisShift(boolean isSelect, boolean shouldDispAvail) {
        isSelected = isSelect;
        if (isSelect) {
            parent.getAvail().acb.AddShiftToCurrentAvailability(myShift, this, shouldDispAvail);
        } else {
            parent.getAvail().acb.RemoveShiftFromCurrentAvailability(myShift, this);
        }
    }

    /**
     * Run on Control and Mouse 1 Down, used to change border color, and also
     * add info to Schedule_view or do inverse...
     */
    public void selectThisShift(boolean isSelect) {
        isSelected = isSelect;
        if (isSelect) {
            parent.getAvail().acb.AddShiftToCurrentAvailability(myShift, this);
        } else {
            parent.getAvail().acb.RemoveShiftFromCurrentAvailability(myShift, this);
        }
    }

    /**
     * Method that controls the graphic qualities of selected and normal shifts.
     * Should only be called from availabilityComboBox... no where else die!
     */
    public void selectGraphicQualities(boolean isSelect) {
        if (isSelect) {
            setBackground(selectedColor);
        } else {
            isSelected = isSelect;
            setBG();
        }
        innerBorder = null;
        setBorder(new javax.swing.border.CompoundBorder(outerBorder, innerBorder));
    }

    /**
     * Method to clear all shifts calls
     * AvailabilityComboBox.ClearAllShiftSelections which then calls
     * selectThisShift(false) on all selected shifts.
     */
    private void clearAllSelectedShifts() {
        parent.getAvail().acb.ClearAllShiftSelections();
    }

    public Point getMyPosition() {
        int x, y;
        Point p = myRow.getMyPosition();
        x = getX() + p.x;
        y = getY() + p.y;
        x = ((getDayCode() - 1) * ComponentDimensions.currentSizes.get("SShift").width) + p.x;
        y = p.y;
        return new Point(x, y);
    }

    /**
     * Called by runOnDrop if a SEmployee is dropped on it, will check the
     * Vector of selected shifts and if anyshifts are selected then will add emp
     * to selection else just add to this shift...
     */
    public void addEmployeeToMultipleOrOneShift(SEmployee empToAdd) {
        AvailabilityComboBox.mySShiftVector selectedShift = getVectorOfAllSelectedShifts();
        SShift tempShift;
        RunQueriesEx myQuery = new RunQueriesEx();
        if (!selectedShift.contains(this)) {
            for (int i = 0; i < selectedShift.size(); i++) {
                tempShift = ((SShift) selectedShift.get(i));
                tempShift.selectThisShift(false);
            }
            selectedShift.removeAllElements();
        }
        if (selectedShift.size() > 10) {
            for (int i = 0; i < selectedShift.size(); i++) {
                try {
                    if (selectedShift.get(i).myShift instanceof DShift) {
                        QueryGenerateShift myShift = new QueryGenerateShift((DShift) selectedShift.get(i).myShift);
                        myShift.moveShift(getClient().getId(), empToAdd.getId() + "", myRow.myWeek.mySched.getInsertScheduleId(), false);
                        myQuery.add(myShift.getMyQuery());
                    }
                } catch (Exception ex) {
                }
            }
            try {
                parent.getConnection().executeQueryEx(myQuery);
            } catch (Exception exe) {
            }
            selectedShift.removeAllElements();
        } else {
            boolean moveMaster = false;
            String tempString = "Make change for this week only";
            String permString = "Make change for all weeks";
            Object[] options = {tempString, permString};
            if (myShift instanceof DShift) {
                if (myShift.isMaster()) {
                    int selectedOption = JOptionPane.showOptionDialog(Main_Window.parentOfApplication, "This is a master shift what would you like to do?", "Master shift move", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, tempString);
                    if (selectedOption == 1) {
                        moveMaster = true;
                    }
                }
                QueryGenerateShift myQShift = new QueryGenerateShift((DShift) myShift);
                myQShift.moveShift(getClient().getId(), empToAdd.getId() + "", myRow.myWeek.mySched.getInsertScheduleId(), moveMaster);
                myQuery.add(myQShift.getMyQuery());
                try {
                    parent.getConnection().executeUpdate(myQuery);
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * Drop METHODS
     */
    public void runOnDrop(Object obj, MouseEvent evt, BufferedImage bi) {

        if (canNotWorkThisShift()) {
            return;
        }
        SShift shift = null;
        SEmployee emp = null;
        RunQueriesEx myQuery;
        if (obj.getClass().equals(this.getClass())) {
            shift = (SShift) obj;
        } else if (obj.getClass().equals(new String().getClass()) && hasData) {
            myRow.myWeek.mySched.myParent.setNoteVisible(true, this);
        } else if (obj.getClass().equals(getEmployee().getClass())) {
            emp = (SEmployee) obj;
        } else if (obj.getClass().equals(myRow.myWeek.mySched.getClass())) {
            emp = ((SSchedule) obj).getEmployee();
        }
        if (emp != null) {
            if (checkAvailabilityBannedAndCerts(emp)) {
                boolean isTraining = false;
                addEmployeeToMultipleOrOneShift(emp);
            }
        } else if (shift != null && shift != this
                && (shift.getEmployee().getId() != getEmployee().getId()
                || getEmployee().getId() == 0) && (shift.getWeekNo() == getWeekNo()
                && shift.getDayCode() == getDayCode()
                && shift.getClient().getIdInt() == getClient().getIdInt()
                && (shift.getEmployee().getId() != getEmployee().getId()
                || shift.getEmployee().getId() == 0))) {
            if (checkAvailabilityBannedAndCerts(null)) {
                boolean moveForAllWeeks = false;
                if (shift.myShift instanceof DShift) {
                    if (checkIfOverPartTimeHours(getEmployee(), (DShift) shift.myShift)) {
                        QueryGenerateShift myMoveShift = new QueryGenerateShift((DShift) shift.myShift);
                        if (shift.myShift.isMaster()) {
                            String tempString = "Make change for this week only";
                            String permString = "Make change for all weeks";
                            Object[] options = {tempString, permString};
                            int selectedOption = JOptionPane.showOptionDialog(Main_Window.parentOfApplication, "This is a master shift what would you like to do?", "Master shift move", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, tempString);
                            if (selectedOption == 1) {
                                moveForAllWeeks = true;
                            }
                        }
                        String shiftId = shift.myShift.getShiftId();

                        myMoveShift.moveShift(getClient().getId(), getEmployee().getId() + "", myRow.myWeek.mySched.getInsertScheduleId(), moveForAllWeeks);
                        try {
                            myRow.myWeek.mySched.myParent.getConnection().executeQueryEx(myMoveShift.getMyQuery());
                        } catch (Exception e) {
                        }

                        try {
                            if (shift.getEmployee().getId() != 0
                                    && this.parent.getConnection().myCompany.equals("2") && !Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "Payroll")) {
                                EventLoggerInternalFrame myPanel = new EventLoggerInternalFrame(this.parent.getConnection().myCompany);

                                Main_Window.parentOfApplication.desktop.add(myPanel);
                                java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                                if (screenSize.width > 950 && screenSize.height > 509) {
                                    myPanel.setBounds((screenSize.width - 950) / 2, (screenSize.height - 509) / 2, 950, 509);
                                } else {
                                    myPanel.setBounds(screenSize.width / 2, (screenSize.height) / 2, screenSize.width, screenSize.height);
                                }
                                myPanel.loadDataForEmployee(shift.getEmployee().getId(), shiftId);
                                myPanel.setVisible(true);
                                myPanel.setSelected(true);
                            }
                        } catch (Exception exe) {
                        }
                    }
                }
            }
        }
    }

    public void highlightMe(boolean highlightMe, Object obj, MouseEvent evt) {
        SShift shift = null;
        SEmployee emp = null;

        try {
            shift = (SShift) obj;
        } catch (Exception e) {
            try {
                emp = (SEmployee) obj;
            } catch (Exception ex) {
                try {
                    emp = ((SSchedule) obj).getEmployee();
                } catch (Exception exe) {
                }
            }
        }
        if (highlightMe) {
            //Shift object is over us...yay...
            if (shift != null) {
                if ((getWeekNo() == shift.getWeekNo())
                        && (getDayCode() == shift.getDayCode())
                        && (getClient() == shift.getClient())
                        && (!canNotWorkThisShift())) {
                    setBackground(Color.green);
                } else {
                    setBackground(Color.red);
                }
            } else if (emp != null) {
                if (!this.myShift.isBlank()) {
                    setBackground(Color.green);
                } else {
                    setBackground(Color.red);
                }
            }

        } else {
            setBG();
        }
    }

    public void displayShiftsForAvail(MouseEvent event, SShift shift) {
        try {
            boolean onmask = (event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK;
            if (!onmask && event.getButton() == MouseEvent.BUTTON1) {
                parent.getAvail().acb.clearCurrentlySelectedShifts();
            }

            if (shift.myShift != null) {
                if (!shift.isSelected || !onmask) {
                    selectThisShift(true);
                } else if (onmask) {
                    selectThisShift(false);
                }
                parent.getAvail().acb.ChangeByAvailability(shift.myShift);
            }

            if (shift.myShift != null && !shift.isSelected) {
                selectThisShift(true);
                parent.getAvail().acb.ChangeByAvailability(shift.myShift);
            } else if (shift.myShift != null) {
            } else {
                parent.getAvail().acb.showAvailabilityForNoShift();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                AjaxSwingManager.endOperation();
            }
        }
    }

    /**
     * Drag Methods
     */
    /**
     * *************************************************************************
     * END DRAG AND DROP METHODS FROM DRAGANDDROP GRAPHICAL COMPS
     * ************************************************************************
     */
    private class displayAvailShiftsOnMousePressedThread extends Thread {

        MouseEvent event;
        SShift shift;
        private boolean killMe;

        public displayAvailShiftsOnMousePressedThread(MouseEvent evt, SShift shft) {
            event = evt;
            shift = shft;
            killMe = false;
        }

        public void run() {
            displayShiftsForAvail(event, shift);
        }

        public void killMe() {
            killMe = true;
            interrupt();
        }
    }

    /**
     * Method to delete all Selected shifts used when 'd' key is pressed or from
     * our popupmenu....fun fun
     */
    public void deleteAllSelectedShifts(boolean deletePermanent) {
        this.selectThisShift(true, false);
        myRow.myWeek.mySched.myParent.deleteSelectedShifts(deletePermanent);
    }

    /**
     * Method to confirm all Selected shifts used when 'c' key is pressed or
     * from our popupmenu
     */
    public void confirmAllSelectedShifts() {
        this.selectThisShift(true, false);
        myRow.myWeek.mySched.myParent.confirmSelectedShifts();
    }

    /**
     * Method to mark all Selected shifts as not conflicted....
     */
    public void setShiftToNotConflicted() {
        this.selectThisShift(true, false);
        myRow.myWeek.mySched.myParent.unConflictSelectedShifts();
    }

    public void runOnDrag(MouseEvent e) {
        Point temp = getMyPosition();
        JScrollPane sv = myRow.myWeek.mySched.myParent.getScrollPane();

        int x, y;

        x = temp.x - 30;
        y = e.getPoint().y + temp.y - 30;

        sv.scrollRectToVisible(new Rectangle((x), (y), ComponentDimensions.currentSizes.get("SShift").width, ComponentDimensions.currentSizes.get("SShift").height));

        calculateOffsets(MousePressedXLocation, MousePressedYLocation);
    }

    public int compareTo(Object o) {
        SShift comp = (SShift) o;
        if (!hasData && comp.hasData) {
            return 1;
        } else if (hasData && !comp.hasData) {
            return -1;
        } else if (!hasData && !comp.hasData) {
            return 0;
        }
        int value = myShift.getStartTime() - comp.myShift.getStartTime();
        if (value != 0) {
            return value;
        } else {
            return myShift.getEndTime() - comp.myShift.getEndTime();
        }
    }

    class mouseHandler extends MouseAdapter {

        SShift shift;
        long lastClick = 0;

        public mouseHandler(SShift s) {
            shift = s;
        }

        public void mouseClicked(MouseEvent event) {
            boolean isRightClick = event.getButton() == MouseEvent.BUTTON2
                    || event.getButton() == MouseEvent.BUTTON3;
            if (!event.isControlDown() || shift.myShift == null) {
                if (shift.myShift == null || !(isRightClick && shift.isSelected)) {
                    clearAllSelectedShifts();
                }
            }

            try {
                if ((event.getButton() == event.BUTTON2 || event.getButton() == event.BUTTON3) && hasData()) {

                    JPopupMenu myMenu = Schedule_Main_Form.getMouseMenuForSShift(shift);
                    if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
                        myMenu.show(shift, event.getX(), event.getY());
                    }
                    return;
                }
                parent.setCurrentSelectedWeek(getWeekNo());
                if (event.getClickCount() > 1) {
                    if (checkAvailabilityBannedAndCerts(getEmployee())) {
//                        try {
//                            if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_RECONCILED) && !Main_Window.parentOfApplication.isUserAMemberOfGroups(parent.getConnection(), "ADMIN", "Payroll")) {
//                                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Shift has been reconciled. This schedule must be unreconciled before \n you can edit this shift!", "Shift Reconciled!", JOptionPane.OK_OPTION);
//                                return;
//                            }
//                        } catch (Exception e) {
//                        }

                        if (canNotWorkThisShift()) {
                            return;
                        }
                        if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
                            if (shift.myShift instanceof DShift || shift.myShift == null) {
                                parent.editShift(shift);
                            }
                        }
                        return;
                    }
                }
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    displayShiftsForAvail(event, shift);
                } else {
                    Main_Window.parentOfApplication.getThreadPool().execute(new displayAvailShiftsOnMousePressedThread(event, shift));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    AjaxSwingManager.endOperation();
                }
            }
        }

        public void mousePressed(MouseEvent event) {
            try {
                MousePressedXLocation = event.getX();
                MousePressedYLocation = event.getY();
//                if (myShift.getType().isShiftType(ShiftTypeClass.SHIFT_RECONCILED) && !Main_Window.parentOfApplication.isUserAMemberOfGroups(parent.getConnection(), "ADMIN", "Payroll")) {
//                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Shift has been reconciled. This schedule must be unreconciled before \n you can edit this shift!", "Shift Reconciled!", JOptionPane.OK_OPTION);
//                    return;
//                }
            } catch (Exception e) {
            }
        }
    }

    private void setBorder(javax.swing.border.AbstractBorder myBorder) {
        setBG();
        repaint();
        super.setBorder(myBorder);
    }

    public class shiftBackgroundCharacteristics {

        private Color myColor;
        private Icon myIcon;
        private boolean hasChanged;
        private Icon landIcon;
        private boolean displayLandIconInstead;
        private boolean alphaA;

        public shiftBackgroundCharacteristics() {
            clearMe();
            alphaA = false;
        }

        public void setLandIcon(boolean canLand) {
            displayLandIconInstead = canLand;
            hasChanged = true;
        }

        public void setCharacteristics(Color newColor) {
            if (!newColor.equals(myColor)) {
                myColor = newColor;
                hasChanged = true;
            }
        }

        public void clearMe() {
            hasChanged = false;
            myIcon = null;
            myColor = Schedule_View_Panel.master_color;
            landIcon = parent.goodDropIcon;
            displayLandIconInstead = false;
        }

        public boolean hasChanged() {
            return hasChanged;
        }

        public void clearChanged() {
            hasChanged = false;
        }

        public void setCharacteristics(Icon newIcon) {
            if (myIcon != newIcon) {
                myIcon = newIcon;
                hasChanged = true;
            }
        }

        /**
         * Allows alpha or not G.... for filters kinda just built in
         */
        public void setAlphaFactor(boolean isAlpha, int alphaAmount) {
            alphaA = isAlpha;
            setCharacteristics(myColor);
        }

        public void setCharacteristics(Color newColor, Icon newIcon) {
            setCharacteristics(newColor);
            setCharacteristics(newIcon);
            if (AjaxSwingManager.isAjaxSwingRunning() && this.hasChanged && parent.isDoneLoadingCompletely) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        repaint();
                        ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObject, true);
                    }
                });

            }
        }

        public Color getColor() {
            return myColor;
        }

        public Icon getIcon() {
            if (!displayLandIconInstead || !parent.getPuzzlePeicesShown()) {
                return myIcon;
            } else {
                return landIcon;
            }
        }
    }

    /**
     * Used to create Note if needed...
     */
    public class shiftNotesLabel extends myNoteIconLabel implements ZoomListener {

        public shiftNotesLabel(String lab) {
            super(myRow.myWeek.mySched.myParent);
            ComponentDimensions.addZoomListener(this);
        }

        public boolean hasNote() {
            if (!hasData()) {
                return false;
            } else if (myShift.hasNote()) {
                return true;
            }
            return false;
        }

        public ArrayList<NoteData> getNotes() {
            if (myShift instanceof DShift) {
                return getNotesForShift(myShift.getShiftId());
            } else if (myShift instanceof DAvailability) {
                DAvailability myAvail = (DAvailability) myShift;
                return getAvailNotes(myAvail.getAvailId());
            }
            return new ArrayList<NoteData>();
        }

        public void zoomPerformed() {
        }

        public void zoomFinished() {
            this.setBounds(ComponentDimensions.currentSizes.get("SShift").width - 17, 1, 16, 16);
        }
    }
}
