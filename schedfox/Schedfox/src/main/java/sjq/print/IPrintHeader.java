/*
 * IPrintHeader.java
 *
 * Created on September 25, 2006, 12:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import javax.swing.*;

/**
 * Silly little interface that fetches a header for the specified page number of
 * a print document.
 *
 * @author shawn
 */
public interface IPrintHeader {
    
    /**
     * Gets a JPanel that will be used as the header for this page number.
     * <p>
     * The header is added to the page with a BorderLayout.NORTH orientation,
     * so ensure that the preferred size of the returned header is set correctly.
     *
     * @param   pageNum the page number
     * @return          the JPanel to be used as the header
     */
    public JPanel getHeader(int pageNum);
}
