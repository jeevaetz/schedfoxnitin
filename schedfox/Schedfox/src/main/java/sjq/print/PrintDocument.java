/*
 * PrintDocument.java
 *
 * Created on September 22, 2006, 2:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

import java.util.*;
import java.awt.*;
import java.awt.print.*;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

/**
 * Part of the new printing API.  This is basically just a list of components.  These components will be added
 * to pages in order -- if a component doesn't fit on the current page, a new page will be added and it will be
 * the first component on that page.  Since we're using JPanels and Java's layout managers to deal with this,
 * they actually have to be displayed on the screen in some fashion before they can be printed.  Any changes
 * that need to be made to a document should be made before a print preview is opened.
 *
 * @author shawn
 */
public class PrintDocument implements Pageable {
    
    private static final DefaultFooterDelegate defaultFooterDelegate = new DefaultFooterDelegate();
    private static final NullDelegate nullDelegate = new NullDelegate();
    
    private ArrayList<Component> componentList;
    private ArrayList<PrintPage> pageList;
    
    private IPrintHeader headerDelegate;
    private IPrintFooter footerDelegate;
    
    private PageFormat pageFormat;
    private PrintRequestAttributeSet printAttributes;
    
    private String documentName;
    
    /** Creates a new instance of PrintDocument */
    public PrintDocument(String docName) {
        this.printAttributes = new HashPrintRequestAttributeSet();
        this.pageList = new ArrayList<PrintPage>();
        this.componentList = new ArrayList<Component>();
        this.setHeaderDelegate(null);
        this.setFooterDelegate(null);
        PageFormat pf = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(8.5 * 72.0, 11.0 * 72.0);
        paper.setImageableArea(36.0, 36.0, 7.5 * 72.0, 10 * 72.0);
        pf.setPaper(paper);        
        this.setPageFormat(pf);
        this.documentName = docName;
    }
    
    public PrintDocument() {
        this("");
    }
    
    /**
     * This clears all current pages in this document, and reconstructs the document.
     * This should be called after all components have been added to this document,
     * and after the pageformat has been set.  If you change the page format, you will
     * need to call this for the changes to take effect.  This should only ever be called
     * by a PrintPreviewForm.
     */
    void setupPages(PrintPreviewPanel ppp) {
        this.pageList.clear();
        int idx = 0;
        int currentPage = 1;
        while(idx < this.componentList.size()) {
            PrintPage page = new PrintPage();
            page.setPageFormat(this.pageFormat);
            page.setHeader(this.headerDelegate.getHeader(currentPage));
            page.setFooter(this.footerDelegate.getFooter(currentPage));
            ppp.displayPage(page);
            int oldIdx = idx;
            while(idx < this.componentList.size() && page.addComponent(this.componentList.get(idx))) {
                idx++;
                if(this.componentList.get(idx-1) instanceof ICurrentPage) {
                    ((ICurrentPage)this.componentList.get(idx-1)).setCurrentPage(currentPage);
                }
                if(this.componentList.get(idx-1) instanceof PageBreak) {
                    break;
                }
            }
            page.addComponent(new PageSpacer());
            this.pageList.add(page);
            if(oldIdx == idx) {
                JOptionPane.showMessageDialog(ppp, "Component is larger than the printable area on a single page!", "Unable to add component to document!", JOptionPane.ERROR_MESSAGE);
                break;
            }            
            currentPage++;
        }
        
        //if there aren't any pages, add one blank page to display
        if(this.pageList.size() == 0) {
            PrintPage page = new PrintPage();
            page.setPageFormat(this.pageFormat);
            page.addComponent(new PageSpacer());
            this.pageList.add(page);
            ppp.displayPage(page);
        }
        
        //go through all the components and see if any need to use the number of pages in the
        //document as part of their content
        for(Component c : this.componentList) {
            if(c instanceof ITotalPages) {
                ((ITotalPages)c).setTotalPages(this.getNumberOfPages());
            }
        }
        
        //do the same for headers and footers
        for(PrintPage p : this.pageList) {
            JPanel head = p.getHeader();
            JPanel foot = p.getFooter();
            
            if(head != null && head instanceof ITotalPages) {
                ((ITotalPages)head).setTotalPages(this.getNumberOfPages());
            }
            
            if(foot != null && foot instanceof ITotalPages) {
                ((ITotalPages)foot).setTotalPages(this.getNumberOfPages());
            }
        }
    }
    
