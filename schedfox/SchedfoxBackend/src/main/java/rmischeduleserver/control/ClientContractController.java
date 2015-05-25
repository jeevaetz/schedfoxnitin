/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.ClientContactControllerInterface;
import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.client.client_contact_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.client.client_contact_query;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.client.client_contract_auto_renew_query;
import rmischeduleserver.mysqlconnectivity.queries.client.get_client_contacts_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_contact_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_contact_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_contact_id_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_contact_types_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContactType;

/**
 *
 * @author user
 */
public class ClientContractController implements ClientContactControllerInterface {

    private String companyId;

    public ClientContractController(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public void autoRenewContracts() {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        client_contract_auto_renew_query query = new client_contract_auto_renew_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClientContact(ClientContact clientContact) throws SaveDataException {

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean update = true;
        try {
            if (clientContact.getClientContactId() == null || clientContact.getClientContactId() == 0) {
                update = false;
                get_client_contact_id_query getContactQuery = new get_client_contact_id_query();
                getContactQuery.setCompany(companyId);
                getContactQuery.setPreparedStatement(new Object[]{});
                Record_Set rst = conn.executeQuery(getContactQuery, "");
                clientContact.setClientContactId(rst.getInt(0));
            }
            save_client_contact_query saveQuery = new save_client_contact_query(clientContact, update);
            saveQuery.setCompany(companyId);
            conn.executeQuery(saveQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public ClientContact getClientContact(int client_contact_id) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        client_contact_by_id_query clientQuery = new client_contact_by_id_query();
        clientQuery.setPreparedStatement(new Object[]{client_contact_id});
        clientQuery.setCompany(companyId);
        ClientContact retVal = null;
        try {
            Record_Set rst = conn.executeQuery(clientQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientContact(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    public ArrayList<ClientContact> getClientContracts(Integer clientId, Boolean includeDeleted) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_client_contacts_query clientQuery = new get_client_contacts_query();
        clientQuery.setCompany(companyId);
        clientQuery.setPreparedStatement(new Object[]{clientId, includeDeleted, includeDeleted});
        ArrayList<ClientContact> retVal = new ArrayList<ClientContact>();
        try {
            Record_Set rst = conn.executeQuery(clientQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientContact(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<ClientContact> getClientContracts(int client_id) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        client_contact_query clientQuery = new client_contact_query(client_id + "");
        clientQuery.setCompany(companyId);
        ArrayList<ClientContact> retVal = new ArrayList<ClientContact>();
        try {
            Record_Set rst = conn.executeQuery(clientQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientContact(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<ClientContactType> getContactTypes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContactType> retVal = new ArrayList<ClientContactType>();
        get_client_contact_types_query idQuery = new get_client_contact_types_query();
        idQuery.setCompany(companyId);
        idQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(idQuery, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal.add(new ClientContactType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ClientContactType getClientContactById(int client_contact_id) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ClientContactType retVal = new ClientContactType();
        get_client_contact_by_id_query idQuery = new get_client_contact_by_id_query();
        idQuery.setCompany(companyId);
        idQuery.setPreparedStatement(new Object[]{client_contact_id});
        try {
            Record_Set rst = conn.executeQuery(idQuery, "");
            retVal = new ClientContactType(rst);
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
