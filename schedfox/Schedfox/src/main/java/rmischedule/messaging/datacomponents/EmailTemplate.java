/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.messaging.datacomponents;

/**
 *
 * @author vnguyen
 */
public class EmailTemplate {
    private String id;
    private String name;
    private String subject;
    private String message;
    public EmailTemplate(String id, String name, String subject, String message){
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.message = message;
    }
    public String toString(){
        return getName();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
