/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.xadmin.model;

/**
 *
 * @author user
 */
public class DynamicFieldType {
    private int id;
    private String value;

    public DynamicFieldType() {
        this.value = "";
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
