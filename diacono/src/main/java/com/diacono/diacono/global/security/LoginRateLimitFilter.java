package com.diacono.diacono.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoginRateLimitFilter.class);
    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_MS = 60_000L;
    private static final String LOGIN_PATH = "/api/v1/auth/login";

    private final ConcurrentHashMap<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (LOGIN_PATH.equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String ip = resolveClientIp(request);
            long now = System.currentTimeMillis();
            long windowStart = now - WINDOW_MS;

            requestLog.compute(ip, (k, v) -> {
                if (v == null) v = new ArrayDeque<>();
                while (!v.isEmpty() && v.peekFirst() < windowStart) v.pollFirst();
                v.addLast(now);
                return v;
            });

            int count = requestLog.get(ip).size();
            if (count > MAX_REQUESTS) {
                logger.warn("Rate limit atingido para IP={} ({} tentativas em 60s)", ip, count);
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"Muitas tentativas de login. Aguarde 1 minuto.\",\"status\":429}");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
