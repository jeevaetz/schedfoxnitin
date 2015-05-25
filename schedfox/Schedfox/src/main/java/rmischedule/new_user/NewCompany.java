/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.new_user;

/**
 *
 * @author user
 */
public class NewCompany {
    private String companyName;
    private String companyPhone;
    private String companyAddress;
    private String companyCity;
    private String companyState;
    private String companyZip;
    private String companyWebsite;

    public NewCompany() {
        companyName = new String();
        companyPhone = new String();
        companyAddress = new String();
        companyCity = new String();
        companyState = new String();
        companyZip = new String();
        companyWebsite = new String();
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyPhone
     */
    public String getCompanyPhone() {
        return companyPhone;
    }

    /**
     * @param companyPhone the companyPhone to set
     */
    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    /**
     * @return the companyAddress
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * @param companyAddress the companyAddress to set
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * @return the companyCity
     */
    public String getCompanyCity() {
        return companyCity;
    }

    /**
     * @param companyCity the companyCity to set
     */
    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    /**
     * @return the companyState
     */
    public String getCompanyState() {
        return companyState;
    }

    /**
     * @param companyState the companyState to set
     */
    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    /**
     * @return the companyZip
     */
    public String getCompanyZip() {
        return companyZip;
    }

    /**
     * @param companyZip the companyZip to set
     */
    public void setCompanyZip(String companyZip) {
        this.companyZip = companyZip;
    }

    /**
     * @return the companyWebsite
     */
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    /**
     * @param companyWebsite the companyWebsite to set
     */
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    /**
     * Returns the basic schema ready name based on the company name passed in.
     * @return
     */
    public String getBasicCompanySchemaName() {
        String myCompanyName = this.companyName.replaceAll(" ", "_");
        if (myCompanyName.length() > 15) {
            myCompanyName = myCompanyName.substring(0, 14);
        }
        return myCompanyName.toLowerCase();
    }
}
