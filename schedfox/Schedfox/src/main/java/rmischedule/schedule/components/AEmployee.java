/*
 * AEmployee.java
 *
 * Created on July 7, 2004, 12:00 PM
 */

package rmischedule.schedule.components;

import schedfoxlib.model.util.Record_Set;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import rmischedule.security.*;
import rmischedule.main.*;
import rmischedule.components.graphicalcomponents.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import schedfoxlib.model.Company;
import schedfoxlib.model.util.ImageLoader;
import rmischedule.schedule.Schedule_View_Panel;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.data_connection_types.*;
import rmischedule.schedule.components.notes.*;
/**
 * 
 * @author  jason.allen
 *
 * Employee for the Avaylability list
 *
 */

public class AEmployee extends PrettyButton implements DragAndDropContainer {
    
    public SEmployee myEmployee;
    private Schedule_View_Panel myParent;
    private boolean dragging;
    private SShift LastHover;
    private Color normColor;
    public Calendar currDate = Calendar.getInstance();
    private boolean hasNote;
    private myEmployeeNoteLabel myNoteLabel;

    private static int width = 225;
    
    public AEmployee() {
        super(null, null, null);
    }
    
    /** Creates a new instance of AEmployee */
    public AEmployee(SEmployee se, Schedule_View_Panel p){
        super(p.getLayeredPane(), p.FloatContainerPanel, se);
        myEmployee = se;   
        hasNote = se.hasNote();
        myParent = p;
        myNoteLabel = new myEmployeeNoteLabel("");

        putClientProperty(AjaxSwingProperties.COMPONENT_CUSTOM_TOOL_TIP, "true");
//        this.setToolTipText("here");

        myNoteLabel.setOpaque(false);
        super.ContentPanel.add(myNoteLabel);
        if (!Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
            super.setDragEnabled(false);
        }
        displayName();
        myEmployee.setAEmployee(this);
        setColorBlue(true);
        try {
            setEmployeeLabelToAvailabilityColors();
        } catch (Exception e) {}
        this.setMaximumSize(new Dimension(600, 20));
        this.setMinimumSize(new Dimension(0, 20));
        this.setPreferredSize(new Dimension(100, 20));
        this.addMouseListener(new myMouseAdapter(this));
        setMyFont(new Font("Dialog", Font.BOLD,11));
        if (se.hasNote()) {
            myNoteLabel.setIcon(Main_Window.Note16x16);
        }
        this.setToolTip();
    }

    public void setToolTip() {
        StringBuffer toolTip = new StringBuffer();
        Company comp = Main_Window.parentOfApplication.getCompanyById(myParent.getCompany());
        String imageLocation = "";
        try {
            imageLocation = ImageLoader.getImageURL(myEmployee.getId() + ".jpg", comp.getDB(), "employee");

        } catch (Exception e) {}

        Color borderColor = getMyBackground().darker();
        String colorStr = "rgb(" + borderColor.getRed() + "," + borderColor.getGreen() + "," + borderColor.getBlue() + ")";

        toolTip.append("<html>");
        toolTip.append("<div style=\"border: 5px solid " + colorStr + "; width:" + width + "px;\">");
        toolTip.append("<table>");
        toolTip.append("<tr>");
        toolTip.append("<th align=\"left\" colspan=\"3\">");
        toolTip.append(myEmployee.getName());
        toolTip.append("</th>");
        toolTip.append("</tr>");

        toolTip.append("<tr>");
        toolTip.append("<td rowspan=\"4\">");
        toolTip.append("<img width=75 height=75 src=\"" + imageLocation + "\"/>");
        toolTip.append("</td>");
        toolTip.append("<th align=\"left\">");
        toolTip.append("Address");
        toolTip.append("</th>");
        toolTip.append("</tr>");

        toolTip.append("<tr>");
        toolTip.append("<td>");
        toolTip.append(myEmployee.getAddress());
        toolTip.append("</td>");
        toolTip.append("</tr>");

        if (myEmployee.getAddress2() != null && myEmployee.getAddress2().length() > 2) {
            toolTip.append("<tr>");
            toolTip.append("<td>");
            toolTip.append("");
            toolTip.append("</td>");
            toolTip.append("<td>");
            toolTip.append(myEmployee.getAddress2());
            toolTip.append("</td>");
            toolTip.append("</tr>");
        }

        toolTip.append("<tr>");
        toolTip.append("<td>");
        toolTip.append(myEmployee.getCity() + ", " + myEmployee.getState() + " " + myEmployee.getZip());
        toolTip.append("</td>");
        toolTip.append("</tr>");

        toolTip.append("<tr>");
        toolTip.append("<th colspan=\"3\" align=\"left\">");
        toolTip.append("Contact Information");
        toolTip.append("</th>");
        toolTip.append("</tr>");

        if (myEmployee.getPhone() != null && myEmployee.getPhone().trim().length() > 4) {
            toolTip.append("<tr>");
            toolTip.append("<td colspan=\"3\">");
            toolTip.append(myEmployee.getPhone());
            toolTip.append("</td>");
            toolTip.append("</tr>");
        }

        if (myEmployee.getPhone2() != null && myEmployee.getPhone2().trim().length() > 4) {
            toolTip.append("<tr>");
            toolTip.append("<td colspan=\"3\">");
            toolTip.append(myEmployee.getPhone2());
            toolTip.append("</td>");
            toolTip.append("</tr>");
        }

        if (myEmployee.getCell() != null && myEmployee.getCell().trim().length() > 4) {
            toolTip.append("<tr>");
            toolTip.append("<td colspan=\"3\">");
            toolTip.append(myEmployee.getCell());
            toolTip.append("</td>");
            toolTip.append("</tr>");
        }

        toolTip.append("<tr>");
        toolTip.append("<th colspan=\"3\" align=\"left\">");
        toolTip.append("Hours Worked (" + myParent.getCurrentSelectedWeekStart() + " - " + myParent.getCurrentSelectedWeekEnd() + ")");
        toolTip.append("</th>");
        toolTip.append("</tr>");
        
        toolTip.append("<tr>");
        toolTip.append("<td colspan=\"3\">");
        toolTip.append("Hours Selected Week: " + myEmployee.getHoursWorkedForWeek(myParent.getCurrentSelectedWeek()));
        toolTip.append("</td>");
        toolTip.append("</tr>");

        toolTip.append("</table>");
        if (AjaxSwingManager.isAjaxSwingRunning()) {
        	this.setToolTipText(toolTip.toString());
        }
    }

//
//    public JToolTip createToolTip() {
//        return(new ToolTipEmployeeInformation(this.myEmployee));
//    }

