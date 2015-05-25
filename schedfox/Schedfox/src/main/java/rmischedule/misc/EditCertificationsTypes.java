/*
 * EditCertificationsTypes.java
 *
 * Created on October 12, 2005, 8:31 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.misc;

import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.*;
import rmischedule.components.graphicalcomponents.GenericEditForm;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
/**
 *
 * @author Ira Juneau
 */
public class EditCertificationsTypes extends GenericEditForm {
    
    private String activeComp;
    private int width = 630;
    private int height = 350;
    
    /** Creates a new instance of EditCertificationsTypes */
    public EditCertificationsTypes(String comp) {
        super();
        setCompany(comp);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-width)/2, (screenSize.height-height)/2, width, height);
        super.addSubForm(new CertInfoPanel());
    }
    
    public void setCompany(String comp) {
        activeComp = comp;
        getData();
    }
    
    public void deleteData() {
        Certification cert = (Certification)this.getSelectedObject();
        if(cert != null) {
            delete_certification_query deleteQuery = new delete_certification_query(cert.certId);
            try {
                getConnection().prepQuery(deleteQuery);
                getConnection().executeUpdate(deleteQuery);
                this.clearData();
                this.getData();
            } catch(Exception ex) { }
        }
    }
    
    public String getWindowTitle() {
        return "Edit Certifications";
    }
    
    public Connection getConnection() {
        Connection myConn = new Connection();
        myConn.setCompany(activeComp);
        return myConn;
    }
    
    public void getData() {
        if(activeComp != null) {
            get_certifications_query myListQuery = new get_certifications_query();
            Record_Set rs = new Record_Set();
            Connection con = getConnection();
            con.prepQuery(myListQuery);
            try {
                rs = con.executeQuery(myListQuery);
            } catch (Exception e) {}
            super.populateList(rs);
        }
    }

    public void addMyMenu(javax.swing.JMenuBar myMenu) {
        
    }
    
    public Object createObjectForList(Record_Set rs) {
        return new Certification(rs);
    }
    
    public String getDisplayNameForObject(Object myObj) {
        return ((Certification)myObj).certName;
    }
    
    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "(SELECT (CASE WHEN MAX(certifications.certification_id) IS NULL " +
                    "THEN 1 ELSE (MAX(certifications.certification_id) + 1) END) FROM certifications)";
        }
        return ((Certification)currentSelectedObject).certId;
    }
    
    private class Certification {
        public String certId;
        public String certName;
        public String certDesc;
        public String certRenewal;
        
        public Certification(String cid, String name, String desc, String renewalTime) {
            certId = cid;
            certName = name;
        }
        
        public Certification(Record_Set rs) {
            certId = rs.getString("certid");
            certName = rs.getString("certname");
            certDesc = rs.getString("certdesc");
            certRenewal = rs.getString("certrenewal");
        }
    }
    
}
