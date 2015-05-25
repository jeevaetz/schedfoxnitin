package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.IndividualClientHeader;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author Rybka
 */
public class SClientIndividualClienHeaderRenderer implements ComponentRenderer {

    public SClientIndividualClienHeaderRenderer() {
    }

    public void renderComponent(Document document, Object component) throws Exception {
        IndividualClientHeader control = (IndividualClientHeader) component;
        HTMLPage page = (HTMLPage) document;

        page.append("<div id=\"");
        page.append(page.getComponentName(control));
        page.append("\"");

        double scaleValue = ComponentDimensions.getScaleValue();
        double fontStartSize = 16.00;

        String customStyle = "font-size:" + fontStartSize * scaleValue
                + "px; font-weight:bold;"
                + "background-color:" + CustomRendererUtil.getHTMLBackgroundColorAsRGB(Schedule_View_Panel.client_color);
        page.getPageRenderer().appendCommonAttributes(page, control);
        page.getPageRenderer().appendComponentStyle(page, control, false, customStyle);
        page.appendln(">");

        page.append("<span");
        page.append(" style=\"");
        page.append("top:30%;");
        page.append("left:1%;");
        page.append("position:absolute;");
        page.append("\">");
        page.append(control.getSName());
        page.appendln("</span>");

        page.appendln("</div>");
    }

    public void initializeDocument(Document document) {
    }
}
