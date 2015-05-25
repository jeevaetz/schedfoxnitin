/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ClientEquipmentCommand implements Serializable {
    private Integer clientEquipmentCommandId;
    private Integer clientEquipmentId;
    private Command command;
    private String data;
    private Date dateSent;
    private Date dateReceived;
    private Boolean active;

    public enum Command {
        
        MSG_TO_DEVICE_FROM_CLIENT("Send Msg"), MSG_TO_SERVER_TO_CLIENT("Receive Msg"), 
        MSG_TO_DEVICE_FROM_ADMIN("Send Msg Admin"), MSG_TO_SERVER_TO_ADMIN("Receive Msg Admin");
        
        private String val;
        
        Command(String value) {
            this.val = value;
        }
        
        public String getValue() {
            return this.val;
        }
    }
    
    public ClientEquipmentCommand() {
        
    }
    
    public ClientEquipmentCommand(Record_Set rst) {
        try {
            this.clientEquipmentCommandId = rst.getInt("client_equipment_command_id");
        } catch (Exception exe) {}
        try {
            this.clientEquipmentId = rst.getInt("client_equipment_id");
        } catch (Exception exe) {}
        try {
            this.active = rst.getBoolean("active");
        } catch (Exception exe) {}
        try {
            if (rst.getString("command").equals(Command.MSG_TO_SERVER_TO_CLIENT.getValue())) {
                this.command = Command.MSG_TO_SERVER_TO_CLIENT;
            } else if (rst.getString("command").equals(Command.MSG_TO_DEVICE_FROM_CLIENT.getValue())) {
                this.command = Command.MSG_TO_DEVICE_FROM_CLIENT;
            } else if (rst.getString("command").equals(Command.MSG_TO_DEVICE_FROM_ADMIN.getValue())) {
                this.command = Command.MSG_TO_DEVICE_FROM_ADMIN;
            } else if (rst.getString("command").equals(Command.MSG_TO_SERVER_TO_ADMIN.getValue())) {
                this.command = Command.MSG_TO_SERVER_TO_ADMIN;
            }
        } catch (Exception exe) {}
        try {
            this.data = rst.getString("data");
        } catch (Exception exe) {}
        try {
            this.dateReceived = rst.getTimestamp("date_received");
        } catch (Exception exe) {}
        try {
            this.dateSent = rst.getTimestamp("date_sent");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the clientEquipmentCommandId
     */
    public Integer getClientEquipmentCommandId() {
        return clientEquipmentCommandId;
    }

    /**
     * @param clientEquipmentCommandId the clientEquipmentCommandId to set
     */
    public void setClientEquipmentCommandId(Integer clientEquipmentCommandId) {
        this.clientEquipmentCommandId = clientEquipmentCommandId;
    }

    /**
     * @return the clientEquipmentId
     */
    public Integer getClientEquipmentId() {
        return clientEquipmentId;
    }

    /**
     * @param clientEquipmentId the clientEquipmentId to set
     */
    public void setClientEquipmentId(Integer clientEquipmentId) {
        this.clientEquipmentId = clientEquipmentId;
    }

    /**
     * @return the command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the dateSent
     */
    public Date getDateSent() {
        return dateSent;
    }

    /**
     * @param dateSent the dateSent to set
     */
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    /**
     * @return the dateReceived
     */
    public Date getDateReceived() {
        return dateReceived;
    }

    /**
     * @param dateReceived the dateReceived to set
     */
    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
            
            
}
