/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control.model;

/**
 *
 * @author user
 */
public class DynamicFieldDef {
    private int id;
    private String name;
    private int fieldTypeId;
    private int fieldLocationId;
    private String dynamicFieldDefDefault;
    private boolean isRequired;
    private boolean isActive;
    private boolean showInClientLogin;
    private boolean showInEmployeeLogin;

    public DynamicFieldDef() {
        name = "";
        dynamicFieldDefDefault = "";
        isRequired = false;
        isActive = true;
        showInClientLogin = false;
        showInEmployeeLogin = false;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the fieldTypeId
     */
    public int getFieldTypeId() {
        return fieldTypeId;
    }

    /**
     * @param fieldTypeId the fieldTypeId to set
     */
    public void setFieldTypeId(int fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    /**
     * @return the fieldLocationId
     */
    public int getFieldLocationId() {
        return fieldLocationId;
    }

    /**
     * @param fieldLocationId the fieldLocationId to set
     */
    public void setFieldLocationId(int fieldLocationId) {
        this.fieldLocationId = fieldLocationId;
    }

    /**
     * @return the isRequired
     */
    public boolean isIsRequired() {
        return isRequired;
    }

    /**
     * @param isRequired the isRequired to set
     */
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    /**
     * @return the dynamicFieldDefDefault
     */
    public String getDynamicFieldDefDefault() {
        return dynamicFieldDefDefault;
    }

    /**
     * @param dynamicFieldDefDefault the dynamicFieldDefDefault to set
     */
    public void setDynamicFieldDefDefault(String dynamicFieldDefDefault) {
        this.dynamicFieldDefDefault = dynamicFieldDefDefault;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the showInClientLogin
     */
    public boolean isShowInClientLogin() {
        return showInClientLogin;
    }

    /**
     * @param showInClientLogin the showInClientLogin to set
     */
    public void setShowInClientLogin(boolean showInClientLogin) {
        this.showInClientLogin = showInClientLogin;
    }

    /**
     * @return the showInEmployeeLogin
     */
    public boolean isShowInEmployeeLogin() {
        return showInEmployeeLogin;
    }

    /**
     * @param showInEmployeeLogin the showInEmployeeLogin to set
     */
    public void setShowInEmployeeLogin(boolean showInEmployeeLogin) {
        this.showInEmployeeLogin = showInEmployeeLogin;
    }
}
