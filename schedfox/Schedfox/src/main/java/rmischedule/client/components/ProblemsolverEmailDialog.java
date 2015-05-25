//  package declaration
package rmischedule.client.components;

//  import declarations
import java.awt.Cursor;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.validator.EmailValidator;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.templates.models.InitializeTemplateSystem;
import rmischedule.utility.FriendlyCalendar;
import rmischeduleserver.control.ProblemSolverController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.EmailContact;
import schedfoxlib.model.ProblemSolverContact;
import schedfoxlib.model.ProblemSolverContacts;
import schedfoxlib.model.Problemsolver;
import rmischeduleserver.mysqlconnectivity.queries.client.load_client_by_id_query;
import com.inet.jortho.SpellChecker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import org.apache.commons.lang3.StringUtils;
import rmischedule.messaging.email.SchedfoxEmail;
import rmischedule.templates.interfaces.TemplateSystemInterface;
import rmischedule.templates.models.TemplateComboBoxModel;
import schedfoxlib.model.TemplateData;
import rmischedule.templates.view.TemplateDiagForm;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.employee.get_employees_worked_at_client_query;
import schedfoxlib.model.ProblemsolverEmail;

/**
 *
 * @author jdavis
 */
public final class ProblemsolverEmailDialog extends javax.swing.JDialog implements TemplateSystemInterface {

    private final Problemsolver problemSolver;
    private final Connection myConnection;
    private final JasperPrint corpReport;
    private int branchId;
    private ArrayList<ClientContact> clientContactList;
    private static final String LOCATION_OF_CHAMP_LOGO = "http://champsecurity.com/images/champEagle.jpg";
    private static final String RED_HEX_COLOR = "990000";
    private static final String GREEN_HEX_COLOR = "006600";
    private ArrayList<ProblemContactTypePanel> contactPanels = new ArrayList<ProblemContactTypePanel>();

