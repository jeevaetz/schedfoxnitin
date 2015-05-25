/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BillingModulesSubScreen.java
 *
 * Created on Jul 18, 2010, 12:18:25 PM
 */

package rmischedule.new_user;

import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.JCheckBox;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.new_user.get_billing_modules_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.get_number_of_free_days_query;

/**
 *
 * @author user
 */
public class BillingModulesSubScreen extends javax.swing.JPanel implements NewUserSubScreen {

    private ArrayList<BillingInformation> billingInfoArray;

    /** Creates new form BillingModulesSubScreen */
    public BillingModulesSubScreen() {
        initComponents();
        Connection myConn = new Connection();
        billingInfoArray = new ArrayList<BillingInformation>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        get_number_of_free_days_query numDaysFree = new get_number_of_free_days_query();
        Record_Set numFree = myConn.executeQuery(numDaysFree);
        int numberOfFreeDays = 0;
        Date today = null;
        Date dateOfFirstBilling = null;
        for (int r = 0; r < numFree.length(); r++) {
            numberOfFreeDays = numFree.getInt("company_billing_value");
            try {
                today = dateFormat.parse(numFree.getString("curtime"));
            } catch (Exception e) {
                today = new Date();
            }
            try {
                dateOfFirstBilling = dateFormat.parse(numFree.getString("outtime"));
            } catch (Exception e) {
                e.printStackTrace();
                dateOfFirstBilling = new Date();
            }
        }

        get_billing_modules_query billingModule = new get_billing_modules_query();
        
        Record_Set rst = myConn.executeQuery(billingModule);
        for (int r = 0; r < rst.length(); r++) {
            BillingInformation billingInfo = new BillingInformation();
            billingInfo.setBilling_module_id(rst.getInt("company_billing_module_id"));
            billingInfo.setModule_name(rst.getString("module_name"));
            billingInfo.setModule_cost(rst.getString("module_cost"));
            billingInfo.setMust_pay(rst.getBoolean("must_pay"));
            billingInfoArray.add(billingInfo);
            rst.moveNext();
        }

        for (int b = 0; b < billingInfoArray.size(); b++) {
            StringBuffer txt = new StringBuffer();
            txt.append(billingInfoArray.get(b).getModule_name());
            txt.append(" (" + billingInfoArray.get(b).getModule_cost() + " / week)");
            JCheckBox check = new JCheckBox(txt.toString());
            billingInfoArray.get(b).setCheckbox(check);
            if (billingInfoArray.get(b).isMust_pay()) {
                check.setSelected(true);
                check.setEnabled(false);
            }
            checkPanel.add(check);
        }

        this.displayInformation(numberOfFreeDays, today, dateOfFirstBilling);

    }

    private void displayInformation(int numberDaysFree, Date today, Date dateOfFirstBilling) {
        StringBuffer info = new StringBuffer();
        info.append("Schedfox is available for free for ").append(numberDaysFree).append(" days ");
        info.append("and you will NOT be charged until you enter your billing information. ");

        informationDate.setText(info.toString());
    }

    private class BillingInformation {
        private int billing_module_id;
        private String module_name;
        private String module_cost;
        private boolean must_pay;
        private JCheckBox checkbox;

        public BillingInformation() {
            billing_module_id = 0;
            module_name = "";
            module_cost = "";
            must_pay = false;
        }

        /**
         * @return the billing_module_id
         */
        public int getBilling_module_id() {
            return billing_module_id;
        }

        /**
         * @param billing_module_id the billing_module_id to set
         */
        public void setBilling_module_id(int billing_module_id) {
            this.billing_module_id = billing_module_id;
        }

        /**
         * @return the module_name
         */
        public String getModule_name() {
            return module_name;
        }

        /**
         * @param module_name the module_name to set
         */
        public void setModule_name(String module_name) {
            this.module_name = module_name;
        }

        /**
         * @return the module_cost
         */
        public String getModule_cost() {
            return module_cost;
        }

        /**
         * @param module_cost the module_cost to set
         */
        public void setModule_cost(String module_cost) {
            this.module_cost = module_cost;
        }

        /**
         * @return the checkbox
         */
        public JCheckBox getCheckbox() {
            return checkbox;
        }

        /**
         * @param checkbox the checkbox to set
         */
        public void setCheckbox(JCheckBox checkbox) {
            this.checkbox = checkbox;
        }

        /**
         * @return the must_pay
         */
        public boolean isMust_pay() {
            return must_pay;
        }

        /**
         * @param must_pay the must_pay to set
         */
        public void setMust_pay(boolean must_pay) {
            this.must_pay = must_pay;
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        informationDate = new javax.swing.JTextArea();
        checkPanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Additional Billing Options"));
        setLayout(new java.awt.GridLayout());

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        informationDate.setBackground(new java.awt.Color(236, 233, 216));
        informationDate.setColumns(20);
        informationDate.setEditable(false);
        informationDate.setLineWrap(true);
        informationDate.setRows(5);
        informationDate.setWrapStyleWord(true);
        informationDate.setBorder(null);
        informationDate.setOpaque(false);
        jPanel2.add(informationDate);

        jPanel1.add(jPanel2);

        checkPanel.setLayout(new javax.swing.BoxLayout(checkPanel, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(checkPanel);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    public Hashtable<String, Object> getValues() {
        Hashtable<String, Object> retVal = new Hashtable<String, Object>();
        ArrayList<Integer> selectedValues = new ArrayList<Integer>();
        for (int b = 0; b < billingInfoArray.size(); b++) {
            if (billingInfoArray.get(b).getCheckbox().isSelected()) {
                selectedValues.add(billingInfoArray.get(b).getBilling_module_id());
            }
        }
        retVal.put("selectedBillingMod", selectedValues);
        return retVal;
    }

    public ArrayList<String> getValidationString() {
        return new ArrayList<String>();
    }

    public Container getContainer() {
        return this;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel checkPanel;
    private javax.swing.JTextArea informationDate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}
