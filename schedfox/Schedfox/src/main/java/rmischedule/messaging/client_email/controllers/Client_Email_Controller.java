//  package declaration
package rmischedule.messaging.client_email.controllers;

//  import declarations
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.commons.validator.EmailValidator;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.messaging.client_email.models.BranchContactData;
import rmischedule.messaging.client_email.models.ClientContactData;
import rmischedule.messaging.client_email.models.ClientOfBranchContactData;
import rmischedule.messaging.client_email.models.Client_Contact_Table_Model;
import rmischedule.messaging.client_email.models.Client_Email_AddressBook_ComboBoxModel;
import rmischedule.messaging.client_email.models.Client_Email_Data;
import rmischedule.messaging.client_email.models.Invalid_Email_Data;
import rmischedule.messaging.client_email.views.Client_Email_AddressBook_Dialog;
import rmischedule.messaging.client_email.views.Client_Email_Form;
import rmischedule.messaging.client_email.views.Confirm_Send_Client_Email_Dialog;
import rmischedule.templates.interfaces.TemplateSystemInterface;
import rmischedule.templates.models.InitializeTemplateSystem;
import rmischedule.templates.models.TemplateComboBoxModel;
import schedfoxlib.model.TemplateData;
import rmischedule.templates.view.TemplateDiagForm;
import rmischeduleserver.control.MessagingController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.ClientContact;
import rmischeduleserver.mysqlconnectivity.queries.messaging.client_email.get_champion_branch_data;
import schedfoxlib.model.MessagingCommunication;

/**
 * This object is a singleton templateController for the Client_Email_System
 *
 * @author Jeffrey N. Davis
 * @since 03/23/2011
 */
public final class Client_Email_Controller implements TemplateSystemInterface {
    /*
     * private variable declarations
     */

    private Client_Email_Form form;
    private static final String TEMPLATE_COMPANY_ID = "-1"; //  -1 denotes an administrator in the template system
    private static final String TEMPLATE_BRANCH_ID = "-1";  //  -1 denotes an administrator in the template system
    private Map<Integer, BranchContactData> branchMap;
    private Map<Integer, Client_Email_Data> sendMap;
    private Map<Integer, Invalid_Email_Data> invalidMap;
    private boolean isSendConfirmed;
    private Client_Email_AddressBook_Dialog addressBookDiag;
    private Client_Contact_Table_Model contactTableModel;
    private String originalToList;
    private Client_Email_Controller thisController;
    /*
     * singleton code
     */
    private static final Client_Email_Controller INSTANCE = new Client_Email_Controller();

    /**
     * Default construction of this object.
     */
    private Client_Email_Controller() {
        this.branchMap = new LinkedHashMap<Integer, BranchContactData>();
        this.sendMap = new LinkedHashMap<Integer, Client_Email_Data>();
        this.invalidMap = new LinkedHashMap<Integer, Invalid_Email_Data>();
        this.isSendConfirmed = false;
        this.contactTableModel = Client_Contact_Table_Model.getInstance();
    }

    /**
     * Returns the one true Client_Email_System @returns INSTANCE
     */
    public static Client_Email_Controller getInstance() {
        return INSTANCE;
    }
    /*
     * end of singleton code
     */

    /*
     * private method implementations
     */
    private void loadView() {
        /*
         * ensure that the view has been reset
         */
        this.form.getBranchPanel().removeAll();
        this.form.getSubjectField().setText("");
        this.form.getBodyField().setText("");
        this.form.getToTextField().setText("");
        this.form.getSelectActiveButton().setSelected(true);
        this.form.getSelectInactiveButton().setSelected(false);
        this.form.getSelectAllButton().setSelected(false);
        this.form.getAddressBookButton().setSelected(false);

        Collection<BranchContactData> collection = this.branchMap.values();
        /*
         * ensure all radio buttons are set to false initially
         */
        for (BranchContactData element : collection) {
            element.setSelected(false);
        }

        /*
         * add components with constraints
         */
        GridBagConstraints c = new GridBagConstraints();
        int idx = 0;
        for (BranchContactData element : collection) {
            /*
             * set constraints
             */
            c.gridx = idx % 4;
            c.gridy = idx / 4;
            c.weightx = .25;
            c.weighty = .25;
            c.anchor = GridBagConstraints.WEST;
            idx++;

            /*
             * add button to panel
             */
            element.setText(element.getBranchName());
            this.form.getBranchPanel().add(element, c);
        }
        this.form.getBranchPanel().revalidate();
        this.form.getBranchPanel().repaint();
    }

