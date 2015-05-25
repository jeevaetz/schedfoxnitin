package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.ColorFontMap;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import java.awt.Rectangle;

import rmischedule.schedule.components.ClientPrinterPanel;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

/**
 *
 * @author Rybka
 */
public class SClientClientPrinterPanelRenderer implements ComponentRenderer {

     public SClientClientPrinterPanelRenderer() {

    }

    public void renderComponent(Document document, Object component) throws Exception {

        ClientPrinterPanel control = (ClientPrinterPanel) component;
        HTMLPage page = (HTMLPage) document;

        double scaleValue = ComponentDimensions.getScaleValue();

        int borderSize = 2;

        page.append("<div id=\"");
            page.append(page.getComponentName(control));
            page.append("\"");

            StringBuffer color = new StringBuffer();
            ColorFontMap.appendHexColor(color, control.getParentViewPan().total_color);

            String style = ";border-style:" + borderSize + "px;border-color:white;background-color:#" + color + ";";

            page.getPageRenderer().appendCommonAttributes(page, control);
            page.getPageRenderer().appendComponentStyle(page, control, false, style);
            page.appendln(">");
        
            page.append("<span");
            page.append(" style=\"");
            
            Rectangle rect = page.getPageRenderer().getComponentRect(page, control);
            int hSize =  rect.height - 2*borderSize;
            int wSize = rect.width - 2*borderSize;     
            page.append("px;height:");
            page.append(hSize);
            page.append("px;width:");
            page.append(wSize);
            page.append("px;");
            page.append("\">");
                
        
        page.append("<img ");
            page.append(" style=\"");
            page.append("position:absolute; left:25%; top:15%;\"");
            page.append(" src=\"");
            page.append(page.getImageUrl(control.getPrintIcon(), control).toString());
            page.append("\" height=\"75%\" width=\"55%\" title=\"Print schedule for "+control.getInnerSname()+" for this week\"/>");
       
        page.appendln("</span>");
        page.appendln("</div>");

        
    }

    public void initializeDocument(Document doc) {  }
    
}
