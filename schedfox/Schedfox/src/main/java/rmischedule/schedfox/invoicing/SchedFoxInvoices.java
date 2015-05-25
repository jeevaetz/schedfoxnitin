/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * schedfox_invoices.java
 *
 * Created on Mar 22, 2011, 12:26:52 PM
 */

package rmischedule.schedfox.invoicing;
import rmischedule.main.Main_Window;
import java.sql.*;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.mysqlconnectivity.mysqldriver;
import schedfoxlib.model.Company;
import java.util.ArrayList;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem;
import javax.swing.JInternalFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.engine.*;
import java.util.HashMap;
import java.io.InputStream;
import rmischeduleserver.RMIScheduleServerImpl;
/**
 *
 * @author hal
 */
public class SchedFoxInvoices extends javax.swing.JInternalFrame {


private Connection con;
private Statement stmt;
private ResultSet rs;
private String invDate;
public ArrayList<Invoice> invoiceTable=new ArrayList();
private Object tabData[][];
public SchedFoxCustomers schedCust=new SchedFoxCustomers();
private int invNoSuffix=0;
private String invNo;
private ResultSet rsCust;
public  sjq.print.PrintPreviewInternalFrame ppf;
private int selIndx=-1;

public boolean isSchedFoxCustomer=false;
public boolean eof=false;
private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

public SchedFoxInvoices() {
    initComponents();
    initMenuComponents();
    setClosable(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    getConnection();
    schedCust=new SchedFoxCustomers();
    printInvoiceButton.setEnabled(false);
}

public void createInvoices(String date,String servicePeriod){
    invoiceTable=new ArrayList();
    rsCust=schedCust.getCustomers();
    createInvoice(date,servicePeriod);
    printInvoiceButton.setEnabled(true);
}

public void createCustInvoice(String custId,String date){
    if(date.equals("")||date.length()==0)
        JOptionPane.showInputDialog(this,"Enter Invoice Date", invDate);
    if(date.equals("")||date.length()==0)
        return;
    invoiceTable=new ArrayList();
    rsCust=schedCust.getCustomers(custId);
    createInvoice(date,"");
    printInvoiceButton.setEnabled(true);
}

private void createInvoice(String date,String servicePeriod){
    invNoSuffix=getBegginingInvoiceNumberSuffix();
    Invoice inv = new Invoice();
    while(getNextCustomer(inv)){
        inv.invoiceNumber=getNextInvoiceNumber(inv.cName);
        inv.invDate=date;
        inv.servicePeriod=servicePeriod;
        invoiceTable.add(inv);
        inv=new Invoice();
    }
    try{
        rsCust.close();
    }catch(SQLException sqle){}
    for(int i=0;i<invoiceTable.size();i++){
        invoiceTable.get(i).empCnt=schedCust.getEmpCnt(invoiceTable.get(i).custId);
        invoiceTable.get(i).calcAddEmpCost();
        invoiceTable.get(i).calcTotal();
    }
    setTableModel();

}

public boolean getNextCustomer(Invoice inv){
    boolean sw=false;

    try{
        if((sw=rsCust.next())){
            inv.custId=rsCust.getString("management_id");
            inv.type=rsCust.getInt("pricing_type");
            inv.typeDescription=rsCust.getString("pricing_description");
            inv.stdPrice=rsCust.getFloat("pricing_std_price");
            inv.addEmpPrice=rsCust.getFloat("pricing_additional_employees");
            inv.texting=rsCust.getBoolean("customer_texting");
            if(inv.texting==true)
                inv.textPrice=rsCust.getFloat("pricing_text");
            else
                inv.textPrice=0;
            inv.storage=rsCust.getBoolean("customer_storage");
            if(inv.storage==true)
                inv.storagePrice=rsCust.getFloat("pricing_storage");
            else
                inv.storagePrice=0;
            inv.salesTaxRate=rsCust.getFloat("pricing_sales_tax");
            inv.cName=rsCust.getString("Management_client_name");
            inv.cAddress=rsCust.getString("Management_client_address");
            inv.cCity=rsCust.getString("Management_client_city");
            inv.cState=rsCust.getString("Management_client_state");
            inv.cZip=rsCust.getString("Management_client_zip");
            isSchedFoxCustomer=true;
        }
    }catch(SQLException e){
        e.printStackTrace();
    }

    if(sw)
        eof=false;
    else
        eof=true;

    return(sw);
}

private void getConnection(){
    try{
        con = DriverManager.getConnection("jdbc:postgresql:" + mysqldriver.ip+IPLocationFile.getDATABASE_NAME()
            + mysqldriver.pw + mysqldriver.properties);
        stmt=con.createStatement();
    }catch(Exception e){
        e.printStackTrace();
    }
}

public String getNextInvoiceNumber(String name){
    return(getInvoiceNumberPrefix(name)+getInvoiceNumberSuffix());
}

private String getInvoiceNumberSuffix(){
    invNoSuffix++;
    if(invNoSuffix<10)
        return ("000"+String.valueOf(invNoSuffix));
    if(invNoSuffix<100)
        return ("00"+String.valueOf(invNoSuffix));
    if(invNoSuffix<1000)
        return ("0"+String.valueOf(invNoSuffix));

    return (String.valueOf(invNoSuffix));
}

private int getBegginingInvoiceNumberSuffix(){
    return (getNextTableId());
}

private String getInvoiceNumberPrefix(String name){
    String str=new String();
    str=name.toLowerCase();
    int x;
    int i=str.indexOf(" ");

    x=i+4;
    if(x>str.length())
        x=str.length();

    if(i>-1)
        return(str.substring(0,1)+str.substring(i+1,x));

    x=4;
    if(x>str.length())
        x=str.length();

    return(str.substring(0,x));

}
    private void rePrintInvoice(Invoice inv){
        sjq.print.PrintDocument doc = new sjq.print.PrintDocument("SchedFox Invoices");
//        doc.addComponent(getInvoicePage(inv));
        ppf = new sjq.print.PrintPreviewInternalFrame(doc);
        Main_Window.parentOfApplication.desktop.add(ppf);
        ppf.displayPrintPreview();
    }

    private void printInvoices(){
        sjq.print.PrintDocument doc = new sjq.print.PrintDocument("SchedFox Invoices");

//        for(int i=0;i<invoiceTable.size();i++){
//            doc.addComponent(getInvoicePage(invoiceTable.get(i)));
//        }

        ppf = new sjq.print.PrintPreviewInternalFrame(doc);
        Main_Window.parentOfApplication.desktop.add(ppf);
        ppf.displayPrintPreview();

    }

//    private InvoiceReportPage getInvoicePage(Invoice inv){
//        ArrayList<ItemizedSingleCharge> myItemized = new ArrayList();
//        ArrayList<String> payRollData = new ArrayList();
//        int numEmployees = 0;
//
//        payRollData.add(inv.typeDescription);
//        payRollData.add("1");
//        double amount = inv.stdPrice;
//        NumberFormat nf = NumberFormat.getCurrencyInstance();
//        payRollData.add(nf.format(amount));
//        payRollData.add(nf.format(amount));
//        myItemized.add(new ItemizedSingleChargeArray(payRollData));
//
//        if(inv.empCost> 0){
//            double chargePerEmployee=inv.addEmpPrice;
//            numEmployees=inv.empCnt-25;
//            amount = (numEmployees * chargePerEmployee);
//            payRollData.clear();
//            payRollData.add("Employees over 25");
//            payRollData.add(String.valueOf(numEmployees));
//            payRollData.add(nf.format(chargePerEmployee));
//            payRollData.add(nf.format(amount));
//            myItemized.add(new ItemizedSingleChargeArray(payRollData));
//        }
//        if(inv.textPrice>0){
//            payRollData.clear();
//            payRollData.add("Texting");
//            payRollData.add("");
//            payRollData.add(nf.format(inv.textPrice));
//            payRollData.add(nf.format(inv.textPrice));
//            myItemized.add(new ItemizedSingleChargeArray(payRollData));
//        }
//        if(inv.storagePrice>0){
//            payRollData.clear();
//            payRollData.add("Storage");
//            payRollData.add("");
//            payRollData.add(nf.format(inv.storagePrice));
//            payRollData.add(nf.format(inv.storagePrice));
//            myItemized.add(new ItemizedSingleChargeArray(payRollData));
//        }
//
//        return(new InvoiceReportPage("SchedFox Inc.",
//                "1616 Gateway Blvd.",
//                "Richardson",
//                "TX",
//                "75080",
//                "214-615-9100",
//                inv.cName,
//                inv.invDate,
//                inv.servicePeriod,
//                inv.invoiceNumber,
//                inv.salesTaxRate,
//                inv.returnedCheckCharge,
//                new ArrayList<InvoiceHourlySingleCharge>(),
//                myItemized,
//                Main_Window.LetterHead,
//                inv.cAddress,
//                inv.cCity + ", " + inv.cState + " " + inv.cZip));
//    }

public void getPrintedInvoices(String date){

    getDataQuery(getPrintedInvoiceQuerySring(date));
    invoiceTable=new ArrayList();
    Invoice inv=new Invoice();
    while(getNextInvoice(inv)){
        inv.calcTotal();
        invoiceTable.add(inv);
        inv=new Invoice();
    }
    setTableModel();
}
private String getPrintedInvoiceQuerySring(String date){
    StringBuilder str=new StringBuilder(512);
    str.append("select * from control_db.schedfox_printed_invoices ");
    if(date.length()>0){
        str.append("where invoice_date ='").append(date).append("' ");
    }
    str.append("order by schedfox_printed_invoices.invoice_date, schedfox_printed_invoices.invoice_number ");
    return(str.toString());
}
public void getDataQuery(String queryStr){
    try{
        rs=stmt.executeQuery(queryStr.toString());
    }catch(SQLException e){
        e.printStackTrace();
    }
}
public boolean getNextInvoice(Invoice inv){
    boolean sw=false;
    try{
        if((sw=rs.next())){
            inv.invoiceNumber=rs.getString("invoice_number");
            inv.custId=rs.getString("invoice_cust_id");
            inv.invDate=rs.getString("invoice_date");
            inv.servicePeriod=rs.getString("invoice_service_period");
            inv.type=rs.getInt("invoice_pricing");
            inv.typeDescription=rs.getString("pricing_description");
            inv.stdPrice=rs.getFloat("invoice_std_price");
            inv.textPrice=rs.getFloat("invoice_texting");
            inv.salesTax=rs.getFloat("invoice_sales_tax");
            inv.salesTaxRate=rs.getFloat("invoice_sales_tax_rate");
            inv.storagePrice=rs.getFloat("invoice_storage");
            inv.returnedCheckCharge=rs.getFloat("check_charge");
            inv.payment=rs.getFloat("payments_amount");
            inv.empCnt=rs.getInt("invoice_emp_cnt");
            inv.empCost=rs.getFloat("invoice_emp_cost");
            inv.addEmpPrice=rs.getFloat("invoice_add_emp_price");
            inv.cName=rs.getString("management_client_name");
            inv.cAddress=rs.getString("management_client_address");
            inv.cCity=rs.getString("management_client_city");
            inv.cState=rs.getString("management_client_state");
            inv.cZip=rs.getString("management_client_zip");
        }
    }catch(SQLException e){
        e.printStackTrace();
    }

    return(sw);
}

public void insertInvoices(){

    for(int i=0;i<invoiceTable.size();i++){
        if(insertInvoice(invoiceTable.get(i))!=0)
            break;
    }
}

private int insertInvoice(Invoice inv){
    StringBuilder str=new StringBuilder(512);
    str.append("insert into control_db.schedfox_invoices");
    str.append("(invoice_number,invoice_date,invoice_cust_id,invoice_std_price,");
    str.append("invoice_texting,invoice_storage,invoice_emp_cnt,invoice_emp_cost,");
    str.append("invoice_add_emp_price,invoice_sales_tax,invoice_sales_tax_rate,invoice_pricing,invoice_service_period) ");
    str.append("values('").append(inv.invoiceNumber).append("','").append(inv.invDate).append("','");
    str.append(inv.custId).append("','").append(inv.stdPrice).append("','");
    str.append(inv.textPrice).append("','").append(inv.storagePrice).append("','");
    str.append(inv.empCnt).append("','").append(inv.empCost).append("','");
    str.append(inv.addEmpPrice).append("','").append(inv.salesTax).append("','");
    str.append(inv.salesTaxRate).append("','").append(inv.type).append("','").append(inv.servicePeriod).append("')");
    try{
        stmt.executeUpdate(str.toString());
    }catch(SQLException sqle){
        if(JOptionPane.showInternalConfirmDialog(this,sqle.getMessage()+" Do you want to continue? ",
                "Duplicate",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
            return(1);

        sqle.printStackTrace();
    }
    return(0);
}


private int getNextTableId(){
    int id=0;
    try{
        rs=stmt.executeQuery("select max(invoice_id) as lastId from control_db.schedfox_invoices");
        if(rs.next())
            id=rs.getInt("lastId");
        else
            id=-1;
    }catch(SQLException sqle){

    }
    return(id);
}
private void payInvoiceDialog(Invoice inv){
    java.awt.Rectangle rt=new java.awt.Rectangle();
    rt=Main_Window.parentOfApplication.getBounds();
    int x=(int) (rt.getX()+(rt.getWidth()/2)-150);
    paymentDialog.setBounds(x,(int)rt.getY()+300,300, 200);
    payDialogInvNumber.setText(inv.invoiceNumber);
    Calendar cal=Calendar.getInstance();
    String date=df.format(cal.getTime());
    payDialogDate.setText(date);
    payDialogCheckNum.setText("");
    payDialogAmount.setText(String.valueOf(inv.totalInv));
    paymentDialog.setVisible(true);
    if(payDialogInvNumber.getText().length()==0)
       payDialogInvNumber.requestFocusInWindow();
    else
        payDialogCheckNum.requestFocusInWindow();
}
private void returnedCheckDialog(Invoice inv){
    if(inv.payment==0){
        JOptionPane.showMessageDialog(this,"There is no payment for this Invoice");
        return;
    }
    String charge=returnedCheckDialog(inv.invoiceNumber);
    if(charge.length()>0)
        inv.returnedCheckCharge=Float.valueOf(charge);
}

private String  returnedCheckDialog(String inv) {
    java.awt.Rectangle rt=new java.awt.Rectangle();
    rt=Main_Window.parentOfApplication.getBounds();
    int x=(int) (rt.getX()+(rt.getWidth()/2)-150);
    returnedCheckDialog.setBounds(x,(int)rt.getY()+300,300, 200);
    Calendar cal=Calendar.getInstance();
    String date=df.format(cal.getTime());
    returnedCheckDialogCheckNum.setText("");
    returnedCheckDialogInvNumber.setText(inv);
    returnedCheckDialogCharge.setText("30.00");
    returnedCheckDialog.setVisible(true);
    if(returnedCheckDialogInvNumber.getText().length()==0)
       returnedCheckDialogInvNumber.requestFocusInWindow();
    else
       returnedCheckDialogCheckNum.requestFocusInWindow();
    return returnedCheckDialogCharge.getText();
}
private void insertReturnedCheck(){
    if(returnedCheckDialogCheckNum.getText().length()==0)
        JOptionPane.showMessageDialog(this,"Enter Check Number");
    else
        insertReturnedCheck(returnedCheckDialogInvNumber.getText(),returnedCheckDialogCheckNum.getText(),
            Float.valueOf(returnedCheckDialogCharge.getText()));
}
private void insertReturnedCheck(String invNo,String checkNo,float charge){
    StringBuilder str=new StringBuilder(512);
    str.append("insert into control_db.schedfox_returned_checks ");
    str.append("(check_number,check_inv_number,check_charge) ");
    str.append("values('").append(checkNo).append("','").append(invNo).append("',").append(charge).append(")");
    try{
        stmt.execute(str.toString());
        JOptionPane.showMessageDialog(this,"Check has been Returned");
        if(selIndx!=-1){
            tabData[selIndx][11]=invoiceTable.get(selIndx).returnedCheckCharge;
            setModel();
        }
    }catch(SQLException sqle){
        JOptionPane.showInternalMessageDialog(this, sqle.getMessage());
    }

}

public void payInvoice() {
    if(payDialogCheckNum.getText().length()==0){
        JOptionPane.showMessageDialog(this, "Enter a Check Number");
        return;
    }

    float payment=Float.parseFloat(payDialogAmount.getText());
    insertPayment(payDialogInvNumber.getText(),payDialogDate.getText(),payment);
    paymentDialog.setVisible(false);
    if(selIndx!=-1){
        invoiceTable.get(selIndx).payment=payment;
        tabData[selIndx][10]=payment;
        setModel();
    }
    
}
private void insertPayment(String invNo,String date,float pay){
    StringBuilder str=new StringBuilder(512);
    str.append("insert into control_db.schedfox_invoice_payments ");
    str.append("(payments_invoice_number,payments_date,payments_amount,payments_check_no) ");
    str.append("values('").append(invNo).append("','").append(date).append("',");
    str.append(pay).append(",'").append(payDialogCheckNum.getText()).append("')");
    try{
        stmt.execute(str.toString());
//        insertCheck(payDialogCheckNum.getText(),invNo);
    }catch(SQLException sqle){
        JOptionPane.showInternalMessageDialog(this, sqle.getMessage());
    }
    
}

//private String getPaymentReportQueryString(String date){
//    StringBuilder str=new StringBuilder(512);
//    str.append("select * from control_db.schedfox_payments_view ");
//    if(date.length()>0)
//        str.append("where to_char(schedfox_payments_view.invoice_date, 'YYYY-MM-DD')='").append(date).append("' ");
//    str.append("order by management_client_name,invoice_date;");
//
//    return(str.toString());
//}
//private String getInvoiceReportQueryString(String date){
//    StringBuilder str=new StringBuilder(512);
//    str.append("select *,to_char(schedfox_invoice_total.invoice_date, 'YYYY-MM-DD') as invDate ");
//    str.append("from control_db.schedfox_invoice_total ");
//    str.append("left outer join control_db.management_clients on management_clients.management_id = schedfox_invoice_total.invoice_cust_id ");
//    if(date.length()>0)
//        str.append("where to_char(schedfox_invoice_total.invoice_date, 'YYYY-MM-DD')='").append(date).append("' ");
//    str.append("order by management_clients.management_client_name,schedfox_invoice_total.invoice_date");
//
//    return(str.toString());
//}
//private String getAgingQueryString(){
//    StringBuilder str =new StringBuilder(512);
//    str.append("select * ");
//    str.append("from control_db.schedfox_invoice_aging where payment = 0 ");
//    str.append("or payment < totalinvoice order by management_client_name,invoice_date");
//    return str.toString();
//}
public void printInvoiceReport(){
    String date=javax.swing.JOptionPane.showInputDialog(this,"Invoice Date");
    if(date!=null && date.length()==0)
        JOptionPane.showMessageDialog(this,"An Invoice Date Must be Entered");
    else if(date != null){
        HashMap param=new HashMap();
        param.put("reportDate",date);
        param.put("header","");
        printReport("/rmischedule/ireports/SchedFoxInvoices.jasper",param);
    }
}
public void printPaymentReport(){
    String date=javax.swing.JOptionPane.showInputDialog(this,"Invoice Date");
    if(date!=null && date.length()==0)
        JOptionPane.showMessageDialog(this,"An Invoice Date Must be Entered");
    else if(date != null){
        HashMap param=new HashMap();
        param.put("reportDate",date);
        param.put("header","");
        printReport("/rmischedule/ireports/SchedFoxInvoicePayments.jasper",param);
    }
}
public void printAgingReport(){
    HashMap param=new HashMap();
    printReport("/rmischedule/ireports/SchedFoxInvoiceAging.jasper",param);
}

public void printReport(String report,HashMap param) {

    JasperPrint print=new JasperPrint();
    try {
        InputStream reportStream =
                getClass().getResourceAsStream(report);
        print=JasperFillManager.fillReport(reportStream, param, RMIScheduleServerImpl.getConnection().generateConnection());
    }catch (Exception e) {
        System.out.println(e);
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error Printing", JOptionPane.OK_OPTION);
    }
    JRViewer viewer=new JRViewer(print);
    JInternalFrame frame=new JInternalFrame();
    frame.add(viewer);
    Main_Window.parentOfApplication.desktop.add(frame);
    frame.setVisible(true);
    frame.setClosable(true);
    frame.setBounds(0,0,900,600);
}

private void setTableModel(){

    tabData=new Object[invoiceTable.size()][12];

    for(int i=0;i<invoiceTable.size();i++){
        tabData[i][0]=invoiceTable.get(i).invoiceNumber;
        tabData[i][1]=invoiceTable.get(i).cName;
        tabData[i][2]=invoiceTable.get(i).invDate;
        tabData[i][3]=invoiceTable.get(i).stdPrice;
        tabData[i][4]=invoiceTable.get(i).empCnt;
        tabData[i][5]=invoiceTable.get(i).empCost;
        tabData[i][6]=invoiceTable.get(i).textPrice;
        tabData[i][7]=invoiceTable.get(i).storagePrice;
        tabData[i][8]=invoiceTable.get(i).salesTax;
        tabData[i][9]=invoiceTable.get(i).totalInv;
        tabData[i][10]=invoiceTable.get(i).payment;
        tabData[i][11]=invoiceTable.get(i).returnedCheckCharge;
    }
    setModel();

}
private void setModel(){
    String str[]=new String[]{"Number","Cust","Date","StdPrice","Employee Cnt","Employee Cost",
            "TextPrice","StoragePrice","sales Tax","Total Invoice","Payment","Rtn Chk"};

    javax.swing.table.DefaultTableModel mod=new javax.swing.table.DefaultTableModel(tabData,str);
    jTable1.setModel(mod);
    selIndx=-1;
}
/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenu1 = new javax.swing.JPopupMenu();
        reportPopupMenu = new javax.swing.JPopupMenu();
        invoiceReportMenuItem = new javax.swing.JMenuItem();
        paymentReportMenuItem = new javax.swing.JMenuItem();
        agingReportMenuItem = new javax.swing.JMenuItem();
        paymentDialog = new javax.swing.JFrame();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        payDialogInvNumber = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        payDialogDate = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        payDialogCheckNum = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        payDialogAmount = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        payButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        returnedCheckDialog = new javax.swing.JFrame();
        jPanel15 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        returnedCheckDialogInvNumber = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        returnedCheckDialogCheckNum = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        returnedCheckDialogCharge = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        returnCheckButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        printInvoiceButton = new javax.swing.JButton();
        createInvoiceButton = new javax.swing.JButton();
        viewInvoiceButton = new javax.swing.JButton();
        payInvoiceButton = new javax.swing.JButton();
        printReportButton = new javax.swing.JButton();
        printReportButton1 = new javax.swing.JButton();

        invoiceReportMenuItem.setText("Invoices");
        reportPopupMenu.add(invoiceReportMenuItem);

        paymentReportMenuItem.setText("Payments");
        reportPopupMenu.add(paymentReportMenuItem);

        agingReportMenuItem.setText("Aging");
        reportPopupMenu.add(agingReportMenuItem);

        paymentDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel7.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 18));
        jLabel7.setText("Pay SchedFox Invoices\n");
        jPanel9.add(jLabel7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        paymentDialog.getContentPane().add(jPanel9, gridBagConstraints);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setText("Invoice Number");
        jLabel8.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel8.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel8.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel10.add(jLabel8);

        payDialogInvNumber.setMaximumSize(new java.awt.Dimension(100, 27));
        payDialogInvNumber.setMinimumSize(new java.awt.Dimension(100, 27));
        payDialogInvNumber.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel10.add(payDialogInvNumber);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        paymentDialog.getContentPane().add(jPanel10, gridBagConstraints);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.X_AXIS));

