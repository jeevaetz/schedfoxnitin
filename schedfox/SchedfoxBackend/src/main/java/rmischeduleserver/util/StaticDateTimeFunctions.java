/*
 * StaticDateTimeFunctions.java
 *
 * Created on November 24, 2004, 12:20 PM
 */
package rmischeduleserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import rmischeduleserver.control.OptionsController;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_company_view_options_query;

/**
 *
 * @author  ira
 */
public class StaticDateTimeFunctions {

    public static final int MILITARY_FORMAT = 1;
    public static final int STANDARD_FORMAT = 2;
    public static final int DECIMAL_FORMAT = 3;
    public static final int SCHEDULE_FORMAT = 4;
    private static final String[] wkdy = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static long differenceBetweenClientAndServer;
    private static HashMap<Integer, Integer> companiesToStartOfWeek = new HashMap<Integer, Integer>();
    private static HashMap<Integer, Integer> companiesRoundingTechnique = new HashMap<Integer, Integer>();

    /** Creates a new instance of StaticDateTimeFunctions */
    public StaticDateTimeFunctions() {
    }

    /**
     * Returns array of Strings representing Months
     */
    public static String[] getMonthsOfYear() {
        return months;
    }

    /**
     * Returns Calendar int, for given month passed in String format ie:
     * January returns Calendar.JANUARY, ignores case
     * -1 if invalid data...
     */
    public static int getCalendarIntForMonth(String month) {
        if (month.compareToIgnoreCase(months[0]) == 0) {
            return Calendar.JANUARY;
        } else if (month.compareToIgnoreCase(months[1]) == 0) {
            return Calendar.FEBRUARY;
        } else if (month.compareToIgnoreCase(months[2]) == 0) {
            return Calendar.MARCH;
        } else if (month.compareToIgnoreCase(months[3]) == 0) {
            return Calendar.APRIL;
        } else if (month.compareToIgnoreCase(months[4]) == 0) {
            return Calendar.MAY;
        } else if (month.compareToIgnoreCase(months[5]) == 0) {
            return Calendar.JUNE;
        } else if (month.compareToIgnoreCase(months[6]) == 0) {
            return Calendar.JULY;
        } else if (month.compareToIgnoreCase(months[7]) == 0) {
            return Calendar.AUGUST;
        } else if (month.compareToIgnoreCase(months[8]) == 0) {
            return Calendar.SEPTEMBER;
        } else if (month.compareToIgnoreCase(months[9]) == 0) {
            return Calendar.OCTOBER;
        } else if (month.compareToIgnoreCase(months[10]) == 0) {
            return Calendar.NOVEMBER;
        } else if (month.compareToIgnoreCase(months[11]) == 0) {
            return Calendar.DECEMBER;
        } else {
            return -1;
        }
    }

    /**
     * Gets a Calendar object for the beggining of this week, MONDAY, yippy
     */
    public static Calendar getBegOfWeek(Integer companyId) {
        Calendar startDate = Calendar.getInstance();
        return getBegOfWeek(startDate, companyId);
    }

    /**
     * Returns Day of Week in Format of First Three Letters Ie: Mon, Tue etc..
     */
    public static String getDayOfWeek(Calendar myCal) {
        if (myCal.getFirstDayOfWeek() != Calendar.MONDAY) {
            myCal.setFirstDayOfWeek(Calendar.MONDAY);
        }
        return getDayOfWeek(myCal.get(Calendar.DAY_OF_WEEK) - 1).substring(0, 3);
    }

    public static String getDayOfWeek(int dow) {
        return wkdy[dow];
    }

