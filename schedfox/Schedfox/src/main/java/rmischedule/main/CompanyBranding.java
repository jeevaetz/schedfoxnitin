/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.main;

import com.creamtec.ajaxswing.AjaxSwingManager;
import schedfoxlib.model.Company;
import java.util.Date;
import javax.swing.ImageIcon;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.util.ImageLoader;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import rmischeduleserver.mysqlconnectivity.queries.client.load_client_by_url_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_company_by_database_query;

/**
 *
 * @author user
 */
public class CompanyBranding {
    private Company company;
    private LoginType loginType;
    private Client myClient;

    public enum LoginType {
        USER("User"), EMPLOYEE("Employee"), CLIENT("Client"), NEWEMPLOYEE("NewEmployee");

        private String val;

        LoginType(String val) {
            this.val = val;
        }

        public String toString() {
            return this.val;
        }
    }

    public CompanyBranding() {
        this.loginType = LoginType.USER;
    }

    /**
     * This method actually loads up our company branding informationg with the
     * specified company db.
     * @param companyDb
     * @param client
     */
    public void loadUpCompanyInfo(String companyDb, String clientUrl) {
        if (companyDb != null && companyDb.trim().length() > 0) {
            get_company_by_database_query companyDB = new get_company_by_database_query();
            companyDB.update(companyDb);
            Connection myConn = new Connection();
            Record_Set rs = myConn.executeQuery(companyDB);
            company = new Company(rs);

            if (clientUrl != null && clientUrl.trim().length() > 0) {
                load_client_by_url_query clientUrlQuery = new load_client_by_url_query();
                myConn.setCompany(company.getId());
                clientUrlQuery.setPreparedStatement(new Object[]{clientUrl});
                rs = myConn.executeQuery(clientUrlQuery);
                myClient = new Client(new Date(), rs);
            }
        }
    }

    public void setLoginInfo(String type) {
        if (type.equalsIgnoreCase(LoginType.EMPLOYEE.toString())) {
            this.loginType = LoginType.EMPLOYEE;
        } else if (type.equalsIgnoreCase(LoginType.CLIENT.toString())) {
            this.loginType = LoginType.CLIENT;
        } else if(type.equalsIgnoreCase(LoginType.NEWEMPLOYEE.toString())){
            this.loginType = LoginType.NEWEMPLOYEE;
        }  else{
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                this.loginType = LoginType.EMPLOYEE;
            } else {
                this.loginType = LoginType.USER;
            }
        }
    }

    public ImageIcon getLoginHeader() {
        ImageIcon image = null;
        try {
            if (loginType.equals(LoginType.CLIENT)) {
                image = ImageLoader.getImage("client_header_" + myClient.getClientId() + ".jpg", this.company.getDB(), "general");
            } else {
                image = ImageLoader.getImage("headerimage.jpg", this.company.getDB(), "general");
            }
        } catch (Exception e) {

        }
        if (image == null && this.company.getCompId().equals("2")) {
            image = Main_Window.ClientLogin;
        } else if (image == null) {
            image = Main_Window.Load_Image;
        }
        return image;
    }

    public LoginType getLoginType() {
        return this.loginType;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    public String getLoginWindowText() {
        String retVal = "Schedfox Login";
        if (this.company != null) {
            retVal = company.getName() + " Schedfox Login";
        }
        if (myClient != null) {
            retVal = myClient.getClientName() + " Schedfox Login";
        }
        return retVal;
    }

}
