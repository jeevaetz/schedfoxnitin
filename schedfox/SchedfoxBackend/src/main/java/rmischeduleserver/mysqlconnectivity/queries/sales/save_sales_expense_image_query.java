/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.SalesExpenseImage;

/**
 *
 * @author ira
 */
public class save_sales_expense_image_query extends GeneralQueryFormat {

    private boolean update;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(SalesExpenseImage image) {
        if (image.getSalesExpenseImagesId() == null || image.getSalesExpenseImagesId() == 0) {
            update = false;
            super.setPreparedStatement(new Object[]{image.getSalesExpenseId(), image.getImage()});
        } else {
            update = true;
            super.setPreparedStatement(new Object[]{image.getSalesExpenseId(), image.getImage(), image.getSalesExpenseImagesId()});
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!update) {
            sql.append("INSERT INTO ");
            sql.append("sales_expense_images ");
            sql.append("(sales_expense_id, image) ");
            sql.append("VALUES ");
            sql.append("(?, ?); ");
        } else {
            sql.append("UPDATE ");
            sql.append("sales_expense_images ");
            sql.append("SET ");
            sql.append("sales_expense_id = ?, image = ? ");
            sql.append("WHERE ");
            sql.append("sales_expense_images_id = ?; ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
