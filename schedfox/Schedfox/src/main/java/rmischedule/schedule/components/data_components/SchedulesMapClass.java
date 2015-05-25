/*
 * SchedulesMapClass.java
 *
 * Created on November 15, 2005, 11:09 AM
 *
 * Copyright: SchedFox 2005
 */
package rmischedule.schedule.components.data_components;

import java.util.*;
import rmischedule.schedule.components.*;

/**
 *
 * @author Ira Juneau
 */
public class SchedulesMapClass {

    private Hashtable<Integer, Vector<SSchedule>> mapSchedulesByEmployeeId;
    private SortedSet<SSchedule> mapSchedulesByClientId;
    private Hashtable<String, SSchedule> hashForSchedules;

    private static SEmployee startEmployee;
    private static SEmployee endEmployee;
    private static SSchedule startSchedule;
    private static SSchedule endSchedule;

    /** Creates a new instance of SchedulesMapClass */
    public SchedulesMapClass() {
        mapSchedulesByEmployeeId = new Hashtable<Integer, Vector<SSchedule>>();
        mapSchedulesByClientId = Collections.synchronizedSortedSet(new TreeSet(new clientComparator()));
        hashForSchedules = new Hashtable();
    }

    private void initBlankSchedules() {
        if (startEmployee == null) {
            startEmployee = new SEmployee("-1");
            endEmployee = new SEmployee("-2");
            startSchedule = new SSchedule(startEmployee);
            endSchedule = new SSchedule(endEmployee);
        }
    }

    public void dispose() {
        try {
            Iterator<Integer> it = mapSchedulesByEmployeeId.keySet().iterator();
            while (it.hasNext()) {
                Integer currKey = it.next();
                Vector<SSchedule> schedules = mapSchedulesByEmployeeId.get(currKey);
                for (int s = 0; s < schedules.size(); s++) {
                    SSchedule currSchedule = schedules.get(s);
                    currSchedule.removeAll();
                    currSchedule.dispose();
                }
            }
            mapSchedulesByEmployeeId = null;

            Iterator<SSchedule> itCli = mapSchedulesByClientId.iterator();
            while (itCli.hasNext()) {
                try {
                    itCli.next().dispose();
                } catch (Exception e) {}
            }
        } catch (NullPointerException e) {

        } finally {
        mapSchedulesByClientId = null;

        hashForSchedules = null;
        }
    }

    public void addSchedule(SSchedule newSchedule) {
        try {
            if (mapSchedulesByEmployeeId.get(newSchedule.getEmployee().getId()) == null) {
                mapSchedulesByEmployeeId.put(newSchedule.getEmployee().getId(), new Vector<SSchedule>());
            }
            mapSchedulesByEmployeeId.get(newSchedule.getEmployee().getId()).add(newSchedule);
            mapSchedulesByClientId.add(newSchedule);
            hashForSchedules.put(newSchedule.getClient().getId() + " " + newSchedule.getEmployee().getId() + newSchedule.getSortingScheduleId(), newSchedule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SortedSet<SSchedule> getAllSchedules() {
        return mapSchedulesByClientId;
    }

    public SortedSet<SSchedule> getClientSchedules(SMainComponent client) {
        initBlankSchedules();
        startSchedule.setClient(client);
        endSchedule.setClient(client);
        return mapSchedulesByClientId.subSet(startSchedule, endSchedule);
    }

    public Vector<SSchedule> getEmployeeSchedules(SEmployee emp) {
        return mapSchedulesByEmployeeId.get(emp.getId());
    }

    public SSchedule getScheduleByDShift(UnitToDisplay shiftToReturnBy) {
        return getScheduleByClientEmployee(shiftToReturnBy.getEmployee(), shiftToReturnBy.getClient(), shiftToReturnBy.getGroupId() + "");
    }

    public SSchedule getScheduleByClientEmployee(SEmployee emp, SMainComponent cli, String groupId) {
        if (emp.getId() > 0) {
            groupId = "0";
        }
        return hashForSchedules.get(cli.getId() + " " + emp.getId() + groupId);
    }

    public void removeSchedule(SSchedule schedToRemove) {
        mapSchedulesByEmployeeId.remove(schedToRemove);
        mapSchedulesByClientId.remove(schedToRemove);
        hashForSchedules.remove(schedToRemove);
    }

    private class employeeIdComparator implements Comparator<SSchedule> {

        public employeeIdComparator() {
        }

        public int compare(SSchedule o1, SSchedule o2) {
            try {
                int employeeName1 = o1.getEmployee().getId();
                int employeeName2 = o2.getEmployee().getId();



                int compSoFar = employeeName1 - employeeName2;
                try {
                    if (compSoFar == 0) {
                        if (o1.getClient() == null || o1.getClient().getId() == null) {
                            compSoFar = -1;
                        } else if (o2.getClient() == null || o2.getClient().getId() == null) {
                            compSoFar = 1;
                        } else {
                            if (o1.getClient().getId().equals("-1") || o2.getClient().getId().equals("-2")) {
                                compSoFar = -1;
                            } else if (o2.getClient().getId().equals("-1") || o1.getClient().getId().equals("-2")) {
                                compSoFar = 1;
                            } else {
                                compSoFar = o1.getClient().getId().compareTo(o2.getClient().getId());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return compSoFar;
//                String[] o1Comp = o1.getMyCompareString();
//                String[] o2Comp = o2.getMyCompareString();
//                int myComp = o1Comp[2].compareToIgnoreCase(o2Comp[2]);
//                if (myComp != 0) {
//                    return myComp;
//                } else {
//                    myComp = o1Comp[0].compareToIgnoreCase(o2Comp[0]);
//                    if (myComp != 0) {
//                        return myComp;
//                    } else {
//                        return o1Comp[1].compareToIgnoreCase(o2Comp[1]);
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    private class clientComparator implements Comparator<SSchedule> {

        public clientComparator() {
        }

        public int compare(SSchedule o1, SSchedule o2) {
            try {
                String[] o1Comp = o1.getMyCompareString();
                String[] o2Comp = o2.getMyCompareString();
                int myComp = 0;
                for (int i = 0; i < o1Comp.length; i++) {
                    myComp = o1Comp[i].trim().compareToIgnoreCase(o2Comp[i].trim());
                    if (myComp != 0) {
                        return myComp;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
