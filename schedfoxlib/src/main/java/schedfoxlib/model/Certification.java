/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class Certification {
    private int cert_id;
    private String name;
    private String description;
    private String certification_default_renewal_time;

    public Certification() {
        
    }

    public Certification(Record_Set rs) {
        if (rs.getInt("cid") > -1) {
            this.cert_id = rs.getInt("cid");
        } else {
            this.cert_id = rs.getInt("certification_id");
        }
        if (rs.getString("name") != "") {
            this.name = rs.getString("name");
        } else {
            this.name = rs.getString("certification_name");
        }
        if (rs.getString("description") != "") {
            this.description = rs.getString("description");
        } else {
            this.description = rs.getString("certification_description");
        }
        try {
            if (rs.getString("renewal") != "") {
                this.certification_default_renewal_time = rs.getString("renewal");
            } else {
                this.certification_default_renewal_time = rs.getString("certification_default_renewal_time");
            }
        } catch (Exception e) {
            this.certification_default_renewal_time = new String();
        }
    }

    /**
     * @return the cert_id
     */
    public int getCert_id() {
        return cert_id;
    }

    /**
     * @param cert_id the cert_id to set
     */
    public void setCert_id(int cert_id) {
        this.cert_id = cert_id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the certification_default_renewal_time
     */
    public String getCertification_default_renewal_time() {
        return certification_default_renewal_time;
    }

    /**
     * @param certification_default_renewal_time the certification_default_renewal_time to set
     */
    public void setCertification_default_renewal_time(String certification_default_renewal_time) {
        this.certification_default_renewal_time = certification_default_renewal_time;
    }
}
