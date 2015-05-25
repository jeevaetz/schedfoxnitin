/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.Client;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.client.FindClientByNameAndPhoneNumberQuery;
import rmischeduleserver.mysqlconnectivity.queries.client.LoadAllClientsQuery;
import rmischeduleserver.mysqlconnectivity.queries.client.SaveClientObjectQuery;

/**
 *
 * @author user
 */
public class ClientDBMapper extends DBMapper {

    private ArrayList<Client> insertedClients;
    private ArrayList<Client> updatedClients;
    private ArrayList<Client> errorClients;

    public ClientDBMapper(RMIScheduleServerImpl myConn, String companyId, String branchId) {
        super(myConn, companyId, branchId);
    }

    public ClientDBMapper(ArrayList<String> columnHeaders, ArrayList<ArrayList<String>> excelData, RMIScheduleServerImpl myConn, String branchId) {
        super(columnHeaders, excelData, myConn, branchId);
    }

    public ArrayList<Client> getErrorClients() {
        return this.errorClients;
    }

    public ArrayList<Client> getInsertedClients() {
        return this.insertedClients;
    }

    public ArrayList<Client> getUpdatedClients() {
        return this.updatedClients;
    }

    @Override
    public GeneralQueryFormat generateQuery(Object[] params) {
        LoadAllClientsQuery rateCodesQuery = new LoadAllClientsQuery();
        rateCodesQuery.setPreparedStatement(params);
        return rateCodesQuery;
    }
    
