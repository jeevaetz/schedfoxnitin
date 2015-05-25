/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedfox.invoicing;
import java.sql.*;
import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.mysqldriver;
import rmischeduleserver.*;
/**
 *
 * @author hal
 */

public class SchedFoxCustomers {
    
private String invDate;
public int type;
//private String id;
public int id;
public String typeDescription;
public float stdPrice=0;
public float addEmpPrice=0;
public float textPrice=0;
public float storagePrice=0;
public float taxRate=0;
private boolean texting;
private boolean storage;
//private String cName;
//private String cAddress;
//private String cCity;
//private String cState;
//private String cZip;
public boolean isSchedFoxCustomer=false;
public boolean eof=false;
public static final int STD_PRICE=1;
private int invNoSuffix;
private int invTableId;
private String invNo;
private Connection con;
private Statement stmt;
private ResultSet rs;
private ResultSet rsEmpCnt;
public  ArrayList<comboItem> priceList=new ArrayList();
public  comboItem item;
public ArrayList<Invoice> printedInvoices=new ArrayList();

public static void main(String args[]){
    SchedFoxCustomers schedFox=new SchedFoxCustomers();
    schedFox.type=1;
    schedFox.typeDescription="Std Pricing";
    schedFox.stdPrice=100;
    schedFox.addEmpPrice=1;
    schedFox.textPrice=25;
    schedFox.stdPrice=30;
    schedFox.saveData();
    schedFox=null;
}
public SchedFoxCustomers(){
    getConnection();
    getPricing();
}

public SchedFoxCustomers(int type){
    this();
    this.type=type;
}

public void setDate(String date){
    invDate=date;
}

private void getConnection(){
    try{
        con = DriverManager.getConnection("jdbc:postgresql:" + mysqldriver.ip+IPLocationFile.getDATABASE_NAME()
            + mysqldriver.pw + mysqldriver.properties);
        stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public ResultSet getCustomers(){
    getDataQuery(getQueryString()+"order by  management_clients.management_client_name ");
//    isSchedFoxCustomer=getNext();
    return(rs);
}

public ResultSet getCustomers(String customer){
    getDataQuery(getQueryString()+"where schedfox_customer.customer_id="+String.valueOf(customer));
//    isSchedFoxCustomer=getNext();
    return(rs);

}

public void getPricingData(){
    getDataQuery(getPricingQueryString());
//    getNext();
}

private String getQueryString(){
    StringBuilder queryStr=new StringBuilder(512);
    queryStr.append("select *,(select control_db.active_employee_count(customer_id)) as empCnt from control_db.schedfox_customer ");
    queryStr.append("inner join control_db.schedfox_pricing on schedfox_customer.customer_pricing_type=schedfox_pricing.pricing_type ");
    queryStr.append("inner join control_db.management_clients on management_clients.management_id=schedfox_customer.customer_id ");
    return(queryStr.toString());
}

public int getEmpCnt(String id){
    StringBuilder queryStr=new StringBuilder();
    queryStr.append("select company_db from control_db.company where  company_management_id='").append(id).append("'");
    String db;
    int empCnt=0;
    try{
        rsEmpCnt=stmt.executeQuery(queryStr.toString());
        if(rsEmpCnt.next()){
            db=rsEmpCnt.getString("company_db");
            queryStr=new StringBuilder();
            queryStr.append("select count(employee_id) as empCnt from ").append(db).append(".employee where  employee_is_deleted=0");
            rsEmpCnt=stmt.executeQuery(queryStr.toString());
            if(rsEmpCnt.next())
                empCnt=rsEmpCnt.getInt("empCnt");
        }
        rsEmpCnt.close();
    }catch(SQLException sqle){
        sqle.printStackTrace();
    }
    return(empCnt);
}
private String getPricingQueryString(){
    StringBuilder queryStr=new StringBuilder(512);
    queryStr.append("select * from control_db.schedfox_pricing ");
    queryStr.append("order by pricing_type ");
    return(queryStr.toString());
    
}


public void getDataQuery(String queryStr){
    try{
        rs=stmt.executeQuery(queryStr.toString());
//        getNext();

    }catch(SQLException e){
        e.printStackTrace();
    }


}
public int getListIndex(){
    for(int i=0;i<priceList.size();i++){
        item=priceList.get(i);
        if((item.getType()==type))
            return(i);
    }
    return(0);
}
public void addType(){
    StringBuilder queryStr=new StringBuilder(128);
    queryStr.append("insert into control_db.schedfox_customer ");
    queryStr.append("(pricing_type,pricing_std_price,pricing_additional_employeess,");
    queryStr.append("pricing_text,pricing_storage,pricing_description,pricing_tax_rate ");
    queryStr.append("values (");
    
}
public void addCustomer(int id,String type,boolean texting,boolean storage){
    StringBuilder queryStr=new StringBuilder(128);
    queryStr.append("insert into control_db.schedfox_customer ");
    queryStr.append("(customer_id,customer_pricing_type,customer_texting,customer_storage) ");
    queryStr.append("values ("+id+","+type+","+texting+","+storage+")");
    executeQuery(queryStr.toString());

}
public void changeCustomer(int id,String type,boolean texting,boolean storage){
    StringBuilder queryStr=new StringBuilder(128);
    queryStr.append("update control_db.schedfox_customer ");
    queryStr.append("set customer_pricing_type="+type+",customer_texting="+texting+",customer_storage="+storage+" ");
    queryStr.append("where customer_id="+id);
    executeQuery(queryStr.toString());

}
public void removeCustomer(int id){
    StringBuilder queryStr=new StringBuilder(128);
    queryStr.append("delete from control_db.schedfox_customer ");
    queryStr.append("where customer_id=").append(id);
    executeQuery(queryStr.toString());
    
}
public void getPricing(){
    StringBuilder queryStr=new StringBuilder(128);
    queryStr.append("select pricing_type,pricing_description from control_db.schedfox_pricing ");
    queryStr.append("order by pricing_type");
    getDataQuery(queryStr.toString());


    try{
        while(rs.next()){
            priceList.add(new comboItem(rs.getInt("pricing_type"),rs.getString("pricing_description")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
}
public void executeQuery(String queryStr){

    try{
        stmt.execute(queryStr);
    }catch(SQLException e){
        e.printStackTrace();
    }


}

public boolean getNextCustomer(){
    boolean sw=false;

    try{
        if((sw=rs.next())){
            type=rs.getInt("pricing_type");
            typeDescription=rs.getString("pricing_description");
            stdPrice=rs.getFloat("pricing_std_price");
            addEmpPrice=rs.getFloat("pricing_additional_employees");
            textPrice=rs.getFloat("pricing_text");
            storagePrice=rs.getFloat("pricing_storage");
            taxRate=rs.getFloat("pricing_sales_tax");
            texting=rs.getBoolean("customer_texting");
            storage=rs.getBoolean("customer_storage");
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


public boolean getNextPrice(){
    boolean sw=false;
    try{
        if((sw=rs.next())){
            type=rs.getInt("pricing_type");
            typeDescription=rs.getString("pricing_description");
            stdPrice=rs.getFloat("pricing_std_price");
            addEmpPrice=rs.getFloat("pricing_additional_employees");
            textPrice=rs.getFloat("pricing_text");
            storagePrice=rs.getFloat("pricing_storage");
            taxRate=rs.getFloat("pricing_sales_tax");
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

public boolean getPreviousPrice(){
    boolean sw=false;
    try{
        if((sw=rs.previous())){
            type=rs.getInt("pricing_type");
            typeDescription=rs.getString("pricing_description");
            stdPrice=rs.getFloat("pricing_std_price");
            addEmpPrice=rs.getFloat("pricing_additional_employees");
            textPrice=rs.getFloat("pricing_text");
            storagePrice=rs.getFloat("pricing_storage");
            taxRate=rs.getFloat("pricing_sales_tax");
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
public String getDescription(){
    return(typeDescription);
}
public float getStdPrice(){
    return(stdPrice);
}
public float getAddEmpPrice(){
    return(addEmpPrice);
}
public boolean getTexting(){
    return(texting);
}
public float getTextPrice(){
        return(textPrice);
}
public boolean getStorage(){
    return(storage);
}
public float getStoragePrice(){
        return(storagePrice);
}
public float getTaxRate(){
    return (taxRate);
}
public int getType(){
    return(type);
}
public void saveData(){
    for(int i=0;i<priceList.size();i++)
        if(priceList.get(i).getType()==type){
            executeQuery(getUpdatePricingStr());
            getPricingData();
            return;
        }

    executeQuery(getInsertPricingStr());
    getPricingData();
}
private String getInsertPricingStr(){
    StringBuilder str=new StringBuilder(512);
    str.append("insert into control_db.schedfox_pricing ");
    str.append("(pricing_type,pricing_std_price,pricing_additional_employees,");
    str.append("pricing_text,pricing_storage,pricing_sales_tax,pricing_description) ");
    str.append("values("+type+","+stdPrice+","+addEmpPrice+","+textPrice+","+storagePrice+",'"+taxRate+"','"+typeDescription+"')");
    return(str.toString());
}
private String getUpdatePricingStr(){
    StringBuilder str=new StringBuilder(512);
    str.append("update control_db.schedfox_pricing ");
    str.append("set pricing_type="+type+",pricing_std_price="+stdPrice+",pricing_additional_employees="+addEmpPrice+",");
    str.append("pricing_text="+textPrice+",pricing_storage="+storagePrice+",pricing_description='"+typeDescription+"', ");
    str.append("pricing_sales_tax="+taxRate+" ");
    str.append("where pricing_type="+type);
    return(str.toString());
}
public void close(){
    try{
        con.close();
    }catch(SQLException e){

    }
}
public class comboItem{
    public int type;
    public String desc;
    comboItem(){
    }
    comboItem(int type,String desc){
        this.type=type;
        this.desc=desc;
    }
    public String toString (){
        return(type + " "+ desc);
    }
    public int getType(){
        return type;
    }
}
public class Invoice{
    private String invoiceNumber;
    private String custId;
    private String invDate;
    private float  stdPrice;
    private float  textPrice;
    private float  storagePrice;

    public Invoice(String invNo,String cust,String date,float std,float text,float storage){
        invoiceNumber=invNo;
        custId=cust;
        invDate=date;
        stdPrice=std;
        textPrice=text;
        storagePrice=storage;
    }
    public String getInvoiceNumber(){
        return invoiceNumber;
    }
    public String getCustId(){
        return custId;
    }
    public String getDate(){
        return invDate;
    }
    public String getcustId(){
        return custId;
    }
    public float getStdPrice(){
        return stdPrice;
    }
    public float getTextPrice(){
        return textPrice;
    }
    public float getStoragePrice(){
        return storagePrice;
    }
}

}
