/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author ira
 */
public interface MobileFormInterface {
    
    public ArrayList<MobileFormFillout> getFormFilloutsForClient(Integer clientId, Integer mobileFormId, String startDate, String endDate, ArrayList<MobileFormDataSearch> searchData) throws RetrieveDataException;
    
    public ArrayList<MobileFormDataSearch> getFormSearchData(Integer formId, Boolean showAll) throws RetrieveDataException;
    
    public ArrayList<MobileReportEmployeeGraphData> getMobileReportEmployeeGraphDataSummary(String startDate, String endDate, String intervalType, ArrayList<Integer> clientId) throws RetrieveDataException;
    
    public ArrayList<MobileReportGraphData> getMobileReportGraphDataSummary(String startDate, String endDate, String intervalType, ArrayList<Integer> clientId) throws RetrieveDataException;
    
    public ArrayList<GlobalMobileFormFillout> getGlobalMobileFormFillouts(Integer mobileFormTypeId, String textToSearch) throws RetrieveDataException;
    
    public Integer getAssociatedFormsCount(Integer formDataTypeId, String dataToSearch) throws RetrieveDataException;
    
    public ArrayList<MobileFormFillout> getNonSentImmediateFormFilloutsForClient(Integer clientId, Integer mobileFormId) throws RetrieveDataException;
    
    public ArrayList<MobileFormsType> getFormsType() throws RetrieveDataException;
    
    public ArrayList<MobileFormFilloutRptScan> getMaxReportScans(Integer mobileFormFilloutId) throws RetrieveDataException;
    
    public ArrayList<MobileFormFillout> getFormFillout(Integer mobileFormId, Date startDate, Date endDate, Integer clientId);
    
    public ArrayList<MobileFormDataType> getFormDataTypes() throws RetrieveDataException;
    
    public ArrayList<Integer> getClientIdsFormMobileId(Integer mobileId) throws RetrieveDataException;

    public void setClientsToMobileForm(int mobileFormId, ArrayList<Integer> clientIds) throws SaveDataException;
    
    public ArrayList<MobileFormFillout> getRecentFormFilloutsForClientAndEmployee(Integer clientId, Integer employeeId, Integer mobileFormId) throws RetrieveDataException;
     
    public ArrayList<MobileFormFillout> getFormFilloutsForClient(Integer clientId, Integer mobileFormId, boolean showDeleted) throws RetrieveDataException;
            
    public void saveOrdering(Integer[] mobileFormDataId) throws SaveDataException;
    
    public Integer saveForm(MobileForms form) throws SaveDataException, RetrieveDataException;
    
    public MobileForms getForm(Integer mobileFormId) throws RetrieveDataException;
    
    public Integer saveFormData(MobileFormData formData) throws SaveDataException, RetrieveDataException;
    
    public ArrayList<MobileForms> getForms(Integer clientId, Integer typeId) throws RetrieveDataException;
    
    public ArrayList<MobileForms> getDailyForms(Integer clientId) throws RetrieveDataException;
    
    public ArrayList<MobileForms> getInstantForms(Integer clientId) throws RetrieveDataException;
    
    public ArrayList<MobileForms> getAllForms(Integer clientId) throws RetrieveDataException;
    
    public ArrayList<MobileForms> getAllFormsForCompany() throws RetrieveDataException;
    
    public void saveFormDataFillout(MobileFormDataFillout formDataFillout) throws SaveDataException;

    public ArrayList<MobileFormData> getFormData(Integer formId, Boolean showAll) throws RetrieveDataException;
    
    public ArrayList<MobileFormData> getFormData(Integer formId) throws RetrieveDataException;
    
    public ArrayList<MobileFormDataFillout> getFormDataFillout(Integer formFilloutId) throws RetrieveDataException;
    
    public MobileFormFillout getFormFillout(Integer formFilloutId) throws RetrieveDataException;
    
    public ArrayList<MobileFormFillout> getFormFillouts(Integer formId, boolean showDeleted) throws RetrieveDataException;
    
    public ArrayList<MobileFormFillout> getFormFillouts(Integer formId) throws RetrieveDataException;
    
    public Integer saveFormFillout(MobileFormFillout formFillout) throws SaveDataException;
    
    public void saveFormDataFillout(ArrayList<MobileFormDataFillout> formDataFillout) throws SaveDataException;
}