    /**
     * Creates new form ProblemsolverEmailDialog
     */
    public ProblemsolverEmailDialog(java.awt.Frame parent, boolean modal,
            Problemsolver problemSolver, String branchId, Connection myConnection,
            ArrayList<ClientContact> clientContactList, JasperPrint corpReport) {
        super(parent, modal);
        this.clientContactList = clientContactList;
        this.branchId = -1;
        try {
            this.branchId = Integer.parseInt(branchId);
        } catch (Exception e) {
        }
        this.problemSolver = problemSolver;
        this.myConnection = myConnection;
        this.corpReport = corpReport;

        //  initialize form
        initComponents();
        this.setTitle("Email Corporate Communicator");

        this.viewController();

        //  setup panels
        this.loadInformationPanel();
        this.loadRecipientsPanel();
        this.setupSaveExitPanel();

        //  set text area
        this.jEmailBodyTextArea.setWrapStyleWord(true);
        this.jEmailBodyTextArea.setLineWrap(true);
        this.jEmailBodyTextArea.setColumns(25);

        //  set spellchecker
        try {
            SpellChecker.register(this.jEmailSubjectTextField);
            SpellChecker.register(this.jEmailBodyTextArea);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Loads all relevant information into jInformationPanel
     */
    private void loadInformationPanel() {
        //  get data
        rmischedule.security.User user = Main_Window.parentOfApplication.getUser();
        String name = user.getFirstName() + " " + user.getLastName();
        String today = FriendlyCalendar.READABLE_TODAY();
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateOfCC = myFormat.format(this.problemSolver.getPsDate());
        String companyName = Main_Window.parentOfApplication.getCompanyNameById(this.myConnection.myCompany);
        String branchName = Main_Window.parentOfApplication.getBranchNameById(this.myConnection.myBranch);
        load_client_by_id_query idQuery = new load_client_by_id_query();
        idQuery.setCompany(this.myConnection.myCompany);
        idQuery.setPreparedStatement(new Object[]{this.problemSolver.getClientId()});
        String clientName = null;
        try {
            this.myConnection.prepQuery(idQuery);
            Record_Set rs = this.myConnection.executeQuery(idQuery);
            Client client = new Client(new Date(), rs);
            clientName = client.getClientName();
        } catch (Exception e) {
            System.out.println("Could not load Client. Error: " + e);
        }

        //  set labels
        this.jFromDataLabel.setText(name);
        this.jTodayDataLabel.setText(today);
        this.jDateOfCCDataLabel.setText(dateOfCC);
        this.jCompanyDataLabel.setText(companyName);
        this.jBranchDataLabel.setText(branchName);
        this.jClientDataLabel.setText(clientName);
    }

    private ArrayList<EmailContact> getEmployeesWorkedClient() {
        get_employees_worked_at_client_query myClientQuery =
                new get_employees_worked_at_client_query();
        Connection myConn = new Connection();
        myConn.myCompany = myConnection.myCompany + "";
        myClientQuery.setPreparedStatement(problemSolver.getClientId());
        Record_Set rst = myConn.executeQuery(myClientQuery);
        ArrayList<EmailContact> employees = new ArrayList<EmailContact>();
        for (int r = 0; r < rst.length(); r++) {
            employees.add(new Employee(new Date(), rst));
            rst.moveNext();
        }
        return employees;
    }

    /**
     * Loads all relevant information into jRecipientsPanel
     */
    private void loadRecipientsPanel() {
        ArrayList<EmailContact> contacts = new ArrayList<EmailContact>();
        contacts.addAll(this.clientContactList);

        //Load who was checked last time.
        ProblemSolverController problemController = ProblemSolverController.getInstance(this.myConnection.myCompany);
        ArrayList<ProblemSolverContacts> lastContacts = new ArrayList<ProblemSolverContacts>();
        try {
            ProblemSolverContact lastContact = problemController.getProblemSolverContactByClient(problemSolver.getClientId());
            lastContacts = lastContact.getContacts(this.myConnection.myCompany);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Left in code since I bet you anything they will want this back at some point, before
        //it would determine last contacts and reset them automatically for you, they asked for this
        //to be removed so I am just resetting the ArrayList to blank
        lastContacts = new ArrayList<ProblemSolverContacts>();

        contactPanels.add(new ProblemContactTypePanel("Client Contacts", "clients", myConnection.myCompany, contacts, lastContacts, true, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Corporate User", "corp", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("District Manager", "dm", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Field Supervisor", "field", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Personnel Manager", "person", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Dispatcher", "disp", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Scheduling Manager", "schedule", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Payroll", "payroll", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Sales", "sales", myConnection.myCompany, this.branchId, lastContacts, recipientScrollPane));
        contactPanels.add(new ProblemContactTypePanel("Employees", "employee", myConnection.myCompany, this.getEmployeesWorkedClient(), lastContacts, true, recipientScrollPane));

        for (int c = 0; c < contactPanels.size(); c++) {
            recipientPanel.add(contactPanels.get(c));
        }
    }

    /**
     * Sets up the jSaveExitPanel
     */
    private void setupSaveExitPanel() {
        this.jSendButton.setIcon(Main_Window.Problemsolver_Email_Dialog_Send_16x16_px);
        this.jExitButton.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);

        this.jSendButton.setToolTipText("Send Corporate Communicator");
        this.jExitButton.setToolTipText("Close this form");
    }

    /**
     * Determines if CC is ready to send
     *
     * @returns isReady - a boolean describing if the CC is ready to be emailed
     */
    private boolean isSendReady() {
        HashMap<String, EmailContact> emailList = this.getEmailList();

        //  check email list
        if (emailList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an email recipient.",
                    "Send CC", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //  check valid email addresses
        Iterator<String> emailIterator = emailList.keySet().iterator();
        EmailValidator emailValidator = EmailValidator.getInstance();
        while (emailIterator.hasNext()) {
            String email = emailIterator.next();
            if (email == null || !emailValidator.isValid(email)) {
                EmailContact contact = emailList.get(email);
                JOptionPane.showMessageDialog(this, "Addressee: " + contact.getFullName()
                        + " has an invalid email address: " + contact.getEmailAddress() + "\r\n"
                        + "Please fix this address or remove them from this sending.",
                        "Send CC Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        //  check subject / body
        if (this.jEmailSubjectTextField.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter an email subject.",
                    "Send CC", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Returns a list of email address to send CC to. Ira changed this to use
     * Hashmap so we have clientcontact information as well as no duplicate
     * emails.
     *
     * @returns emailList - an <code>Hashmap</code> of Strings containing email
     * addresses
     */
    private HashMap<String, EmailContact> getEmailList() {
        HashMap<String, EmailContact> emailList = new HashMap<String, EmailContact>();
        ArrayList<EmailContact> allContacts = new ArrayList<EmailContact>();
        for (int c = 0; c < contactPanels.size(); c++) {
            allContacts.addAll(contactPanels.get(c).getSelectedContacts());
        }
        for (int c = 0; c < allContacts.size(); c++) {
            emailList.put(allContacts.get(c).getEmailAddress(), allContacts.get(c));
        }
        return emailList;
    }

    private void saveSelectedContacts() {
        ProblemSolverContact problemContact = new ProblemSolverContact();
        problemContact.setPsId(this.problemSolver.getPsId());
        problemContact.setProblemSolverContactId(ProblemSolverContact.getNextPrimaryId(myConnection.myCompany));

        ArrayList<ProblemSolverContacts> problemContacts = new ArrayList<ProblemSolverContacts>();
        ProblemSolverController problemController =
                ProblemSolverController.getInstance(this.myConnection.myCompany);
        for (int c = 0; c < contactPanels.size(); c++) {
            ArrayList<EmailContact> contacts = contactPanels.get(c).getSelectedContacts();
            for (int co = 0; co < contacts.size(); co++) {
                ProblemSolverContacts contact = new ProblemSolverContacts();
                contact.setProblemSolverContactId(problemContact.getProblemSolverContactId());
                contact.setContactId(contacts.get(co).getPrimaryId());
                contact.setContactType(contactPanels.get(c).getValueInDb());
                problemContacts.add(contact);
            }
        }

        try {
            problemController.saveProblemSolverContactsWithDate(problemContact, problemContacts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method parses the outputted html from {@code JasperExportManager}
     * into a more pleasing format for Jim
     *
     * @author Jeffrey N. Davis
     * @param htmlFile a {@code File} object containing the outputted html from
     * {@code JasperExportManager}
     * @return html a String representing the correct html to be placed in the
     * email
     * @since 03/21/2011
     * @throws FileNotFoundException
     * @throw IOException
     */
    private String parseHtmlForEmail(File originalHtmlFile) throws FileNotFoundException, IOException {
        StringBuilder html = new StringBuilder();

        /*  remove all image tags first  */
        String nextLine = null;
        //System.setProperty("file.encoding", "UTF8");
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(originalHtmlFile), "UTF-8"));
        File imageRemovedHtmlFile = File.createTempFile("imageRemovedHtmlFile", ".html");
        Writer fOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(imageRemovedHtmlFile), "UTF-8"));
        while ((nextLine = in.readLine()) != null) {
            if (!nextLine.trim().startsWith("<td><img ") && !nextLine.contains("<img alt")) {
                fOut.write(nextLine);
            }
        }
        fOut.flush();
        fOut.close();

        /*  format remaining html   */
        in = null;
        in = new BufferedReader(new InputStreamReader(new FileInputStream(imageRemovedHtmlFile), "UTF-8"));
        boolean isTrFound = false;
        final String FOOTER_0 = "<td colspan=\"3\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #000000; font-size: 12px;\">Communication Report</span></td>";
        final String FOOTER_1 = "<td style=\"text-align: right;\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #000000; font-size: 12px;\">";
        final String PROBLEM_CHANGES = "<td colspan=\"4\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #344077; font-size: 18px; font-weight: bold;\">Communication:</span></td>";
        final String SOLUTION_CHANGES = "<td colspan=\"4\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #344077; font-size: 18px; font-weight: bold;\">Solution:</span></td>";
        final String COMPANY_CHANGES = "<td colspan=\"5\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #666666; font-size: 26px;\">";
        final String TABLE_HEADER_0 = "<td rowspan=\"2\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #FFFFFF; font-size: 12px; font-weight: bold;\">";
        final String TABLE_HEADER_1 = "<td><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #FFFFFF; font-size: 12px; font-weight: bold;\">";
        final String CORPORATE_COMMUNICATOR = "<td colspan=\"6\" valign=\"middle\" style=\"text-align: left;\"><span style=\"font-family: \"MS Sans-serif\", sans-serif; color: #000000; font-size: 44px;\">Corporate Communicator</span></td>";
        nextLine = null;
        int tableCount = 0;
        while ((nextLine = in.readLine()) != null) {
            /*  banner insertion    */
            if (nextLine.trim().equalsIgnoreCase("<tr>") && !isTrFound) {
                StringBuilder imageLine = new StringBuilder();
                imageLine.append(nextLine);
                imageLine.append("<td colspan='6' style='font-family: Arial; color: #000000; font-size: 12px;' valign='middle' style='text-align: left;'> ");
                imageLine.append(this.jEmailBodyTextArea.getText());
                imageLine.append("<br><br>");
                imageLine.append("<img src='");
                imageLine.append(LOCATION_OF_CHAMP_LOGO);
                imageLine.append("' align='left' />");
                imageLine.append("</td>");
                imageLine.append("<br>");
                isTrFound = true;
                html.append(imageLine.toString());
                //System.out.println( imageLine.toString() );
            } /*  removal of corporate communicator   */ else if (nextLine.trim().equalsIgnoreCase(CORPORATE_COMMUNICATOR)) {
                html.trimToSize();
            } /*  realignment */ else if (nextLine.trim().startsWith("<td colspan=\"6\"")) {
                String original = "\"text-align: right;\"";
                String replacement = "\"text-align: left;\"";
                String replaced = null;
                if (nextLine.contains(original)) {
                    replaced = nextLine.replace(original, replacement);
                } else {
                    replaced = nextLine;
                }
                html.append(replaced);
                //System.out.println( replaced );
            } /*  insertion of breaks, color change to Problem  */ else if (nextLine.trim().equalsIgnoreCase(PROBLEM_CHANGES)) {
                String trimmedNextLine = nextLine.trim();
                StringBuilder problemLine = new StringBuilder();
                problemLine.append(trimmedNextLine.substring(0, 16));
                problemLine.append("<br>");
                int trimmedNextLineLength = trimmedNextLine.length();
                problemLine.append(trimmedNextLine.substring(16, 57));
                problemLine.append(RED_HEX_COLOR);
                problemLine.append(trimmedNextLine.substring(63, trimmedNextLineLength));
                html.append(problemLine.toString());

                //System.out.println( problemLine.toString() );
            } /* removal of footer  */ else if (nextLine.trim().equalsIgnoreCase(FOOTER_0) || nextLine.trim().startsWith(FOOTER_1)) {
                html.trimToSize();
            } /*  set color of solution   */ else if (nextLine.trim().equalsIgnoreCase(SOLUTION_CHANGES)) {
                String trimmedNextLine = nextLine.trim();
                int trimmedNextLineLength = trimmedNextLine.length();
                StringBuilder solutionLine = new StringBuilder();
                solutionLine.append(trimmedNextLine.substring(0, 57));
                solutionLine.append(GREEN_HEX_COLOR);
                solutionLine.append(trimmedNextLine.substring(63, trimmedNextLineLength));
                html.append(solutionLine.toString());

                //System.out.println( solutionLine.toString() );
            } /*  insert break prior to company name */ else if (nextLine.trim().startsWith(COMPANY_CHANGES)) {
                String trimmedNextLine = nextLine.trim();
                int trimmedNextLineLength = trimmedNextLine.length();
                StringBuilder companyLine = new StringBuilder();
                companyLine.append(trimmedNextLine.substring(0, 16));
                companyLine.append("<br>");
                companyLine.append(trimmedNextLine.substring(16, trimmedNextLineLength));
                html.append(companyLine.toString());

                //System.out.println( companyLine.toString() );
            } /*  realign <vr>    */ else if (nextLine.trim().equalsIgnoreCase("<tr valign=\"top\">")) {
                String realign = "<tr valign=\"left\">";
                html.append(realign);

                //System.out.println( realign );
            } /*  removal of second table header  */ else if (nextLine.trim().startsWith(TABLE_HEADER_0)) {
                if (tableCount < 2) {
                    html.append(nextLine);
                    //System.out.println( nextLine );
                } else {
                    html.trimToSize();
                }

                tableCount++;
            } /*  removal of date in table header */ else if (nextLine.trim().startsWith(TABLE_HEADER_1)) {
                html.trimToSize();
            } else {
                html.append(nextLine);
                //System.out.println( nextLine );
            }
        }

        return html.toString().trim();
    }


    /*  Implementation of {@code TemplateSystemInterface }  */
    public void viewController() {
        LinkedHashMap<Integer, TemplateData> templates = templateController.reloadTemplates(myConnection.myCompany, this.getTemplateType());
        this.jEmailSubjectTextField.setText("");
        this.jEmailBodyTextArea.setText("");

        /*  set drop down menu visible/invisible, data model    */
        if (templates.isEmpty()) {
            this.jTemplatesNameComboBox.setVisible(false);
        } else {
            this.jTemplatesNameComboBox.setVisible(true);
            TemplateComboBoxModel templateComboBoxModel = new TemplateComboBoxModel(templates);
            this.jTemplatesNameComboBox.setModel(templateComboBoxModel);

            if (templateController.hasNewSave()) {
                this.jEmailBodyTextArea.setText(templateController.getNewTemplateName());
                this.jEmailBodyTextArea.setText(templateController.getNewTemplateValue());
                this.jTemplatesNameComboBox.setSelectedIndex(templateController.getNewTemplateIndex());
                templateController.resetAfterSave();
            } else {
                this.jTemplatesNameComboBox.setSelectedIndex(0);
            }

            this.jTemplatesNameComboBox.revalidate();
            this.jTemplatesNameComboBox.repaint();
        }
    }

    public void editTemplateAction() {
        TemplateDiagForm templateForm = new TemplateDiagForm();
        templateForm.init(this.initTemplateSystem());
        templateForm.pack();
        templateForm.setVisible(true);
        this.viewController();
    }

    public void templateSelectionAction() {
        TemplateData selectedElement = (TemplateData) this.jTemplatesNameComboBox.getSelectedItem();
        this.jEmailSubjectTextField.setText(selectedElement.getTemplateName());
        this.jEmailBodyTextArea.setText(selectedElement.getTemplateValue());
    }

    public InitializeTemplateSystem initTemplateSystem() {

        String compId = this.myConnection.myCompany;
        String branch_id = this.myConnection.myBranch;
        int templateType = this.getTemplateType();
        String incomingText = this.jEmailBodyTextArea.getText();
        String templateName = this.jEmailSubjectTextField.getText();

        InitializeTemplateSystem init = ((incomingText.length() > 0) && (templateName.length() == 0))
                ? new InitializeTemplateSystem.Builder(compId, branch_id, templateType, templateController).incomingText(incomingText).build()
                : new InitializeTemplateSystem.Builder(compId, branch_id, templateType, templateController).build();

        return init;
    }

    public int getTemplateType() {
        return 1;
    }
    /*  TemplateSystemInterface implementation complete */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jBasePanel = new javax.swing.JPanel();
        jInformationPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jFromLabel = new javax.swing.JLabel();
        jDateOfCCLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jFromDataLabel = new javax.swing.JLabel();
        jDateOfCCDataLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTodayLabel = new javax.swing.JLabel();
        jClientLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTodayDataLabel = new javax.swing.JLabel();
        jClientDataLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jCompanyLabel = new javax.swing.JLabel();
        jBranchLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jCompanyDataLabel = new javax.swing.JLabel();
        jBranchDataLabel = new javax.swing.JLabel();
        jMessagePanel = new javax.swing.JPanel();
        jEmailSubjectLabel = new javax.swing.JLabel();
        jEmailSubjectTextField = new javax.swing.JTextField();
        jEmailBodyLabel = new javax.swing.JLabel();
        jEmailBodyScrollPane = new javax.swing.JScrollPane();
        jEmailBodyTextArea = new javax.swing.JTextArea();
        jEditTemplateButton = new javax.swing.JButton();
        jTemplatesNameComboBox = new javax.swing.JComboBox();
        jRecipientsBasePanel = new javax.swing.JPanel();
        recipientScrollPane = new javax.swing.JScrollPane();
        recipientPanel = new javax.swing.JPanel();
        jButtonPanel = new javax.swing.JPanel();
        jExitButton = new javax.swing.JButton();
        jSendButton = new javax.swing.JButton();
        jAttatchPdfCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(380, 206));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jBasePanel.setLayout(new javax.swing.BoxLayout(jBasePanel, javax.swing.BoxLayout.PAGE_AXIS));

        jInformationPanel.setMaximumSize(new java.awt.Dimension(500000, 45));
        jInformationPanel.setMinimumSize(new java.awt.Dimension(153, 45));
        jInformationPanel.setPreferredSize(new java.awt.Dimension(930, 45));
        jInformationPanel.setLayout(new javax.swing.BoxLayout(jInformationPanel, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(85, 600));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jFromLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jFromLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jFromLabel.setText("From: ");
        jFromLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel1.add(jFromLabel);

        jDateOfCCLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jDateOfCCLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jDateOfCCLabel.setText("CC Date: ");
        jDateOfCCLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel1.add(jDateOfCCLabel);

        jInformationPanel.add(jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(200, 900));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jFromDataLabel.setMaximumSize(new java.awt.Dimension(2000, 40));
        jPanel2.add(jFromDataLabel);

        jDateOfCCDataLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel2.add(jDateOfCCDataLabel);

        jInformationPanel.add(jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(95, 600));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jTodayLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jTodayLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jTodayLabel.setText("Today: ");
        jTodayLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel3.add(jTodayLabel);

        jClientLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jClientLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jClientLabel.setText("Client: ");
        jClientLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel3.add(jClientLabel);

        jInformationPanel.add(jPanel3);

        jPanel4.setMaximumSize(new java.awt.Dimension(260, 900));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jTodayDataLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel4.add(jTodayDataLabel);

        jClientDataLabel.setMaximumSize(new java.awt.Dimension(2000, 40));
        jPanel4.add(jClientDataLabel);

        jInformationPanel.add(jPanel4);

        jPanel7.setMaximumSize(new java.awt.Dimension(95, 600));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        jCompanyLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jCompanyLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jCompanyLabel.setText("Company: ");
        jCompanyLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel7.add(jCompanyLabel);

        jBranchLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jBranchLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jBranchLabel.setText("Branch: ");
        jBranchLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel7.add(jBranchLabel);

        jInformationPanel.add(jPanel7);

        jPanel6.setMaximumSize(new java.awt.Dimension(260, 900));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jCompanyDataLabel.setMaximumSize(new java.awt.Dimension(200, 40));
        jPanel6.add(jCompanyDataLabel);

        jBranchDataLabel.setMaximumSize(new java.awt.Dimension(2000, 40));
        jPanel6.add(jBranchDataLabel);

        jInformationPanel.add(jPanel6);

        jBasePanel.add(jInformationPanel);

        jMessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Email Message"));
        jMessagePanel.setMinimumSize(new java.awt.Dimension(0, 150));
        jMessagePanel.setPreferredSize(new java.awt.Dimension(594, 150));

        jEmailSubjectLabel.setText("Subject:  ");

        jEmailSubjectTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jEmailBodyLabel.setText("Body:  ");

        jEmailBodyTextArea.setColumns(20);
        jEmailBodyTextArea.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jEmailBodyTextArea.setRows(5);
        jEmailBodyScrollPane.setViewportView(jEmailBodyTextArea);

        jEditTemplateButton.setText("Edit Templates");
        jEditTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditTemplateButtonActionPerformed(evt);
            }
        });

        jTemplatesNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTemplatesNameComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jMessagePanelLayout = new javax.swing.GroupLayout(jMessagePanel);
        jMessagePanel.setLayout(jMessagePanelLayout);
        jMessagePanelLayout.setHorizontalGroup(
            jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMessagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jEmailSubjectLabel)
                    .addComponent(jEmailBodyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMessagePanelLayout.createSequentialGroup()
                        .addComponent(jTemplatesNameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jEditTemplateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jEmailBodyScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addComponent(jEmailSubjectTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
                .addContainerGap())
        );
        jMessagePanelLayout.setVerticalGroup(
            jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMessagePanelLayout.createSequentialGroup()
                .addGroup(jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jEditTemplateButton)
                    .addComponent(jTemplatesNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jEmailSubjectLabel)
                    .addComponent(jEmailSubjectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jEmailBodyLabel)
                    .addComponent(jEmailBodyScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                .addContainerGap())
        );

        jBasePanel.add(jMessagePanel);

        jRecipientsBasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Recipients"));
        jRecipientsBasePanel.setMaximumSize(new java.awt.Dimension(500000, 300));
        jRecipientsBasePanel.setMinimumSize(new java.awt.Dimension(53, 220));
        jRecipientsBasePanel.setPreferredSize(new java.awt.Dimension(128, 220));
        jRecipientsBasePanel.setLayout(new java.awt.GridLayout(1, 0));

        recipientScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        recipientPanel.setLayout(new javax.swing.BoxLayout(recipientPanel, javax.swing.BoxLayout.Y_AXIS));
        recipientScrollPane.setViewportView(recipientPanel);

        jRecipientsBasePanel.add(recipientScrollPane);

        jBasePanel.add(jRecipientsBasePanel);

        jButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jButtonPanel.setMaximumSize(new java.awt.Dimension(32871, 30));
        jButtonPanel.setMinimumSize(new java.awt.Dimension(204, 30));
        jButtonPanel.setPreferredSize(new java.awt.Dimension(594, 30));

        jExitButton.setText("Exit");
        jExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jExitButtonActionPerformed(evt);
            }
        });

        jSendButton.setText("Send");
        jSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSendButtonActionPerformed(evt);
            }
        });

        jAttatchPdfCheckBox.setText("Attach pdf?");
        jAttatchPdfCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAttatchPdfCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jButtonPanelLayout = new javax.swing.GroupLayout(jButtonPanel);
        jButtonPanel.setLayout(jButtonPanelLayout);
        jButtonPanelLayout.setHorizontalGroup(
            jButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jButtonPanelLayout.createSequentialGroup()
                .addComponent(jExitButton)
                .addGap(331, 331, 331)
                .addComponent(jAttatchPdfCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSendButton)
                .addGap(53, 53, 53))
        );
        jButtonPanelLayout.setVerticalGroup(
            jButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jButtonPanelLayout.createSequentialGroup()
                .addGroup(jButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jExitButton)
                    .addComponent(jAttatchPdfCheckBox)
                    .addComponent(jSendButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBasePanel.add(jButtonPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jBasePanel, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-615)/2, (screenSize.height-550)/2, 615, 550);
    }// </editor-fold>//GEN-END:initComponents

