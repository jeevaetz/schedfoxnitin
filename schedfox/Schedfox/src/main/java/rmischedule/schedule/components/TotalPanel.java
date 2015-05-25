/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComboBox;
import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.schedulesizes.ZoomablePanel;

/**
 *
 * @author user
 */
public class TotalPanel extends ZoomablePanel {

    private double myVal;
    private int numShifts;
    private TotalPanel myTotalP;
    private boolean isCompleteTotal;

    private JComboBox shiftTotalDetails;

    /**
     * Constructor takes in a dayNum which is not so much a day of the week, but is used to
     * figure out what day of week and an array, the array is necessary for the totals column
     * to work properly it figures what TotalPanel need to sum up and add their total into
     * the total panel whenever they are changed.
     */
    public TotalPanel(int dayNum, TotalPanel[] myTotalArray, JComboBox shiftTotal) {
        super();
        this.shiftTotalDetails = shiftTotal;
        setLayout(new BorderLayout());
        setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        setBackground(Schedule_View_Panel.total_color);
        if ((dayNum + 1) % 8 == 0) {
            isCompleteTotal = true;
            for (int i = 7; i > 0; i--) {
                myTotalArray[dayNum - i].setCompleteTotalPanel(this);
            }
        } else {
            isCompleteTotal = false;
        }
        myVal = 0.0;
        numShifts = 0;
        addTotal(0.0);
    }

    public boolean shouldDisplayNumberOfShiftsForClientTotal() {
        if (shiftTotalDetails.getSelectedIndex() == 1) {
            return true;
        }
        return false;
    }

    public JComboBox getShiftTotalDetails() {
        //TODO: Fix this later, CreamTec
        return new JComboBox();
    }

    /**
     * Sets the complete Total Panel so that when any of the children are updated
     * they will then update their parent...
     */
    public void setCompleteTotalPanel(TotalPanel myTotalPanel) {
        myTotalP = myTotalPanel;
    }

    public void addTotal(double total) {
        if (getMyTotalP() != null) {
            getMyTotalP().addTotal(total);
        }
        if (total > 0) {
            numShifts++;
        } else if (total < 0) {
            numShifts--;
        }
        myVal += total;
        displayTotal(total);
    }

    public void displayTotal(double total) {
        if (shiftTotalDetails != null) {
            repaint();
        }
    }

    public void paintComponentCustom(Graphics g) {
        g.setColor(Color.BLACK);
        if (shiftTotalDetails != null) {
            int size = isIsCompleteTotal() ? 50 : 60;
            g.setFont(Main_Window.shift_totals_font);
            if (shouldDisplayNumberOfShiftsForClientTotal() == false) {
                g.drawString(getMyVal() + "", (size - g.getFontMetrics().getStringBounds(getMyVal() + "", g).getBounds().width) / 2, 15);
            } else {
                g.drawString(getNumShifts() + "", (size - g.getFontMetrics().getStringBounds(getNumShifts() + "", g).getBounds().width) / 2, 15);
            }
        }
    }

    public String getSizeKey() {
        if (isIsCompleteTotal()) {
            return "shiftsTotalsSize";
        } else {
            return "shiftsDaySize";
        }
    }

    /**
     * @return the myVal
     */
    public double getMyVal() {
        return myVal;
    }

    /**
     * @return the numShifts
     */
    public int getNumShifts() {
        return numShifts;
    }

    /**
     * @return the myTotalP
     */
    public TotalPanel getMyTotalP() {
        return myTotalP;
    }

    /**
     * @return the isCompleteTotal
     */
    public boolean isIsCompleteTotal() {
        return isCompleteTotal;
    }
}
