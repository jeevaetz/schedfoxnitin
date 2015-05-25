//  package declaration
package rmischedule.utility;

//  import declarations
import java.util.Calendar;

/**
 *  A class to return string dates.
 *
 *  <p>This class contains simple public static methods designed to
 *      handle the <code>Calendar</code> class in a more friendly manner.
 *      The API is designed to return properly formatted <code>Strings</code>
 *      for various date components.
 *  @author jdavis
 *  @since 02/10/2011
 */
public final class FriendlyCalendar 
{
    /** Default initialization of this object */
    public FriendlyCalendar()   {}

    /**
     *  Determines day of week
     *  @returns dayOfWeek - a string describing the day of the week
     */
    public static String DAY_OF_WEEK()
    {
        String dayOfWeek = null;
        Calendar cal = Calendar.getInstance();

        if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
            dayOfWeek = "Sunday";
        else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.MONDAY )
            dayOfWeek = "Monday";
        else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.TUESDAY )
            dayOfWeek = "Tuesday";
        else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.WEDNESDAY )
            dayOfWeek = "Wednesday";
        else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.THURSDAY )
            dayOfWeek = "Thursday";
        else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY )
            dayOfWeek = "Friday";
        else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY )
            dayOfWeek = "Saturday";

        return dayOfWeek;
    }

    /**
     *  Determines the year
     *  @returns year - a string representing the year
     */
    public static String YEAR()
    {
        String year = null;

        Calendar cal = Calendar.getInstance();
        year = Integer.toString( cal.get( Calendar.YEAR ));

        return year;
    }

    /**
     *  Determines the day of the month
     *  @returns dayOfMonth - a string describing the day of the month
     */
    public static String DAY_OF_MONTH()
    {
        String dayOfMonth = null;

        Calendar cal = Calendar.getInstance();
        dayOfMonth = Integer.toString( cal.get( Calendar.DAY_OF_MONTH ));

        return dayOfMonth;
    }

    /**
     *  Determines the month
     *  @returns month - a string representing the month
     */
    public static String MONTH()
    {
        String month = null;

        Calendar cal = Calendar.getInstance();
        if ( cal.get( Calendar.MONTH ) == Calendar.JANUARY )
            month = "January";
        else if ( cal.get( Calendar.MONTH ) == Calendar.FEBRUARY )
            month = "February";
        else if ( cal.get( Calendar.MONTH ) == Calendar.MARCH )
            month = "March";
        else if ( cal.get( Calendar.MONTH ) == Calendar.APRIL )
            month = "April";
        else if ( cal.get( Calendar.MONTH ) == Calendar.MAY )
            month = "May";
        else if ( cal.get( Calendar.MONTH ) == Calendar.JUNE )
            month = "June";
        else if ( cal.get( Calendar.MONTH ) == Calendar.JULY )
            month = "July";
        else if ( cal.get( Calendar.MONTH ) == Calendar.AUGUST )
            month = "August";
        else if ( cal.get( Calendar.MONTH ) == Calendar.SEPTEMBER )
            month = "September";
        else if ( cal.get( Calendar.MONTH ) == Calendar.OCTOBER )
            month = "October";
        else if ( cal.get( Calendar.MONTH ) == Calendar.NOVEMBER )
            month = "November";
        else if ( cal.get( Calendar.MONTH ) == Calendar.DECEMBER )
            month = "December";
        
        return month;
    }

    /**
     *  Formats a readable date for today, returns it
     *
     *  <p><b>FORMAT:  </b> DAY_OF_WEEK, MONTH DAY_OF_MONTH, YEAR
     *  @returns readableToday - a string representing today's date in a readable
     *      format
     */
    public static String READABLE_TODAY()
    {
        StringBuilder readableToday = new StringBuilder();

        readableToday.append(FriendlyCalendar.DAY_OF_WEEK());
        readableToday.append(" ");
        readableToday.append(FriendlyCalendar.MONTH());
        readableToday.append(" ");
        readableToday.append(FriendlyCalendar.DAY_OF_MONTH());
        readableToday.append(" ");
        readableToday.append(FriendlyCalendar.YEAR());

        return readableToday.toString();
    }
};
