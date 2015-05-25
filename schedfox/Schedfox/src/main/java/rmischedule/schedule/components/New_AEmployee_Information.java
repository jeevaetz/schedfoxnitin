/*
 * New_AEmployee_Information.java
 *
 * Created on August 18, 2005, 10:07 AM
 */
package rmischedule.schedule.components;

import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.SwingUtilities;
import rmischedule.schedule.ScheduleThread;
import rmischedule.schedule.Schedule_View_Panel;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.CalcedLocationDistance;
import schedfoxlib.model.Client;

/**
 *
 * @author Ira Juneau
 */
public class New_AEmployee_Information extends javax.swing.JPanel {

    private static int textSize = 14;
    private static Font myHeaderFont = new Font("Dialog", Font.BOLD, textSize);
    private static Font myInfoFont = new Font("Dialog", Font.BOLD, textSize - 2);
    private javax.swing.border.CompoundBorder myBorder;
    private AEmployee myEmployee;
    private Color headerColor;
    private Color panelColor;
    private Color darkerColor;
    /**
     * INFORMATION G *
     */
    private String myName;
    private String myAddress;
    private String myAddress2;
    private String myCityStateZip;
    private String myArmedOrUnarmed;
    private String myPhone;
    private String myPhone2;
    private String myCell;
    private String myPage;
    private String myDateHoursWorked;
    private String myHoursWorked;
    private double distance = 0;
    private String distanceToTravel;
    private String durationToTravel;
    private Schedule_View_Panel parent;
    private int myNewXLocation;
    private int myNewYLocation;
    private int ParentLocation;
    private int LocationOfGlassPane;
    private static int spacing = 3;
    private static int xNoBuffer = 8;
    private static int xBuffer = 20;
    private static int myWidth = 200;
    private int pencilLocation;
    private boolean shouldHideMe;
    private New_AEmployee_Information thisObj;

    private String firstClosestClient;
    private String secondClosestClient;
    private String thirdClosestClient;
    private String fourthClosestClient;
    private String fifthClosestClient;
    
    /**
     * Creates new form New_AEmployee_Information
     */
    public New_AEmployee_Information(Schedule_View_Panel p) {
        parent = p;
        initComponents();
        thisObj = this;
        myNewXLocation = 0;
        myNewYLocation = 0;
        LocationOfGlassPane = 0;
        ParentLocation = 0;
        shouldHideMe = true;
        setVisible(false);
        myShowMeThread myVisibleThread = new myShowMeThread();
        myVisibleThread.start();
        myVisibleThread.registerMe(parent);
    }

