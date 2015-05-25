/*
 * xManagementDetail.java
 *
 * Created on September 7, 2005, 11:28 AM
 */
package rmischedule.xadmin;

import java.awt.Image;
import java.awt.Dimension;
import java.io.File;
import java.math.BigDecimal;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.components.*;
import rmischedule.main.*;

import rmischeduleserver.mysqlconnectivity.queries.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import rmischedule.employee.login.EmployeeLogin;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.schedfox.invoicing.*;

import rmischeduleserver.control.ManagementClientController;
import schedfoxlib.model.Company;
import schedfoxlib.model.ManagementClient;
import schedfoxlib.model.User;
/**
 *
 * @author  Ira Juneau
 */
public class xManagementDetail extends GenericEditSubForm implements PictureParentInterface {

    private PicturePanel clientHeader;
    private JFileChooser fchooser;
    DateFormat df = new SimpleDateFormat("MM/01/yyyy");
    private SchedFoxInvoices schedInv;
    private int CLIENT_INVOICE=1;
    private int ALL_INVOICES=2;

    private ManagementClient mangmentClient;
    private JCalendarComboBox calStartDate;

    /** Creates new form xManagementDetail */
    public xManagementDetail() {
        initComponents();
        generateListViews();

        calStartDate = new JCalendarComboBox();
        startPanel.add(calStartDate);

        this.fchooser = new JFileChooser();
        this.fchooser.setDialogTitle("Select an image for this company header");
        this.fchooser.setAcceptAllFileFilterUsed(false);
        this.fchooser.setMultiSelectionEnabled(false);

        //Add the preview pane.
        this.fchooser.setAccessory(new ImagePreview(this.fchooser));

        this.fchooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return "Image (JPEG, JPG)";
            }
        });
    }

    public void generateListViews() {
        clientHeader = new PicturePanel(this);
        imageHolder.add(clientHeader);
    }

    public javax.swing.JPanel getMyForm() {
        return this;
    }

    public java.lang.String getMyTabTitle() {
        return "General Information";
    }

    public void loadData(schedfoxlib.model.util.Record_Set rs) {
        mangmentClient = new ManagementClient(rs);
        cname.setText(mangmentClient.getManagement_client_name());
        caddress.setText(mangmentClient.getManagement_client_address());
        caddress2.setText(mangmentClient.getManagement_client_address2());
        ccity.setText(mangmentClient.getManagement_client_city());
        cstate.setText(mangmentClient.getManagement_client_state());
        czip.setText(mangmentClient.getManagement_client_zip());
        cphone.setText(mangmentClient.getManagement_client_phone());
        cemail.setText(mangmentClient.getManagement_client_email());
        activeCheckBox.setSelected(!mangmentClient.isManagement_is_deleted());
        billingEmail1.setText(mangmentClient.getManagement_billing_email1());
        billingEmail2.setText(mangmentClient.getManagement_billing_email2());
        try {
            Integer timeBill =
                    Integer.parseInt(mangmentClient.getBill_interval().substring(0, mangmentClient.getBill_interval().indexOf(" ")));
            String endType = mangmentClient.getBill_interval().substring(mangmentClient.getBill_interval().indexOf(" ") + 1);
            intervalCmb.setSelectedItem(convertRange(endType));
            intervalBillSpinner.setValue(timeBill);
        } catch (Exception e) {}
        try {
            Calendar myCal = Calendar.getInstance();
            myCal.setTime(mangmentClient.getBill_start_date());
            this.calStartDate.setCalendar(myCal);
        } catch (Exception e) {}
        try {
            billAmountTxt.setText(mangmentClient.getAmount_to_bill().toString());
        } catch (Exception e) {}
        try {
            perEmployeeTxt.setText(mangmentClient.getAmount_per_employee().toString());
        } catch (Exception e) {}
        getInvoiceData(mangmentClient.getManagement_id());
        getManagementCompanyStats();
    }

    public static String convertRange(String rangeIn) {
        String retVal = "";
        if (rangeIn.equalsIgnoreCase("mons") || rangeIn.equalsIgnoreCase("mon")) {
            retVal = "Month";
        } else if (rangeIn.equalsIgnoreCase("days") || rangeIn.equalsIgnoreCase("day")) {
            retVal = "Day";
        } else if (rangeIn.equalsIgnoreCase("years") || rangeIn.equalsIgnoreCase("year")) {
            retVal = "Year";
        } else {
            retVal = "Week";
        }
        return retVal;
    }
    
    /**
     * Gets statistics about this client...
     */
    private void getManagementCompanyStats() {
        int totalEmployees = 0;
        int totalClients = 0;
        String maxSoFar = "";
        String userModified = "";

        ManagementClientController controller = ManagementClientController.getInstance("");
        try {
            ArrayList<Company> companies = controller.getCompaniesForManagementClient(mangmentClient.getManagement_id());
            totalEmployees = controller.getActiveEmployeeCount(companies);
            totalClients = controller.getActiveClientCount(companies);
            HashMap<Date, User> users = controller.getDateAndUserOfLastModified(companies);
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
            Iterator<Date> dates = users.keySet().iterator();
            while (dates.hasNext()) {
                Date myDate = dates.next();
                maxSoFar = myFormat.format(myDate);
                userModified = users.get(myDate).getFullName();
            }
            //Lame way of clearing out template results
            if (maxSoFar.equals("08/17/10 08:51:48")) {
                totalEmployees = 0;
                totalClients = 0;
                maxSoFar = "";
                userModified = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveEmpsLabel.setText(totalEmployees + "");
            ActiveClientsLabel.setText(totalClients + "");
            LastModifiedLabel.setText(maxSoFar);
            UserModifiedLabel.setText(userModified);
        }
        
    }
    
    private void getInvoiceData(int myId){

        
    }
    
    public boolean needsMoreRecordSets() {
        return false;
    }

    public boolean userHasAccess() {
        return true;
    }

    public void doOnClear() {
        mangmentClient = null;
        this.intervalBillSpinner.setValue(0);
        this.activeCheckBox.setSelected(true);
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        if (mangmentClient == null) {
            mangmentClient = new ManagementClient();
        }
        mangmentClient.setManagement_client_name(cname.getText().trim());
        mangmentClient.setManagement_client_address(caddress.getText().trim());
        mangmentClient.setManagement_client_address2(caddress2.getText().trim());
        mangmentClient.setManagement_client_city(ccity.getText().trim());
        mangmentClient.setManagement_client_state(cstate.getText().trim());
        mangmentClient.setManagement_client_zip(czip.getText().trim());
        mangmentClient.setManagement_client_phone(cphone.getText().trim());
        mangmentClient.setManagement_client_email(cemail.getText().trim());
        mangmentClient.setManagement_is_deleted(!activeCheckBox.isSelected());
        mangmentClient.setManagement_billing_email1(billingEmail1.getText());
        mangmentClient.setManagement_billing_email2(billingEmail2.getText());
        try {
            Date myDate = this.calStartDate.getCalendar().getTime();
            mangmentClient.setBill_start_date(myDate);
        } catch (Exception e) {}
        try {
            double amount = Double.parseDouble(billAmountTxt.getText().trim());
            mangmentClient.setAmount_to_bill(new BigDecimal(amount));
        } catch (Exception e) {
            mangmentClient.setAmount_to_bill(new BigDecimal(0));
        }
        try {
            mangmentClient.setAmount_per_employee(new BigDecimal(Double.parseDouble(perEmployeeTxt.getText())));
        } catch (Exception e) {
            mangmentClient.setAmount_per_employee(new BigDecimal(0));
        }
        try {
            mangmentClient.setBill_interval(intervalBillSpinner.getValue() + " " + intervalCmb.getSelectedItem());
        } catch (Exception e) {

        }
        //mangmentClient.setManagement_client_name(cname.getText().trim());
        ManagementClientController controller = ManagementClientController.getInstance("");
        try {
            int numberAffected = controller.doesBillingAlreadyExist(mangmentClient.getBill_start_date(), mangmentClient.getManagement_id(), mangmentClient.getAmount_to_bill(), mangmentClient.getAmount_per_employee());
            if (numberAffected != 0) {
                int response = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "You appear to be changing invoice amounts for a client that already has been invoiced a different "
                        + "amount for these dates. Are you sure you want to do this? \r\nIf you click Yes all old invoice amounts will be replaced with the new values and period you "
                        + "have entered. This will clear out " + numberAffected + " old invoices!\r\nIf you click No this will only apply from here on out\r\nClicking cancel will cancel "
                        + "this operation in entirety.", "Invoicing exists!", JOptionPane.YES_NO_CANCEL_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    controller.removeExistingBilling(mangmentClient.getBill_start_date(), mangmentClient.getManagement_id());
                } else if (response == JOptionPane.CANCEL_OPTION) {
                    return new GenericQuery("SELECT NOW();");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            
            controller.saveManagementClient(mangmentClient);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Could not save data!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return new GenericQuery("SELECT NOW()");
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {
        get_available_management_co_query myQuery = new get_available_management_co_query();
        myQuery.update(myparent.getMyIdForSave());
        return myQuery;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        invoiceListFrame = new javax.swing.JInternalFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        invoiceListTabel = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cname = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        caddress = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        caddress2 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        ccity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cstate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        czip = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cphone = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cemail = new javax.swing.JTextField();
        activeCheckBox = new javax.swing.JCheckBox();
        jPanel23 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        billingEmail1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        billingEmail2 = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        billAmountTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        startPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        intervalBillSpinner = new javax.swing.JSpinner();
        intervalCmb = new javax.swing.JComboBox();
        jPanel25 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        perEmployeeTxt = new javax.swing.JTextField();
        startPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        imageHolder = new javax.swing.JPanel();
        tabbedSubPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        ActiveEmpsLabel = new javax.swing.JLabel();
        ActiveClientsLabel = new javax.swing.JLabel();
        LastModifiedLabel = new javax.swing.JLabel();
        UserModifiedLabel = new javax.swing.JLabel();

        invoiceListFrame.setVisible(true);
        invoiceListFrame.getContentPane().setLayout(new javax.swing.BoxLayout(invoiceListFrame.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("jLabel1");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 456, Short.MAX_VALUE)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(0, 205, Short.MAX_VALUE)
                    .add(jLabel1)
                    .add(0, 206, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 212, Short.MAX_VALUE)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(0, 97, Short.MAX_VALUE)
                    .add(jLabel1)
                    .add(0, 98, Short.MAX_VALUE)))
        );

        invoiceListFrame.getContentPane().add(jPanel2);

        invoiceListTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(invoiceListTabel);

        invoiceListFrame.getContentPane().add(jScrollPane2);

        setMinimumSize(new java.awt.Dimension(640, 458));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(640, 400));
        setRequestFocusEnabled(false);
        setVerifyInputWhenFocusTarget(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 462));

        jPanel1.setMinimumSize(new java.awt.Dimension(600, 350));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 400));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel17.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel17.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Company Name");
        jLabel4.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel17.add(jLabel4);

        cname.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel17.add(cname);

        jPanel1.add(jPanel17);

        jPanel18.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel18.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel18.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Company Address");
        jLabel5.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel18.add(jLabel5);

        caddress.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel18.add(caddress);

        jPanel1.add(jPanel18);

        jPanel19.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel19.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel19.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Address Line 2");
        jLabel6.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel6.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel6.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel19.add(jLabel6);

        caddress2.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel19.add(caddress2);

        jPanel1.add(jPanel19);

        jPanel20.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel20.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel20.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setText("City");
        jLabel7.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel7.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel7.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel20.add(jLabel7);

        ccity.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel20.add(ccity);

        jLabel10.setText("State");
        jPanel20.add(jLabel10);

        cstate.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel20.add(cstate);

        jLabel11.setText("Zip");
        jPanel20.add(jLabel11);

        czip.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel20.add(czip);

        jPanel1.add(jPanel20);

        jPanel21.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel21.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel21.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setText("Phone Number");
        jLabel8.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel8.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel8.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel21.add(jLabel8);

        cphone.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel21.add(cphone);

        jPanel1.add(jPanel21);

        jPanel22.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel22.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel22.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel22.setLayout(new javax.swing.BoxLayout(jPanel22, javax.swing.BoxLayout.LINE_AXIS));

        jLabel9.setText("Company E-Mail");
        jLabel9.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel9.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel9.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel22.add(jLabel9);

        cemail.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel22.add(cemail);

        activeCheckBox.setSelected(true);
        activeCheckBox.setText("Active");
        activeCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        activeCheckBox.setFocusPainted(false);
        activeCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        activeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeCheckBoxActionPerformed(evt);
            }
        });
        jPanel22.add(activeCheckBox);

        jPanel1.add(jPanel22);

        jPanel23.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel23.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel23.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel23.setLayout(new javax.swing.BoxLayout(jPanel23, javax.swing.BoxLayout.LINE_AXIS));

        jLabel15.setText("Billing  E-Mail 1");
        jLabel15.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel15.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel15.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel23.add(jLabel15);

        billingEmail1.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel23.add(billingEmail1);

        jLabel16.setText("Billing  E-Mail 2");
        jLabel16.setMaximumSize(new java.awt.Dimension(130, 14));
        jLabel16.setMinimumSize(new java.awt.Dimension(130, 14));
        jLabel16.setPreferredSize(new java.awt.Dimension(130, 14));
        jPanel23.add(jLabel16);

        billingEmail2.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel23.add(billingEmail2);

        jPanel1.add(jPanel23);

        jPanel24.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel24.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel24.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel24.setLayout(new javax.swing.BoxLayout(jPanel24, javax.swing.BoxLayout.LINE_AXIS));

        jLabel12.setText("Weekly Base Bill Amount");
        jLabel12.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel12.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel12.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel24.add(jLabel12);

        billAmountTxt.setMaximumSize(new java.awt.Dimension(200, 25));
        billAmountTxt.setMinimumSize(new java.awt.Dimension(100, 28));
        billAmountTxt.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel24.add(billAmountTxt);

        jLabel2.setText("Starts On");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jLabel2.setMaximumSize(new java.awt.Dimension(70, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(70, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel24.add(jLabel2);

        startPanel.setMaximumSize(new java.awt.Dimension(150, 32767));
        startPanel.setMinimumSize(new java.awt.Dimension(150, 30));
        startPanel.setPreferredSize(new java.awt.Dimension(150, 26));
        startPanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel24.add(startPanel);

        jLabel13.setText("Bill every");
        jLabel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jLabel13.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel13.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel24.add(jLabel13);
        jPanel24.add(intervalBillSpinner);

        intervalCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Day", "Week", "Month", "Year" }));
        jPanel24.add(intervalCmb);

        jPanel1.add(jPanel24);

        jPanel25.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel25.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel25.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel25.setLayout(new javax.swing.BoxLayout(jPanel25, javax.swing.BoxLayout.LINE_AXIS));

        jLabel14.setText("Charge for > 25 Emps");
        jLabel14.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel25.add(jLabel14);

        perEmployeeTxt.setMaximumSize(new java.awt.Dimension(100, 25));
        perEmployeeTxt.setMinimumSize(new java.awt.Dimension(100, 28));
        perEmployeeTxt.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel25.add(perEmployeeTxt);

        startPanel1.setMaximumSize(new java.awt.Dimension(150, 32767));
        startPanel1.setMinimumSize(new java.awt.Dimension(150, 30));
        startPanel1.setPreferredSize(new java.awt.Dimension(150, 26));
        startPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jLabel3.setText("(Per Employee)");
        startPanel1.add(jLabel3);

        jPanel25.add(startPanel1);

        jPanel1.add(jPanel25);

        jPanel7.setMinimumSize(new java.awt.Dimension(600, 100));
        jPanel7.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanel7.setLayout(new java.awt.GridLayout(1, 2));

        imageHolder.setBorder(javax.swing.BorderFactory.createTitledBorder("Client Header Image"));
        imageHolder.setLayout(new java.awt.GridLayout(1, 0));
        jPanel7.add(imageHolder);

        tabbedSubPanel.setPreferredSize(new java.awt.Dimension(412, 100));
        tabbedSubPanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Client Statistics"));
        jPanel10.setPreferredSize(new java.awt.Dimension(412, 100));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel18.setText("Number Of Active Employees");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel19.setText("Number Of Active Clients");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel20.setText("Date Of Last Shift Modified");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jLabel21.setText("User Who Modified Last Shift");

        ActiveEmpsLabel.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        ActiveEmpsLabel.setForeground(new java.awt.Color(255, 0, 102));

        ActiveClientsLabel.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        ActiveClientsLabel.setForeground(new java.awt.Color(255, 0, 102));

        LastModifiedLabel.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        LastModifiedLabel.setForeground(new java.awt.Color(255, 0, 102));

        UserModifiedLabel.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        UserModifiedLabel.setForeground(new java.awt.Color(255, 0, 102));

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 149, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel21, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel20, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel19, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(UserModifiedLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .add(LastModifiedLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .add(ActiveEmpsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .add(ActiveClientsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(ActiveEmpsLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19)
                    .add(ActiveClientsLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel20)
                    .add(LastModifiedLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel21)
                    .add(UserModifiedLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        tabbedSubPanel.add(jPanel10);

        jPanel7.add(tabbedSubPanel);

        jPanel1.add(jPanel7);

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void activeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeCheckBoxActionPerformed
        if (!this.activeCheckBox.isSelected()) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to deactivate this management company?", "Deactivate Management Company?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.myparent.saveData();
            } else {
                this.activeCheckBox.setSelected(true);
            }
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to activate this management company?", "Activate Management Company?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.myparent.saveData();
            } else {
                this.activeCheckBox.setSelected(false);
            }
        }
    }//GEN-LAST:event_activeCheckBoxActionPerformed

    private void createInvoices(int sw){
        String invDate=new String();

        if(schedInv==null)
            schedInv=new SchedFoxInvoices();
        if(sw==CLIENT_INVOICE){
            invDate=javax.swing.JOptionPane.showInternalInputDialog(this,"Enter Invoice Date");
            if(invDate==null || invDate.equals(""))
                return;
            schedInv.createCustInvoice(myparent.getMyIdForSave(),invDate);
        }
//        else if(sw==ALL_INVOICES)
//            schedInv.createInvoices(invDate);
//        else
//            return;

        Main_Window.parentOfApplication.desktop.add(schedInv);
        schedInv.setVisible(true);
        Dimension dim=new Dimension(Main_Window.parentOfApplication.getWidth()-100,Main_Window.parentOfApplication.getHeight()-100);
        schedInv.setSize(dim);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActiveClientsLabel;
    private javax.swing.JLabel ActiveEmpsLabel;
    private javax.swing.JLabel LastModifiedLabel;
    private javax.swing.JLabel UserModifiedLabel;
    private javax.swing.JCheckBox activeCheckBox;
    private javax.swing.JTextField billAmountTxt;
    private javax.swing.JTextField billingEmail1;
    private javax.swing.JTextField billingEmail2;
    private javax.swing.JTextField caddress;
    private javax.swing.JTextField caddress2;
    private javax.swing.JTextField ccity;
    private javax.swing.JTextField cemail;
    private javax.swing.JTextField cname;
    private javax.swing.JTextField cphone;
    private javax.swing.JTextField cstate;
    private javax.swing.JTextField czip;
    private javax.swing.JPanel imageHolder;
    private javax.swing.JSpinner intervalBillSpinner;
    private javax.swing.JComboBox intervalCmb;
    private javax.swing.JInternalFrame invoiceListFrame;
    private javax.swing.JTable invoiceListTabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField perEmployeeTxt;
    private javax.swing.JPanel startPanel;
    private javax.swing.JPanel startPanel1;
    private javax.swing.JPanel tabbedSubPanel;
    // End of variables declaration//GEN-END:variables

    public void editImage(PicturePanel panel) {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Image image = ImageIO.read(fchooser.getSelectedFile());
                Image scaledImage = image.getScaledInstance(EmployeeLogin.headerWidth, EmployeeLogin.headerHeight, Image.SCALE_SMOOTH);

                try {
                    ImageIcon newIcon = new ImageIcon(scaledImage);

//                    for (int c = 0; c < this.myCompanyList.getRowCount(); c++) {
//                        String currentCompanyId = myCompanyList.getTrueValueAt(c, 0).toString();
//                        Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);
//
//
//                        ImageLoader.saveImage("login_header.jpg", comp.getDB(), "general", newIcon);
//                    }
                    this.clientHeader.setImage(newIcon.getImage());

                } catch (Exception ex) {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addImage(PicturePanel panel) {
    }

    public void deleteImage(PicturePanel panel) {
    }

    public void downloadImage(PicturePanel panel) {
        
    }
}