    private LinkedHashMap<Integer, BranchContactData> loadBranches() {
        LinkedHashMap<Integer, BranchContactData> returnMap = new LinkedHashMap<Integer, BranchContactData>();
        List<BranchContactData> unsortedList = new LinkedList<BranchContactData>();
        Connection myConnection = new Connection();
        Record_Set rs = null;
        get_champion_branch_data query = new get_champion_branch_data();
        myConnection.prepQuery(query);

        try {
            rs = myConnection.executeQuery(query);
            do {
                int branch_id = rs.getInt("branch_id");
                String branch_name = rs.getString("branch_name");
                BranchContactData branch = new BranchContactData(branch_id, branch_name);
                unsortedList.add(branch);
            } while (rs.moveNext());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new NullPointerException("An error has occured parsing branches from the Record Set.  rs.length() == " + rs.length());
        }

        if (unsortedList.isEmpty()) {
            throw new RuntimeException("Branches could not be loaded from the database.");
        }

        /*
         * added to remove null branches
         */
        for (Iterator<BranchContactData> itr = unsortedList.iterator(); itr.hasNext();) {
            BranchContactData element = itr.next();
            if (element.getClientMap().isEmpty()) {
                itr.remove();
            }
        }

        Collections.sort(unsortedList);
        for (BranchContactData element : unsortedList) {
            returnMap.put(element.hashCode(), element);
        }

        return returnMap;
    }

    /**
     * This method is used to {@code setEnabled( false ) } certain buttons
     * during development. This is due to Jim's constant need to push things to
     * production that aren't ready. Once all 4 Select Buttons have been written
     * and tested, this method should be removed
     */
    private void setDevelopmentVersion() {
        /*
         * currently, the address book is disabled
         */
        this.form.getAddressBookButton().setEnabled(false);
    }

    /**
     * Drills down into each object's data structure calling {@code map.clear()
     * }
     */
    private void clearAllMaps() {
        Collection<BranchContactData> collection = this.branchMap.values();
        for (BranchContactData element : collection) {
            element.clearClientOfBranchContactMaps();
        }

        this.branchMap.clear();
        this.sendMap.clear();
        this.invalidMap.clear();
    }

    /**
     * This method parses any email addresses contained with {@code form.jToTextField}
     *
     * @return toTextFieldSendList a {@code LinkedList<String>} of all email
     * addresses
     */
    private List<String> parseToTextField() {
        List<String> toList = new LinkedList<String>();

        String toTextField = this.form.getToTextField().getText();
        if (toTextField.length() > 0) {
            Pattern semicolon = Pattern.compile("\\;");
            String[] semicolonSplitArray = semicolon.split(toTextField);
            toList.addAll(Arrays.asList(semicolonSplitArray));
        }

        return toList;
    }

