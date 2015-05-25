/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.TimeOffControllerInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Availability;
import schedfoxlib.model.EmployeeTypes;
import schedfoxlib.model.TimeOffAccrual;
import schedfoxlib.model.TimeOffAdjustment;
import schedfoxlib.model.TimeOffCalc;
import schedfoxlib.model.TimeOffSeries;
import schedfoxlib.model.TimeOffType;
import rmischeduleserver.mysqlconnectivity.queries.availability.get_availability_by_type_range_id_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_adjustments_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_assigned_series_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_available_series_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_time_off_accrual_for_series_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_time_off_accrual_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_time_off_types_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_assigned_timeoff_series_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_next_series_id_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.get_time_off_calcs_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.remove_series_from_type_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.remove_time_off_accrual_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.save_series_to_type_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.save_time_off_accrual_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.save_new_time_off_series_query;
import rmischeduleserver.mysqlconnectivity.queries.timeoff.save_timeoff_adjustment_query;

/**
 *
 * @author user
 */
public class TimeOffController implements TimeOffControllerInterface {

    private String companyId;

    private TimeOffController(String companyId) {
        this.companyId = companyId;
    }

    public static TimeOffController getInstance(String companyId) {
        return new TimeOffController(companyId);
    }

    @Override
    public String getCompanyId() {
        return this.companyId;
    }

