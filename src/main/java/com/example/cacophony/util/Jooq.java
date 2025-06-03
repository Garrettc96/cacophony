package com.example.cacophony.util;

import java.util.Optional;

import org.jooq.Record1;

public class Jooq {

    public static <T> Optional<T> optional(Record1<T> record) {
        return record != null ? Optional.of(record.value1()) : Optional.empty();
    }

}
