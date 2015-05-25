/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.equipment.*;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_device_for_equipment_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.save_sales_equipment_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.EmployeeEquipment;
import schedfoxlib.model.Equipment;
import schedfoxlib.controller.EquipmentControllerInterface;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientEquipment;
import schedfoxlib.model.ClientEquipmentCommand;
import schedfoxlib.model.ClientEquipmentContact;
import schedfoxlib.model.ClientEquipmentVendor;
import schedfoxlib.model.ClientEquipmentWithStats;
import schedfoxlib.model.ClientMessaging;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EntityEquipment;
import schedfoxlib.model.SalesDeviceBundle;
import schedfoxlib.model.SalesEquipment;
import schedfoxlib.model.User;

/**
 *
 * @author user
 */
public class EquipmentController implements EquipmentControllerInterface {

    private String companyId;

    public EquipmentController(String companyId) {
        this.companyId = companyId;
    }

    public static EquipmentController getInstance(String companyId) {
        return new EquipmentController(companyId);
    }

    public String getCompanyId() {
        return this.companyId;
    }

    @Override
    public void markEquipmentReturned() throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
    }

    @Override
    public void saveClientEquipmentContact(ClientEquipmentContact contact) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_client_equipment_contact_query saveQuery = new save_client_equipment_contact_query();
        saveQuery.update(contact, true);
        saveQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveQuery, "");

        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    public ClientEquipment getClientEquipmentByMdn(String mdn) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_by_mdn_query query = new get_equipment_by_mdn_query();
        query.setPreparedStatement(new Object[]{mdn});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<ClientEquipmentWithStats> getClientEquipmentWithStats(int equipmentType, int clientId) throws RetrieveDataException {
        ArrayList<ClientEquipmentWithStats> retVal = new ArrayList<ClientEquipmentWithStats>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_with_stats_query query = new get_equipment_with_stats_query();
        query.setPreparedStatement(new Object[]{equipmentType, equipmentType, clientId, clientId, equipmentType, equipmentType});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                ClientEquipmentWithStats equip = new ClientEquipmentWithStats(rst);
                
                try {
                    Client myClient = new Client(new Date(), rst);
                    equip.setClient(myClient);
                } catch (Exception exe) {}
                retVal.add(equip);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        EquipmentController controller = EquipmentController.getInstance(companyId);
        HashMap<Integer, Equipment> equipHash = new HashMap<Integer, Equipment>();
        for (int r = 0; r < retVal.size(); r++) {
            if (equipHash.get(retVal.get(r).getClientEquipment().getEquipmentId()) == null) {
                Equipment equip = controller.getEquipmentById(retVal.get(r).getClientEquipment().getEquipmentId());
                equipHash.put(equip.getEquipmentId(), equip);
            }
            retVal.get(r).getClientEquipment().setEquipment(equipHash.get(retVal.get(r).getClientEquipment().getEquipmentId()));
        }

        return retVal;
    }

    @Override
    public ArrayList<ClientEquipment> getClientEquipmentGroupedByMdn(int equipmentType, int clientId, boolean loadLastKnownDate) throws RetrieveDataException {
        ArrayList<ClientEquipment> retVal = new ArrayList<ClientEquipment>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_group_by_mdn_query query = new get_equipment_group_by_mdn_query();
        query.update(loadLastKnownDate);
        query.setPreparedStatement(new Object[]{equipmentType, equipmentType, clientId, clientId, equipmentType, equipmentType});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientEquipment(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        for (int r = 0; r < retVal.size(); r++) {
            Equipment equip = EquipmentController.getInstance(companyId).getEquipmentById(retVal.get(r).getEquipmentId());
            retVal.get(r).setEquipment(equip);
        }

        return retVal;
    }

    @Override
    public ArrayList<EntityEquipment> getEquipmentByTypeAndId(int equipmentType, int clientId, Class<EntityEquipment> entityType, boolean loadLastKnownDate) throws RetrieveDataException {
        ArrayList<EntityEquipment> retVal = new ArrayList<EntityEquipment>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_by_type_and_id_query query = new get_equipment_by_type_and_id_query();
        query.update(entityType, loadLastKnownDate);
        query.setPreparedStatement(new Object[]{equipmentType, clientId, clientId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                if (entityType.equals(ClientEquipment.class)) {
                    retVal.add(new ClientEquipment(rst));
                } else {
                    retVal.add(new EmployeeEquipment(rst));
                }
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        for (int r = 0; r < retVal.size(); r++) {
            Equipment equip = EquipmentController.getInstance(companyId).getEquipmentById(retVal.get(r).getEquipmentId());
            retVal.get(r).setEquipment(equip);
        }

        return retVal;
    }

    @Override
    public ArrayList<EntityEquipment> getEquipmentByType(int equipmentType, Class<EntityEquipment> entityType) throws RetrieveDataException {
        ArrayList<EntityEquipment> retVal = new ArrayList<EntityEquipment>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_by_type_query query = new get_equipment_by_type_query();
        query.update(entityType);
        query.setPreparedStatement(new Object[]{equipmentType});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                if (entityType.equals(ClientEquipment.class)) {
                    retVal.add(new ClientEquipment(rst));
                } else {
                    retVal.add(new EmployeeEquipment(rst));
                }
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        for (int r = 0; r < retVal.size(); r++) {
            Equipment equip = EquipmentController.getInstance(companyId).getEquipmentById(retVal.get(r).getEquipmentId());
            retVal.get(r).setEquipment(equip);
        }

        return retVal;
    }

    public ClientEquipment getEquipmentByMdn(String mdn) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_client_equipment_by_mdn_query query = new get_client_equipment_by_mdn_query();
        query.setPreparedStatement(new Object[]{mdn});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ClientEquipment getEquipmentByIdentifier(String identifier) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_client_equipment_by_id_query query = new get_client_equipment_by_id_query();
        query.setPreparedStatement(new Object[]{identifier});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ClientEquipment getEquipmentByIdAndIdentifier(int equipmentId, String identifier) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_client_equipment_query query = new get_client_equipment_query();
        query.setPreparedStatement(new Object[]{equipmentId, identifier});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public Equipment getEquipmentByName(String name) throws RetrieveDataException {
        Equipment retVal = new Equipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_by_name_query query = new get_equipment_by_name_query();
        query.setPreparedStatement(new Object[]{name});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Equipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public Equipment getEquipmentById(int equipmentId) throws RetrieveDataException {
        Equipment retVal = new Equipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_by_id_query query = new get_equipment_by_id_query();
        query.setPreparedStatement(new Object[]{equipmentId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Equipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public Integer saveClientEquipment(ClientEquipment clientEquipment) throws RetrieveDataException, SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        boolean isNewReport = false;
        ClientEquipment doesExist = this.getClientEquipmentByMdn(clientEquipment.getMdn());
        if (doesExist != null && doesExist.getClientEquipmentId() != null) {
            clientEquipment.setClientEquipmentId(doesExist.getClientEquipmentId());
            if (clientEquipment.getPhoneNumber() == null || clientEquipment.getPhoneNumber().trim().length() == 0) {
                clientEquipment.setPhoneNumber(doesExist.getPhoneNumber());
            }
            if (clientEquipment.getNickname() == null || clientEquipment.getNickname().trim().length() == 0) {
                clientEquipment.setNickname(doesExist.getNickname());
            }
            if (clientEquipment.getClientEquipmentId() == null) {
                clientEquipment.setActive(true);
            }
        }

        if (clientEquipment.getClientEquipmentId() == null || clientEquipment.getClientEquipmentId() == 0) {
            isNewReport = true;
            get_client_equipment_next_sequence_query sequenceQuery = new get_client_equipment_next_sequence_query();
            sequenceQuery.setPreparedStatement(new Object[]{});
            sequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(sequenceQuery, "");
                clientEquipment.setClientEquipmentId(rst.getInt(0));
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        if (clientEquipment.getActive() == null) {
            clientEquipment.setActive(true);
        }
        
        save_client_equipment_query saveEquipmentQuery = new save_client_equipment_query();
        saveEquipmentQuery.update(clientEquipment, isNewReport);
        saveEquipmentQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveEquipmentQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        return clientEquipment.getClientEquipmentId();
    }

    /**
     * Gets a list of the equipment available to this controller.
     *
     * @return ArrayList<Equipment>
     */
    @Override
    public ArrayList<Equipment> getEquipment() throws RetrieveDataException {
        ArrayList<Equipment> retVal = new ArrayList<Equipment>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_active_equipment_query query = new get_active_equipment_query();
        query.setPreparedStatement(new Object[]{});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Equipment(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void returnEmployeeEquipment(EmployeeEquipment empEquip, Integer receivedby) throws SaveDataException {
        mark_equipment_returned_query returnQuery = new mark_equipment_returned_query();
        returnQuery.setPreparedStatement(new Object[]{receivedby, empEquip.getEmployeeEquipmentId()});

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        returnQuery.setCompany(companyId);
        try {
            conn.executeUpdate(returnQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public void waiveEmployeeEquipmentReturn(EmployeeEquipment empEquip, Integer waivedBy) throws SaveDataException {
        mark_equipment_waived_query waivedQuery = new mark_equipment_waived_query();
        waivedQuery.setPreparedStatement(new Object[]{waivedBy, empEquip.getEmployeeEquipmentId()});

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        waivedQuery.setCompany(companyId);
        try {
            conn.executeUpdate(waivedQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void clearReturnEmployeeEquipment(EmployeeEquipment empEquip) throws SaveDataException {
        clear_equipment_returned_query returnQuery = new clear_equipment_returned_query();
        returnQuery.setPreparedStatement(new Object[]{empEquip.getEmployeeEquipmentId()});

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        returnQuery.setCompany(companyId);
        try {
            conn.executeUpdate(returnQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    /**
     * Gets a list of the equipment available to this controller.
     *
     * @return ArrayList<Equipment>
     */
    @Override
    public ArrayList<EmployeeEquipment> getEmployeeEquipment(Integer equipmentId, String searchStr, boolean showAll, ArrayList<Integer> selBranches, Date startDate, Date endDate, Integer numberOfContacts) throws RetrieveDataException {
        ArrayList<EmployeeEquipment> retVal = new ArrayList<EmployeeEquipment>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_employee_equipment_query query = new get_employee_equipment_query();
        query.update(equipmentId, searchStr, showAll, selBranches, startDate, endDate, numberOfContacts);
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeeEquipment(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<Equipment> getEquipmentByClientOrEmployee(boolean loadForClient, boolean loadForEmployee) throws RetrieveDataException {
        ArrayList<Equipment> retVal = new ArrayList<Equipment>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_by_client_or_employee_query query = new get_equipment_by_client_or_employee_query();
        query.setPreparedStatement(new Object[]{loadForEmployee, loadForClient});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Equipment(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public Integer saveClientEquipmentCommand(ClientEquipmentCommand equipmentCommand) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        java.sql.Timestamp dateSent = null;
        java.sql.Timestamp dateReceived = null;
        try {
            dateSent = new java.sql.Timestamp(equipmentCommand.getDateSent().getTime());
        } catch (Exception exe) {
        }
        try {
            dateReceived = new java.sql.Timestamp(equipmentCommand.getDateReceived().getTime());
        } catch (Exception exe) {
        }

        boolean isInsert = false;
        if (equipmentCommand.getClientEquipmentCommandId() == null) {
            get_next_equipment_command_seq_query nextSeqQuery = new get_next_equipment_command_seq_query();
            nextSeqQuery.setPreparedStatement(new Object[]{});
            nextSeqQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(nextSeqQuery, "");
                equipmentCommand.setClientEquipmentCommandId(rst.getInt(0));
                isInsert = true;
            } catch (Exception exe) {
            }
        }

        save_client_equipment_command_query query = new save_client_equipment_command_query();
        query.setPreparedStatement(new Object[]{equipmentCommand.getClientEquipmentId(), equipmentCommand.getCommand().getValue(), equipmentCommand.getData(),
            dateSent, dateReceived, equipmentCommand.getActive(), equipmentCommand.getClientEquipmentCommandId()});
        if (isInsert) {
            query.setIsInsert(true);
        } else {
            query.setIsInsert(false);
        }
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");

        } catch (Exception e) {
            throw new SaveDataException();
        }
        return equipmentCommand.getClientEquipmentCommandId();
    }

    public ArrayList<ClientMessaging> getClientMessaging(Integer clientEquipmentId) throws RetrieveDataException {
        ArrayList<ClientMessaging> retVal = new ArrayList<ClientMessaging>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_messaging_query query = new get_equipment_messaging_query();
        query.setPreparedStatement(new Object[]{clientEquipmentId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientMessaging(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<ClientEquipmentVendor> getClientEquipmentVendor() throws RetrieveDataException {
        ArrayList<ClientEquipmentVendor> retVal = new ArrayList<ClientEquipmentVendor>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_client_equipment_vendor_query query = new get_client_equipment_vendor_query();
        query.setPreparedStatement(new Object[]{});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientEquipmentVendor(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ClientEquipment getEquipmentByPrimaryId(int equipmentClientId) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_client_equipment_from_id_query query = new get_client_equipment_from_id_query();
        query.setPreparedStatement(new Object[]{equipmentClientId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ClientEquipment getEquipmentByClientId(int equipmentClientId) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_client_equipment_by_int_id_query query = new get_client_equipment_by_int_id_query();
        query.setPreparedStatement(new Object[]{equipmentClientId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public User getUserWithThisEquipment(String mdn) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        User retVal = new User();

        get_user_associated_with_sales_equipment_query query = new get_user_associated_with_sales_equipment_query();
        query.setPreparedStatement(new Object[]{mdn});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new User(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public SalesEquipment getEquipmentByUserId(Integer userId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        SalesEquipment retVal = new SalesEquipment();
        get_sales_equipment_by_user_query query = new get_sales_equipment_by_user_query();
        query.setPreparedStatement(new Object[]{userId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            retVal = new SalesEquipment(rst);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void saveSalesEquipment(SalesEquipment equipment) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_sales_equipment_query query = new save_sales_equipment_query();
        query.update(equipment);
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public void saveSalesDevice(User user, ClientEquipment clientEquipment) throws RetrieveDataException, SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_sales_device_for_equipment_query query = new get_sales_device_for_equipment_query();
        query.setPreparedStatement(new Object[]{clientEquipment.getClientEquipmentId()});
        query.setCompany(companyId);

        SalesEquipment retVal = new SalesEquipment();
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new SalesEquipment(rst);
                rst.moveNext();
            }

            if (retVal == null || retVal.getUserId() == null) {
                SalesEquipment salesEquipment = new SalesEquipment();
                salesEquipment.setActive(true);
                salesEquipment.setClientEquipmentId(clientEquipment.getClientEquipmentId());
                salesEquipment.setDateAssigned(new Date(GenericController.getInstance(companyId).getCurrentTimeMillis()));
                salesEquipment.setUserId(user.getUserId());

                saveSalesEquipment(salesEquipment);
            } else if (retVal.getUserId() != user.getUserId()) {
                retVal.setUserId(user.getUserId());
                saveSalesEquipment(retVal);
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    public void associateSalesDeviceWithUser(SalesDeviceBundle bundle) throws RetrieveDataException, SaveDataException {
        try {
            ClientEquipment clientEquipment = bundle.getClientEquipment();
            String clientEquipmentNewName = clientEquipment.getNickname();
            if (clientEquipmentNewName == null) {
                clientEquipmentNewName = "";
            }
            ClientEquipment checkIfExists = this.getClientEquipmentByMdn(clientEquipment.getMdn());
            if ((checkIfExists == null || checkIfExists.getClientEquipmentId() == null) || !clientEquipmentNewName.equals(checkIfExists.getNickname())) {
                checkIfExists.setNickname(clientEquipmentNewName);
                try {
                    clientEquipment.setClientEquipmentId(this.saveClientEquipment(clientEquipment));
                } catch (Exception exe) {
                    throw new SaveDataException();
                }
                clientEquipment = checkIfExists;
            } else {
                clientEquipment.setClientEquipmentId(checkIfExists.getClientEquipmentId());
            }

            User user = bundle.getUser();
            User compUser = getUserWithThisEquipment(clientEquipment.getUniqueId());
            if (compUser == null || compUser.getId() == null || !compUser.getId().equals(user.getId())) {
                saveSalesDevice(user, clientEquipment);
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }

    }

    public ClientEquipment getEquipmentFromSalesPersonId(Integer userId) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_for_user_id_query query = new get_equipment_for_user_id_query();
        query.setPreparedStatement(new Object[]{userId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<ClientEquipmentCommand> getPendingCommands(Integer clientEquipmentId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientEquipmentCommand> retVal = new ArrayList<ClientEquipmentCommand>();

        get_pending_equipment_commands_query query = new get_pending_equipment_commands_query();
        query.setPreparedStatement(new Object[]{clientEquipmentId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientEquipmentCommand(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void markCommandReceived(ClientEquipmentCommand command) throws SaveDataException {
        command.setDateReceived(new Date(GenericController.getInstance(companyId).getCurrentTimeMillis()));
        this.saveClientEquipmentCommand(command);
    }

    public ArrayList<SalesEquipment> getSalesEquipmentForActiveUsers() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_sales_equipment_by_active_users_query query = new get_sales_equipment_by_active_users_query();
        query.setPreparedStatement(new Object[]{});
        query.setCompany(companyId);
        ArrayList<SalesEquipment> retVal = new ArrayList<SalesEquipment>();
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesEquipment(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<Employee> getEmployeesAtClient(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employees_assigned_to_client_equip_query query = new get_employees_assigned_to_client_equip_query();
        query.setPreparedStatement(new Object[]{clientId});
        query.setCompany(companyId);
        ArrayList<Employee> retVal = new ArrayList<Employee>();
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<ClientEquipmentContact> getClientContact(Integer clientEquipmentId, Integer daysToGoBack) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_contact_for_equipment_id_query query = new get_contact_for_equipment_id_query();
        query.setPreparedStatement(new Object[]{clientEquipmentId, daysToGoBack});
        query.setCompany(companyId);
        ArrayList<ClientEquipmentContact> retVal = new ArrayList<ClientEquipmentContact>();
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientEquipmentContact(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public Boolean clearSalesEquipment(Integer clientEquipmentId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        delete_sales_equipment_link_query deleteQuery = new delete_sales_equipment_link_query();
        deleteQuery.setPreparedStatement(new Object[]{clientEquipmentId});
        deleteQuery.setCompany(companyId);
        try {
            conn.executeUpdate(deleteQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        
        delete_equipment_query deleteEquipQuery = new delete_equipment_query();
        deleteEquipQuery.setPreparedStatement(new Object[]{clientEquipmentId});
        deleteEquipQuery.setCompany(companyId);
        try {
            conn.executeUpdate(deleteEquipQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        return true;
    }

    @Override
    public ClientEquipment getClientEquipmentById(int equipmentId) throws RetrieveDataException {
        ClientEquipment retVal = new ClientEquipment();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_equipment_for_equipment_id_query query = new get_equipment_for_equipment_id_query();
        query.setPreparedStatement(new Object[]{equipmentId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientEquipment(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<ClientEquipment> getClientEquipmentByIds(ArrayList<Integer> equipmentIds) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
