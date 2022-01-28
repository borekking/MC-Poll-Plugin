package de.borekking.mcPolls.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JavaUtils {

    public static String[] toArray(List<String> l) {
        return l.toArray(new String[0]);
    }

    public static long secsToMillis(int secs) {
        return secs * 1_000L;
    }

    public static int millisToSecs(long millis) {
        return (int) (millis / 1_000);
    }

    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <T> T[] removeFront(T[] arr, int l) {
        return Arrays.copyOfRange(arr, l, arr.length);
    }

    public static long getLongSave(Object o) {
        return ((Number) o).longValue();
    }

    // Methode for creating String of Date using a given formatter
    public static String getDateAsString(Date date, DateTimeFormatter formatter) {
        // Get Instant
        Instant instant = date.toInstant();
        // Convert instant to a LocalDateTime object
        LocalDateTime ldt = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
        // Get formatted String of Date using DateTimeFormatter
        return ldt.format(formatter);
    }
}