    /**
     * Returns Full Day of Week
     */
    public static String getDayOfWeekFull(Calendar myCal) {
        if (myCal.getFirstDayOfWeek() != Calendar.MONDAY) {
            myCal.setFirstDayOfWeek(Calendar.MONDAY);
        }
        return wkdy[myCal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * Loads the week information for the provided company.
     * @param companyId
     */
    public static void loadWeekInfoForCompany(Integer companyId) {
        if (companiesToStartOfWeek.get(companyId) == null) {
            try {
                OptionsController optionsController = OptionsController.getInstance(companyId.toString());
                companiesToStartOfWeek = optionsController.getCompaniesToStartWeek();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    /**
     * Loads the rounding strategies for the provided company.
     * @param companyId
     */
    public static void loadNumberMinutesRoundingForCompany(Integer companyId) {
        if (companiesRoundingTechnique.get(companyId) == null) {
            try {
                OptionsController optionsController = OptionsController.getInstance(companyId.toString());
                companiesRoundingTechnique = optionsController.getMinutesRoundingForCompany();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    /**
     * Gets the number of minutes to round by for a specific company
     * @param companyId
     * @return
     */
    public static int getNumberOfMinutesToRound(Integer companyId) {
        loadNumberMinutesRoundingForCompany(companyId);
        return companiesRoundingTechnique.get(companyId);
    }

    /**
     * Gets a Calendar object for the end of this week, SUNDAY
     */
    public static Calendar getEndOfWeek(Integer companyId) {
        Calendar endDate = Calendar.getInstance();
        return getEndOfWeek(endDate, companyId);
    }

    //MODIFIED BY LUIS 28/DEC/2006
    public static Calendar getBegOfWeek(Calendar startDate, Integer companyId) {
        loadWeekInfoForCompany(companyId);
        Integer dayOfWeek = companiesToStartOfWeek.get(companyId);
        startDate.setFirstDayOfWeek(dayOfWeek.intValue());
        startDate.add(Calendar.DAY_OF_WEEK, -(startDate.get(Calendar.DAY_OF_WEEK) - dayOfWeek.intValue()));
        return startDate;
    }

    //MODIFIED BY LUIS 28/DEC/2006
    public static Calendar getEndOfWeek(Calendar endDate, Integer companyId) {
        loadWeekInfoForCompany(companyId);
        Integer dayOfWeek = companiesToStartOfWeek.get(companyId);
        endDate.setFirstDayOfWeek(dayOfWeek.intValue());
        endDate.add(Calendar.DAY_OF_WEEK, -(endDate.get(Calendar.DAY_OF_WEEK) - 6 - dayOfWeek.intValue()));
        return endDate;
    }

    public static boolean isEndOfWeek(Calendar endDate, Integer companyId) {
        loadWeekInfoForCompany(companyId);
        Integer dayOfWeek = companiesToStartOfWeek.get(companyId);
        int tempDayOfWeek = endDate.get(Calendar.DAY_OF_WEEK) + 1;
        if (tempDayOfWeek == 8) {
            tempDayOfWeek = 1;
        }
        return (tempDayOfWeek == dayOfWeek);
    }

    public static Calendar getBegOfMonth(Calendar month) {
        month.add(Calendar.DAY_OF_MONTH, -(month.get(Calendar.DAY_OF_MONTH) - 1));
        return month;
    }

    public static int getStartOfWeekInt(Integer companyId) {
        loadWeekInfoForCompany(companyId);
        return companiesToStartOfWeek.get(companyId);
    }

    public static Calendar getEndOfMonth(Calendar month) {

        int Month = month.get(Calendar.MONTH);
        while (month.get(Calendar.MONTH) == Month) {
            month.add(Calendar.DAY_OF_YEAR, 1);
        }
        month.add(Calendar.DAY_OF_YEAR, -1);
        return month;
    }

    /**
     * Tests if two Calendars are equal by Day_Of_Month, Month and Year Fields
     *
     * This Function Tests If Two Calendars are equal by testing the DAY_OF_MONTH, YEAR, and MONTH fields
     */
    public static boolean areCalendarsEqual(Calendar cal1, Calendar cal2) {
        if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {
            if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function compares two Calendars returning > 0 if Cal1 greater, < 0 if Cal1 less and 0 if equal
     * Strings in format yyyy-mm-dd (Database format)
     */
    public static int compareDatesWithHourMinute(Calendar date1, Calendar date2) {
        int difference = (date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR));
        if (difference != 0) {
            return difference;
        }
        difference = (date1.get(Calendar.MONTH) - date2.get(Calendar.MONTH));
        if (difference != 0) {
            return difference;
        }
        difference = (date1.get(Calendar.DAY_OF_MONTH) - date2.get(Calendar.DAY_OF_MONTH));
        if (difference != 0) {
            return difference;
        }
        difference = (date1.get(Calendar.HOUR_OF_DAY) - date2.get(Calendar.HOUR_OF_DAY));
        if (difference != 0) {
            return difference;
        }
        difference = (date1.get(Calendar.MINUTE) - date2.get(Calendar.MINUTE));
        return difference;
    }

    /**
     * Small helper function designed to convert any time into a four digit string
     * ie 20 = 0020, why you ask? Well string compare of course! Otherwise 1000 < 20
     * cause 1 < 2....
     */
    public static String convertTimeToFourDigits(int time) {
        String returnString = new String(time + "");
        while (returnString.length() < 4) {
            returnString = "0" + returnString;
        }
        return returnString;
    }

    /**
     * Same idea as Database Format but without 02 instead 2 for months and days
     * created really to compare dates supplied by DShift
     */
    public static String convertCalendarToSchedFormat(Calendar input) {
        String year = input.get(Calendar.YEAR) + "";
        String month = input.get(Calendar.MONTH) + 1 + "";
        if (month.compareTo("0") == 0) {
            month = "1";
        }
        String day = input.get(Calendar.DAY_OF_MONTH) + "";
        return year + "-" + month + "-" + day;
    }

    /**
     * Converts Calendar to format YYYY-MM-DD with subtracting one from the month field...
     */
    public static String convertCalendarToDatabaseFormatWithMinusMonth(Calendar input) {
        String year = input.get(Calendar.YEAR) + "";
        String month = (input.get(Calendar.MONTH) - 1) + "";
        if (month.compareTo("0") == 0) {
            month = "01";
        }
        String day = input.get(Calendar.DAY_OF_MONTH) + "";
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public static String convertCalendarToDatabaseFormatWithOutAdjust(Calendar input) {
        String year = input.get(Calendar.YEAR) + "";
        String month = input.get(Calendar.MONTH) + 1 + "";
        if (month.compareTo("0") == 0) {
            month = "01";
        }
        String day = input.get(Calendar.DAY_OF_MONTH) + "";
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public static String convertCalendarToDatabaseFormat(Calendar input) {
        String year = input.get(Calendar.YEAR) + "";
        String month = input.get(Calendar.MONTH) + 1 + "";
        if (month.compareTo("0") == 0) {
            month = "01";
        }
        String day = input.get(Calendar.DAY_OF_MONTH) + "";
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public static String convertCalendarToReadableFormat(Calendar input) {
        String year = input.get(Calendar.YEAR) + "";
        String month = (input.get(Calendar.MONTH) + 1) + "";
        if (month.compareTo("0") == 0) {
            month = "12";
        }
        String day = input.get(Calendar.DAY_OF_MONTH) + "";
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        return month + "/" + day + "/" + year;
    }

    /**
     * Generates a Calendar from either a yyyy-mm-dd format or mm/dd/yyyy format
     * or mm\dd\yyyy
     */
    public static Calendar setCalendarToString(String input) {

        StringTokenizer myTokenizer = new StringTokenizer(input, "- ");
        if (myTokenizer.countTokens() > 1) {
            return setCalendarToStringFromDatabaseFormat(myTokenizer);
        } else {
            StringTokenizer tokenizer = new StringTokenizer(input, "/\\ ");
            return setCalendarToStringFromReadableFormat(tokenizer);
        }
    }

    public static Calendar zeroHourMinuteFeild(Calendar input) {
        input.add(Calendar.HOUR_OF_DAY, -input.get(Calendar.HOUR_OF_DAY));
        input.add(Calendar.MINUTE, -input.get(Calendar.MINUTE));
        return input;
    }

    private static Calendar setCalendarToStringFromDatabaseFormat(StringTokenizer input) {
        Calendar tempCal = Calendar.getInstance();
        int year = Integer.parseInt(input.nextToken());
        int month = Integer.parseInt(input.nextToken()) - 1;

        int day = Integer.parseInt(input.nextToken());
        tempCal.clear();
        tempCal.set(year, month, day);

        return tempCal;
    }

    private static Calendar setCalendarToStringFromReadableFormat(StringTokenizer input) {
        Calendar tempCal = Calendar.getInstance();
        int month = Integer.parseInt(input.nextToken());
        int day = Integer.parseInt(input.nextToken());
        int year = Integer.parseInt(input.nextToken());
        tempCal.add(Calendar.YEAR, -(tempCal.get(Calendar.YEAR) - year));
        tempCal.add(Calendar.MONTH, (-(tempCal.get(Calendar.MONTH) - month)));
        tempCal.add(Calendar.DAY_OF_MONTH, -(tempCal.get(Calendar.DAY_OF_MONTH) - day));
        return tempCal;
    }

    /**
     * Takes a format from yyyy-mm-dd and converts to more legible mm/dd/yyyy
     */
    public static String convertDatabaseDateToReadable(String input) {
        StringTokenizer st = new StringTokenizer(input, "- ");
        String year = st.nextToken();
        String month = st.nextToken();
        String day = st.nextToken();
        return month + "/" + day + "/" + year;
    }

    /**
     * Takes a format from mm/dd/yyyy and converts to a database format of yyyy-mm-dd
     */
    public static String convertReadableToDatabase(String input) {
        StringTokenizer st = new StringTokenizer(input, "/ ");
        String month = st.nextToken();
        String day = st.nextToken();
        String year = st.nextToken();
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    /**
     * Takes a time in either standard to military format and converts it to database
     * minute format, input either 1840 for Military or 12:01 PM for standard, must contain
     * both a colon and a space for standard.
     */
    public static String fromTextToTime(String text) {
        String time = new String();
        StringTokenizer st = new StringTokenizer(text, ": ");
        if (st.countTokens() > 1) {
            int hour = 60 * Integer.parseInt(st.nextToken());
            int minute = Integer.parseInt(st.nextToken());
            int ttime = hour + minute;
            try {
                String AMPM = st.nextToken();
                if (AMPM.matches("P.") || AMPM.matches("p.") || AMPM.matches("p") || AMPM.matches("P")) {
                    ttime = ttime + (60 * 12);
                }
            } catch (Exception e) {
            }
            time = new Integer(ttime).toString();
            if (text.equals("12:00 AM")) {
                time = "0";
            } else if (text.equals("12:00 PM")) {
                time = "720";
            }
        } else {
            int pos = 2;
            if (text.length() < 4) {
                pos = 1;
            }
            if (text.length() < 3) {
                return text;
            }
            int hour = Integer.parseInt(text.substring(0, pos));
            int min = Integer.parseInt(text.substring(pos));
            int ttime = (hour * 60) + min;
            return ttime + "";
        }

        return time + "";
    }

    /**
     * Uses format specified by Main_Window
     */
    public static String stringToFormattedTime(String time, boolean is12HourFormat) {
        int format = MILITARY_FORMAT;
        try {
            if (is12HourFormat) {
                format = STANDARD_FORMAT;
            }
        } catch (Exception e) {}
        return stringToFormattedTime(time, format);
    }

    /**
     * Takes into consideration day of week to print out the following time in, out in
     * military or standard, plus two letter abreviation for what day it lies on...
     *
     */
    public static String[] stringToScheduleTime(int stime, int etime, int format, int sDayOfWeek) {
        String[] myRetString = new String[2];
        myRetString[0] = stringToFormattedTime(stime + "", format);
        myRetString[1] = stringToFormattedTime(etime + "", format);
        if (sDayOfWeek == 7) {
            sDayOfWeek = 0;
        }
        if (etime == 1440 || etime == 0 || (etime <= stime && sDayOfWeek >= 0)) {
            myRetString[0] = myRetString[0] + " " + wkdy[sDayOfWeek].substring(0, 2);
            myRetString[1] = myRetString[1] + " " + wkdy[(sDayOfWeek + 1) % 7].substring(0, 2);
        }
        return myRetString;
    }

    /**
     * Takes a string in database minute format and converts it to whatever
     * format is specified in our Main_Window, should only be used by a running
     * copy of SchedFox do not use in other modules will throw null pointer when
     * accessing the Main_Window otherwise
     */
    public static String stringToDefinedFormattedTime(String time, Boolean is12HourFormat) {
        try {
            if (is12HourFormat) {
                return stringToFormattedTime(time, STANDARD_FORMAT);
            }
        } catch (Exception e) {}
        return stringToFormattedTime(time, MILITARY_FORMAT);
    }

    /*
     * Converts from minute format to either Military or Standard Format
     */
    public static String stringToFormattedTime(String time, int format) {
        try {
            if (format == MILITARY_FORMAT) {
                return stringToMilitaryTime(time);
            } else if (format == STANDARD_FORMAT) {
                return stringToStandardTime(time);
            } else {
                return stringToDecimalTime(time);
            }
        } catch (Exception e) {
            if (format == MILITARY_FORMAT) {
                return "0000";
            } else if (format == STANDARD_FORMAT) {
                return "12:00 AM";
            } else {
                return "00.0";
            }
        }
    }

    /**
     * Takes in a integer string, and returns back a string in the format HH:MM AM
     * @param time String of time,
     * @return
     * @throws ParseException
     */
    public static String stringToStandardTime(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");

        int totalTime = Integer.parseInt(time);
        int hour = totalTime / 60;
        int min = totalTime % 60;

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.HOUR_OF_DAY, hour);
        myCal.set(Calendar.MINUTE, min);

        return dateFormat.format(myCal.getTime());
    }

    public static String stringToMilitaryTime(String time) {
        int myVal = Integer.parseInt(time);
        int hour = new Double(Math.ceil(myVal / 60)).intValue();
        int min = myVal - (hour * 60);
        String hourS = new String();
        String minS = new String();
        String returnS = new String();
        if (hour < 10) {
            hourS = "0" + hour;
        } else {
            hourS = hour + "";
        }
        if (min < 10) {
            minS = "0" + min;
        } else {
            minS = min + "";
        }
        if (hourS.equalsIgnoreCase("00") && minS.equalsIgnoreCase("00")) {
            hourS = "24";
        }
        return hourS + minS;
    }

    public static String stringToDecimalTime(String time) {
        int myVal = Integer.parseInt(time);
        int hour = new Double(Math.ceil(myVal / 60)).intValue();
        int min = (myVal - (hour * 60)) / 60;
        String hourS = new String();
        String minS = new String();
        String returnS = new String();
        hourS = hour + "";
        minS = min + "";
        return hourS + "." + minS;
    }

    /**
     * This method will return a valid Calendar object where all fields should
     * be set appropriately from input Calendar, this method will get rid of erros
     * when only date, month and year are set right...
     */
    public static Calendar setCalendarTo(Calendar input) {
        int differenceDay = 0;
        int differenceMonth = 0;
        int differenceYear = 0;
        Calendar output = Calendar.getInstance();
//        differenceDay = output.get(Calendar.DAY_OF_YEAR) - input.get(Calendar.DAY_OF_YEAR);
//        //differenceMonth = output.get(Calendar.MONTH) - input.get(Calendar.MONTH);
//        differenceYear = output.get(Calendar.YEAR) - input.get(Calendar.YEAR);
//
//
//        output.add(Calendar.YEAR, -differenceYear);
//        //output.add(Calendar.MONTH, -differenceMonth);
//        output.add(Calendar.DAY_OF_YEAR, -differenceDay);
//        
//        output.setTime(input.getTime());

        output.setTime(input.getTime());

        return output;
        //return input;
    }

    /**
     * Best way to difference in Days between two Calendars, handles between years and etc quite
     * well...has been tested specifically for this...
     */
    public static int getDifferenceInDays(Calendar fromC, Calendar toC) {
        long from = fromC.getTime().getTime();
        long to = toC.getTime().getTime();
        double difference = to - from;
        int days = new Long(Math.round((difference / (1000 * 60 * 60 * 24)))).intValue();
        if (days < 0) {
            return -days;
        }
        return days;
    }

    /**
     * Slow method but really only way of setting all fields in a Calendar object given only
     * the DAY, MONTH, and YEAR fields, will set all other fields appropriately...
     */
    public static Calendar setAllCalendarFields(Calendar input) {
        Calendar newCal = Calendar.getInstance();
        int diffyear = newCal.get(Calendar.YEAR) - input.get(Calendar.YEAR);
        int diffmonth = newCal.get(Calendar.MONTH) - input.get(Calendar.MONTH);
        int diffday = newCal.get(Calendar.DAY_OF_MONTH) - input.get(Calendar.DAY_OF_MONTH);
        int diff = diffyear * 4000 + diffmonth * 200 + diffday;
        int incrementAmount = 0;
        if (diff > 0) {
            incrementAmount = -1;
        } else {
            incrementAmount = 1;
        }
        newCal.setFirstDayOfWeek(Calendar.MONDAY);
        while (newCal.get(Calendar.DAY_OF_MONTH) != input.get(Calendar.DAY_OF_MONTH)
                || newCal.get(Calendar.MONTH) != input.get(Calendar.MONTH)
                || newCal.get(Calendar.YEAR) != input.get(Calendar.YEAR)) {
            newCal.add(Calendar.DAY_OF_WEEK, incrementAmount);
        }
        return newCal;
    }

    /**
     * Takes a input in the format of 2005-06-12 10:15:19.123432 and returns a Calendar...yippy
     */
    public static Calendar setCalendarToWithHours(String input) {
        StringTokenizer strToken = new StringTokenizer(input, " .");
        Calendar myCal = StaticDateTimeFunctions.setCalendarToString(strToken.nextToken());
        StringTokenizer hourMin = new StringTokenizer(strToken.nextToken(), ":");
        if (hourMin.countTokens() < 2) {
            return myCal;
        } else {
            Integer hour = Integer.parseInt(hourMin.nextToken());
            Integer min = Integer.parseInt(hourMin.nextToken());
            myCal.add(Calendar.HOUR_OF_DAY, -myCal.get(Calendar.HOUR_OF_DAY));
            myCal.add(Calendar.HOUR_OF_DAY, hour);
            myCal.add(Calendar.MINUTE, -myCal.get(Calendar.MINUTE));
            myCal.add(Calendar.MINUTE, min);
        }
        return myCal;
    }

    public static String fromTextBoxToTime(String text, JComboBox jcb) {
        int time = 0;
        int addPM = 0;

        if (((String) (jcb.getSelectedItem())).compareTo("PM") == 0) {
            addPM = 12 * 60;
        }
        //Handles correct format for when 12:00 AM should be val of zero not 740 or whatever
        if ((((String) (jcb.getSelectedItem())).compareTo("AM") == 0) && text.compareTo("12:00") == 0) {
            text = "00:00";
        }
        if (text.indexOf(":") > 0) {
            time = new Double(Math.ceil(Integer.parseInt(text.substring(0, text.indexOf(":"))) * 60.0)).intValue();
            time = time + addPM;
            time = time + Integer.parseInt(text.substring(text.indexOf(":") + 1, text.length()));
        } else {
            String hourS = new String();
            if (text.length() > 3) {
                hourS = text.substring(0, 1);
            } else if (text.length() > 2) {
                hourS = text.substring(0, 2);
            } else {
                hourS = "0";
            }
            time = new Double(Math.ceil(Integer.parseInt(hourS) * 60)).intValue();
            time = time + ((Integer.parseInt(text) - (time * 60)));
        }
        return time + "";
    }

    /**
     * Sets client and server time zone used to get real time based off of the
     * offset and time zone difference between the too by the method getCorrectTimeFromServer
     */
    public static void setClientServerTimeZone(TimeZone serverTimeZone, long currentServerTime) {
        int timeZoneDifference = TimeZone.getDefault().getRawOffset() - serverTimeZone.getRawOffset();
        differenceBetweenClientAndServer = (System.currentTimeMillis() - currentServerTime) - timeZoneDifference;
    }

    /**
     * Should be used instead of System.currentTimeMillis to get the current number
     * of milliseconds... Uses server time and client time difference to get real time
     * rather than (possibly incorrect) client time...
     */
    public static long getCorrectTimeFromServer() {
        return System.currentTimeMillis() - differenceBetweenClientAndServer;
    }

    /**
     * Function used to trim a double String ie:
     * trimAfterDecimalPlaces("3.1278", 2) will return 3.12
     */
    public static String trimAfterDecimalPlaces(String input, int decimalPlaces) {
        int decPos = input.indexOf(".");
        if (decPos == -1) {
            return input;
        } else {
            String str1 = input.substring(0, decPos);
            String str2 = input.substring(decPos + 1);
            if (str2.length() > decimalPlaces) {
                str2 = str2.substring(0, decimalPlaces);
            }
            return str1 + "." + str2;
        }
    }

    /**
     * Takes in a total and a part and returns a Percentage String
     * ie: getPercentage(25, 5, 2) returns "20%"
     */
    public static String getPercentage(double totalTime, double fractionTime, int decimalPlaces) {
        double myPercentage = 0.0;
        try {
            myPercentage = fractionTime / totalTime;
        } catch (Exception e) {
        }
        myPercentage *= 100;
        String perc = trimAfterDecimalPlaces(myPercentage + "", decimalPlaces);
        return perc + "%";
    }

    public static double getDifferenceAndRoundByNumberOfMinutes(double start, double end, int companyId, double breakMinutes) {
        if (end < start) {
            end += 1440.0;
        }
        int numberMinutesToRound = getNumberOfMinutesToRound(companyId);
        if (numberMinutesToRound == 0) {
            numberMinutesToRound = 1;
        }
        double divideBy = (double) numberMinutesToRound / 60.0;
        double hourDiff = (end - start - breakMinutes);
        double diff = Math.floor(((hourDiff) / 60));
        diff += (Math.floor(Math.abs((diff * 60) - (hourDiff)) / numberMinutesToRound) * divideBy);
        return round2(diff);
    }
    
    /**
     * Takes in two minute values and gets difference rounds off according to how many fifteen minutes are left...
     */
    public static double getDifferenceAndRoundByNumberOfMinutes(double start, double end, int companyId) {
        return StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(start, end, companyId, 0);
    }

    public static double round2(double num) {
        double result = num * 100;
        result = Math.round(result);
        result = result / 100;
        return result;
    }
}
