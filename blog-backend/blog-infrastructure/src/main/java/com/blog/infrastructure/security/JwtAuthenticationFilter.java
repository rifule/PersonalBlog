package com.blog.infrastructure.security;

import com.blog.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 放行登录接口和公开接口
        return path.equals("/api/auth/login") ||
               path.startsWith("/doc.html") ||
               path.startsWith("/webjars/") ||
               path.startsWith("/swagger-resources/") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/favicon.ico") ||
               path.equals("/api/articles") ||
               path.startsWith("/api/articles/") ||
               path.equals("/api/categories") ||
               path.startsWith("/api/tags/") ||
               path.startsWith("/api/comments/") ||
               path.startsWith("/uploads/");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = JwtUtil.getUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.error("JWT Token validation failed", e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            // 解析 token，如果过期会抛出异常
            var decodedJWT = JwtUtil.parseToken(jwt);
            String username = decodedJWT.getClaim("username").asString();
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            logger.error("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
