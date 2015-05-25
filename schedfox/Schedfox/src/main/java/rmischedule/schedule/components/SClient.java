/*
 * SClient.java
 *
 * Created on April 13, 2004, 2:02 PM
 */
package rmischedule.schedule.components;

import schedfoxlib.model.util.Record_Set;
import java.awt.Color;
import rmischedule.schedule.Schedule_View_Panel;
import rmischeduleserver.util.StaticDateTimeFunctions;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.main.Main_Window;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.util.xprint.xPrintData;
import schedfoxlib.model.Client;
import rmischeduleserver.mysqlconnectivity.queries.reports.assemble_schedule_for_client_report_query;

/**
 *
 * @author  jason.allen
 */
public class SClient extends SMainComponent implements Comparable {

    private Hashtable bannedEmployeeHash;
    private Vector<CertificationClass> necessaryCertifications;
    private SSchedule myBlankSchedule;
    private Client clientData;

    /** Our Constructor */
    public SClient(Schedule_View_Panel myParent, Record_Set rs) {
        super(myParent, rs);
        bannedEmployeeHash = new Hashtable(60);
        necessaryCertifications = new Vector(60);
        clientData = new Client(new Date(), rs);
        super.initializeComponent();
    }

    public SClient(String id) {
        super(id);
        clientData = new Client(new Date());
        clientData.setClientWorksite(0);
        if (id.equals("-1")) {
            clientData.setClientName("             ");
        } else if (id.equals("-2")) {
            clientData.setClientName("?????????????");
        }
        super.initializeComponent();
    }

    public Schedule_View_Panel getMyParent() {
        return this.parent;
    }

    public boolean isDefaultToNonBillable() {
        return this.clientData.getDefaultNonBillable();
    }

    /**
     * These dispose methods are so very important... We have a massive memory leak
     * since it appears java's GC cannot handle all of our bi-direction references...
     * Therefore it is very very important as you add classes that you properly
     * dispose all new private class, and remove all objects from sub panels. Please
     * verify from time to time in HEAP stack that this is still working.
     */
    @Override
    public void dispose() {
        super.dispose();
        necessaryCertifications = null;
        bannedEmployeeHash = null;
        parent = null;
        myBlankSchedule = null;
    }

    public Client getClientData() {
        return this.clientData;
    }

    /**
     * Does client have notes....
     */
    public boolean hasNotes() {
        if (clientData.getHasNotes() == null) {
        }
        return clientData.getHasNotes();
    }

    /**
     * Returns if we should have the blank schedule in our client.
     * @return
     */
    public boolean shouldHaveBlankSchedule() {
        return !Main_Window.isEmployeeLoggedIn()
                && !Main_Window.isClientLoggedIn();
    }

    /**
     * Used to create our one blank schedule for this Client....It has a Max Value
     * for it's Group ID...
     */
    public void addMyBlankSchedule() {
        if (shouldHaveBlankSchedule()) {
            myBlankSchedule = new SSchedule(parent.getEmployee("0"), this, parent.getWeekCount(), parent, Long.MAX_VALUE);
            myBlankSchedule.plantWeeks();
        }
    }

    public SSchedule getMyBlankSchedule() {
        return myBlankSchedule;
    }

    /**
     * Get My Rate Code
     */
    public Integer getRate() {
        return clientData.getRateCodeId();
    }

    /**
     * Is This client marked deleted?
     */
    public boolean isDeleted() {
        return this.clientData.getClientIsDeleted() == 1 ? true : false;
    }

    /**
     * Returns our needed training time...
     * 0 if no training is necessary to work this post...
     */
    public double getTrainingTime() {
        return clientData.getClientTrainingTime().doubleValue();
    }

    @Override
    public MouseAdapter getClientMouseAdapter() {
        return new EditClientListener();
    }

    @Override
    public MouseListener getPrinterMouseListener() {
        return new ClientMapAction(this, this);
    }

    /**
     * Private sub to use usked Id and worksite plus Client Name to generate a
     * complete Client name...
     */
    @Override
    public String generateClientName() {
        if (parent.getClient(clientData.getClientWorksite()) != null) {
            if (parent.getClient(clientData.getClientWorksite()) != this) {
                try {
                    return parent.getClient(clientData.getClientWorksite()).generateClientName() + " " + generateClientName();
                } catch (Exception e) {
                }
            }
        }
        return getClientName();
    }

