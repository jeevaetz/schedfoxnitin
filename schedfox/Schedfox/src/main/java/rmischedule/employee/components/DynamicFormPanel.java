/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DynamicFormPanel.java
 *
 * Created on Sep 14, 2011, 3:22:01 PM
 */

package rmischedule.employee.components;

import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JComponent;
import rmischedule.components.DynamicFieldPanel;
import rmischedule.components.graphicalcomponents.GenericTabbedEditForm;
import rmischedule.main.Main_Window;
import rmischedule.xadmin.model.DynamicFieldValue;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.employee.save_employee_dynamic_data_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_dynamic_field_values_for_key_query;

/**
 *
 * @author user
 */
public class DynamicFormPanel extends javax.swing.JPanel {

    private GenericTabbedEditForm myParent;
    private DynamicPanelParent panelParent;
    private int type;
    private Vector<DynamicFieldPanel> myFieldPanels;
    private boolean isClient;
    private boolean isEmp;

    /** Creates new form DynamicFormPanel */
    public DynamicFormPanel(GenericTabbedEditForm myParent, DynamicPanelParent panelParent,
            int type, boolean isClient, boolean isEmp) {
        initComponents();
        this.myParent = myParent;
        this.panelParent = panelParent;
        this.type = type;
        this.isClient = isClient;
        this.isEmp = isEmp;
        this.myFieldPanels = new Vector<DynamicFieldPanel>();
    }
    
    public void clear() {
        this.compPanel.removeAll();
        this.myFieldPanels.clear();
    }

    public void loadData() {
        get_dynamic_field_values_for_key_query myQuery = new get_dynamic_field_values_for_key_query();
        try {
            myQuery.update(Integer.parseInt(myParent.getMyIdForSave()), this.type, isClient, isEmp);
        } catch (Exception e) {
        }

        Record_Set rs = myParent.getConnection().executeQuery(myQuery);

        for (int r = 0; r < rs.length(); r++) {
            int key = 0;
            try {
                key = Integer.parseInt(myParent.getMyIdForSave());
            } catch (Exception e) {
            }
            DynamicFieldValue fieldValue = new DynamicFieldValue(rs, key);
            this.initUnsetDateToDayOfHire(fieldValue);
            DynamicFieldPanel myPanel = new DynamicFieldPanel(fieldValue, myParent);
            if (fieldValue.getFieldDefObj().getFieldTypeId() == 8) {
                if (panelParent.getPanelForFileDynamicForms() != null) {
                    panelParent.getPanelForFileDynamicForms().add(myPanel);
                }
            } else {
                this.compPanel.add(myPanel);
            }
            this.myFieldPanels.add(myPanel);
            rs.moveNext();
        }
        this.revalidate();
    }

    public void save() {
        save_employee_dynamic_data_query saveQuery = new save_employee_dynamic_data_query();
        for (int r = 0; r < this.myFieldPanels.size(); r++) {
            DynamicFieldValue value = myFieldPanels.get(r).getDynamicFieldValue();
            try {
                saveQuery.addDynamicData(value.getDynamic_field_value_id(), value.getDynamic_field_value(),
                        value.getDynamic_field_def_id(), value.getKey_value(),
                        Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            } catch (Exception e) {
                saveQuery.addDynamicData(value.getDynamic_field_value_id(), value.getDynamic_field_value(),
                        value.getDynamic_field_def_id(), value.getKey_value(), 0);
            }
        }
        myParent.getConnection().prepQuery(saveQuery);
        myParent.getConnection().executeUpdate(saveQuery);
    }

    private void initUnsetDateToDayOfHire(DynamicFieldValue fieldValue) {
        try {
            //If we have no value, and it's a date, assume date is date of hire.
            if (fieldValue.getFieldDefObj().getFieldTypeId() == 3
                    && fieldValue.getDynamic_field_value().equals("0")) {
                if (myParent.getSelectedObject() instanceof Employee) {
                    Employee semp = (Employee) myParent.getSelectedObject();
                    SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                    fieldValue.setDynamic_field_value(myFormat.format(semp.getEmployeeHireDate()));
                } else if (myParent.getSelectedObject() instanceof Client) {
                    Client semp = (Client) myParent.getSelectedObject();
                    SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                    fieldValue.setDynamic_field_value(myFormat.format(semp.getClientStartDate()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        compPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        compPanel.setLayout(new javax.swing.BoxLayout(compPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(compPanel);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel compPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
