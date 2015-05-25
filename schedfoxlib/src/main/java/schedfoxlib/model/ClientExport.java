/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

/**
 *
 * @author user
 */
public class ClientExport {
    private int uskedClientId;
    private int clientId;
    private int clientBranch;
    private String uskedCliId;
    private String uskedWsId;

    public ClientExport() {

    }
    
    /**
     * @return the uskedClientId
     */
    public int getUskedClientId() {
        return uskedClientId;
    }

    /**
     * @param uskedClientId the uskedClientId to set
     */
    public void setUskedClientId(int uskedClientId) {
        this.uskedClientId = uskedClientId;
    }

    /**
     * @return the clientId
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientBranch
     */
    public int getClientBranch() {
        return clientBranch;
    }

    /**
     * @param clientBranch the clientBranch to set
     */
    public void setClientBranch(int clientBranch) {
        this.clientBranch = clientBranch;
    }

    /**
     * @return the uskedCliId
     */
    public String getUskedCliId() {
        return uskedCliId;
    }

    /**
     * @param uskedCliId the uskedCliId to set
     */
    public void setUskedCliId(String uskedCliId) {
        this.uskedCliId = uskedCliId;
    }

    /**
     * @return the uskedWsId
     */
    public String getUskedWsId() {
        return uskedWsId;
    }

    /**
     * @param uskedWsId the uskedWsId to set
     */
    public void setUskedWsId(String uskedWsId) {
        this.uskedWsId = uskedWsId;
    }
    
}