    /**
     * Sets the page format for every page in this document.
     *
     * @param   pf  the page format to use
     */
    public void setPageFormat(PageFormat pf) {
        this.pageFormat = pf;
        
        float x, y, w, h;
        
        x = (float)(pf.getImageableX() / 72.0);
        y = (float)(pf.getImageableY() / 72.0);
        w = (float)(pf.getImageableWidth() / 72.0);
        h = (float)(pf.getImageableHeight() / 72.0);
        
        if(pf.getOrientation() == PageFormat.LANDSCAPE) {
            this.printAttributes.add(new MediaPrintableArea(y, x, h, w, MediaPrintableArea.INCH));
            this.printAttributes.add(OrientationRequested.LANDSCAPE);
        } else if (pf.getOrientation() == PageFormat.PORTRAIT) {
            this.printAttributes.add(new MediaPrintableArea(x, y, w, h, MediaPrintableArea.INCH));
            this.printAttributes.add(OrientationRequested.PORTRAIT);
        } else if (pf.getOrientation() == PageFormat.REVERSE_LANDSCAPE) {
            this.printAttributes.add(new MediaPrintableArea(y, x, h, w, MediaPrintableArea.INCH));
            this.printAttributes.add(OrientationRequested.REVERSE_LANDSCAPE);
        }
    }
    
    /**
     * Adds a component to this document.  This component will be added to the last page
     * in the document if there is room.  If there isn't room, a new page will be added
     * to the document and the component will be added to that page.  The body of the pages
     * use a BoxLayout on the Y-Axis, so it is highly recommended that any component you add
     * have its minimum and maximum sizes set properly to ensure the component is sized
     * correctly.
     *
     * @param   c   the component to be added
     */
    public void addComponent(Component c) {
        this.componentList.add(c);
    }
    
    /**
     * Method to set the delegate responsible for generating headers for the pages
     * of this document.  Passing null in will result in no header being displayed.
     *
     * @param   del the {@link:IPrintHeader} to create headers for this document
     */
    public void setHeaderDelegate(IPrintHeader del) {
        if(del != null) {
            this.headerDelegate = del;
        } else {
            this.headerDelegate = nullDelegate;
        }
    }
    
    /**
     * Method to set the delegate responsible for generating footers for the pages
     * of this document.  Passing null in will result in no header being displayed.
     *
     * @param   del the {@link:IPrintFooter} to create footers for this document
     */
    public void setFooterDelegate(IPrintFooter del) {
        if(del != null) {
            this.footerDelegate = del;
        } else {
            this.footerDelegate = nullDelegate;
        }
    }
    
    /**
     * Gets the current header delegate for this document.
     *
     * @return  the current header delegate
     */
    public IPrintHeader getHeaderDelegate() {
        return this.headerDelegate;
    }
    
    /**
     * Gets the current footer delegate for this document.
     *
     * @return  the current footer delegate
     */
    public IPrintFooter getFooterDelegate() {
        return this.footerDelegate;
    }
    
    /**
     * Gets the name for this document.
     *
     * @return  the name of the document
     */
    public String getDocumentName() {
        return this.documentName;
    }
    
    /**
     * Sets the name of this document.
     *
     * @param   name    the name of the document
     */
    public void setDocumentName(String name) {
        this.documentName = name;
    }
    
    /**
     * Forces a new page to be created at the current position in the component list.
     */
    public void addPageBreak() {
        this.componentList.add(new PageBreak());
    }
    
    /**
     * Returns a {@link:PrintRequestAttributeSet} associated with this document so we can
     * easily use the cross-platform print dialogs built into Java.
     *
     * @returns the {@link:PrintRequestAttributeSet} for this document
     */
    public PrintRequestAttributeSet getPrintAttributes() {
        return this.printAttributes;
    }
    
    /**
     * Returns a simple footer delegate.
     *
     * @return  the default footer delegate
     */
    public static IPrintFooter getDefaultFooterDelegate() {
        return defaultFooterDelegate;
    }
    
    /**
     * Returns a simple header delegate.
     *
     * @param   title   the title to use as the header
     * @return          the default header delegate
     */
    public static IPrintHeader getDefaultHeaderDelegate(String title) {
        return new DefaultHeaderDelegate(title);
    }

    public int getNumberOfPages() {
        return this.pageList.size();
    }

    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return this.pageFormat;
    }

    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return pageList.get(pageIndex);
    }
    
    private static class NullDelegate implements IPrintHeader, IPrintFooter {
        public JPanel getHeader(int pageNum) {
            return null;
        }

        public JPanel getFooter(int pageNum) {
            return null;
        }
    }
    
    class PageBreak extends JComponent { }
    
    class PageSpacer extends JPanel {
        public PageSpacer(Color c) {
            Dimension zero = new Dimension(0, 0);
            Dimension max  = new Dimension(32767, 32767);
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setMinimumSize(zero);
            this.setPreferredSize(zero);
            this.setMaximumSize(max);
            this.setBackground(c);
        }
        
        public PageSpacer() {
            this(Color.WHITE);
        }
    }
    
}
