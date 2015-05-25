/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author user
 */
public abstract class OfficerCommunication implements Comparable, Serializable {
    public abstract Date getDateEntered();
    public abstract String getType();
    public abstract String getText();
    public abstract Integer getEnteredBy();
    public abstract Integer getId();
    public abstract Integer getImageCount();
    public abstract ArrayList<ImageInterface> getImages(String companyId);
    
    @Override
    public int compareTo(Object obj) {
        if (obj instanceof OfficerCommunication) {
            OfficerCommunication officerComm = (OfficerCommunication)obj;
            Date myDate = this.getDateEntered();
            Date theirDate = officerComm.getDateEntered();
            if (myDate != null && theirDate != null) {
                return myDate.compareTo(theirDate);
            } else {
                return 0;
            } 
        } else {
            return 0;
        }
    }
}
