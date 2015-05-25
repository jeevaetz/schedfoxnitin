/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model.helpers;

import java.util.List;

/**
 *
 * @author user
 */
public class GoogleJSONResult {

    private List<GoogleJSONAddress> results;
    private String status;

    /**
     * @return the result
     */
    public List<GoogleJSONAddress> getResults() {
        return results;
    }

    /**
     * @param result the result to set
     */
    public void setResults(List<GoogleJSONAddress> results) {
        this.results = results;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