        jLabel9.setText("Pay Date");
        jLabel9.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel9.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel9.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel11.add(jLabel9);

        payDialogDate.setMaximumSize(new java.awt.Dimension(100, 27));
        payDialogDate.setMinimumSize(new java.awt.Dimension(100, 27));
        payDialogDate.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel11.add(payDialogDate);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        paymentDialog.getContentPane().add(jPanel11, gridBagConstraints);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.X_AXIS));

        jLabel10.setText("Check Number");
        jLabel10.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel10.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel10.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel12.add(jLabel10);

        payDialogCheckNum.setMaximumSize(new java.awt.Dimension(100, 27));
        payDialogCheckNum.setMinimumSize(new java.awt.Dimension(100, 27));
        payDialogCheckNum.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel12.add(payDialogCheckNum);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        paymentDialog.getContentPane().add(jPanel12, gridBagConstraints);

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.X_AXIS));

        jLabel11.setText("Amount");
        jLabel11.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel11.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel11.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel13.add(jLabel11);

        payDialogAmount.setMaximumSize(new java.awt.Dimension(100, 27));
        payDialogAmount.setMinimumSize(new java.awt.Dimension(100, 27));
        payDialogAmount.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel13.add(payDialogAmount);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        paymentDialog.getContentPane().add(jPanel13, gridBagConstraints);

        payButton.setText("Pay Invoice");
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });
        jPanel14.add(payButton);

        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        paymentDialog.getContentPane().add(jPanel14, gridBagConstraints);

        returnedCheckDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel12.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 18));
        jLabel12.setText("Returned Check\n");
        jPanel15.add(jLabel12);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        returnedCheckDialog.getContentPane().add(jPanel15, gridBagConstraints);

        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        jLabel13.setText("Invoice Number");
        jLabel13.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel13.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel13.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel16.add(jLabel13);

        returnedCheckDialogInvNumber.setMaximumSize(new java.awt.Dimension(100, 27));
        returnedCheckDialogInvNumber.setMinimumSize(new java.awt.Dimension(100, 27));
        returnedCheckDialogInvNumber.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel16.add(returnedCheckDialogInvNumber);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        returnedCheckDialog.getContentPane().add(jPanel16, gridBagConstraints);

        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.X_AXIS));

        jLabel15.setText("Check Number");
        jLabel15.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel15.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel15.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel18.add(jLabel15);

        returnedCheckDialogCheckNum.setMaximumSize(new java.awt.Dimension(100, 27));
        returnedCheckDialogCheckNum.setMinimumSize(new java.awt.Dimension(100, 27));
        returnedCheckDialogCheckNum.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel18.add(returnedCheckDialogCheckNum);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        returnedCheckDialog.getContentPane().add(jPanel18, gridBagConstraints);

        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.X_AXIS));

        jLabel16.setText("Charge");
        jLabel16.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel16.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel16.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel19.add(jLabel16);

        returnedCheckDialogCharge.setMaximumSize(new java.awt.Dimension(100, 27));
        returnedCheckDialogCharge.setMinimumSize(new java.awt.Dimension(100, 27));
        returnedCheckDialogCharge.setPreferredSize(new java.awt.Dimension(100, 27));
        jPanel19.add(returnedCheckDialogCharge);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        returnedCheckDialog.getContentPane().add(jPanel19, gridBagConstraints);

        returnCheckButton.setText("Process");
        returnCheckButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnCheckButtonActionPerformed(evt);
            }
        });
        jPanel20.add(returnCheckButton);

        jButton5.setText("Cancel");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel20.add(jButton5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        returnedCheckDialog.getContentPane().add(jPanel20, gridBagConstraints);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jTable1.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 16));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        jTable1.setRowHeight(22);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 36));

        printInvoiceButton.setText("Print Invoices");
        printInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvoiceButtonActionPerformed(evt);
            }
        });
        jPanel1.add(printInvoiceButton);

        createInvoiceButton.setText("Create Invoices");
        createInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createInvoiceButtonActionPerformed(evt);
            }
        });
        jPanel1.add(createInvoiceButton);

        viewInvoiceButton.setText("View Invoices");
        viewInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewInvoiceButtonActionPerformed(evt);
            }
        });
        jPanel1.add(viewInvoiceButton);

        payInvoiceButton.setText("Pay Invoices");
        payInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payInvoiceButtonActionPerformed(evt);
            }
        });
        jPanel1.add(payInvoiceButton);

        printReportButton.setText("Print Report");
        printReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printReportButtonMouseClicked(evt);
            }
        });
        printReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReportButtonActionPerformed(evt);
            }
        });
        jPanel1.add(printReportButton);

        printReportButton1.setText("Returned Check");
        printReportButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printReportButton1MouseClicked(evt);
            }
        });
        printReportButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReportButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(printReportButton1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initMenuComponents(){
        JMenuItem payPopupItem=new JMenuItem("Pay");
        payPopupItem.addActionListener(createPayPopupListener());
        jPopupMenu1.add(payPopupItem);
        payPopupItem=new JMenuItem("Return Check");
        payPopupItem.addActionListener(createReturnCheckPopupListener());
        jPopupMenu1.add(payPopupItem);
        payPopupItem=new JMenuItem("Reprint Invoice");
        payPopupItem.addActionListener(createRePrintCheckPopupListener());
        jPopupMenu1.add(payPopupItem);

        invoiceReportMenuItem.addActionListener(createInvoiceReportPopupListener());
        paymentReportMenuItem.addActionListener(createPaymentReportPopupListener());
        agingReportMenuItem.addActionListener(createAgingReportPopupListener());

    }
    private void printInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceButtonActionPerformed
        printInvoices();
        insertInvoices();
        printInvoiceButton.setEnabled(false);

    }//GEN-LAST:event_printInvoiceButtonActionPerformed

    private void viewInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewInvoiceButtonActionPerformed
        invDate=javax.swing.JOptionPane.showInputDialog(this,"Enter Date(blank for All)");
        if(invDate!=null){
//            String cust=javax.swing.JOptionPane.showInputDialog(this,"Enter Customer(blank for All)");
//            if(cust!=null){
                getPrintedInvoices(invDate);
                printInvoiceButton.setEnabled(false);
//            }
        }
    }//GEN-LAST:event_viewInvoiceButtonActionPerformed

    private void createInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createInvoiceButtonActionPerformed
        Calendar cal=Calendar.getInstance();
        String date=df.format(cal.getTime());
        String servicePeriod=new String();
        invDate=javax.swing.JOptionPane.showInputDialog(this,"Enter Invoice Date as YYYY-MM-DD",date);
        if(invDate!=null&&invDate.length()>0){
            servicePeriod=javax.swing.JOptionPane.showInputDialog(this,"Enter Service Period");
            if(servicePeriod!=null)
                if(servicePeriod.length()>0)
                    createInvoices(invDate,servicePeriod);
            printInvoiceButton.setEnabled(true);
        }
    }//GEN-LAST:event_createInvoiceButtonActionPerformed

    private void payInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payInvoiceButtonActionPerformed
        payInvoiceDialog(new Invoice());
    }//GEN-LAST:event_payInvoiceButtonActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if(evt.getButton()==java.awt.event.MouseEvent.BUTTON3)
            jPopupMenu1.show(jTable1,evt.getX(),evt.getY());
    }//GEN-LAST:event_jTable1MouseClicked

    private void printReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReportButtonActionPerformed
