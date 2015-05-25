/*
 * ITotalPages.java
 *
 * Created on September 28, 2006, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

/**
 * Another little one method interface to allow components in a document to use the
 * total number of pages in a document as part of their content.
 *
 * @author shawn
 */
public interface ITotalPages {
   
    /**
     * Components in a PrintDocument, particularly headers and footers, don't know the
     * total number of pages in the document until the entire document has been created
     * and laid out.  If you need to use the total number of pages in the document for
     * a header, footer, or component in the body of a page, then provide that implementation
     * here.  This method will be called for all components in a document that implement this
     * interface after the layout of the document has been completed.  Any changes you make
     * here should not change the height of the component.
     *
     * @param   numPages    the total number of pages in the document
     */
    public void setTotalPages(int numPages);    
}
