/*
 * xClientEdit.java
 *
 * Created on September 7, 2005, 2:47 PM
 *
 * Copyright: SchedFox 2005
 */
package rmischedule.client;

import schedfoxlib.model.util.Record_Set;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.client.components.*;
import rmischedule.data_connection.*;
import rmischedule.main.*;
import rmischedule.security.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import rmischedule.client.insurance.InsuranceAdditionalDialog;
import rmischedule.client.search_client.ClientSearch;
import rmischedule.messaging.email.SchedfoxEmail;
import rmischedule.xadmin.CompanyLoginInformation;
import rmischeduleserver.control.BillingController;
import rmischeduleserver.control.ClientController;

import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContract;
import schedfoxlib.model.ClientExport;
import schedfoxlib.model.ClientRateCode;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import schedfoxlib.model.CompanyInformationObj;

/**
 *
 * @author Ira Juneau
 */
public class xClientEdit extends GenericTabbedEditForm implements CompanyBranchMenuInterface {

    private Connection myConnection;
    public String cpny;
    public String branch;
    private String deleted;
    private xClientEdit thisobject;

    private Client_Information myInfo;
    private Client_Contacts myContact;
    private xClient_Notes myNotes;
    private Client_ProblemSolver myProblems;
    private Client_Certifications myCerts;
    private Client_Training myTraining;
    private Client_Bannination myBan;
    private Client_Ids myIds;
    private Client_Pictures myPictures;
    private Client_Files myFiles;
    private Client_Branch branchPanel;
    private ClientOfficerDailyReports myOdr;

    /**
     * Creates a new instance of xClientEdit
     */
    public xClientEdit() {
        myConnection = new Connection();
        deleted = "0";

        thisobject = this;
    }

    public Client_Ids getIdForm() {
        return this.myIds;
    }

    public String getBranchFromChild() {
        if (this.branchPanel != null && !this.branchPanel.getBranch().equals("-1")) {
            return this.branchPanel.getBranch();
        } else {
            return this.branch;
        }
    }

