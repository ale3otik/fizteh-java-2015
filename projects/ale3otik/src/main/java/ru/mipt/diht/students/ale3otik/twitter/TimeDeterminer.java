package ru.mipt.diht.students.ale3otik.twitter;

import java.time.LocalTime;
import java.util.Date;

/**
 * Created by alex on 29.09.15.
 */
public class TimeDeterminer {
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int MILISECONDS_IN_SECONDS = 1000;

    public static final String getTimeDifference(Date createdAt) {
        final FormDeclenser formDeclenser = new FormDeclenser();

        long localTime = System.currentTimeMillis();
        long createdTime = createdAt.getTime();
        long todaySeconds = LocalTime.now().getSecond();

        long secondDifference = (localTime - createdTime) / MILISECONDS_IN_SECONDS;
        if (secondDifference / SECONDS_IN_MINUTE < 2) {
            return "Только что";
        }

        long minutDifference = (localTime - createdTime)
                / (MILISECONDS_IN_SECONDS * SECONDS_IN_MINUTE);

        if (minutDifference / MINUTES_IN_HOUR < 1) {
            return minutDifference + " "
                    + formDeclenser.getMinutsDeclension(minutDifference)
                    + " назад";
        }

        long hoursDifference = (localTime - createdTime) / MINUTES_IN_HOUR;

        if (secondDifference < todaySeconds) {
            return hoursDifference + " "
                    + formDeclenser.getHoursDeclension(hoursDifference)
                    + " назад";
        }

        long secondsSinceYesterday = secondDifference - todaySeconds;

        long daysDifference = secondsSinceYesterday
                / (SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY) + 1;

        if (daysDifference < 2) {
            return "вчера";
        }

        return (daysDifference) + " "
                + formDeclenser.getDaysDeclension(daysDifference)
                + " назад";

    }
}