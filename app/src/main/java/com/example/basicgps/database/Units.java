package com.example.basicgps.database;

public final class Units {
    public static enum Time {
        SECONDS,
        MINUTES,
        HOURS,
        DAYS
    }

    public static enum Distance {
        METERS,
        KILOMETERS,
        MILES,
        FEET
    }

    public static enum Speed {
        METERS_PER_SECOND,
        KILOMETERS_PER_HOUR,
        MILES_PER_HOUR,
        MILES_PER_MINUTE
    }
}
