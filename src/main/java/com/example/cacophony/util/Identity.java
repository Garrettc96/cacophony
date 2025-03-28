package com.example.cacophony.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.cacophony.security.UserInfoDetails;

public class Identity {
    
    public static String getUserId() {
        return ((UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).userId.toString();
    }
}
