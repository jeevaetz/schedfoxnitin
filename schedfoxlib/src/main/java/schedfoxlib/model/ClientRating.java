/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientRating implements Serializable {
    private Integer clientRatingId;
    private Integer clientId;
    private Integer UserId;
    private Date dateOfRating;
    private Integer customer_rating;

    public ClientRating() {
        customer_rating = 0;
        clientId = 0;
        clientRatingId = 0;
        dateOfRating = new Date();
        UserId = 0;
    }

    public ClientRating(Record_Set rst) {
        try {
            this.clientRatingId = rst.getInt("client_rating_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.clientId = rst.getInt("client_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.UserId = rst.getInt("user_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.dateOfRating = rst.getDate("date_of_rating");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.customer_rating = rst.getInt("customer_rating");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the clientRatingId
     */
    public Integer getClientRatingId() {
        return clientRatingId;
    }

    /**
     * @param clientRatingId the clientRatingId to set
     */
    public void setClientRatingId(Integer clientRatingId) {
        this.clientRatingId = clientRatingId;
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

    /**
     * @return the UserId
     */
    public Integer getUserId() {
        return UserId;
    }

    /**
     * @param UserId the UserId to set
     */
    public void setUserId(Integer UserId) {
        this.UserId = UserId;
    }

    /**
     * @return the dateOfRating
     */
    public Date getDateOfRating() {
        return dateOfRating;
    }

    /**
     * @param dateOfRating the dateOfRating to set
     */
    public void setDateOfRating(Date dateOfRating) {
        this.dateOfRating = dateOfRating;
    }

    /**
     * @return the customer_rating
     */
    public Integer getCustomer_rating() {
        return customer_rating;
    }

    /**
     * @param customer_rating the customer_rating to set
     */
    public void setCustomer_rating(Integer customer_rating) {
        this.customer_rating = customer_rating;
    }
}
