/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class EmployeeTest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer employeeTestId;
    private String employeeTest;
    private String testSettingsUrl;
    private String testEditUrl;
    private String testDeleteUrl;
    private String testUrl;
    private double testScore;
    private String externalId;
    private boolean default_to_na;

    public EmployeeTest() {
    }

    public EmployeeTest(Record_Set rst) {
        this.employeeTestId = rst.getInt("employee_test_id");
        this.employeeTest = rst.getString("employee_test");
        this.externalId = rst.getString("external_id");
        this.testDeleteUrl = rst.getString("test_delete_url");
        this.testEditUrl = rst.getString("test_edit_url");
        this.testSettingsUrl = rst.getString("test_settings_url");
        this.testUrl = rst.getString("test_url");
        this.default_to_na = rst.getBoolean("default_to_na");
        try {
            if(rst.hasColumn("score")) {
                this.testScore = Double.parseDouble(rst.getString("score"));
            }
        } catch (Exception exe) {}
    }

    public EmployeeTest(Integer employeeTestId) {
        this.employeeTestId = employeeTestId;
    }

    public EmployeeTest(Integer employeeTestId, String employeeTest) {
        this.employeeTestId = employeeTestId;
        this.employeeTest = employeeTest;
    }

    public Integer getEmployeeTestId() {
        return employeeTestId;
    }

    public double getEmployeeTestScore(){
        return testScore;
    }
    public void setEmployeeTestId(Integer employeeTestId) {
        this.employeeTestId = employeeTestId;
    }

    public String getEmployeeTest() {
        return employeeTest;
    }

    public String getEmployeeTestDisplay() {
        return employeeTest.replaceAll("\\[(.*?)\\]", "");
    }

    public void setEmployeeTest(String employeeTest) {
        this.employeeTest = employeeTest;
    }

    public String getTestSettingsUrl() {
        return testSettingsUrl;
    }

    public void setTestSettingsUrl(String testSettingsUrl) {
        this.testSettingsUrl = testSettingsUrl;
    }

    public String getTestEditUrl() {
        return testEditUrl;
    }

    public void setTestEditUrl(String testEditUrl) {
        this.testEditUrl = testEditUrl;
    }

    public String getTestDeleteUrl() {
        return testDeleteUrl;
    }

    public void setTestDeleteUrl(String testDeleteUrl) {
        this.testDeleteUrl = testDeleteUrl;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setScore(double score){
        this.testScore=score;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeTestId != null ? employeeTestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeTest)) {
            return false;
        }
        EmployeeTest other = (EmployeeTest) object;
        if ((this.employeeTestId == null && other.employeeTestId != null) || (this.employeeTestId != null && !this.employeeTestId.equals(other.employeeTestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedfox.parsetesting.dao.EmployeeTest[employeeTestId=" + employeeTestId + "]";
    }

    /**
     * @return the default_to_na
     */
    public boolean isDefault_to_na() {
        return default_to_na;
    }

    /**
     * @param default_to_na the default_to_na to set
     */
    public void setDefault_to_na(boolean default_to_na) {
        this.default_to_na = default_to_na;
    }

}
