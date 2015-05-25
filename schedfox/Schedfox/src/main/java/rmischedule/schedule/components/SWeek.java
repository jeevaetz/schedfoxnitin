/*
 * SWeek.java
 *
 * Created on April 14, 2004, 12:43 PM
 */
package rmischedule.schedule.components;

import javax.swing.*;
import java.util.*;
import rmischedule.schedule.schedulesizes.ZoomablePanel;
import rmischeduleserver.mysqlconnectivity.queries.reports.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.data_connection.Connection;
import rmischedule.ireports.viewer.IReportViewer;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;

import rmischedule.schedule.print.employeereports.*;
import rmischeduleserver.util.xprint.xPrintData;
import rmischedule.main.Main_Window;
import schedfoxlib.model.Company;
import rmischedule.schedule.schedulesizes.ComponentDimensions;
import schedfoxlib.model.Branch;
import schedfoxlib.model.ShiftTypeClass;

/**
 *
 * @author jason.allen
 */
public class SWeek extends JPanel {

    public Vector<SRow> Rows;
    private RowTotal pnTotal;    //Panel that contains totals on right side of each week
    public JPanel rowContainer; //Panel that contains rows stacked on top of each other...
    public SSchedule mySched;
    public int week_no;
    public double total;
    public double[] dayTotal;

    private SWeek thisObject;

    public SWeek(int weekNo, SSchedule s) {
        mySched = s;
        thisObject = this;
        week_no = weekNo;
        rowContainer = new JPanel();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        Rows = new Vector(10);
        Rows.add(new SRow(this));

        dayTotal = new double[7];

        setOpaque(true);
        setEnabled(true);

        pnTotal = new RowTotal("", JLabel.CENTER, this);
        pnTotal.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(rowContainer);
        add(pnTotal);
        rowContainer.setLayout(new BoxLayout(rowContainer, BoxLayout.Y_AXIS));
    }

    public boolean isWeekInPast() {
        try {
            return Rows.get(0).getShift(6).isPast();
        } catch (Exception exe) {}
        return false;
    }
    
    public void calTotals() {
        int i, c, d;
        c = Rows.size();
        dayTotal = new double[7];
        for (i = 0; i < c; i++) {
            for (d = 0; d < 7; d++) {
                if (Rows.get(i).getSShift(d).myShift != null) {
                    dayTotal[d] += Rows.get(i).getSShift(d).myShift.getNoHoursDouble();
                }
            }
        }

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
        for (int r = 0; r < Rows.size(); r++) {
            Rows.get(r).dispose();
        }
        Rows = null;
        pnTotal = null;
        rowContainer = null;
        mySched = null;
        thisObject = null;
        this.removeAll();
    }

    public SSchedule getSchedule() {
        return this.mySched;
    }

    public void addTotal(double newTotal) {
        total += newTotal;
        double d = java.lang.StrictMath.rint(total * 100) / 100;
        if (pnTotal != null) {
            pnTotal.setText(String.valueOf(d));

        }
    }

    public int getRowSize() {
        return Rows.size();
    }

    public int getUsedRowSize() {
        for (int i = 0; i < Rows.size(); i++) {
            if (Rows.get(i).isBlank()) {
                if (i == 0) {
                    return 1;
                } else {
                    return i;
                }
            }
        }
        return Rows.size();
    }

    public SRow getRow(int i) {
        return Rows.get(i);
    }

