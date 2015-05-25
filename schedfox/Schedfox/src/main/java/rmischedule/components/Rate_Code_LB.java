/*
 * NewBranch_LB.java
 *
 * Created on February 23, 2005, 1:31 PM
 */

package rmischedule.components;
import schedfoxlib.model.util.Record_Set;
import rmischedule.main.Main_Window;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.util.Vector;
import java.util.HashMap;

import javax.swing.*;
import rmischeduleserver.mysqlconnectivity.queries.rate_codes.rate_code_list_query;
import rmischeduleserver.data_connection_types.*;
import rmischedule.data_connection.Connection;
/**
 *
 * @author  ira
 */
public class Rate_Code_LB extends javax.swing.JPanel {
    
    private Vector  <String> RateCodeId;
    private Vector  <String> RateCodeName;
    private Vector  <String> RateCodeUsked;
    
    private HashMap RateCodeHash;
    private HashMap UskedHash;
    
    private Main_Window parent;
    
    private ActionListener myActionListener = null;    
    private Record_Set myRs;
    
    /**
     * Create new Rate_Code_LB from another
     */
    public Rate_Code_LB(Rate_Code_LB rcl){
        parent     = Main_Window.parentOfApplication;
        initComponents();
        updateList(rcl.getRecordSet());
        lbRateCode.addActionListener(new Rate_Code_LB.RateCodeActionListener(this));        
    }
    
    /** 
     * Creates new form Rate_Code_LB 
     */
    public Rate_Code_LB(String cpny) {
        parent     = Main_Window.parentOfApplication;

        initComponents();
        
        Connection myConn = new Connection();        
        myConn.setCompany(cpny);
        rate_code_list_query rclq = new rate_code_list_query();

        myRs = new Record_Set();        

        try{
            myRs = myConn.executeQuery(rclq);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        updateList(myRs);
        lbRateCode.addActionListener(new Rate_Code_LB.RateCodeActionListener(this));
    }
    
    public Record_Set getRecordSet(){return myRs;}
    
    public void updateList(Record_Set rs){
        rs.moveToFront();
        if(rs.length() > 0){
            int a = rs.length() * 2;
            
            RateCodeId   = new  Vector(a);
            RateCodeName = new  Vector(a);
            RateCodeHash = new HashMap(a);
            UskedHash    = new HashMap(a);
            
            rs.moveToFront();
            RateCodeId.add("0");
            RateCodeName.add("");
            RateCodeHash.put("0", "");
            UskedHash.put("0", "");
            lbRateCode.addItem("");
            
            for(int i=0;i<rs.length();i++){
                String id    = rs.getString("id");
                String name  = rs.getString("name");
                String usked = rs.getString("usked_id");
                
                RateCodeId.add(id);
                RateCodeName.add(name);
                RateCodeHash.put(id, name);
                UskedHash.put(id, usked);
                
                lbRateCode.addItem(name);
                
                rs.moveNext();
            }
        }
       
    }
    
    /**
     * Self explainatory...should really be moved to Main_Window eventually...
     */
    public String getRateCodeNameById(String id) {
        //return parent.getRateCodeNameById(id);
        return (String)RateCodeHash.get(id);
    }

    public String getUskedCodeNameById(String id) {
        //return parent.getRateCodeNameById(id);
        return (String)UskedHash.get(id);
    }
    
    public void setRateCode(String bId){
        int c, i;
        try {
            c = RateCodeId.size();
            for(i=0;i<c;i++){
                if(((String)RateCodeId.get(i)).equals(bId)){
                    lbRateCode.setSelectedItem((String)RateCodeName.get(i));
                    break;
                }
            }
        } catch (Exception e) {}
    }
    
    public void setDefault(){
        lbRateCode.setSelectedItem("Unarmed");
    }
    
    public int getLength(){
        return RateCodeId.size();
    }
    
    public String getRateCodeName(int i) {
        return (String)RateCodeName.get(i);
    }
    
    public String getRateCodeId(int i) {
        return (String)RateCodeId.get(i);
    }
    
    public void addActionListener(ActionListener al){
        myActionListener = al;
    }
    
    public int getSelectedIndex(){
        return lbRateCode.getSelectedIndex();
    }
    
    public String getSelectedItem(){
        return (String) lbRateCode.getSelectedItem();
    }
    
    public String getSelectedRateCodeId(){
        return (String)RateCodeId.get(lbRateCode.getSelectedIndex());
    }
    
    public String getSelectedRateCodeName(){
        return (String)RateCodeName.get(lbRateCode.getSelectedIndex());
    }
    
    class RateCodeActionListener implements ActionListener{
        
        private Rate_Code_LB myCb;
        
        public RateCodeActionListener(Rate_Code_LB c){
            myCb = c;
        }
        
        public void actionPerformed(ActionEvent e){
            if(myCb.myActionListener != null){
                myCb.myActionListener.actionPerformed(e);
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
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        TitleLabel = new javax.swing.JLabel();
        SpacerPanel = new javax.swing.JPanel();
        lbRateCode = new JComboBox();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 10)));
        setOpaque(false);
        jPanel3.setMaximumSize(new java.awt.Dimension(5000, 5000));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanel3.setOpaque(false);
        add(jPanel3);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32814, 22));
        jPanel1.setMinimumSize(new java.awt.Dimension(70, 22));
        jPanel1.setOpaque(false);
        TitleLabel.setText("Rate Code:");
        TitleLabel.setMaximumSize(new java.awt.Dimension(70, 14));
        TitleLabel.setMinimumSize(new java.awt.Dimension(70, 14));
        TitleLabel.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel1.add(TitleLabel);

        SpacerPanel.setMaximumSize(new java.awt.Dimension(10, 10));
        SpacerPanel.setOpaque(false);
        jPanel1.add(SpacerPanel);

        jPanel1.add(lbRateCode);

        add(jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(5000, 5000));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanel2.setOpaque(false);
        add(jPanel2);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel SpacerPanel;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox lbRateCode;
    // End of variables declaration//GEN-END:variables
    
}
