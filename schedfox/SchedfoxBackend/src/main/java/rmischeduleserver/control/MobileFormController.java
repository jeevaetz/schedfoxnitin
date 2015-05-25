/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_all_mobile_form_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_all_mobile_forms;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_associated_mobile_forms_count_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_associated_mobile_forms_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_client_ids_by_mobile_id_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_form_data_fillout_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_form_fillout_for_client_with_delete_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_form_fillout_for_form_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_form_fillout_for_form_with_delete_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_form_fillout_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_forms_with_data_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_global_mobile_form_fillouts;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_max_scans_for_shift_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_daily_forms_for_client;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_data;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_data_sequence_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_data_types;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_data_with_all_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_fillout_by_dates_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_sequence_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_form_types;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_forms;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_forms_for_client;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_mobile_immediate_forms_for_client;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_non_sent_immediate_form_fillout_for_form_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.get_recent_form_fillout_for_client_employee_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.save_form_fillout_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.save_mobile_form_data_fillout_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.save_mobile_form_data_ordering_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.save_mobile_form_data_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.save_mobile_form_query;
import rmischeduleserver.mysqlconnectivity.queries.mobileform.save_mobile_form_to_clients_query;
import schedfoxlib.controller.MobileFormInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.GlobalMobileFormFillout;
import schedfoxlib.model.MobileFormData;
import schedfoxlib.model.MobileFormDataFillout;
import schedfoxlib.model.MobileFormDataSearch;
import schedfoxlib.model.MobileFormDataType;
import schedfoxlib.model.MobileFormFillout;
import schedfoxlib.model.MobileFormFilloutRptScan;
import schedfoxlib.model.MobileForms;
import schedfoxlib.model.MobileFormsType;
import schedfoxlib.model.MobileReportEmployeeGraphData;
import schedfoxlib.model.MobileReportGraphData;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileFormController implements MobileFormInterface {

    private String companyId;

    private MobileFormController(String companyId) {
        this.companyId = companyId;
    }

    public static MobileFormController getInstance(String companyId) {
        return new MobileFormController(companyId);
    }
    
    public ArrayList<MobileForms> getFormsWithJasper() throws RetrieveDataException  {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> retVal = new ArrayList<MobileForms>();
        
        get_forms_with_data_query query = new get_forms_with_data_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});
        
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }
    
    public Integer getAssociatedFormsCount(Integer formDataTypeId, String dataToSearch) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        
        get_associated_mobile_forms_count_query query = new get_associated_mobile_forms_count_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formDataTypeId, Integer.parseInt(this.companyId), dataToSearch});
        
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal = rst.getInt(0);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }
    
    public ArrayList<MobileForms> getAssociatedForms(Integer formDataTypeId, String dataToSearch) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> retVal = new ArrayList<MobileForms>();
        
        get_associated_mobile_forms_query query = new get_associated_mobile_forms_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formDataTypeId, Integer.parseInt(this.companyId), dataToSearch});
        
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }

    public ArrayList<MobileFormFillout> getFormFillout(Integer mobileFormId, Date startDate, Date endDate, Integer clientId) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> retVal = new ArrayList<MobileFormFillout>();
        
        get_mobile_form_fillout_by_dates_query query = new get_mobile_form_fillout_by_dates_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{mobileFormId, new java.sql.Timestamp(startDate.getTime()), new java.sql.Timestamp(endDate.getTime()), clientId});
        
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal.add(new MobileFormFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }
    
    @Override
    public ArrayList<Integer> getClientIdsFormMobileId(Integer mobileId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Integer> retVal = new ArrayList<Integer>();
        
        get_client_ids_by_mobile_id_query query = new get_client_ids_by_mobile_id_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{mobileId});
        
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal.add(rst.getInt("client_id"));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }
    
    public ArrayList<MobileFormsType> getFormsType() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormsType> formTypes = new ArrayList<MobileFormsType>();

        get_mobile_form_types query = new get_mobile_form_types();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileFormsType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public void setClientsToMobileForm(int mobileFormId, ArrayList<Integer> clientIds) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            save_mobile_form_to_clients_query saveQuery = new save_mobile_form_to_clients_query();
            saveQuery.update(mobileFormId, clientIds);
            saveQuery.setCompany(companyId);
            conn.executeUpdate(saveQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public Integer saveFormData(MobileFormData formData) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            save_mobile_form_data_query query = new save_mobile_form_data_query();
            query.setCompany(companyId);

            boolean isUpdate = true;
            if (formData.getMobileFormDataId()== null) {
                get_mobile_form_data_sequence_query sequenceQuery = new get_mobile_form_data_sequence_query();
                sequenceQuery.setPreparedStatement(new Object[]{});
                try {
                    isUpdate = false;
                    Record_Set rst = conn.executeQuery(sequenceQuery, "");
                    formData.setMobileFormDataId(rst.getInt("myid"));
                } catch (Exception exe) {
                    throw new RetrieveDataException();
                }
            }
            query.update(formData, isUpdate);
            conn.executeUpdate(query, "");
            return formData.getMobileFormDataId();
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public Integer saveForm(MobileForms form) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            save_mobile_form_query query = new save_mobile_form_query();
            query.setCompany(companyId);
            boolean isUpdate = true;
            if (form.getMobileFormsId() == null) {
                isUpdate = false;
                get_mobile_form_sequence_query sequenceQuery = new get_mobile_form_sequence_query();
                sequenceQuery.setPreparedStatement(new Object[]{});
                try {
                    Record_Set rst = conn.executeQuery(sequenceQuery, "");
                    form.setMobileFormsId(rst.getInt("myid"));
                } catch (Exception exe) {
                    throw new RetrieveDataException();
                }
            }
            query.update(form, isUpdate);
            conn.executeUpdate(query, "");
            return form.getMobileFormsId();
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public ArrayList<MobileForms> getForms(Integer clientId, Integer typeId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> formTypes = new ArrayList<MobileForms>();

        get_mobile_forms query = new get_mobile_forms();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    public ArrayList<MobileFormData> getFormData(Integer formId, Boolean showAll) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormData> formTypes = new ArrayList<MobileFormData>();

        get_mobile_form_data_with_all_query query = new get_mobile_form_data_with_all_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formId, showAll, showAll});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileFormData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }
    
    public ArrayList<MobileFormData> getFormData(Integer formId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormData> formTypes = new ArrayList<MobileFormData>();

        get_mobile_form_data query = new get_mobile_form_data();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileFormData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public ArrayList<MobileFormDataType> getFormDataTypes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormDataType> formTypes = new ArrayList<MobileFormDataType>();

        get_mobile_form_data_types query = new get_mobile_form_data_types();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileFormDataType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public ArrayList<MobileFormDataFillout> getFormDataFillout(Integer formFilloutId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormDataFillout> formData = new ArrayList<MobileFormDataFillout>();

        get_form_data_fillout_query query = new get_form_data_fillout_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formFilloutId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData.add(new MobileFormDataFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public MobileFormFillout getFormFillout(Integer formFilloutId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        MobileFormFillout formData = new MobileFormFillout();

        get_form_fillout_query query = new get_form_fillout_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formFilloutId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData = new MobileFormFillout(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public Integer saveFormFillout(MobileFormFillout formFillout) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            save_form_fillout_query query = new save_form_fillout_query();
            query.setCompany(companyId);
            boolean isUpdate = true;
            if (formFillout.getMobileFormFilloutId()== null) {
                isUpdate = false;
                get_mobile_form_sequence_query sequenceQuery = new get_mobile_form_sequence_query();
                sequenceQuery.setPreparedStatement(new Object[]{});
                try {
                    Record_Set rst = conn.executeQuery(sequenceQuery, "");
                    formFillout.setMobileFormFilloutId(rst.getInt("myid"));
                } catch (Exception exe) {
                    throw new RetrieveDataException();
                }
            }
            query.update(formFillout, isUpdate);
            conn.executeUpdate(query, "");
            return formFillout.getMobileFormFilloutId();
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveFormDataFillout(MobileFormDataFillout formDataFillout) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            ArrayList<MobileFormDataFillout> tempVals = new ArrayList<MobileFormDataFillout>();
            tempVals.add(formDataFillout);
            save_mobile_form_data_fillout_query query = new save_mobile_form_data_fillout_query();
            query.setCompany(companyId);
            query.update(tempVals);
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public void saveFormDataFillout(ArrayList<MobileFormDataFillout> formDataFillout) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            save_mobile_form_data_fillout_query query = new save_mobile_form_data_fillout_query();
            query.setCompany(companyId);
            query.update(formDataFillout);
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    @Override
    public ArrayList<MobileFormFillout> getNonSentImmediateFormFilloutsForClient(Integer clientId, Integer mobileFormId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> formData = new ArrayList<MobileFormFillout>();

        get_non_sent_immediate_form_fillout_for_form_query query = new get_non_sent_immediate_form_fillout_for_form_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{mobileFormId, clientId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData.add(new MobileFormFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFilloutsForClient(Integer clientId, Integer mobileFormId, boolean showDeleted) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> formData = new ArrayList<MobileFormFillout>();

        get_form_fillout_for_client_with_delete_query query = new get_form_fillout_for_client_with_delete_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId, mobileFormId, showDeleted, showDeleted});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData.add(new MobileFormFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }
    
    @Override
    public ArrayList<MobileFormFillout> getFormFillouts(Integer formId, boolean showDeleted) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> formData = new ArrayList<MobileFormFillout>();

        get_form_fillout_for_form_with_delete_query query = new get_form_fillout_for_form_with_delete_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formId, showDeleted, showDeleted});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData.add(new MobileFormFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }
    
    @Override
    public ArrayList<MobileFormFillout> getFormFillouts(Integer formId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> formData = new ArrayList<MobileFormFillout>();

        get_form_fillout_for_form_query query = new get_form_fillout_for_form_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{formId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData.add(new MobileFormFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public ArrayList<MobileForms> getAllFormsForCompany() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> formTypes = new ArrayList<MobileForms>();

        get_all_mobile_forms query = new get_all_mobile_forms();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public MobileForms getForm(Integer mobileFormId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        MobileForms formData = new MobileForms();

        get_all_mobile_form_by_id_query query = new get_all_mobile_form_by_id_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{mobileFormId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData = new MobileForms(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public ArrayList<MobileForms> getAllForms(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> formTypes = new ArrayList<MobileForms>();

        get_mobile_forms_for_client query = new get_mobile_forms_for_client();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public ArrayList<MobileFormFillout> getRecentFormFilloutsForClientAndEmployee(Integer clientId, Integer employeeId, Integer mobileFormId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> formData = new ArrayList<MobileFormFillout>();

        get_recent_form_fillout_for_client_employee_query query = new get_recent_form_fillout_for_client_employee_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId, employeeId, mobileFormId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formData.add(new MobileFormFillout(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public void saveOrdering(Integer[] mobileFormDataId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFillout> formData = new ArrayList<MobileFormFillout>();

        save_mobile_form_data_ordering_query query = new save_mobile_form_data_ordering_query();
        query.setCompany(companyId);

        try {
            for (int i = 0; i < mobileFormDataId.length; i++) {
                query.setPreparedStatement(new Object[]{i, mobileFormDataId[i]});
                conn.executeUpdate(query, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<MobileFormFilloutRptScan> getMaxReportScans(Integer mobileFormFilloutId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileFormFilloutRptScan> retVal = new ArrayList<MobileFormFilloutRptScan>();
        get_max_scans_for_shift_query maxQuery = new get_max_scans_for_shift_query();
        maxQuery.setPreparedStatement(new Object[]{mobileFormFilloutId});
        maxQuery.setCompany(companyId);
        
        try {
            Record_Set rst = conn.executeQuery(maxQuery, "");
            for (int c = 0; c < rst.length(); c++) {
                retVal.add(new MobileFormFilloutRptScan(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }

    @Override
    public ArrayList<MobileForms> getDailyForms(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> formTypes = new ArrayList<MobileForms>();

        get_mobile_daily_forms_for_client query = new get_mobile_daily_forms_for_client();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public ArrayList<MobileForms> getInstantForms(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<MobileForms> formTypes = new ArrayList<MobileForms>();

        get_mobile_immediate_forms_for_client query = new get_mobile_immediate_forms_for_client();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientId});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                formTypes.add(new MobileForms(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formTypes;
    }

    @Override
    public ArrayList<GlobalMobileFormFillout> getGlobalMobileFormFillouts(Integer mobileFormTypeId, String textToSearch) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GlobalMobileFormFillout> retVal = new ArrayList<GlobalMobileFormFillout>();
        
        HashMap<GlobalMobileFormFillout, GlobalMobileFormFillout> fillouts = new HashMap<GlobalMobileFormFillout, GlobalMobileFormFillout>();
        
        get_global_mobile_form_fillouts query = new get_global_mobile_form_fillouts();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{mobileFormTypeId, Integer.parseInt(this.companyId), textToSearch});
        
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int c = 0; c < rst.length(); c++) {
                GlobalMobileFormFillout fillout = new GlobalMobileFormFillout(rst);
                if (fillouts.get(fillout) == null) {
                    fillouts.put(fillout, fillout);
                }
                if (rst.getString("mobile_data") != null && rst.getString("mobile_data").trim().length() > 0) {
                    fillouts.get(fillout).getFilledOutDataSummaries().add(rst.getString("mobile_data"));
                }
                rst.moveNext();
            }
            
            Collection<GlobalMobileFormFillout> filloutSet = fillouts.values();
            Iterator<GlobalMobileFormFillout> iterator = filloutSet.iterator();
            while (iterator.hasNext()) {
                retVal.add(iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return retVal;
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFilloutsForClient(Integer clientId, Integer mobileFormId, String startDate, String endDate, ArrayList<MobileFormDataSearch> searchData) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<MobileFormDataSearch> getFormSearchData(Integer formId, Boolean showAll) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<MobileReportEmployeeGraphData> getMobileReportEmployeeGraphDataSummary(String startDate, String endDate, String intervalType, ArrayList<Integer> clientId) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<MobileReportGraphData> getMobileReportGraphDataSummary(String startDate, String endDate, String intervalType, ArrayList<Integer> clientId) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
