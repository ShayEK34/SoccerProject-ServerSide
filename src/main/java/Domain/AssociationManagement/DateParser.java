package Domain.AssociationManagement;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * this class meant to turn the Date class into the form of "HH:MM:SS dayName dd:mm:yy
 */
public class DateParser {
    private static final String[] weekdays = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    private static final String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    public static String toString(Date date) {
        StringBuilder localStringBuilder = new StringBuilder(30);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;    // Day of the Week are indexed from 1.

        localStringBuilder.append(date.getHours()).append(":").append(date.getMinutes()).append(":").append(date.getSeconds()).append(" ");

        localStringBuilder.append(Character.toUpperCase(weekdays[dayOfWeek % 7].charAt(0)));
        localStringBuilder.append(weekdays[dayOfWeek % 7].charAt(1)).append(weekdays[dayOfWeek % 7].charAt(2));
        localStringBuilder.append(' ');

        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth <= 9) {
            localStringBuilder.append('0');
        }
        localStringBuilder.append(dayOfMonth);
        localStringBuilder.append('.');

        int monthOfYear = cal.get(Calendar.MONTH);    // Month of the year are indexed from 0.
        if (monthOfYear <= 9) {
            localStringBuilder.append('0');
        }
        localStringBuilder.append(monthOfYear);
        localStringBuilder.append('.');

//        localStringBuilder.append(Character.toUpperCase(months[monthOfYear % 12].charAt(0)));
//        localStringBuilder.append(months[monthOfYear % 12].charAt(1)).append(months[monthOfYear % 12].charAt(2));
//        localStringBuilder.append('.');

        int year = cal.get(Calendar.YEAR);
        localStringBuilder.append(year);

        return localStringBuilder.toString();
    }

}
