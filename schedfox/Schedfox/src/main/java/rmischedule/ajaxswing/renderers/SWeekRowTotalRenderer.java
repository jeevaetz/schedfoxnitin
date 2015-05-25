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
import java.awt.Rectangle;

import rmischedule.schedule.components.SWeek;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author Rybka
 */
public class SWeekRowTotalRenderer implements ComponentRenderer {

     public SWeekRowTotalRenderer() {
    }

    public void renderComponent(Document document, Object component) throws Exception {
        SWeek.RowTotal control = (SWeek.RowTotal) component;
        HTMLPage page = (HTMLPage) document;

        double scaleValue = ComponentDimensions.getScaleValue();
        double fontStartSize = 9.00;
        Color bgColor = control.getMySched().myParent.total_color;

        String customStyle ="font-family: verdana; background-color: #cecece; border: 2px double white; font-weight:bold;font-size:"+fontStartSize*scaleValue+"px";

        page.append("<div id=\"");
        page.append(page.getComponentName(control));
        page.append("\"");
        page.getPageRenderer().appendCommonAttributes(page, control);
        page.getPageRenderer().appendComponentStyle(page, control, false, customStyle);
        page.appendln(">");

        page.append("<span");
            page.append(" style=\"");
            page.append("background-color: #");
            ColorFontMap.appendHexColor(page, bgColor);

            int borderSize = (int)Math.floor(3*scaleValue);
            Rectangle rect = page.getPageRenderer().getComponentRect(page, control);
            int hSize =  rect.height - 2*borderSize;
            int wSize = rect.width - 2*borderSize;

            page.append("height:"
                        +hSize+"px;width:"
                        +wSize+"px;");
            page.append("\">");

        page.append("<span");
        page.append(" style=\"");
        page.append("left:25%;");
        page.append("top:15%;");
        page.append("position:absolute");
        page.append("\">");
             page.append(control.getMyVal());
        page.appendln("</span>");
        page.appendln("</span>");
        page.appendln("</div>");
    }

    public void initializeDocument(Document document) {    }


}
