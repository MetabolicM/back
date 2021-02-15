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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.marinin.back.security.service.UserSecurity;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserSecurity userSecurity;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Filter START");
        System.out.println(request.getMethod());
        if ((request.getMethod().equals("POST")) && (request.getRequestURI().equals("/auth"))) {
            System.out.println("START LOGINING ///////////////////////////////////");
            filterChain.doFilter(request, response);
        } else {
            try {
                System.out.println("FILTER START--");
                String jwt = parseJwt(request);
                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    System.out.println("JWT IS VALIDATE, all ok");
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    System.out.println("JWT: "+jwt);
                    System.out.println("USER NAME: "+username);
                    UserDetails userDetails = userSecurity.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                }else {
                    System.out.println("JWT == null or not validate");
                }
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e);
            }
        }

    }

    private String parseJwt(HttpServletRequest request) {
        System.out.println("PARSE JWT START");
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("COOKIE IS NOT NULL");
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }else{
            System.out.println("COOKIE IS  NULL!!!!!!!!!!");
        }
        return token;
    }

}