//        printInvoiceReport();
    }//GEN-LAST:event_printReportButtonActionPerformed

    private void printReportButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printReportButtonMouseClicked
        reportPopupMenu.show(printReportButton,evt.getX(),evt.getY()-100);

    }//GEN-LAST:event_printReportButtonMouseClicked

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
        payInvoice();
    }//GEN-LAST:event_payButtonActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        paymentDialog.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void returnCheckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnCheckButtonActionPerformed
        insertReturnedCheck();
    }//GEN-LAST:event_returnCheckButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        returnedCheckDialog.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void printReportButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printReportButton1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_printReportButton1MouseClicked

    private void printReportButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReportButton1ActionPerformed
        returnedCheckDialog("");
    }//GEN-LAST:event_printReportButton1ActionPerformed
    private ActionListener createPayPopupListener(){
        return(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                selIndx=jTable1.getSelectedRow();
                if(selIndx!=-1){
                    payInvoiceDialog(invoiceTable.get(selIndx));
                }
            }});
    }
    private ActionListener createReturnCheckPopupListener(){
        return(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                selIndx=jTable1.getSelectedRow();
                if(selIndx!=-1)
                    returnedCheckDialog(invoiceTable.get(selIndx));
            }});
    }
    private ActionListener createRePrintCheckPopupListener(){
        return(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                int i=jTable1.getSelectedRow();
                if(i!=-1){
                    Invoice inv =invoiceTable.get(i);
                    rePrintInvoice(inv);
                }
            }});
    }
    private ActionListener createInvoiceReportPopupListener(){

        return(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                printInvoiceReport();
            }});
    }
    private ActionListener createPaymentReportPopupListener(){

        return(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                printPaymentReport();
            }});
    }
    private ActionListener createAgingReportPopupListener(){

        return(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                printAgingReport();
            }});
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SchedFoxInvoices schedInv=new SchedFoxInvoices();
                schedInv.setVisible(true);
                schedInv.setTableModel();
            }
        });
    }
