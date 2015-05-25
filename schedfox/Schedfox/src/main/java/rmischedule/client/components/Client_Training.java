/*
 * Client_Training.java
 *
 * Created on June 1, 2005, 7:38 AM
 */
package rmischedule.client.components;

import rmischedule.components.List_View;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.main.*;
import rmischedule.security.*;

import rmischeduleserver.mysqlconnectivity.queries.schedule_data.training.get_training_by_client_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.training.save_trained_or_not_query;
import schedfoxlib.model.util.Record_Set;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.Client;

/**
 *
 * @author  ira
 */
public class Client_Training extends GenericEditSubForm {

    private List_View trainingListView;
    private get_training_by_client_query myQuery;

    /** Creates new form Client_Training */
    public Client_Training() {
        initComponents();
        trainingListView = new List_View();
        trainingListView.addColumn("Id", List_View.STRING, false, false);
        trainingListView.addColumn("Last Name", List_View.STRING, false, true);
        trainingListView.addColumn("First Name", List_View.STRING, false, true);
        trainingListView.addColumn("Hours Trained", List_View.NUMBER, false, true);
        trainingListView.addColumn("Hours Required", List_View.NUMBER, false, true);
        trainingListView.addColumn("Trained By Hours", List_View.BOOLEAN, false, true);
        trainingListView.addColumn("Override Training", List_View.BOOLEAN, true, true);
        trainingListView.buildTable();
        trainingListView.maximizeTable();
        trainingListView.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                saveValue();
            }
        });
        MainPanel.add(trainingListView.myScrollPane);
    }

    private void saveValue() {
        save_trained_or_not_query myUpdateQuery = new save_trained_or_not_query();
        myUpdateQuery.update(myparent.getMyIdForSave(), ((String) trainingListView.getSelectedCell(0)), ((Boolean) trainingListView.getSelectedCell(6)));
        try {
            myparent.getConnection().executeUpdate(myUpdateQuery);
        } catch (Exception e) {
        }
    }

    public void doOnClear() {
    }

    public javax.swing.JPanel getMyForm() {
        return this;
    }

    public java.lang.String getMyTabTitle() {
        return "Training";
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {
        myQuery = new get_training_by_client_query();
        myQuery.update("", myparent.getMyIdForSave());
        return myQuery;
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        try {
            TrainingSpinner.commitEdit();
        } catch (Exception e) {
        }
        Double hoursTrained = 0.0;
        try {
            hoursTrained = ((Double) TrainingSpinner.getValue());
        } catch (Exception e) {
        }
        if (hoursTrained == null) {
            hoursTrained = 0.0;
        }
        try {
            Client currentClient = (Client) myparent.getSelectedObject();
            currentClient.setClientTrainingTime(hoursTrained);
            currentClient.setClientBillForTrainingBoolean(TrainingBillableCheckBox.isSelected());

            ClientController clientController = ClientController.getInstance(myparent.getConnection().myCompany);
            try {
                clientController.saveClient(currentClient);
                myparent.setCurrentSelectedObjectDirectly(currentClient);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
        }
        return myQuery;
    }

    public void loadData(Record_Set rs) {
        int trainedTime = 0;
        int totalTrained = 0;
        trainingListView.clearRows();

        try {
            Client currentClient = (Client) myparent.getSelectedObject();
            TrainingSpinner.setValue(currentClient.getClientTrainingTime());
            if (currentClient.getClientBillForTraining() == null) {
                TrainingBillableCheckBox.setSelected(false);
            } else {
                TrainingBillableCheckBox.setSelected(currentClient.getClientBillForTraining());
            }
        } catch (Exception e) {
            System.out.println("Database does not have client.client_training_time field or client.client_bill_for_training...damn");
        }
        for (int i = 0; i < rs.length(); i++) {
            Object[] myData = new Object[7];
            myData[0] = rs.getString("id");
            myData[1] = rs.getString("lname");
            myData[2] = rs.getString("fname");
            try {
                trainedTime = rs.getInt("tot");
            } catch (Exception e) {
                trainedTime = 0;
            }
            totalTrained = rs.getInt("ttime");
            myData[3] = new Integer(trainedTime);
            myData[4] = new Integer(totalTrained);
            if (totalTrained - trainedTime <= 0) {
                myData[5] = new Boolean(true);
            } else {
                myData[5] = new Boolean(false);
            }
            myData[6] = false;
            if (rs.getString("override").equals("t")) {
                myData[6] = true;
            }
            trainingListView.addRow(myData);
            rs.moveNext();
        }
        trainingListView.fireTableDataChanged();

    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    public boolean userHasAccess() {
        return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.CLIENT_INFORMATION);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TrainingPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        TrainingSpinner = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        TrainingBillableCheckBox = new javax.swing.JCheckBox();
        MainPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        TrainingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Training Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(51, 51, 51))); // NOI18N
        TrainingPanel.setMaximumSize(new java.awt.Dimension(90000000, 70));
        TrainingPanel.setMinimumSize(new java.awt.Dimension(268, 70));
        TrainingPanel.setPreferredSize(new java.awt.Dimension(200, 70));
        TrainingPanel.setLayout(new javax.swing.BoxLayout(TrainingPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 43));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10));
        jLabel1.setText(" Hours Required For Training");
        jPanel3.add(jLabel1);

        jPanel5.setMaximumSize(new java.awt.Dimension(20, 32767));
        jPanel3.add(jPanel5);

        TrainingSpinner.setMaximumSize(new java.awt.Dimension(60, 22));
        TrainingSpinner.setMinimumSize(new java.awt.Dimension(6, 22));
        TrainingSpinner.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel3.add(TrainingSpinner);

        jPanel6.setMaximumSize(new java.awt.Dimension(20, 32767));
        jPanel3.add(jPanel6);

        TrainingBillableCheckBox.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        TrainingBillableCheckBox.setText("Bill Location For Training");
        jPanel3.add(TrainingBillableCheckBox);

        TrainingPanel.add(jPanel3);

        add(TrainingPanel);

        MainPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(MainPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    private javax.swing.JCheckBox TrainingBillableCheckBox;
    private javax.swing.JPanel TrainingPanel;
    private javax.swing.JFormattedTextField TrainingSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
