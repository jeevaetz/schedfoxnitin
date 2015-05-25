/*
 * Client_ProblemSolver.java
 *
 * Created on August 11, 2006, 2:03 PM
 */
package rmischedule.client.components;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListDataListener;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.lang3.StringUtils;
import rmischedule.client.xClientEdit;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.data_connection.Connection;
import rmischedule.ireports.viewer.IReportViewer;
import schedfoxlib.model.Company;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.*;
import rmischedule.main.Main_Window;
import rmischedule.messaging.email.SchedfoxEmail;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.ProblemSolverController;
import rmischeduleserver.control.UserController;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.save_problem_for_client_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_by_group_query;

/**
 *
 * @author shawn
 */
public class Client_ProblemSolver extends GenericEditSubForm {

    private String emailContacts[];

    private UserComboModel userComboModel = new UserComboModel();

    /**
     * Creates new form Client_ProblemSolver
     */
    public Client_ProblemSolver() {
        initComponents();

        this.problemSolverList.setCellRenderer(new ProblemSolverCellRenderer());
    }

    public boolean userHasAccess() {
        return true;
    }

    public void doOnClear() {
        this.problemSolverList.setModel(new DefaultListModel());
        this.editButton.setEnabled(false);
        this.deleteButton.setEnabled(false);
        this.addButton.setEnabled(false);
        this.printButton.setEnabled(false);
        this.emailButton.setEnabled(false);

        try {
            notifyCheckbox.setSelected(false);
            minNotifySpinner.setEnabled(false);
            minNotifySpinner.setValue(15);
        } catch (Exception exe) {
        }
    }

    public JPanel getMyForm() {
        return this;
    }