public class Invoice{
    public int  type;
    public String typeDescription;
    public String invoiceNumber;
    public String custId;
    public String invDate;
    public String servicePeriod;
    public float  stdPrice;
    public float  salesTaxRate;
    public float  salesTax;
    public int    empCnt;
    public float  addEmpPrice;
    public float  empCost;
    public boolean texting;
    public float  textPrice;
    public boolean storage;
    public float  storagePrice;
    public float  totalInv;
    public float  totalLines;
    public float  returnedCheckCharge;
    public String cName;
    public String cAddress;
    public String cCity;
    public String cState;
    public String cZip;
    public float  payment;
    private NumberFormat nf= NumberFormat.getNumberInstance();
    public Invoice(){
        
    }
    private void calcTotal(){
//        if(!texting)
//            textPrice=0;
//        if(!storage)
//            storagePrice=0;
        nf.setMaximumFractionDigits(2);
        totalLines=stdPrice+empCost+textPrice+storagePrice;
        nf.setMaximumFractionDigits(2);
        salesTax=Float.valueOf(nf.format(totalLines*salesTaxRate));
        totalInv=Float.valueOf(nf.format(totalLines+salesTax));
    }
    private void calcAddEmpCost(){
        
        if(empCnt>25)
            empCost=(empCnt-25)*addEmpPrice;
        else
            empCost=0;
        
    }
    
private class payDialog{
}



}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem agingReportMenuItem;
    private javax.swing.JButton createInvoiceButton;
    private javax.swing.JMenuItem invoiceReportMenuItem;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton payButton;
    private javax.swing.JTextField payDialogAmount;
    private javax.swing.JTextField payDialogCheckNum;
    private javax.swing.JTextField payDialogDate;
    private javax.swing.JTextField payDialogInvNumber;
    private javax.swing.JButton payInvoiceButton;
    private javax.swing.JFrame paymentDialog;
    private javax.swing.JMenuItem paymentReportMenuItem;
    private javax.swing.JButton printInvoiceButton;
    private javax.swing.JButton printReportButton;
    private javax.swing.JButton printReportButton1;
    private javax.swing.JPopupMenu reportPopupMenu;
    private javax.swing.JButton returnCheckButton;
    private javax.swing.JFrame returnedCheckDialog;
    private javax.swing.JTextField returnedCheckDialogCharge;
    private javax.swing.JTextField returnedCheckDialogCheckNum;
    private javax.swing.JTextField returnedCheckDialogInvNumber;
    private javax.swing.JButton viewInvoiceButton;
    // End of variables declaration//GEN-END:variables

}
