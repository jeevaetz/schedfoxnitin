/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.controller;

import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientRating;

/**
 *
 * @author ira
 */
public interface ClientRatingControllerInterface {
    public ClientRating getLastClientRating(int client_id) throws RetrieveDataException;
    
    public void saveRating(ClientRating clientRating) throws SaveDataException;
}
