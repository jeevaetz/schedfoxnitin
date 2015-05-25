/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */

public class TestToClient implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer testToClientId;
    private int clientId;
    private int testId;

    public TestToClient() {
    }

    public TestToClient(Record_Set rst) {
        this.testToClientId = rst.getInt("test_to_client_id");
        this.clientId = rst.getInt("client_id");
        this.testId = rst.getInt("test_id");
    }

    public TestToClient(Integer testToClientId) {
        this.testToClientId = testToClientId;
    }

    public TestToClient(Integer testToClientId, int clientId, int testId) {
        this.testToClientId = testToClientId;
        this.clientId = clientId;
        this.testId = testId;
    }

    public Integer getTestToClientId() {
        return testToClientId;
    }

    public void setTestToClientId(Integer testToClientId) {
        this.testToClientId = testToClientId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testToClientId != null ? testToClientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestToClient)) {
            return false;
        }
        TestToClient other = (TestToClient) object;
        if ((this.testToClientId == null && other.testToClientId != null) || (this.testToClientId != null && !this.testToClientId.equals(other.testToClientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedfox.parsetesting.dao.TestToClient[testToClientId=" + testToClientId + "]";
    }

}
