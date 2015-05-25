/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.controller.CompanyControllerInterface;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class CompanyInformationObj {

    private int company_view_options_id;
    private int company_id;
    private int company_view_id;
    private String option_key;
    private String option_value;
    //Lazy object
    private CompanyView companyViewObj;

    public CompanyInformationObj() {
        this.option_value = new String();
    }

    public CompanyInformationObj(Record_Set rs) {
        //this.company_id = rs.getInt("company_id");
        this.company_view_id = rs.getInt("company_view_id");
        this.company_view_options_id = rs.getInt("company_view_options_id");
        this.option_value = rs.getString("myvalue");
        this.option_key = rs.getString("company_view_key");
    }

    /**
     * @return the company_view_options_id
     */
    public int getCompany_view_options_id() {
        return company_view_options_id;
    }

    /**
     * @param company_view_options_id the company_view_options_id to set
     */
    public void setCompany_view_options_id(int company_view_options_id) {
        this.company_view_options_id = company_view_options_id;
    }

    /**
     * @return the company_id
     */
    public int getCompany_id() {
        return company_id;
    }

    /**
     * @param company_id the company_id to set
     */
    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    /**
     * @return the company_view_id
     */
    public int getCompany_view_id() {
        return company_view_id;
    }

    /**
     * @param company_view_id the company_view_id to set
     */
    public void setCompany_view_id(int company_view_id) {
        this.company_view_id = company_view_id;
    }

    /**
     * @return the option_value
     */
    public String getOption_value() {
        return option_value;
    }

    /**
     * @param option_value the option_value to set
     */
    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }

    /**
     * @return the option_key
     */
    public String getOption_key() {
        return option_key;
    }

    /**
     * @param option_key the option_key to set
     */
    public void setOption_key(String option_key) {
        this.option_key = option_key;
    }

    public CompanyView getCompanyViewObj(String companyId) {
        if (this.companyViewObj == null) {
            try {
                CompanyControllerInterface companyInterface = ControllerRegistryAbstract.getCompanyController(companyId);
                this.companyViewObj = companyInterface.getCompanyView(company_view_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.companyViewObj;
    }
}
