/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
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

/**
 *
 * @author ira
 */
public class MobileFormService implements MobileFormInterface {

    private static String location = "MobileForm/";
    private String companyId = "2";

    public static void main(String args[]) {
        try {
            MobileFormService mobileService = new MobileFormService("1852");
            Integer count = mobileService.getAssociatedFormsCount(9, "NEW LICENSE|CA");
            System.out.println("Here");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public MobileFormService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public MobileFormService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public ArrayList<MobileFormsType> getFormsType() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformstype");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormsType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveForm(MobileForms form) throws SaveDataException, RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveform/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(form))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveFormData(MobileFormData formData) throws SaveDataException, RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveformdata/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(formData))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileForms> getForms(Integer clientId, Integer typeId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getforms/" + clientId + "/" + typeId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileForms>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormData> getFormData(Integer formId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformdata/" + formId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormData>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormDataType> getFormDataTypes() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformdatatype");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormDataType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormDataFillout> getFormDataFillout(Integer formFilloutId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformdatafillout/" + formFilloutId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormDataFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public MobileFormFillout getFormFillout(Integer formFilloutId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformfillout/" + formFilloutId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), MobileFormFillout.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFillouts(Integer formId, boolean showDeleted) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallformsfillouts/" + formId + "/" + showDeleted);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveFormFillout(MobileFormFillout formFillout) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveformfillout/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            //ObjectMapper myMapper = new ObjectMapper();
            //myMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            Gson gson = SchedfoxLibServiceVariables.getGson();

            Object obj = new JsonRepresentation(gson.toJson(formFillout));
            String json = cr.post(obj).getText();

            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveFormDataFillout(ArrayList<MobileFormDataFillout> formDataFillout) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveformdatafillout/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(formDataFillout)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFillouts(Integer formId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformfillouts/" + formId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileForms> getAllFormsForCompany() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallforms/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileForms>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormData> getFormData(Integer formId, Boolean showAll) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformdata/" + formId + "/" + showAll);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormData>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void setClientsToMobileForm(int mobileFormId, ArrayList<Integer> clientIds) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "setclientstomobileform/" + mobileFormId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(clientIds)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Integer> getClientIdsFormMobileId(Integer mobileId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientidsformmobileid/" + mobileId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<Integer>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFilloutsForClient(Integer clientId, Integer mobileFormId, boolean showDeleted) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformfilloutsforclient/" + clientId + "/" + mobileFormId + "/" + showDeleted);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public MobileForms getForm(Integer mobileFormId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getform/" + mobileFormId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), MobileForms.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileForms> getAllForms(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallformsforclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileForms>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFillout(Integer mobileFormId, Date startDate, Date endDate, Integer clientId) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();

            ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformfilloutbyrange/" + mobileFormId + "/"
                    + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8") + "/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            ArrayList<MobileFormFillout> retVal = gson.fromJson(cr.get().getReader(), collectionType);
            cr.getResponse().release();
            return retVal;
        } catch (Exception exe) {
            exe.printStackTrace();
            return new ArrayList<MobileFormFillout>();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getRecentFormFilloutsForClientAndEmployee(Integer clientId, Integer employeeId, Integer mobileFormsId) throws RetrieveDataException {
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();

            ClientResource cr = new ClientResource(Method.GET, getLocation() + "getrecentformfilloutsforclientandemployee/" + clientId + "/"
                    + employeeId + "/" + mobileFormsId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            ArrayList<MobileFormFillout> retVal = gson.fromJson(cr.get().getReader(), collectionType);
            cr.getResponse().release();
            return retVal;
        } catch (Exception exe) {
            exe.printStackTrace();
            return new ArrayList<MobileFormFillout>();
        }
    }

    @Override
    public void saveOrdering(Integer[] mobileFormDataId) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveordering/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(mobileFormDataId)));
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveFormDataFillout(MobileFormDataFillout formDataFillout) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveoneformdatafillout/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            ObjectMapper myMapper = new ObjectMapper();
            myMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            Object obj = new JsonRepresentation(myMapper.writeValueAsString(formDataFillout));
            cr.post(obj).getText();
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFilloutRptScan> getMaxReportScans(Integer mobileFormFilloutId) throws RetrieveDataException {
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFilloutRptScan>>() {
            }.getType();

            ClientResource cr = new ClientResource(Method.GET, getLocation() + "getmaxreportscans/" + mobileFormFilloutId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            ArrayList<MobileFormFilloutRptScan> retVal = gson.fromJson(cr.get().getReader(), collectionType);
            cr.getResponse().release();
            return retVal;
        } catch (Exception exe) {
            exe.printStackTrace();
            return new ArrayList<MobileFormFilloutRptScan>();
        }
    }

    @Override
    public ArrayList<MobileForms> getDailyForms(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getdailyforms/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileForms>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileForms> getInstantForms(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getimmediateforms/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileForms>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getNonSentImmediateFormFilloutsForClient(Integer clientId, Integer mobileFormId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getnonimmediateFormFilloutsForClient/" + clientId + "/" + mobileFormId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer getAssociatedFormsCount(Integer formDataTypeId, String dataToSearch) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getassociatedformscount/" + formDataTypeId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(dataToSearch)).getText(), Integer.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GlobalMobileFormFillout> getGlobalMobileFormFillouts(Integer formDataTypeId, String dataToSearch) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getglobalmobileformfillouts/" + formDataTypeId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<GlobalMobileFormFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(dataToSearch)).getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileReportGraphData> getMobileReportGraphDataSummary(String startDate, String endDate, String intervalType, ArrayList<Integer> clientIds) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getmobilereportgraphdatasummary/" + 
                    URLEncoder.encode(startDate, "UTF-8") + "/" + URLEncoder.encode(endDate, "UTF-8") + "/" + 
                    URLEncoder.encode(intervalType, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<MobileReportGraphData>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(clientIds))).getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileReportEmployeeGraphData> getMobileReportEmployeeGraphDataSummary(String startDate, String endDate, String intervalType, ArrayList<Integer> clientIds) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getmobilereportemployeegraphdatasummary/" + 
                    URLEncoder.encode(startDate, "UTF-8") + "/" + URLEncoder.encode(endDate, "UTF-8") + "/" + 
                    URLEncoder.encode(intervalType, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<MobileReportEmployeeGraphData>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(clientIds))).getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormDataSearch> getFormSearchData(Integer formId, Boolean showAll) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getformsearchdata/" + formId + "/" + showAll);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<MobileFormDataSearch>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<MobileFormFillout> getFormFilloutsForClient(Integer clientId, Integer mobileFormId, String startDate, String endDate, ArrayList<MobileFormDataSearch> searchData) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getformfilloutsforclientwithsearches/" + clientId + "/" + mobileFormId + "/" +
                    URLEncoder.encode(startDate, "UTF-8") + "/" + URLEncoder.encode(endDate, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<MobileFormFillout>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(searchData))).getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
