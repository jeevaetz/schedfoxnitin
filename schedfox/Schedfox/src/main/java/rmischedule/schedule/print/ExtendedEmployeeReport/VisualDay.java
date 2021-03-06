/*
 * VisualDay.java
 *
 * Created on December 19, 2005, 1:18 PM
 */

package rmischedule.schedule.print.ExtendedEmployeeReport;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.components.*;
import rmischedule.xprint.xdata.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
/**
 *
 * @author  Ira Juneau
 */
public class VisualDay extends javax.swing.JPanel {
    
    private Color DateColor = new Color(210,210,210);
    private Font DateFont = new Font("Dialog", Font.BOLD, 50);
    private Color timeColor = new Color(10,10,10);
    private Font timeFont = new Font("Dialog", Font.BOLD, 14);
    
    private int DayOfMonth;
    private String dateS;
    private xNewPrintDay myData;
    
    /** Creates new form VisualDay */
    public VisualDay(Calendar date) {
        initComponents();
        DayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        DayOfWeekLabel.setText(StaticDateTimeFunctions.getDayOfWeekFull(date));
        dateS = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(date);
    }
    
    /**
     * If date is correct will use data in passed in xNewPrintDay to populate the 
     * visual aspects of this day...
     */
    public boolean insertDataIfCorrectDate(xNewPrintDay myDate) {
        if (myDate.databasedate.equals(dateS)) {
            myData = myDate;
            return true;
        }
        return false;
    }
    
    public void paintComponent(Graphics g) {
        g.setFont(DateFont);
        g.setColor(DateColor);
        String dayOfMonth = DayOfMonth + "";
        int StringWidth = g.getFontMetrics(DateFont).stringWidth(dayOfMonth);
        int StringHeight = g.getFontMetrics(DateFont).getHeight();
        g.drawString(dayOfMonth, (getBounds().width - StringWidth) / 2, 50);
        if (myData != null) {
            g.setFont(timeFont);
            g.setColor(timeColor);
            g.drawString(myData.stime, 5, timeFont.getSize() + 15);
            g.drawString(myData.diffTime, getBounds().width - (g.getFontMetrics().stringWidth(myData.diffTime) + 5), (getBounds().height + timeFont.getSize() + 10) / 2);
            g.drawString(myData.etime, 5, getBounds().height - 5);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        DayOfWeekLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        setDoubleBuffered(false);
        setMaximumSize(new java.awt.Dimension(50, 60));
        setMinimumSize(new java.awt.Dimension(50, 60));
        setPreferredSize(new java.awt.Dimension(50, 60));
        jPanel1.setLayout(new java.awt.GridLayout());

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 3, 1, 3)));
        jPanel1.setDoubleBuffered(false);
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 12));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 12));
        DayOfWeekLabel.setBackground(new java.awt.Color(218, 224, 237));
        DayOfWeekLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 9));
        DayOfWeekLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DayOfWeekLabel.setOpaque(true);
        jPanel1.add(DayOfWeekLabel);

        add(jPanel1, java.awt.BorderLayout.NORTH);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DayOfWeekLabel;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