    @Override
    public void setClientHeader() {
        int i, c;
        c = parent.getWeekCount();

        for (i = 0; i < c; i++) {
            getHeader().add(new IndividualClientHeader(this, new EditClientListener()));
            getHeader().add(new ClientPrinterPanel(this, new ClientMapAction(this, this), i));
        }
        setOpaque(false);
    }

    /**
     * Adds certification info to our client...
     */
    public void addCertification(CertificationClass certId) {
        necessaryCertifications.add(certId);
    }

    public void clearCertifications() {
        necessaryCertifications.clear();
    }

    public Vector<CertificationClass> getCertificationsNeeded() {
        return necessaryCertifications;
    }

    /**
     * Given a Hashtable of Certifications that an employee has it will tell us whether or not
     * they can work for this client... If Vector is empty then employee has all necessary
     * certs otherwise returns list of certs required....
     */
    public Vector<CertificationClass> getUnfilledCertifications(Hashtable employeesCerts) {
        Vector<CertificationClass> vectorOfUnfilledCerts = new Vector();
        for (int i = 0; i < necessaryCertifications.size(); i++) {
            if (employeesCerts.get(necessaryCertifications.get(i).getId()) == null) {
                vectorOfUnfilledCerts.add(necessaryCertifications.get(i));
            }
        }
        return vectorOfUnfilledCerts;
    }

    /**
     * Adds a banned employee to this client....
     */
    public void addBannedEmployee(String eid) {
        bannedEmployeeHash.put(eid, true);
    }

    /**
     * Is Passed in Employee Banned Or Not?
     */
    public boolean isEmployeeBanned(String eid) {
        boolean isBanned = false;
        try {
            isBanned = (Boolean) bannedEmployeeHash.get(eid);
        } catch (Exception e) {
        }
        return isBanned;
    }

    /**
     * Should be used after creates entire schedule...to plant weeks correctly...
     */
    public void plantShifts() {
        SortedSet myMap = parent.mySchedules.getClientSchedules(this);
        Iterator<SSchedule> myIterator = myMap.iterator();
        while (myIterator.hasNext()) {
            myIterator.next().plantWeeks();
        }
    }

    @Override
    public String toString() {
        return getClientName();
    }

    public void printClientSchedule(int week) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_client_report_query myQuery = new assemble_schedule_for_client_report_query();
        myEmployeeQuery.update("", 0, true);

        String startWeek = parent.getDateByWeekDay(week, 0);
        String endWeek = parent.getDateByWeekDay(week, 7);

        myClientQuery.update(0, "", "", startWeek, endWeek, "");
        try {
            myQuery.update(clientData.getClientId().toString(), "", startWeek, endWeek, "", "", false);

            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, parent.getConnection().getServer(), parent.getConnection().myCompany, parent.getConnection().myBranch);
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/ClientSchedule.jasper");

            Hashtable parameters = new Hashtable();
            parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");

            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, tableData);
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Error Creating Report, May Not Have Data To Display!", "Error Printing", JOptionPane.OK_OPTION);
        }
    }

    public void loadTrainingInfo(String employeeId, double trainingTime, boolean override) {
        try {
            SEmployee emp = parent.getEmployee(employeeId);

            if (override) {
                emp.addTrainingInformation(clientData.getClientTrainingTime().doubleValue() + 1.0, this);
            } else {
                emp.addTrainingInformation(trainingTime, this);
            }
        } catch (Exception ex) {
        }
    }

    /** public access to private values */
    public String getClientName() {
        try {
            return this.clientData.getClientName();
        } catch (Exception e) {
            return "";
        }
    }

    public String getFullClientName() {
        try {
            Client parentSite = this.clientData.getParentWorksite(parent.getConnection().myCompany);
            String worksiteName = "";


            if (parentSite != null) {
                worksiteName = parentSite.getClientName() + " ";


            }
            return worksiteName + getClientName();


        } catch (Exception e) {
            return getClientName();


        }
    }

    public void setClientName(String clientName) {
        clientData.setClientName(clientName);


    }

    /**
     * Used on heartbeat to deleted/undeleted clients...
     */
    public void setDeleted(Integer del) {
        clientData.setClientIsDeletedShort(del.shortValue());


    }

    public int getIdInt() {
        return clientData.getClientId();


    }

    public String getId() {
        return clientData.getClientId().toString();


    }

    @Override
    public SortedSet getSchedules() {
        return parent.mySchedules.getClientSchedules(this);


    }

    @Override
    public Color getMyColor() {
        return Schedule_View_Panel.client_color;

    }
}
