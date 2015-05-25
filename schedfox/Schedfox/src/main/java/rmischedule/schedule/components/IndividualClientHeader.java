/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components;

import com.creamtec.ajaxswing.AjaxSwingManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import javax.swing.JLabel;
import rmischedule.main.Main_Window;
import rmischedule.schedule.schedulesizes.ZoomablePanel;

/**
 *
 * @author user
 */
public class IndividualClientHeader extends ZoomablePanel {

    private static Dimension maxSize = new Dimension(32767, 32767);
    private SMainComponent parent;

    public IndividualClientHeader(SMainComponent parent, MouseAdapter adapter) {
        super();
        this.parent = parent;
        setLayout(new GridLayout());
        setBackground(parent.getMyColor());
        addMouseListener(adapter);
    }

    @Override
    public void paintComponentCustom(Graphics g) {
        g.setFont(Main_Window.client_font);
        g.setColor(Color.BLACK);
        g.drawString(parent.getFullClientName(), 5, 2 + (((30 - g.getFontMetrics().getStringBounds(parent.getFullClientName(), g).getBounds().height) / 2)) + Main_Window.client_font.getSize());
    }

    public String getSizeKey() {
        return "individualClientHeader";
    }

    @Override
    public Dimension getMaximumSize() {
        return maxSize;
    }

    public String getSName() {
        return parent.getFullClientName();
    }
}
