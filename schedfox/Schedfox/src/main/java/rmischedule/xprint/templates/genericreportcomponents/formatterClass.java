/*
 * formatterClass.java
 *
 * Created on August 31, 2005, 1:09 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.xprint.templates.genericreportcomponents;

/**
 *
 * @author Ira Juneau
 */
public abstract class formatterClass {
    
    /** Creates a new instance of formatterClass */
    public formatterClass() {
    }
    
    /**
     * Override to format stuff...
     */
    public abstract String formatInputString(String blah);
    
}
