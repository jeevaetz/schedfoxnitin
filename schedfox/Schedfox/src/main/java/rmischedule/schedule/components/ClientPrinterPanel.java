/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.schedulesizes.ZoomablePanel;

/**
 *
 * @author user
 */
public class ClientPrinterPanel extends ZoomablePanel {

    private Icon printIcon;
    private SClient parent;

    public ClientPrinterPanel(SClient parent, MouseListener listener, int i) {
        super();
        this.parent = parent;
        setLayout(new GridLayout(1, 1));
        setBackground(Schedule_View_Panel.total_color);
        setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        JLabel myPrintLabel = new JLabel();
        myPrintLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        if (!Main_Window.isEmployeeLoggedIn()
                && !Main_Window.isClientLoggedIn()) {
            myPrintLabel.setIcon(Main_Window.Printer_View_Icon);
            try {
                myPrintLabel.setToolTipText("Print schedule for " + parent.getFullClientName() + " for this week");
            } catch (Exception e) {}
            myPrintLabel.addMouseListener(new ClientPrintClickAction(i));
            setPrintIcon(myPrintLabel.getIcon());
        } else {
            myPrintLabel.setIcon(Main_Window.MapIcon);
            myPrintLabel.setToolTipText("Show map for " + parent.getFullClientName());
            myPrintLabel.addMouseListener(listener);
            setPrintIcon(Main_Window.MapIcon);
        }
        add(myPrintLabel);

    }

    public Schedule_View_Panel getParentViewPan() {
        return parent.getMyParent();
    }

    public String getInnerSname() {
        return parent.getFullClientName();
    }

    public Icon getPrintIcon() {
        return printIcon;
    }

    public void setPrintIcon(Icon printIcon) {
        this.printIcon = printIcon;
    }

    public String getSizeKey() {
        return "printerLabelSize";
    }

    private class ClientPrintClickAction implements MouseListener {

        private int w;

        public ClientPrintClickAction(int week) {
            w = week;
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() > 1) {
                parent.printClientSchedule(w);
            }
        }
    }
}