    public synchronized void consolidateRowsAtDay(int day) {
        int size = Rows.size();
        Vector<SShift> myArrayOfSShift = new Vector();
        getSShiftsForDayAndFindBlankRow(day, myArrayOfSShift);
        Collections.sort(myArrayOfSShift);
        for (int i = 0; i < size; i++) {
            try {
                Rows.get(i).addSShift(myArrayOfSShift.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        this.revalidate();
        mySched.balanceWeeks();
        mySched.repaint();
    }

    public double getDayTotal(int day) {
        return dayTotal[day];
    }

    /**
     * Puts all SShifts in Rows defined by day, into a Vector and returns true
     * if a blank row is present and false otherwise...
     */
    private boolean getSShiftsForDayAndFindBlankRow(int day, Vector myArrayOfSShift) {
        int size = Rows.size();
        boolean hasBlankRow = false;
        for (int i = 0; i < size; i++) {
            myArrayOfSShift.add(Rows.get(i).getShift(day));
            if (!Rows.get(i).getShift(day).hasData()) {
                hasBlankRow = true;
            }
        }
        return hasBlankRow;
    }

    public boolean areRowsAtDayFull(int pos, ArrayList<SShift> myArrayOfSShift) {
        int size = Rows.size();
        boolean retVal = true;
        for (int i = 0; i < size; i++) {
            SShift currentDay = Rows.get(i).getShiftWithoutOffset(pos);
            if (!currentDay.hasData()) {
                retVal = retVal && false;
            } else {
                myArrayOfSShift.add(currentDay);
            }
        }
        return retVal;
    }

    /**
     * Yes it looks big and ugly but its pretty fast here is the logic, go
     * through all rows getting pointers to SShifts, Merge Sort them ( Big Oh of
     * nlogn so fast as hell) then traverse back through swapping out only if
     * not equal...
     */
    public synchronized void addDShift(UnitToDisplay sp) {
        if (sp.getDayCode() == 0) {
            return;
        }
        int size = Rows.size();
        SShift newSShift = new SShift(sp.getDayCode(), sp.getWeekNo(), Rows.get(0));
        newSShift.buildSShift(sp);


        ArrayList<SShift> myArrayOfSShift = new ArrayList<SShift>();
        myArrayOfSShift.add(newSShift);

        if (areRowsAtDayFull(newSShift.getOffset(), myArrayOfSShift)) {
            mySched.addRowsToSchedule(1);
            try {
                SRow currRow = Rows.get(myArrayOfSShift.size() - 1);
                myArrayOfSShift.add(currRow.getShift(sp.getDayCode()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Collections.sort(myArrayOfSShift);
        size = Rows.size();
        for (int i = 0; i < size; i++) {
            try {
                if (i < myArrayOfSShift.size()) {
                    Rows.get(i).addSShift(myArrayOfSShift.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mySched.myParent.isDoneLoadingCompletely) {
            repaint();
        }
    }

    /*
     * Add A Blank Row...
     */
    public void addRow(int rows) {
        final int c = Rows.size();
        final int c2 = rows + c;

        try {
//            SwingUtilities.invokeAndWait(new Runnable() {
//                public void run() {
            for (int i = c; i < c2; i++) {
                Rows.add(new SRow(thisObject));
                if (mySched.myParent.isInitialized()) {
                    rowContainer.add(Rows.get(i));
                }
            }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reconcileShifts(boolean reconcile) {
        SRow myRow;
        for (int i = 0; i < Rows.size(); i++) {
            myRow = Rows.get(i);
            myRow.reconcileShifts(reconcile);
        }
        mySched.myParent.getSeaContainer().scanAndRemoveEmpFromReconciliation(mySched.getEmployee());
    }

    public void removeRow(int rows) {
        final int c2 = (Rows.size()) - rows;
        try {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
            for (int i = Rows.size() - 1; i >= c2; i--) {
                rowContainer.remove(Rows.get(i));
                Rows.remove(i);
            }
//            }
//        });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void plantRows() {
        int c = Rows.size();

        for (int i = 0; i < c; i++) {
            rowContainer.add(Rows.get(i));
        }

    }

    public void checkToDisplayReconcIcon() {
        pnTotal.checkToDisplayReconcIcon();
    }

    public boolean removeMaster(DShift d) {
        UnitToDisplay s;
        for (int i = 0; i < Rows.size(); i++) {
            s = Rows.get(i).getShift(d.getDayCode()).myShift;
            if (s != null) {
                if (s.getMaster() != null) {
                    if (s.getMaster().equals(d.myMaster)) {
                        s.getShift().cleareInfo();
                        s.setShift(null);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * does this ever get called?
     */
    public Point getMyPosition() {
        int x, y;
        Point p = mySched.getMyPosition();
        x = getX() + p.x;
        y = getY() + p.y;
        return new Point(x, y);
    }

    public void printEmployeeSchedule(int week) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();
        String startWeek = mySched.myParent.getDateByWeekDay(week_no, 0);
        String endWeek = mySched.myParent.getDateByWeekDay(week_no, 7);
        myClientQuery.update(0, "", "", startWeek, endWeek, "");
        myEmployeeQuery.update("", 0, true);
        try {
            Connection myConn = mySched.myParent.getConnection();
            myQuery.update("", mySched.getEmployee().getId() + "", startWeek, endWeek, "", "", false);
            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConn.getServer(), myConn.myCompany, myConn.myBranch);
            tableData.setSortType(tableData.SORT_BY_EMPLOYEE);
            try {
                InputStream reportStream =
                        getClass().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");

                Company companyInfo = Main_Window.parentOfApplication.getCompanyById(myConn.myCompany);
                Branch branchInfo = Main_Window.parentOfApplication.getBranchById(myConn.myCompany, myConn.myBranch);

                Hashtable parameters = new Hashtable();
                parameters.put("SUBREPORT_DIR", "rmischeduleserver/ireports/");
                parameters.put("Company_Name", companyInfo.getName());
                parameters.put("Company_Address", branchInfo.getBranchInfo().getAddress());
                parameters.put("Company_City", branchInfo.getBranchInfo().getCity());
                parameters.put("Company_State", branchInfo.getBranchInfo().getState());
                parameters.put("Company_Zip", branchInfo.getBranchInfo().getZip());
                EmployeeReportOptions myOptions = new EmployeeReportOptions(parameters);
                myOptions.setVisible(true);

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, tableData);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Error Creating Report, May Not Have Data To Display!", "Error Printing", JOptionPane.OK_OPTION);
        }
    }
    private static Dimension maxTotalSize = new Dimension(32767, 32767);

    public class RowTotal extends ZoomablePanel {

        private Color defaultBackground;
        private SWeek myParent;
        private JLabel iconLabel;
        private Dimension d = new Dimension();
        private String myVal;

        public RowTotal(String text, int Layout, SWeek myP) {
            super();
            myParent = myP;
            myVal = "0.0";
            setLayout(new BorderLayout());
            iconLabel = new JLabel("", JLabel.CENTER);
            iconLabel.addMouseListener(new clickReconcilliationCheck(iconLabel));
            if (mySched.getEmployee().getId() != 0) {
                iconLabel.setToolTipText("Print schedule for " + mySched.getEmployee().getName() + " for this week");
            } else {
                iconLabel.setToolTipText("Print this open schedule");
            }
            if (!Main_Window.isEmployeeLoggedIn()
                    && !Main_Window.isClientLoggedIn()) {
                this.addMouseListener(new clickOnWeekTotals());
                this.add(iconLabel, BorderLayout.SOUTH);
            }
            this.setBackground(mySched.myParent.total_color);
            defaultBackground = mySched.myParent.total_color;
        }

        public SSchedule getMySched() {
            return myParent.mySched;
        }

        public JLabel getIconLabel() {
            return this.iconLabel;
        }

        public String getMyVal() {
            return this.myVal;
        }

        public void setText(String text) {
            myVal = text;
            repaint();
        }

        @Override
        public void paintComponentCustom(Graphics g) {
            g.setColor(new Color(102, 102, 102));
            g.setFont(Main_Window.shift_totals_font);
            int y = myParent.getRowSize() * 60;
            g.drawString(myVal + "", (50 - g.getFontMetrics().getStringBounds(myVal + "", g).getBounds().width) / 2,
                    (y - g.getFontMetrics().getStringBounds(myVal + "", g).getBounds().height) / 2);
        }

        public void checkToDisplayReconcIcon() {
            if (myParent.mySched.myParent.shouldDisplayReconcile()) {
                iconLabel.setIcon(Main_Window.Reconcile_Icon);
            } else {
                iconLabel.setIcon(Main_Window.Employee_Print_Icon);
            }

            boolean reconciled = true;
            for (int r = 0; r < SWeek.this.Rows.size(); r++) {
                for (int d = 0; d < Rows.get(r).getShifts().length; d++) {
                    try {
                        DShift dshift = (DShift) Rows.get(r).getShifts()[d].myShift;
                        if (dshift != null && !dshift.getType().isShiftType(ShiftTypeClass.SHIFT_RECONCILED)) {
                            reconciled = false;
                        }
                    } catch (Exception exe) {
                    }
                }
            }
            if (reconciled && isWeekInPast()) {
                iconLabel.setIcon(Main_Window.Ok_Icon);
            }

        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(ComponentDimensions.currentSizes.get("RowTotal").width, 5365);
        }

        public String getSizeKey() {
            return "RowTotal";
        }

        /**
         * Small MouseListener to enable user to select all shift for a user for
         * a particular week nice for deleting, or confirming en masse...
         */
        private class clickOnWeekTotals extends MouseAdapter {

            public clickOnWeekTotals() {
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    SRow currRow;
                    SShift currShift;
                    mySched.myParent.getAvail().acb.ClearAllShiftSelections();
                    for (int i = 0; i < Rows.size(); i++) {
                        currRow = Rows.get(i);
                        for (int x = 1; x < 8; x++) {
                            currShift = currRow.getShift(x);
                            if (currShift.myShift != null) {
                                currShift.selectThisShift(true);
                            }
                        }
                    }
                }
            }
        }

        /**
         * ActionListener for clicking the checkbox in our header to Reconcile
         * the damn schedule.
         */
        private class clickReconcilliationCheck implements MouseListener {

            private JLabel parent;

            public clickReconcilliationCheck(JLabel p) {
                parent = p;
            }

            public void mouseClicked(MouseEvent e) {
                if (!myParent.mySched.myParent.shouldDisplayReconcile()) {
                    printEmployeeSchedule(1);
                } else {
                    if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Reconcile " + mySched.getEmployee().getName() + "'s schedule For " + mySched.getClient().getClientName() + "?", "Confirm Reconcilliation!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        reconcileShifts(true);
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        }
    }

    private class EmployeePrintClickAction implements MouseListener {

        private int w;

        public EmployeePrintClickAction(int week) {
            w = week;
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() > 1) {
                printEmployeeSchedule(w);
            }
        }
    }
}
