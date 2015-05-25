/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.AccessIndividualLogs;
import schedfoxlib.model.AccessIndividualTypes;
import schedfoxlib.model.AccessIndividuals;

/**
 *
 * @author ira
 */
public interface AccessControllerInterface {
    public ArrayList<AccessIndividualTypes> getAccessTypes() throws RetrieveDataException;

    public AccessIndividuals getAccessIndividual(Integer accessIndividualId) throws RetrieveDataException;
    
    public void saveAccessTypes(AccessIndividualTypes type) throws SaveDataException;

    public ArrayList<AccessIndividuals> getAccessIndividuals(Integer companyId) throws RetrieveDataException;

    public void saveAccessIndividual(AccessIndividuals access) throws SaveDataException;
    
    public void saveAccessIndividualLog(AccessIndividualLogs access) throws SaveDataException;
}
