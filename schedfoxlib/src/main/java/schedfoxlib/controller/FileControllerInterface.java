/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.HashMap;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.FileModel;

/**
 *
 * @author user
 */
public interface FileControllerInterface {
    public HashMap<Integer, ArrayList<String>> getPostInstructionsForClient(ArrayList<Integer> clientIds) throws RetrieveDataException;
    public ArrayList<String> getPostInstructionsForClient(Integer clientId) throws RetrieveDataException;
    public FileModel getFile(String fileName) throws RetrieveDataException;
}
