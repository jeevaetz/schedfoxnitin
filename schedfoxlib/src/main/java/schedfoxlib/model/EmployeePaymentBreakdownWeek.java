/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ira
 */
public class EmployeePaymentBreakdownWeek {

    private ArrayList<EmployeePaymentBreakdown> breakdowns;
    private Date startDate;

    //Lazy loaded objects
    private EmployeePayments payment;
    private ArrayList<EmployeePaymentDeduction> deductions;
    private ArrayList<EmployeePaymentTaxes> taxes;

    public EmployeePaymentBreakdownWeek() {
        breakdowns = new ArrayList<EmployeePaymentBreakdown>();
        deductions = new ArrayList<EmployeePaymentDeduction>();
        taxes = new ArrayList<EmployeePaymentTaxes>();
    }
    
    public EmployeePaymentBreakdownWeek(Date startDate) {
        breakdowns = new ArrayList<EmployeePaymentBreakdown>();
        deductions = new ArrayList<EmployeePaymentDeduction>();
        taxes = new ArrayList<EmployeePaymentTaxes>();
        this.startDate = startDate;
    }

    /**
     * @return the breakdowns
     */
    public ArrayList<EmployeePaymentBreakdown> getBreakdowns() {
        return breakdowns;
    }

    /**
     * @param breakdowns the breakdowns to set
     */
    public void setBreakdowns(ArrayList<EmployeePaymentBreakdown> breakdowns) {
        this.breakdowns = breakdowns;
    }

    /**
     * @return the payment
     */
    public EmployeePayments getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(EmployeePayments payment) {
        this.payment = payment;
    }
    
    public Double getRegularHoursOnDay(int dayOfWeek) {
        double retVal = 0.0;
        Calendar myStartDate = Calendar.getInstance();
        for (int b = 0; b < breakdowns.size(); b++) {
            try {
                myStartDate.setTime(breakdowns.get(b).getWork_date());
                if (myStartDate.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                    retVal += breakdowns.get(b).getReg_pay_hours();
                }
            } catch (Exception exe) {}
        }
        return retVal;
    }
    
    public Double getOvertimeHoursOnDay(int dayOfWeek) {
        double retVal = 0.0;
        Calendar myStartDate = Calendar.getInstance();
        for (int b = 0; b < breakdowns.size(); b++) {
            try {
                myStartDate.setTime(breakdowns.get(b).getWork_date());
                if (myStartDate.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                    retVal += breakdowns.get(b).getOver_pay_hours();
                }
            } catch (Exception exe) {}
        }
        return retVal;
    }
    
    public Double getDoubleHoursOnDay(int dayOfWeek) {
        double retVal = 0.0;
        Calendar myStartDate = Calendar.getInstance();
        for (int b = 0; b < breakdowns.size(); b++) {
            try {
                myStartDate.setTime(breakdowns.get(b).getWork_date());
                if (myStartDate.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                    retVal += breakdowns.get(b).getDbl_pay_hours();
                }
            } catch (Exception exe) {}
        }
        return retVal;
    }

    public Double calcGrossPay() {
        double retVal = 0.0;
        for (int b = 0; b < breakdowns.size(); b++) {
            try {
                retVal += breakdowns.get(b).getReg_pay_amount() + breakdowns.get(b).getDbl_pay_amount() + breakdowns.get(b).getOver_pay_amount();
            } catch (Exception exe) {
            }
        }
        return retVal;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the deductions
     */
    public ArrayList<EmployeePaymentDeduction> getDeductions() {
        return deductions;
    }

    /**
     * @param deductions the deductions to set
     */
    public void setDeductions(ArrayList<EmployeePaymentDeduction> deductions) {
        this.deductions = deductions;
    }

    /**
     * @return the taxes
     */
    public ArrayList<EmployeePaymentTaxes> getTaxes() {
        return taxes;
    }

    /**
     * @param taxes the taxes to set
     */
    public void setTaxes(ArrayList<EmployeePaymentTaxes> taxes) {
        this.taxes = taxes;
    }
}
