/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CompanyViewOptions.java
 *
 * Created on Aug 9, 2010, 4:01:02 PM
 */
package rmischedule.xadmin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import rmischedule.data_connection.Connection;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.CompanyInformationObj;
import schedfoxlib.model.CompanyView;
import rmischeduleserver.mysqlconnectivity.queries.admin.save_company_information_view_query;
import schedfoxlib.model.Group;

/**
 *
 * @author user
 */
public class CompanyViewOptions extends javax.swing.JPanel {

    private Vector<CompanyInformationObj> companyInfo;

    /** Creates new form CompanyViewOptions */
    public CompanyViewOptions(Vector<CompanyInformationObj> companyInfo) {
        initComponents();
        this.companyInfo = companyInfo;
        this.initDataComp();
    }

    private void initDataComp() {
        for (int c = 0; c < companyInfo.size(); c++) {
            CompanyInformationObj obj = companyInfo.get(c);
            CompanyView compView = obj.getCompanyViewObj(obj.getCompany_id() + "");
            if (compView.getViewType() == 1) {
                this.initRadioButton(obj);
            } else if (compView.getViewType() == 2) {
                this.initWeeklyButton(obj);
            } else if (compView.getViewType() == 3) {
                this.initSpinnerButton(obj);
            } else if (compView.getViewType() == 4) {
                this.initGroupButton(obj);
            }
        }
    }

    private void initSpinnerButton(CompanyInformationObj obj) {
        JPanel groupPanel = new JPanel();
        FlowLayout myFlowLayout = new FlowLayout();
        myFlowLayout.setAlignment(FlowLayout.LEFT);
        groupPanel.setLayout(myFlowLayout);

        SpinnerNumberModel numberModel =
                new SpinnerNumberModel(Integer.parseInt(obj.getOption_value()), 0, 59, 1);
        JSpinner mySpinner = new JSpinner(numberModel);
        JLabel myLabel = new JLabel(obj.getOption_key());
        myLabel.setPreferredSize(new Dimension(140, 18));

        mySpinner.addChangeListener(new SpinnerChangeListener(mySpinner, obj));

        groupPanel.add(myLabel);
        groupPanel.add(mySpinner);
        
        contentPane.add(groupPanel);
    }

    private void initWeeklyButton(CompanyInformationObj obj) {
        JPanel groupPanel = new JPanel();
        FlowLayout myFlowLayout = new FlowLayout();
        myFlowLayout.setAlignment(FlowLayout.LEFT);
        groupPanel.setLayout(myFlowLayout);
        JComboBox weekDayCombo = new JComboBox();
        WeeklyComboModel comboModel = new WeeklyComboModel();
        weekDayCombo.setModel(comboModel);
        comboModel.setSelectedValue(obj.getOption_value());
        
        weekDayCombo.addActionListener(new ComboListener(comboModel, obj));
        JLabel myLabel = new JLabel(obj.getOption_key());
        myLabel.setPreferredSize(new Dimension(190, 18));
        groupPanel.add(myLabel);
        groupPanel.add(weekDayCombo);
        contentPane.add(groupPanel);
    }

    private void initGroupButton(CompanyInformationObj obj) {
        UserController userController = new UserController(obj.getCompany_id() + "");
        JPanel groupPanel = new JPanel();
        FlowLayout myFlowLayout = new FlowLayout();
        myFlowLayout.setAlignment(FlowLayout.LEFT);
        groupPanel.setLayout(myFlowLayout);
        JComboBox weekDayCombo = new JComboBox();
        try {
            ArrayList<Group> groups = userController.getGroups();
            GroupComboModel comboModel = new GroupComboModel(groups);
            weekDayCombo.setModel(comboModel);
            comboModel.setSelectedValue(obj.getOption_value());

            weekDayCombo.addActionListener(new ComboListener(comboModel, obj));
            JLabel myLabel = new JLabel(obj.getOption_key());
            myLabel.setPreferredSize(new Dimension(190, 18));
            groupPanel.add(myLabel);
            groupPanel.add(weekDayCombo);
            contentPane.add(groupPanel);
        } catch (Exception exe) {}
    }
    
    private void initRadioButton(CompanyInformationObj obj) {
        JPanel groupPanel = new JPanel();
        FlowLayout myFlowLayout = new FlowLayout();
        myFlowLayout.setAlignment(FlowLayout.LEFT);
        groupPanel.setLayout(myFlowLayout);
        ButtonGroup group = new ButtonGroup();

        JRadioButton viewButton = new JRadioButton("Show");
        JRadioButton hideButton = new JRadioButton("Hide");
        group.add(viewButton);
        group.add(hideButton);
        viewButton.addActionListener(new CheckListener(viewButton, hideButton, obj));
        hideButton.addActionListener(new CheckListener(viewButton, hideButton, obj));
        if (obj.getOption_value().equals("true")) {
            viewButton.setSelected(true);
        } else {
            hideButton.setSelected(true);
        }
        groupPanel.add(viewButton);
        groupPanel.add(hideButton);
        JLabel myLabel = new JLabel(obj.getOption_key());
        myLabel.setPreferredSize(new Dimension(140, 18));
        groupPanel.add(myLabel);

        contentPane.add(groupPanel);
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
        contentPane = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout());

