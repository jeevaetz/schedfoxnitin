/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.SalesExpense;

/**
 *
 * @author ira
 */
public class save_sales_expense_query extends GeneralQueryFormat {

    private boolean isInsert = false;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(SalesExpense expense, Boolean isInsert) {
        java.sql.Timestamp approvedOn = null;
        this.isInsert = isInsert;
        if (expense.getExpApprovedOn() != null) {
            approvedOn = new java.sql.Timestamp(expense.getExpApprovedOn().getTime());
        }
        super.setPreparedStatement(new Object[]{
                    expense.getUserId(), expense.getExpenseAmount(), expense.getUserApprovedBy(),
                    expense.getDateOfExpense(), expense.getNotes(), expense.getSalesExpenseTypeId(),
                    expense.getExpApprovedBy(), approvedOn, expense.getActive(),
                    expense.getSalesExpenseId()
                });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("sales_expense ");
            sql.append("(user_id, expense_amount, user_approved_by, date_of_expense, notes, sales_expense_type_id, exp_approved_by, exp_approved_on, active, sales_expense_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        } else {
            sql.append("UPDATE ");
            sql.append("sales_expense ");
            sql.append("SET ");
            sql.append("user_id = ?, expense_amount = ?, user_approved_by = ?, date_of_expense = ?, notes = ?, sales_expense_type_id = ?, exp_approved_by = ?, exp_approved_on = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("sales_expense_id = ? ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
