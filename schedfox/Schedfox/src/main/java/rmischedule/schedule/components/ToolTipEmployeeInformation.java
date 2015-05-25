/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.plaf.metal.MetalToolTipUI;

/**
 *
 * @author user
 */
public class ToolTipEmployeeInformation extends JToolTip {

    private static int textSize = 14;
    private static Font myHeaderFont = new Font("Dialog", Font.BOLD, textSize);
    private static Font myInfoFont = new Font("Dialog", Font.BOLD, textSize - 2);
    private static int spacing = 3;
    private static int xNoBuffer = 8;
    private static int xBuffer = 20;
    public static int myWidth = 200;
    private int pencilLocation;
    private SEmployee employee;

    private Color headerColor;
    private Color panelColor;
    private Color darkerColor;

    public ToolTipEmployeeInformation(SEmployee employee) {
        this.employee = employee;
        this.setUI(new CustomToolTipUI());
        this.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.LineBorder(headerColor, 1, true), new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), headerColor)));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel addressLabel = new JLabel("Address Info");
        this.add(addressLabel);
    }

    private class CustomToolTipUI extends MetalToolTipUI {

        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            pencilLocation = spacing + 32;
            g.setFont(myHeaderFont);
            incrementAndDraw(g, "Address", xNoBuffer);
            g.setFont(myInfoFont);
            incrementAndDraw(g, employee.getAddress(), xBuffer);
            incrementAndDraw(g, employee.getAddress2(), xBuffer);
            incrementAndDraw(g, employee.getCity() + ", " + employee.getState() + " " + employee.getZip(), xBuffer);
            g.setFont(myHeaderFont);
            g.setFont(myHeaderFont);
            incrementAndDraw(g, "Contact Information", xNoBuffer);
            g.setFont(myInfoFont);
            incrementAndDraw(g, employee.getPhone(), xBuffer);
            incrementAndDraw(g, employee.getPhone2(), xBuffer);
            incrementAndDraw(g, employee.getCell(), xBuffer);
            incrementAndDraw(g, employee.getPager(), xBuffer);
            g.setFont(myHeaderFont);
            //incrementAndDraw(g, myDateHoursWorked, xNoBuffer);
            g.setFont(myInfoFont);
            //incrementAndDraw(g, myHoursWorked, xBuffer);
            setBackground(panelColor);
        }

        private void incrementAndDraw(Graphics g, String text, int xPos) {
            if (text != null) {
                if (text.trim().length() > 0) {
                    if (g.getFont() == myHeaderFont) {
                        g.setColor(darkerColor);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.drawString(text, xPos, pencilLocation);
                    pencilLocation += (spacing + textSize);
                }
            }
        }
        
        public Dimension getPreferredSize(JComponent comp) {
            return new Dimension(myWidth, 200);
        }
    }
}
