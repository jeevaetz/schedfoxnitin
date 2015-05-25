/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

/**
 *
 * @author ira
 */
public class GoogleURLModel {
    private String kind;
    private String id;
    private String longUrl;
    
    public GoogleURLModel() {
        
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the longUrl
     */
    public String getLongUrl() {
        return longUrl;
    }

    /**
     * @param longUrl the longUrl to set
     */
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
