/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.DefaultJComponentRenderer;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import rmischedule.schedule.components.SRow;

/**
 *
 * @author user
 */
public class SRowRenderer extends DefaultJComponentRenderer implements ComponentRenderer {

    public void initializeDocument(Document dcmnt) {
        
    }


    @Override
    public void renderComponent(HTMLPage htmlp, Object o) throws Exception {
        SRow comp = ((SRow)o);
        if (comp.isDirty()) {
            comp.addComponentsAsNeeded();
        }
        super.renderKnownComponent(htmlp, o);

        
//        htmlp.append("<div ");
//        htmlp.append("id=\"");
//        htmlp.append(page.getComponentName(o));
//        renderComponentId(htmlp, comp);
//        htmlp.append(" style=\"");
//        htmlp.getPageRenderer().appendComponentStyle(htmlp, o, true);
//        htmlp.append("\" ");
//        htmlp.append(">");
//        htmlp.append("</div>");
    }

}
