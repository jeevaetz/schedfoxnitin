/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.xadmin.model;

/**
 *
 * @author user
 */
public class DynamicFieldLocation {
    private int id;
    private String value;

    public DynamicFieldLocation() {
        id = 0;
        value = "";
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
