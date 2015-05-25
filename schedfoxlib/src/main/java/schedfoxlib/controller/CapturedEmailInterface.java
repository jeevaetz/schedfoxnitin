/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.CapturedEmail;

/**
 *
 * @author ira
 */
public interface CapturedEmailInterface {
    public void saveCapturedemail(CapturedEmail email) throws SaveDataException;
}
