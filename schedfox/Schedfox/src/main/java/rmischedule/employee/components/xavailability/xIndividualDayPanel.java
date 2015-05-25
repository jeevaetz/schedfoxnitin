/*
 * xIndividualDayPanel.java
 *
 * Created on August 17, 2005, 6:39 PM
 */

package rmischedule.employee.components.xavailability;

import java.awt.*;
import javax.swing.*;
import java.util.Calendar;
import java.awt.event.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import rmischedule.main.*;

import rmischedule.components.graphicalcomponents.*;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.AvailabilityInterface;
import schedfoxlib.model.TimeOffCalc;
/**
 *
 * @author  Ira
 */
public class xIndividualDayPanel extends JPanel implements DragAndDropContainer {
    
    private boolean isActive;
    private boolean isPast;
    private int dayOfWeek;
    private String databaseDate = new String();
    private String regDate = new String();
    private String dayOfMonth = new String();
    private Calendar myDate;
    private xFormInterface myParent;
    private xIndividualDayPanel thisObject;
    private myTextImageClass activeIcon;
    private AvailabilityInterface avail;

    private static final Color myDefaultColor = new Color(205,205,255);
    private static final Color myPastDefaultColor = new Color(180, 180, 235);
    private static final Color myHighlightColor = new Color(230, 230, 255);
    private static final Color myNonAvailColor = new Color(250, 199, 195);
    
    private static final Font myDateFont = new Font("Dialog", Font.ITALIC, 12);
    private static final Font myTimeFont = new Font("Dialog", Font.BOLD, 12);
    
    private static int sizeVertical = 15;
    private static int sizeHorizontal = 5;
    
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor for this with non of that pansy drag and drop stuff...
     */
    public xIndividualDayPanel() {
        super();
        init(null);
    }
    
    /** Creates new form xIndividualDayPanel */
    public xIndividualDayPanel(int dow, xFormInterface myParentForm) {
        isActive = true;
        myParent = myParentForm;
        dayOfWeek = dow;
        init(null);
    }
    
    public xIndividualDayPanel(boolean val, Calendar myDay, xFormInterface myParentForm) {
        myParent = myParentForm;
        isActive = val;
        init(myDay);
    }
    
