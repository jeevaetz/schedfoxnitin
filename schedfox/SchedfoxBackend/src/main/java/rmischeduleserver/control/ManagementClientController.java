/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Company;
import schedfoxlib.model.ManagementClient;
import schedfoxlib.model.ManagementClientInvoice;
import schedfoxlib.model.User;
import rmischeduleserver.mysqlconnectivity.queries.management.check_if_billing_exists_query;
import rmischeduleserver.mysqlconnectivity.queries.management.generate_invoice_query;
import rmischeduleserver.mysqlconnectivity.queries.management.get_clients_with_invoices_query;
import rmischeduleserver.mysqlconnectivity.queries.management.get_companies_for_management_query;
import rmischeduleserver.mysqlconnectivity.queries.management.get_invoicing_for_client_query;
import rmischeduleserver.mysqlconnectivity.queries.management.get_management_client_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.management.remove_old_invoices_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_count_for_stats_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_employee_count_for_invoicing_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_schedule_last_modified_query;
import rmischeduleserver.mysqlconnectivity.queries.util.save_management_info_query;

/**
 *
 * @author user
 */
public class ManagementClientController {

    private String companyId;

    private ManagementClientController(String companyId) {
        this.companyId = companyId;
    }

    public static ManagementClientController getInstance(String companyId) {
        return new ManagementClientController(companyId);
    }

    public void runInvoicing(ArrayList<Integer> managementClients) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        for (int m = 0; m < managementClients.size(); m++) {
            try {
                generate_invoice_query invoiceQuery = new generate_invoice_query();
                Integer id = managementClients.get(m);
                invoiceQuery.setPreparedStatement(new Object[]{id, id, id, id, id, id, id, id, id});
                conn.executeUpdate(invoiceQuery, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ManagementClient> getManagementClientsWithInvoicing() {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ManagementClient> retVal = new ArrayList<ManagementClient>();

        get_clients_with_invoices_query invoicesQuery = new get_clients_with_invoices_query();
        invoicesQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(invoicesQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ManagementClient(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public ArrayList<Company> getCompaniesForManagementClient(int managementId) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_companies_for_management_query myQuery = new get_companies_for_management_query();
        myQuery.setPreparedStatement(new Object[]{managementId});
        ArrayList<Company> retVal = new ArrayList<Company>();
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Company(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int getActiveEmployeeCount(ArrayList<Company> companies) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        int employeeCount = 0;
        for (int c = 0; c < companies.size(); c++) {
            get_employee_count_for_invoicing_query myQuery = new get_employee_count_for_invoicing_query();
            myQuery.update(companies.get(c).getDB());
            myQuery.setPreparedStatement(new Object[]{});
            try {
                Record_Set rst = conn.executeQuery(myQuery, "");
                for (int r = 0; r < rst.length(); r++) {
                    employeeCount += rst.getInt(0);
                    rst.moveNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return employeeCount;
    }

    public int getActiveClientCount(ArrayList<Company> companies) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        int employeeCount = 0;
        for (int c = 0; c < companies.size(); c++) {
            get_client_count_for_stats_query myQuery = new get_client_count_for_stats_query();
            myQuery.update(companies.get(c).getDB());
            myQuery.setPreparedStatement(new Object[]{});
            try {
                Record_Set rst = conn.executeQuery(myQuery, "");
                for (int r = 0; r < rst.length(); r++) {
                    employeeCount += rst.getInt(0);
                    rst.moveNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return employeeCount;
    }

    public HashMap<Date, User> getDateAndUserOfLastModified(ArrayList<Company> companies) {
        HashMap<Date, User> retVal = new HashMap<Date, User>();
        ArrayList<Date> myDates = new ArrayList<Date>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_schedule_last_modified_query clientQuery = new get_schedule_last_modified_query();
        clientQuery.setPreparedStatement(new Object[]{});

        for (int c = 0; c < companies.size(); c++) {
            clientQuery.update(companies.get(c).getDB());

            try {
                Record_Set rst = conn.executeQuery(clientQuery, "");
                for (int r = 0; r < rst.length(); r++) {

                    retVal.put(rst.getTimestamp(0), new User(new Date(), rst));
                    myDates.add(rst.getTimestamp(0));
                    rst.moveNext();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Collections.sort(myDates);

            Date maxDate = myDates.get(myDates.size() - 1);

            User user = retVal.get(maxDate);
            retVal = new HashMap<Date, User>();
            retVal.put(maxDate, user);
        } catch (Exception exe) {
        }

        return retVal;
    }

    public ArrayList<ManagementClientInvoice> getClientInvoices(int managementId) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ManagementClientInvoice> retVal = new ArrayList<ManagementClientInvoice>();
        get_invoicing_for_client_query clientQuery = new get_invoicing_for_client_query();
        clientQuery.setPreparedStatement(new Object[]{managementId});

        try {
            Record_Set rst = conn.executeQuery(clientQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ManagementClientInvoice(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public ArrayList<ManagementClient> getAllManagementClients() {
        ArrayList<ManagementClient> retVal = new ArrayList<ManagementClient>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();


        return retVal;
    }

    public int doesBillingAlreadyExist(Date startDate, int managementId, BigDecimal amount, BigDecimal amountPerEmp) throws RetrieveDataException {
        int retVal = 0;
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        check_if_billing_exists_query myQuery = new check_if_billing_exists_query();
        myQuery.setPreparedStatement(new Object[]{managementId, startDate, amount, amountPerEmp});
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt(0);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void removeExistingBilling(Date startDate, int managementId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        remove_old_invoices_query myQuery = new remove_old_invoices_query();
        myQuery.setPreparedStatement(new Object[]{managementId, startDate});
        try {
            conn.executeUpdate(myQuery, "");

        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    public ManagementClient getManagementClientById(int managementId) throws RetrieveDataException {
        ManagementClient retVal = new ManagementClient();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_management_client_by_id_query myQuery = new get_management_client_by_id_query();
        myQuery.setPreparedStatement(new Object[]{managementId});
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ManagementClient(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public void saveManagementClient(ManagementClient client) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_management_info_query mySaveQuery = new save_management_info_query();
        mySaveQuery.update(client);
        try {
            conn.executeUpdate(mySaveQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

    }
}
