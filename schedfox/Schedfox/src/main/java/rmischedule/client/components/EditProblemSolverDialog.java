/*
 * EditProblemSolverDialog.java
 *
 * Created on August 14, 2006, 1:31 PM
 */
package rmischedule.client.components;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.CorporateCommunicatorController;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.control.ProblemSolverController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.PhoneContact;
import schedfoxlib.model.ProblemSolverContact;
import schedfoxlib.model.ProblemSolverContacts;
import schedfoxlib.model.Problemsolver;
import schedfoxlib.model.ProblemsolverTemplate;
import schedfoxlib.model.User;
import rmischeduleserver.mysqlconnectivity.queries.employee.get_employees_worked_at_client_query;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.get_problem_templates_query;
import schedfoxlib.model.ProblemSolverType;
import schedfoxlib.services.GenericService;

/**
 *
 * @author shawn
 */
public class EditProblemSolverDialog extends javax.swing.JDialog {

    private Problemsolver savedPS;
    private String idForSave;
    private Date dateForSave;
    private int clientIdForSave;
    private int userIdForSave;
    private int companyId;
    private final Problemsolver ps;
    private HashMap<Integer, JCheckBox> ccTypes;

    /**
     * Creates new form EditProblemSolverDialog
     */
    public EditProblemSolverDialog(java.awt.Frame parent, boolean modal, Problemsolver ps, int companyId) {
        super(parent, modal);
        initComponents();
        this.helpLabel2.setIcon(Main_Window.Note16x16);
        this.savedPS = null;
        this.idForSave = ps.getPsId().toString();
        this.dateForSave = ps.getPsDate();
        this.companyId = companyId;
        this.ps = ps;



        Connection myConn = new Connection();
        Client myClient = ps.getClientObj(companyId + "");
        this.clientIdForSave = ps.getClientId();
        this.userIdForSave = ps.getUserId();
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            this.dateDataLabel.setText(myFormat.format(ps.getPsDate()));
        } catch (Exception e) {
            this.dateDataLabel.setText(myFormat.format(new Date()));
        }
        this.clientDataLabel.setText(myClient.getClientName());
        this.phoneDataLabel.setText(myClient.getClientPhone());
        this.cellDataLabel.setText(myClient.getClientPhone2());
        this.faxDataLabel.setText(myClient.getClientFax());
        User originator = ps.getOriginatorObj(idForSave);
        this.userDataLabel.setText(originator.getUserFullName());
        privateCheckBox.setSelected(ps.isPrivateCommunication());

        try {
            ccTypes = new HashMap<Integer, JCheckBox>();
            ProblemSolverController controller = ProblemSolverController.getInstance(companyId + "");
            ArrayList<ProblemSolverType> types = controller.getProblemSolverTypes();
            for (int t = 0; t < types.size(); t++) {
                JCheckBox myCheckBox = new JCheckBox(types.get(t).getTypeName());
                corpTypesPanel.add(myCheckBox);
                ccTypes.put(types.get(t).getProblemsolverTypeId(), myCheckBox);
            }
            
            ArrayList<ProblemSolverType> selTypes = controller.getProblemsSolverTypesForProblem(this.ps.getPsId());
            for (int s = 0; s < selTypes.size(); s++) {
                JCheckBox selBox = ccTypes.get(selTypes.get(s).getProblemsolverTypeId());
                selBox.setSelected(true);
            }
        } catch (Exception exe) {}
        ArrayList<ProblemsolverTemplate> templates = this.getAllTemplates();
        Collections.sort(templates);

