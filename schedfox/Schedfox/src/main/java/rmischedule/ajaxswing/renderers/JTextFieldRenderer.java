/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ajaxswing.renderers;

import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;
import rmischedule.utility.JTextFieldValidatable;

/**
 *
 * @author hal
 */
public class JTextFieldRenderer implements ComponentRenderer {

    public void initializeDocument(Document document) {
    }

    public void renderComponent(Document document, Object component) {
        JTextFieldValidatable textField = (JTextFieldValidatable) component;
        String myMask = textField.getMask();
        String newMask = myMask.replaceAll("#", "9");
        
        HTMLPage page = (HTMLPage) document;
        page.append("<input type=\"text\" ");
        page.append("class=\"text\" ");
        page.append("alt=\"{");
        page.append("type:'fixed', ");
        page.append("mask:'");
        page.append(newMask);
        page.append("', ");
        page.append("stripMask: false ");
        page.append("}\"");
        page.append("id=\"" + page.getComponentName(textField) + "\" ");
        page.append("name=\"" + page.getComponentName(textField) + "\" ");
        page.append("value=\"" + textField.getText() + "\" ");
        page.getPageRenderer().appendCommonAttributes(page, textField);
        page.append(" style=\"");
        page.getPageRenderer().appendComponentStyle(page, textField, true);
        page.append("\" ");
        if (!textField.isEditable() || !textField.isEnabled()) {
            page.append("readonly ");
        }
        page.append("/>");

        page.append("<script type=\"text/javascript\">");
        page.append("jQuery(function(jQuery){ ");
        page.append("jQuery(\"#" + page.getComponentName(textField) + "\")");
        page.append(".mask(\"" + newMask + "\",{placeholder:\" \"})");
        page.append("});");
        page.append("jQuery(function(jQuery){ ");
        page.append("jQuery(\"#" + page.getComponentName(textField) + "\")");
        page.append(".val('" + textField.getText() + "')");
        page.append("});");
        page.append("</script>");
    }
}
