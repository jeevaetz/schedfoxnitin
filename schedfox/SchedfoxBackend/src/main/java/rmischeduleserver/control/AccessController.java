/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.access.get_access_individual_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.access.get_access_individual_query;
import rmischeduleserver.mysqlconnectivity.queries.access.get_access_types_query;
import rmischeduleserver.mysqlconnectivity.queries.access.get_next_access_sequence_query;
import rmischeduleserver.mysqlconnectivity.queries.access.save_access_individual_query;
import rmischeduleserver.mysqlconnectivity.queries.access.save_access_logs_query;
import rmischeduleserver.mysqlconnectivity.queries.access.save_access_types_query;
import schedfoxlib.controller.AccessControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.AccessIndividualLogs;
import schedfoxlib.model.AccessIndividualTypes;
import schedfoxlib.model.AccessIndividuals;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class AccessController implements AccessControllerInterface {

    private String companyId;

    private AccessController(String companyId) {
        this.companyId = companyId;
    }
    
    public AccessController getInstance(String companyId) {
        return new AccessController(companyId);
    }

    public ArrayList<AccessIndividualTypes> getAccessTypes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<AccessIndividualTypes> retVal = new ArrayList<AccessIndividualTypes>();
        get_access_types_query query = new get_access_types_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new AccessIndividualTypes(rst));
                rst.moveNext();
            }
        } catch (Exception xe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void saveAccessTypes(AccessIndividualTypes type) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        save_access_types_query schemaQuery = new save_access_types_query();
        schemaQuery.update(type);
        schemaQuery.setCompany(companyId);

        try {
            conn.executeUpdate(schemaQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public ArrayList<AccessIndividuals> getAccessIndividuals(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<AccessIndividuals> retVal = new ArrayList<AccessIndividuals>();
        get_access_individual_query query = new get_access_individual_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId});
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new AccessIndividuals(rst));
                rst.moveNext();
            }
        } catch (Exception xe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void saveAccessIndividual(AccessIndividuals access) throws SaveDataException {
        boolean isUpdate = true;
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        if (access.getAccessIndividualId() == null) {
            isUpdate = false;
            try {
                get_next_access_sequence_query query = new get_next_access_sequence_query();
                query.setCompany(companyId);
                query.setPreparedStatement(new Object[]{});
                Record_Set rs = conn.executeQuery(query, "");
                access.setAccessIndividualId(rs.getInt(0));
            } catch (Exception exe) {
            }
        }

        save_access_individual_query schemaQuery = new save_access_individual_query();
        schemaQuery.update(access, isUpdate);
        schemaQuery.setCompany(companyId);

        try {
            conn.executeUpdate(schemaQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public AccessIndividuals getAccessIndividual(Integer accessIndividualId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        AccessIndividuals retVal = new AccessIndividuals();
        get_access_individual_by_id_query query = new get_access_individual_by_id_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{accessIndividualId});
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new AccessIndividuals(rst);
                rst.moveNext();
            }
        } catch (Exception xe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void saveAccessIndividualLog(AccessIndividualLogs access) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        save_access_logs_query schemaQuery = new save_access_logs_query();
        schemaQuery.update(access);
        schemaQuery.setCompany(companyId);

        try {
            conn.executeUpdate(schemaQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

}
