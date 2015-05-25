/*
 * ICurrentPage.java
 *
 * Created on September 29, 2006, 9:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sjq.print;

/**
 * Simple interface that allows components in a document to use their current page as part of their content.
 *
 * @author shawn
 */
public interface ICurrentPage {
    
    /**
     * Components in a document might want to use the current page number as part of their content.
     * This method will be called when any component that implements this interface is added to a
     * page in the document.  Note that this will only be called for items added to the document's
     * body, not headers or footers -- the delegate responsible for generating headers and footers
     * already has access to the current page number.
     *
     * @param   curPage     the page this component is on in the document
     */
    public void setCurrentPage(int curPage);
}
