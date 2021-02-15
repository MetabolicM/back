package com.example.marinin.back.controllers;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.marinin.back.models.LoginRequest;
import com.example.marinin.back.models.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

@CrossOrigin(origins = "http://localhost:8080/", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
/*
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;*/

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 4800, allowCredentials = "true")
    @PostMapping
    //public String authenticateUser(@Valid @RequestBody LoginRequest login) {
    public ResponseEntity<LoginResponse> authenticateUser( @RequestBody LoginRequest login) {
        System.out.println("controller start!!!!!!!!!!!!!!!!!!!!");
        System.out.println(login.getUsername() + "  " + login.getPassword());
        System.out.println(login);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        System.out.println("---------------" + jwt);

        User user = (User) authentication.getPrincipal();

        String accessType = "user";
        Set<Role> setRole = user.getRoles();

        for (Role role: setRole) {
            String str = role.getRole();
            if (str.equals("ROLE_ADMIN")){
                accessType = "admin";
            }
        }

         return new ResponseEntity<>(new LoginResponse(jwt, accessType), HttpStatus.OK);
        //return  ResponseEntity.ok("="+jwt);
    }

    @GetMapping("/access")
    public ResponseEntity<String> getCurrentRole() {
        String maxRole = null;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Role> setRole = user.getRoles();

        for (Role role: setRole) {
            if (role.getRole().equals("ROLE_ADMIN")){
                maxRole = "admin";
                break;
            }
            maxRole = "user";
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<>(maxRole, HttpStatus.OK);
        return responseEntity;
    }


/*
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }*/
}