        contentPane.setLayout(new javax.swing.BoxLayout(contentPane, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(contentPane);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private class SpinnerChangeListener implements ChangeListener {

        private CompanyInformationObj obj;
        private JSpinner jspinner;

        public SpinnerChangeListener(JSpinner jspinner, CompanyInformationObj compObj) {
            this.obj = compObj;
            this.jspinner = jspinner;
        }

        public void stateChanged(ChangeEvent e) {
            save_company_information_view_query saveQuery = new save_company_information_view_query();
            saveQuery.update(obj.getCompany_view_options_id(), obj.getCompany_id(), obj.getCompany_view_id(), jspinner.getValue().toString());
            (new Connection()).executeUpdate(saveQuery);
        }

    }

    private class ComboListener implements ActionListener {

        private CompanyInformationObj obj;
        private WeeklyComboModel weeklyModel;
        private GroupComboModel groupModel;

        public ComboListener(WeeklyComboModel model, CompanyInformationObj compObj) {
            this.obj = compObj;
            this.weeklyModel = model;
        }
        
        public ComboListener(GroupComboModel model, CompanyInformationObj compObj) {
            this.obj = compObj;
            this.groupModel = model;
        }

        public void actionPerformed(ActionEvent e) {
            JComboBox myCombo = (JComboBox)e.getSource();
            save_company_information_view_query saveQuery = new save_company_information_view_query();
            if (weeklyModel != null) {
                saveQuery.update(obj.getCompany_view_options_id(), obj.getCompany_id(), obj.getCompany_view_id(), weeklyModel.getValueAt(myCombo.getSelectedIndex()) + "");
            } else {
                saveQuery.update(obj.getCompany_view_options_id(), obj.getCompany_id(), obj.getCompany_view_id(), groupModel.getValueAt(myCombo.getSelectedIndex()) + "");
            }
            (new Connection()).executeUpdate(saveQuery);
        }
    }

    private class CheckListener implements ActionListener {

        private CompanyInformationObj obj;
        private JToggleButton view;
        private JToggleButton hide;

        public CheckListener(JToggleButton view, JToggleButton hide, CompanyInformationObj compObj) {
            this.obj = compObj;
            this.view = view;
            this.hide = hide;
        }

        public void actionPerformed(ActionEvent e) {
            save_company_information_view_query saveQuery = new save_company_information_view_query();
            saveQuery.update(obj.getCompany_view_options_id(), obj.getCompany_id(), obj.getCompany_view_id(), view.isSelected());
            (new Connection()).executeUpdate(saveQuery);
        }
    }

    private class GroupComboModel implements ComboBoxModel {

        private ArrayList<Group> groups;
        int selectedItem = 0;
        
        public GroupComboModel(ArrayList<Group> groups) {
            this.groups = groups;
        }
        
        public void setSelectedItem(Object anItem) {
            if (anItem instanceof Group) {
                Group selGroup = (Group)anItem;
                for (int g = 0; g < groups.size(); g++) {
                    if (groups.get(g).getGroupId() == selGroup.getGroupId()) {
                        selectedItem = g;
                    }
                }
            }
        }

        public void setSelectedValue(Object anItem) {
            for (int i = 0; i < groups.size(); i++) {
                if (anItem.toString().equals(groups.get(i).getGroupId() + "")) {
                    this.selectedItem = i;
                }
            }
        }
        
        public int getValueAt(int index) {
            return this.groups.get(index).getGroupId();
        }
        
        public Object getSelectedItem() {
            return groups.get(selectedItem);
        }

        public int getSize() {
            return groups.size();
        }

        public Object getElementAt(int index) {
            return groups.get(index);
        }

        public void addListDataListener(ListDataListener l) {
            
        }

        public void removeListDataListener(ListDataListener l) {
            
        }
        
    }
    
    private class WeeklyComboModel implements ComboBoxModel {

        private ArrayList<Integer> weekdayKeys = new ArrayList<Integer>();
        private ArrayList<String> weekdayValues = new ArrayList<String>();
        int selectedItem = 0;

        private WeeklyComboModel() {
            weekdayKeys.add(Calendar.MONDAY);
            weekdayValues.add("Monday");

            weekdayKeys.add(Calendar.TUESDAY);
            weekdayValues.add("Tuesday");

            weekdayKeys.add(Calendar.WEDNESDAY);
            weekdayValues.add("Wednesday");

            weekdayKeys.add(Calendar.THURSDAY);
            weekdayValues.add("Thursday");

            weekdayKeys.add(Calendar.FRIDAY);
            weekdayValues.add("Friday");

            weekdayKeys.add(Calendar.SATURDAY);
            weekdayValues.add("Saturday");

            weekdayKeys.add(Calendar.SUNDAY);
            weekdayValues.add("Sunday");
        }

        public void setSelectedValue(Object anItem) {
            for (int i = 0; i < weekdayKeys.size(); i++) {
                if (anItem.toString().equals(weekdayKeys.get(i).toString())) {
                    this.selectedItem = i;
                }
            }
        }

        public void setSelectedItem(Object anItem) {
            for (int i = 0; i < weekdayValues.size(); i++) {
                if (anItem.toString().equals(weekdayValues.get(i))) {
                    this.selectedItem = i;
                }
            }
        }

        public int getSelectedValue() {
            return this.weekdayKeys.get(selectedItem);
        }

        public Object getSelectedItem() {
            return weekdayValues.get(selectedItem);
        }

        public int getSize() {
            return weekdayValues.size();
        }

        public int getValueAt(int index) {
            return this.weekdayKeys.get(index);
        }

        public Object getElementAt(int index) {
            return weekdayValues.get(index);
        }

        public void addListDataListener(ListDataListener l) {
            
        }

        public void removeListDataListener(ListDataListener l) {
            
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPane;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
