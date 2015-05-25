/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.controller.UserControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class Problemsolver extends OfficerCommunication implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;

    private Integer psId;
    private int clientId;
    private int userId;
    private Date psDate;
    private String problem;
    private String solution;
    private String schedulerInst;
    private String supervisorInst;
    private String dmInst;
    private String hrInst;
    private String postcomInst;
    private String officerInst;
    private String payrollInst;
    private String dispatchInst;
    private String checkinInst;
    private boolean privateCommunication;

    //Lazy loaded object
    private transient Client client;
    private transient User user;

    public Problemsolver() {
    }

    public Problemsolver(Integer psId) {
        this.psId = psId;
    }

    public Problemsolver(Integer psId, int clientId, int userId) {
        this.psId = psId;
        this.clientId = clientId;
        this.userId = userId;
    }

    public Problemsolver(Date currDate, Record_Set rst) {
        try {
            this.psId = rst.getInt("ps_id");
        } catch (Exception e) {
            this.psId = 0;
        }
        try {
            this.clientId = rst.getInt("client_id");
        } catch (Exception e) {
            this.clientId = 0;
        }
        try {
            this.userId = rst.getInt("user_id");
        } catch (Exception e) {
            this.userId = 0;
        }
        try {
            this.psDate = rst.getDate("ps_date");
        } catch (Exception e) {
            this.psDate = currDate;
        }
        try {
            this.problem = rst.getString("problem");
        } catch (Exception e) {
            this.problem = "";
        }
        try {
            this.solution = rst.getString("solution");
        } catch (Exception e) {
            this.solution = "";
        }
        try {
            this.schedulerInst = rst.getString("scheduler_inst");
        } catch (Exception e) {
            this.schedulerInst = "";
        }
        try {
            this.supervisorInst = rst.getString("supervisor_inst");
        } catch (Exception e) {
            this.supervisorInst = "";
        }
        try {
            this.dmInst = rst.getString("dm_inst");
        } catch (Exception e) {
            this.dmInst = "";
        }
        try {
            this.hrInst = rst.getString("hr_inst");
        } catch (Exception e) {
            this.hrInst = "";
        }
        try {
            this.postcomInst = rst.getString("postcom_inst");
        } catch (Exception e) {
            this.postcomInst = "";
        }
        try {
            this.officerInst = rst.getString("officer_inst");
        } catch (Exception e) {
            this.officerInst = "";
        }
        try {
            this.payrollInst = rst.getString("payroll_inst");
        } catch (Exception e) {
            this.payrollInst = "";
        }
        try {
            this.dispatchInst = rst.getString("dispatch_inst");
        } catch (Exception e) {
            this.dispatchInst = "";
        }
        try {
            this.checkinInst = rst.getString("checkin_inst");
        } catch (Exception e) {
            this.checkinInst = "";
        }
        try {
            this.privateCommunication = rst.getBoolean("private_communication");
        } catch (Exception e) {}
    }

    /**
     * Does this CC, contain either problem or any solutions, returns true if no
     * information is in these fields, false otherwise.
     * @return boolean
     */
    public boolean isEmpty() {
        boolean retVal = true;
        try {
            retVal = retVal && !(this.problem.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.solution.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.schedulerInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.supervisorInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.dmInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.hrInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.postcomInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.officerInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.payrollInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.dispatchInst.length() > 1);
        } catch (Exception e) {

        }
        try {
            retVal = retVal && !(this.checkinInst.length() > 1);
        } catch (Exception e) {

        }
        return retVal;
    }

    public Integer getPsId() {
        if (psId == null) {
            return 0;
        }
        return psId;
    }

    public void setPsId(Integer psId) {
        this.psId = psId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getPsDate() {
        if (psDate == null) {
            psDate = new Date();
        }
        return psDate;
    }

    public void setPsDate(Date psDate) {
        this.psDate = psDate;
    }

    public String getProblem() {
        if (problem == null) {
            return "";
        }
        return problem.trim();
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        if (solution == null) {
            return "";
        }
        return solution.trim();
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSchedulerInst() {
        if (schedulerInst == null) {
            return "";
        }
        return schedulerInst;
    }

    public void setSchedulerInst(String schedulerInst) {
        this.schedulerInst = schedulerInst;
    }

    public String getSupervisorInst() {
        if (supervisorInst == null) {
            return "";
        }
        return supervisorInst;
    }

    public void setSupervisorInst(String supervisorInst) {
        this.supervisorInst = supervisorInst;
    }

    public String getDmInst() {
        if (dmInst == null) {
            return "";
        }
        return dmInst;
    }

    public void setDmInst(String dmInst) {
        this.dmInst = dmInst;
    }

    public String getHrInst() {
        if (hrInst == null) {
            return "";
        }
        return hrInst;
    }

    public void setHrInst(String hrInst) {
        this.hrInst = hrInst;
    }

    public String getPostcomInst() {
        if (postcomInst == null) {
            return "";
        }
        return postcomInst;
    }

    public void setPostcomInst(String postcomInst) {
        this.postcomInst = postcomInst;
    }

    public String getOfficerInst() {
        if (officerInst == null) {
            return "";
        }
        return officerInst;
    }

    public void setOfficerInst(String officerInst) {
        this.officerInst = officerInst;
    }

    public String getPayrollInst() {
        if (payrollInst == null) {
            return "";
        }
        return payrollInst;
    }

    public void setPayrollInst(String payrollInst) {
        this.payrollInst = payrollInst;
    }

    public String getDispatchInst() {
        if (dispatchInst == null) {
            return "";
        }
        return dispatchInst;
    }

    public void setDispatchInst(String dispatchInst) {
        this.dispatchInst = dispatchInst;
    }

    public String getCheckinInst() {
        if (checkinInst == null) {
            return "";
        }
        return checkinInst;
    }

    public void setCheckinInst(String checkinInst) {
        this.checkinInst = checkinInst;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (psId != null ? psId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Problemsolver)) {
            return false;
        }
        Problemsolver other = (Problemsolver) object;
        if ((this.psId == null && other.psId != null) || (this.psId != null && !this.psId.equals(other.psId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.Problemsolver[psId=" + psId + "]";
    }

    public Client getClientObj(String companyId) {
        if (this.client == null) {
            ClientControllerInterface clientInterface = ControllerRegistryAbstract.getClientController(companyId);
            try {
                this.client = clientInterface.getClientById(clientId);
            } catch (Exception e) {
                System.out.println("Could not load Client. Error: " + e);
            }
        }
        return this.client;
    }

    public User getOriginatorObj(String companyId) {
        if (this.user == null) {
            UserControllerInterface userInterface = ControllerRegistryAbstract.getUserController(companyId);
            try {
                this.user = userInterface.getUserById(this.userId);
            } catch (Exception e) {
                System.out.println("Could not load Client. Error: " + e);
            }
        }
        return this.user;
    }

    /**
     * @return the privateCommunication
     */
    public boolean isPrivateCommunication() {
        return privateCommunication;
    }

    /**
     * @param privateCommunication the privateCommunication to set
     */
    public void setPrivateCommunication(boolean privateCommunication) {
        this.privateCommunication = privateCommunication;
    }

    @Override
    public Date getDateEntered() {
        return this.getPsDate();
    }

    @Override
    public String getType() {
        return "Corporate Communicator";
    }

    @Override
    public String getText() {
        return this.getOfficerInst();
    }

    @Override
    public Integer getEnteredBy() {
        return this.getUserId();
    }

    @Override
    public Integer getId() {
        return this.psId;
    }

    @Override
    public Integer getImageCount() {
        return 0;
    }

    @Override
    public ArrayList<ImageInterface> getImages(String companyId) {
        return new ArrayList<ImageInterface>();
    }
    
    @Override
    public int compareTo(Object obj) {
        int retval = -1;
        if (obj instanceof Problemsolver) {
            Problemsolver compto = (Problemsolver)obj;
            try {
                retval = -1 * this.psDate.compareTo(compto.getPsDate());
            } catch (Exception exe) {}
        }
        return retval;
    }
}
