/*
 * AvailableToTrain.java
 *
 * Created on May 11, 2005, 7:45 AM
 */

package rmischedule.schedule.components.training;
import rmischedule.schedule.components.*;
import rmischedule.main.*;
import rmischedule.schedule.*;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
/**
 *
 * @author  ira
 */
public class AvailableToTrain extends javax.swing.JPanel {
    
    private ArrayList employeesWorkingToday;
    private ArrayList otherEmployees;
    private Schedule_View_Panel par;
    private int selected;
    
    /** Creates new form AvailableToTrain */
    public AvailableToTrain(SClient currentClient, SEmployee empToIgnore, int week, int day, Schedule_View_Panel mw) {
        initComponents();
        par = mw;
        updateValues(currentClient, empToIgnore, week, day, mw);
        EmployeeComboBox.setModel(new myEmployeeComboBoxModel());
    }
    
    public void updateInfo(int trainId) {
        for (int i = 0; i < employeesWorkingToday.size(); i++) {
            if (((SEmployee)employeesWorkingToday.get(i)).getId() == trainId) {
                EmployeeComboBox.setSelectedIndex(i);
                return;
            }
        }
        for (int x = employeesWorkingToday.size(); x < employeesWorkingToday.size() + otherEmployees.size(); x++) {
            if (((SEmployee)otherEmployees.get(x - employeesWorkingToday.size())).getId() == trainId) {
                EmployeeComboBox.setSelectedIndex(x);
            }
        }
    }
    
    public String getTrainerName() {
        try {
            return (getItem(selected)).getName();
        } catch (Exception e) {
            return "";
        }
    }
    
    public String getTrainerId() {
        try {
            return (getItem(selected)).getId() + "";
        } catch (Exception e) {
            return "";
        }
    }
    
    public SEmployee getItem(int pos) {
        if (pos < employeesWorkingToday.size()) {
            return ((SEmployee)employeesWorkingToday.get(pos));
        } else if (selected == employeesWorkingToday.size()) {
            return new SEmployee(par);
        } else {
            return ((SEmployee)otherEmployees.get(pos - employeesWorkingToday.size()));
        }
    }
    
    public void updateValues(SClient currentClient, SEmployee empToIgnore, int week, int day, Schedule_View_Panel mw) {
        employeesWorkingToday = new ArrayList(10);
        Vector<SEmployee> listEmps = mw.getEmployeeList();
        otherEmployees = new ArrayList(listEmps.size());
        SSchedule currSchedule;
        SWeek currWeek;
        SRow currRow;
        SortedSet myMap = par.mySchedules.getClientSchedules(currentClient);
        Iterator<SSchedule> myIterator = myMap.iterator();
        for (int i = 0; i < myMap.size(); i++) {
            currSchedule = myIterator.next();
            currWeek = currSchedule.getWeek(week);
            for (int x = 0; x < currWeek.getRowSize(); x++) {
                currRow = currWeek.getRow(x);
                if (currRow.getShift(day).myShift != null &&
                        !employeesWorkingToday.contains(currSchedule.getEmployee()) &&
                        !empToIgnore.equals(currSchedule.getEmployee())) {
                    employeesWorkingToday.add(currSchedule.getEmployee());
                }
            }
        }
        for (int i = 0; i < listEmps.size(); i++) {
            if (!employeesWorkingToday.contains(listEmps.get(i))) {
                otherEmployees.add(listEmps.get(i));
            }
        }
    }
    
    private class myEmployeeComboBoxModel implements ComboBoxModel {
        
        public myEmployeeComboBoxModel() {
            selected = 0;
        }
        
        public Object getSelectedItem() {
            if (selected != employeesWorkingToday.size()) {
                return getItem(selected).getName();
            } else {
                return "---- Employees Below Are Not Working Today ----";
            }
        }
        
        public void setSelectedItem(Object anItem) {
            for (int i = 0; i < employeesWorkingToday.size() + otherEmployees.size(); i++) {
                if (i < employeesWorkingToday.size()) {
                    if (((SEmployee)employeesWorkingToday.get(i)).getName().equals(anItem)) {
                        selected = i;
                        return;
                    }
                } else if (i > employeesWorkingToday.size()) {
                    if (((SEmployee)otherEmployees.get(i - employeesWorkingToday.size())).getName().equals(anItem)) {
                        selected = i;
                        return;
                    }
                }
            }
        }
        
        public void addListDataListener(ListDataListener l) {
            
        }
        
        public void removeListDataListener(ListDataListener l) {
            
        }
        
        public int getSize() {
            return employeesWorkingToday.size() + otherEmployees.size();
        }
        
        public Object getElementAt(int index) {
            if (index != employeesWorkingToday.size()) {
                return getItem(index).getName();
            } else {
                return "---- Employees Below Are Not Working Today ----";
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        EmployeeComboBox = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setOpaque(false);
        jLabel1.setText("jLabel1");
        add(jLabel1);

        EmployeeComboBox.setMaximumSize(new java.awt.Dimension(32767, 22));
        add(EmployeeComboBox);

        jPanel1.setOpaque(false);
        add(jPanel1);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox EmployeeComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
