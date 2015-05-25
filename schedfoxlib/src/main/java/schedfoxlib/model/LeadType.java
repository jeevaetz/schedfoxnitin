/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ira
 */
public class LeadType {

    private Integer id;
    private String value;

    public LeadType() {
    }

    public LeadType(ResultSet rst) throws SQLException {
        id = rst.getInt("id");
        value = rst.getString("value");
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
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
