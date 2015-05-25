/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;

/**
 *
 * @author user
 */

public class ClientPhoneCallsToProblemSolverPK implements Serializable {

    private int clientPhoneCallId;
    private int problemSolverId;

    public ClientPhoneCallsToProblemSolverPK() {
    }

    public ClientPhoneCallsToProblemSolverPK(int clientPhoneCallId, int problemSolverId) {
        this.clientPhoneCallId = clientPhoneCallId;
        this.problemSolverId = problemSolverId;
    }

    public int getClientPhoneCallId() {
        return clientPhoneCallId;
    }

    public void setClientPhoneCallId(int clientPhoneCallId) {
        this.clientPhoneCallId = clientPhoneCallId;
    }

    public int getProblemSolverId() {
        return problemSolverId;
    }

    public void setProblemSolverId(int problemSolverId) {
        this.problemSolverId = problemSolverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) clientPhoneCallId;
        hash += (int) problemSolverId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientPhoneCallsToProblemSolverPK)) {
            return false;
        }
        ClientPhoneCallsToProblemSolverPK other = (ClientPhoneCallsToProblemSolverPK) object;
        if (this.clientPhoneCallId != other.clientPhoneCallId) {
            return false;
        }
        if (this.problemSolverId != other.problemSolverId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientPhoneCallsToProblemSolverPK[clientPhoneCallId=" + clientPhoneCallId + ", problemSolverId=" + problemSolverId + "]";
    }

}
