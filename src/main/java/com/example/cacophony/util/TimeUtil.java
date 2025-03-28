package com.example.cacophony.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimeUtil {
    public static OffsetDateTime epochToTimestamp(long epoch) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }
}
