/**
 * CheckInPanel.java
 *
 * Created on December 20, 2004, 12:50 PM
 */
package rmischedule.schedule.checkincheckout;

import rmischeduleserver.util.StaticDateTimeFunctions;
import java.util.*;
import javax.swing.event.ChangeEvent;
import rmischedule.components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.xprint.data.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.*;
import rmischedule.xprint.templates.genericreportcomponents.*;
import schedfoxlib.model.AssembleCheckinScheduleType;

/**
 *
 * @author ira
 */
public class CheckPanel extends javax.swing.JPanel {

    public int PRINT_YESTERDAY = 0;
    public int PRINT_TODAY = 1;
    public int PRINT_TOMMOROW = 2;
    private DisplayCheckInDataPanel myDispPanel;
    private boolean checkin;
    public static int IS_CHECKIN_DATA = 1;
    public static int IS_CHECKOUT_DATA = 2;
    public static int IS_NEVER_CHECKED_IN_DATA = 3;
    public static Calendar Now = Calendar.getInstance();
    private int myType = 0;
    private CheckInCheckOutWindow Parent;
    private CheckInCheckOutOptions myOptions;
    private ArrayList<IndividualCheckInCheckOutPanel> myPanels;

    /**
     * Creates new form CheckInPanel
     */
    public CheckPanel(int type, CheckInCheckOutWindow myParent) {
        initComponents();
        myPanels = new ArrayList();
        Parent = myParent;
        myType = type;
        myDispPanel = new DisplayCheckInDataPanel();
        IndividualCheckInCheckOutPanel testPanel = new IndividualCheckInCheckOutPanel(myType, this);
        HeaderPanel.add(testPanel);
        myScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        String myText = "Check In Report (Today)";

        PrintYesterday.setIcon(Main_Window.CheckInPrinterIcon);
        PrintYesterday.setText("Check In Report (Yesterday)");
        PrintYesterday.addMouseListener(new myMouseListener(PRINT_YESTERDAY));

        Header.setIcon(Main_Window.CheckInPrinterIcon);
        Header.setText(myText);
        Header.addMouseListener(new myMouseListener(PRINT_TODAY));

        PrintTommorow.setIcon(Main_Window.CheckInPrinterIcon);
        PrintTommorow.setText("Check In Report (Tomorrow)");
        PrintTommorow.addMouseListener(new myMouseListener(PRINT_TOMMOROW));
        myOptions = new CheckInCheckOutOptions();
        myLayeredPane.add(myOptions, javax.swing.JLayeredPane.POPUP_LAYER);

        myOptions.setVisible(false);

        myScrollPane.getViewport().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Rectangle myBounds = myOptions.getBounds();
                myBounds.y = (int) myScrollPane.getViewport().getViewRect().getY();
                myOptions.setBounds(myBounds);
            }
        });
    }

    /**
     * Returns the small window that contains the checkin information on the
     * south portion of the Check Panel...
     */
    public CheckInCheckOutOptions getCheckInOptions() {
        return myOptions;
    }

    public int getType() {
        return myType;
    }

    /**
     * Small helper function used to set Preferred, Minimum and Max size all at
     * one used for setting our header sizes...
     */
    private void setAllSizes(int x, int y, JLabel myComp) {
        myComp.setPreferredSize(new Dimension(x, y));
        myComp.setMinimumSize(new Dimension(x, y));
        myComp.setMaximumSize(new Dimension(x, y));
        myComp.setSize(x, y);
    }

    public void add(IndividualCheckInCheckOutPanel indPanel) {
        myPanels.add(indPanel);
    }

    public void refreshData() {
        Collections.sort(myPanels);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                DataDisplayPanel.removeAll();
                for (int i = 0; i < myPanels.size(); i++) {
                    DataDisplayPanel.add(myPanels.get(i));
                }
                DataDisplayPanel.revalidate();
                DataDisplayPanel.repaint();
                myOptions.repaint();
                myScrollPane.revalidate();
                repaint();
                Main_Window.parentOfApplication.repaint();
                resizeForm();
            }
        });
    }

    public void remove(IndividualCheckInCheckOutPanel indPanel) {
        myPanels.remove(indPanel);
        DataDisplayPanel.remove(indPanel);
    }

    public void removeAll() {
        myPanels.clear();
    }

    /**
     * Display client info from clicking on employee label from
     * IndividualCheckInCheckOutPanel
     */
    public void showEmployeeInfo(IndividualCheckInCheckOutPanel myPanel, int x1) {
        myDispPanel.setVisible(true);
        int x = x1 + myPanel.getX();
        int y = myPanel.getY() - this.myScrollPane.getViewport().getViewPosition().y;
        myDispPanel.setBounds(x, y, 200, 100);
        myDispPanel.showData(myPanel, myDispPanel.DISPLAY_EMPLOYEE_INFO);
    }

    /**
     * Display client info from clicking on client label from
     * IndividualCheckInCheckOutPanel
     */
    public void showClientInfo(IndividualCheckInCheckOutPanel myPanel, int x1) {
        myDispPanel.setVisible(true);
        int x = x1 + myPanel.getX();
        int y = myPanel.getY() - this.myScrollPane.getViewport().getViewPosition().y;
        myDispPanel.setBounds(x, y, 200, 100);
        myDispPanel.showData(myPanel, myDispPanel.DISPLAY_CLIENT_INFO);
    }

    private class myMouseListener extends MouseAdapter {

        private int DayToPrint;

        public myMouseListener(int dayToPrint) {
            DayToPrint = dayToPrint;
        }

        public void mouseClicked(MouseEvent e) {
            new_assemble_schedule_for_checkin_query myQuery = new new_assemble_schedule_for_checkin_query();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Calendar myCalendar = Calendar.getInstance();
            if (DayToPrint == PRINT_YESTERDAY) {
                myCalendar.add(Calendar.DAY_OF_MONTH, -1);
            } else if (DayToPrint == PRINT_TOMMOROW) {
                myCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            myQuery.setPrintableQuery(true);

            ArrayList<Record_Set> details = new ArrayList<Record_Set>();
            try {
                details = (DataSynchronizerThread.getPrintableArrayListForAllCompanies(myQuery, myCalendar));
            } catch (Exception ex) {
            }

            String[] columns = {"start_time", "end_time", "cname", "ename"};
            String[] headers = {"Start", "End", "Client Name", "Employee Name"};
            int[] size = {35, 35, 180, 180};


            Hashtable<String, formatterClass> formatter = new Hashtable();
            formatterClass timeFormat = new formatterClass() {

                public String formatInputString(String input) {
                    if (input == null || input.length() == 0) {
                        return "";
                    }
                    return StaticDateTimeFunctions.stringToFormattedTime(input, Main_Window.parentOfApplication.is12HourFormat());
                }
            };
            formatter.put("Start", timeFormat);
            formatter.put("End", timeFormat);
            try {
                InputStream reportStream =
                        getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

                Hashtable parameters = new Hashtable();
                parameters.put("Title", "Check In Report");
                String[] titles = new String[details.size()];
                for (int i = 0; i < details.size(); i++) {
                    Record_Set currSet = details.get(i);
                    String title = Main_Window.parentOfApplication.getCompanyNameById("") + " - " + Main_Window.parentOfApplication.getBranchNameById(currSet.getString("branch_id"));
                    titles[i] = title;
                }
                xGenericReportData reportData = new xGenericReportData(details, headers, columns, titles, formatter);

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, reportData);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        HeaderPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        PrintYesterday = new javax.swing.JLabel();
        Header = new javax.swing.JLabel();
        PrintTommorow = new javax.swing.JLabel();
        controlPanel = new javax.swing.JPanel();
        myScrollPane = new javax.swing.JScrollPane();
        myLayeredPane = new javax.swing.JLayeredPane();
        DataDisplayPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        HeaderPanel.setLayout(new javax.swing.BoxLayout(HeaderPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 100, 5));
        jPanel1.add(PrintYesterday);
        jPanel1.add(Header);
        jPanel1.add(PrintTommorow);

        HeaderPanel.add(jPanel1);

        add(HeaderPanel);

        controlPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        controlPanel.setLayout(new java.awt.GridBagLayout());

        myScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        DataDisplayPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED)));
        DataDisplayPanel.setMaximumSize(new java.awt.Dimension(90000, 5000));
        DataDisplayPanel.setMinimumSize(new java.awt.Dimension(500, 50));
        DataDisplayPanel.setLayout(new javax.swing.BoxLayout(DataDisplayPanel, javax.swing.BoxLayout.Y_AXIS));
        DataDisplayPanel.setBounds(0, 0, 12, 12);
        myLayeredPane.add(DataDisplayPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        myScrollPane.setViewportView(myLayeredPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        controlPanel.add(myScrollPane, gridBagConstraints);

        add(controlPanel);

        jPanel3.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 20));
        add(jPanel3);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        this.resizeForm();
    }//GEN-LAST:event_formComponentResized

    private void resizeForm() {
        Rectangle oldBounds = myLayeredPane.getBounds();
        
        int height = myScrollPane.getViewport().getHeight();
        
        if (getVisibleComponentCount() * 22 > height) {
            height = getVisibleComponentCount() * 22;
        }
        oldBounds.height = height;
        myLayeredPane.setBounds(oldBounds);
        myLayeredPane.setSize(new Dimension((int) oldBounds.getWidth(), height));
        myLayeredPane.setPreferredSize(new Dimension((int) oldBounds.getWidth(), height));
        DataDisplayPanel.setSize(myLayeredPane.getSize());
        DataDisplayPanel.setPreferredSize(myLayeredPane.getPreferredSize());
        DataDisplayPanel.revalidate();
        myScrollPane.revalidate();
        myScrollPane.repaint();

        myOptions.setBounds(myLayeredPane.getWidth() - 253, 0, myOptions.getPreferredSize().width, myOptions.getPreferredSize().height);
    }

    private int getVisibleComponentCount() {
        int retVal = 0;
        Component[] comps = DataDisplayPanel.getComponents();
        for (int c = 0; c < comps.length; c++) {
            if (comps[c].isVisible()) {
                retVal++;
            }
        }
        return retVal;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel DataDisplayPanel;
    protected javax.swing.JLabel Header;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JLabel PrintTommorow;
    private javax.swing.JLabel PrintYesterday;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLayeredPane myLayeredPane;
    public javax.swing.JScrollPane myScrollPane;
    // End of variables declaration//GEN-END:variables
}
