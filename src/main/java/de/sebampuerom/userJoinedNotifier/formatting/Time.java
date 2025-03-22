package de.sebampuerom.userJoinedNotifier.formatting;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static String formatTime(ZonedDateTime zonedDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

}
