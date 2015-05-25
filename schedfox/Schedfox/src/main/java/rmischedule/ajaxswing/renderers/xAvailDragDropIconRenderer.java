package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import rmischedule.employee.components.xavailability.xAvailDragDropIcon;

/**
 *
 * @author Rybka
 */
public class xAvailDragDropIconRenderer implements ComponentRenderer {

     public xAvailDragDropIconRenderer() {
    }

    public void renderComponent(Document document, Object component) throws Exception {
        xAvailDragDropIcon control = (xAvailDragDropIcon) component;
        HTMLPage page = (HTMLPage) document;

        page.append("<div id=\"");
        page.append(page.getComponentName(control));
        page.append("\"");
        page.getPageRenderer().appendCommonAttributes(page, control);
        page.getPageRenderer().appendComponentStyle(page, control, false);
        page.appendln(">");
        if (control.getMyImage() != null) {
        page.append("<img ");
            page.append(" style=\"");
            page.append("position:absolute; left:0; top:0;\"");
            page.append(" src=\"");
            page.append(page.getImageUrl(control.getMyImage()).toString());
            page.append("\" height=\"100%\" width=\"100%\" />");
        }
        page.appendln("</div>");
    }

    public void initializeDocument(Document doc) {

    }

}
