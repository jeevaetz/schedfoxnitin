/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messaging.datacomponents;

import java.util.Date;

/**
 *
 * @author vnguyen
 */
public class Template implements Comparable {
    // Data Fields

    private String templateName = new String("");
    private String template_db_id = new String("");
    private String sms_type_db_id = new String("");
    private StringBuilder template = new StringBuilder("");

    public Template() {
    }

    public Template(String templateName, String template_db_id, String sms_type_db_id, String template) {
        this.templateName = templateName;
        this.template_db_id = template_db_id;
        this.sms_type_db_id = sms_type_db_id;
        this.template.append(template);
    }

    @Override
    public String toString() {
        return this.getTemplateName();
    }

    public int compareTo(Object o) {
        if (o instanceof Template) {
            Template t = (Template)o;
            return t.getTemplateName().compareTo(this.getTemplateName());
        } else {
            return -1;
        }
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }
    public String getTemplate(){
        return this.template.toString();
    }

    /**
     * @return the template_db_id
     */
    public String getTemplate_db_id() {
        return template_db_id;
    }

    /**
     * @return the sms_type_db_id
     */
    public String getSms_type_db_id() {
        return sms_type_db_id;
    }

    /**
     * @param sms_type_db_id the sms_type_db_id to set
     */
    public void setSms_type_db_id(String sms_type_db_id) {
        this.sms_type_db_id = sms_type_db_id;
    }
}
