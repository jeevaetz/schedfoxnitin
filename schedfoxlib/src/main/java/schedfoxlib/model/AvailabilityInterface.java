/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author user
 */
public interface AvailabilityInterface {
    public int getAvailabilityId();
    public int getStartTime();
    public int getEndTime();
    public BigDecimal getHoursCompensated();

    /**
     * @return the isDeleted
     */
    public boolean isIsDeleted();

    public int getAvailType();

    public boolean hasNote();

    public String getAvailIdStr();

    public Date getDateRequested();

    public Employee getEmployeeObj(String companyId);

    public User getUserCreatedBy(String companyId);

    public boolean isIsMaster();
}
