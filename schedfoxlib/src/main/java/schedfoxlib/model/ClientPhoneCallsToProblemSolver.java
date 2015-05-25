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
public class ClientPhoneCallsToProblemSolver implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected ClientPhoneCallsToProblemSolverPK clientPhoneCallsToProblemSolverPK;

    public ClientPhoneCallsToProblemSolver() {
    }

    public ClientPhoneCallsToProblemSolver(ClientPhoneCallsToProblemSolverPK clientPhoneCallsToProblemSolverPK) {
        this.clientPhoneCallsToProblemSolverPK = clientPhoneCallsToProblemSolverPK;
    }

    public ClientPhoneCallsToProblemSolver(int clientPhoneCallId, int problemSolverId) {
        this.clientPhoneCallsToProblemSolverPK = new ClientPhoneCallsToProblemSolverPK(clientPhoneCallId, problemSolverId);
    }

    public ClientPhoneCallsToProblemSolverPK getClientPhoneCallsToProblemSolverPK() {
        return clientPhoneCallsToProblemSolverPK;
    }

    public void setClientPhoneCallsToProblemSolverPK(ClientPhoneCallsToProblemSolverPK clientPhoneCallsToProblemSolverPK) {
        this.clientPhoneCallsToProblemSolverPK = clientPhoneCallsToProblemSolverPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientPhoneCallsToProblemSolverPK != null ? clientPhoneCallsToProblemSolverPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientPhoneCallsToProblemSolver)) {
            return false;
        }
        ClientPhoneCallsToProblemSolver other = (ClientPhoneCallsToProblemSolver) object;
        if ((this.clientPhoneCallsToProblemSolverPK == null && other.clientPhoneCallsToProblemSolverPK != null) || (this.clientPhoneCallsToProblemSolverPK != null && !this.clientPhoneCallsToProblemSolverPK.equals(other.clientPhoneCallsToProblemSolverPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientPhoneCallsToProblemSolver[clientPhoneCallsToProblemSolverPK=" + clientPhoneCallsToProblemSolverPK + "]";
    }

}
