package net.csongradyp.badger.provider.date;

import java.util.Date;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Named
public class DateProvider {

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    public DateProvider() {
        dateFormatter = DateTimeFormat.forPattern("MM-dd");
        timeFormatter = DateTimeFormat.forPattern("HH:mm");
    }

    public String currentDateString() {
        return format(new Date().getTime());
    }

    public Date currentDate() {
        return new DateTime().toLocalDate().toDate();
    }

    public String currentTimeString() {
        return getTime(new Date());
    }

    public Date currentTime() {
        return new Date();
    }

    public String getDate(final Date date) {
        return format(date.getTime());
    }

    public Date parseDate(final String date) {
        return dateFormatter.parseLocalDate(date).toDate();
    }

    protected String format(final Long time) {
        return dateFormatter.print(time);
    }

    public String getTime(final Date date) {
        return timeFormatter.print(date.getTime());
    }

    public LocalTime parseTime(final String time) {
        return timeFormatter.parseLocalTime(time);
    }

}
