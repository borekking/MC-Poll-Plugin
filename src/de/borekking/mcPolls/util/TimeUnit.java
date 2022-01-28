package de.borekking.mcPolls.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public enum TimeUnit {

    SECOND(1, "Seconds", "s"),
    MINUTE(60, "Minutes", "min"),
    HOUR(MINUTE.seconds * 60, "Hours", "h"),
    DAY(HOUR.seconds * 24, "Day", "d"),
    WEEK(DAY.seconds * 7, "Weeks", "w");

    private static final Comparator<TimeUnit> reverseComparator = (x, y) -> Long.compare(y.seconds, x.seconds);

    private final int seconds;

    private final String name, shortName;

    TimeUnit(int seconds, String name, String shortName) {
        this.seconds = seconds;
        this.name = name;
        this.shortName = shortName;
    }

    // Return List of TimeUnits from biggest to lowest
    public static List<TimeUnit> getTimeUnitsSorted() {
        List<TimeUnit> list = new ArrayList<>(Arrays.asList(TimeUnit.values()));
        list.sort(reverseComparator);
        return list;
    }

    // Get Units from seconds
    public static Map<TimeUnit, Integer> asTimeUnits(long seconds) {
        Map<TimeUnit, Integer> timeUnits = new HashMap<>();

        for (TimeUnit timeUnit : getTimeUnitsSorted()) {
            if (seconds == 0) break;

            int counter = 0;
            while (seconds >= timeUnit.seconds) {
                seconds-=timeUnit.seconds;
                counter++;
            }

            if (counter > 0) timeUnits.put(timeUnit, counter);
        }

        return timeUnits;
    }

    public static int getSeconds(Map<TimeUnit, Integer> timeUnits) {
        return timeUnits.entrySet().stream().mapToInt(entry -> entry.getKey().getSeconds() * entry.getValue()).sum();
    }

    public static String toString(long seconds) {
        return toString(TimeUnit.asTimeUnits(seconds));
    }

    public static String toString(Map<TimeUnit, Integer> timeUnits) {
        return toString(timeUnits, true, true);
    }

    /**
     *
     * @param timeUnits Map of TimeUnits point to an Integer representing their amount.
     * @param sort Boolean; If the TimeUni-Map should be sorted by Seconds
     * @param ignoreEmpty Boolean; If TimeUnits in map that have an amount of zero should be ignored in the returned String.
     * @return A String containing the provided TimeUnits names and amounts separated by commas.
     */
    public static String toString(Map<TimeUnit, Integer> timeUnits, boolean sort, boolean ignoreEmpty) {
        // Return "/" on empty map
        if (timeUnits.isEmpty()) return "/";

        StringJoiner joiner = new StringJoiner(", ");

        // Sort Keys if wanted
        List<TimeUnit> keys = sort ? timeUnits.keySet().stream().sorted(reverseComparator).collect(Collectors.toList()) : new ArrayList<>(timeUnits.keySet());

        // Go through keys and get the belonging amount in Map
        for (TimeUnit timeUnit : keys) {
            int amount = timeUnits.get(timeUnit);

            // If ignoreEmpty is true and amount actually is 0 skip current.
            if (ignoreEmpty && amount == 0) continue;

            // Finally, add the amount and TimeUnit's shortName
            joiner.add(amount + timeUnit.shortName);
        }

        return joiner.toString();
    }

    public String getName() {
        return name;
    }

    public int getSeconds() {
        return seconds;
    }

    public long getMillis() {
        return JavaUtils.secsToMillis(this.seconds);
    }
}
