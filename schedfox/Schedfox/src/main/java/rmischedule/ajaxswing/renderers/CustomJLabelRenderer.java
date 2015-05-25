package rmischedule.ajaxswing.renderers;
import java.awt.Font;

import javax.swing.JLabel;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.AbstractHTMLComponentRenderer;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
/**
 *
 * @author agunko
 */
public class CustomJLabelRenderer extends AbstractHTMLComponentRenderer implements ComponentRenderer {

    public CustomJLabelRenderer() {
    }

    public void initializeDocument(Document document) {
    }
   
    public void renderComponent(HTMLPage page, Object object) throws Exception {
        JLabel comp = (JLabel) object;
        comp.setFont(new Font("arial,helvetica,espy,sans-serif", Font.PLAIN, 11));
        String text = comp.getText();
        if ((text != null) && text.startsWith(" ")) {
			int spacesCount = 0;
			StringBuffer textBuf = new StringBuffer(text.length() + 5);
			while ((spacesCount < text.length())
					&& (text.charAt(spacesCount) == ' ')) {
				spacesCount++;
				textBuf.append("&nbsp;");
			}
			String trim = text.trim();
			int endSpaces = text.length() - trim.length() - spacesCount;
			textBuf.append(trim);
			for(int i=0;i<endSpaces;i++) {
				textBuf.append("&nbsp;");
			}
			text = textBuf.toString();
		}
        int padding = 166 - comp.getPreferredSize().width;
        page.append("<span style=\"padding-right:" + padding + "px\"");
        renderComponentId(page, comp);
        page.getPageRenderer().appendCommonAttributes(page, comp);
        page.append("\">");
        page.appendText(text, false, comp);
        page.append("</span>");

    }
}