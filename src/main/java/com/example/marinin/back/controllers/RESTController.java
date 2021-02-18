package com.example.marinin.back.controllers;

import com.example.marinin.back.models.Role;
import com.example.marinin.back.models.User;
import com.example.marinin.back.service.service.RoleService;
import com.example.marinin.back.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 4800, allowCredentials = "true")
@Controller
@RestController
public class RESTController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/rest/current")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<User> getCurrent() {
        return new ResponseEntity<>((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), HttpStatus.OK);
    }

    @GetMapping("/rest/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//??
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/rest/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//??
    public ResponseEntity<Set<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getSetOfAllRoles(), HttpStatus.OK);
    }

    @PostMapping("/rest")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> newUser(@RequestBody User user) {
        userService.save(user);
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/rest/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PatchMapping("rest/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> edit(@RequestBody User user) {
        userService.update(user);
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
