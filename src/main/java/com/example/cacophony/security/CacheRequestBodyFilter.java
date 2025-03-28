package com.example.cacophony.security;

import java.io.IOException;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CacheRequestBodyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ServletRequestCachedBody wrapper = new ServletRequestCachedBody(request);
        filterChain.doFilter(wrapper, response);
    }
    
}
