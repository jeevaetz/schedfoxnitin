/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller.registry;

import java.lang.reflect.Constructor;
import schedfoxlib.controller.*;

/**
 *
 * @author user
 */
public class ControllerRegistryAbstract {
    
    private static Class addressInterface;
    private static Class branchInterface;
    private static Class clientInterface;
    private static Class userInterface;
    private static Class employeeInterface;
    private static Class billingInterface;
    private static Class clientContractInterface;
    private static Class companyInterface;
    private static Class timeoffInterface;
    private static Class problemSolverInterface;
    private static Class genericInterface;
    private static Class incidentReportInterface;
    private static Class scheduleInterface;
    private static Class officerDailyReportInterface;
    private static Class equipmentInterface;
    private static Class gpsInterface;
    private static Class wayPointInterface;
    private static Class fileInterface;
    private static Class capturedEmailInterface;
    private static Class healthInterface;
    private static Class salesInterface;
    private static Class outlookInterface;
    private static Class accessInterface;
    private static Class mobileFormInterface;
    private static Class messagingInterface;
    private static Class geoFenceInterface;
    private static Class clientRatingInterface;
    
    public static void setClientRatingInterface(Class clientRatingInterface) {
        ControllerRegistryAbstract.clientRatingInterface = clientRatingInterface;
    }
    
    public static void setAccessInterface(Class accessInterface) {
        ControllerRegistryAbstract.accessInterface = accessInterface;
    }
    
    public static void setOutlookInterface(Class outlookInterface) {
        ControllerRegistryAbstract.outlookInterface = outlookInterface;
    }
    
    public static void setSalesInterface(Class salesInterface) {
        ControllerRegistryAbstract.salesInterface = salesInterface;
    }
    
    public static void setHealthInterface(Class healthInterface) {
        ControllerRegistryAbstract.healthInterface = healthInterface;
    }
    
    public static void setFileInterface(Class fileInterface) {
        ControllerRegistryAbstract.fileInterface = fileInterface;
    }
    
    public static void setEquipmentInterface(Class equipmentInterface) {
        ControllerRegistryAbstract.equipmentInterface = equipmentInterface;
    }
    
    public static void setGPSInterface(Class gpsInterface) {
        ControllerRegistryAbstract.gpsInterface = gpsInterface;
    }
    
    public static void setOfficerDailyReportInterface(Class officerDailyReportInterface) {
        ControllerRegistryAbstract.officerDailyReportInterface = officerDailyReportInterface;
    }
    
    public static void setAddressController(Class addressInterface) {
        ControllerRegistryAbstract.addressInterface = addressInterface;
    }
    
    public static void setGenericController(Class genericInterface) {
        ControllerRegistryAbstract.genericInterface = genericInterface;
    }
    
    public static void setClientController(Class interfaceInterface) {
        ControllerRegistryAbstract.clientInterface = interfaceInterface;
    }
    
    public static void setUserController(Class userInterface) {
        ControllerRegistryAbstract.userInterface = userInterface;
    }
    
    public static void setEmployeeController(Class employeeInterface) {
        ControllerRegistryAbstract.employeeInterface = employeeInterface;
    }
    
    public static void setBillingController(Class billingInterface) {
        ControllerRegistryAbstract.billingInterface = billingInterface;
    }
    
    public static void setClientContractController(Class contractInterface) {
        ControllerRegistryAbstract.clientContractInterface = contractInterface;
    }
    
    public static void setCompanyController(Class companyInterface) {
        ControllerRegistryAbstract.companyInterface = companyInterface;
    }
    
    public static void setTimeOffController(Class timeoffInterface) {
        ControllerRegistryAbstract.timeoffInterface = timeoffInterface;
    }
    
    public static void setProblemSolverInterface(Class problemSolverInterface) {
        ControllerRegistryAbstract.problemSolverInterface = problemSolverInterface;
    }
    
    /**
     * @param aScheduleInterface the scheduleInterface to set
     */
    public static void setScheduleInterface(Class aScheduleInterface) {
        scheduleInterface = aScheduleInterface;
    }
    
