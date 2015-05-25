/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public interface OutlookControllerInterface {
    public boolean attemptEmailLogin(Integer userId);
    public boolean checkEmailPassword(String email, String password);
    public void downloadCalendar(Integer userId);
}
