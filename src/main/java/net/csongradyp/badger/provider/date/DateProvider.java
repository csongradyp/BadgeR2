package net.csongradyp.badger.provider.date;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateProvider {

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM-dd");
    private static final DateTimeFormatter  timeFormatter = DateTimeFormat.forPattern("HH:mm");

    private DateProvider() {
    }

    public static String currentDateString() {
        return format(new Date().getTime());
    }

    public static Date currentDate() {
        return new DateTime().toLocalDate().toDate();
    }

    public static String currentTimeString() {
        return getTime(new Date());
    }

    public static Date currentTime() {
        return new Date();
    }

    public static String getDate(final Date date) {
        return format(date.getTime());
    }

    public static Date parseDate(final String date) {
        return dateFormatter.parseLocalDate(date).toDate();
    }

    protected static String format(final Long time) {
        return dateFormatter.print(time);
    }

    public static String getTime(final Date date) {
        return timeFormatter.print(date.getTime());
    }

    public static LocalTime parseTime(final String time) {
        return timeFormatter.parseLocalTime(time);
    }

}
