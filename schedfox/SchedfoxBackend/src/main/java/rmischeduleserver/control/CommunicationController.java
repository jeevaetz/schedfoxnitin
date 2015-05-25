/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.ServerSideConnection;
import schedfoxlib.model.CommunicationSource;
import schedfoxlib.model.CommunicationTransaction;
import schedfoxlib.model.Employee;
import rmischeduleserver.log.MyLogger;
import rmischeduleserver.mysqlconnectivity.queries.communication.get_next_communication_source_sequence_query;
import rmischeduleserver.mysqlconnectivity.queries.communication.lookup_communication_by_value_query;
import rmischeduleserver.mysqlconnectivity.queries.communication.save_employee_communication_assoc_query;
import rmischeduleserver.mysqlconnectivity.queries.communication.save_new_communication_source_query;
import rmischeduleserver.mysqlconnectivity.queries.communication.save_new_communication_transaction_query;
import rmischeduleserver.mysqlconnectivity.queries.login.get_employees_by_cid_from_inbound_caller;

/**
 *
 * @author user
 */
public class CommunicationController {
    public MyLogger log = null;
    //private static CommunicationController myInstance;

    private String companyId;

    public CommunicationController(String companyId, MyLogger log) {
        this.log = log;
        this.companyId = companyId;
    }

    /*
    public static CommunicationController getInstance(String companyId) {
        if (myInstance == null) {
            myInstance = new CommunicationController(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            try {
                conn.setConnectionObject(new ServerSideConnection());
            } catch (Exception e) {
                System.out.println("Could not set up server!");
            }
        }
        return myInstance;
    }
     *
     */

    public CommunicationSource getCommunicationSource(String phoneNumber) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        lookup_communication_by_value_query getCommunicationSourceQuery = new lookup_communication_by_value_query();
        getCommunicationSourceQuery.setCompany(companyId);
        getCommunicationSourceQuery.setPreparedStatement(new Object[]{phoneNumber});
        CommunicationSource mySource = null;
        try {
            Record_Set rst = conn.executeQuery(getCommunicationSourceQuery, "");
            if (rst.length() > 0) {
                mySource = new CommunicationSource(rst);
            }
        } catch (Exception e) {
            conn = null;
            e.printStackTrace();
        }
        if (mySource == null) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return mySource;
    }

    public void setClientAssociatedWithSource(CommunicationSource source, int clientId) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        save_employee_communication_assoc_query saveAssocQuery = new save_employee_communication_assoc_query();
        saveAssocQuery.setCompany(companyId);
        saveAssocQuery.setPreparedStatement(new Object[]{source.getCommunicationSourceId(), clientId, source.getCommunicationSourceId()});
        CommunicationSource mySource = null;
        try {
            conn.executeUpdate(saveAssocQuery, "");
        } catch (Exception e) {
            conn = null;
            throw new SaveDataException();
        }
        conn = null;
    }

    /**
     * Returns all employees associated w/ communication source.
     * @param source
     * @return
     */
    public ArrayList<Employee> getEmployeesByCommunicationSource(CommunicationSource source) {
        ArrayList<Employee> retVal = new ArrayList<Employee>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_employees_by_cid_from_inbound_caller getCommunicationSourceQuery = new get_employees_by_cid_from_inbound_caller();
        getCommunicationSourceQuery.setCompany(companyId);
        getCommunicationSourceQuery.setPreparedStatement(new Object[]{source.getSource()});
        try {
            Record_Set rst = conn.executeQuery(getCommunicationSourceQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
            }
        } catch (Exception e) {
            conn = null;
            e.printStackTrace();
        }
        conn = null;
        return retVal;
    }

    /**
     * Pass in a phone number it will record a transaction for you, also will create
     * a new communication source if necessary. Please remove any dashes from phone
     * number.
     * @return
     */
    public void logPhoneNumberTransaction(String phoneNumber, Integer empId) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        lookup_communication_by_value_query getCommunicationSourceQuery = new lookup_communication_by_value_query();
        getCommunicationSourceQuery.setCompany(companyId);
        getCommunicationSourceQuery.setPreparedStatement(new Object[]{phoneNumber});
        CommunicationSource mySource = null;
        try {
            Record_Set rst = conn.executeQuery(getCommunicationSourceQuery, "");
            if (rst.length() > 0) {
                mySource = new CommunicationSource(rst);
            }
        } catch (Exception e) {
            conn = null;
            e.printStackTrace();
        }

        //No existing source was found lets insert a new one.
        if (mySource == null) {
            get_next_communication_source_sequence_query sequenceQuery = new get_next_communication_source_sequence_query();
            sequenceQuery.setCompany(companyId);
            sequenceQuery.setPreparedStatement(new Object[]{});
            try {
                Record_Set rst = conn.executeQuery(sequenceQuery, "");

                mySource = new CommunicationSource();
                mySource.setCommunicationSourceId(rst.getInt(0));
                mySource.setCommunicationTypeId(1);
                mySource.setSource(phoneNumber);

                save_new_communication_source_query saveQuery = new save_new_communication_source_query();
                saveQuery.update(mySource);
                saveQuery.setCompany(companyId);
                conn.executeUpdate(saveQuery, "");
                System.out.println("Should have saved the transaction");
            } catch (SQLException se) {
                conn = null;
                throw new SaveDataException();
            }
        }

        //We already have a valid source insert trans
        if (mySource != null) {
            CommunicationTransaction myTrans = new CommunicationTransaction();
            myTrans.setCommunicationSourceId(mySource.getCommunicationSourceId());
            myTrans.setAssociatedUserId(empId);
            myTrans.setAssociatedUserType(new Integer(1));
            save_new_communication_transaction_query saveTrans = new save_new_communication_transaction_query();
            saveTrans.setCompany(companyId);
            saveTrans.update(myTrans);
            try {
                conn.executeUpdate(saveTrans, "");
            } catch (SQLException sqle) {
                conn = null;
                throw new SaveDataException();
            }
        }
        conn = null;
    }
}
