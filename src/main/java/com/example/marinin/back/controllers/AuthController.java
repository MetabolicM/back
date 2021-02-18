package com.example.marinin.back.controllers;

import java.util.Set;

import com.example.marinin.back.models.LoginRequest;
import com.example.marinin.back.models.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.marinin.back.models.Role;
import com.example.marinin.back.models.User;
import com.example.marinin.back.security.jwt.JwtUtils;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 4800, allowCredentials = "true")
    @PostMapping
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest login) {

        Authentication authentication;
        try{
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        }catch (Exception e){
            return new ResponseEntity<>(new LoginResponse(null, null), HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();

        String accessType = "user";
        Set<Role> setRole = user.getRoles();

        for (Role role : setRole) {
            String str = role.getRole();
            if (str.equals("ROLE_ADMIN")) {
                accessType = "admin";
            }
        }

        return new ResponseEntity<>(new LoginResponse(jwt, accessType), HttpStatus.OK);
    }
}
