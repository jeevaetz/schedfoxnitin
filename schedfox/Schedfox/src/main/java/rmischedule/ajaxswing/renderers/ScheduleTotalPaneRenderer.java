package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import rmischedule.main.Main_Window;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author Rybka
 */
public class ScheduleTotalPaneRenderer implements ComponentRenderer {

    public ScheduleTotalPaneRenderer() {
    }

    public void initializeDocument(Document document) {
    }

    public void renderComponent(Document document, Object component) throws Exception {
    	HTMLPage page = (HTMLPage) document;
    	double scaleValue = ComponentDimensions.getScaleValue();
        int fontSize = (int)Math.floor(14.00*scaleValue);

        JComponent comp = (JComponent) component;
        page.append("<div class=\"scheduleTotalHeader\" id=\"");
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

        Graphics g = comp.getGraphics();
        g.setFont(Main_Window.totals_font);
        //g.drawString("Total", (50 - g.getFontMetrics().getStringBounds("Total", g).getBounds().width) / 2, (50 + g.getFont().getSize()) / 2);

        page.append("<span style=\"position:absolute;left:");
        page.append((int)Math.floor(((45 - g.getFontMetrics().getStringBounds("Total", g).getBounds().width) / 2)*scaleValue));
        page.append("px;top:");
        page.append((int)Math.floor((25 - (g.getFont().getSize() / 2))*scaleValue));
        page.append("px;font-size:");
        page.append(fontSize);
        page.append("px;color:#000000\">");
        page.appendText("Total");
        page.append("</span>");

        page.appendln("</div>");
    }


}
