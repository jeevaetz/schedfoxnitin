/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.ireports.classes;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import rmischedule.ireports.RunReportInterface;

/**
 *
 * @author user
 */
public class GetReportListing {

    public static String classPath = "/rmischedule/ireports/classes/";

    public ArrayList<RunReportInterface> getReports(URL packageLocation) {
        ArrayList<RunReportInterface> retVal = new ArrayList<RunReportInterface>();
//
//        System.out.println("Location: " + packageLocation);
//
//        try {
//            File packageFolder = new File(packageLocation.toURI());
//            File[] packageContents = packageFolder.listFiles();
//
//            for (int f = 0; f < packageContents.length; f++) {
//                try {
//                    String fileName = packageContents[f].getName();
//                    fileName = fileName.replaceFirst(".class", "");
//                    Class myClass = Class.forName("rmischedule.ireports.classes." + fileName);
//                    if (RunReportInterface.class.isAssignableFrom(myClass)) {
//                        Object newReport = myClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
//                        retVal.add((RunReportInterface)newReport);
//                    }
//                } catch (Exception e) {}
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
         retVal.add(new PrintActiveEmps());
         retVal.add(new PrintCoporateCommUsageReport());
         retVal.add(new PrintDemographics());
         retVal.add(new PrintEmpCountReport());
         retVal.add(new PrintExtendedEmployeeReport());
         retVal.add(new PrintOverTime());
         retVal.add(new PrintOverUnder());
         retVal.add(new PrintTotalsForLocation());


         return retVal;
    }

    public static void main(String args[]) {
        GetReportListing reportListing = new GetReportListing();
        reportListing.getReports(reportListing.getClass().getResource("/rmischedule/ireports/classes/"));
    }
}
