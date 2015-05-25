/*
 * usked_main.java
 *
 * Created on December 10, 2004, 10:17 AM
 */

package rmischedule.data_import.ultra32;
import rmischedule.main.Main_Window;
import rmischedule.components.data.dbf_reader;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import rmischedule.components.Branch_LB;
import rmischedule.data_connection.Connection;
import rmischedule.components.Company_LB;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.ImportCCS.*;

/**
 *
 * @author  jason.allen
 */
public class usked_main  extends JInternalFrame{
    /* basic information */
    public Main_Window parent;

    private Container myContentPane;
    
    /* connection */
    public Connection myConnection;
    
    private JButton     pbBrowse;
    private JButton     pbOk;
    private JButton     pbCancel;
    
    private JTextField   efDir;        
    private JFileChooser jfcDir;    
    private DataList     dlDataList;
    
    public usked_progress dlUSP;
    
    /** Creates a new instance of usked_main */
    public usked_main(Main_Window Parent) {
        /* set the title and attribs */
        setTitle("Usked Import Utility");
        
        setIconifiable(true);
        setClosable   (true);

        setResizable  (false);
        setMaximizable(false);

        parent = Parent;

        myConnection = new Connection();
        
        //myWebService = new JWebService(parent.getBase());       
        myContentPane = getContentPane();
        
        /* build our frame here */
        myContentPane.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        ((BorderLayout) topPanel.getLayout()).setHgap(4);
        
        efDir = new JTextField();
        jfcDir = new JFileChooser();
        jfcDir.setDialogType(JFileChooser.OPEN_DIALOG);
        jfcDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        pbBrowse = new JButton("Browse");
        pbBrowse.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(jfcDir.showDialog(parent, "Open") == JFileChooser.APPROVE_OPTION){
                        if(jfcDir.getSelectedFile().exists()){
                            efDir.setText(jfcDir.getSelectedFile().toString());                        
                            processDir();
                        }
                    }                    
                }
            }
        );                
        
        topPanel.add(new JLabel("Usked Data Dir:"), BorderLayout.WEST);
        topPanel.add(efDir,    BorderLayout.CENTER);
        topPanel.add(pbBrowse, BorderLayout.EAST);
        
        dlDataList = new DataList();
        JScrollPane sjc = dlDataList.getScrollPane();
        
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        
        pbOk     = new JButton("OK");
        pbCancel = new JButton("Cancel");
        
        buttonPanel.add(pbOk);
        buttonPanel.add(pbCancel);
        
        pbOk.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    //beginImport();
                    showDialog sd = new showDialog();
                    tBeginImport tbi = new tBeginImport();
                    sd.start();
                    tbi.start();
                }
            }
        );
        
        pbCancel.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    dispose();
                }
            }
        );
        
        myContentPane.add(topPanel,     BorderLayout.NORTH);
        myContentPane.add(sjc,          BorderLayout.CENTER);
        myContentPane.add(buttonPanel,  BorderLayout.SOUTH);
        
        /* add it to the dekstop and size/show it */
        parent.desktop.add(this);
        
        setSize(760, 500);
        setLocation(((parent.getWidth()/2)-(760/2)), ((parent.getHeight()/2)-(500/2)));
        show();        
    }
 
    private void processDir(){
        java.io.File dir = new java.io.File(efDir.getText());
        if(dir.exists() && dir.isDirectory()){
            String[] list = dir.list();
            Vector dirList = new Vector();
            for(int i=0;i<list.length;i++){
                java.io.File tf = new java.io.File(efDir.getText()+"/"+list[i]);
                if(tf.isDirectory()){
                    java.io.File tf2 = new java.io.File(
                        efDir.getText()+"/"+list[i]+"/setmast.dbf"
                    );
                    if(tf2.exists()){
                        dirList.add(efDir.getText()+"/"+list[i]);
                    }                    
                }
            }
            String[] ts = new String[dirList.size()];
            dirList.toArray(ts);
            dlDataList.updateList(ts);
        }
    }
    
    private void beginImport(){
         while(dlUSP == null){
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                
            }            
        }

        dlUSP.newProgress();
        clientImport();
        employeeImport();
        import_schedule();
        finalizeImport();
        JOptionPane.showMessageDialog(this, "DONE!", "DONE!", JOptionPane.INFORMATION_MESSAGE);
        dlUSP.setVisible(false);
    }
    
    private void finalizeImport(){
        int c = dlDataList.cbDataDir.length;
        for(int i=0;i<c;i++){
            if(dlDataList.cbDataDir[i].isSelected()){
                myConnection.setCompanyLB(dlDataList.clbCompany[i]);
                ImportCCSFinalizeData fn =  new ImportCCSFinalizeData();
                myConnection.prepQuery(fn);
                try{
                    myConnection.executeQuery(fn);
                }catch(Exception e){}
            }
        }
    }
    
    private void clientImport(){
        int c = dlDataList.cbDataDir.length;
        String name;
        String address;
        String address2;
        String city;
        String state;
        String zip;
        String usked_id;
        String usked_ws;
        String deleted;
        String masterCCSId;
        String phone;
        String phone2;
        String phone3;
        
        
        for(int i=0;i<c;i++){
            if(dlDataList.cbDataDir[i].isSelected()){
                myConnection.setCompanyLB(dlDataList.clbCompany[i]);
                myConnection.setBranchLB(dlDataList.blbBranch[i]);

                dbf_reader db = new dbf_reader(
                    dlDataList.strDataDir[i] + "/cusmast.dbf"
                );
                
                //New Reader for Customer Continued for Customer Phone and other info...
                dbf_reader db_cont = new dbf_reader(
                    dlDataList.strDataDir[i] + "/cuscont.dbf"
                );
                
                dlUSP.setClient(
                        db.length(),
                        "Location Import: " + dlDataList.strDataDir[i] + "/cusmast.dbf"
                        );
                rmischeduleserver.mysqlconnectivity.queries.ImportCCS.ImportCCSDataClient myQuery = new rmischeduleserver.mysqlconnectivity.queries.ImportCCS.ImportCCSDataClient();
                myConnection.initializeSession(myQuery);
                myConnection.prepQuery(myQuery);
                int numOfRecords = 0;
                do{
                    dlUSP.incClient();
                    name = new String(db.getString("CUCNAME").trim());
                    address = new String(db.getString("CUCADDR1").trim());
                    address2 = new String(db.getString("CUCADDR2").trim());
                    city = new String(db.getString("CUCCITY").trim());
                    state = new String(db.getString("CUCSTATE").trim());
                    zip = new String(db.getString("CUCZIPCODE").trim());
                    usked_id = new String(db.getString("CUCCUSTID").trim());
                    usked_ws = new String(db.getString("CUCSITECDE").trim());
                    deleted = new String((db.getString("CUCCUSMAST").equals("I")?1:0) +"");
                    masterCCSId = new String((db.getString("CUCCUSMAST")));
                    phone =  "";
                    phone2 = "";
                    phone3 = "";
                    
                    db_cont.moveFirst();
                    for (int x = 0; x < db_cont.length(); x++) {
                        if (db_cont.getString("CCCCUSMAST").trim().equals(masterCCSId)) {
                            phone = db_cont.getString("CCCPHONE1").trim();
                            phone2 = db_cont.getString("CCCPHONE2").trim();
                            phone3 = db_cont.getString("CCCPHONE3").trim();
                            x = db_cont.length();
                        }
                        db_cont.moveNext();
                    }
                    
                    myQuery.update(usked_id, usked_ws, name.toString(), phone, phone2,phone3, address, address2, city, state, zip, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", usked_ws);
                    if (numOfRecords > 200) {
                        try {
                            myConnection.executeQuery(myQuery);
                        } catch (Exception e) {}
                        numOfRecords = 0;
                    }
                    numOfRecords++;
                }while(db.moveNext());
                try {
                    myConnection.executeQuery(myQuery);
                    myQuery.clear();
                } catch (Exception e) {}
                myConnection.closeSession();
            }
        }
        dlUSP.setClient(0, "Client Import: Completed!");
    }
    
    private void runQueries(GeneralQueryFormat updateQuery, GeneralQueryFormat synchQuery) {
        try {
            myConnection.executeUpdate(updateQuery);
            myConnection.executeUpdate(synchQuery);
        } catch (Exception e) {
        }
    }
    
    private void employeeImport(){
        int c = dlDataList.cbDataDir.length;
        for(int i=0;i<c;i++){
            if(dlDataList.cbDataDir[i].isSelected()){
                myConnection.setCompanyLB(dlDataList.clbCompany[i]);
                myConnection.setBranchLB(dlDataList.blbBranch[i]);

                dbf_reader db = new dbf_reader(
                    dlDataList.strDataDir[i] + "/empmast.dbf"
                );
                
                dbf_reader empcont = new dbf_reader(
                    dlDataList.strDataDir[i] + "/empcont.dbf"
                );
                
                empcont.setIndex("ECCEMPMAST");

                dlUSP.setEmployee(
                    db.length(),
                    "Employee Import: " + dlDataList.strDataDir[i] + "/empmast.dbf"
                );
                ImportCCSDataEmployee myInsertQuery = new ImportCCSDataEmployee();
                myInsertQuery.dontUpdate();
                myConnection.initializeSession(myInsertQuery);
                int numOfRecords = 0;
                do{
                    dlUSP.incEmployee();
                    String usked_emp = db.getString("EMCEMPID").trim();
                    String fname = db.getString("EMCFNAME").trim();
                    String lname = db.getString("EMCLNAME").trim();
                    String mname = db.getString("EMCMNAME").trim();
                    String address = db.getString("EMCADDR1").trim();
                    String address2 = db.getString("EMCADDR2").trim();
                    String city = db.getString("EMCCITY").trim();
                    String state = db.getString("EMCSTATE").trim();
                    String zip = db.getString("EMCZIPCODE").trim();
                    String hiredate = db.getString("EMTHIRE").trim();
                    String termdate = db.getString("EMTTERM").trim();
                    String ssn = db.getString("EMCSSNO").replaceAll("-","");
                    String cell = empcont.getString("ECCPHONE3", db.getString("EMCEMPMAST")).trim();
                    String phone = empcont.getString("ECCPHONE1", db.getString("EMCEMPMAST")).trim();
                    String phone2 = empcont.getString("ECCPHONE2", db.getString("EMCEMPMAST")).trim();
                    String email = empcont.getString("ECCEMAIL", db.getString("EMCEMPMAST")).trim();
                    try {
                        
                        myInsertQuery.update(usked_emp, "0", fname,lname, mname, phone, phone2, cell, phone2, address, address2, city, state, zip, ssn, email, hiredate, termdate, "0");
                        try {
                                numOfRecords = 0;
                                myConnection.executeQuery(myInsertQuery);
                                myInsertQuery.clear();
                        } catch (Exception e) {}
                        
                        
                        
                    } catch (Exception e) {
                    }
                }while(db.moveNext());
                try {
                    myConnection.executeQuery(myInsertQuery);
                } catch(Exception e) {}
                myConnection.closeSession();
                
            }
        }
        dlUSP.setEmployee(0, "Employee Import: Completed!");
    }
    
    private void import_schedule(){
        int c = dlDataList.cbDataDir.length;
        int currNum = 0;
        
        for(int i=0;i<c;i++){
            if(dlDataList.cbDataDir[i].isSelected()){
                myConnection.setCompanyLB(dlDataList.clbCompany[i]);
                myConnection.setBranchLB(dlDataList.blbBranch[i]);
                
                dbf_reader db = new dbf_reader(
                        dlDataList.strDataDir[i] + "/schmast.dbf"
                        );
                
                dbf_reader db_client = new dbf_reader(
                        dlDataList.strDataDir[i] + "/cusmast.dbf"
                        );
                
                db_client.setIndex("CUCCUSMAST");
                
                dbf_reader db_employee = new dbf_reader(
                        dlDataList.strDataDir[i] + "/empmast.dbf"
                        );
                
                db_employee.setIndex("EMCEMPMAST");
                
                String eid, cid, wsid, tDt, dt, st, ed, ovd;
                String[] aDt;
                int ist, ied;
                
                dlUSP.setSchedule(
                        db.length(),
                        "Schedule Import: " + dlDataList.strDataDir[i] + "/cusmast.dbf"
                        );
                ImportCCSDataSchedule myScheduleQuery = new ImportCCSDataSchedule();
                myConnection.initializeSession(myScheduleQuery);
                int numOfRecords = 0;
                long currentTime = System.currentTimeMillis();
                for (int x = 0; x < db.length(); x++) {
                    dlUSP.incSchedule();
                    if(!db.isRecDel()){
                        // get our id from the other files
                        eid  =   db_employee.getString("EMCEMPID",   db.getString("SMCEMPMAST"));
                        cid  =   db_client.getString("CUCCUSTID",  db.getString("SMCCUSMAST"));
                        wsid =   db_client.getString("CUCSITECDE", db.getString("SMCCUSMAST"));
                        
                        // get our date and time
                        StringTokenizer stoken = new StringTokenizer(db.getString("SMTSHFTBEG"), " :");
                        dt = stoken.nextToken();
                        ist  = (Integer.parseInt(stoken.nextToken())) * 60;
                        ist += (Integer.parseInt(stoken.nextToken()));
                        if (ist % 15 == 14) {
                            ist += 1;
                        }
                        st   = String.valueOf(ist);
                        
                        // get end time in min
                        StringTokenizer stoken2 = new StringTokenizer(db.getString("SMTSHFTEND"), " .:");
                        String trash = stoken2.nextToken();
                        ied  = (Integer.parseInt(stoken2.nextToken())) * 60;
                        ied += (Integer.parseInt(stoken2.nextToken()));
                        if (ied % 15 == 14) {
                            ied += 1;
                        }
                        ed   = String.valueOf(ied);
                        String rateCode;
                        rateCode = db.getString("SMCRATECDE").trim();
                        
                        try {
                            myScheduleQuery.update(eid.trim(), cid.trim(), wsid.trim(),st,ed,dt);
                            if (numOfRecords > 300) {
                                myConnection.executeQuery(myScheduleQuery);
                                myScheduleQuery.clear();
                                numOfRecords = 0;
                            }
                            numOfRecords++;
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    db.moveNext();
                }
                long currentTime2 = System.currentTimeMillis();
                System.out.println("Took " + (currentTime2 - currentTime) + " milliseconds to do schedule");
                myConnection.closeSession();
            }
            dlUSP.setSchedule(0, "Schedule Import: Completed!");
        }
    }
     
    class DataList extends JPanel{
        public JCheckBox[]  cbDataDir;
        public Company_LB[] clbCompany;
        public Branch_LB[]  blbBranch;
        public String[]     strDataDir;
        
        private JScrollPane  myScrollPane;
        
        public DataList(){            
        
        }
        
        public void updateList(String list[]){
            removeAll();
            setLayout(new GridLayout(list.length, 3));
            
            cbDataDir   = new  JCheckBox[list.length];
            clbCompany  = new Company_LB[list.length];
            blbBranch   = new  Branch_LB[list.length];
            strDataDir  = new     String[list.length];

            dbf_reader tmpReader;
            
            for(int i=0;i<list.length;i++){
                 tmpReader = new dbf_reader(list[i]+"/setmast.dbf");

                 cbDataDir[i] = new JCheckBox(tmpReader.getString("CDDNAME"));
                clbCompany[i] = new Company_LB(parent);
                 blbBranch[i] = new Branch_LB(parent);                 
                strDataDir[i] = list[i];
                
                 add(cbDataDir[i]);
                 add(clbCompany[i]);
                 add(blbBranch[i]);
                 myScrollPane.validate();
            }
        } 
        
        public JScrollPane getScrollPane(){
            if(myScrollPane == null){
                myScrollPane = new JScrollPane(this);
            }
            return myScrollPane;
        }
    }
    
    class showDialog extends Thread{
        public showDialog() {
        }    
    
        public void run(){
            dlUSP = new usked_progress(parent,true);
            dlUSP.setVisible(true);            
        }
    }
    
    class tBeginImport extends Thread{
        public tBeginImport() {
            
        }    
    
        public void run(){
            this.setPriority(Thread.MAX_PRIORITY);
            beginImport();
        }
    }
}
