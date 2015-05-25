/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.util.ImageLoader;
import schedfoxlib.services.ClientService;
import schedfoxlib.services.CompanyService;
import schedfoxlib.services.EmployeeService;
import schedfoxlib.services.MobileFormService;

/**
 *
 * @author ira
 */
public class MobileFormsPrintable {

    private static HashMap<Integer, Employee> empHash;

    private static HashMap<String, Object> generateParameterMap(Class context, Date startDate, Date endDate, MobileForms mobileForm, CompanyObj companyObj) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String currTimezone = "US/Central";
        Client client = null;
        try {
            CompanyService companyService = new CompanyService();
            ClientService clientService = new ClientService(companyObj.getCompanyId() + "");

            client = clientService.getClientById(mobileForm.getClientId());

            Branch branch = companyService.getBranchById(client.getBranchId());
            if (branch.getTimezone().equals("EST")) {
                currTimezone = "US/Eastern";
            } else {
                currTimezone = branch.getTimezone();
            }
        } catch (Exception exe) {
        }
        try {
            params.put("checkbox_checked", context.getResourceAsStream("checked.jpg"));
            params.put("checkbox_checked_URL", context.getResource("checked.jpg"));
            params.put("checkbox_checked_img", ImageIO.read(context.getResourceAsStream("checked.jpg")));
        } catch (Exception exe) {
        }
        try {
            params.put("checkbox_unchecked", context.getResourceAsStream("unchecked.jpg"));
            params.put("checkbox_unchecked_URL", context.getResource("unchecked.jpg"));
            params.put("checkbox_unchecked_img", ImageIO.read(context.getResourceAsStream("unchecked.jpg")));
        } catch (Exception exe) {
        }
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            format.setTimeZone(TimeZone.getTimeZone(currTimezone));
        } catch (Exception exe) {
        }

        params.put("mobileFormId", mobileForm.getMobileFormsId());
        params.put("start_date", new SimpleDateFormat("yyyy-MM-dd").format(startDate));
        params.put("end_date", new SimpleDateFormat("yyyy-MM-dd").format(endDate));
        params.put("active_db", companyObj.getCompanyDb());
        params.put("client_name", client.getClientName());
        params.put("dateFormatter", format);
        return params;
    }

    public static byte[] getHeaderByteArray(Class context, CompanyObj companyObj) {
        InputStream imageStream = context.getResourceAsStream("Patrol-Pro-Client-Banner.jpg");

        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try {
            BufferedImage bImage = (BufferedImage) ImageLoader.getImage("login_header.jpg", companyObj.getCompanyDb(), "general").getImage();
            if (bImage.getWidth() > 500) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();

                ImageIO.write(bImage, "gif", os);
                imageStream = new ByteArrayInputStream(os.toByteArray());
            }
        } catch (Exception exe) {
        }
        try {
            byte[] buffer = new byte[2048];
            int numRead = 0;
            while ((numRead = imageStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numRead);
            }
        } catch (Exception exe) {
        }
        return oStream.toByteArray();
    }

    public static JasperPrint generateImmediatePrint(Class context, MobileForms mobileForm, String companyId, Date startDate, Date endDate, Connection conn) {
        MobileFormService mobileService = new MobileFormService(companyId);

        CompanyService companyService = new CompanyService();

        try {
            CompanyObj companyObj = companyService.getCompanyObjById(Integer.parseInt(companyId));

            HashMap<String, Object> params = MobileFormsPrintable.generateParameterMap(context, startDate, endDate, mobileForm, companyObj);
            byte[] headerByteArray = getHeaderByteArray(context, companyObj);

            JasperPrint totalReport = null;
            if (mobileForm.getReportData() != null) {
                ByteArrayInputStream iStream = null;
                JasperReport jasperReport = null;
                try {
                    iStream = new ByteArrayInputStream(mobileForm.getReportData());
                    jasperReport = (JasperReport) JRLoader.loadObject(iStream);
                } catch (Exception e) {
                    iStream = new ByteArrayInputStream(mobileForm.getReportData());
                    jasperReport = JasperCompileManager.compileReport(iStream);
                }
                ArrayList<MobileFormFillout> fillouts = mobileService.getNonSentImmediateFormFilloutsForClient(mobileForm.getClientId(), mobileForm.getMobileFormsId());
                if (fillouts != null) {
                    for (int f = 0; f < fillouts.size(); f++) {
                        params.put("logo", new ByteArrayInputStream(headerByteArray));
                        totalReport = addJasperPrintToTotalReport(context, totalReport, fillouts.get(f), params, conn, startDate, endDate, companyId, jasperReport, companyObj);
                        fillouts.get(f).setNotificationSent(new Date());
                        mobileService.saveFormFillout(fillouts.get(f));
                    }
                }
                MobileFormsPrintable.empHash = new HashMap<Integer, Employee>();
            }
            return totalReport;
        } catch (RetrieveDataException rExcept) {
            rExcept.printStackTrace();
        } catch (JRException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Employee getEmployeeId(String companyId, Integer employeeId) {
        if (empHash == null) {
            empHash = new HashMap<Integer, Employee>();
        }
        if (!empHash.containsKey(employeeId)) {
            try {
                EmployeeService employeeService = new EmployeeService(companyId);
                Employee employee = employeeService.getEmployeeById(employeeId);
                empHash.put(employeeId, employee);
            } catch (Exception exe) {}
        }
        return empHash.get(employeeId);
    }

    public static JasperPrint generateDailyPrint(Class context, ArrayList<MobileFormFillout> fillouts, MobileForms mobileForm, String companyId, Date startDate, Date endDate, Connection conn) {
        CompanyService companyService = new CompanyService();

        try {
            CompanyObj companyObj = companyService.getCompanyObjById(Integer.parseInt(companyId));
            HashMap<String, Object> params = MobileFormsPrintable.generateParameterMap(context, startDate, endDate, mobileForm, companyObj);

            byte[] headerByteArray = getHeaderByteArray(context, companyObj);

            JasperPrint totalReport = null;
            if (mobileForm.getReportData() != null) {
                ByteArrayInputStream iStream = null;
                JasperReport jasperReport = null;
                try {
                    iStream = new ByteArrayInputStream(mobileForm.getReportData());
                    jasperReport = (JasperReport) JRLoader.loadObject(iStream);
                } catch (Exception e) {
                    iStream = new ByteArrayInputStream(mobileForm.getReportData());
                    jasperReport = JasperCompileManager.compileReport(iStream);
                }

                
                if (fillouts != null) {
                    for (int f = 0; f < fillouts.size(); f++) {
                        params.put("logo", new ByteArrayInputStream(headerByteArray));
                        totalReport = addJasperPrintToTotalReport(context, totalReport, fillouts.get(f), params, conn, startDate, endDate, companyId, jasperReport, companyObj);
                    }
                    MobileFormsPrintable.empHash = new HashMap<Integer, Employee>();
                }
            }
            return totalReport;
        } catch (RetrieveDataException rExcept) {
            rExcept.printStackTrace();
        } catch (JRException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JasperPrint addJasperPrintToTotalReport(Class context, JasperPrint totalReport, MobileFormFillout fillout, HashMap<String, Object> params, Connection conn, Date startDate, Date endDate, String companyId, JasperReport jasperReport, CompanyObj companyObj) {
        try {
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);

            if (startDate == null || (fillout.getDateEntered().compareTo(startDate) >= 0 && fillout.getDateEntered().compareTo(endCal.getTime()) < 0)) {
                Employee employee = MobileFormsPrintable.getEmployeeId(companyId, fillout.getEmployeeId());

                MobileFormService mobileService = new MobileFormService(companyId);
                try {
                    boolean containsNFC = false;
                    JRParameter[] parameters = jasperReport.getParameters();
                    for (int p = 0; p < parameters.length; p++) {
                        if (parameters[p].getName().startsWith("nfc_l_l")) {
                            containsNFC = true;
                        }
                    }
                    if (containsNFC) {
                        ArrayList<MobileFormFilloutRptScan> filloutRptScans = mobileService.getMaxReportScans(fillout.getMobileFormFilloutId());
                        for (int s = 0; s < filloutRptScans.size(); s++) {
                            MobileFormFilloutRptScan scan = filloutRptScans.get(s);
                            params.put("nfc_l_" + scan.getClientWaypointId(), scan.getMaxDate());
                        }
                    }
                } catch (Exception exe) {
                    exe.printStackTrace();
                }

                if (fillout.getEmployeeId().equals(999999)) {
                    employee.setEmployeeFirstName("Supervisor");
                }

                ArrayList<MobileFormDataFillout> filloutData = mobileService.getFormDataFillout(fillout.getMobileFormFilloutId());
                ArrayList<MobileFormData> myData = mobileService.getFormData(fillout.getMobileFormId());

                for (int myFill = 0; myFill < filloutData.size(); myFill++) {
                    MobileFormDataFillout fillData = filloutData.get(myFill);
                    String key = "";
                    Class dataClass = String.class;
                    for (int dd = 0; dd < myData.size(); dd++) {
                        if (myData.get(dd).getMobileFormDataId().equals(fillData.getMobileFormDataId())) {
                            key = myData.get(dd).getDataLabel();
                            if (myData.get(dd).getDateType() == 3) {
                                dataClass = Boolean.class;
                            } else if (myData.get(dd).getDateType() == 5) {
                                dataClass = Date.class;
                            } else if (myData.get(dd).getDateType() == 8) {
                                dataClass = byte[].class;
                            } else if (myData.get(dd).getDateType() == 9) {
                                dataClass = MobileFormsPrintableLocationAndData.class;
                            } else if (myData.get(dd).getDateType() == 10) {
                                dataClass = MobileFormsPrintableLocationAndData.class;
                            }
                        }
                    }
                    params.put("curr_date", fillout.getDateEntered());
                    params.put("employee_name", employee.getEmployeeFullName());

                    boolean isReservedIdentifier = false;
                    for (int i = 0; i < MobileForms.getReservedIdentifiers().length; i++) {
                        if (MobileForms.getReservedIdentifiers()[i].equals(key) || key.startsWith("nfc_l_") || key.endsWith("_loc")) {
                            isReservedIdentifier = true;
                        }
                    }
                    if (!isReservedIdentifier) {
                        if (dataClass.equals(String.class)) {
                            params.put(key, fillData.getMobileData());
                        } else if (dataClass.equals(Boolean.class)) {
                            params.put(key, Boolean.parseBoolean(fillData.getMobileData()));
                        } else if (dataClass.equals(Integer.class)) {
                            params.put(key, Integer.parseInt(fillData.getMobileData()));
                        } else if (dataClass.equals(byte[].class)) {
                            try {
                                params.put(key, new ByteArrayInputStream(fillData.getMobileDataBytes()));
                            } catch (Exception exe) {
                                params.put(key, null);
                            }
                        } else if (dataClass.equals(MobileFormsPrintableLocationAndData.class)) {
                            String data = fillData.getMobileData();
                            try {
                                if (data.contains("|")) {
                                    params.put(key, data.substring(0, data.indexOf("|")));
                                    params.put(key + "_loc", data.substring(data.indexOf("|") + 1));
                                } else {
                                    params.put(key, data);
                                }
                            } catch (Exception exe) {
                            }
                        }
                    }
                }

                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, conn);
                if (totalReport == null) {
                    totalReport = print;
                } else {
                    List pages = print.getPages();
                    for (int j = 0; j < pages.size(); j++) {
                        JRPrintPage object = (JRPrintPage) pages.get(j);
                        totalReport.addPage(object);
                    }
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return totalReport;
    }
}
