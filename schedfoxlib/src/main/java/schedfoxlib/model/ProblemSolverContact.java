/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.ProblemSolverControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class ProblemSolverContact implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer problemSolverContactId;
    private Integer psId;
    private Date contactDate;
    private ArrayList<ProblemSolverContacts> problemSolverContacts;

    public ProblemSolverContact() {
    }

    public ProblemSolverContact(Record_Set rst) {
        try {
            this.problemSolverContactId = rst.getInt("problem_solver_contact_id");
        } catch (Exception e) {
        }
        try {
            this.psId = rst.getInt("ps_id");
        } catch (Exception e) {
        }
        try {
            this.contactDate = rst.getDate("contact_date");
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ProblemSolverContact[problemSolverContactId=" + getProblemSolverContactId() + "]";
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

    /**
     * @return the psId
     */
    public Integer getPsId() {
        return psId;
    }

    /**
     * @param psId the psId to set
     */
    public void setPsId(Integer psId) {
        this.psId = psId;
    }

    /**
     * @return the contactDate
     */
    public Date getContactDate() {
        return contactDate;
    }

    /**
     * @param contactDate the contactDate to set
     */
    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public ArrayList<ProblemSolverContacts> getContacts(String companyId) {
        ArrayList<ProblemSolverContacts> retVal = new ArrayList<ProblemSolverContacts>();
        ProblemSolverControllerInterface problemSolverInterface = ControllerRegistryAbstract.getProblemSolverController(companyId);
        try {
            retVal = problemSolverInterface.getProblemSolverContactById(problemSolverContactId);
        } catch (Exception exe) {
            retVal = new ArrayList<ProblemSolverContacts>();
        }
        return retVal;
    }

    public static Integer getNextPrimaryId(String companyId) {
        Integer retVal = new Integer(0);
        ProblemSolverControllerInterface problemSolverInterface = ControllerRegistryAbstract.getProblemSolverController(companyId);
        try {
            retVal = problemSolverInterface.getNextPrimaryId();
        } catch (Exception exe) {
            
        }
        return retVal;
    }
}
