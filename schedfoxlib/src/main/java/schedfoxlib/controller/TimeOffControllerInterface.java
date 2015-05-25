/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public interface TimeOffControllerInterface {

    void assignSeriesToType(ArrayList<Integer> type, TimeOffSeries accrual) throws SaveDataException;

    Double calcCalcuationsForEmployee(ArrayList<TimeOffCalc> daysOffCalcs) throws RetrieveDataException;

    void deleteAccrual(TimeOffAccrual accrual) throws SaveDataException;

    ArrayList<TimeOffAccrual> getAccrualForSeries(TimeOffSeries series) throws RetrieveDataException;

    HashMap<Integer, Integer> getAdjustments(int employeeId) throws RetrieveDataException;

    ArrayList<TimeOffSeries> getAssignedSeries(TimeOffType type) throws RetrieveDataException;

    ArrayList<TimeOffSeries> getAssignedTimeOffSeries(EmployeeTypes selType, String timeoffType) throws RetrieveDataException;

    /**
     * Returns a list of availability
     */
    ArrayList<Availability> getAvailabilityForDateRange(Date startDate, Date endDate, Integer employeeId, Integer availType) throws RetrieveDataException;

    ArrayList<TimeOffSeries> getAvailableSeries(TimeOffType type) throws RetrieveDataException;

    ArrayList<TimeOffCalc> getCalculationsForEmployee(Integer employeeId, Integer timeOffType) throws RetrieveDataException;

    String getCompanyId();

    /**
     * This returns the number of days that the employee currently is entitled too.
     * @param employeeId
     * @param timeOffType
     * @return
     * @throws RetrieveDataException
     */
    Double getCurrentCalculationsForEmployee(Integer employeeId, Integer timeOffType) throws RetrieveDataException;

    ArrayList<TimeOffAccrual> getTimeOff() throws RetrieveDataException;

    ArrayList<TimeOffType> getTimeOffTypes() throws RetrieveDataException;

    void removeSeriesFromType(TimeOffType type, TimeOffSeries series) throws SaveDataException;

    TimeOffSeries saveNewTimeOffSeries(TimeOffSeries timeOffSeries) throws SaveDataException;

    void saveTimeOffAccrual(TimeOffAccrual timeOff) throws SaveDataException;

    void saveTimeOffAdjustment(TimeOffAdjustment timeOffAdjustment) throws SaveDataException;
    
}
