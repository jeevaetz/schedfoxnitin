package rmischedule.ajaxswing.renderers;


import com.creamtec.ajaxswing.rendering.ComponentRenderer;
import com.creamtec.ajaxswing.rendering.Document;
import com.creamtec.ajaxswing.rendering.html.ColorFontMap;
import com.creamtec.ajaxswing.rendering.html.HTMLPage;

import java.awt.Color;
import java.awt.Rectangle;
import rmischedule.main.Main_Window;

import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.SShift;
import rmischedule.schedule.components.SShift.shiftNotesLabel;
import rmischedule.schedule.schedulesizes.ComponentDimensions;


/**
 *
 * @author Rybka
 */
public class SShiftRenderer implements ComponentRenderer {

    public SShiftRenderer() {
    }

    public void initializeDocument(Document document) {
    }

    public void renderComponent(Document document, Object component) throws Exception {
        Color bgColor;
        SShift control = (SShift) component;
        if (!control.isInstantiated()) {
            control.instantiate();
        }

        HTMLPage page = (HTMLPage) document;
        double scaleValue = ComponentDimensions.getScaleValue();
        String bgColorStr = "";

        page.append("<div class=\"");
        if(control.isHasData()) {
        	page.append("sshiftdata");
        } else {
        	page.append("sshift");
        }
        page.append("\" id=\"");
        page.append(page.getComponentName(control));
        page.append("\"");
        
        page.append(" client_id=\"");
        page.append(control.getClient().getId());
        page.append("\"");
        

        double fontStartSize = 11.00;

        //check if we need darker background
        if (!control.isHasData()) {
            bgColor = control.myShiftChar.getColor();
            bgColorStr = "background-color: rgb("+bgColor.getRed()+","
                                               +bgColor.getGreen()+","
                                               +bgColor.getBlue()+")";
        }

        String customStyle = "font-size:"
                +fontStartSize*scaleValue+"px;"+bgColorStr;

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

         if (control.myShiftChar.getIcon() != null && !control.isHasData()) {

            page.append("<img ");
            page.append(" style=\"");
            page.append("position:absolute; left:4%; top:1px;\"");
            page.append(" src=\"");
            page.append(page.getImageUrl(control.myShiftChar.getIcon(), control).toString());
            page.append("\" height=\"90%\" width=\"90%\" />");
        }


        if (control.isHasData()) {

            page.append("<span");
            page.append(" style=\"");
            page.append("background-color:#");
            ColorFontMap.appendHexColor(page, control.myShiftChar.getColor());
            int hwStartSize = 55;
            int borderStartSize = 2;
            page.append(";border-width:"
                        +borderStartSize*scaleValue+"px;border-style:outset;border-color:white;height:"
                        +hwStartSize*scaleValue+"px;width:"
                        +hwStartSize*scaleValue+"px;position:absolute;");
            page.append("\">");

            if (control.myShiftChar.getIcon() != null) {

            page.append("<img ");
            page.append(" style=\"");
            page.append("position:absolute; left:4%; top:1px;\"");
            page.append(" src=\"");
            page.append(page.getImageUrl(control.myShiftChar.getIcon(), control).toString());
            page.append("\" height=\"90%\" width=\"90%\" />");
        }

                page.append("<span");
                page.append(" style=\"position:absolute;left:");
                int startTimeLeft = 1;
                page.append(Double.toString(startTimeLeft*scaleValue));
                page.append("px;top:2px;white-space:nowrap;font-weight:bold");
                page.append("\">");
                page.append(this.cutCorrectTime(control.myShift.getFormattedStartTime()));
                page.appendln("</span>");

                page.append("<span");
                page.append(" style=\"position:absolute;left:");
                page.append((int)(rect.width*0.5));
                page.append("px;top:");
                page.append((int)(rect.height*0.4));
                page.append("px;white-space:nowrap;color:#");
                ColorFontMap.appendHexColor(page, Color.GRAY);
                page.append("\">");
                page.append(Double.toString(control.myShift.getNoHoursDouble()));
                page.appendln("</span>");

                page.append("<span");
                page.append(" style=\"position:absolute;left:");
                int endTimeLeft = 1;
                page.append(Double.toString(endTimeLeft*scaleValue));
                page.append("px;top:");
                page.append((int)(rect.height*0.7));
                page.append("px;white-space:nowrap;font-weight:bold");
                page.append("\">");
                page.append(this.cutCorrectTime(control.myShift.getFormattedEndTime()));
                page.appendln("</span>");

                if (control.myShift.hasNote()) {
                                 page.append("<span>");
					if(control.noteIconLabel == null) {
						control.noteIconLabel = control.new shiftNotesLabel("");
						control.setLayout(null);
						control.add(control.noteIconLabel);
						control.noteIconLabel.setBounds(ComponentDimensions.currentSizes.get("SShift").width - 17, 1, 16, 16);
						control.noteIconLabel.setOpaque(false);
						control.noteIconLabel.setIcon(Main_Window.Note16x16);
					}
                page.getPageRenderer().renderComponent(page, control.noteIconLabel);
                page.appendln("</span>");
                }
            page.appendln("</span>");
        }

        page.appendln("</div>");

    }

    public String cutCorrectTime(String time) {
    	if(time == null) return "";
    	if(time.length() > 8) {
    		time = time.substring(0, 8);
    	}
        return time;
    }


}