        this.problemSolverPanel.add(new ProblemSolverEditPanel("Communication:", ps.getProblem(), 1, extractTemplatesByType(1, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Solution:", ps.getSolution(), 2, extractTemplatesByType(2, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Scheduler Instructions:", ps.getSchedulerInst(), 3, extractTemplatesByType(3, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Supervisor Instructions:", ps.getSupervisorInst(), 4, extractTemplatesByType(4, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("District Manager Instructions:", ps.getDmInst(), 5, extractTemplatesByType(5, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Personnel Instructions:", ps.getHrInst(), 6, extractTemplatesByType(6, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Post Commander Instructions:", ps.getPostcomInst(), 7, extractTemplatesByType(7, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Security Officers Instructions:", ps.getOfficerInst(), 8, extractTemplatesByType(8, templates), companyId, this, loadEmployeesWorked()));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Payroll Instructions:", ps.getPayrollInst(), 9, extractTemplatesByType(9, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Night Managers and Dispatch Instructions:", ps.getDispatchInst(), 10, extractTemplatesByType(10, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Secretary and Check-In Instructions:", ps.getCheckinInst(), 11, extractTemplatesByType(11, templates), companyId, this, null));

        this.problemScrollPane.getVerticalScrollBar().setUnitIncrement(6);
        this.problemScrollPane.getVerticalScrollBar().setBlockIncrement(18);
        this.loadEmployeesWorked();
    }

    private void savePhoneContacts() {
        Component[] problemSolvers = this.problemSolverPanel.getComponents();
        ArrayList<PhoneContact> phoneContacts = new ArrayList<PhoneContact>();
        for (int c = 0; c < problemSolvers.length; c++) {
            ProblemSolverEditPanel panel = (ProblemSolverEditPanel) problemSolvers[c];
            phoneContacts.addAll(panel.getPhoneContacts());
        }
        ProblemSolverContact problemContact = new ProblemSolverContact();
        problemContact.setPsId(Integer.parseInt(this.idForSave));
        problemContact.setProblemSolverContactId(ProblemSolverContact.getNextPrimaryId(companyId + ""));

        ArrayList<ProblemSolverContacts> problemContacts = new ArrayList<ProblemSolverContacts>();
        ProblemSolverController problemController =
                ProblemSolverController.getInstance(companyId + "");

        for (int co = 0; co < phoneContacts.size(); co++) {
            ProblemSolverContacts contact = new ProblemSolverContacts();
            contact.setProblemSolverContactId(problemContact.getProblemSolverContactId());
            contact.setContactId(phoneContacts.get(co).getPrimaryId());
            contact.setContactType(CorporateCommunicatorController.SECURITY_GUARD_PHONE);
            problemContacts.add(contact);
        }


        try {
            problemController.saveReloadableProblemSolverContacts(problemContact, problemContacts, CorporateCommunicatorController.SECURITY_GUARD_PHONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProblemContactTypePanel loadEmployeesWorked() {
        get_employees_worked_at_client_query myClientQuery =
                new get_employees_worked_at_client_query();
        Connection myConn = new Connection();
        myConn.myCompany = companyId + "";
        myClientQuery.setPreparedStatement(ps.getClientId());
        Record_Set rst = myConn.executeQuery(myClientQuery);
        ArrayList<PhoneContact> employees = new ArrayList<PhoneContact>();
        for (int r = 0; r < rst.length(); r++) {
            employees.add(new Employee(new Date(), rst));
            rst.moveNext();
        }

        ProblemSolverController controller = ProblemSolverController.getInstance(companyId + "");

        ArrayList<ProblemSolverContacts> lastContacts = new ArrayList<ProblemSolverContacts>();
        try {
            lastContacts = controller.getProblemSolverContactsForProblem(ps.getPsId(), CorporateCommunicatorController.SECURITY_GUARD_PHONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProblemContactTypePanel<PhoneContact>("Security Officers Phone",
                CorporateCommunicatorController.SECURITY_GUARD_PHONE, myConn.myCompany, employees, lastContacts, false, null);
    }

    /**
     * Grabs all of the ProblemsolverTemplates for the specified problem type.
     * If none returns blank ArrayList.
     */
    private ArrayList<ProblemsolverTemplate> extractTemplatesByType(int type, ArrayList<ProblemsolverTemplate> templates) {
        ArrayList<ProblemsolverTemplate> retVal = new ArrayList<ProblemsolverTemplate>();
        for (int t = 0; t < templates.size(); t++) {
            if (templates.get(t).getProblemSolverType() == type) {
                retVal.add(templates.get(t));
            }
        }
        return retVal;
    }

    /**
     * Returns all problem solver templates.
     *
     * @return
     */
    private ArrayList<ProblemsolverTemplate> getAllTemplates() {
        ArrayList<ProblemsolverTemplate> retVal = new ArrayList<ProblemsolverTemplate>();
        get_problem_templates_query getTemplates = new get_problem_templates_query();
        getTemplates.setPreparedStatement(new Object[]{});
        Connection myConn = new Connection();
        myConn.myCompany = companyId + "";
        Record_Set rst = myConn.executeQuery(getTemplates);
        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new ProblemsolverTemplate(rst));
            rst.moveNext();
        }
        return retVal;
    }

    public EditProblemSolverDialog(java.awt.Frame parent, boolean modal, int companyId) {
        this(parent, modal, new Problemsolver(), companyId);
        this.setTitle("Add new Corporate Communicator");
        try {
            GenericController genericService = GenericController.getInstance("");
            this.dateForSave = new Date(genericService.getCurrentTimeMillis());
        } catch (Exception exe) {
        }
    }

    public Problemsolver getSavedPS() {
        return this.savedPS;
    }

    /**
     * This method allows for reloading the screen when a new template is
     * entered
     */
    protected void reload() {
        ArrayList<ProblemsolverTemplate> templates = this.getAllTemplates();
        Collections.sort(templates);

        this.problemSolverPanel.removeAll();
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Communication:", ps.getProblem(), 1, extractTemplatesByType(1, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Solution:", ps.getSolution(), 2, extractTemplatesByType(2, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Scheduler Instructions:", ps.getSchedulerInst(), 3, extractTemplatesByType(3, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Supervisor Instructions:", ps.getSupervisorInst(), 4, extractTemplatesByType(4, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("District Manager Instructions:", ps.getDmInst(), 5, extractTemplatesByType(5, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Personnel Instructions:", ps.getHrInst(), 6, extractTemplatesByType(6, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Post Commander Instructions:", ps.getPostcomInst(), 7, extractTemplatesByType(7, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Security Officers Instructions:", ps.getOfficerInst(), 8, extractTemplatesByType(8, templates), companyId, this, this.loadEmployeesWorked()));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Payroll Instructions:", ps.getPayrollInst(), 9, extractTemplatesByType(9, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Night Managers and Dispatch Instructions:", ps.getDispatchInst(), 10, extractTemplatesByType(10, templates), companyId, this, null));
        this.problemSolverPanel.add(new ProblemSolverEditPanel("Secretary and Check-In Instructions:", ps.getCheckinInst(), 11, extractTemplatesByType(11, templates), companyId, this, null));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        infoPanel1 = new javax.swing.JPanel();
        labelPanel1 = new javax.swing.JPanel();
        clientLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        dataPanel1 = new javax.swing.JPanel();
        clientDataLabel = new javax.swing.JLabel();
        dateDataLabel = new javax.swing.JLabel();
        userDataLabel = new javax.swing.JLabel();
        infoPanel2 = new javax.swing.JPanel();
        labelPanel2 = new javax.swing.JPanel();
        phoneLabel = new javax.swing.JLabel();
        cellLabel = new javax.swing.JLabel();
        faxLabel = new javax.swing.JLabel();
        dataPanel2 = new javax.swing.JPanel();
        phoneDataLabel = new javax.swing.JLabel();
        cellDataLabel = new javax.swing.JLabel();
        faxDataLabel = new javax.swing.JLabel();
        corpTypesPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        privateCheckBox = new javax.swing.JCheckBox();
        problemScrollPane = new javax.swing.JScrollPane();
        problemSolverPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        helpPanel = new javax.swing.JPanel();
        helpLabel1 = new javax.swing.JLabel();
        helpLabel2 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setTitle("Edit Corporate Communicator");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Corporate Communicator Information"));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 150));
        headerPanel.setLayout(new java.awt.GridLayout(1, 0));

        infoPanel1.setLayout(new javax.swing.BoxLayout(infoPanel1, javax.swing.BoxLayout.LINE_AXIS));

        labelPanel1.setMinimumSize(new java.awt.Dimension(80, 10));
        labelPanel1.setPreferredSize(new java.awt.Dimension(80, 48));
        labelPanel1.setLayout(new javax.swing.BoxLayout(labelPanel1, javax.swing.BoxLayout.Y_AXIS));

        clientLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        clientLabel.setText("Client Name:");
        clientLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        clientLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        labelPanel1.add(clientLabel);

        dateLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        dateLabel.setText("Date:");
        dateLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        dateLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        labelPanel1.add(dateLabel);

        userLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        userLabel.setText("Originator:");
        userLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        userLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        labelPanel1.add(userLabel);

        infoPanel1.add(labelPanel1);

        dataPanel1.setLayout(new javax.swing.BoxLayout(dataPanel1, javax.swing.BoxLayout.Y_AXIS));

        clientDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        clientDataLabel.setText("CLIENT NAME HERE");
        clientDataLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        clientDataLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        dataPanel1.add(clientDataLabel);

        dateDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dateDataLabel.setText("DATE HERE");
        dateDataLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        dateDataLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        dataPanel1.add(dateDataLabel);

        userDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        userDataLabel.setText("ORIGINATOR HERE");
        userDataLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        userDataLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        dataPanel1.add(userDataLabel);

        infoPanel1.add(dataPanel1);

        headerPanel.add(infoPanel1);

        infoPanel2.setLayout(new javax.swing.BoxLayout(infoPanel2, javax.swing.BoxLayout.LINE_AXIS));

        labelPanel2.setMinimumSize(new java.awt.Dimension(80, 48));
        labelPanel2.setPreferredSize(new java.awt.Dimension(80, 48));
        labelPanel2.setLayout(new javax.swing.BoxLayout(labelPanel2, javax.swing.BoxLayout.Y_AXIS));

        phoneLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        phoneLabel.setText("Client Phone:");
        phoneLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        phoneLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        labelPanel2.add(phoneLabel);

        cellLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cellLabel.setText("Client Cell:");
        cellLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        cellLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        labelPanel2.add(cellLabel);

        faxLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        faxLabel.setText("Client Fax:");
        faxLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        faxLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        labelPanel2.add(faxLabel);

        infoPanel2.add(labelPanel2);

        dataPanel2.setLayout(new javax.swing.BoxLayout(dataPanel2, javax.swing.BoxLayout.Y_AXIS));

        phoneDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        phoneDataLabel.setText("CLIENT PHONE HERE");
        phoneDataLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        phoneDataLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        dataPanel2.add(phoneDataLabel);

        cellDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cellDataLabel.setText("CLIENT CELL HERE");
        cellDataLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        cellDataLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        dataPanel2.add(cellDataLabel);

        faxDataLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        faxDataLabel.setText("CLIENT FAX HERE");
        faxDataLabel.setMinimumSize(new java.awt.Dimension(100, 16));
        faxDataLabel.setPreferredSize(new java.awt.Dimension(32767, 16));
        dataPanel2.add(faxDataLabel);

        infoPanel2.add(dataPanel2);

        headerPanel.add(infoPanel2);

        corpTypesPanel.setLayout(new java.awt.GridLayout(0, 2));
        headerPanel.add(corpTypesPanel);

        getContentPane().add(headerPanel);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel1.setMinimumSize(new java.awt.Dimension(97, 25));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        privateCheckBox.setText("Mark Communication as Private");
        privateCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jPanel1.add(privateCheckBox);

        getContentPane().add(jPanel1);

        problemScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        problemSolverPanel.setLayout(new javax.swing.BoxLayout(problemSolverPanel, javax.swing.BoxLayout.Y_AXIS));
        problemScrollPane.setViewportView(problemSolverPanel);

        getContentPane().add(problemScrollPane);

        controlPanel.setMaximumSize(new java.awt.Dimension(32767, 23));
        controlPanel.setMinimumSize(new java.awt.Dimension(10, 23));
        controlPanel.setPreferredSize(new java.awt.Dimension(100, 23));
        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.LINE_AXIS));

        helpPanel.setLayout(new java.awt.GridLayout(1, 0));

        helpLabel1.setText("Click a field to show/hide its contents.");
        helpPanel.add(helpLabel1);

        helpLabel2.setText("indicates that the field is not blank.");
        helpPanel.add(helpLabel2);

        controlPanel.add(helpPanel);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        controlPanel.add(cancelButton);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        controlPanel.add(saveButton);

        getContentPane().add(controlPanel);

        setSize(new java.awt.Dimension(713, 595));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (this.problemSolverPanel.getComponent(0).toString().length() == 0
                || this.problemSolverPanel.getComponent(1).toString().length() == 0) {
            JOptionPane.showMessageDialog(this, "You have to enter at least a problem and a solution", "Unable to save", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.savedPS = new Problemsolver();
        this.savedPS.setPsId(Integer.parseInt(this.idForSave));
        this.savedPS.setClientId(this.clientIdForSave);
        try {
            this.savedPS.setPsDate(this.dateForSave);
        } catch (Exception e) {
            try {
                GenericController genericService = GenericController.getInstance("");
                this.savedPS.setPsDate(new Date(genericService.getCurrentTimeMillis()));
            } catch (Exception exe) {
                this.savedPS.setPsDate(new Date());
            }
        }
        this.savedPS.setUserId(this.userIdForSave);
        this.savedPS.setPrivateCommunication(privateCheckBox.isSelected());
        this.savedPS.setProblem(this.problemSolverPanel.getComponent(0).toString());
        this.savedPS.setSolution(this.problemSolverPanel.getComponent(1).toString());
        this.savedPS.setSchedulerInst(this.problemSolverPanel.getComponent(2).toString());
        this.savedPS.setSupervisorInst(this.problemSolverPanel.getComponent(3).toString());
        this.savedPS.setDmInst(this.problemSolverPanel.getComponent(4).toString());
        this.savedPS.setHrInst(this.problemSolverPanel.getComponent(5).toString());
        this.savedPS.setPostcomInst(this.problemSolverPanel.getComponent(6).toString());
        this.savedPS.setOfficerInst(this.problemSolverPanel.getComponent(7).toString());
        this.savedPS.setPayrollInst(this.problemSolverPanel.getComponent(8).toString());
        this.savedPS.setDispatchInst(this.problemSolverPanel.getComponent(9).toString());
        this.savedPS.setCheckinInst(this.problemSolverPanel.getComponent(10).toString());

        this.savePhoneContacts();

        try {
            Iterator<Integer> keys = this.ccTypes.keySet().iterator();
            ArrayList<Integer> selKeys = new ArrayList<Integer>();
            while (keys.hasNext()) {
                Integer currKey = keys.next();
                if (ccTypes.get(currKey).isSelected()) {
                    selKeys.add(currKey);
                }
            }
            ProblemSolverController.getInstance(this.companyId + "").saveProblemSolverTypesToProblem(this.savedPS.getPsId(), selKeys);
        } catch (Exception exe) {}
        
        
        this.setVisible(false);
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.savedPS = null;
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel cellDataLabel;
    private javax.swing.JLabel cellLabel;
    private javax.swing.JLabel clientDataLabel;
    private javax.swing.JLabel clientLabel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel corpTypesPanel;
    private javax.swing.JPanel dataPanel1;
    private javax.swing.JPanel dataPanel2;
    private javax.swing.JLabel dateDataLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel faxDataLabel;
    private javax.swing.JLabel faxLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel helpLabel1;
    private javax.swing.JLabel helpLabel2;
    private javax.swing.JPanel helpPanel;
    private javax.swing.JPanel infoPanel1;
    private javax.swing.JPanel infoPanel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel labelPanel1;
    private javax.swing.JPanel labelPanel2;
    private javax.swing.JLabel phoneDataLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JCheckBox privateCheckBox;
    private javax.swing.JScrollPane problemScrollPane;
    private javax.swing.JPanel problemSolverPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel userDataLabel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
}
