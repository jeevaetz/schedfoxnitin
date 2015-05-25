/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientEquipment;
import schedfoxlib.model.ClientEquipmentCommand;
import schedfoxlib.model.ClientEquipmentContact;
import schedfoxlib.model.ClientEquipmentVendor;
import schedfoxlib.model.ClientEquipmentWithStats;
import schedfoxlib.model.ClientMessaging;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EmployeeEquipment;
import schedfoxlib.model.EntityEquipment;
import schedfoxlib.model.Equipment;
import schedfoxlib.model.SalesDeviceBundle;
import schedfoxlib.model.SalesEquipment;
import schedfoxlib.model.User;

/**
 *
 * @author user
 */
public interface EquipmentControllerInterface {
    
    public ArrayList<ClientEquipment> getClientEquipmentByIds(ArrayList<Integer> equipmentIds) throws RetrieveDataException;
    
    public ArrayList<ClientEquipmentWithStats> getClientEquipmentWithStats(int equipmentType, int clientId) throws RetrieveDataException;
    
    public void waiveEmployeeEquipmentReturn(EmployeeEquipment empEquip, Integer waivedBy) throws SaveDataException;
            
    public ClientEquipment getClientEquipmentByMdn(String mdn) throws RetrieveDataException;
    
    public ClientEquipment getClientEquipmentById(int equipmentId) throws RetrieveDataException;
            
    public ClientEquipment getEquipmentByMdn(String mdn) throws RetrieveDataException;
    
    public ArrayList<Employee> getEmployeesAtClient(Integer clientId) throws RetrieveDataException;
    
    public Integer saveClientEquipmentCommand(ClientEquipmentCommand equipmentCommand) throws SaveDataException;
    
    public ArrayList<ClientEquipment> getClientEquipmentGroupedByMdn(int equipmentType, int clientId, boolean loadLastKnownDate) throws RetrieveDataException;
    
    public void saveClientEquipmentContact(ClientEquipmentContact contact) throws SaveDataException, RetrieveDataException;

    public ArrayList<ClientMessaging> getClientMessaging(Integer clientEquipmentId) throws RetrieveDataException;
    
    public ClientEquipment getEquipmentByPrimaryId(int equipmentClientId) throws RetrieveDataException;
    
    public void clearReturnEmployeeEquipment(EmployeeEquipment empEquip) throws SaveDataException;
    
    public Integer saveClientEquipment(ClientEquipment clientEquipment) throws RetrieveDataException, SaveDataException;
    
    public ArrayList<ClientEquipmentVendor> getClientEquipmentVendor() throws RetrieveDataException;
    
    public User getUserWithThisEquipment(String mdn) throws RetrieveDataException;
    
    public ClientEquipment getEquipmentFromSalesPersonId(Integer userId) throws RetrieveDataException;
    
    public void associateSalesDeviceWithUser(SalesDeviceBundle bundle) throws RetrieveDataException, SaveDataException;
    
    public SalesEquipment getEquipmentByUserId(Integer userId) throws RetrieveDataException;
    
    public Boolean clearSalesEquipment(Integer clientEquipmentId) throws SaveDataException;
    
    /**
     * Gets a list of the equipment available to this controller.
     * @return ArrayList<Equipment>
     */
    public ArrayList<EmployeeEquipment> getEmployeeEquipment(Integer equipmentId, String searchStr, boolean showAll, ArrayList<Integer> selBranches, Date startDate, Date endDate, Integer numberContacts) throws RetrieveDataException;
    
    public ArrayList<Equipment> getEquipmentByClientOrEmployee(boolean loadForClient, boolean loadForEmployee) throws RetrieveDataException;
    
    public ArrayList<EntityEquipment> getEquipmentByTypeAndId(int equipmentType, int clientId, Class<EntityEquipment> entityType, boolean loadLastKnownDate) throws RetrieveDataException;
    
    public ArrayList<EntityEquipment> getEquipmentByType(int equipmentType, Class<EntityEquipment> entityType) throws RetrieveDataException;
    
    public ClientEquipment getEquipmentByIdAndIdentifier(int equipmentId, String identifier) throws RetrieveDataException;
    
    public ClientEquipment getEquipmentByClientId(int equipmentClientId) throws RetrieveDataException;
    
    public ClientEquipment getEquipmentByIdentifier(String identifier) throws RetrieveDataException;
            
    public ArrayList<ClientEquipmentCommand> getPendingCommands(Integer clientEquipmentId) throws RetrieveDataException;
    
    public void markCommandReceived(ClientEquipmentCommand command) throws SaveDataException;
    
    /**
     * Gets a list of the equipment available to this controller.
     * @return ArrayList<Equipment>
     */
    public ArrayList<Equipment> getEquipment() throws RetrieveDataException;
    
    public Equipment getEquipmentById(int equipmentId) throws RetrieveDataException;
    
    public Equipment getEquipmentByName(String name) throws RetrieveDataException;
    
    public void markEquipmentReturned() throws SaveDataException;
    
    public void returnEmployeeEquipment(EmployeeEquipment empEquip, Integer receivedby) throws SaveDataException;
    
    public ArrayList<ClientEquipmentContact> getClientContact(Integer clientEquipmentId, Integer daysToGoBack) throws RetrieveDataException;
    
    public ArrayList<SalesEquipment> getSalesEquipmentForActiveUsers() throws RetrieveDataException;
}
