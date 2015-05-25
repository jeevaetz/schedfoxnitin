/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.templates.get_template_by_name_query;
import rmischeduleserver.mysqlconnectivity.queries.templates.retrieve_templates_by_id_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.Template;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class TemplateController {
    
    private String companyId;
    
    private TemplateController(String companyId) {
        this.companyId = companyId;
    }
    
    public static TemplateController getInstance(String companyId) {
        return new TemplateController(companyId);
    }
    
    public ArrayList<Template> getTemplatesByType(Integer type) throws RetrieveDataException {
        ArrayList<Template> retVal = new ArrayList<Template>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        retrieve_templates_by_id_query query = new retrieve_templates_by_id_query(type);
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Template(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<Template> getTemplatesByName(String name) throws RetrieveDataException {
        ArrayList<Template> retVal = new ArrayList<Template>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_template_by_name_query query = new get_template_by_name_query();
        query.setPreparedStatement(new Object[]{name});
        query.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Template(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
