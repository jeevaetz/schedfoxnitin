/*
 * xClientEdit.java
 *
 * Created on September 7, 2005, 2:47 PM
 *
 * Copyright: SchedFox 2005
 */
package rmischedule.client.constant_contact;

import java.awt.Color;
import schedfoxlib.model.util.Record_Set;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.data_connection.*;
import rmischedule.main.*;
import rmischedule.security.*;
import javax.swing.*;
import java.util.Date;
import rmischedule.client.components.EditProblemSolverDialog;
import rmischeduleserver.control.ClientRatingController;

import schedfoxlib.model.Client;
import schedfoxlib.model.ClientRating;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import schedfoxlib.model.ClientDisplayContact;
import schedfoxlib.model.Problemsolver;

/**
 *
 * @author Ira Juneau
 */
public class ConstantContact extends GenericTabbedEditForm implements CompanyBranchMenuInterface {

    private Connection myConnection;
    private String deleted;
    private ClientContactInfo clientContact;
    private ContactProblemSolver problemSolver;

    /**
     * Creates a new instance of xClientEdit
     */
    public ConstantContact() {
        myConnection = new Connection();

        clientContact = new ClientContactInfo();
        problemSolver = new ContactProblemSolver();
        super.addSubForm(clientContact);
        super.addSubForm(new ClientContactNotes());
        super.addSubForm(problemSolver);

    }

    @Override
    protected boolean hasAddData() {
        return false;
    }

    @Override
    protected boolean hasDelete() {
        return false;
    }

    @Override
    protected String getOtherButtonString() {
        return "New Corporate Communicator";
    }

    @Override
    protected String getSaveString() {
        return "Mark as called";
    }

    public void setInformation(String co, String br) {
        myConnection.setCompany(co);
        myConnection.setBranch(br);

        getData();
    }

    public ImageIcon getOtherButtonIcon() {
        return Main_Window.Add_Data_24x24;
    }

    @Override
    public void otherAction() {
        EditProblemSolverDialog ed = new EditProblemSolverDialog(Main_Window.parentOfApplication, true,
                Integer.parseInt(getConnection().myCompany));
        ed.setVisible(true);

        if (ed.getSavedPS() != null) {
            Problemsolver ps = ed.getSavedPS();
            try {
                ps.setClientId(Integer.parseInt(getMyIdForSave()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Error Saving", "Error Saving", JOptionPane.ERROR_MESSAGE);
            }
            try {
                ps.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            } catch (Exception e) {
                ps.setUserId(0);
            }

            problemSolver.saveProblemSolver(ps);
            try {
                problemSolver.displayEmailFormForProblemSolver(problemSolver.retrieveProblemsolverFromDB(ps));
            } catch (Exception e) {
                e.printStackTrace();
            }
            problemSolver.reloadData();
        }
        ed.dispose();
        getData();
    }

    public void addMyMenu(JMenuBar myMenu) {
        JMenu mainMenu = new JMenu("Select Branch/Company");
        Main_Window.parentOfApplication.setUpMenuOfCompsAndBranches(mainMenu, this, "");
        myMenu.add(mainMenu);
    }
    
    @Override
    public void runAfterLoad() {
        try {
            for (int m = 0; m < myListComponents.size(); m++) {
                GraphicalListComponent currentItem = myListComponents.get(m);
                ClientDisplayContact displayContact = (ClientDisplayContact)currentItem.getObject();
                if (displayContact.getLastWeekTotal() == 0 && displayContact.getCurrentWeekTotal() == 0) {
                    currentItem.setBackground(new Color(224, 90, 90));
                } else if (displayContact.getCurrentWeekTotal() > 0) {
                    currentItem.setBackground(new Color(90, 224, 90));
                }
            }
        } catch (Exception exe) {}
    }

    public Object createObjectForList(Record_Set rs) {
        return new ClientDisplayContact(new Date(), rs);
    }

    public String getDisplayNameForObject(Object input) {
        return ((Client) input).getClientName();
    }

    public void markAsContacted(boolean isEmail) {
        Integer messageResolution = 1;
        if (!isEmail) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            ButtonGroup group = new ButtonGroup();
            JRadioButton messageRadio = new JRadioButton("Left Message");
            JRadioButton contactRadio = new JRadioButton("Made Contact with Client");
            messageRadio.setSelected(true);
            group.add(messageRadio);
            group.add(contactRadio);

            panel.add(messageRadio);
            panel.add(contactRadio);
            JOptionPane.showOptionDialog(null, panel,
                    "Call Status?", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.OK_OPTION, null, null, null);

            if (messageRadio.isSelected()) {
                messageResolution = 2;
            }
        }
        save_client_contact_for_dm_query saveDMQuery = new save_client_contact_for_dm_query();

        saveDMQuery.setPreparedStatement(new Object[]{Integer.parseInt(this.getMyIdForSave()),
            Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId()), clientContact.getRating(), isEmail, messageResolution});
        try {
            getConnection().prepQuery(saveDMQuery);
            myConnection.executeUpdate(saveDMQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getData();
    }

    @Override
    public void saveData() {
        Integer parentId = Integer.parseInt(this.getMyIdForSave());
        markAsContacted(false);

        ClientRating clientRating = new ClientRating();
        clientRating.setClientId(parentId);
        clientRating.setCustomer_rating(clientContact.getRating());
        clientRating.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));

        ClientRatingController controller = ClientRatingController.getInstance(myConnection.myCompany);
        try {
            controller.saveRating(clientRating);
        } catch (Exception exe) {
            exe.printStackTrace();
        }

        hasChanged = false;
    }

    @Override
    public void getData() {
        Record_Set rs = new Record_Set();
        GeneralQueryFormat myListQuery = null;
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(this.getConnection(), "Corporate User", "Courtesy Call Manager")) {
            myListQuery = new get_clients_to_contact_for_corporate_query();
            myListQuery.setPreparedStatement(new Object[]{});
        } else {
            int userId = Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId());
            myListQuery = new get_clients_to_contact_for_dm_query();
            myListQuery.setPreparedStatement(new Object[]{
                userId, userId, userId, userId, userId});

        }
        if (myListQuery != null) {
            try {
                getConnection().prepQuery(myListQuery);
                rs = myConnection.executeQuery(myListQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.populateList(rs, "client_is_deleted", "1");
        }
    }

    @Override
    public boolean isSubComponent(Object o) {
        return false;
    }

    public void deleteData() {
    }

    public String getWindowTitle() {
        return "Client Call Queue ? CC’s";
    }

    protected void showDeleted(boolean isPressed) {
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
        getData();
        clearData();
    }
}
