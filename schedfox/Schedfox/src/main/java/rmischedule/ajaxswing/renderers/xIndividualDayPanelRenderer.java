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
import rmischedule.employee.components.xavailability.xIndividualDayPanel;

/**
 *
 * @author Rybka
 */
public class xIndividualDayPanelRenderer implements ComponentRenderer {

    public void renderComponent(Document document, Object component) throws Exception {
        Color bgColor;
        Color fontColor;
        xIndividualDayPanel control = (xIndividualDayPanel) component;
        HTMLPage page = (HTMLPage) document;
        String bgColorStr = "";

        if (control.getIsActive()) {
            fontColor = Color.BLACK;
        } else {
            fontColor = Color.LIGHT_GRAY;
        }

        page.append("<div");
        page.append(" id=\"");
        page.append(page.getComponentName(control));
        page.append("\"");

        bgColor = control.getBackground();
        bgColorStr = "background-color: rgb("+bgColor.getRed()+","
                                             +bgColor.getGreen()+","
                                             +bgColor.getBlue()+")";

        String customStyle = "border-style:outset;border-width:1px;;border-color:white;"+bgColorStr;
                page.getPageRenderer().appendCommonAttributes(page, control);
        Rectangle rect = page.getPageRenderer().getComponentRect(page, control);
        page.getPageRenderer().appendCommonAttributes(page, control);
                page.append(" style=\"");
		page.getPageRenderer().appendComponentStyle(page, control, true,
				customStyle);
		page.append(";width:");
		page.append(rect.width - 2);
		page.append("px;height:");
		page.append(rect.height - 2);
		page.appendln("px\">");

           if (control.getActiveIcon().getFadedImage() != null) {

        	   if(control.getDisplayIcon()!=null) {
	            page.append("<img ");
	            page.append(" style=\"");
	            page.append("position:absolute; left:1px; top:1px;\"");
	            page.append(" src=\"");
	            page.append(page.getImageUrl(control.getDisplayIcon(),control));
	            page.append("\" height=\"93%\" width=\"93%\" />");
        	   }
        }

            page.append("<span");
            page.append(" style=\"");

            page.append("\">");

                page.append("<span class=\"alignright\"");
                page.append(" style=\"color:#");
                ColorFontMap.appendHexColor(page, fontColor);
                page.append("\">");
                page.append(control.getDayOfMonth());
                page.appendln("</span>");

                page.append("<span class=\"startTime\"");
                page.append(" style=\"color:#");
                ColorFontMap.appendHexColor(page, fontColor);
                page.append("\">");
                page.append(control.getActiveIcon().getStart());
                page.appendln("</span>");

                page.append("<span class=\"endTime\"");
                page.append(" style=\"color:#");
                ColorFontMap.appendHexColor(page, fontColor);
                page.append("\">");
                page.append(control.getActiveIcon().getEnd());
                page.appendln("</span>");

           page.appendln("</span>");

        page.appendln("</div>");
    }

    public void initializeDocument(Document dcmnt) {    }

}
