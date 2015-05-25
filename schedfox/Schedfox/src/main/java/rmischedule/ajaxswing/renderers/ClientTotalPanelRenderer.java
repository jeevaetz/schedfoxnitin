/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.ColorFontMap;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import java.awt.Color;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.TotalPanel;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author user
 */
public class ClientTotalPanelRenderer implements ComponentRenderer {

    public void renderComponent(Document document, Object component) throws Exception {

        TotalPanel control = (TotalPanel) component;
        HTMLPage page = (HTMLPage) document;

        Color bgColor = Schedule_View_Panel.total_color;
         //Color bgColor = Color.RED;ambiguous
        double scaleValue = ComponentDimensions.getScaleValue();
        double fontStartSize = 9.00;

        String customStyle ="font-family: verdana; font-weight:bold;font-size:"+fontStartSize*scaleValue+"px";

        page.append("<div id=\"");
        page.append(page.getComponentName(control));
        page.append("\"");
        page.getPageRenderer().appendCommonAttributes(page, control);
        page.getPageRenderer().appendComponentStyle(page, control, false, customStyle);
        page.appendln(">");
        page.append("<span");
            page.append(" style=\"");
            page.append("background-color:#");
            ColorFontMap.appendHexColor(page, bgColor);
            int hStartSize = 17;
//            int wStartSize = 59 ;
            int borderStartSize = 1;
            //int left = 1;
            page.append(";border-width:"
                        +borderStartSize*scaleValue+"px;border-style:outset;border-color:white;height:"
                        +hStartSize*scaleValue+"px;width:96%;position:absolute;");
            page.append("\">");

        page.append("<span");
            page.append(" style=\"");
            page.append("left:30%;");
            page.append("top:15%;");
            page.append("position:absolute");
            page.append("\">");
                 if (!control.shouldDisplayNumberOfShiftsForClientTotal()) {
                      page.append(Double.toString(control.getMyVal()));
                 } else {
                      page.append(Integer.toString(control.getNumShifts()));
                 }
        page.appendln("</span>");

        page.appendln("</span>");
        page.appendln("</div>");
    }

    public void initializeDocument(Document document) {  }

}
