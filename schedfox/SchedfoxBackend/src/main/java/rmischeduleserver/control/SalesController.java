/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.data_connection_types.ServerSideConnection;
import rmischeduleserver.mysqlconnectivity.queries.sales.deactivate_sales_itinerary_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_deleted_sales_expense_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_itinerary_by_external_gid_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_next_expense_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_call_logs_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_expense_image_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_expense_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_expense_types_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_images_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_itinerary_for_month_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.get_sales_itinerary_types_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.save_sales_calls_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.save_sales_expense_image_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.save_sales_expense_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.save_sales_itinerary_for_user_query;
import rmischeduleserver.mysqlconnectivity.queries.sales.save_sales_itinerary_query;
import schedfoxlib.controller.SalesControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.SalesCall;
import schedfoxlib.model.SalesExpense;
import schedfoxlib.model.SalesExpenseImage;
import schedfoxlib.model.SalesExpenseType;
import schedfoxlib.model.SalesItinerary;
import schedfoxlib.model.SalesItineraryType;
import schedfoxlib.model.User;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesController implements SalesControllerInterface {

    private static SalesController myInstance;
    private String companyId;

    private SalesController(String companyId) {
        this.companyId = companyId;
    }

    public static SalesController getInstance(String companyId) {
        if (myInstance == null) {
            myInstance = new SalesController(companyId);
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            try {
                conn.setConnectionObject(new ServerSideConnection());
            } catch (Exception e) {
                System.out.println("Could not set up server!");
            }
        }
        return myInstance;
    }
    
    @Override
    public ArrayList<SalesItinerary> getSalesItineraryForUserId(int userId) throws RetrieveDataException {
        ArrayList<SalesItinerary> retVal = new ArrayList<SalesItinerary>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            save_sales_itinerary_for_user_query viewQuery = new save_sales_itinerary_for_user_query();
            viewQuery.setPreparedStatement(new Object[]{userId});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesItinerary(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<SalesItineraryType> getSalesItineraryTypes() throws RetrieveDataException {
        ArrayList<SalesItineraryType> retVal = new ArrayList<SalesItineraryType>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            get_sales_itinerary_types_query viewQuery = new get_sales_itinerary_types_query();
            viewQuery.setPreparedStatement(new Object[]{});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesItineraryType(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
            
    @Override
    public ArrayList<SalesExpenseType> getSalesExpenseTypes() throws RetrieveDataException {
        ArrayList<SalesExpenseType> retVal = new ArrayList<SalesExpenseType>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            get_sales_expense_types_query viewQuery = new get_sales_expense_types_query();
            viewQuery.setPreparedStatement(new Object[]{});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesExpenseType(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<SalesExpense> getSalesExpense(Integer userId) throws RetrieveDataException {
        ArrayList<SalesExpense> retVal = new ArrayList<SalesExpense>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            get_sales_expense_query viewQuery = new get_sales_expense_query();
            viewQuery.setPreparedStatement(new Object[]{userId});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesExpense(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    @Override
    public Integer saveSalesExpense(SalesExpense sales) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            boolean isInsert = false;
            if (sales.getSalesExpenseId() == null) {
                get_next_expense_seq_query seqQuery = new get_next_expense_seq_query();
                seqQuery.setCompany(companyId);
                seqQuery.setPreparedStatement(new Object[]{});
                Record_Set rst = conn.executeQuery(seqQuery, "");
                sales.setSalesExpenseId(rst.getInt(0));
                isInsert = true;
            }
            save_sales_expense_query viewQuery = new save_sales_expense_query();
            viewQuery.update(sales, isInsert);
            viewQuery.setCompany(companyId);
            conn.executeUpdate(viewQuery, "");
            
            return sales.getSalesExpenseId();
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveCallLogs(ArrayList<SalesCall> calls) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            save_sales_calls_query viewQuery = new save_sales_calls_query();
            for (int c = 0; c < calls.size(); c++) {
                SalesCall call = calls.get(c);
                viewQuery.update(call);
                viewQuery.setCompany(companyId);
                conn.executeUpdate(viewQuery, "");
            }
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<SalesCall> getCallLogs(Integer userId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<SalesCall> retVal = new ArrayList<SalesCall>();
        try {
            get_sales_call_logs_query query = new get_sales_call_logs_query();
            query.setPreparedStatement(new Object[]{userId});
            query.setCompany(companyId);
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesCall(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public SalesItinerary getSalesItineraryByExternalGid(String externalGid, Integer userId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            SalesItinerary retVal = new SalesItinerary();
            get_itinerary_by_external_gid_query query = new get_itinerary_by_external_gid_query();
            query.setPreparedStatement(new Object[]{externalGid, userId});
            query.setCompany(companyId);
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new SalesItinerary(rst);
                rst.moveNext();
            }
            return retVal;
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }
    
    public void deactivateAppointments(User user, ArrayList<String> apptGuids) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            deactivate_sales_itinerary_query query = new deactivate_sales_itinerary_query();
            query.update(apptGuids, user);
            query.setCompany(companyId);
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public void saveSalesItinerary(SalesItinerary itinerary) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            save_sales_itinerary_query query = new save_sales_itinerary_query();
            query.update(itinerary);
            query.setCompany(companyId);
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<SalesItinerary> getSalesItineraryForMonth(Integer userId, Date monthDate) throws RetrieveDataException {
        ArrayList<SalesItinerary> retVal = new ArrayList<SalesItinerary>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {           
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            get_sales_itinerary_for_month_query viewQuery = new get_sales_itinerary_for_month_query();
            viewQuery.setPreparedStatement(new Object[]{userId, myFormat.format(monthDate), myFormat.format(monthDate)});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesItinerary(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void saveSalesImage(SalesExpenseImage image) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            save_sales_expense_image_query viewQuery = new save_sales_expense_image_query();
            viewQuery.update(image);
            viewQuery.setCompany(companyId);
            conn.executeUpdate(viewQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public SalesExpenseImage getExpenseImage(Integer imageId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        SalesExpenseImage retVal = new SalesExpenseImage();
        try {
            get_sales_expense_image_query viewQuery = new get_sales_expense_image_query();
            viewQuery.setPreparedStatement(new Object[]{imageId});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new SalesExpenseImage(rst);
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<SalesExpenseImage> getImagesForExpense(Integer expense, boolean loadByteData) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<SalesExpenseImage> retVal = new ArrayList<SalesExpenseImage>();
        try {
            get_sales_images_query viewQuery = new get_sales_images_query();
            viewQuery.setPreparedStatement(new Object[]{expense});
            viewQuery.update(loadByteData);
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesExpenseImage(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<SalesExpense> getSalesExpense(Integer userId, boolean isDeleted) throws RetrieveDataException {
        ArrayList<SalesExpense> retVal = new ArrayList<SalesExpense>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            get_deleted_sales_expense_query viewQuery = new get_deleted_sales_expense_query();
            viewQuery.setPreparedStatement(new Object[]{userId, isDeleted, isDeleted});
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new SalesExpense(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
