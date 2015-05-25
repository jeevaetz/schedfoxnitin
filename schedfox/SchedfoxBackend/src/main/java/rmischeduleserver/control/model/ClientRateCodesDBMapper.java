/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.model.DBMapper;
import rmischeduleserver.control.model.DataMappingClass;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_client_export_rate_codes_query;

/**
 *
 * @author user
 */
public class ClientRateCodesDBMapper extends DBMapper {

    public ClientRateCodesDBMapper(RMIScheduleServerImpl myConn, String companyId, String branchId) {
        super(myConn, companyId, branchId);
    }

    public ClientRateCodesDBMapper(ArrayList<String> columnHeaders, ArrayList<ArrayList<String>> excelData, RMIScheduleServerImpl myConn, String branchId) {
        super(columnHeaders, excelData, myConn, branchId);
    }

    @Override
    public GeneralQueryFormat generateQuery(Object[] params) {
        get_client_export_rate_codes_query rateCodesQuery = new get_client_export_rate_codes_query();
        rateCodesQuery.setPreparedStatement(params);
        return rateCodesQuery;
    }

    @Override
    public LinkedHashMap<DataMappingClass, Integer> getMappings() {
        return new LinkedHashMap<DataMappingClass, Integer>();
    }

    @Override
    public LinkedHashMap<DataMappingClass, Integer> getUskedMappings() {
        LinkedHashMap<DataMappingClass, Integer> retVal = new LinkedHashMap<DataMappingClass, Integer>();
        helperUskedMapMethod(retVal, "usked_client", "usked_cli_id", "Customer Id");
        helperUskedMapMethod(retVal, "", "", "Site Code");
        helperUskedMapMethod(retVal, "", "hour_type", "Hour Type");
        helperUskedMapMethod(retVal, "rate_code", "usked_rate_code", "Rate Code");
        helperUskedMapMethod(retVal, "rate_code", "rate_code_name", "Rate Code Description");
        helperUskedMapMethod(retVal, "client_rate_code", "pay_amount", "Regular Pay Rate");
        helperUskedMapMethod(retVal, "client_rate_code", "overtime_amount", "Overtime Pay Rate");
        helperUskedMapMethod(retVal, "client_rate_code", "", "Double Time Pay Rate");
        helperUskedMapMethod(retVal, "client_rate_code", "bill_amount", "Regular Bill Rate");
        helperUskedMapMethod(retVal, "client_rate_code", "overtime_bill", "Overtime Bill Rate");
        helperUskedMapMethod(retVal, "client_rate_code", "", "Double Time Bill Rate");
        helperUskedMapMethod(retVal, "", "is_holliday_bill", "Holiday Billing");
        helperUskedMapMethod(retVal, "", "is_holliday_pay", "Holiday Pay");
        helperUskedMapMethod(retVal, "client_rate_code", "", "Overtime Billing");
        return retVal;
    }

    @Override
    public void insertValuesIntoDB(LinkedHashMap<DataMappingClass, Integer> mappedColumns) {
        
    }

}
