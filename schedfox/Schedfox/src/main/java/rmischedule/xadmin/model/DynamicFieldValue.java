/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.xadmin.model;

import rmischeduleserver.control.model.DynamicFieldDef;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class DynamicFieldValue {

    private int dynamic_field_value_id;
    private String dynamic_field_value;
    private int key_value;
    private int dynamic_field_def_id;
    private int last_user_changed;
    private Date last_updated;
    private DynamicFieldDef fieldDefObj;

    public DynamicFieldValue(Record_Set rs, int key) {
        this.dynamic_field_value = "";
        this.fieldDefObj = new DynamicFieldDef();

        this.setDynamic_field_def_id(rs.getInt("dynamic_field_def_id"));
        this.setDynamic_field_value(rs.getString("dynamic_field_value"));
        this.setDynamic_field_value_id(rs.getInt("dynamic_field_value_id"));
        //fieldValue.setLast_updated(rs.get)
        try {
            this.setKey_value(key);
        } catch (Exception e) {
            this.setKey_value(0);
        }
        this.setLast_user_changed(rs.getInt("last_user_changed"));

        DynamicFieldDef fieldDef = new DynamicFieldDef();
        fieldDef.setName(rs.getString("dynamic_field_def_name"));
        fieldDef.setFieldTypeId(rs.getInt("dynamic_field_type_id"));
        try {
            fieldDef.setIsRequired(rs.getBoolean("is_required"));
        } catch (Exception e) {
            System.out.println("Caught error @: " + e);
            fieldDef.setIsRequired(false);
        }
        fieldDef.setDynamicFieldDefDefault(rs.getString("dynamic_field_def_default"));
        this.setFieldDefObj(fieldDef);
    }

    /**
     * @return the dynamic_field_value_id
     */
    public int getDynamic_field_value_id() {
        return dynamic_field_value_id;
    }

    /**
     * @param dynamic_field_value_id the dynamic_field_value_id to set
     */
    public void setDynamic_field_value_id(int dynamic_field_value_id) {
        this.dynamic_field_value_id = dynamic_field_value_id;
    }

    /**
     * @return the dynamic_field_value
     */
    public String getDynamic_field_value() {
        return dynamic_field_value;
    }

    /**
     * @param dynamic_field_value the dynamic_field_value to set
     */
    public void setDynamic_field_value(String dynamic_field_value) {
        this.dynamic_field_value = dynamic_field_value;
    }

    /**
     * @return the dynamic_field_def_id
     */
    public int getDynamic_field_def_id() {
        return dynamic_field_def_id;
    }

    /**
     * @param dynamic_field_def_id the dynamic_field_def_id to set
     */
    public void setDynamic_field_def_id(int dynamic_field_def_id) {
        this.dynamic_field_def_id = dynamic_field_def_id;
    }

    /**
     * @return the last_user_changed
     */
    public int getLast_user_changed() {
        return last_user_changed;
    }

    /**
     * @param last_user_changed the last_user_changed to set
     */
    public void setLast_user_changed(int last_user_changed) {
        this.last_user_changed = last_user_changed;
    }

    /**
     * @return the last_updated
     */
    public Date getLast_updated() {
        return last_updated;
    }

    /**
     * @param last_updated the last_updated to set
     */
    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    /**
     * @return the fieldDefObj
     */
    public DynamicFieldDef getFieldDefObj() {
        return fieldDefObj;
    }

    /**
     * @param fieldDefObj the fieldDefObj to set
     */
    public void setFieldDefObj(DynamicFieldDef fieldDefObj) {
        this.fieldDefObj = fieldDefObj;
    }

    /**
     * @return the key_value
     */
    public int getKey_value() {
        return key_value;
    }

    /**
     * @param key_value the key_value to set
     */
    public void setKey_value(int key_value) {
        this.key_value = key_value;
    }
}