    /**
     * Method in common to do our stuff
     */ 
    public void init(Calendar myDay) {
        initComponents();
        activeIcon = new myTextImageClass();
        thisObject = this;
        if (!isActive) {
            setBackground(Color.WHITE);
            setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY, 1, false));
        } else {
            setBackground(myDefaultColor);
            this.addMouseListener(new myPopUpMouseListener());
        }
        if (myDay != null) {
            databaseDate = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(myDay);
            //myDay.roll(Calendar.MONTH, 1);
            regDate = StaticDateTimeFunctions.convertCalendarToReadableFormat(myDay);
            //myDay.roll(Calendar.MONTH, -1);
            dayOfMonth = myDay.get(Calendar.DAY_OF_MONTH) + "";
            dayOfWeek = myDay.get(Calendar.DAY_OF_WEEK);
        }
        myDate = myDay;
        addMouseListener(new myPopUpMouseListener());
    }

    public void setToPast(boolean isMyPast) {
        isPast = isMyPast;
        setBackground(myPastDefaultColor);
    }
    
    /**
     * Creamtec mod
     * @return
     */
    public boolean getIsActive() {
        return this.isActive;
    }

    /**
     * Creamtec mod
     * @return
     */
    public myTextImageClass getActiveIcon () {
        return this.activeIcon;
    }

    public String getDayOfMonth() {
        return this.dayOfMonth;
    }

    public Icon getDisplayIcon() {
        return activeIcon.getIcon();
    }

    /**
     * Gets Calendar
     */
    public Calendar getMyCal() {
        return myDate;
    }
    
    /**
     * Set this shift to Non Avail?
     */
    public void setNonAvail(boolean isNonAvail) {
        if (isNonAvail) {
            setBackground(myNonAvailColor);
        } else {
            if (isPast) {
                setBackground(myPastDefaultColor);
            } else {
                setBackground(myDefaultColor);
            }
        }
    }
    
    public String getDayOfWeek() {
        return dayOfWeek + "";
    }
    
    /**
     * Checks background to see if employee is available or not...Background is good
     * what you see is what you get philosophy
     */
    public boolean isAvail() {
        if (getBackground() == myDefaultColor || 
            getBackground() == myHighlightColor ||
            getBackground() == myPastDefaultColor) {
            return true;
        }
        return false;
    }
    
    /**
     * Override the damn paint Component to display our stuff correctly...
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isActive) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        if (activeIcon.getFadedImage() != null) {
            g.drawImage(activeIcon.getFadedImage().getImage(), 3, 1, getBounds().width - 6, getBounds().height - 2, 0, 0, activeIcon.getIcon().getIconWidth(),activeIcon.getIcon().getIconHeight(), this);
        }
        try {
            if (!isAvail() && this.isActive && avail.hasNote()) {
                iconLabel.setIcon(Main_Window.Note16x16);
            }
        } catch (Exception e) {}
        g.setFont(myDateFont);
        g.drawString(dayOfMonth, getBounds().width - new Double(sizeHorizontal + g.getFontMetrics().getStringBounds(dayOfMonth, g).getWidth()).intValue(), sizeVertical);
        g.setFont(myTimeFont);
        g.drawString(activeIcon.getStart(), sizeHorizontal, sizeVertical);
        g.drawString(activeIcon.getEnd(), sizeHorizontal, getBounds().height - 5);
    }
    
    /**
     * Sets the start and end time for our unavailability, Pass in nulls to clear, myShiftId
     * is the Avail id from the database, negative if from master maybe?
     */
    public void setStartEndTimes(int type, String dbasestart, String dbaseend,
            String myShiftId, String isdeleted) {
        if (dbasestart == null || dbaseend == null || isdeleted.equals("1")) {
            activeIcon.clear();
            setNonAvail(false);
        } else {
            activeIcon.setImageStartAndEnd(type, dbasestart, dbaseend, myShiftId);
        }
    }
    
    /**
     * Sets the availability information for this date.
     * @param avail
     */
    public void setAvailability(AvailabilityInterface avail) {
        if (avail.isIsDeleted()) {
            activeIcon.clear();
            setNonAvail(false);
        } else {
            if (avail.getAvailType() != Main_Window.AVAILABLE) {
                this.avail = avail;
            }
            activeIcon.setImageStartAndEnd(avail.getAvailType(), avail.getStartTime() + "",
                    avail.getEndTime() + "", avail.getAvailIdStr());
        }
    }

    /**
     * Returns the availability 
     * @return
     */
    public AvailabilityInterface getAvailability() {
        return this.avail;
    }

    public void clear() {
        this.activeIcon = new myTextImageClass();
    }
    
    /**
     * gets Shift Id set
     */
    public String getShiftId() {
        return activeIcon.getMyId();
    }
    
    /**
     * Gets the Shift Type refer to Main_Window statics to see what the int means
     */
    public int getShiftType() {
        return activeIcon.getType();
    }
    
    /**
     * Gets start time in dbase value, ie 600 instead of 1000 or 1000 AM
     */
    public String getStartDBase() {
        return activeIcon.getDBaseStart();
    }

    public double getHoursCompensated() {
        return activeIcon.getHoursCompensated();
    }

    public double getDaysAccrued() {
        return activeIcon.getDaysAccrued();
    }
    
    /**
     * Gets end time in dbase value, ie 600 instead of 1000 or 1000 AM
     */
    public String getEndDBase() {
        return activeIcon.getDBaseEnd();
    }
    
    /**
     * Gets readable date
     */
    public String getMyReadDate() {
        return regDate;
    }
    
    /**
     * Inherieted method from our container thingy...
     */
    public void runOnDrop(Object objectToPassIn, MouseEvent evt, BufferedImage bi) {
        if (objectToPassIn instanceof Integer) {
            Integer myVal = (Integer)objectToPassIn;
            if (isActive) {
                activeIcon.setImageStartAndEnd(myVal, "0", "1440", getShiftId());
            }
            this.myParent.getMainAvailPanel().refreshCalculations();
        } else if (objectToPassIn instanceof String && objectToPassIn.equals("Note Class") && !isAvail()) {
            int availId = this.getAvailability().getAvailabilityId();
            if (this.getAvailability().isIsMaster()) {
                availId = availId * -1;
            }
            xAvailNotes myNoteDisplay = new xAvailNotes(availId, myParent.getMyParent());
            myParent.getLayeredPane().add(myNoteDisplay);
            myParent.getLayeredPane().setLayer(myNoteDisplay, JLayeredPane.MODAL_LAYER);

            Rectangle mybounds = new Rectangle(new Double((myParent.getContentPanel().getWidth() - 280) / 2).intValue(), new Double((myParent.getContentPanel().getHeight() - 190) / 2).intValue(), 280, 190);
            myNoteDisplay.setBounds(mybounds);
        }
    }
    
    /**
     * Inherieted method from our container thingy...
     */
    public void highlightMe(boolean highlightMe, Object myObj, MouseEvent evt) {
        if (!isActive || !isAvail()) {
            return;
        }
        try {
            String myString = (String)myObj;
            if (highlightMe) {
                setBackground(myHighlightColor);
            } else {
                if (isPast) {
                    setBackground(myPastDefaultColor);
                } else {
                    setBackground(myDefaultColor);
                }
            }
            
        } catch (Exception e) {}
    }
    
    /**
     * Returns database format date ie: 08-09-2005
     */
    public String getDBaseDate() {
        return databaseDate;
    }
    
    /**
     * Small class used to display our Mouse Pop up menu if mouse clicked not left
     * button.
     */
    private class myPopUpMouseListener extends MouseAdapter {
        public myPopUpMouseListener() {
            
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getButton() == evt.BUTTON2 || evt.getButton() == evt.BUTTON3 &&
                    (!isPast || Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User"))) {
                myParent.setActiveDayForMenu(thisObject);
                myParent.getMyMenu().show(thisObject, evt.getX(), evt.getY());
            } else {
                if (myParent != null && isActive &&
                        (!isPast || Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User", "Scheduling Manager"))) {
                    myParent.runOnClickDay(thisObject);
                }
            }
        }
        
    }
    
    /**
     * Small class to ensure that the faded image and icon are always synched together
     */
    public class myTextImageClass {
        private ImageIcon myIcon;
        private ImageIcon myFadedImage;
        private String start_time;
        private String end_time;
        private String startDBase;
        private String endDBase;
        private String myId;
        private int myType;
        private double hoursCompensatedDouble;
        private int daysAccrued;
        
        public myTextImageClass() {
            myIcon = null;
            myFadedImage = null;
            start_time = new String();
            end_time = new String();
            startDBase = new String();
            endDBase = new String();
            myId = "0";
            myType = Main_Window.AVAILABLE;
        }
        
        public void setImageStartAndEnd(AvailabilityInterface avail) {
            this.setImageStartAndEnd(avail.getAvailType(), avail.getStartTime() + "",
                    avail.getEndTime() + "", avail.getAvailIdStr());
        }

        public void setImageStartAndEnd(int newType, String start, String end, String id) {
            myId = id;
            myType = newType;
            double hoursCompensated = 0;
            if (myParent instanceof xTempAvailabilityPanel) {
                xTempAvailabilityPanel tempAvail = (xTempAvailabilityPanel)myParent;
                ArrayList<TimeOffCalc> timeOffCalcs = tempAvail.getTimeOffCalcsForType(newType);
                if (timeOffCalcs != null && timeOffCalcs.size() > 0) {
                    for (int t = 0; t < timeOffCalcs.size(); t++) {
                        if (!timeOffCalcs.get(t).isIsExpired() && hoursCompensated == 0) {
                            try {
                                hoursCompensated = timeOffCalcs.get(t).getHoursAccrued().doubleValue();
                            } catch (Exception exe) {}
                            String[] datesTaken =  timeOffCalcs.get(t).getDatesTaken().split(",");
                            int numDatesTaken = 0;
                            for (int d = 0; d < datesTaken.length; d++) {
                                numDatesTaken += (datesTaken[d].trim().length() > 0 ? 1 : 0);
                            }
                            this.setDaysAccrued(timeOffCalcs.get(t).getDaysAccrued() - numDatesTaken);
                        }
                    }
                    this.hoursCompensatedDouble = hoursCompensated;
                }
            }
            if (newType == Main_Window.NON_AVAILABLE) {
                myFadedImage = Main_Window.Generic_NA_Icon_Faded;
                myIcon = Main_Window.Generic_NA_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE_PERSONAL) {
                myFadedImage = Main_Window.Personal_Icon_Faded;
                myIcon = Main_Window.Personal_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE_HALF_PERSONAL) {
                myFadedImage = Main_Window.Personal_Half_Icon_Faded;
                myIcon = Main_Window.Personal_Half_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE_SICK) {
                myFadedImage = Main_Window.Sic_Icon_Faded;
                myIcon = Main_Window.Sic_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE_VACATION) {
                myFadedImage = Main_Window.Vac_Icon_Faded;
                myIcon = Main_Window.Vac_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE_HALF_VACATION) {
                myFadedImage = Main_Window.Vac_Half_Icon_Faded;
                myIcon = Main_Window.Vac_Half_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE) {
                myFadedImage = Main_Window.Na_Icon_Faded;
                myIcon = Main_Window.Generic_NA_Icon;
            } else if (newType == Main_Window.NON_AVAILABLE_MILITARY) {
                myFadedImage = Main_Window.Military_Icon_Faded;
                myIcon = Main_Window.Military_Icon;
            } else {
                myFadedImage = null;
                myIcon = null;
                myType = Main_Window.AVAILABLE;
            }
            startDBase = start;
            endDBase = end;
            if (newType == Main_Window.NON_AVAILABLE ||
                    ((!start.equals("0") && !start.equals("1440")) ||
                    (!end.equals("1440") && !end.equals("0")) && newType != Main_Window.AVAILABLE)) {
                start_time = StaticDateTimeFunctions.stringToFormattedTime(start, Main_Window.parentOfApplication.is12HourFormat());
                end_time = StaticDateTimeFunctions.stringToFormattedTime(end, Main_Window.parentOfApplication.is12HourFormat());
            } else {
                start_time = "";
                end_time = "";
            }
            if (newType != Main_Window.AVAILABLE) {
                setNonAvail(true);
            } else {
                setNonAvail(false);
            }
            if (!myParent.isGettingData()) {
                myId = myParent.saveDayInformation(thisObject);
            }
            if (start == null || end == null) {
                start_time = "";
                end_time = "";
                if (!myParent.isGettingData()) {
                    myParent.deletePermenantDayInformation(thisObject);
                }
            }
            repaint();
        }
        
        public String getMyId() {
            return myId;
        }
        
        public String getDBaseStart() {
            if (startDBase.trim().length() > 0) {
                return startDBase;
            }
            return "0";
        }
        
        public int getType() {
            return myType;
        }
        
        /**
         * Returns database format end, or 1440 == 2400
         */
        public String getDBaseEnd() {
            if (endDBase.trim().length() > 0) {
                return endDBase;
            } else {
                return "1440";
            }
        }
        
        public String getStart() {
            if (start_time != null) {
                return start_time;
            } else {
                return "";
            }
        }
        
        public String getEnd() {
            if (end_time != null) {
                return end_time;
            } else {
                return "";
            }
        }
        
        public void clear() {
            myFadedImage = null;
            myIcon = null;
            start_time = null;
            end_time = null;
            startDBase = new String();
            endDBase = new String();
        }
        
        public ImageIcon getFadedImage() {
            return myFadedImage;
        }
        
        public ImageIcon getIcon() {
            return myIcon;
        }

        /**
         * @return the hoursCompensatedDouble
         */
        public double getHoursCompensated() {
            return hoursCompensatedDouble;
        }

        /**
         * @param hoursCompensatedDouble the hoursCompensatedDouble to set
         */
        public void setHoursCompensated(double hoursCompensatedDouble) {
            this.hoursCompensatedDouble = hoursCompensatedDouble;
        }

        /**
         * @return the daysAccrued
         */
        public int getDaysAccrued() {
            return daysAccrued;
        }

        /**
         * @param daysAccrued the daysAccrued to set
         */
        public void setDaysAccrued(int daysAccrued) {
            this.daysAccrued = daysAccrued;
        }
    }
    
    /**
     * Used for permanent Availability to see if the dow provided matches with the one this class
     * has...
     */
    public boolean equalsDow(int dow) {
        if (dayOfWeek == dow && myDate == null) {
            return true;
        }
        return false;
    }
    
    /**
     * Used for temporary Availability to see if the doy provided matches with the one this class has...
     */
    public boolean equalsDoy(String doy) {
        if (databaseDate.equals(doy)) {
            return true;
        }
        return false;
    }

    public boolean equalsDoy(Date doy) {
        try {
            if (databaseDate.equals(this.dateFormatter.format(doy))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iconLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(205, 205, 255));
        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMaximumSize(new java.awt.Dimension(200, 200));
        setMinimumSize(new java.awt.Dimension(50, 50));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        iconLabel.setMaximumSize(new java.awt.Dimension(17, 17));
        iconLabel.setMinimumSize(new java.awt.Dimension(17, 17));
        iconLabel.setPreferredSize(new java.awt.Dimension(17, 17));
        add(iconLabel, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        if (activeIcon.getFadedImage() != null) {
            myParent.displayAvailStats(this);
        } else {
            myParent.hideAvailStats();
        }
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        myParent.hideAvailStats();
    }//GEN-LAST:event_formMouseExited
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel iconLabel;
    // End of variables declaration//GEN-END:variables
    
}
