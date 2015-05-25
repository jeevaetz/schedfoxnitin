/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.problemsolver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.ProblemSolverController;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;
import schedfoxlib.model.ProblemSolverType;
import schedfoxlib.model.Problemsolver;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class SearchProblemSolverDialog extends javax.swing.JDialog {

    private UserTableModel userTableModel = new UserTableModel();
    private ClientTableModel clientTableModel = new ClientTableModel();
    private ProblemTableModel problemTableModel = new ProblemTableModel();

    private HashMap<Integer, JCheckBox> myBranches;
    private HashMap<Integer, JCheckBox> myTypes;
    private HashMap<Integer, Branch> myBranchObjects;
    private HashMap<Integer, Client> myClientObjects;
    private HashMap<Integer, User> myUserObjects;

    private String companyId;
    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;

    /**
     * Creates new form SearchProblemSolverDialog
     */
    public SearchProblemSolverDialog(java.awt.Frame parent, boolean modal, String companyId) {
        super(parent, modal);
        initComponents();

        this.companyId = companyId;

        try {
            CompanyController companyController = new CompanyController();
            ArrayList<Branch> branches = companyController.getBranchesForCompany(Integer.parseInt(companyId));

            myBranches = new HashMap<Integer, JCheckBox>();
            myBranchObjects = new HashMap<Integer, Branch>();
            JCheckBox allBranchChecks = new JCheckBox("All Branches");
            allBranchChecks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isSelected = ((JCheckBox) e.getSource()).isSelected();
                    Iterator<Integer> keys = myBranches.keySet().iterator();
                    while (keys.hasNext()) {
                        myBranches.get(keys.next()).setSelected(isSelected);
                    }
                    loadDMInfo();
                    loadClientInfo();
                }
            });
            branchesPanel.add(allBranchChecks);
            for (int b = 0; b < branches.size(); b++) {
                Branch currBranch = branches.get(b);
                JCheckBox branchCheck = new JCheckBox(currBranch.getBranchName());
                branchCheck.setSelected(true);
                branchesPanel.add(branchCheck);
                myBranchObjects.put(currBranch.getBranchId(), currBranch);

                branchCheck.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadDMInfo();
                        loadClientInfo();
                    }
                });

                myBranches.put(currBranch.getBranchId(), branchCheck);
            }

            ProblemSolverController problemController = ProblemSolverController.getInstance(companyId);
            ArrayList<ProblemSolverType> types = problemController.getProblemSolverTypes();

            myTypes = new HashMap<Integer, JCheckBox>();
            JCheckBox allTypeshecks = new JCheckBox("All Types");
            allTypeshecks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isSelected = ((JCheckBox) e.getSource()).isSelected();
                    Iterator<Integer> keys = myTypes.keySet().iterator();
                    while (keys.hasNext()) {
                        myTypes.get(keys.next()).setSelected(isSelected);
                    }
                }
            });
            typePanel.add(allTypeshecks);
            for (int t = 0; t < types.size(); t++) {
                ProblemSolverType currType = types.get(t);
                JCheckBox typeCheck = new JCheckBox(currType.getTypeName());
                typeCheck.setSelected(true);
                typePanel.add(typeCheck);
                myTypes.put(currType.getProblemsolverTypeId(), typeCheck);
            }

            loadDMInfo();
            loadClientInfo();
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        
        try {
            begCal = new JCalendarComboBox();
            endCal = new JCalendarComboBox();
            
            Calendar bCal = Calendar.getInstance();
            bCal.setFirstDayOfWeek(Calendar.MONDAY);
            bCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            bCal.add(Calendar.WEEK_OF_YEAR, -1);
            begCal.setCalendar(bCal);
            
            Calendar eCal = Calendar.getInstance();
            eCal.setFirstDayOfWeek(Calendar.MONDAY);
            eCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            eCal.add(Calendar.DAY_OF_WEEK, 6);
            eCal.add(Calendar.WEEK_OF_YEAR, -1);
            endCal.setCalendar(eCal);
            
            datePanel.add(begCal);
            datePanel.add(endCal);
        } catch (Exception exe) {}
        
        dmTable.getColumnModel().getColumn(0).setMinWidth(45);
        dmTable.getColumnModel().getColumn(0).setMaxWidth(45);
        dmTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        
        clientTable.getColumnModel().getColumn(0).setMinWidth(45);
        clientTable.getColumnModel().getColumn(0).setMaxWidth(45);
        clientTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        
        problemTable.getColumnModel().getColumn(0).setMinWidth(70);
        problemTable.getColumnModel().getColumn(0).setMaxWidth(70);
        problemTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        
        problemTable.getColumnModel().getColumn(1).setMinWidth(100);
        problemTable.getColumnModel().getColumn(1).setMaxWidth(100);
        problemTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        
        problemTable.getColumnModel().getColumn(3).setMinWidth(140);
        problemTable.getColumnModel().getColumn(3).setMaxWidth(140);
        problemTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        
        problemTable.getColumnModel().getColumn(5).setMinWidth(70);
        problemTable.getColumnModel().getColumn(5).setMaxWidth(70);
        problemTable.getColumnModel().getColumn(5).setPreferredWidth(70);
    }

    private ArrayList<Integer> getSelectedBranches() {
        Iterator<Integer> keys = myBranches.keySet().iterator();
        ArrayList<Integer> branches = new ArrayList<Integer>();
        while (keys.hasNext()) {
            Integer key = keys.next();
            if (myBranches.get(key).isSelected()) {
                branches.add(key);
            }
        }
        return branches;
    }

    private void loadClientInfo() {
        try {
            ArrayList<Integer> branches = getSelectedBranches();
            myClientObjects = new HashMap<Integer, Client>();
            
            ClientController clientController = ClientController.getInstance(companyId);
            ArrayList<Client> clients = clientController.getClientsByBranch(branches);
            clientTableModel.setClients(clients);
            for (int c = 0; c < clients.size(); c++) {
                myClientObjects.put(clients.get(c).getClientId(), clients.get(c));
            }
        } catch (Exception exe) {
        }
    }

    private void loadDMInfo() {
        try {
            ArrayList<Integer> branches = getSelectedBranches();
            myUserObjects = new HashMap<Integer, User>();

            userTableModel.clear();
            if (branches.size() > 0) {
                UserController userController = new UserController(companyId);
                ArrayList<User> users = userController.getUsersByTypes("District Manager", branches);
                for (int u = 0; u < users.size(); u++) {
                    myUserObjects.put(users.get(u).getUserId(), users.get(u));
                    userTableModel.addUser(users.get(u));
                }
            }
        } catch (Exception exe) {
        }
    }
    
    private class ProblemTableModel extends AbstractTableModel {
        private ArrayList<Problemsolver> problems;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        
        public ProblemTableModel() {
            problems = new ArrayList<Problemsolver>();
        }

        public void clear() {
            this.problems.clear();
            super.fireTableDataChanged();
        }

        public Problemsolver getProblemAt(int row) {
            return problems.get(row);
        }
        
        public void setProblems(ArrayList<Problemsolver> problems) {
            this.problems = problems;
            super.fireTableDataChanged();
        }

        public void addProblem(Problemsolver problem) {
            this.problems.add(problem);
            super.fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return problems.size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "ID";
            } else if (column == 1) {
                return "Branch";
            } else if (column == 2) {
                return "Client";
            } else if (column == 3) {
                return "DM";
            } else if (column == 4) {
                return "Problem";
            } else if (column == 5) {
                return "Date";
            }
            return "";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return problems.get(rowIndex).getPsId();
            } else if (columnIndex == 1) {
                try {
                    return myBranchObjects.get(myClientObjects.get(problems.get(rowIndex).getClientId()).getBranchId());
                } catch (Exception exe) {}
            } else if (columnIndex == 2) {
                try {
                    return myClientObjects.get(problems.get(rowIndex).getClientId());
                } catch (Exception exe) {}
            } else if (columnIndex == 3) {
                try {
                    return myUserObjects.get(problems.get(rowIndex).getUserId()).getFullName();
                } catch (Exception exe) {}
            } else if (columnIndex == 4) {
                return problems.get(rowIndex).getProblem();
            } else if (columnIndex == 5) {
                return dateFormat.format(problems.get(rowIndex).getPsDate());
            }
             
            return "";
        }
    }
    
    private class ClientTableModel extends AbstractTableModel {
        private ArrayList<Client> clients;
        private ArrayList<Boolean> clientSelected;

        public ClientTableModel() {
            clients = new ArrayList<Client>();
            clientSelected = new ArrayList<Boolean>();
        }
        
        public void clear() {
            this.clients.clear();
            this.clientSelected.clear();
            super.fireTableDataChanged();
        }
        
        public ArrayList<Integer> getClientsSelected() {
            ArrayList<Integer> retVal = new ArrayList<Integer>();
            for (int s = 0; s < clientSelected.size(); s++) {
                if (clientSelected.get(s)) {
                    retVal.add(clients.get(s).getClientId());
                }
            }
            return retVal;
        }

        public void toggleSelected(Boolean value) {
            try {
                for (int u = 0; u < clientSelected.size(); u++) {
                    clientSelected.set(u, value);
                }
                super.fireTableDataChanged();
            } catch (Exception exe) {}
        }
        
        public void setClients(ArrayList<Client> clients) {
            this.clients = clients;
            clientSelected.clear();
            for (int u = 0; u < clients.size(); u++) {
                clientSelected.add(true);
            }
            super.fireTableDataChanged();
        }

        public void addClient(Client client) {
            this.clients.add(client);
            this.clientSelected.add(true);
            super.fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return clients.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }
        
        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "";
            } else if (column == 1) {
                return "Branch";
            } else if (column == 2) {
                return "Client Name";
            }
            return "";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return clientSelected.get(rowIndex);
            } else if (columnIndex == 1) {
                try {
                    return myBranchObjects.get(clients.get(rowIndex).getBranchId()).getBranchName();
                } catch (Exception exe) {}
            } else if (columnIndex == 2) {
                return clients.get(rowIndex).getClientName();
            }
            return "";
        }
        
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else {
                return String.class;
            }
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            try {
                if (columnIndex == 0) {
                    Boolean val = (Boolean) value;
                    this.clientSelected.set(rowIndex, val);
                }
            } catch (Exception exe) {
            }
        }
    }

    private class UserTableModel extends AbstractTableModel {

        private ArrayList<User> users;
        private ArrayList<Boolean> userSelected;

        public UserTableModel() {
            users = new ArrayList<User>();
            userSelected = new ArrayList<Boolean>();
        }

        public void clear() {
            this.users.clear();
            this.userSelected.clear();
            super.fireTableDataChanged();
        }
        
        public void toggleSelected(Boolean value) {
            try {
                for (int u = 0; u < userSelected.size(); u++) {
                    userSelected.set(u, value);
                }
                super.fireTableDataChanged();
            } catch (Exception exe) {}
        }
        
        public ArrayList<Integer> getClientsSelected() {
            ArrayList<Integer> retVal = new ArrayList<Integer>();
            for (int s = 0; s < userSelected.size(); s++) {
                if (userSelected.get(s)) {
                    retVal.add(users.get(s).getUserId());
                }
            }
            return retVal;
        }

        public void setUsers(ArrayList<User> users) {
            this.users = users;
            userSelected.clear();
            for (int u = 0; u < users.size(); u++) {
                userSelected.add(true);
            }
            super.fireTableDataChanged();
        }

        public void addUser(User user) {
            this.users.add(user);
            this.userSelected.add(true);
            super.fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "";
            } else if (column == 1) {
                return "First Name";
            } else if (column == 2) {
                return "Last Name";
            }
            return "";
        }

        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            try {
                User myUser = this.users.get(rowIndex);
                if (columnIndex == 0) {
                    return this.userSelected.get(rowIndex);
                } else if (columnIndex == 1) {
                    return myUser.getUserFirstName();
                } else if (columnIndex == 2) {
                    return myUser.getUserLastName();
                }
            } catch (Exception exe) {
            }
            return "";
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            try {
                if (columnIndex == 0) {
                    Boolean val = (Boolean) value;
                    this.userSelected.set(rowIndex, val);
                }
            } catch (Exception exe) {
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        branchesPanel = new javax.swing.JPanel();
        typePanel = new javax.swing.JPanel();
        datePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        searchTxt = new javax.swing.JTextField();
        userPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        selectAllDMsChk = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        dmTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        selectCientsChk = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        clientTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        searchBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        problemTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jPanel7.setMaximumSize(new java.awt.Dimension(320, 98301));
        jPanel7.setMinimumSize(new java.awt.Dimension(120, 100));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        branchesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Branches"));
        branchesPanel.setMinimumSize(new java.awt.Dimension(20, 140));
        branchesPanel.setPreferredSize(new java.awt.Dimension(242, 140));
        branchesPanel.setLayout(new java.awt.GridLayout(0, 2));
        jPanel7.add(branchesPanel);

        typePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Corp Types"));
        typePanel.setMaximumSize(new java.awt.Dimension(32767, 120));
        typePanel.setMinimumSize(new java.awt.Dimension(12, 120));
        typePanel.setPreferredSize(new java.awt.Dimension(320, 120));
        typePanel.setLayout(new java.awt.GridLayout(0, 2));
        jPanel7.add(typePanel);

        datePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Dates"));
        datePanel.setMaximumSize(new java.awt.Dimension(32767, 80));
        datePanel.setPreferredSize(new java.awt.Dimension(320, 80));
        datePanel.setLayout(new java.awt.GridLayout(2, 0));
        jPanel7.add(datePanel);

        jPanel4.add(jPanel7);

        jPanel1.setMaximumSize(new java.awt.Dimension(450, 18000));
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 180));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Text"));
        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 70));
        jPanel8.setMinimumSize(new java.awt.Dimension(0, 70));
        jPanel8.setName(""); // NOI18N
        jPanel8.setPreferredSize(new java.awt.Dimension(450, 70));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));
        jPanel8.add(searchTxt);

        jPanel1.add(jPanel8);

        userPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected DMs"));
        userPanel.setLayout(new javax.swing.BoxLayout(userPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setLayout(new java.awt.GridLayout());

        selectAllDMsChk.setSelected(true);
        selectAllDMsChk.setText("Select all DMs");
        selectAllDMsChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllDMsChkActionPerformed(evt);
            }
        });
        jPanel9.add(selectAllDMsChk);

        userPanel.add(jPanel9);

        dmTable.setAutoCreateRowSorter(true);
        dmTable.setModel(userTableModel);
        jScrollPane2.setViewportView(dmTable);

        userPanel.add(jScrollPane2);

        jPanel1.add(userPanel);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Clients"));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        jPanel10.setLayout(new java.awt.GridLayout());

        selectCientsChk.setSelected(true);
        selectCientsChk.setText("Select all Clients");
        selectCientsChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCientsChkActionPerformed(evt);
            }
        });
        jPanel10.add(selectCientsChk);

        jPanel5.add(jPanel10);

        clientTable.setAutoCreateRowSorter(true);
        clientTable.setModel(clientTableModel);
        jScrollPane3.setViewportView(clientTable);

        jPanel5.add(jScrollPane3);

        jPanel1.add(jPanel5);

        jPanel4.add(jPanel1);

        jPanel6.add(jPanel4);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel2.setPreferredSize(new java.awt.Dimension(780, 40));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        jPanel2.add(searchBtn);

        jPanel6.add(jPanel2);

        jTabbedPane1.addTab("Search Parameters", jPanel6);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 34));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 34));
        jPanel3.setPreferredSize(new java.awt.Dimension(592, 34));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        problemTable.setAutoCreateRowSorter(true);
        problemTable.setModel(problemTableModel);
        problemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                problemTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(problemTable);

        jPanel3.add(jScrollPane1);

        jTabbedPane1.addTab("Results", jPanel3);

        getContentPane().add(jTabbedPane1);

        setSize(new java.awt.Dimension(803, 678));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        ArrayList<Integer> selectedClients = clientTableModel.getClientsSelected();
        ArrayList<Integer> selectedDMs = userTableModel.getClientsSelected();
        ArrayList<Integer> selectedTypes = new ArrayList<Integer>();
        Iterator<Integer> keys = this.myTypes.keySet().iterator();
        while (keys.hasNext()) {
            Integer key = keys.next();
            if (this.myTypes.get(key).isSelected()) {
                selectedTypes.add(key);
            }
        }
        ProblemSolverController problemController = ProblemSolverController.getInstance(this.companyId);
        ArrayList<Problemsolver> problems = problemController.getProblemSolversByParams(selectedClients, selectedDMs, selectedTypes, begCal.getCalendar().getTime(), endCal.getCalendar().getTime(), searchTxt.getText());
        problemTableModel.clear();
        if (problems.size() > 0) {
            problemTableModel.setProblems(problems);
            jTabbedPane1.setSelectedIndex(1);
        }
    }//GEN-LAST:event_searchBtnActionPerformed

    private void problemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_problemTableMouseClicked
        if (evt.getClickCount() > 1) {
            Problemsolver problem = this.problemTableModel.getProblemAt(problemTable.getSelectedRow());
            final ProblemSolverDisplay display = new ProblemSolverDisplay(problem, Integer.parseInt(this.companyId));
            jTabbedPane1.addTab("Problem " + problem.getId(), display);
            jTabbedPane1.setSelectedComponent(display);
            
            ActionListener tabListener = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    jTabbedPane1.remove(display);
                }
            };
            display.addBtnActionListener(tabListener);
        }
    }//GEN-LAST:event_problemTableMouseClicked

    private void selectAllDMsChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllDMsChkActionPerformed
        this.userTableModel.toggleSelected(selectAllDMsChk.isSelected());
    }//GEN-LAST:event_selectAllDMsChkActionPerformed

    private void selectCientsChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCientsChkActionPerformed
        this.clientTableModel.toggleSelected(selectCientsChk.isSelected());
    }//GEN-LAST:event_selectCientsChkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel branchesPanel;
    private javax.swing.JTable clientTable;
    private javax.swing.JPanel datePanel;
    private javax.swing.JTable dmTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable problemTable;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JCheckBox selectAllDMsChk;
    private javax.swing.JCheckBox selectCientsChk;
    private javax.swing.JPanel typePanel;
    private javax.swing.JPanel userPanel;
    // End of variables declaration//GEN-END:variables
}
