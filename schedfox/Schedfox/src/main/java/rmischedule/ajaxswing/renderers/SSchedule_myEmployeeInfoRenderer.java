package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.ColorFontMap;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import java.awt.Rectangle;

import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.SSchedule;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author Rybka
 */
public class SSchedule_myEmployeeInfoRenderer implements ComponentRenderer {

    public SSchedule_myEmployeeInfoRenderer() {
    }

    public void initializeDocument(Document document) {
    }

    public void renderComponent(Document document, Object component) throws Exception {
    	HTMLPage page = (HTMLPage) document;
    	double scaleValue = ComponentDimensions.getScaleValue();
        int rectWidth    = ComponentDimensions.currentSizes.get("RowHeader").width;
        int rectHeight   = ComponentDimensions.currentSizes.get("RowHeader").height;
        int fontSize = (int)Math.floor(11.00*scaleValue);
        rectWidth=(int)Math.floor(rectWidth*scaleValue);
        rectHeight=(int)Math.floor(rectHeight*scaleValue);
        SSchedule.myEmployeeInfo comp = (SSchedule.myEmployeeInfo) component;
        Rectangle bounds = comp.getBounds();
        SEmployee myEmployee = comp.getMyEmployee();
        page.append("<div class=\"myEmployeeInfo\" id=\"");
        page.append(page.getComponentName(comp));
        page.append("\"");
        page.getPageRenderer().appendCommonAttributes(page, comp);
        Rectangle rect = page.getPageRenderer().getComponentRect(page, comp);
        page.append(" style=\"");
        page.getPageRenderer().appendComponentStyle(page, comp, true);
        page.append(";width:");
        page.append(rect.width-2);
        page.append("px;height:");
        page.append(rect.height-2);
        page.appendln("px\">");
        page.appendln(">");

        if (!myEmployee.isDeleted()) {
	        page.append("<div style=\"position:absolute;background-color:#");
	        ColorFontMap.appendHexColor(page, Schedule_View_Panel.client_color);
	        page.append(";left:0px;top:0px;width:");
	        page.append("100%");
	        page.append(";height:");
	        page.append(rectHeight / 3);
	        page.append("px");
	        page.appendln("\">");
	        page.appendln("</div>");
        }

        int y = bounds.y ;//+ Main_Window.employee_font.getSize();
        page.append("<span style=\"position:absolute;left:");
        page.append((int)Math.floor((bounds.x+2)*scaleValue));
        page.append("px;top:");
        page.append((int)Math.floor((y)*scaleValue));
        page.append("px;font-size:");
        page.append(fontSize);
        page.append("px;color:#000000\">");
        page.appendText(myEmployee.getName());
        page.append("</span>");
        //g.setFont(Main_Window.employee_font);
        //g.drawString(myEmployee.getName(), bounds.x + 2, y);
        //g.setFont(Main_Window.shift_font);
        y += Main_Window.shift_font.getSize() + 8;
        page.append("<span style=\"position:absolute;left:");
        page.append((int)Math.floor((bounds.x+2)*scaleValue));
        page.append("px;top:");
        page.append((int)Math.floor((y)*scaleValue));
        page.append("px;font-size:");
        page.append(fontSize);
        page.append("px;color:#000000\">");
        if (!Main_Window.isClientLoggedIn()) {
            page.appendText(myEmployee.getPhone());
        }
        page.append("</span>");
        //g.drawString(myEmployee.getPhone(), bounds.x + 2, y);
        y += Main_Window.shift_font.getSize() + 8;
        if (myEmployee.isDeleted()) {
            //g.setColor(Color.RED);
            page.append("<span style=\"position:absolute;left:");
            page.append((int)Math.floor((bounds.x+2)*scaleValue));
            page.append("px;top:");
            page.append((int)Math.floor((y)*scaleValue));
            page.append("px;font-size:");
            page.append(fontSize);
            page.append("px;color:#FF0000\">");
            page.appendText("Terminated (" + myEmployee.getReadableTerm() + ")");
            page.append("</span>");
        } else {
            page.append("<span style=\"position:absolute;left:");
            page.append((int)Math.floor((bounds.x+2)*scaleValue));
            page.append("px;top:");
            page.append((int)Math.floor((y)*scaleValue));
            page.append("px;font-size:");
            page.append(fontSize);
            page.append("px;color:#000000\">");
            if (!Main_Window.isClientLoggedIn()) {
                page.appendText(myEmployee.getPhone2());
            }
            page.append("</span>");
        }

        page.appendln("</div>");
    }

    public String cutCorrectTime(String time) {
        time = time.substring(0, 8);
        return time;
    }
}
