/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;

import java.awt.Color;

import rmischedule.components.graphicalcomponents.PrettyButton;
import rmischedule.schedule.components.AEmployee;
/**
 *
 * @author user
 */
public class AEmployeeRenderer implements ComponentRenderer {

    public AEmployeeRenderer() {
    }

    public void initializeDocument(Document document) {
    }

    public void renderComponent(Document document, Object component) throws Exception {
        Color bgColor;
        AEmployee control = (AEmployee) component;
        HTMLPage page = (HTMLPage) document;
        StringBuffer html = new StringBuffer();

        String controlName = page.getComponentName(control);

        String timeClass = "EmpUndertime";
        int index = control.getColorIndex();
        if (index == PrettyButton.yellow) {
            timeClass = "EmpClose";
        } else if (index == PrettyButton.red) {
            timeClass = "EmpOvertime";
        }

        html.append("<div class=\"AEmployee " + timeClass + "\" ");
        html.append("style=\"top:" + control.getY() + "px;\" ");
        html.append("onmouseout=\"onHideDynamicToolTip()\" onmouseover=\"onShowDynamicToolTip('AEmployee_921447026')\" ");
        html.append("onDblClick=\"doSubmit('/event/" + controlName + "/mouseclick')\" ");
        html.append("dragSource=\"true\" dragTarget=\"true\" dragSource=\"true\" dragTarget=\"true\" ");
        html.append("id=\"" + controlName + "\">");

        html.append("<div class=\"border\" >");

        html.append("<span id=\"JLabel_1484208833\" class=\"label\">");
        html.append("<span class=\"AELabel\">" + control.getDisplayName() + "</span>");
        html.append("</div>");
        html.append("</div>");
        page.append(html.toString());

    }

}

