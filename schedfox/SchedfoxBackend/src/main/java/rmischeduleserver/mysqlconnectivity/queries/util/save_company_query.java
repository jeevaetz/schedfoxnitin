/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.CompanyObj;

/**
 *
 * @author ira
 */
public class save_company_query extends GeneralQueryFormat {

    private boolean isUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(CompanyObj companyObj, boolean isUpdate) {
        this.isUpdate = isUpdate;
        super.setPreparedStatement(new Object[]{
                    companyObj.getCompanyName(), companyObj.getCompanyDb(),
                    companyObj.getCompanyManagementId(), companyObj.getCompanyStatus(),
                    companyObj.getStatusDescription(), companyObj.getEmployeeLoginPrefix(),
                    companyObj.getCompanyUrl(), companyObj.getCompanyId()
                });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE ");
            sql.append(super.getManagementSchema()).append(".company ");
            sql.append("SET ");
            sql.append("company_name = ?, company_db = ?, company_management_id = ?, ");
            sql.append("company_status = ?, status_description = ?, status_modifieddate = NOW(), ");
            sql.append("employee_login_prefix = ?, company_url = ? ");
            sql.append("WHERE ");
            sql.append("company_id = ?; ");
        } else {
            sql.append("INSERT INTO ").append(super.getManagementSchema()).append(".company ");
            sql.append("(company_name, company_db, company_management_id, company_status, status_description, status_modifieddate, employee_login_prefix, company_url, company_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, NOW(), ?, ?, ?); ");

        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