    public void show(AEmployee a, Color newColor, Color PanelColor) {
        myEmployee = a;
        headerColor = newColor.darker();
        //headerColor=Color.WHITE;
        darkerColor = headerColor.darker();
        myBorder = new javax.swing.border.CompoundBorder(new javax.swing.border.LineBorder(headerColor, 1, true), new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), headerColor));

        panelColor = PanelColor;
        myName = a.myEmployee.getName();

        myAddress = a.myEmployee.getAddress();
        myAddress2 = a.myEmployee.getAddress2();
        myCityStateZip = a.myEmployee.getCity() + ", " + a.myEmployee.getState() + " " + a.myEmployee.getZip();

        myPhone = a.myEmployee.getPhone();
        myPhone2 = a.myEmployee.getPhone2();
        myCell = a.myEmployee.getCell();
        myPage = a.myEmployee.getPager();
        myDateHoursWorked = "Hours Worked (" + parent.getCurrentSelectedWeekStart() + " - " + parent.getCurrentSelectedWeekEnd() + ")";
        myHoursWorked = "Hours Selected Week: " + a.myEmployee.getHoursWorkedForWeek(parent.getCurrentSelectedWeek());
        if (LocationOfGlassPane == 0) {
            LocationOfGlassPane = parent.getLayeredPane().getLocationOnScreen().y + 2;
        }
        ParentLocation = a.getLocationOnScreen().y;
        myNewXLocation = parent.getScrollPane().getWidth() - (myWidth + 2);

        try {
            this.distance = 0;
            if (parent.getSelectedShifts().size() == 1) {
                Client myClient = parent.getSelectedShifts().get(0).getClient().getClientData();
                ArrayList<CalcedLocationDistance> clientDist = myEmployee.myEmployee.getLocationDistance();
                for (int d = 0; d < clientDist.size(); d++) {
                    if (myClient.getClientId().intValue() == clientDist.get(d).getClientId()) {
                        this.durationToTravel = (clientDist.get(d).getTravelDuration() / 60) + " minutes";
                        this.distanceToTravel = (Math.round(clientDist.get(d).getTravelDistance() * 0.000621371 * 10) / 10.0) + " miles";
                        this.distance = (Math.round(clientDist.get(d).getTravelDistance() * 0.000621371 * 10) / 10.0);
                    }
                }
            } else {
                this.durationToTravel = "";
                this.distanceToTravel = "";
            }
        } catch (Exception exe) {
            this.durationToTravel = "";
            this.distanceToTravel = "";
        }
        try {
            ArrayList<CalcedLocationDistance> clientDist = myEmployee.myEmployee.getLocationDistance();
            ArrayList<CalcedLocationDistance> closestClients = new ArrayList<CalcedLocationDistance>();
            closestClients.addAll(clientDist);
            Collections.sort(closestClients, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof CalcedLocationDistance && o2 instanceof CalcedLocationDistance) {
                        CalcedLocationDistance cacl1 = (CalcedLocationDistance)o1;
                        CalcedLocationDistance calc2 = (CalcedLocationDistance)o2;
                        Integer distance1 = cacl1.getTravelDistance();
                        Integer distance2 = calc2.getTravelDistance();
                        if (distance1 <= 0) {
                            distance1 = 9999999;
                        }
                        if (distance2 <= 0) {
                            distance2 = 9999999;
                        }
                        return distance1.compareTo(distance2);
                    }
                    return 1;
                }
            });
            firstClosestClient = null;
            secondClosestClient = null;
            thirdClosestClient = null;
            fourthClosestClient = null;
            fifthClosestClient = null;

            if (closestClients.size() > 0) {
                firstClosestClient = "(" + (Math.round(closestClients.get(0).getTravelDistance() * 0.000621371 * 10) / 10.0) + "m) " + parent.getClient(closestClients.get(0).getClientId()).getClientName();
            }
            if (closestClients.size() > 1) {
                secondClosestClient = "(" + (Math.round(closestClients.get(1).getTravelDistance() * 0.000621371 * 10) / 10.0) + "m) " + parent.getClient(closestClients.get(1).getClientId()).getClientName();
            }
            if (closestClients.size() > 2) {
                thirdClosestClient = "(" + (Math.round(closestClients.get(2).getTravelDistance() * 0.000621371 * 10) / 10.0) + "m) " + parent.getClient(closestClients.get(2).getClientId()).getClientName();
            }
            if (closestClients.size() > 3) {
                fourthClosestClient = "(" + (Math.round(closestClients.get(3).getTravelDistance() * 0.000621371 * 10) / 10.0) + "m) " + parent.getClient(closestClients.get(3).getClientId()).getClientName();
            }
            if (closestClients.size() > 4) {
                fifthClosestClient = "(" + (Math.round(closestClients.get(4).getTravelDistance() * 0.000621371 * 10) / 10.0) + "m) " + parent.getClient(closestClients.get(4).getClientId()).getClientName();
            }
        } catch (Exception exe) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setBorder(myBorder);
                setSize(200, 150);
                EmployeeLabel.setBackground(headerColor);
                EmployeeLabel.setText(" " + myName);
                setVisible(true);
                if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                    ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObj, true);
                }
            }
        });

    }

    /**
     * Should make rendering faster both in swing and in creamtec by stating
     * that non of my children will overlap.
     *
     * @return
     */
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return true;
    }

    /**
     * Here is where it all goes down...
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        pencilLocation = spacing + 32;
        g.setFont(myHeaderFont);
        incrementAndDraw(g, "Address", xNoBuffer);
        g.setFont(myInfoFont);
        incrementAndDraw(g, myAddress, xBuffer);
        incrementAndDraw(g, myAddress2, xBuffer);
        incrementAndDraw(g, myCityStateZip, xBuffer);
        g.setFont(myHeaderFont);
        incrementAndDraw(g, "Contact Information", xNoBuffer);
        g.setFont(myInfoFont);
        incrementAndDraw(g, myPhone, xBuffer);
        incrementAndDraw(g, myPhone2, xBuffer);
        incrementAndDraw(g, myCell, xBuffer);
        incrementAndDraw(g, myPage, xBuffer);
        g.setFont(myHeaderFont);
        incrementAndDraw(g, myDateHoursWorked, xNoBuffer);
        g.setFont(myInfoFont);
        incrementAndDraw(g, myHoursWorked, xBuffer);

        if (durationToTravel != null && durationToTravel.length() > 0) {
            g.setFont(myHeaderFont);
            incrementAndDraw(g, "Travel Information", xNoBuffer, distance > 50.0);
            g.setFont(myInfoFont);
            incrementAndDraw(g, this.durationToTravel, xBuffer, distance > 50.0);
            incrementAndDraw(g, this.distanceToTravel, xBuffer, distance > 50.0);
        }
        if (firstClosestClient != null || secondClosestClient != null || thirdClosestClient != null) {
            g.setFont(myHeaderFont);
            incrementAndDraw(g, "Closest Clients", xNoBuffer);
            g.setFont(myInfoFont);
            if (firstClosestClient != null) {
                incrementAndDraw(g, firstClosestClient, xBuffer);
            }
            if (secondClosestClient != null) {
                incrementAndDraw(g, secondClosestClient, xBuffer);
            }
            if (thirdClosestClient != null) {
                incrementAndDraw(g, thirdClosestClient, xBuffer);
            }
            if (fourthClosestClient != null) {
                incrementAndDraw(g, fourthClosestClient, xBuffer);
            }
            if (fifthClosestClient != null) {
                incrementAndDraw(g, fifthClosestClient, xBuffer);
            }
        }

        if (getHeight() != pencilLocation - textSize) {
            myNewYLocation = 0;
            if ((pencilLocation + ParentLocation) - LocationOfGlassPane > parent.getHeight()) {
                myNewYLocation = ((ParentLocation - LocationOfGlassPane) - pencilLocation) + (textSize + 17);
            } else {
                myNewYLocation = ParentLocation - LocationOfGlassPane;
            }
            setSize(myWidth, pencilLocation - (textSize - 5));
            setLocation(myNewXLocation, myNewYLocation);
            setBackground(panelColor);

        }
    }

    private void incrementAndDraw(Graphics g, String text, int xPos, boolean errorText) {
        if (text != null) {
            if (text.trim().length() > 0) {
                if (errorText) {
                    g.setColor(Color.RED);
                } else if (g.getFont() == myHeaderFont) {
                    g.setColor(darkerColor);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.drawString(text, xPos, pencilLocation);
                pencilLocation += (spacing + textSize);
            }
        }
    }
    
    private void incrementAndDraw(Graphics g, String text, int xPos) {
        this.incrementAndDraw(g, text, xPos, false);
    }

    public void hide(boolean hideMe) {
        shouldHideMe = hideMe;
    }

    /**
     * Class just keeps checking to see if we need to exit or not...
     */
    private class myShowMeThread extends ScheduleThread {

        private boolean killed;

        public myShowMeThread() {
            killed = false;
        }

        public void killMe() {
            killed = true;
            interrupt();
        }

        public void run() {
            setPriority(Thread.MIN_PRIORITY);
            while (!killed) {
                try {
                    sleep(200);
                    if (shouldHideMe && isVisible()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                setVisible(false);
                                //AjaxSwingManager.endOperation();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        EmployeeLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(200, 2147483647));
        setMinimumSize(new java.awt.Dimension(200, 0));
        setPreferredSize(new java.awt.Dimension(200, 0));
        EmployeeLabel.setBackground(new java.awt.Color(255, 255, 255));
        EmployeeLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 14));
        EmployeeLabel.setForeground(new java.awt.Color(255, 255, 255));
        EmployeeLabel.setText("jLabel1");
        EmployeeLabel.setMaximumSize(new java.awt.Dimension(48, 18));
        EmployeeLabel.setMinimumSize(new java.awt.Dimension(48, 18));
        EmployeeLabel.setOpaque(true);
        EmployeeLabel.setPreferredSize(new java.awt.Dimension(48, 18));
        add(EmployeeLabel, java.awt.BorderLayout.NORTH);

    }
    // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EmployeeLabel;
    // End of variables declaration//GEN-END:variables
}
