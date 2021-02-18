package com.example.marinin.back.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.marinin.back.security.service.UserSecurity;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserSecurity userSecurity;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ((request.getMethod().equals("POST")) && (request.getRequestURI().equals("/auth"))) {
            filterChain.doFilter(request, response);
        } else {
            try {
                String jwt = parseJwt(request);
                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    UserDetails userDetails = userSecurity.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);//???
                }
            } catch (Exception e) {
                response.sendError(401);//???
            }
        }
    }

    private String parseJwt(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}