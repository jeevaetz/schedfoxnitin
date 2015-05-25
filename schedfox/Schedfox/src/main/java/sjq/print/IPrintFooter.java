/*
 * IPrintFooter.java
 *
 * Created on September 25, 2006, 12:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import javax.swing.*;

/**
 * Silly little interface that fetches a footer for the specified page number of
 * a print document.
 *
 * @author shawn
 */
public interface IPrintFooter {
    
    /**
     * Gets a JPanel that will be used as the footer for this page number.
     * <p>
     * The footer is added to the page with a BorderLayout.SOUTH orientation,
     * so ensure that the preferred size of the returned footer is set correctly.
     *
     * @param   pageNum the page number
     * @return          the JPanel to be used as the footer
     */    
    public JPanel getFooter(int pageNum);
}
