/*
 * PrintPage.java
 *
 * Created on September 22, 2006, 2:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;

/**
 * Part of the new printing API.  A panel representing a single page in a document.
 * Components will be added to the body panel of one of these objects, which by
 * default has a BoxLayout on the Y-Axis.  Any components you add should have their
 * minimum and maximum sizes set properly.  In addition to the body, a header or footer
 * can be added to the page.
 *
 * @author shawn
 */
class PrintPage extends JPanel implements Printable {
    
    private JPanel header;
    private JPanel footer;
    private JPanel body;
    
    /** Creates a new instance of PrintPage */
    PrintPage() {
        this.setBackground(Color.WHITE);
        this.body = new JPanel();
        this.body.setLayout(new BoxLayout(this.body, BoxLayout.Y_AXIS));
        this.body.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        this.add(this.body, BorderLayout.CENTER);
    }    
    
    /**
     * Adds a component to this page.  Returns true if the component was added
     * successfully, and false if there wasn't enoguh room left on the page.
     *
     * @param   c   the Component to be added
     * @return      true if the component could be added, false otherwise
     */
    boolean addComponent(Component c) {
        this.body.add(c);
        this.body.validate();

        int height = c.getMinimumSize().height > c.getHeight() ? c.getMinimumSize().height : c.getHeight();
        
        if((height + c.getY()) > this.body.getHeight() && !(c instanceof PrintDocument.PageSpacer)) {
            this.body.remove(c);
            this.body.validate();
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Sets the page's layout using the given page format.
     *
     * @param   pf  the page format to use on this page
     */
    void setPageFormat(PageFormat pf) {
        Dimension pageSize;
        int leftMargin, rightMargin, topMargin, bottomMargin;
        
        pageSize = new Dimension((int)pf.getWidth(), (int)pf.getHeight());
        leftMargin = (int)pf.getImageableX();
        rightMargin = pageSize.width - (int)(pf.getImageableX() + pf.getImageableWidth());
        topMargin = (int)pf.getImageableY();
        bottomMargin = pageSize.height - (int)(pf.getImageableY() + pf.getImageableHeight());

        this.setMinimumSize(pageSize);
        this.setMaximumSize(pageSize);
        this.setPreferredSize(pageSize);
        this.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));
    }
  
    /**
     * Sets the header for this page.
     *
     * @param   head    the panel to be used as the header
     */
    void setHeader(JPanel head) {
        if(this.header != null) {
            this.remove(this.header);
        }
        
        if(head != null) {
            this.header = head;
            this.add(head, BorderLayout.NORTH);
        }
    }
    
    /**
     * Gets the header for this page.
     *
     * @return  the header for this page
     */
    JPanel getHeader() {
        return this.header;
    }

    /**
     * Sets the footer for this page.
     *
     * @param   foot    the panel to be used as the footer
     */
    void setFooter(JPanel foot) {
        if(this.footer != null) {
            this.remove(this.header);
        }
        
        if(foot != null) {
            this.footer = foot;
            this.add(foot, BorderLayout.SOUTH);
        }
    }
    
    /**
     * Gets the footer for this page.
     *
     * @return  the footer for this page
     */
    JPanel getFooter() {
        return this.footer;
    }

    public int print(Graphics g, PageFormat pf, int idx) throws PrinterException {
        RepaintManager currentManager = RepaintManager.currentManager(this);
        currentManager.setDoubleBufferingEnabled(false);
        this.paintAll(g);
        currentManager.setDoubleBufferingEnabled(true);

        return this.PAGE_EXISTS;
    }
    
}
