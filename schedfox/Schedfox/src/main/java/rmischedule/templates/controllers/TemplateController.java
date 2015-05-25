//  package declaration
package rmischedule.templates.controllers;

//  import declaration
import java.util.Collection;
import java.util.LinkedHashMap;
import rmischedule.data_connection.Connection;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.model.TemplateData;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.templates.retrieve_templates_by_id_query;

/**
 * @author Jeffrey N. Davis
 * @see rmischedule.templates.interfaces.TemplateSystemInterface
 * @since 03/09/2011
 */
public class TemplateController {

    private LinkedHashMap<Integer, TemplateData> templates;
    private boolean hasNewSave;
    private String newTemplateName;  //  if this.hasNewSave == false, default == ""

    /*  singleton code  */
    private static final TemplateController INSTANCE = new TemplateController();

    /**
     * Default construction of this object.
     */
    private TemplateController() {
        templates = new LinkedHashMap<Integer, TemplateData>();
    }

    /*  public method implementations   */
    public void init(String companyId, int templateType) {
        this.templates.clear();
        this.hasNewSave = false;
        this.newTemplateName = "";
        this.loadTemplates(companyId, templateType);
    }
    
    /**
     * Returns the one true instance of {@code ParentTemplateController}
     *
     * @returns INSTANCE
     */
    public static TemplateController getInstance() {
        return INSTANCE;
    }

    /**
     * Hits the database to load all associated templates into
     * {@code this.templates}
     */
    private LinkedHashMap<Integer, TemplateData> loadTemplates(String companyId, int templateType) {
        LinkedHashMap<Integer, TemplateData> retVal = new LinkedHashMap<Integer, TemplateData>();
        RMIScheduleServerImpl myConnection = RMIScheduleServerImpl.getInstance();
        Record_Set rs = null;

        retrieve_templates_by_id_query retrieveQuery = new retrieve_templates_by_id_query(templateType);
        if (!companyId.equalsIgnoreCase("-1")) {
            retrieveQuery.setCompany(companyId);
        }
        try {
            rs = myConnection.executeQuery(retrieveQuery, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (rs.length() > 0) {
            do {
                TemplateData data = new TemplateData(rs);
                retVal.put(-1, new TemplateData());
                retVal.put(data.hashCode(), data);
            } while (rs.moveNext());
        }
        
        return retVal;
    }

    /**
     * Determines the TemplateData element in {@code templates} by
     * {@code this.newTemplateName}, name, returns it.
     *
     * @throws RuntimeException if no template name is found, or if no
     * corresponding name is found
     */
    private TemplateData getTemplateByTemplateName() {
        if (this.newTemplateName.length() == 0) {
            throw new RuntimeException("Template Search failed:  no template name.");
        }

        Collection<TemplateData> collection = this.templates.values();
        TemplateData returnTemplate = null;
        for (TemplateData element : collection) {
            if (element.getTemplateName().equalsIgnoreCase(this.newTemplateName)) {
                returnTemplate = element;
            }
        }

        if (returnTemplate == null) {
            throw new RuntimeException("A corresponding template could not be found.  Is name out of synch?");
        }

        return returnTemplate;
    }

    /**
     * Allows the view to force a template reload
     */
    public LinkedHashMap<Integer, TemplateData> reloadTemplates(String companyId, int templateType) {
        return this.loadTemplates(companyId, templateType);
    }

    /**
     * Resets {@code this.hasNewSave, this.newTemplateName}
     */
    public void resetAfterSave() {
        this.hasNewSave = false;
        this.newTemplateName = "";
    }

    /**
     * Determines the template_value associated with the newTemplateName,
     * returns it.
     *
     * @return returnString a string representing the new template value
     */
    public String getNewTemplateValue() {
        String returnString = null;
        TemplateData element = null;

        try {
            element = this.getTemplateByTemplateName();
            returnString = element.getTemplateValue();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            returnString = "";
        }

        return returnString;
    }

    /**
     * Determines the index of the new template, returns it.
     *
     * @return newIndex an int describing the index of the new template
     */
    public int getNewTemplateIndex() {
        TemplateData element = null;
        int newIndex = 0;

        /*  use getTemplateByTemplateName for Exception handling    */
        try {
            element = this.getTemplateByTemplateName();
            int count = 0;
            boolean hasFound = false;
            Collection<TemplateData> collection = this.templates.values();
            for (TemplateData data : collection) {
                if (element.equals(data)) {
                    hasFound = true;
                } else {
                    count++;
                }
            }
            if (hasFound) {
                newIndex = count;
            }
        } catch (RuntimeException ex) {
            //ex.printStackTrace();
            newIndex = 0;
        }
        return newIndex;
    }

    /*  mutators    */
    public LinkedHashMap<Integer, TemplateData> getTemplates() {
        return this.templates;
    }

    public boolean hasNewSave() {
        return this.hasNewSave;
    }

    public void setHasNewSave(boolean hasNewSave) {
        this.hasNewSave = hasNewSave;
    }

    public void setNewTemplateName(String val) {
        this.newTemplateName = val;
    }

    public String getNewTemplateName() {
        return this.newTemplateName;
    }
};
