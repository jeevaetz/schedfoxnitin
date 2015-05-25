/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.SalesCall;
import schedfoxlib.model.SalesExpense;
import schedfoxlib.model.SalesExpenseImage;
import schedfoxlib.model.SalesExpenseType;
import schedfoxlib.model.SalesItinerary;
import schedfoxlib.model.SalesItineraryType;

/**
 *
 * @author ira
 */
public interface SalesControllerInterface {
    public ArrayList<SalesExpenseType> getSalesExpenseTypes() throws RetrieveDataException;
    public ArrayList<SalesExpense> getSalesExpense(Integer userId) throws RetrieveDataException;
    public ArrayList<SalesExpense> getSalesExpense(Integer userId, boolean isDeleted) throws RetrieveDataException;
    public ArrayList<SalesItinerary> getSalesItineraryForMonth(Integer userId, Date monthDate) throws RetrieveDataException;
    public Integer saveSalesExpense(SalesExpense sales) throws SaveDataException;
    public ArrayList<SalesItineraryType> getSalesItineraryTypes() throws RetrieveDataException;
    public void saveCallLogs(ArrayList<SalesCall> calls) throws SaveDataException;
    public ArrayList<SalesCall> getCallLogs(Integer userId) throws RetrieveDataException;
    public void saveSalesItinerary(SalesItinerary itinerary) throws SaveDataException;
    public ArrayList<SalesItinerary> getSalesItineraryForUserId(int userId) throws RetrieveDataException;
    public SalesExpenseImage getExpenseImage(Integer imageId) throws RetrieveDataException;
    public ArrayList<SalesExpenseImage> getImagesForExpense(Integer expense, boolean loadByteData) throws RetrieveDataException;
    public void saveSalesImage(SalesExpenseImage image) throws SaveDataException;
}