    @Override
    public LinkedHashMap<DataMappingClass, Integer> getUskedMappings() {
        LinkedHashMap<DataMappingClass, Integer> retVal = new LinkedHashMap<DataMappingClass, Integer>();

        helperUskedMapMethod(retVal, "usked_client", "usked_cli_id", "Customer Id");

        UskedActiveColumn tClientStatus = new UskedActiveColumn("client", "client_is_deleted");
        DataMappingClass clientStatus = new DataMappingClass(tClientStatus, "Is Active");
        clientStatus.setUskedId("Status");
        retVal.put(clientStatus, -1);

        TableMapClass tClientName = new TableMapClass("client", "client_name");
        DataMappingClass clientName = new DataMappingClass(tClientName, "Client Name", "Company Name", "CompanyName", "ClientName", "Location", "Name");
        clientName.setUskedId("Customer Name");
        retVal.put(clientName, -1);

        TableMapClass tClientAddress = new TableMapClass("client", "client_address");
        DataMappingClass clientAddress = new DataMappingClass(tClientAddress, "Address Line 1", "Address1", "Address");
        clientAddress.setUskedId("Address 1");
        retVal.put(clientAddress, -1);

        TableMapClass tClientAddress2 = new TableMapClass("client", "client_address2");
        DataMappingClass clientAddress2 = new DataMappingClass(tClientAddress2, "Address Line 2", "Address2", "Line2");
        clientAddress2.setUskedId("Address 2");
        retVal.put(clientAddress2, -1);

        TableMapClass tClientCity = new TableMapClass("client", "client_city");
        DataMappingClass clientCity = new DataMappingClass(tClientCity, "City");
        clientCity.setUskedId("City");
        retVal.put(clientCity, -1);

        TableMapClass tClientState = new UskedStateColumn("client", "client_state");
        DataMappingClass clientState = new DataMappingClass(tClientState, "State");
        clientState.setUskedId("State");
        retVal.put(clientState, -1);

        TableMapClass tClientZip = new TableMapClass("client", "client_zip");
        DataMappingClass clientZip = new DataMappingClass(tClientZip, "Zip", "Zip Code", "ZipCode");
        clientZip.setUskedId("Zip");
        retVal.put(clientZip, -1);

        TableMapClass tClientCountry = new TableMapClass("client", "");
        DataMappingClass clientCountry = new DataMappingClass(tClientCountry, "Country");
        clientCountry.setUskedId("Country");
        clientCountry.setShouldDisplayInManualExport(false);
        retVal.put(clientCountry, -1);

        TableMapClass tClientAddedOn = new UskedDateColumn("client", "client_last_updated");
        DataMappingClass clientAddedOn = new DataMappingClass(tClientAddedOn, "Added On");
        clientAddedOn.setUskedId("Added On");
        clientAddedOn.setShouldDisplayInManualExport(false);
        retVal.put(clientAddedOn, -1);
        
        helperUskedMapMethod(retVal, "client", "", "Business Code");
        helperUskedMapMethod(retVal, "client", "", "Location Code");
        helperUskedMapMethod(retVal, "client", "", "Office Code");
        helperUskedMapMethod(retVal, "", "", "Salesman 1 Code");
        helperUskedMapMethod(retVal, "", "", "Salesman 2 Code");
        helperUskedMapMethod(retVal, "", "", "P/R State Code");
        helperUskedMapMethod(retVal, "", "", "P/R City Code");
        helperUskedMapMethod(retVal, "", "", "DST Adjustment");
        helperUskedMapMethod(retVal, "client", "client_name", "Bill To Name");
        helperUskedMapMethod(retVal, "client", "client_address", "Bill To Address 1");
        helperUskedMapMethod(retVal, "client", "client_address2", "Bill To Address 2");
        helperUskedMapMethod(retVal, "client", "client_city", "Bill To City");
        TableMapClass tBillToState = new UskedStateColumn("client", "client_state");
        DataMappingClass billToState = new DataMappingClass(tBillToState, "Bill To State");
        billToState.setUskedId("Bill To State");
        billToState.setShouldDisplayInManualExport(false);
        retVal.put(billToState, -1);
        helperUskedMapMethod(retVal, "client", "client_zip", "Bill To Zip");
        helperUskedMapMethod(retVal, "", "", "Bill To Country");
        TableMapClass tBillable = new UskedBillableColumn("", "");
        DataMappingClass billable = new DataMappingClass(tBillable, "Billable");
        billable.setUskedId("Billable");
        billable.setShouldDisplayInManualExport(false);
        retVal.put(billable, -1);
        helperUskedMapMethod(retVal, "", "", "Bill Frequency");
        helperUskedMapMethod(retVal, "", "", "Sales Tax Code");
        helperUskedMapMethod(retVal, "", "", "Invoice Batch Code");
        helperUskedMapMethod(retVal, "", "", "Terms Code");
        helperUskedMapMethod(retVal, "", "", "Late Charge Days");
        helperUskedMapMethod(retVal, "", "", "Late Charge %");
        helperUskedMapMethod(retVal, "", "", "Credit Limit");
        helperUskedMapMethod(retVal, "", "", "PO Number");
        helperUskedMapMethod(retVal, "", "", "Release Number");
        helperUskedMapMethod(retVal, "", "", "Project");
        helperUskedMapMethod(retVal, "", "", "Overtime Bill Type");
        helperUskedMapMethod(retVal, "", "", "Holiday Bill");
        helperUskedMapMethod(retVal, "", "", "Holiday Pay");
        helperUskedMapMethod(retVal, "", "", "Contact 1 Name");
        helperUskedMapMethod(retVal, "", "", "Contact 1 Description");
        helperUskedMapMethod(retVal, "", "", "Contact 1 Phone 1");
        helperUskedMapMethod(retVal, "", "", "Contact 1 Phone 2");
        helperUskedMapMethod(retVal, "", "", "Contact 1 Phone 3");
        helperUskedMapMethod(retVal, "", "", "Contact 2 Name");
        helperUskedMapMethod(retVal, "", "", "Contact 2 Description");
        helperUskedMapMethod(retVal, "", "", "Contact 2 Phone 1");
        helperUskedMapMethod(retVal, "", "", "Contact 2 Phone 2");
        helperUskedMapMethod(retVal, "", "", "Contact 2 Phone 3");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Hour Type");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Rate Code");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Comp Code");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Regular Pay Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Overtime Pay Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Double Time Pay Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Regular Bill Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Overtime Bill Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 1 Double Time Bill Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Hour Type");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Rate Code");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Comp Code");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Regular Pay Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Overtime Pay Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Double Time Pay Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Regular Bill Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Overtime Bill Rate");
        helperUskedMapMethod(retVal, "", "", "Rate 2 Double Time Bill Rate");
        helperUskedMapMethod(retVal, "", "", "Note 1 Type");
        helperUskedMapMethod(retVal, "", "", "Note 1 Notes");
        helperUskedMapMethod(retVal, "", "", "Note 2 Type");
        helperUskedMapMethod(retVal, "", "", "Note 2 Notes");
        helperUskedMapMethod(retVal, "", "", "Note 3 Type");
        helperUskedMapMethod(retVal, "", "", "Note 3 Notes");
        helperUskedMapMethod(retVal, "", "", "Note 4 Type");
        helperUskedMapMethod(retVal, "", "", "Note 4 Notes");
        helperUskedMapMethod(retVal, "", "", "Note 5 Type");
        helperUskedMapMethod(retVal, "", "", "Note 5 Notes");
        helperUskedMapMethod(retVal, "", "", "Branch Id");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Type");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Category");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Notes");

        helperUskedMapMethod(retVal, "", "", "Tracking 2 Type");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Category");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Notes");

        helperUskedMapMethod(retVal, "", "", "Tracking 3 Type");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Category");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Notes");

        
        return retVal;
    }

    @Override
    public LinkedHashMap<DataMappingClass, Integer> getMappings() {
        LinkedHashMap<DataMappingClass, Integer> retVal = new LinkedHashMap<DataMappingClass, Integer>();

        TableMapClass tClientName = new TableMapClass("client", "client_name");
        DataMappingClass clientName = new DataMappingClass(tClientName, "Client Name", "Company Name", "CompanyName", "ClientName", "Location", "Name");
        clientName.setUskedId("Customer Name");
        retVal.put(clientName, -1);

        UskedActiveColumn tClientStatus = new UskedActiveColumn("client", "client_is_deleted");
        DataMappingClass clientStatus = new DataMappingClass(tClientStatus, "Is Active");
        clientStatus.setUskedId("Status");
        retVal.put(clientStatus, -1);

        TableMapClass tClientPhone = new TableMapClass("client", "client_phone");
        DataMappingClass clientPhone = new DataMappingClass(tClientPhone, "Phone Number", "Phone", "Phn", "PhoneNumber");
        retVal.put(clientPhone, -1);

        TableMapClass tClientPhone2 = new TableMapClass("client", "client_phone2");
        DataMappingClass clientPhone2 = new DataMappingClass(tClientPhone2, "Phone Number 2", "Phone 2", "Phn 2", "PhoneNumber 2", "Alt Phone Number", "Alt Phn");
        retVal.put(clientPhone2, -1);

        TableMapClass tClientFax = new TableMapClass("client", "client_fax");
        DataMappingClass clientFax = new DataMappingClass(tClientFax, "Fax", "Fax Number", "FaxNumber");
        retVal.put(clientFax, -1);

        TableMapClass tClientAddress = new TableMapClass("client", "client_address");
        DataMappingClass clientAddress = new DataMappingClass(tClientAddress, "Address Line 1", "Address1", "Address");
        clientAddress.setUskedId("Address 1");
        retVal.put(clientAddress, -1);

        TableMapClass tClientAddress2 = new TableMapClass("client", "client_address2");
        DataMappingClass clientAddress2 = new DataMappingClass(tClientAddress2, "Address Line 2", "Address2", "Line2");
        clientAddress2.setUskedId("Address 2");
        retVal.put(clientAddress2, -1);

        TableMapClass tClientCity = new TableMapClass("client", "client_city");
        DataMappingClass clientCity = new DataMappingClass(tClientCity, "City");
        clientCity.setUskedId("City");
        retVal.put(clientCity, -1);

        TableMapClass tClientState = new UskedStateColumn("client", "client_state");
        DataMappingClass clientState = new DataMappingClass(tClientState, "State");
        clientState.setUskedId("State");
        retVal.put(clientState, -1);

        TableMapClass tClientZip = new TableMapClass("client", "client_zip");
        DataMappingClass clientZip = new DataMappingClass(tClientZip, "Zip", "Zip Code", "ZipCode");
        clientZip.setUskedId("Zip");
        retVal.put(clientZip, -1);

        TableMapClass tClientStart = new TableMapClass("client", "client_start_date");
        DataMappingClass clientStart = new DataMappingClass(tClientStart, "Start Date", "StartDate", "Start");
        retVal.put(clientStart, -1);

        TableMapClass tClientEnd = new TableMapClass("client", "client_end_date");
        DataMappingClass clientEnd = new DataMappingClass(tClientEnd, "End Date", "EndDate", "End");
        retVal.put(clientEnd, -1);

        TableMapClass tClientTraining = new TableMapClass("client", "client_training_time");
        DataMappingClass clientTraining = new DataMappingClass(tClientTraining, "Required Training Time", "Training Time", "Training");
        retVal.put(clientTraining, -1);

        return retVal;
    }

    @Override
    public void insertValuesIntoDB(LinkedHashMap<DataMappingClass, Integer> mappedColumns) {
        this.insertValuesIntoClientTable(mappedColumns);
    }
    
    public void insertValuesIntoClientTable(LinkedHashMap<DataMappingClass, Integer> mappedColumns) {
        ArrayList<DataMappingClass> clientValues = super.getDataMappingByTable(mappedColumns, "client");

        insertedClients = new ArrayList<Client>();
        updatedClients = new ArrayList<Client>();
        errorClients = new ArrayList<Client>();

        DataMappingClass clientName = super.getDataMappingClassByColumnName("client_name", clientValues);
        DataMappingClass clientPhone = super.getDataMappingClassByColumnName("client_phone", clientValues);
        DataMappingClass clientPhone2 = super.getDataMappingClassByColumnName("client_phone2", clientValues);
        DataMappingClass clientFax = super.getDataMappingClassByColumnName("client_fax", clientValues);
        DataMappingClass clientAddress = super.getDataMappingClassByColumnName("client_address", clientValues);
        DataMappingClass clientAddress2 = super.getDataMappingClassByColumnName("client_address2", clientValues);
        DataMappingClass clientCity = super.getDataMappingClassByColumnName("client_city", clientValues);
        DataMappingClass clientState = super.getDataMappingClassByColumnName("client_state", clientValues);
        DataMappingClass clientZip = super.getDataMappingClassByColumnName("client_zip", clientValues);
        DataMappingClass clientStartDate = super.getDataMappingClassByColumnName("client_start_date", clientValues);
        DataMappingClass clientEndDate = super.getDataMappingClassByColumnName("client_end_date", clientValues);
        DataMappingClass clientDeleted = super.getDataMappingClassByColumnName("client_is_deleted", clientValues);
        DataMappingClass clientType = super.getDataMappingClassByColumnName("client_type", clientValues);
        DataMappingClass clientLastUpdated = super.getDataMappingClassByColumnName("client_last_updated", clientValues);
        DataMappingClass clientWorksite = super.getDataMappingClassByColumnName("client_worksite", clientValues);
        DataMappingClass clientTraining = super.getDataMappingClassByColumnName("client_training_time", clientValues);
        DataMappingClass clientBillForTraining = super.getDataMappingClassByColumnName("client_bill_for_training", clientValues);
        DataMappingClass clientRateCode = super.getDataMappingClassByColumnName("rate_code_id", clientValues);

        ArrayList<String> clientNamesArrayList = clientName.getValues();
        ArrayList<String> clientPhoneArrayList = clientPhone.getValues();
        for (int s = 0; s < clientNamesArrayList.size(); s++) {
            Date currDate = new Date(myConn.getServerCurrentTimeMillis());
            Client currClient = new Client(currDate);
            boolean isNewEmployee = false;
            try {
                int client_id = -1;
                Record_Set rs = null;
                try {
                    FindClientByNameAndPhoneNumberQuery clientQuery = new FindClientByNameAndPhoneNumberQuery();
                    clientQuery.setPreparedStatement(new Object[]{clientPhoneArrayList.get(s), clientNamesArrayList.get(s)});
                    rs = this.myConn.executeQuery(clientQuery, "");

                    try {
                        client_id = rs.getInt("client_id");
                    } catch (Exception e) {}
                } catch (Exception e) {}
                if (client_id != -1) {
                    //An employee matches this lets update employee
                    currClient.setClientId(client_id);
                } else {
                    isNewEmployee = true;
                }
                try {
                    currClient.setClientName(clientName.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setBranchId(Integer.parseInt(branchId));
                } catch (Exception e) {}
                try {
                    currClient.setClientAddress(clientAddress.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientAddress2(clientAddress2.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientCity(clientCity.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientState(clientState.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientZip(clientZip.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientBillForTraining(clientBillForTraining.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientStartDate(null);
                } catch (Exception e) {}
                try {
                    currClient.setClientEndDate(null);
                } catch (Exception e) {}
                try {
                    currClient.setClientFax(clientFax.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientPhone(clientPhone.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientPhone2(clientPhone2.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setRateCodeId(Integer.parseInt(clientRateCode.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currClient.setClientTrainingTime(Double.parseDouble(clientTraining.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currClient.setClientIsDeleted(clientDeleted.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currClient.setClientType(Integer.parseInt(clientType.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currClient.setClientWorksite(Integer.parseInt(clientWorksite.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currClient.setClientLastUpdated(null);
                } catch (Exception e) {}

                ClientController myController = ClientController.getInstance("2");
                myController.saveClient(currClient);

                if (isNewEmployee) {
                    insertedClients.add(currClient);
                } else { 
                    updatedClients.add(currClient);
                }
            } catch (Exception e) {
                errorClients.add(currClient);
            }
        }
    }

   

    

    

    private class UskedStateColumn extends TableMapClass {

        private Pattern myPattern = Pattern.compile("UU");

        public UskedStateColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            Matcher myMatcher = myPattern.matcher(value);
            if (!myMatcher.matches()) {
                return "TX";
            }
            return value;
        }
    }

    private class UskedBillableColumn extends TableMapClass {

        public UskedBillableColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            return "Y";
        }
    }

}
