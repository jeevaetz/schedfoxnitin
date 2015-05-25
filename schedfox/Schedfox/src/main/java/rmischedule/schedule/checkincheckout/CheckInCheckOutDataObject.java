/*
 * CheckInCheckOutDataObject.java
 *
 * Created on June 23, 2005, 7:26 AM
 */
package rmischedule.schedule.checkincheckout;

import java.text.SimpleDateFormat;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;

import rmischeduleserver.util.StaticDateTimeFunctions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;
import javax.swing.SwingUtilities;
import schedfoxlib.model.AssembleCheckinScheduleType;
import schedfoxlib.model.Branch;

/**
 *
 * @author ira
 */
public class CheckInCheckOutDataObject {

    public static CheckPanel myCheckInPanel;
    public static CheckPanel myCheckOutPanel;
    public static CheckPanel myNeverClocked;
    private DataSynchronizerThread myDataThread;
    private static final int MINUTES_BEFORE_MOVE_TO_NOT_CHECKED_IN = 90;
    private ArrayList<IndividualCheckInCheckOutPanel> myCheckInCheckOutArrayList;
    private Hashtable<String, IndividualCheckInCheckOutPanel> myHashTable;
    private static TimeZone timezone = Calendar.getInstance().getTimeZone();

    /**
     * Creates a new instance of CheckInCheckOutDataObject
     */
    public CheckInCheckOutDataObject(CheckPanel myCheckIn, CheckPanel myCheckOut, CheckPanel myNeverClockedTemp) {
        myCheckInPanel = myCheckIn;
        myCheckOutPanel = myCheckOut;
        myNeverClocked = myNeverClockedTemp;

        myCheckInCheckOutArrayList = new ArrayList(90);

        myHashTable = new Hashtable(90);

        myDataThread = new DataSynchronizerThread(this);
        myDataThread.start();
    }

    /**
     * Method to update this data via the heartbeat....
     */
    public void updateObjectWithScheduleData(AssembleCheckinScheduleType currRecord) {
        IndividualCheckInCheckOutPanel myPanel = myHashTable.get(currRecord.getSid());

        if (myPanel == null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            String oldId = "-" + currRecord.getSmid() + "/" + myFormat.format(currRecord.getDate());
            myPanel = myHashTable.get(oldId);
            if (myPanel != null) {
                updateHashTableWithShiftId(oldId, currRecord.getSid(), myPanel);
            }
        }

        if (myPanel != null) {
            myPanel.updateValuesWithRecordSet(currRecord);
        } else {
            addNewData(currRecord);
        }
    }

    /**
     * Method to update shift id entry in hashtable only for given shift id,
     * links to provided object
     */
    public void updateHashTableWithShiftId(String oldShiftId, String newShiftId, IndividualCheckInCheckOutPanel myPanel) {
        myHashTable.remove(oldShiftId);
        myHashTable.put(newShiftId, myPanel);
    }

    /**
     * Tests if RecordSet of data falls in necessary range and adds it if it
     * does...
     */
    public void addNewData(AssembleCheckinScheduleType currRecord) {
        boolean shouldAdd = false;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar dataCal = Calendar.getInstance();
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(currRecord.getDate());
        dataCal = StaticDateTimeFunctions.setCalendarTo(tempCal);
        startCal.add(Calendar.MINUTE, -1 * Main_Window.AMOUNT_TO_CHECKIN_BUFFER);
        endCal.add(Calendar.MINUTE, Main_Window.AMOUNT_TO_CHECKIN_BUFFER);
        StaticDateTimeFunctions.zeroHourMinuteFeild(dataCal);
        dataCal.add(Calendar.MINUTE, currRecord.getStart_time());
        if (dataCal.compareTo(startCal) > 0 && dataCal.compareTo(endCal) < 0) {
            shouldAdd = true;
        }
        dataCal.setTime(currRecord.getDate());
        StaticDateTimeFunctions.zeroHourMinuteFeild(dataCal);
        dataCal.add(Calendar.MINUTE, currRecord.getEnd_time());
        if (dataCal.compareTo(startCal) > 0 && dataCal.compareTo(endCal) < 0) {
            shouldAdd = true;
        }
        if (shouldAdd) {
            IndividualCheckInCheckOutPanel newPanel = new IndividualCheckInCheckOutPanel(myCheckInPanel, currRecord);
            newPanel.setBranch(currRecord.getBranch_id().toString());
            newPanel.setCompany(currRecord.getCompanyId());
            myCheckInCheckOutArrayList.add(newPanel);
            if (myHashTable.get(currRecord.getSid()) == null) {
                myHashTable.put(currRecord.getSid(), newPanel);
            } else {
                System.out.println("bad!!");
            }
            newPanel.setMyParent(myCheckInPanel);
            refreshPanels();
        }
    }

