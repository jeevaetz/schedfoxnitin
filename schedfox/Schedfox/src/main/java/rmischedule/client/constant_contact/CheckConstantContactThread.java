/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.client.constant_contact;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.security.User;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.ClientDisplayContact;
import schedfoxlib.model.Company;

/**
 *
 * @author ira
 */
public class CheckConstantContactThread extends Thread {

    @Override
    public void run() {
        try {
            sleep(1000 * 60 * 5);
        } catch (Exception exe) {
        }
        Vector<Company> companies = Main_Window.getActiveListOfCompanies();
        User user = Main_Window.parentOfApplication.getUser();
        Connection conn = new Connection();
        boolean isCorporateUser = Main_Window.parentOfApplication.isUserAMemberOfGroups(conn, "Courtesy Call Manager");
        isCorporateUser = isCorporateUser || Main_Window.parentOfApplication.isUserAMemberOfGroups(conn, "Corporate User");
        boolean isDistrictManager = Main_Window.parentOfApplication.isUserAMemberOfGroups(conn, "District Manager");

        for (int c = 0; c < companies.size(); c++) {
            Company comp = companies.get(c);
            if (comp.getCompId().equals("2")) {
                int corporateUserNumDays = Main_Window.parentOfApplication.getNumberOfDaysForCorporateUser(comp.getCompId());
                ClientController clientController = ClientController.getInstance(comp.getCompId());
                if (isCorporateUser || isDistrictManager) {
                    while (true) {
                        try {
                            try {
                                sleep(1000 * 60 * 60); //Every hour - aggravate user
                            } catch (Exception e) {
                            }

                            ArrayList<ClientDisplayContact> clientsWithoutContact = clientController.getClientsToBeContacted(corporateUserNumDays, Integer.parseInt(user.getUserId()), isCorporateUser);
                            int numberNotContactedThisWeek = 0;
                            for (int cli = 0; cli < clientsWithoutContact.size(); cli++) {
                                if (clientsWithoutContact.get(cli).getCurrentWeekTotal() == 0) {
                                    numberNotContactedThisWeek++;
                                }
                            }
                            if (numberNotContactedThisWeek > 0) {
                                int decision = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "There are currently " + numberNotContactedThisWeek + " number of clients that need to be called on your contact list, would you like to view these now?", "View Clients", JOptionPane.YES_NO_OPTION);
                                if (decision == JOptionPane.YES_OPTION) {
                                    ConstantContact constantContact = new ConstantContact();
                                    constantContact.setInformation(comp.getCompId(), "0");
                                    Main_Window.parentOfApplication.desktop.add(constantContact);
                                    constantContact.setVisible(true);
                                }
                            }
                        } catch (Exception exe) {
                        }
                    }
                }
            }
        }
    }
}
