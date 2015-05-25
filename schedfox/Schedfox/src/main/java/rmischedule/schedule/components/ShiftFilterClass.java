/*
 * ShiftFilterClass.java
 *
 * Created on August 15, 2005, 8:57 AM
 *
 * Copyright: SchedFox 2005
 */
package rmischedule.schedule.components;

/**
 *
 * @author Ira Juneau
 */
public abstract class ShiftFilterClass {

    //Initial value of showing schedule
    public boolean initialVal = false;

    /**
     * Creates a new instance of ShiftFilterClass
     */
    public ShiftFilterClass() {
    }

    public ShiftFilterClass(boolean initVal) {
        initialVal = initVal;
    }

    /**
     * Normally by default if a schedule has at least one shift that meets the
     * criteria we are looking for we show the entire schedule. This however
     * only shows the shift shift we are looking for.
     *
     * @return
     */
    public boolean onlyShowByShiftNotSchedule() {
        return false;
    }

    public abstract boolean shouldShowMe(UnitToDisplay shiftToShow);

    public boolean shouldShowMe(SSchedule schedToShow) {
        return true;
    }

    protected boolean showWholeSchedule(SSchedule tempSched) {
        return false;
    }

    /**
     * Yes I know its intensive has too loop through every week, row and day to
     * see if given schedule should be shown. Returns true if the schedule
     * should be shown...
     */
    public boolean runFilterOnThisSchedule(SSchedule mySched) {
        boolean showSchedule = initialVal;
        int mySchedsize = mySched.getWeekSize();
        SWeek tempWeek;
        for (int w = 0; w < mySchedsize; w++) {
            tempWeek = mySched.getWeek(w);
            for (int r = 0; r < tempWeek.getRowSize(); r++) {
                for (int d = 1; d < 8; d++) {
                    if (mySched.getWeek(w).getRow(r).getShift(d).shiftNonVisible()) {
                        mySched.getWeek(w).getRow(r).getShift(d).setNonVisibleShift(false);
                        mySched.getWeek(w).getRow(r).getShift(d).setBG();
                    }
                    if (mySched.getWeek(w).getRow(r).getShift(d).hasData()) {
                        boolean showShift = shouldShowMe(mySched.getWeek(w).getRow(r).getShift(d).myShift);
                        if (showShift) {
                            showSchedule = true;
                        } else if (onlyShowByShiftNotSchedule()) {
                            //Otherwise if not show shift, and we are not breaking things down to just schedule level, mark shift as invisible
                            mySched.getWeek(w).getRow(r).getShift(d).setNonVisibleShift(true);
                            mySched.getWeek(w).getRow(r).getShift(d).setBG();
                            mySched.getWeek(w).getRow(r).getShift(d).repaint();
                        }
                    }
                }
            }
        }
        if (showSchedule || showWholeSchedule(mySched)) {
            if (!mySched.isVisible()) {
                mySched.setVisible(true);
            }
            showSchedule = true;
        } else {
            if (mySched.isVisible()) {
                mySched.setVisible(false);
            }
        }
        return showSchedule;
    }
}
