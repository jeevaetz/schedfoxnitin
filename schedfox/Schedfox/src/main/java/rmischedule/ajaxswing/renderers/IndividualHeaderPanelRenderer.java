package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.ColorFontMap;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import java.awt.Graphics;

import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author Rybka
 */
public class IndividualHeaderPanelRenderer implements ComponentRenderer {

    public IndividualHeaderPanelRenderer() {
    }

    public void initializeDocument(Document document) {
    }

    public void renderComponent(Document document, Object component) throws Exception {
    	HTMLPage page = (HTMLPage) document;
    	double scaleValue = ComponentDimensions.getScaleValue();
        int fontSize = (int)Math.floor(20.00*scaleValue);
        int fontSize2 = (int)Math.floor(13.00*scaleValue);

        Schedule_View_Panel.individualHeaderPanel comp = (Schedule_View_Panel.individualHeaderPanel) component;
        page.append("<div class=\"individualClienHeader\" id=\"");
        page.append(page.getComponentName(comp));
        page.append("\"");
        page.getPageRenderer().appendCommonAttributes(page, comp);
        page.append(" style=\"");
        page.getPageRenderer().appendComponentStyle(page, comp, true);
        page.append(";background-color:#");
        ColorFontMap.appendHexColor(page, Schedule_View_Panel.hcol_color);
        page.appendln(";\">");

        Graphics g = comp.getGraphics();
        g.setFont(Main_Window.header_font);
        int y = 5;

        page.append("<span style=\"position:absolute;left:");
        page.append((int)Math.floor(((60 - g.getFontMetrics().getStringBounds(comp.getMyDay(), g).getBounds().width) / 2)*scaleValue));
        page.append("px;top:");
        page.append((int)Math.floor((y)*scaleValue));
        page.append("px;font-size:");
        page.append(fontSize);
        page.append("px;color:#000000\">");
        page.appendText(comp.getMyDay());
        page.append("</span>");
        y += g.getFont().getSize() + 4;
        g.setFont(Main_Window.date_font);
        page.append("<span style=\"position:absolute;left:");
        page.append((int)Math.floor(((60 - g.getFontMetrics().getStringBounds(comp.getMyDate(), g).getBounds().width) / 2)*scaleValue));
        page.append("px;top:");
        page.append((int)Math.floor((y)*scaleValue));
        page.append("px;font-size:");
        page.append(fontSize2);
        page.append("px;color:#000000\">");
        page.appendText(comp.getMyDate());
        page.append("</span>");


        page.appendln("</div>");
    }

  }