    @Override
    public ArrayList<TimeOffSeries> getAssignedTimeOffSeries(EmployeeTypes selType, String timeoffType) throws RetrieveDataException {
        ArrayList<TimeOffSeries> retVal = new ArrayList<TimeOffSeries>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_assigned_timeoff_series_query query = new get_assigned_timeoff_series_query();
        query.setPreparedStatement(new Object[]{selType.getEmployeeTypeId(), timeoffType});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffSeries(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public void saveTimeOffAdjustment(TimeOffAdjustment timeOffAdjustment) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_timeoff_adjustment_query query = new save_timeoff_adjustment_query();
        query.update(timeOffAdjustment);
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public HashMap<Integer, Integer> getAdjustments(int employeeId) throws RetrieveDataException {
        HashMap<Integer, Integer> retVal = new HashMap<Integer, Integer>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_adjustments_query query = new get_adjustments_query();
        query.setPreparedStatement(new Object[]{employeeId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.put(rst.getInt("time_off_type_id"), rst.getInt("adjustment"));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<TimeOffCalc> getCalculationsForEmployee(Integer employeeId, Integer timeOffType) throws RetrieveDataException {
        ArrayList<TimeOffCalc> retVal = new ArrayList<TimeOffCalc>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_time_off_calcs_query query = new get_time_off_calcs_query();
        query.setPreparedStatement(new Object[]{employeeId, timeOffType});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffCalc(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Double calcCalcuationsForEmployee(ArrayList<TimeOffCalc> daysOffCalcs) throws RetrieveDataException {
        double retVal = 0;
        for (int p = 0; p < daysOffCalcs.size(); p++) {
            if (!daysOffCalcs.get(p).isIsExpired()) {
                double adjust = daysOffCalcs.get(p).getDaysAccrued() - daysOffCalcs.get(p).getDaysTaken().doubleValue();
                retVal += adjust;
            }
        }
        return retVal;
    }

    /**
     * This returns the number of days that the employee currently is entitled too.
     * @param employeeId
     * @param timeOffType
     * @return
     * @throws RetrieveDataException
     */
    @Override
    public Double getCurrentCalculationsForEmployee(Integer employeeId, Integer timeOffType) throws RetrieveDataException {
        ArrayList<TimeOffCalc> daysOffCalcs =
                this.getCalculationsForEmployee(employeeId, timeOffType);
        double retVal = 0;
        for (int p = 0; p < daysOffCalcs.size(); p++) {
            if (!daysOffCalcs.get(p).isIsExpired()) {
                double adjust = daysOffCalcs.get(p).getDaysAccrued() - daysOffCalcs.get(p).getDaysTaken().doubleValue();
                retVal += adjust;
            }
        }
        return retVal;
    }

    /**
     * Returns a list of availability
     */
    @Override
    public ArrayList<Availability> getAvailabilityForDateRange(Date startDate, Date endDate,
            Integer employeeId, Integer availType) throws RetrieveDataException {
        ArrayList<Availability> retVal = new ArrayList<Availability>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_availability_by_type_range_id_query query = new get_availability_by_type_range_id_query();
        query.setPreparedStatement(new Object[]{startDate, endDate, availType, employeeId});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Availability(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<TimeOffAccrual> getAccrualForSeries(TimeOffSeries series) throws RetrieveDataException {
        ArrayList<TimeOffAccrual> retVal = new ArrayList<TimeOffAccrual>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_time_off_accrual_for_series_query query = new get_time_off_accrual_for_series_query();
        query.setPreparedStatement(new Object[]{series.getTimeOffSeriesId()});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffAccrual(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<TimeOffAccrual> getTimeOff() throws RetrieveDataException {
        ArrayList<TimeOffAccrual> retVal = new ArrayList<TimeOffAccrual>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_time_off_accrual_query query = new get_time_off_accrual_query();
        query.setPreparedStatement(new Object[]{});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffAccrual(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void assignSeriesToType(ArrayList<Integer> type, TimeOffSeries accrual) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_series_to_type_query saveTypeQuery = new save_series_to_type_query();
        saveTypeQuery.update(type, accrual);
        saveTypeQuery.setCompany(companyId);

        try {
            conn.executeQuery(saveTypeQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void removeSeriesFromType(TimeOffType type, TimeOffSeries series) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        remove_series_from_type_query saveTypeQuery = new remove_series_from_type_query();
        saveTypeQuery.setPreparedStatement(new Object[]{
                    series.getTimeOffSeriesId(), type.getTimeOffTypeId()
                });
        saveTypeQuery.setCompany(companyId);

        try {
            conn.executeQuery(saveTypeQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void deleteAccrual(TimeOffAccrual accrual) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        remove_time_off_accrual_query saveTypeQuery = new remove_time_off_accrual_query();
        saveTypeQuery.setPreparedStatement(new Object[]{
                    accrual.getTimeOffAccrualId()
                });
        saveTypeQuery.setCompany(companyId);

        try {
            conn.executeQuery(saveTypeQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<TimeOffSeries> getAvailableSeries(TimeOffType type) throws RetrieveDataException {
        ArrayList<TimeOffSeries> retVal = new ArrayList<TimeOffSeries>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_available_series_query query = new get_available_series_query();
        query.setPreparedStatement(new Object[]{type.getTimeOffTypeId()});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffSeries(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public TimeOffSeries saveNewTimeOffSeries(TimeOffSeries timeOffSeries) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_next_series_id_query getSeriesQuery = new get_next_series_id_query();
        getSeriesQuery.setPreparedStatement(new Object[]{});
        getSeriesQuery.setCompany(companyId);

        int seriesId = -1;
        try {
            Record_Set rst = conn.executeQuery(getSeriesQuery, "");
            seriesId = rst.getInt("val");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        if (seriesId == -1) {
            throw new SaveDataException();
        }
        timeOffSeries.setTimeOffSeriesId(seriesId);
        save_new_time_off_series_query query = new save_new_time_off_series_query();
        query.update(timeOffSeries);
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        return timeOffSeries;
    }

    @Override
    public ArrayList<TimeOffSeries> getAssignedSeries(TimeOffType type) throws RetrieveDataException {
        ArrayList<TimeOffSeries> retVal = new ArrayList<TimeOffSeries>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_assigned_series_query query = new get_assigned_series_query();
        query.setPreparedStatement(new Object[]{type.getTimeOffTypeId()});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffSeries(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<TimeOffType> getTimeOffTypes() throws RetrieveDataException {
        ArrayList<TimeOffType> retVal = new ArrayList<TimeOffType>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_time_off_types_query timeoffTypeQuery = new get_time_off_types_query();
        timeoffTypeQuery.setPreparedStatement(new Object[]{});
        timeoffTypeQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(timeoffTypeQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TimeOffType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public void saveTimeOffAccrual(TimeOffAccrual timeOff) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_time_off_accrual_query saveAccrualQuery = new save_time_off_accrual_query();
        saveAccrualQuery.setPreparedStatement(new Object[]{});
        saveAccrualQuery.setCompany(companyId);
        try {
            saveAccrualQuery.update(timeOff);
            conn.executeUpdate(saveAccrualQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
}
