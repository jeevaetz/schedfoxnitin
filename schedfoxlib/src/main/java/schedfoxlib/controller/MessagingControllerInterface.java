/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.MessagingCommunication;

/**
 *
 * @author ira
 */
public interface MessagingControllerInterface {
    public void saveMessagingCommunication(MessagingCommunication messageComm) throws SaveDataException;
    public ArrayList<MessagingCommunication> getUnsentCommunication() throws RetrieveDataException;
    public MessagingCommunication getMessageCommunication(String email) throws RetrieveDataException;
}