    /**
     * @param aIncidentReportInterface the incidentReportInterface to set
     */
    public static void setIncidentReportInterface(Class aIncidentReportInterface) {
        incidentReportInterface = aIncidentReportInterface;
    }
    
    public static ClientRatingControllerInterface getClientRatingInterface(String companyId) {
        try {
            Constructor genericConstructor = clientRatingInterface.getDeclaredConstructor(String.class);
            if (!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (ClientRatingControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static OutlookControllerInterface getOutlookController(String companyId) {
        try {
            Constructor genericConstructor = outlookInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (OutlookControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static AccessControllerInterface getAccessController(String companyId) {
        try {
            Constructor genericConstructor = accessInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (AccessControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static SalesControllerInterface getSalesController(String companyId) {
        try {
            Constructor genericConstructor = salesInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (SalesControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static HealthCareControllerInterface getHealthController(String companyId) {
        try {
            Constructor genericConstructor = healthInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (HealthCareControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static FileControllerInterface getFileController(String companyId) {
        try {
            Constructor genericConstructor = fileInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (FileControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static EquipmentControllerInterface getEquipmentController(String companyId) {
        try {
            Constructor genericConstructor = equipmentInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (EquipmentControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static GPSControllerInterface getGPSController(String companyId) {
        try {
            Constructor genericConstructor = gpsInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (GPSControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static OfficerDailyReportControllerInterface getOfficerDailyReportController(String companyId) {
        try {
            Constructor genericConstructor = officerDailyReportInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (OfficerDailyReportControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static AddressControllerInterface getAddressController(String companyId) {
        try {
            Constructor genericConstructor = addressInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (AddressControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static GenericControllerInterface getGenericController(String companyId) {
        try {
            Constructor genericConstructor = genericInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (GenericControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static ClientControllerInterface getClientController(String companyId){
        try {
            Constructor clientConstructor = clientInterface.getDeclaredConstructor(String.class);
            if(!clientConstructor.isAccessible()) {
                clientConstructor.setAccessible(true);
            }
            return (ClientControllerInterface)clientConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    public static UserControllerInterface getUserController(String companyId) {
        try {
            Constructor userConstructor = userInterface.getDeclaredConstructor(String.class);
            if(!userConstructor.isAccessible()) {
                userConstructor.setAccessible(true);
            }
            return (UserControllerInterface)userConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static EmployeeControllerInterface getEmployeeController(String companyId) {
        try {
            Constructor employeeConstructor = employeeInterface.getDeclaredConstructor(String.class);
            if(!employeeConstructor.isAccessible()) {
                employeeConstructor.setAccessible(true);
            }
            return (EmployeeControllerInterface)employeeConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static BillingControllerInterface getBillingController(String companyId) {
        try {
            Constructor billingConstructor = billingInterface.getDeclaredConstructor(String.class);
            if(!billingConstructor.isAccessible()) {
                billingConstructor.setAccessible(true);
            }
            return (BillingControllerInterface)billingConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static ClientContactControllerInterface getClientContractController(String companyId) {
        try {
            Constructor clientContractConstructor = clientContractInterface.getDeclaredConstructor(String.class);
            if(!clientContractConstructor.isAccessible()) {
                clientContractConstructor.setAccessible(true);
            }
            return (ClientContactControllerInterface)clientContractConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static CompanyControllerInterface getCompanyController(String companyId) {
        try {
            Constructor companyConstructor = companyInterface.getDeclaredConstructor(String.class);
            if(!companyConstructor.isAccessible()) {
                companyConstructor.setAccessible(true);
            }
            return (CompanyControllerInterface)companyConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static TimeOffControllerInterface getTimeoffController(String companyId) {
        try {
            Constructor timeOffConstructor = timeoffInterface.getDeclaredConstructor(String.class);
            if(!timeOffConstructor.isAccessible()) {
                timeOffConstructor.setAccessible(true);
            }
            return (TimeOffControllerInterface)timeOffConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static ProblemSolverControllerInterface getProblemSolverController(String companyId) {
        try {
            Constructor problemSolverConstructor = problemSolverInterface.getDeclaredConstructor(String.class);
            if(!problemSolverConstructor.isAccessible()) {
                problemSolverConstructor.setAccessible(true);
            }
            return (ProblemSolverControllerInterface)problemSolverConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the incidentReportInterface
     */
    public static IncidentReportControllerInterface getIncidentReportInterface(String companyId) {
        try {
            Constructor incidentReportConstructor = incidentReportInterface.getDeclaredConstructor(String.class);
            if(!incidentReportConstructor.isAccessible()) {
                incidentReportConstructor.setAccessible(true);
            }
            return (IncidentReportControllerInterface)incidentReportConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the scheduleInterface
     */
    public static ScheduleControllerInterface getScheduleInterface(String companyId) {
        try {
            Constructor scheduleConstructor = scheduleInterface.getDeclaredConstructor(String.class);
            if(!scheduleConstructor.isAccessible()) {
                scheduleConstructor.setAccessible(true);
            }
            return (ScheduleControllerInterface)scheduleConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the wayPointInterface
     */
    public static WayPointControllerInterface getWayPointInterface(String companyId) {
        try {
            Constructor scheduleConstructor = wayPointInterface.getDeclaredConstructor(String.class);
            if(!scheduleConstructor.isAccessible()) {
                scheduleConstructor.setAccessible(true);
            }
            return (WayPointControllerInterface)scheduleConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param aWayPointInterface the wayPointInterface to set
     */
    public static void setWayPointInterface(Class aWayPointInterface) {
        wayPointInterface = aWayPointInterface;
    }

    /**
     * @return the capturedEmailInterface
     */
    public static CapturedEmailInterface getCapturedEmailInterface(String companyId) {
        try {
            Constructor scheduleConstructor = capturedEmailInterface.getDeclaredConstructor(String.class);
            if(!scheduleConstructor.isAccessible()) {
                scheduleConstructor.setAccessible(true);
            }
            return (CapturedEmailInterface)scheduleConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param aCapturedEmailInterface the capturedEmailInterface to set
     */
    public static void setCapturedEmailInterface(Class aCapturedEmailInterface) {
        capturedEmailInterface = aCapturedEmailInterface;
    }

    /**
     * @return the branchInterface
     */
    public static BranchControllerInterface getBranchInterface(String companyId) {
        try {
            Constructor genericConstructor = branchInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (BranchControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param aBranchInterface the branchInterface to set
     */
    public static void setBranchInterface(Class aBranchInterface) {
        branchInterface = aBranchInterface;
    }

    /**
     * @return the mobileFormInterface
     */
    public static MobileFormInterface getMobileFormInterface(String companyId) {
        try {
            Constructor scheduleConstructor = mobileFormInterface.getDeclaredConstructor(String.class);
            if(!scheduleConstructor.isAccessible()) {
                scheduleConstructor.setAccessible(true);
            }
            return (MobileFormInterface)scheduleConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param aMobileFormInterface the mobileFormInterface to set
     */
    public static void setMobileFormInterface(Class aMobileFormInterface) {
        mobileFormInterface = aMobileFormInterface;
    }

    /**
     * @return the messagingInterface
     */
    public static MessagingControllerInterface getMessagingInterface(String companyId) {
        try {
            Constructor messagingConstructor = messagingInterface.getDeclaredConstructor(String.class);
            if(!messagingConstructor.isAccessible()) {
                messagingConstructor.setAccessible(true);
            }
            return (MessagingControllerInterface)messagingConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param aMessagingInterface the messagingInterface to set
     */
    public static void setMessagingInterface(Class aMessagingInterface) {
        messagingInterface = aMessagingInterface;
    }

    /**
     * @return the geoFenceInterface
     */
    public static GeoFenceControllerInterface getGeoFenceInterface(String companyId) {
        try {
            Constructor genericConstructor = geoFenceInterface.getDeclaredConstructor(String.class);
            if(!genericConstructor.isAccessible()) {
                genericConstructor.setAccessible(true);
            }
            return (GeoFenceControllerInterface)genericConstructor.newInstance(companyId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param aGeoFenceInterface the geoFenceInterface to set
     */
    public static void setGeoFenceInterface(Class aGeoFenceInterface) {
        geoFenceInterface = aGeoFenceInterface;
    }

    

    
}