    public String getMyTabTitle() {
        return "CC";
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    private void loadUserInformation() {
        try {
            UserController userController = new UserController(super.myparent.getConnection().myCompany);
            ArrayList<User> users = userController.getUsersByTypes("District Manager", "-1");
            userComboModel = new UserComboModel();
            userComboModel.setUsers(users);
            emailCombo.setModel(userComboModel);

            emailCombo.repaint();
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public void loadData(Record_Set rs) {
        this.addButton.setEnabled(true);
        this.loadUserInformation();

        Problemsolver[] problemSolvers = new Problemsolver[rs.length()];
        for (int i = 0; i < rs.length(); i++) {
            Problemsolver ps = new Problemsolver(new Date(), rs);
            problemSolvers[i] = ps;
            rs.moveNext();
        }
        this.problemSolverList.setListData(problemSolvers);
        this.problemSolverList.revalidate();

        try {
            ClientController clientController = ClientController.getInstance(super.myparent.getConnection().myCompany);
            User clientUser = clientController.getDMAssignedToClient(Integer.parseInt(super.myparent.getMyIdForSave()));
            if (clientUser != null) {
                userComboModel.setSelectedItem(clientUser.getUserId());
            }

            try {
                Client selClient = (Client) super.myparent.getSelectedObject();
                if (selClient.getNotifyDmLateCheckoutMinutes() > -1) {
                    notifyCheckbox.setSelected(true);
                    minNotifySpinner.setEnabled(true);
                    minNotifySpinner.setValue(selClient.getNotifyDmLateCheckoutMinutes());
                }
            } catch (Exception exe) {
            }

            emailCombo.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeneralQueryFormat getSaveQuery(boolean b) {
        client_save_problem_contact_query saveQuery = new client_save_problem_contact_query();
        try {
            int myClientInt = Integer.parseInt(super.myparent.getMyIdForSave());

            try {
                save_client_dm_notification_query saveDMQuery = new save_client_dm_notification_query();
                if (notifyCheckbox.isSelected()) {
                    saveDMQuery.setPreparedStatement(new Object[]{Integer.parseInt(minNotifySpinner.getValue().toString()), myClientInt});
                } else {
                    saveDMQuery.setPreparedStatement(new Object[]{-1, myClientInt});
                }
                saveDMQuery.setCompany(myparent.getConnection().myCompany);
                    super.myparent.getConnection().executeUpdate(saveDMQuery);
            } catch (Exception exe) {
            }
            User usr = (User) emailCombo.getSelectedItem();
            saveQuery.setPreparedStatement(new Object[]{myClientInt, myClientInt, usr.getUserId()});
            super.myparent.getConnection().executeQuery(saveQuery);
        } catch (Exception e) {
            return null;
        }
        return saveQuery;
    }

    public GeneralQueryFormat getQuery(boolean b) {
        return new get_problem_solvers_query(this.myparent.getMyIdForSave());
    }

    private void saveProblemSolver(Problemsolver ps) {
        save_problem_for_client_query saveQuery = new save_problem_for_client_query();
        saveQuery.update(ps);
        this.myparent.getConnection().prepQuery(saveQuery);
        this.myparent.getConnection().executeUpdate(saveQuery);
    }

    /**
     * This retrieves the problem solver from the database. Mainly to get the id
     * for our email function.
     *
     * @param ps
     * @return
     */
    private Problemsolver retrieveProblemsolverFromDB(Problemsolver ps) {
        get_last_problem_solver_query getLastProblem = new get_last_problem_solver_query();
        getLastProblem.setPreparedStatement(new Object[]{ps.getClientId(), ps.getProblem()});
        this.myparent.getConnection().prepQuery(getLastProblem);
        return new Problemsolver(new Date(), myparent.getConnection().executeQuery(getLastProblem));
    }

    public void reloadData() {
        this.clearData();
        get_problem_solvers_query getQuery = new get_problem_solvers_query();
        getQuery.setPreparedStatement(new Object[]{Integer.parseInt(myparent.getMyIdForSave())});
        this.myparent.getConnection().prepQuery(getQuery);
        this.loadData(this.myparent.getConnection().executeQuery(getQuery));
    }

    private void displayEmailFormForProblemSolver(Problemsolver ps) {
        Client client = (Client) super.myparent.getSelectedObject();
        ArrayList<ClientContact> contacts = client.getContacts(super.myparent.getConnection().myCompany);

        ProblemsolverEmailDialog emailDiag = new ProblemsolverEmailDialog(
                Main_Window.parentOfApplication,
                true,
                ps,
                ((xClientEdit) super.myparent).branch,
                super.myparent.getConnection(),
                contacts,
                this.getReportOfCorpCommunicator(ps));
        emailDiag.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emailDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        emailList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        cancelDialog = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        emailCombo = new javax.swing.JComboBox();
        notifyCheckbox = new javax.swing.JCheckBox();
        minNotifySpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        labelPanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        problemLabel = new javax.swing.JLabel();
        problemSolverScrollPane = new javax.swing.JScrollPane();
        problemSolverList = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        printButton = new javax.swing.JButton();
        emailButton = new javax.swing.JButton();
        spacerPanel = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();

        emailDialog.setTitle("Email");
        emailDialog.setAlwaysOnTop(true);
        emailDialog.setModal(true);
        emailDialog.getContentPane().setLayout(new javax.swing.BoxLayout(emailDialog.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setText("Please Select Email Recepiants");
        jPanel4.add(jLabel2);

        emailDialog.getContentPane().add(jPanel4);

        emailList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        emailList.setPreferredSize(new java.awt.Dimension(350, 95));
        jScrollPane1.setViewportView(emailList);

        jPanel2.add(jScrollPane1);

        emailDialog.getContentPane().add(jPanel2);

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        cancelDialog.setText("Cancel");
        cancelDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDialogActionPerformed(evt);
            }
        });
        jPanel3.add(cancelDialog);

        emailDialog.getContentPane().add(jPanel3);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 8, 1, 1));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel1.setMinimumSize(new java.awt.Dimension(38, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 40));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jLabel1.setText("Account DM");
        jLabel1.setMaximumSize(new java.awt.Dimension(75, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(75, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(75, 16));
        jPanel1.add(jLabel1);

        emailCombo.setModel(userComboModel);
        emailCombo.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel1.add(emailCombo);

        notifyCheckbox.setText("Notify DM on late checkin after");
        notifyCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notifyCheckboxActionPerformed(evt);
            }
        });
        jPanel1.add(notifyCheckbox);

        minNotifySpinner.setEnabled(false);
        minNotifySpinner.setMaximumSize(new java.awt.Dimension(50, 26));
        minNotifySpinner.setMinimumSize(new java.awt.Dimension(50, 26));
        minNotifySpinner.setPreferredSize(new java.awt.Dimension(50, 26));
        jPanel1.add(minNotifySpinner);

        jLabel3.setText(" minutes");
        jPanel1.add(jLabel3);

        add(jPanel1);

        labelPanel.setLayout(new javax.swing.BoxLayout(labelPanel, javax.swing.BoxLayout.LINE_AXIS));

        dateLabel.setText("    Date");
        dateLabel.setMaximumSize(new java.awt.Dimension(96, 19));
        dateLabel.setMinimumSize(new java.awt.Dimension(96, 19));
        dateLabel.setPreferredSize(new java.awt.Dimension(96, 19));
        labelPanel.add(dateLabel);

        problemLabel.setText("Problem");
        problemLabel.setMaximumSize(new java.awt.Dimension(32767, 19));
        problemLabel.setMinimumSize(new java.awt.Dimension(38, 19));
        problemLabel.setPreferredSize(new java.awt.Dimension(38, 19));
        labelPanel.add(problemLabel);

        add(labelPanel);

        problemSolverScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        problemSolverScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        problemSolverList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        problemSolverList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                problemSolverListMouseClicked(evt);
            }
        });
        problemSolverList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                problemSolverListValueChanged(evt);
            }
        });
        problemSolverScrollPane.setViewportView(problemSolverList);

        add(problemSolverScrollPane);

        buttonPanel.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel, javax.swing.BoxLayout.LINE_AXIS));

        printButton.setText("Print Preview");
        printButton.setEnabled(false);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(printButton);

        emailButton.setText("E-Mail");
        emailButton.setEnabled(false);
        emailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(emailButton);

        spacerPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        spacerPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        spacerPanel.setLayout(new java.awt.BorderLayout());
        buttonPanel.add(spacerPanel);

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(deleteButton);

        editButton.setText("View/Edit");
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(editButton);

        addButton.setText("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(addButton);

        add(buttonPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void emailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailButtonActionPerformed
        Problemsolver ps = (Problemsolver) this.problemSolverList.getSelectedValue();
        this.displayEmailFormForProblemSolver(ps);

    }//GEN-LAST:event_emailButtonActionPerformed
    private void sendEmail(Problemsolver ps) {
        try {
            JasperPrint corpReport = this.getReportOfCorpCommunicator(ps);
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(corpReport, oStream);

            int sel[] = emailList.getSelectedIndices();

            if (sel.length == 0) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Please Select an email Recepiant!",
                        "Error Sending Email", JOptionPane.ERROR_MESSAGE);
            } else {
                String subject = "Corporate Communicator";
                String body = "Attached you will find a corporate communicator pdf.";
                String[] contactArray = new String[sel.length];
                for (int vc = 0; vc < sel.length; vc++) {
                    contactArray[vc] = emailContacts[sel[vc]];
                }

                try {
                    ProblemsolverEmail email = new ProblemsolverEmail();
                    email.setCcd(null);
                    email.setSentTo(StringUtils.join(contactArray, ","));
                    email.setPsId(ps.getId());
                    email.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));

                    ProblemSolverController controller = ProblemSolverController.getInstance(super.myparent.getConnection().myCompany);
                    controller.saveProblemSolverEmail(email);

                } catch (Exception exe) {
                }

                new SchedfoxEmail(subject, body, contactArray, oStream.toByteArray(), true, "CorporateCommunicator");
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Sent Corporate Communicator to client!",
                        "Sent CC", JOptionPane.INFORMATION_MESSAGE);
                emailDialog.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Could not send email! Error: \r\n"
                    + e.getLocalizedMessage(), "Error Sending Email", JOptionPane.ERROR_MESSAGE);
            emailDialog.dispose();
        }
    }

    private JasperPrint getReportOfCorpCommunicator(Problemsolver ps) {
        try {
            Company myCompany = Main_Window.getCompanyById(super.myparent.getConnection().myCompany);
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/corporate_communicator.jasper");

            HashMap parameters = new HashMap();
            parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");
            parameters.put("active_db", myCompany.getDB());
            parameters.put("ps_id", ps.getPsId());
            return JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        Problemsolver ps = (Problemsolver) this.problemSolverList.getSelectedValue();
        JasperPrint report = this.getReportOfCorpCommunicator(ps);
        if (report != null) {
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        }
    }//GEN-LAST:event_printButtonActionPerformed

    private void problemSolverListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_problemSolverListMouseClicked
        if (evt.getClickCount() > 1 && this.problemSolverList.getSelectedIndex() != -1) {
            this.editButton.doClick();
        }
    }//GEN-LAST:event_problemSolverListMouseClicked

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        //  ensure user wants to delete
        int answer = JOptionPane.showConfirmDialog(this, "Do you wish to delete the highlighted Problem Solver?",
                "Delete Problem Solver", JOptionPane.OK_CANCEL_OPTION);
        if (answer == 0) {
            //  set cursor to wait
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            Problemsolver ps = (Problemsolver) this.problemSolverList.getSelectedValue();

            //  create, prep, execute query
            delete_problem_solver_query query = new delete_problem_solver_query(ps.getPsId());
            Connection myConnection = super.getMyParent().getConnection();
            myConnection.prepQuery(query);
            myConnection.executeQuery(query);

            //  reload form
            this.reloadData();

            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);

            JOptionPane.showMessageDialog(this, "Deletion successful.", "Delete Problem Solver", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        EditProblemSolverDialog ed = new EditProblemSolverDialog(Main_Window.parentOfApplication, true,
                (Problemsolver) this.problemSolverList.getSelectedValue(),
                Integer.parseInt(super.myparent.getConnection().myCompany));
        ed.setVisible(true);

        if (ed.getSavedPS() != null) {
            Problemsolver ps = ed.getSavedPS();
            try {
                ps.setClientId(Integer.parseInt(this.myparent.getMyIdForSave()));
            } catch (Exception e) {
                ps.setClientId(0);
            }

            this.saveProblemSolver(ps);
            this.reloadData();
        }
        ed.dispose();
    }//GEN-LAST:event_editButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        EditProblemSolverDialog ed = new EditProblemSolverDialog(Main_Window.parentOfApplication, true,
                Integer.parseInt(super.myparent.getConnection().myCompany));
        ed.setVisible(true);

        if (ed.getSavedPS() != null) {
            Problemsolver ps = ed.getSavedPS();
            try {
                ps.setClientId(Integer.parseInt(this.myparent.getMyIdForSave()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Error Saving", "Error Saving", JOptionPane.ERROR_MESSAGE);
            }
            try {
                ps.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            } catch (Exception e) {
                ps.setUserId(0);
            }

            this.saveProblemSolver(ps);

            try {
                this.displayEmailFormForProblemSolver(retrieveProblemsolverFromDB(ps));
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.reloadData();
        }
        ed.dispose();
    }//GEN-LAST:event_addButtonActionPerformed

    private void problemSolverListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_problemSolverListValueChanged
        if (!evt.getValueIsAdjusting()) {
            if (this.problemSolverList.getSelectedIndex() == -1) {
                this.editButton.setEnabled(false);
                this.deleteButton.setEnabled(false);
                this.printButton.setEnabled(false);
                this.emailButton.setEnabled(false);
            } else {
                this.editButton.setEnabled(true);
                this.deleteButton.setEnabled(true);
                this.printButton.setEnabled(true);
                this.emailButton.setEnabled(true);
            }
        }
    }//GEN-LAST:event_problemSolverListValueChanged

    private void cancelDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogActionPerformed
        emailDialog.dispose();
    }//GEN-LAST:event_cancelDialogActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Problemsolver ps = (Problemsolver) this.problemSolverList.getSelectedValue();
        sendEmail(ps);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void notifyCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notifyCheckboxActionPerformed
        minNotifySpinner.setEnabled(notifyCheckbox.isSelected());
    }//GEN-LAST:event_notifyCheckboxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelDialog;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton emailButton;
    private javax.swing.JComboBox emailCombo;
    private javax.swing.JDialog emailDialog;
    private javax.swing.JList emailList;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel labelPanel;
    private javax.swing.JSpinner minNotifySpinner;
    private javax.swing.JCheckBox notifyCheckbox;
    private javax.swing.JButton printButton;
    private javax.swing.JLabel problemLabel;
    private javax.swing.JList problemSolverList;
    private javax.swing.JScrollPane problemSolverScrollPane;
    private javax.swing.JPanel spacerPanel;
    // End of variables declaration//GEN-END:variables

    private class UserComboModel implements ComboBoxModel {

        private int selectedIndex = -1;
        private ArrayList<User> users = new ArrayList<User>();

        public ArrayList<User> getUsers() {
            return this.users;
        }

        public void setUsers(ArrayList<User> users) {
            this.users = users;
        }

        public void setSelectedItem(Object anItem) {
            selectedIndex = -1;
            if (anItem instanceof User) {
                User currUser = (User) anItem;
                for (int u = 0; u < users.size(); u++) {
                    if (currUser.getUserId().equals(users.get(u).getUserId())) {
                        selectedIndex = u;
                    }
                }
            } else if (anItem instanceof Integer) {
                for (int u = 0; u < users.size(); u++) {
                    if (anItem.equals(users.get(u).getUserId())) {
                        selectedIndex = u;
                    }
                }
            }
        }

        public void setSelectedIndex(int newIndex) {
            selectedIndex = newIndex;
        }

        public Object getSelectedItem() {
            if (selectedIndex >= 0) {
                return users.get(selectedIndex);
            }
            return "Please select a Manager";
        }

        public int getSize() {
            return users.size();
        }

        public Object getElementAt(int index) {
            return users.get(index);
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    }
}
