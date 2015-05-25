package rmischedule.ajaxswing.renderers;

import java.awt.Color;

/**
 *
 * @author Rybka
 */
public  class CustomRendererUtil {

//get background-color as RGB prop from AWT Color
    static String getHTMLBackgroundColorAsRGB(Color color) {
       String bgColorStr = " rgb("+color.getRed()+","
                                  +color.getGreen()+","
                                  +color.getBlue()+")";
       return bgColorStr;
    }
}