    private void jExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jExitButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_jExitButtonActionPerformed

    private void jSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSendButtonActionPerformed
        if (this.isSendReady()) {
            try {
                //  set cursor to wait
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                saveSelectedContacts();

                /*  generate pdf, html output   */
                ByteArrayOutputStream oStream = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(this.corpReport, oStream);
                File htmlTemp = File.createTempFile("html", ".html");
                JasperExportManager.exportReportToHtmlFile(corpReport, htmlTemp.getAbsolutePath());

                /*  parse html  */
                String html = null;
                try {
                    html = this.parseHtmlForEmail(htmlTemp);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException("The original html file written by JasperExportManager could not be found.");
                } catch (IOException ex) {
                    throw new RuntimeException("There was an error reading from the original html file.");
                }

                /*  email contacts  */
                HashMap<String, EmailContact> emailList = this.getEmailList();
                String[] contactArray = new String[emailList.size()];
                Iterator<String> emailIterator = emailList.keySet().iterator();
                for (int idx = 0; idx < emailList.size(); idx++) {
                    contactArray[idx] = emailIterator.next();
                }

                try {
                    ProblemsolverEmail email = new ProblemsolverEmail();
                    email.setCcd(null);
                    email.setSentTo(StringUtils.join(contactArray, ","));
                    email.setPsId(problemSolver.getId());
                    email.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                    email.setEmailSubject(this.jEmailSubjectTextField.getText());
                    email.setEmailBody(html.toString());
                    email.setAttachPdf(jAttatchPdfCheckBox.isSelected());

                    ProblemSolverController controller = ProblemSolverController.getInstance(myConnection.myCompany);
                    controller.saveProblemSolverEmail(email);
                    
                    JOptionPane.showMessageDialog(this, "Corporate Communicator was successfully sent!",
                            "Send Corporate Communicator", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "There was an error sending CC.  Please contact Schedfox Admin.",
                        "Send Corporate Communicator", JOptionPane.ERROR_MESSAGE);
            } finally {
                //  reset cursor
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);

                this.dispose();
            }
        }
    }//GEN-LAST:event_jSendButtonActionPerformed

    private void jTemplateNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTemplateNameComboBoxActionPerformed
        this.templateSelectionAction();
    }//GEN-LAST:event_jTemplateNameComboBoxActionPerformed

    private void jEditTemplatesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditTemplatesButtonActionPerformed
        this.editTemplateAction();
    }//GEN-LAST:event_jEditTemplatesButtonActionPerformed

    private void jEditTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditTemplateButtonActionPerformed
        this.editTemplateAction();
    }//GEN-LAST:event_jEditTemplateButtonActionPerformed

    private void jTemplatesNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTemplatesNameComboBoxActionPerformed
        this.templateSelectionAction();
    }//GEN-LAST:event_jTemplatesNameComboBoxActionPerformed

    private void jAttatchPdfCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAttatchPdfCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jAttatchPdfCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jAttatchPdfCheckBox;
    private javax.swing.JPanel jBasePanel;
    private javax.swing.JLabel jBranchDataLabel;
    private javax.swing.JLabel jBranchLabel;
    private javax.swing.JPanel jButtonPanel;
    private javax.swing.JLabel jClientDataLabel;
    private javax.swing.JLabel jClientLabel;
    private javax.swing.JLabel jCompanyDataLabel;
    private javax.swing.JLabel jCompanyLabel;
    private javax.swing.JLabel jDateOfCCDataLabel;
    private javax.swing.JLabel jDateOfCCLabel;
    private javax.swing.JButton jEditTemplateButton;
    private javax.swing.JLabel jEmailBodyLabel;
    private javax.swing.JScrollPane jEmailBodyScrollPane;
    private javax.swing.JTextArea jEmailBodyTextArea;
    private javax.swing.JLabel jEmailSubjectLabel;
    private javax.swing.JTextField jEmailSubjectTextField;
    private javax.swing.JButton jExitButton;
    private javax.swing.JLabel jFromDataLabel;
    private javax.swing.JLabel jFromLabel;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JPanel jMessagePanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jRecipientsBasePanel;
    private javax.swing.JButton jSendButton;
    private javax.swing.JComboBox jTemplatesNameComboBox;
    private javax.swing.JLabel jTodayDataLabel;
    private javax.swing.JLabel jTodayLabel;
    private javax.swing.JPanel recipientPanel;
    private javax.swing.JScrollPane recipientScrollPane;
    // End of variables declaration//GEN-END:variables

    public void loadTemplates() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
};
