/*
 * ScheduleDashboardPanel.java
 *
 * Created on Oct 18, 2010, 9:24:36 AM
 */
package rmischedule.schedule;

import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import rmischedule.corporate_comm.CorporateCommunicator;
import rmischedule.data_connection.Connection;
import rmischedule.employee.components.EmployeePayCheckTablePanel;
import rmischedule.employee.components.HealthCarePanel;
import rmischedule.employee.dashboard.Employee_Dashboard;
import rmischedule.main.Main_Window;
import rmischedule.mapping.MappingPanel;
import rmischedule.schedule.components.SMainComponent;
import rmischedule.training.TrainingInformationPanel;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.client.get_client_by_id_query;

/**
 *
 * 
 */
public class ScheduleDashboardPanel extends javax.swing.JPanel {

    private Employee_Dashboard empDash;
    private TrainingInformationPanel clientTraining;
    private CorporateCommunicator corporateComm;
    private MappingPanel myMapping;
    private int selectedClientId = 0;
    private Employee employee;
    private Client client;
    private JTabbedPane tab = new JTabbedPane();
    private JTabbedPane tabs = new JTabbedPane();
    private JPanel tablePanel = new JPanel();
    private HealthCarePanel healthcarePanel = new HealthCarePanel();
    private JSplitPane split = new JSplitPane();
    private EmployeePayCheckTablePanel payPanel;
    private Schedule_Main_Form scheduleForm;

    /** Creates new form ScheduleDashboardPanel */
    public ScheduleDashboardPanel(Schedule_Main_Form scheduleForm) {
        initComponents();

        this.scheduleForm = scheduleForm;
        
        if (shouldIncludeEmployeeInfo()) {
            empDash = new Employee_Dashboard(this);
            contentPanel.add(empDash);
        }

        if (shouldIncludeClientTraining()) {
            clientTraining = new TrainingInformationPanel(this);
            contentPanel.add(clientTraining);
        }

        if (shouldIncludeCorporateComm()) {
            corporateComm = new CorporateCommunicator(this);
            contentPanel.add(corporateComm);
        } else {
            tabs.addTab("Payroll Information", tablePanel);
            tabs.addTab("Aetna Healthcare Links",healthcarePanel);
            
            contentPanel.add(tabs);
        }

    }

    /**
     * Returns the active client.
     * @return int
     */
    public int getCurrentClientId() {
        int retVal = 0;
        if (Main_Window.isClientLoggedIn()) {
            retVal = Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId());
        } else {
            return selectedClientId;
        }
        return retVal;
    }

    public void refreshScreen(Schedule_View_Panel svp) {
        int maxHeight = this.scheduleForm.getMaxSizeForMaxScheduleViewPanel();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Dimension formSize = new Dimension(this.getWidth(), (int) screenSize.getHeight() - 400);
        if (Main_Window.isClientLoggedIn()) {
            formSize = new Dimension(this.getWidth(), (int)300);
        }
        
        
        if (400 >= screenSize.getHeight()) {
            formSize.height = (int)(screenSize.getHeight() - 150);
        } else if (maxHeight < 200) {
            formSize.height = screenSize.height - 250;
        } else if (maxHeight > 0 && maxHeight < 400) {
            formSize.height = screenSize.height - maxHeight - 90;
        }

        this.setPreferredSize(formSize);
        this.setMinimumSize(formSize);
        this.setSize(formSize);

        scheduleForm.revalidate();

        clientTraining.showTrainingInformation(svp);
        corporateComm.refreshProblems(svp);
    }

    private boolean shouldIncludeEmployeeInfo() {
        return Main_Window.isEmployeeLoggedIn();
    }
    
    private boolean shouldIncludeClientTraining() {
        return Main_Window.isEmployeeLoggedIn();
    }

    private boolean shouldIncludeCorporateComm() {
        return !Main_Window.isEmployeeLoggedIn();
    }

    public void setEmployeeInfo(Employee emp, Schedule_View_Panel svp) {

        empDash.setEmployee(emp, svp);
        employee = emp;
        String companyID = svp.getCompany();
        String branchID = svp.getBranch();
        Connection con = new Connection();
        con.setCompany(companyID);
        con.setBranch(branchID);
        payPanel = new EmployeePayCheckTablePanel(con, employee); //TODO need to find parent
        payPanel.loadEmployeePayments();
        
        tablePanel.setBorder(new TitledBorder("Double click a check to see the details."));
        tablePanel.setLayout(new GridLayout());


        Dimension d = tab.getSize();
        this.tablePanel.setPreferredSize(d);
        payPanel.setSize(d);

        this.tablePanel.add(payPanel);

        if (employee != null && this.client != null) {

            //myMapping.mapDirections(employee, this.client);
        }
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(this, true);
        }
    }

    public void setClientInfo(SMainComponent client, Schedule_View_Panel svp) {
        try {
            selectedClientId = Integer.parseInt(client.getId());
            this.empDash.loadEmployeeTestScores(svp);
            clientTraining.showTrainingInformation(svp);
            if (corporateComm != null) {
                corporateComm.refreshProblems(svp);
            }
        } catch (Exception e) {
        }
        try {
            if (myMapping != null) {
                get_client_by_id_query clientQuery = new get_client_by_id_query();
                clientQuery.setPreparedStatement(new Object[]{selectedClientId});
                Connection myConn = svp.getConnection();
                Record_Set rst = myConn.executeQuery(clientQuery);
                Client myClient = new Client(new Date(), rst);
                this.client = myClient;
                if (employee != null && this.client != null) {
                    myMapping.mapDirections(employee, this.client);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                if (myMapping != null) {
                    ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(myMapping, true);
                }
                ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(this, true);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(400000, 800));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(400, 350));
        setLayout(new java.awt.GridLayout(1, 0));

        contentPanel.setLayout(new java.awt.GridLayout());
        jScrollPane1.setViewportView(contentPanel);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
