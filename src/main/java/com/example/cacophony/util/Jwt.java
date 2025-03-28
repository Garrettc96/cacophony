package com.example.cacophony.util;

import java.util.Date;

public class Jwt {
    public static java.util.Date getExpiration() {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365);
    }

    public static java.util.Date getIssuedAt() {
        return new Date();
    }

}
