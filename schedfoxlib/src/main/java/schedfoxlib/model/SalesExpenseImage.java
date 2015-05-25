/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesExpenseImage implements java.io.Serializable {
    private Integer salesExpenseImagesId;
    private Integer salesExpenseId;
    private byte[] image;
    
    
    public SalesExpenseImage() {

    }

    public SalesExpenseImage(Record_Set rst) {
        try {
            this.salesExpenseId = rst.getInt("sales_expense_id");
        } catch (Exception exe) {}
        try {
            this.salesExpenseImagesId = rst.getInt("sales_expense_images_id");
        } catch (Exception exe) {}
        try {
            this.image = rst.getByteArray("image");
        } catch (Exception exe) {}
    }

    /**
     * @return the salesExpenseImagesId
     */
    public Integer getSalesExpenseImagesId() {
        return salesExpenseImagesId;
    }

    /**
     * @param salesExpenseImagesId the salesExpenseImagesId to set
     */
    public void setSalesExpenseImagesId(Integer salesExpenseImagesId) {
        this.salesExpenseImagesId = salesExpenseImagesId;
    }

    /**
     * @return the salesExpenseId
     */
    public Integer getSalesExpenseId() {
        return salesExpenseId;
    }

    /**
     * @param salesExpenseId the salesExpenseId to set
     */
    public void setSalesExpenseId(Integer salesExpenseId) {
        this.salesExpenseId = salesExpenseId;
    }

    /**
     * @return the image
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(byte[] image) {
        this.image = image;
    }
    
    
}