    private void setupTabs() {
        super.clearSubForms();
        Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions =
                Main_Window.parentOfApplication.getCompanyViewOptions(cpny);

        if (myInfo == null) {
            myInfo = new Client_Information(this);
        }
        if (myContact == null) {
            myContact = new Client_Contacts(this);
        }
        if (myNotes == null) {
            myNotes = new xClient_Notes(this);
        }
        if (myProblems == null) {
            myProblems = new Client_ProblemSolver();
        }
        if (myCerts == null) {
            myCerts = new Client_Certifications(this);
        }
        if (myTraining == null) {
            myTraining = new Client_Training();
        }
        if (myBan == null) {
            myBan = new Client_Bannination(this);
        }
        if (myIds == null) {
            myIds = new Client_Ids();
        }
        if (myPictures == null) {
            myPictures = new Client_Pictures(this);
        }
        if (myFiles == null) {
            myFiles = new Client_Files(this);
        }
        if (myOdr == null) {
            myOdr = new ClientOfficerDailyReports(this);
        }

        super.addSubForm(myInfo);
        super.addSubForm(myContact);
        super.addSubForm(myNotes);
        super.addSubForm(myProblems);
        super.addSubForm(myCerts);

        if (Main_Window.parentOfApplication.showTrainingForClients()) {
            if (Main_Window.newOptions.getOptionByName("cshowtrainedtab") == null
                    || ((Boolean) Main_Window.newOptions.getOptionByName("cshowtrainedtab").read()).booleanValue()) {
                super.addSubForm(myTraining);
            }
        }
        if (Main_Window.newOptions.getOptionByName("cshowbannedtab") == null
                || ((Boolean) Main_Window.newOptions.getOptionByName("cshowbannedtab").read()).booleanValue()) {
            super.addSubForm(myBan);
        }
        super.addSubForm(myIds);
        super.addSubForm(myPictures);
        super.addSubForm(myFiles);
        super.addSubForm(myOdr);
//        super.addSubForm(new ClientDynamicForm(this));
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(this.getConnection(), "ADMIN", "Payroll")) {
            super.addSubForm(new Client_Billing(this));
        }
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(this.getConnection(), "ADMIN", "Payroll")) {
            super.addSubForm(new Client_Export());
        } else {
            super.addSubForm(new Client_Export(), false);
        }

        CompanyInformationObj branchObj
                = Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_VIEW_BRANCH);
        if (!branchObj.getOption_value().equalsIgnoreCase("false")) {
            this.branchPanel = new Client_Branch(this);
            super.addSubForm(this.branchPanel);
        }
    }

    public void setInformation(String co, String br, boolean fetchData) {
        myConnection.setCompany(co);
        myConnection.setBranch(br);
        cpny = co;
        branch = br;
        this.setupTabs();
        if (fetchData) {
            getData();
        }
    }

    public void setInformation(String co, String br) {
        this.setInformation(co, br, true);
    }

    public void addMyMenu(JMenuBar myMenu) {
        JMenu mainMenu = new JMenu("Select Branch/Company");
        JMenu searchMenu = new JMenu("Search Clients");
        JMenuItem searchBySSN = new JMenuItem("Search Client");
        searchBySSN.addActionListener(new ClientSearchListener());
        searchMenu.add(searchBySSN);
        Main_Window.parentOfApplication.setUpMenuOfCompsAndBranches(mainMenu, this, "");
        myMenu.add(mainMenu);
        myMenu.add(searchBySSN);
    }

    public Object createObjectForList(Record_Set rs) {
        return new Client(new Date(), rs);
    }

    public String getDisplayNameForObject(Object input) {
        return ((Client) input).getClientName();
    }

    @Override
    protected String getOtherButtonString() {
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User", "Payroll")) {
            return "Insurance Notification";
        }
        return "";
    }

    @Override
    protected String getOtherButton2String() {
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User", "Payroll")) {
            return "Copy";
        }
        return "";
    }

    @Override
    protected void other2Action() {
        try {
            Client currentClient = (Client) this.getSelectedObject();
            String newClientName = (String) JOptionPane.showInputDialog(Main_Window.parentOfApplication, "Enter a new client name",
                    "Client Name", JOptionPane.PLAIN_MESSAGE, null, null,
                    currentClient.getClientName());

            ClientController clientController = ClientController.getInstance(myConnection.myCompany);
            BillingController billingController = BillingController.getInstance(myConnection.myCompany);

            ArrayList<ClientContact> contacts = currentClient.getContacts(cpny);
            ArrayList<ClientRateCode> rates = currentClient.getClientRateCodes(cpny);
            ArrayList<ClientContract> contracts = clientController.getClientContract(currentClient.getClientId());

            ClientExport clientExport = currentClient.getClientExport(cpny);

            currentClient.setClientId(0);
            currentClient.setClientName(newClientName);
            clientController.saveClient(currentClient);

            if (contracts != null) {
                for (int c = 0; c < contracts.size(); c++) {
                    contracts.get(c).setClientContractId(null);
                    contracts.get(c).setClientId(currentClient.getClientId());
                    clientController.saveClientContract(contracts.get(c));
                }
            }

            if (contacts != null) {
                for (int c = 0; c < contacts.size(); c++) {
                    contacts.get(c).setClientContactId(0);
                    contacts.get(c).setClientId(currentClient.getClientId());
                    clientController.saveClientContact(contacts.get(c), false);
                }
            }

            if (rates != null) {
                for (int c = 0; c < rates.size(); c++) {
                    rates.get(c).setClientRateCodeId(0);
                    rates.get(c).setClientId(currentClient.getClientId());
                    billingController.saveClientRateCode(rates.get(c));
                }
            }

            clientExport.setUskedClientId(0);
            clientExport.setClientId(currentClient.getClientId());
            String uskedWs = clientExport.getUskedCliId();
            clientExport.setUskedCliId(clientExport.getUskedWsId());
            clientExport.setUskedWsId(uskedWs);
            clientController.saveClientExport(clientExport, this.myConnection.myBranch);

            this.getData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Please select a client!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected ImageIcon getOtherButtonIcon() {
        return Main_Window.E24x24EMAILSEND;
    }

    @Override
    protected void otherAction() {
        Client currentClient = (Client) this.getSelectedObject();
        if (currentClient == null) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Please select a client!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication,
                    "Do you want to send the insurance company an email notification?",
                    "Send Notification?",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                InsuranceAdditionalDialog dialog
                        = new InsuranceAdditionalDialog(Main_Window.parentOfApplication, true);
                dialog.setVisible(true);
                String additionalInfo = dialog.getText();

                String textEmail = "";
                textEmail += "Attention Insurance Agent: <br/><br/>";
                textEmail += "We would like to request a certificate of insurance for the following client:<br/>";
                textEmail += "<b>Account Name:</b> " + currentClient.getClientName() + "<br/>";
                textEmail += "<b>Address 1:</b> " + currentClient.getAddress1() + "<br/>";
                textEmail += "<b>Address 2:</b> " + currentClient.getCity() + ", " + currentClient.getState() + " " + currentClient.getZip() + "<br/><br/>";

                if (additionalInfo != null && additionalInfo.length() > 0) {
                    textEmail += "<b>Additional Info:</b> " + additionalInfo + "<br/><br/>";
                }

                textEmail += "Champion National Security Inc.<br/>";
                textEmail += "972-235-8844 x128";

                String[] recipients = {"dpeterson@eldoradoinsurance.com", "bmccoy@champ.net", new SchedfoxEmail().getEmailAddressFrom()};

                try {

                    new SchedfoxEmail("Champion Security COI Request", textEmail, recipients, true);

                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Message Sent",
                            "Sent", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Error sending message!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void getData() {
        client_list_query myListQuery = new client_list_query();
        Record_Set rs = new Record_Set();
        myListQuery.update(deleted, "", "1", false);
        try {
            getConnection().prepQuery(myListQuery);
            rs = myConnection.executeQuery(myListQuery);
        } catch (Exception e) {
        }
        super.populateList(rs, "client_is_deleted", "1");
    }

    private JMenuItem addMenuItem(JMenu m, String t, ActionListener a) {
        JMenuItem mi = new JMenuItem(t);
        mi.addActionListener(a);
        m.add(mi);
        return mi;
    }

    /**
     * Used when you have sub components like for worksites pass in object
     * extended class will return what GraphicalListComp to add sub to...
     */
    @Override
    public GraphicalListComponent getSubParents(Object o) {
        Client c = ((Client) o);
        for (int i = 0; i < super.myListComponents.size(); i++) {
            Client temp = (Client) super.myListComponents.get(i).myObject;
            if (temp.getClientId().equals(c.getClientWorksite())) {
                return super.myListComponents.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean isSubComponent(Object o) {
        if (((Client) o).getClientWorksite() == 0) {
            return false;
        }
        return true;
    }

    public void deleteData() {
        if (this.currentSelectedObject == null) {
            return;
        }

        if (this.selectedIsMarkedDeleted()) {

            if (JOptionPane.showConfirmDialog(this, "Are you SURE you want to delete this location? (THIS CANNONT BE UNDONE!)", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                RunQueriesEx myQuery = new RunQueriesEx();

                myQuery.add(new GenericQuery("DELETE FROM client WHERE client_id = " + ((Client) currentSelectedObject).getClientId() + ";"));

                try {
                    this.getConnection().executeQueryEx(myQuery);
                } catch (Exception ex) {
                }

                this.getData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You can only delete inactive locations!", "Unable to delete location", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public String getWindowTitle() {
        try {
            if (currentSelectedObject == null) {
                return "Adding New Location For " + Main_Window.parentOfApplication.getCompanyNameById(cpny) + " in " + Main_Window.parentOfApplication.getBranchNameById(branch);
            }
            String windowtext = getDisplayNameForObject(currentSelectedObject);
            if (((Client) currentSelectedObject).getClientWorksite() != 0) {
                try {
                    windowtext = ((Client) getObjectById(((Client) currentSelectedObject).getClientWorksite() + "")).getClientName() + " " + windowtext;
                } catch (Exception e) {
                }
            }

            return "Editing Location - " + windowtext + " (" + Main_Window.parentOfApplication.getCompanyNameById(cpny) + ", " + Main_Window.parentOfApplication.getBranchNameById(branch) + ")";
        } catch (Exception e) {
            return "";
        }
    }

    public void showDeleted(boolean isPressed) {
        if (isPressed) {
            deleted = "1";
        } else {
            deleted = "0";
        }
        getData();
    }

    public void setVisible(boolean val) {
        if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.CLIENT_INFORMATION)) {
            super.setVisible(val);
        } else if (!val) {
            super.setVisible(val);
        }
    }

    public Connection getConnection() {
        return myConnection;
    }

    public boolean getToggleDeleted() {
        return true;
    }

    protected ImageIcon getDeletedUpIcon() {
        return Main_Window.Viewing_Active_Clients;
    }

    protected ImageIcon getDeletedDownIcon() {
        return Main_Window.Viewing_All_Clients;
    }

    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "(SELECT COALESCE ((MAX(client_id) + 1), 1) From client)";
        }
        return ((Client) currentSelectedObject).getClientId() + "";
    }

    public void exitForm() {
        setVisible(false);
        clearData();
    }

    public void clickedMenu(String action, String companyName, String branchName, String companyId, String branchId) {
        myConnection.setBranch(branchId);
        myConnection.setCompany(companyId);
        branch = branchId;
        cpny = companyId;
        getData();
        clearData();
    }

    /**
     * @return the deleted
     */
    public String getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    private class ClientSearchListener implements ActionListener {

        public ClientSearchListener() {
        }

        public void actionPerformed(ActionEvent e) {
            ClientSearch mySearch = new ClientSearch(Main_Window.parentOfApplication, true, thisobject.getConnection(), thisobject);
            mySearch.setVisible(true);
        }
    }
}