    /**
     * Checks to see if the parameter is a valid email address
     *
     * @param emailAddress
     * @return isValid true if valid, false if not
     */
    private boolean isEmailAddressValid(String emailAddress) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(emailAddress);
    }

    /**
     * This method generates a report on the invalid email addresses
     */
    private void runInvalidEmailAddressesReport() {
        try {
            File reportFile = File.createTempFile("Invalid Email Addresses Report", ".txt");
            PrintWriter writer = new PrintWriter(reportFile);
            writer.println("This is a Invalid Client Email Address Report. ");
            Collection<Invalid_Email_Data> collection = this.invalidMap.values();
            for (Invalid_Email_Data element : collection) {
                writer.println(element.getReportLine());
            }
            writer.flush();
            writer.close();

            //Formatter format = new Formatter ( reportFile );
            //format.format("hello", reportFile);
            Desktop.getDesktop().open(reportFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method loads {@code Confirm_Send_Client_Email_Form to confirm send
     *
     * @return isConfirmed a boolean describing whether to send the email
     */
    private boolean confirmSend() {
        /*
         * initialize form
         */

        Confirm_Send_Client_Email_Dialog confirmSendDialog = new Confirm_Send_Client_Email_Dialog(this.sendMap);
        confirmSendDialog.getSubjectDataLabel().setText(this.form.getSubjectField().getText());
        confirmSendDialog.getMessageTextPane().setText(this.form.getBodyField().getText());

        /*
         * set form visible, wait on response
         */
        confirmSendDialog.pack();
        confirmSendDialog.setVisible(true);

        return this.isSendConfirmed;
    }

    /**
     * Determines the reply to address for the current user, returns it.
     *
     * @return returnAddress
     */
    private String getReturnAddress() {
        String userEmailAddress = Main_Window.parentOfApplication.myUser.getEmail();
        if (!this.isEmailAddressValid(userEmailAddress)) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "You appear to have an missing or invalid email associated with your account, you will "
                    + "still be able to send emails, however your employees will not be able to respond until you fix this.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            userEmailAddress = "";
        }

        return userEmailAddress;
    }

    /**
     * This method parses the {@code jToTextField} and returns a {@code LinkedHashMap<Integer, Client_Email_Data>}
     * to be added to
     *
     * @param incomingForm
     */
    /*
     * end of private method implementations
     */

    /*
     * public method implementations
     */
    public void init(Client_Email_Form incomingForm) {
        this.thisController = this;
        this.form = null;
        this.clearAllMaps();
        this.form = incomingForm;
        this.branchMap = this.loadBranches();
        this.loadView();
        //this.setDevelopmentVersion(); //    used to hide inactive components during developmental stage

        this.viewController();
    }

    /**
     * This method validates that a send is ready to occur.
     *
     * @return isSendReady
     */
    public boolean validateSend() {
        /*
         * check subject / body of email
         */
        if (this.form.getSubjectField().getText().length() == 0) {
            JOptionPane.showMessageDialog(this.form, " You must enter a subject before sending an email.",
                    "Send Email", JOptionPane.ERROR_MESSAGE);
            this.form.getSubjectField().requestFocus();
            return false;
        }
        if (this.form.getBodyField().getText().length() == 0) {
            JOptionPane.showMessageDialog(this.form, " You must enter a body to the email before sending.",
                    "Send Email", JOptionPane.ERROR_MESSAGE);
            this.form.getBodyField().requestFocus();
            return false;
        }

        /*
         * parse toTextField, check to see if either Select Active or Select
         * Inactive Buttons are pressed
         */
        List<String> toTextFieldList = this.parseToTextField();
        if (toTextFieldList.isEmpty()) {
            if (this.form.getSelectActiveButton().isSelected() == false && this.form.getSelectInactiveButton().isSelected() == false) {
                JOptionPane.showMessageDialog(this.form, "To email recipients by branch, you must select the Active Button, Inactive Button, or both.",
                        "Send Email", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        /*
         * fill sendMap with valid Client_Email_Data, check to see if any valid
         * email addresses are present
         */
        /*
         * clear maps, create data objects
         */
        this.sendMap.clear();
        this.invalidMap.clear();
        String emailSubject = this.form.getSubjectField().getText();
        String emailBody = this.form.getBodyField().getText();
        List<ClientContact> contactList = new LinkedList<ClientContact>();

        /*
         * drill down into data structures to fill validList and invalidList
         */
        Collection<BranchContactData> branchContactCollection = this.branchMap.values();
        boolean noBranchSelected = true;
        for (BranchContactData element : branchContactCollection) {
            noBranchSelected = noBranchSelected && !element.isSelected();
        }

        for (BranchContactData element : branchContactCollection) {
            if (element.isSelected() || noBranchSelected) {
                element.fillSendDataStructures(contactList,
                        this.form.getSelectActiveButton().isSelected(),
                        this.form.getSelectInactiveButton().isSelected());
            }
        }

        /*
         * create lists for valid and invalid email address
         */
        List<Client_Email_Data> validList = new LinkedList<Client_Email_Data>();
        List<Invalid_Email_Data> invalidList = new LinkedList<Invalid_Email_Data>();

        /*
         * parse toTextFieldList and contactList into validList and invalidList
         */
        for (String element : toTextFieldList) {
            if (this.isEmailAddressValid(element)) {
                Client_Email_Data valid = new Client_Email_Data(emailSubject, emailBody, element, "From to text field.", "From to text field.");
                validList.add(valid);
            } else {
                Invalid_Email_Data invalid = new Invalid_Email_Data();
                invalid.setContactTitle("Email Address found in To field.");
                invalidList.add(invalid);
            }
        }
        for (ClientContact element : contactList) {
            if (this.isEmailAddressValid(element.getEmailAddress()) && element.getClientContactIncludeMassEmail()) {
                String clientEmail = emailBody;

                clientEmail = clientEmail.replaceAll("\\{client_name\\}", element.getClient().getClientName());
                clientEmail = clientEmail.replaceAll("\\{contact_name\\}", element.getClientContactFirstName() + " " + element.getClientContactLastName());
                clientEmail = clientEmail.replaceAll("\\{client_url\\}", "http://www.schedfox.com/champion/" + element.getClient().getUrl());
                clientEmail = clientEmail.replaceAll("\\{champ_url\\}", "http://www.champsecurity.com/client/" + element.getClient().getUrl());
                clientEmail = clientEmail.replaceAll("\\{username\\}", element.getClient().getcUserName());
                clientEmail = clientEmail.replaceAll("\\{client_password\\}", element.getClient().getcPassword());

                Client_Email_Data valid = new Client_Email_Data(emailSubject, clientEmail, element.getEmailAddress(),
                        element.getClient().getClientName(), element.getClientContactFirstName() + " " + element.getClientContactLastName());
                validList.add(valid);
            } else {
                Invalid_Email_Data invalid = new Invalid_Email_Data();
                invalid.setEmailAddress(element.getEmailAddress());
                //invalid.setBranchName(element.getBranchName());
                invalid.setClientName(element.getClient().getClientName());
                invalid.setClientPhone(element.getClient().getClientPhone());
                invalid.setContactPhone(element.getClientContactPhone());
                invalid.setContactTitle(element.getClientContactTitle());
                invalid.setContactName(element.getClientContactFirstName() + " " + element.getClientContactLastName());
                invalidList.add(invalid);
            }
        }

        for (Client_Email_Data element : validList) {
            this.sendMap.put(element.hashCode(), element);
        }
        for (Invalid_Email_Data element : invalidList) {
            this.invalidMap.put(element.hashCode(), element);
        }

        /*
         * check to see if any valid emails ahve been found
         */
        if (this.sendMap.isEmpty()) {
            JOptionPane.showMessageDialog(this.form, "No valid emails addresses have been found.  Please select one or more valid email address(es).",
                    "Send Email", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /*
     * action methods
     */
    /**
     * This method sends the email.
     */
    public void sendEmail() {
        if (this.confirmSend()) {
            try {
                
                final Client_Email_Form myForm = this.form;
                final String from = this.getReturnAddress();
                final ArrayList<Client_Email_Data> collection = new ArrayList<Client_Email_Data>();
                
                Iterator<Client_Email_Data> tempData = this.sendMap.values().iterator();
                while (tempData.hasNext()) {
                    Client_Email_Data data = tempData.next();
                    if (data.getIsSelected()) {
                        collection.add(data);
                    }
                }
                
                Thread sendEmailRunnable = new Thread() {

                    @Override
                    public void run() {
                        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                        myForm.setCursor(
                                hourglassCursor);
                        HashMap<String, String> recipients = new HashMap<String, String>();

                        MessagingController messagingController = new MessagingController("2");
                        for (Iterator<Client_Email_Data> iter = collection.iterator(); iter.hasNext();) {
                            Client_Email_Data element = iter.next();
                            String subject = element.getEmailSubject();
                            String message = element.getEmailBody();
                            message = message.replaceAll("\n", "<br/>");

                            try {
                                if (recipients.get(element.getEmailAddress().toLowerCase()) == null) {
                                    recipients.put(element.getEmailAddress().toLowerCase(), element.getEmailAddress().toLowerCase());
                                    
                                    try {
                                        MessagingCommunication comm = new MessagingCommunication();
                                        comm.setAttachPdf(false);
                                        comm.setBody(message);
                                        comm.setCcd(from);
                                        comm.setEmployeeId(0);
                                        comm.setIsEmail(true);
                                        comm.setIsSMS(false);
                                        comm.setFromEmail(from);
                                        comm.setSentTo(element.getEmailAddress());
                                        comm.setShiftId("");
                                        comm.setSubject(subject);
                                        comm.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                                        messagingController.saveMessagingCommunication(comm);
                                    } catch (Exception exe) {
                                        exe.printStackTrace();
                                    }

                                }
                            } catch (Exception exe) {
                                exe.printStackTrace();
                            }
                        }
                        JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Emails have been sent!");
                    }
                };
                sendEmailRunnable.start();
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Your emails are being sent, in the meantime you "
                        + "may continue to use Schedfox, you will be notified when the emails have been sent.", "Emails Sending",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                this.form.setCursor(normalCursor);
                this.isSendConfirmed = false;
            }
        }
    }

    /**
     * This method selects all elements in {@code this.branchMap}
     *
     * @param isSelected a boolean describing if the button has been pressed
     */
    public void selectAllControl(boolean isSelected) {
        Collection<BranchContactData> collection = this.branchMap.values();
        for (BranchContactData element : collection) {
            element.setSelected(isSelected);
        }
    }

    /**
     * This method is the action control for the Address Book button in
     *      {@code Client_Email_Form}.
     */
    public void addressBookButtonControl() {
        this.originalToList = null;
        this.originalToList = this.form.getToTextField().getText();
        this.addressBookDiag = new Client_Email_AddressBook_Dialog();
        this.addressBookDiag.init();

        Client_Email_AddressBook_ComboBoxModel branchComboBoxModel = new Client_Email_AddressBook_ComboBoxModel(this.branchMap);
        this.addressBookDiag.getBranchComboBox().setModel(branchComboBoxModel);
        this.addressBookDiag.getBranchComboBox().setSelectedIndex(0);
        this.addressBookDiag.getContactTable().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    ClientOfBranchContactData client = (ClientOfBranchContactData) thisController.addressBookDiag.getClientsComboBox().getSelectedItem();
                    Collection<ClientContactData> collection = client.getContactMap().values();
                    int idx = 0;
                    for (ClientContactData element : collection) {
                        if (idx == row) {
                            String originalTo = thisController.form.getToTextField().getText();
                            String newContact = element.getContactEmailAddress() + ";";
                            thisController.form.getToTextField().setText(originalTo + newContact);
                        }
                        idx++;
                    }
                }
            }
        });

        this.addressBookDiag.pack();
        this.addressBookDiag.setVisible(true);
    }

    /**
     * This method controls the action when a branch is selected in {@code Client_Email_AddressBook_Dialog.branchComboBox}
     */
    public void addressBookBranchComboBoxControl() {
        BranchContactData selectedBranch = (BranchContactData) this.addressBookDiag.getBranchComboBox().getSelectedItem();
        Client_Email_AddressBook_ComboBoxModel clientComboBoxModel = new Client_Email_AddressBook_ComboBoxModel(selectedBranch.getClientMap());
        this.addressBookDiag.getClientsComboBox().setModel(clientComboBoxModel);
        this.addressBookDiag.getClientsComboBox().setSelectedIndex(0);

        this.addressBookDiag.getBranchComboBox().revalidate();
        this.addressBookDiag.getBranchComboBox().repaint();
        this.addressBookDiag.getClientsComboBox().revalidate();
        this.addressBookDiag.getClientsComboBox().repaint();
    }

    /**
     * This method controls the action when a client is selected in {@code Client_Email_AddressBook_Dialog.clientComboBox}
     */
    public void addressBookClientComboBoxControl() {
        ClientOfBranchContactData client = (ClientOfBranchContactData) this.addressBookDiag.getClientsComboBox().getSelectedItem();
        this.contactTableModel.init(client.getContactMap(), client.getClientName());
        this.contactTableModel.fireTableDataChanged();
        this.addressBookDiag.getContactTable().revalidate();
        this.addressBookDiag.getContactTable().repaint();

    }

    /**
     * This method handles the addContact action from {@code Client_Email_AddressBook_Dialog}
     */
    public void addressBookAddContactControl() {
        /*
         * check for valid selections
         */
        int rowCount = this.addressBookDiag.getContactTable().getSelectedRowCount();
        int[] rows = new int[rowCount];
        rows = this.addressBookDiag.getContactTable().getSelectedRows();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this.addressBookDiag, "Please select a contact to add to the email.",
                    "Add Contact", JOptionPane.ERROR_MESSAGE);
        } else {
            for (int idx : rows) {
                ClientOfBranchContactData client = (ClientOfBranchContactData) this.addressBookDiag.getClientsComboBox().getSelectedItem();
                Collection<ClientContactData> collection = client.getContactMap().values();
                int internalIdx = 0;
                for (ClientContactData element : collection) {
                    if (idx == internalIdx) {
                        String address = element.getContactEmailAddress() + "; ";
                        String originalTo = this.form.getToTextField().getText();
                        String newTo = originalTo + address;
                        this.form.getToTextField().setText(newTo);
                    }
                    internalIdx++;
                }
            }
        }
    }

    /**
     * This method handles the {@code AddressBook.exitButton}
     */
    public void addressBookExitButtonControl() {
        this.form.getToTextField().setText(this.originalToList);
        this.originalToList = null;
    }

    /**
     * Method returns the this controller's table model
     *
     * @return {@code this.contactTableModel}
     */
    public Client_Contact_Table_Model getTableModel() {
        return this.contactTableModel;
    }

    /**
     * Method allows for control of the cancel button in {@code Confirm_Send_Client_Email_Dialog}.
     * <p>Sets {@code this.isSendConfirmed = false}
     */
    public void confirmSendDialogCancelControl() {
        this.isSendConfirmed = false;
    }

    /**
     * Method allows for control of send button in {@code Confirm_Send_Client_Email_Dialog}.
     * <p>Sets {@code this.isSendConfirmed = true }
     */
    public void confirmSendDialogSendControl() {
        this.isSendConfirmed = true;
    }


    /*
     * template system implementation
     */
    public void viewController() {
        LinkedHashMap<Integer, TemplateData> templates = templateController.reloadTemplates(TEMPLATE_COMPANY_ID, this.getTemplateType());
        this.form.getSubjectField().setText("");
        this.form.getBodyField().setText("");

        /*
         * set drop down menu visible/invisible, data model
         */
        if (templates.isEmpty()) {
            this.form.getTemplatesComboBox().setEnabled(false);
            this.form.getTemplatesComboBox().removeAllItems();
        } else {
            this.form.getTemplatesComboBox().setEnabled(true);
            TemplateComboBoxModel templateComboBoxModel = new TemplateComboBoxModel(templates);
            this.form.getTemplatesComboBox().setModel(templateComboBoxModel);

            if (templateController.hasNewSave()) {
                this.form.getSubjectField().setText(templateController.getNewTemplateName());
                this.form.getBodyField().setText(templateController.getNewTemplateValue());
                this.form.getTemplatesComboBox().setSelectedIndex(templateController.getNewTemplateIndex());
                templateController.resetAfterSave();
            } else {
                this.form.getTemplatesComboBox().setSelectedIndex(0);
            }

            this.form.getTemplatesComboBox().revalidate();
            this.form.getTemplatesComboBox().repaint();
            this.form.getTemplatesLabel().revalidate();
            this.form.getTemplatesLabel().repaint();
        }
    }

    public InitializeTemplateSystem initTemplateSystem() {
        String incomingText = this.form.getBodyField().getText();

        InitializeTemplateSystem init = (this.isTemplateNew())
                ? new InitializeTemplateSystem.Builder(TEMPLATE_COMPANY_ID, TEMPLATE_BRANCH_ID, this.getTemplateType(), templateController).incomingText(incomingText).build()
                : new InitializeTemplateSystem.Builder(TEMPLATE_COMPANY_ID, TEMPLATE_BRANCH_ID, this.getTemplateType(), templateController).build();

        return init;
    }

    public int getTemplateType() {
        return 4;
    }

    public void editTemplateAction() {
        TemplateDiagForm templateForm = new TemplateDiagForm();
        templateForm.init(this.initTemplateSystem());
        templateForm.pack();
        templateForm.setVisible(true);
        this.viewController();
    }

    public void templateSelectionAction() {
        TemplateData selectedElement = (TemplateData) this.form.getTemplatesComboBox().getSelectedItem();
        this.form.getSubjectField().setText(selectedElement.getTemplateName());
        this.form.getBodyField().setText(selectedElement.getTemplateValue());
    }

    private boolean isTemplateNew() {
        String outgoingText = this.form.getBodyField().getText();

        if (outgoingText.length() == 0) {
            return false;
        }

        boolean hasMatch = false;
        LinkedHashMap<Integer, TemplateData> map = templateController.getTemplates();
        Collection<TemplateData> collection = map.values();
        for (TemplateData element : collection) {
            if (outgoingText.equalsIgnoreCase(element.getTemplateValue())) {
                hasMatch = true;
            }
        }

        return hasMatch ? false : true;
    }

    public void loadTemplates() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
};
