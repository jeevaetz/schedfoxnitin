/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesExpenseType implements Serializable {
    private Integer salesExpenseTypeId;
    private String salesExpenseType;
    
    public SalesExpenseType() {
        
    }
    
    public SalesExpenseType(Record_Set rst) {
        try {
            salesExpenseTypeId = rst.getInt("sales_expense_type_id");
        } catch (Exception exe) {}
        try {
            salesExpenseType = rst.getString("sales_expense_type");
        } catch (Exception exe) {}
    }

    /**
     * @return the salesExpenseTypeId
     */
    public Integer getSalesExpenseTypeId() {
        return salesExpenseTypeId;
    }

    /**
     * @param salesExpenseTypeId the salesExpenseTypeId to set
     */
    public void setSalesExpenseTypeId(Integer salesExpenseTypeId) {
        this.salesExpenseTypeId = salesExpenseTypeId;
    }

    /**
     * @return the salesExpenseType
     */
    public String getSalesExpenseType() {
        return salesExpenseType;
    }

    /**
     * @param salesExpenseType the salesExpenseType to set
     */
    public void setSalesExpenseType(String salesExpenseType) {
        this.salesExpenseType = salesExpenseType;
    }
    
    @Override
    public String toString() {
        return salesExpenseType;
    }
}
