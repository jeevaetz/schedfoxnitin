/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.ireports.viewer;
import rmischedule.main.*;
import javax.swing.JOptionPane;

/**
 *
 * @author hal
 */
import javax.swing.JTabbedPane;
import java.util.HashMap;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import rmischeduleserver.mysqlconnectivity.mysqldriver;
import net.sf.jasperreports.view.JRViewer;
import rmischeduleserver.IPLocationFile;
import net.sf.jasperreports.engine.*;

public class NewEmployeeForms extends javax.swing.JFrame
{
    private Connection con;
    private Statement stmt;
    private ResultSet rs,rs1;
    private HashMap param=new HashMap();
    private JTabbedPane tabs;
    private int empNo;

    public NewEmployeeForms(int empNo)
{
    super("View Report");
    init(empNo);
}

private void init(int empNo){
    tabs=new JTabbedPane();
    this.add(tabs);
    this.empNo=empNo;
    try{
        con = DriverManager.getConnection(mysqldriver.connectString);
        stmt=con.createStatement();
    }catch(SQLException e){
        System.out.println(e);
    }
}

public void print(){
    printEmployeeForms("/home/hal/myiReports/NewEmployeeForm1.jasper");
    printEmployeeForms("/home/hal/myiReports/NewEmployeeForm2.jasper");

    setBounds(10,10,600,500);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
}

public void printEmployeeForms(String fileName){
    try{
        rs=stmt.executeQuery("select * from champion_db.employee where employee_id="+empNo);
    }catch(SQLException e){
        System.out.println(e);
    }
    tabs.add("Employee Report",printEmployeeForms(fileName,new JRResultSetDataSource(rs),
            new JasperPrint(),new HashMap()));

}
public JRViewer printEmployeeForms(String fileName,JRResultSetDataSource jrRs,JasperPrint print,HashMap param){
    try {
        print = JasperFillManager.fillReport(fileName, param, jrRs);
    } catch (Exception e) {
        System.out.println(e);
        JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Error Creating Report, May Not Have Data To Display!", "Error Printing", JOptionPane.OK_OPTION);
    }
        JRViewer viewer=new JRViewer(print);

    return(viewer);

}

public void close(){

    try{
        rs.close();
        con.close();
    }catch(SQLException sqle){}
}

public static void main(String args[])
{
    NewEmployeeForms viewer=new NewEmployeeForms(13204);
    viewer.print();


    viewer.setVisible(true);
    viewer.close();
}
}
