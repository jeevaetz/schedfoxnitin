/*
 * xMainAvailabilityPanel.java
 *
 * Created on August 17, 2005, 6:52 PM
 */
package rmischedule.employee.components.xavailability;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import rmischedule.main.Main_Window;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.employee.xEmployeeEdit;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischedule.security.*;
import rmischeduleserver.control.TimeOffController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Availability;
import schedfoxlib.model.AvailabilityMaster;
import schedfoxlib.model.TimeOffCalc;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;

/**
 *
 * @author  Owner
 */
public class xMainAvailabilityPanel extends GenericEditSubForm {

    private xPermanentAvailabilityWeekPanel myPermPanel;
    private xTempAvailabilityPanel myTempPanel;
    private xAvailabilitySettings mySettingsPanel;
    private xEmployeeEdit employeeEdit;
    private int numOfRecordSets;
    private String empid;

    /** Creates new form xMainAvailabilityPanel */
    public xMainAvailabilityPanel(xEmployeeEdit employeeEdit) {
        initComponents();
        numOfRecordSets = 0;
        this.employeeEdit = employeeEdit;
        myPermPanel = new xPermanentAvailabilityWeekPanel(employeeEdit, this);
        myTempPanel = new xTempAvailabilityPanel(employeeEdit, this);
        mySettingsPanel = new xAvailabilitySettings(employeeEdit);
        myTabbedPane.addTab("Temporary Unavailability", null, myTempPanel);
        myTabbedPane.addTab("Permanent Unavailability", null, myPermPanel);
        myTabbedPane.addTab("Timeoff Detail", null, mySettingsPanel);

        revalidate();
    }

    public javax.swing.JPanel getMyForm() {
        return this;
    }

    public String getMyTabTitle() {
        return "Availability";
    }

    public void refreshCalculations() {
        try {
            Thread refreshCals = new Thread() {
                @Override
                public void run() {
                    try {
                        this.setPriority(Thread.MAX_PRIORITY);
                        System.out.println("Refreshing Calculations");
                        RunQueriesEx myQuery = new RunQueriesEx();
                        
                        myQuery.add(myPermPanel.getQueryFormEmployee(myparent.getMyIdForSave()));
                        rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat myTempQuery = myTempPanel.getQueryFormEmployee(myparent.getMyIdForSave());
                        myparent.getConnection().prepQuery(myTempQuery);
                        myQuery.add(myTempQuery);

                        ArrayList<Record_Set> results = employeeEdit.getConnection().executeQueryEx(myQuery);

                        Record_Set availMasterRs = results.get(0);
                        final ArrayList<AvailabilityMaster> masterAvail = new ArrayList<AvailabilityMaster>();
                        for (int r = 0; r < availMasterRs.length(); r++) {
                            masterAvail.add(new AvailabilityMaster(availMasterRs));
                            availMasterRs.moveNext();
                        }

                        Record_Set availRs = results.get(1);
                        final ArrayList<Availability> avails = new ArrayList<Availability>();
                        for (int r = 0; r < availRs.length(); r++) {
                            avails.add(new Availability(availRs));
                            availRs.moveNext();
                        }

                        TimeOffController timeOffController =
                                TimeOffController.getInstance(employeeEdit.getConnection().myCompany);
                        final HashMap<Integer, Integer> integers = timeOffController.getAdjustments(Integer.parseInt(myparent.getMyIdForSave()));
                        final ArrayList<TimeOffCalc> personalDaysOffCalcs =
                                timeOffController.getCalculationsForEmployee(Integer.parseInt(myparent.getMyIdForSave()), 1);
                        final ArrayList<TimeOffCalc> vacationDaysOffCalcs =
                                timeOffController.getCalculationsForEmployee(Integer.parseInt(myparent.getMyIdForSave()), 2);

                        Runnable refreshPanels = new Runnable() {
                            public void run() {
                                doOnClear();
                                myPermPanel.loadData(masterAvail);
                                myTempPanel.loadData(avails);
                                
                                myTempPanel.refreshCounts(personalDaysOffCalcs, vacationDaysOffCalcs, integers);
                                mySettingsPanel.refreshCalculations(personalDaysOffCalcs, vacationDaysOffCalcs);
                            }
                        };
                        SwingUtilities.invokeAndWait(refreshPanels);
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                }
            };
            refreshCals.start();
        } catch (Exception e) {
        }
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {
        this.refreshCalculations();
        return new GenericQuery("Select now();");
    }

    public void loadData(schedfoxlib.model.util.Record_Set rs) {
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    public boolean userHasAccess() {
        return Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
    }

    public void doOnClear() {
        myPermPanel.clearData();
        myTempPanel.clearData();
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        myTabbedPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.GridLayout(1, 0));

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 3, 3, 3)), new javax.swing.border.EtchedBorder()));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        myTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateTemp(evt);
            }
        });

        jPanel1.add(myTabbedPane);

        add(jPanel1);

    }
    // </editor-fold>//GEN-END:initComponents

    private void updateTemp(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_updateTemp
        if (myTabbedPane.getSelectedComponent() == myTempPanel && empid != null) {
            this.refreshCalculations();
        }
    }//GEN-LAST:event_updateTemp
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane myTabbedPane;
    // End of variables declaration//GEN-END:variables
}
