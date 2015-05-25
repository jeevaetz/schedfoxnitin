/*
 * SRow.java
 *
 * Created on April 14, 2004, 12:44 PM
 */
package rmischedule.schedule.components;

import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import javax.swing.*;
import java.awt.*;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author  jason.allen
 */
public class SRow extends JPanel {

    private SShift[] shifts;
    public SWeek myWeek;
    private myRowShiftCount shift_count;
    private boolean isDirty;
    private static DayOfWeekComparator dayOfWeekComparator;

    public SRow(SWeek w) {
        myWeek = w;
        shifts = new SShift[7];
        shift_count = new myRowShiftCount();

        if (dayOfWeekComparator == null) {
            dayOfWeekComparator = new DayOfWeekComparator();
        }
        isDirty = true;
        initRow();
        
    }

    public boolean isDirty() {
        return isDirty;
    }

    /**
     * These dispose methods are so very important... We have a massive memory leak
     * since it appears java's GC cannot handle all of our bidirection references...
     * Therefore it is very very important as you add classes that you properly
     * dispose all new private class, and remove all objects from sub panels. Please
     * verify from time to time in HEAP stack that this is still working.
     */
    public void dispose() {
        for (int s = 0; s < getShifts().length; s++) {
            getShifts()[s].dispose();
        }
        shifts = null;
        myWeek = null;
        shift_count = null;
        this.removeAll();

    }

    private void initRow() {
        for (int c = 0; c < 7; c++) {
            SShift newShift = new SShift((c + 1), myWeek.week_no, this);
            shifts[newShift.getOffset()] = newShift;
        }
        Arrays.sort(getShifts(), dayOfWeekComparator);
        for (int s = 0; s < getShifts().length; s++) {
            add(getShifts()[s], getShifts()[s].getOffset());
        }
        setEnabled(true);
        setVisible(true);
        setLayout(new GridLayout(1, 7));

        if (AjaxSwingManager.isAjaxSwingRunning()) {
            this.addComponentsAsNeeded();
        }
    }

    public void addSShift(final SShift newShift) {
        if (newShift.hasData()) {
            shift_count.add();
        }
        final int offset = newShift.getOffset();
        if (getShifts()[offset].myShift != null && newShift.myShift != null
                && getShifts()[offset].myShift.equals(newShift.myShift)) {
            return;
        } else {
            newShift.myRow = this;
            shifts[offset] = newShift;
            isDirty = true;
        }
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            this.addComponentsAsNeeded();
        }
    }

    public void addComponentsAsNeeded() {
        try {
            isDirty = false;
            SwingUtilities.invokeLater(new Thread() {

                public void run() {

                    try {
                        Arrays.sort(getShifts(), dayOfWeekComparator);
                        removeAll();
                        for (int s = 0; s < getShifts().length; s++) {
                            add(getShifts()[s], getShifts()[s].getOffset());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    revalidate();
                    if (!AjaxSwingManager.isAjaxSwingRunning()) {
                        repaint();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        if (isDirty) {
            addComponentsAsNeeded();
        }
        super.paintComponent(g);
    }

    /**
     * This should only affect AjaxSwing behaviour as it will prompt the AjaxSwingManager that this
     * row is dirty and needs a refresh
     */
    public void checkIfWeShouldRefresh() {
        if (this.myWeek.getSchedule().myParent.isDoneLoadingCompletely && AjaxSwingManager.isAjaxSwingRunning()) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    repaint();
                    //ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(this, true);
                }
            });

        }
    }

    public void removeShiftFromHash(int day) {
        shift_count.sub();
    }

    public SShift getSShift(int c) {
        for (int d = 0; d < getShifts().length; d++) {
            if (getShifts()[d].getOffset() == c) {
                return getShifts()[d];
            }
        }
        return getShifts()[c];
    }

    public void reconcileShifts(boolean reconcile) {
        for (int i = 0; i < getShifts().length; i++) {
            if (getShifts()[i].myShift != null) {
                if (getShifts()[i].myShift instanceof DShift) {
                    ((DShift)getShifts()[i].myShift).reconcile(reconcile);
                }
            }
        }
    }

    public SShift getShiftWithoutOffset(int position) {
        return getShifts()[position];
    }

    public SShift getShift(int day) {
        int shiftOffset = day - 1;
        for (int d = 0; d < getShifts().length; d++) {
            if (getShifts()[d].getOffset() == shiftOffset) {
                return getShifts()[d];
            }
        }
        //Should never get here
        return getShifts()[shiftOffset];
    }

    public Point getMyPosition() {
        int x, y;
        Point p = myWeek.getMyPosition();
        x = getX() + p.x;
        y = getY() + p.y;
        return new Point(x, y);
    }

    /* checks to see if the row is blank */
    public boolean isBlank() {
        if (shift_count.size() == 0) {
            return true;
        }
        for (SShift s : getShifts()) {
            if (s != null && s.myShift != null && s.myShift.getMyType() == SourceOfConflict.DSHIFT) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the shifts
     */
    public SShift[] getShifts() {
        return shifts;
    }

    public class myRowShiftCount {

        private int myCount;

        public myRowShiftCount() {
            myCount = 0;
        }

        public void add() {
            myCount++;
            myWeek.mySched.addShiftToSchedule();
        }

        public void sub() {
            myCount--;
            myWeek.mySched.removeShiftFromSchedule();
        }

        public int size() {
            return myCount;
        }
    }

    private class DayOfWeekComparator implements Comparator<SShift> {

        public int compare(SShift o1, SShift o2) {
            return o1.getOffset() - o2.getOffset();
        }
    }
}
