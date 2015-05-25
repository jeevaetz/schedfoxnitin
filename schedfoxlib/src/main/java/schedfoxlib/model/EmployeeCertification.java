/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class EmployeeCertification extends Certification {

    private Date certAcquired;
    private Date certExpired;

    private Calendar calendarAcquired;
    private Calendar calendarExpired;

    private boolean hasCert;

    private static SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EmployeeCertification() {
        
    }
            
    public EmployeeCertification(Record_Set rst) {
        super(rst);

        if (rst.getString("iscert") != null) {
            try {
                this.setHasCert(rst.getBoolean("iscert"));
            } catch (Exception e) {}
        }

        if (rst.getString("acquired") != null) {
            try {
                this.certAcquired = myFormat.parse(rst.getString("acquired"));
                this.calendarAcquired = Calendar.getInstance();
                this.calendarAcquired.setTime(certAcquired);
            } catch (Exception e) {}
        }
        if (rst.getString("expired") != null) {
            try {
                this.certExpired = myFormat.parse(rst.getString("expired"));
                this.calendarExpired = Calendar.getInstance();
                this.calendarExpired.setTime(certExpired);
            } catch (Exception e) {
                //Problem setting expired, probably never set.
                try {
                    String defaultRenewal = super.getCertification_default_renewal_time();
                    String[] renewalArray = defaultRenewal.split(":");
                    this.calendarExpired = Calendar.getInstance();
                    try {
                        int numYears = Integer.parseInt(renewalArray[0]);
                        this.calendarExpired.add(Calendar.YEAR, numYears);
                    } catch (Exception exe) {}
                    try {
                        int numMonths = Integer.parseInt(renewalArray[0]);
                        this.calendarExpired.add(Calendar.MONTH, numMonths);
                    } catch (Exception exe) {}
                    try {
                        int numDays = Integer.parseInt(renewalArray[0]);
                        this.calendarExpired.add(Calendar.DAY_OF_MONTH, numDays);
                    } catch (Exception exe) {}
                    this.certExpired = this.calendarExpired.getTime();
                } catch (Exception exe) {}

            }
        }
    }

    /**
     * @return the certAcquired
     */
    public Date getCertAcquired() {
        return certAcquired;
    }

    /**
     * @param certAcquired the certAcquired to set
     */
    public void setCertAcquired(Date certAcquired) {
        this.certAcquired = certAcquired;
    }

    /**
     * @return the certExpired
     */
    public Date getCertExpired() {
        return certExpired;
    }

    /**
     * @param certExpired the certExpired to set
     */
    public void setCertExpired(Date certExpired) {
        this.certExpired = certExpired;
    }

    /**
     * @return the calendarAcquired
     */
    public Calendar getCalendarAcquired() {
        return calendarAcquired;
    }

    /**
     * @param calendarAcquired the calendarAcquired to set
     */
    public void setCalendarAcquired(Calendar calendarAcquired) {
        this.calendarAcquired = calendarAcquired;
    }

    /**
     * @return the calendarExpired
     */
    public Calendar getCalendarExpired() {
        return calendarExpired;
    }

    /**
     * @param calendarExpired the calendarExpired to set
     */
    public void setCalendarExpired(Calendar calendarExpired) {
        this.calendarExpired = calendarExpired;
    }

    /**
     * @return the hasCert
     */
    public boolean isHasCert() {
        return hasCert;
    }

    /**
     * @param hasCert the hasCert to set
     */
    public void setHasCert(boolean hasCert) {
        this.hasCert = hasCert;
    }
}
