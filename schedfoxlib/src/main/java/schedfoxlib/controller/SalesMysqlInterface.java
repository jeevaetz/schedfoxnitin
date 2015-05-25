/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Lead;
import schedfoxlib.model.LeadNote;
import schedfoxlib.model.LeadType;
import schedfoxlib.model.SalesCallQueue;

/**
 *
 * @author ira
 */
public interface SalesMysqlInterface {
    public ArrayList<Lead> getLeadsById(ArrayList<SalesCallQueue> queue);
    public ArrayList<Lead> getLeadsByUpdate(Date date);
    public Integer getLeadsCount();
    public ArrayList<Lead> getLeadsByOffset(Integer startOffset, Integer limit);
    public ArrayList<Lead> getLeads();
    public ArrayList<Lead> getCallQueue(Integer userId);
    public ArrayList<LeadType> getLeadTypes();
    public ArrayList<LeadNote> getLeadNotes(Integer leadId);
    public void saveLead(Lead lead);
    public Integer saveLeadNote(LeadNote leadNote) throws SaveDataException;
}
