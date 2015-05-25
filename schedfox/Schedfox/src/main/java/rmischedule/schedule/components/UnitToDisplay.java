/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.components;

import schedfoxlib.model.ShiftTypeClass;
import rmischedule.schedule.Schedule_View_Panel;

/**
 *
 * @author user
 */
public interface UnitToDisplay extends SourceOfConflict {
    public boolean hasNote();
    public SEmployee getEmployee();
    public SMainComponent getClient();
    public SShift getShift();
    public void setShift(SShift shift);
    public void setShift(SShift shift, boolean clearVal);
    public String getGroupId();
    public double getNoHoursDouble();
    public boolean hasConflict();
    public boolean isMaster();
    public UnitToDisplay getMaster();
    public ShiftTypeClass getType();
    public void recalTimes();
    public Schedule_View_Panel getParent();
    public String getLastUpdateStr();
    public void zeroTimes();
    public boolean isBlank();
    public boolean isOverwrittingBuffer();
    public String getDatabaseDate();
    public String getRealMasterId();

    /**
     * Called by filterMaster in SClient when we are filtering Master so that it wont save the
     * info as a temp sched when it builds the shift....
     */
    public void setAsFilteringMasterDontSave(boolean filtering);

    public boolean isCurrentlyFilteringDontSave();

    @Override
    public boolean equals(Object obj);
}
