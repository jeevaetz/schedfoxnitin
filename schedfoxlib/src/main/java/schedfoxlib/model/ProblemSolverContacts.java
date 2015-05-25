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
 * @author user
 */
public class ProblemSolverContacts implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer problemSolverContactsId;
    private int contactId;
    private String contactType;
    private Integer problemSolverContactId;
    private Date messageDeliveredDate;

    //Lazy loaded objects
    private ProblemSolverContact problemSolverContact;

    public ProblemSolverContacts() {
    }

    public ProblemSolverContacts(Integer problemSolverContactsId) {
        this.problemSolverContactsId = problemSolverContactsId;
    }

    public ProblemSolverContacts(Integer problemSolverContactsId, int contactId, String contactType) {
        this.problemSolverContactsId = problemSolverContactsId;
        this.contactId = contactId;
        this.contactType = contactType;
    }

    public ProblemSolverContacts(Record_Set rst) {
        try {
            this.problemSolverContactsId = rst.getInt("problem_solver_contacts_id");
        } catch (Exception e) {}
        try {
            this.problemSolverContactId = rst.getInt("problem_solver_contact_id");
        } catch (Exception e) {}
        try {
            this.contactId = rst.getInt("contact_id");
        } catch (Exception e) {}
        try {
            this.contactType = rst.getString("contact_type");
        } catch (Exception e) {}
        try {
            this.messageDeliveredDate = rst.getDate("message_delivered_date");
        } catch (Exception e) {}
    }

    public Integer getProblemSolverContactsId() {
        return problemSolverContactsId;
    }

    public void setProblemSolverContactsId(Integer problemSolverContactsId) {
        this.problemSolverContactsId = problemSolverContactsId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    /**
     * @return the problemSolverContactId
     */
    public Integer getProblemSolverContactId() {
        return problemSolverContactId;
    }

    /**
     * @param problemSolverContactId the problemSolverContactId to set
     */
    public void setProblemSolverContactId(Integer problemSolverContactId) {
        this.problemSolverContactId = problemSolverContactId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (problemSolverContactsId != null ? problemSolverContactsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProblemSolverContacts)) {
            return false;
        }
        ProblemSolverContacts other = (ProblemSolverContacts) object;
        if ((this.problemSolverContactsId == null && other.problemSolverContactsId != null) || (this.problemSolverContactsId != null && !this.problemSolverContactsId.equals(other.problemSolverContactsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ProblemSolverContacts[problemSolverContactsId=" + problemSolverContactsId + "]";
    }

    /**
     * @return the messageDeliveredDate
     */
    public Date getMessageDeliveredDate() {
        return messageDeliveredDate;
    }

    /**
     * @param messageDeliveredDate the messageDeliveredDate to set
     */
    public void setMessageDeliveredDate(Date messageDeliveredDate) {
        this.messageDeliveredDate = messageDeliveredDate;
    }

}
