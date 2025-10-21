package com.cop.common.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestContextFilter extends OncePerRequestFilter {

    public static final ThreadLocal<String> currentRequestPath = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            currentRequestPath.set(request.getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            currentRequestPath.remove(); // 꼭 해제!
        }
    }

    public static String getCurrentRequestPath() {
        return currentRequestPath.get();
    }
}