    /**
     * Refreshes our panels...
     */
    public void refreshPanels() {
        Calendar currCal = Calendar.getInstance();
        final String currTime = (currCal.get(Calendar.HOUR_OF_DAY) * 60 + currCal.get(Calendar.MINUTE)) + "";
        final String date = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(currCal);
        CheckPanel.Now = Calendar.getInstance();

        try {
            Runnable refreshPanelsRunnable = new Runnable() {

                public void run() {
                    myCheckInPanel.removeAll();
                    myCheckOutPanel.removeAll();
                    myNeverClocked.removeAll();



                    int size = myCheckInCheckOutArrayList.size();
                    for (int i = 0; i < size; i++) {
                        myCheckInCheckOutArrayList.get(i).setParent();
                        myCheckInCheckOutArrayList.get(i).setBackground(i, currTime, date);
                    }

                    myCheckInPanel.refreshData();
                    myCheckOutPanel.refreshData();
                    myNeverClocked.refreshData();
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                refreshPanelsRunnable.run(); 
            } else {
                SwingUtilities.invokeAndWait(refreshPanelsRunnable);
            }
        } catch (Exception xe) {
            xe.printStackTrace();
        }
    }

    /**
     * Updates our individual panels with the checkindata from either timer, or
     * heartbeat...
     */
    public void updateWithCheckInData(ArrayList checkInData) {
        Record_Set currRecord;

        int size = 0;

        for (int i = 0; i < checkInData.size(); i++) {
            try {
                currRecord = (Record_Set) checkInData.get(i);
                currRecord.decompressData();
                size += currRecord.length();
                for (int x = 0; x < currRecord.length(); x++) {
                    AssembleCheckinScheduleType newCheckin = new AssembleCheckinScheduleType(currRecord);
                    String shiftId = newCheckin.getSid();
                    IndividualCheckInCheckOutPanel newPanel = myHashTable.get(shiftId);

                    if (!newCheckin.getPerson_checked_out().equals("0") && newPanel != null) {
                        newPanel.setVisible(false);
                        myCheckInCheckOutArrayList.remove(newPanel);
                    } else if (!newCheckin.getPerson_checked_in().equals("0") && newPanel != null) {
                        newPanel.setCheckOutInfo(newCheckin);
                        newPanel.setMyParent(myCheckOutPanel);
                    }
                    currRecord.moveNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        refreshPanels();
    }

    /**
     * Method to reload all displayed data completely from the timer....
     */
    public void updateViaTimer(ArrayList<AssembleCheckinScheduleType> schedData) {
        AssembleCheckinScheduleType currRecord;
        Calendar currCal = Calendar.getInstance();
        myCheckInCheckOutArrayList.clear();
        myHashTable.clear();
        for (int s = 0; s < schedData.size(); s++) {
            currRecord = schedData.get(s);
            IndividualCheckInCheckOutPanel newPanel = null;
            Branch entryBranch =
                    IndividualCheckInCheckOutPanel.getBranchInfo(Integer.parseInt(currRecord.getCompanyId()), currRecord.getBranch_id());

            Calendar compCal = Calendar.getInstance();

            compCal.setTime(currRecord.getDate());
            compCal.set(Calendar.HOUR_OF_DAY, (int) Math.floor(currRecord.getStart_time() / 60));
            compCal.set(Calendar.MINUTE, (int) Math.floor(currRecord.getStart_time() % 60));
            //Give one hour of buffer before they drop off.
            compCal.add(Calendar.HOUR_OF_DAY, +2);
            compCal.setTimeZone(TimeZone.getTimeZone(entryBranch.getTimezone()));

            //Not checked in.
            if ((!currCal.after(compCal)) && (currRecord.getPerson_checked_in() == null || currRecord.getPerson_checked_in().equals(""))) {
                newPanel = new IndividualCheckInCheckOutPanel(myCheckInPanel, currRecord);
            } else if (currRecord.getPerson_checked_in() == null || currRecord.getPerson_checked_in().equals("")) {
                //Never checked in
                newPanel = new IndividualCheckInCheckOutPanel(myNeverClocked, currRecord);
            } else {
                Calendar endCal = Calendar.getInstance();

                endCal.setTime(currRecord.getDate());
                endCal.set(Calendar.HOUR_OF_DAY, (int) Math.floor(currRecord.getEnd_time() / 60));
                endCal.set(Calendar.MINUTE, (int) Math.floor(currRecord.getEnd_time() % 60));
                if (currRecord.getEnd_time() < currRecord.getStart_time()) {
                    endCal.add(Calendar.DAY_OF_WEEK, 1);
                }
                endCal.setTimeZone(TimeZone.getTimeZone(entryBranch.getTimezone()));
                //Checked in
                if ((!currCal.after(endCal))) {
                    newPanel = new IndividualCheckInCheckOutPanel(myCheckOutPanel, currRecord);
                    newPanel.setCheckOutInfo(currRecord);
                }
            }
            if (newPanel != null) {
                myCheckInCheckOutArrayList.add(newPanel);
                myHashTable.put(currRecord.getSid(), newPanel);
            }
        }
        this.refreshPanels();
    }
}