    public Point getToolTipLocation(MouseEvent event) {
        return new Point(-ToolTipEmployeeInformation.myWidth - 15, -1);
    }

    public String getDisplayName() {
        String name = myEmployee.getName();
        int numOfDays = Main_Window.parentOfApplication.getNumberOfDaysNewEmployee();
        if (name.length() > 18) {
            name = myEmployee.getName().substring(0, 18);
        }
        if (myEmployee.checkIfEmployeeHiredWithinLastXDays(numOfDays)) {
            name = " " + name + " (NEW)";
        }
        return name;
    }

    public void displayName() {
        setText(" " + getDisplayName());
    }
    
    public boolean hasNote() {
        return hasNote;
    }
    
    public void setVisible(boolean val) {
        super.setVisible(val);
    }

    public int getColorIndex() {
        double hours = myEmployee.getHoursWorkedForWeek(myParent.getCurrentSelectedWeek());
        int val = 0;
        if (hours < 32.0) {
            val = PrettyButton.blue;
        } else if (hours < 40.0) {
            val = PrettyButton.yellow;
        } else {
            val = PrettyButton.red;
        }
        double miles = (Math.round(this.myEmployee.getTravelDistance() * 0.000621371 * 10) / 10.0);
        if (miles > 50.0) {
            val = PrettyButton.grey;
        }
        return val;
    }

    public void setEmployeeLabelToAvailabilityColors() {
        setColorBlue(this.getColorIndex());
    }
    
    public Point getMyLocation(){
        int x, y;
        x = myParent.getScrollPane().getWidth();
        y =   myParent.getScrollPane().getHeight() 
            - myParent.getAvail().js.getHeight() 
            - myParent.getAvail().js.getVerticalScrollBar().getValue() 
            + getY() 
            + 1 + 30;
        
        return new Point(x,y);
    }
    
    public void mouseEnter() {
        super.MouseEntered();
        if(!dragging){
            myParent.getEmpInfo().show(this, getMyBackground(), getPanelBackground());
        }
    }
    
    /**
     * Method from DragAndDropContainer...
     */
    public void highlightMe(boolean highlight, Object val, MouseEvent evt) {
        if (highlight) {
            normColor = getBackground();
            setBackground(Color.white);
        } else {
            setBackground(Color.white);
        }
    }
    
    /**
     * Method from DragAndDropContainer...
     */
    public void runOnDrop(Object myObj, MouseEvent evt, BufferedImage bi) {
        SShift myShift = (SShift)myObj;
        if (myShift != null) {
            myShift.addEmployeeToMultipleOrOneShift(myEmployee);
        }
    }
    
    private class myMouseAdapter extends MouseAdapter {
        private AEmployee Parent;
        
        public myMouseAdapter(AEmployee parent) {
            Parent = parent;
        }
        
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                myParent.editEmployee(myEmployee.getId() + "");
            }
        }
        
        public void mouseEntered(MouseEvent e) {
            mouseEnter();
            myParent.AEinfo.hide(false);
        }
        
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            MouseExited();
            myParent.AEinfo.hide(true);
        }
        
    }
    
    /**
     * Used to create Note ToolTip if needed...
     */
    private class myEmployeeNoteLabel extends myNoteIconLabel {
        public myEmployeeNoteLabel(String lab) {
            super(myParent);
        }
        
        public boolean hasNote() {
            return myEmployee.hasNote();
        }
        
        public ArrayList<NoteData> getNotes() {
            get_employee_notes_query myQuery = new get_employee_notes_query();
            myQuery.update(myEmployee.getId() + "");
            Record_Set rs = new Record_Set();
            ArrayList<NoteData> retVal = new ArrayList<NoteData>();
            try {
                rs = myParent.getConnection().executeQuery(myQuery);
                for (int r = 0; r < rs.length(); r++) {
                    NoteData note = new NoteData();
                    note.setDate(rs.getDate("notes_date_time"));
                    note.setNote(rs.getString("notes"));
                    note.setNoteType(rs.getString("note_type_name"));
                    note.setUserLogin(rs.getString("user_login"));
                    retVal.add(note);
                    rs.moveNext();
                }
                return retVal;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

