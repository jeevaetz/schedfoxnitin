/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

/**
 *
 * @author ira
 */
public class DocClass {

    private String fullDocName;
    private String docName;
    private String clientName;
    private Integer clientId;

    /**
     * @return the fullDocName
     */
    public String getFullDocName() {
        return fullDocName;
    }

    /**
     * @param fullDocName the fullDocName to set
     */
    public void setFullDocName(String fullDocName) {
        this.fullDocName = fullDocName;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

}
