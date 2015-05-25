/*
 * CertificationClass.java
 *
 * Created on October 13, 2005, 2:38 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.schedule.components;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author Ira Juneau
 * Why the public class to hold certifications? Because later on these bad boys are 
 * going to get complicated if we want to do this right, so might as well start off on
 * right foot right?
 */
public class CertificationClass {
    
    private String CertId;
    private String CertName;
    private String CertDesc;
    private String RenewTime;
    private String Aquired;
    private boolean iscertified;
    
    /** Creates a new instance of CertificationClass */
    public CertificationClass(String certId, String certName,  String certDescription, String RenewalTime, String Acquired, String Expired, boolean isCertified) {
        CertId = certId;
        CertName = certName;
        CertDesc = certDescription;
        RenewTime = RenewalTime;
        Aquired = Acquired;
        iscertified = isCertified;
    }

    public CertificationClass(Record_Set rst) {
        CertId = rst.getString("certid");
        CertName = rst.getString("certname");
        CertDesc = rst.getString("certdesc");
        RenewTime = rst.getString("renewal");
        try {
            Aquired = rst.getString("acquired");
        } catch (Exception e) {}
        try {
            iscertified = Boolean.parseBoolean(rst.getString("iscert"));
        } catch (Exception e) {
            iscertified = false;
        }
    }
    
    public String getId() {
        return CertId;
    }
    
    public String getName() {
        return CertName;
    }
    
}
