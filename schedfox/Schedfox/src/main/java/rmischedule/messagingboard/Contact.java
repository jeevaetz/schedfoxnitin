/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messagingboard;

import java.util.Vector;

/**
 *
 * @author vnguyen
 */
public class Contact implements Comparable{

    private String userId;
    private String companyId;
    private String firstName;
    private String lastName;

    public Contact(String userId, String companyId, String firstName, String lastName) {
        this.userId = userId;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int compareTo(Object o) {
        if (o instanceof Contact) {
            Contact comparing = (Contact) o;
            return this.firstName.compareTo(comparing.firstName);
        } else {
            return -1;
        }
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String toString() {
        StringBuilder a = new StringBuilder();
        a.append(this.firstName);
        a.append(" " + this.lastName +";");
        return a.toString();
    }
}
